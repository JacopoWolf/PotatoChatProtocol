/*
 * this is a school project under "The Unlicence".
 */
package PCP.net;

import PCP.Min.data.*;
import PCP.*;
import PCP.PCPException.ErrorCode;
import PCP.data.*;
import PCP.logic.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;


/**
 *
 * @author Jacopo_Wolf
 */
public class PCPManager implements IPCPManager
{
    
    /**
     * list of all cores
     */
    private final LinkedList<IPCPLogicCore> cores = new LinkedList<>();
    
    /* 
     * sockets are mapped on the core they are being executed on.
     */
    private final HashMap<IPCPChannel,IPCPLogicCore> channelsExecutionMap = new HashMap<>();
    
    /**
     * list of incomleted datas by protocol version
     */
    private final HashMap<PCP.Versions,HashMap<IPCPData,Integer>> incompleteSetsMap = new HashMap<>();
    
    
    
    /**
     * used to queue data sending operation
     */
    private final ExecutorService 
            sendingExecutor             = Executors.newSingleThreadExecutor(),
            initialRegistrationHandler  = Executors.newSingleThreadExecutor();
    
    /**
     * calls clean every scheduled time
     */
    private ScheduledExecutorService cleanService;
    
    
    
    
    //<editor-fold defaultstate="collapsed" desc="default values">
    
    private int DefaultQueueMaxLenght = 512;
    
    // once a core's queue is full, this'll wait until the core's queue lenght is lower than this value
    // before enqueuing new data.
    private int defaultCoreThreshold = 214;
    
    // time in milliseconds to wait before killing an empty logicore.
    private int killThresholdMillis = 10000;
    
    // time after wich the cache will be cleaned
    private int cacheCleaningTimerMillis = 10000;
    
    private int DefaultkeepAlive = 90000;
    
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="getters and setters">

    
    
    
    public int getDefaultQueueMaxLenght()
    {
        return DefaultQueueMaxLenght;
    }

    public void setDefaultQueueMaxLenght( int DefaultQueueMaxLenght )
    {
        this.DefaultQueueMaxLenght = DefaultQueueMaxLenght;
    }

    public int getDefaultCoreThreshold()
    {
        return defaultCoreThreshold;
    }

    public void setDefaultCoreThreshold( int defaultCoreThreshold )
    {
        this.defaultCoreThreshold = defaultCoreThreshold;
    }

    public int getKillThresholdMillis()
    {
        return killThresholdMillis;
    }

    public void setKillThresholdMillis( int killThresholdMillis )
    {
        this.killThresholdMillis = killThresholdMillis;
    }

    public int getCacheCleaningTimerMillis()
    {
        return cacheCleaningTimerMillis;
    }

    /**
     * sets the new cache cleaning time re-initializing the cleaner service. 
     * @param cacheCleaningTimerMillis 
     */
    public void setCacheCleaningTimerMillis( int cacheCleaningTimerMillis )
    {
        this.cacheCleaningTimerMillis = cacheCleaningTimerMillis;
        if (this.cleanService != null)
            this.cleanService.shutdownNow().clear();
        
        this.cleanService = Executors.newScheduledThreadPool(1);
            // schedules cache cleaner
            cleanService.scheduleWithFixedDelay
            ( 
                () -> this.clearCache() , 
                cacheCleaningTimerMillis, // initial time wait
                cacheCleaningTimerMillis, // delay
                TimeUnit.MILLISECONDS
            );
    }

    public int getDefaultkeepAlive()
    {
        return DefaultkeepAlive;
    }

    public void setDefaultkeepAlive( int DefaultkeepAlive )
    {
        this.DefaultkeepAlive = DefaultkeepAlive;
    }
    
    
    
    
    @Override
    public List<IPCPLogicCore> getCores()
    {
        return this.cores;
    }
    
    @Override
    public Set<IPCPChannel> getChannels()
    {
        return this.channelsExecutionMap.keySet();
    }
    
//</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="constructors">
    
    /**
     * default constructor
     */
    public PCPManager()
    {
        // initialized cleaning daemon service
        this.setCacheCleaningTimerMillis(cacheCleaningTimerMillis);

    }
    
    //</editor-fold>
  
    
    @Override
    public synchronized void accept( byte[] data, IPCPChannel from )
    {

        PCP.Versions version = null;
        
        // checks if the recieved data comes from a new connection
        if (!this.channelsExecutionMap.containsKey(from))
            try
            {
                throw new PCPException( ErrorCode.ServerExploded );

                //todo assign values to the new socket
                // write a method queuing on initialRegistrationHandler
                // version = NewConnectionsInterpreter;

            }
            catch ( PCPException e )
            {
                try
                {
                    Logger.getGlobal().log
                    (
                        Level.WARNING, 
                            "Error while recieving a new connection from {0}, reason {1}", 
                            new Object[]{from.getChannel().getRemoteAddress().toString(), e.getErrorCode().toString()}
                    );
                    // always close the connection at this point. An error in the initial registration packet is irreversable.
                    close( from, new ErrorMsg(e) ); 
                }
                catch(Exception exc) { Logger.getGlobal().log(Level.SEVERE, exc.getMessage(), exc); }
                
                return;
            }
            
        else
            version = from.getUserInfo().getVersion();
        
        // queues data
        
        // checks for preferred logicCore
        if ( channelsExecutionMap.containsKey(from) )
        {
            IPCPLogicCore core = channelsExecutionMap.get(from);
            if ( core.canAccept() ) 
            {
                core.enqueue(data);
                return;
            }
        }
            
        // if the preferred core is not available then map on a new one
        IPCPLogicCore core = getCoreByVersion(version);
            core.enqueue(data);
        channelsExecutionMap.put(from, core);
        return;
        
    }

    
    
    
    
    
    //<editor-fold defaultstate="collapsed" desc="LogicCores operations">
    /**
     * optimized core getter.
     * @param version
     * @return the reference to the next core. If every core is saturated, instantiates a new one.
     */
    public IPCPLogicCore getCoreByVersion( PCP.Versions version )
    {
        return cores
                .stream()
                .filter( core -> core.getVersion() == version && core.canAccept() )
                .findFirst()
                .orElse( initLogicCore(version) );
    }
    
    
    
