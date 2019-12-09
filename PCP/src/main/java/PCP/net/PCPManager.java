/*
 * this is a school project under "The Unlicence".
 */
package PCP.net;

import PCP.Min.data.Disconnection.Reason;
import PCP.Min.data.*;
import PCP.*;
import PCP.Min.data.*;
import PCP.PCPException.ErrorCode;
import PCP.data.*;
import PCP.logic.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;
import java.util.stream.*;
import org.javatuples.*;


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
     * manages all possible IDS
     */
    private final IDmanager<byte[]> idmanager; // initialized in constructor
    
    
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
    private int cacheCleaningTimerMillis = 60000;
    
    private int DefaultkeepAlive = 90000;
    
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="getters and setters">

    @Override
    public IDmanager<byte[]> getIdmanager()
    {
        return idmanager;
    }
    
    
    
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
    public Collection<IPCPUserInfo> allConnectedUsers()
    {
        return getChannels()
                .stream()
                .map( IPCPChannel::getUserInfo )
                .collect( Collectors.toList() );
                
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
    
    
    //<editor-fold defaultstate="collapsed" desc="constructor and disposer">
    
    /**
     * default constructor
     */
    public PCPManager()
    {
        // initialized cleaning daemon service
        this.setCacheCleaningTimerMillis(cacheCleaningTimerMillis);
        this.idmanager = new IDmanager<>
        (
            ( Set<byte[]> set) -> 
            {
                byte[] newkey = new byte[2];
                Random rnd = new Random();
                do
                {
                    rnd.nextBytes(newkey);
                }
                while( set.contains(newkey) );
                
                set.add(newkey);
                return newkey;
            }
        );
    }
    
    private boolean isDisposed = false;
    @Override
    public void dispose()
    {   
        if ( !isDisposed )
        {
            // terminates all connections
            synchronized ( this.channelsExecutionMap )
            {
                // this way it doesn't throw a ConcurrentModificationException
                while ( this.getChannels().size() > 0 )
                    this.close(this.getChannels().iterator().next(), new  Disconnection(Reason.goneOffline) );
            }
            
            // tells the executor to execute all pending tasks
            this.sendingExecutor.shutdown(); 

            // closes cache cleaning daemon
            this.cleanService.shutdownNow();
            
            // clears big-data lists
            incompleteSetsMap.clear();
            channelsExecutionMap.clear();

            // closes all logicores
            this.initialRegistrationHandler.shutdownNow();
            
            synchronized (this.cores)
            {
                while (this.cores.size() > 0)
                    this.killCore(this.cores.getFirst());
            }


            // awaits 1 minute for every disconnection data to be sent
            try
            {
                this.sendingExecutor.awaitTermination(60, TimeUnit.SECONDS);
                Logger.getGlobal().log(Level.INFO, "middleware resources disposing completed succesfully!");
            }
            catch( InterruptedException ex )
            {
                Logger.getGlobal().log(Level.SEVERE, "middleware failed to notify shutdown to clients", ex);
            }
            isDisposed = true;
            
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        try
        {
            this.dispose();
        }
        finally
        {
            super.finalize();
        }
    }
    
    
    
    //</editor-fold>
  
    
    @Override
    public synchronized void accept( byte[] data, IPCPChannel from )
    {   
        try
        {
            Logger.getGlobal().log
            ( 
                Level.FINEST,
                "from recieved raw from {0}:\n{1}", 
                new Object[] { from.getChannel().getRemoteAddress().toString(), Arrays.toString(data)} 
            );
        }catch(IOException e){}
        
        
        IPCPLogicCore core;
        
        // checks if the recieved data comes from a new connection
        if ( channelsExecutionMap.containsKey(from) )
        {
            // NOT a new connection 
            
            // checks for preferred logicCore:
            // getCore verifies the core is able to accept the connection,
            // otherwise it initialized a new LogicCore
            core = channelsExecutionMap.get(from);
            if ( core == null )
                core = getCoreByVersion(from.getUserInfo().getVersion()); 
            
            channelsExecutionMap.put(from, core );
            
        }
        else
        {
            // a new connection
            
            PCP.Versions version = PCP.versionByVersionCode( data[1] );
            if ( version == null )
                close(from, new ErrorMsg(ErrorCode.PackageMalformed));
            
            core = getCoreByVersion( version );
            if ( core == null )
                close(from, new ErrorMsg(ErrorCode.PackageMalformed));
            
            channelsExecutionMap.put(from, core);
        }

        // enqueues the data to elaborate
        core.enqueue(Pair.with(data, from));

        
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
        IPCPLogicCore core = PCP.logicCoreByVersion(version);
            core.setManager(this);
            core.setMaxQueueLenght(DefaultQueueMaxLenght);
            core.setThreshold(defaultCoreThreshold);
            if ( this.incompleteSetsMap.get(version) == null ) // inits incomplete sets map if necessary
                this.incompleteSetsMap.put(version, new HashMap<>());
            core.getInterpreter().setIncompleteDataList(this.incompleteSetsMap.get(version).keySet()); //todo implement pcpmanger managed incompletedatalist access methods
        
        // run the logicore on a new thread
        Thread thr = new Thread( core );
        this.cores.add(core);
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
        toKill.dispose();
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
        Logger.getGlobal().log(Level.INFO,"cleaning cache.");
        
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
                        if (!core.isKeepAlive() && core.getQueue().isEmpty() && !channelsExecutionMap.containsValue(core) )
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
                                .filter( pcps -> pcps.getUserInfo() != null )
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
        sendingExecutor.submit
        ( 
            () -> 
            {
                try
                { 
                    destination.send(data.toBytes());
                }
                catch ( IOException ioe )
                {
                    Logger.getGlobal().log(Level.WARNING, "error while sending data to {0}", destination.getUserInfo().getAlias());
                }
            } 
        );
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
        if (!this.channelsExecutionMap.containsKey(pcpchannel))
            return;
                
        Logger.getGlobal().log
        (
            Level.INFO, "user [ {0} , {1} ] gone offline", 
            new Object[]{pcpchannel.getUserInfo().getAlias(), Arrays.toString(pcpchannel.getUserInfo().getId())}
        );
        
        this.channelsExecutionMap.remove(pcpchannel);
        
        sendingExecutor.submit
        ( 
            () -> 
            {
                try
                {
                    if ( with != null )
                        pcpchannel.send(with.toBytes());

                    pcpchannel.getChannel().close();
                }
                catch( IOException ex )
                {
                    Logger.getGlobal().log( Level.WARNING, "Error while closing a connection:\n", ex );
                }
                finally
                {
                    // frees the acquired id
                    idmanager.freeID(pcpchannel.getUserInfo().getId());
                }
            }
        );
    }
//</editor-fold>
    
      
}