    @Override
    public IPCPLogicCore initLogicCore( PCP.Versions version )
    {
        // initializes the new core
        IPCPLogicCore core = PCP.getLogicCore_ByVersion(version);
        core.setManager(this);
        core.setMaxQueueLenght(DefaultQueueMaxLenght);
        core.setThreshold(defaultCoreThreshold);
        core.getInterpreter().setIncompleteDataList(this.incompleteSetsMap.get(version).keySet());
        
        // run the logicore on a new thread
        Thread thr = new Thread( core );
        synchronized (cores)
        { this.cores.add( core ); }
        thr.start();
        
        
        Logger.getGlobal().log(Level.INFO, "Initialized new logic core, version {0}", version.toString());
        return core;
    }
    
    
    /**
     * kills a core.
     * WARNING: All data relative to the core and the queued data is ereased.
     * @param toKill the logic core to kill
     */
    void killCore ( IPCPLogicCore toKill )
    {
        toKill.stop();
        toKill.getQueue().clear();
        
        synchronized (this)
        {
            this.cores
                    .remove(toKill);
            this.channelsExecutionMap
                    .values()
                    .removeIf(core -> core.equals(toKill));
        }
    }
//</editor-fold>
    

    @Override
    public void clearCache()
    {
        synchronized ( incompleteSetsMap )
        {
            this.incompleteSetsMap.values()
            .forEach
            (
                (HashMap<IPCPData,Integer> map) ->
                    map.forEach
                    (
                        ( IPCPData data, Integer i ) ->
                        {
                            if ( i < 0 )
                                map.remove(data);
                            else
                                map.put(data, i-cacheCleaningTimerMillis);
                        }
                    )
                    
            );
        }
        
        synchronized (cores)
        {
            this.cores
                .forEach
                ( 
                    core -> 
                    {
                        if (!core.keepAlive() && core.getQueue().size() == 0 )
                            cores.remove(core);
                    }   
                );
        }
        
        synchronized ( channelsExecutionMap )
        {
            this.getChannels()
            .forEach
            (
                channel ->
                {
                    int time = channel.getTimeLeftAwake() - cacheCleaningTimerMillis;
                    if ( time > 0 )
                        channel.setTimeLeftAwake( time );
                    else
                    {
                        send(new Disconnection(Disconnection.Reason.timedOut), channel );
                        channelsExecutionMap.remove(channel);
                    }
                    
                }
            );
        }
    }
    

    
    //<editor-fold defaultstate="collapsed" desc="I/O operations">
    
    @Override
    public void send( IPCPData data, String destination )
    {
        // finds the respectinve socket and then calls method below.
        this.send
                (data,
                        this.getChannels()
                                .stream()
                                .filter( pcps -> pcps.getUserInfo().getAlias().equals(destination) )
                                .findFirst()
                                .get()
                );
    }
    
    @Override
    public void sendBroadcast( IPCPData data, Collection<String> destinations )
    {
        for (String str : destinations)
            send(data, str);
    }
    
    @Override
    public void send( final IPCPData data, final IPCPChannel destination )
    {
        sendingExecutor.submit( () -> destination.send(data.toBytes()) );
    }
    
    
    
    
    @Override
    public void close( String alias,  IPCPData with  )
    {
        close
            (
                this.getChannels()
                        .stream()
                        .filter( pcps -> pcps.getUserInfo().getAlias().equals(alias) )
                        .findFirst()
                        .get(),
                with
            );
    }
    
    @Override
    public void close( IPCPChannel pcpchannel, IPCPData with )
    {
        this.channelsExecutionMap.remove(pcpchannel);
        
        try
        {
            sendingExecutor.submit( () -> pcpchannel.send(with.toBytes()) ).get();
        }
        catch( InterruptedException | ExecutionException ex )
        {
            Logger.getGlobal().log( Level.WARNING, "Error while closing a connection:\n", ex );
        }
        finally
        {
            try
            {
                pcpchannel.getChannel().close();
            }
            catch ( IOException ioe )
            {
                Logger.getGlobal().log( Level.WARNING, "Impossible to close channel:\n", ioe );
            }
        }
    }
//</editor-fold>
    
      
}