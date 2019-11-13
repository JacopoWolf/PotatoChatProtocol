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


/**
 *
 * @author Jacopo_Wolf
 */
public class PCPManager implements IPCPManager
{

    private LinkedList<IPCPLogicCore> cores = new LinkedList<>();
    
    // sockets are mapped on the core they are being executed on.
    private HashMap<IPCPChannel,IPCPLogicCore> channelsExecutionMap = new HashMap<>();
    
    private HashMap<PCP.Versions,HashSet<IPCPData>> incompleteSetsMap = new HashMap<>();
    
    
    int DefaultQueueMaxLenght = 512;
    
    // once a core's queue is full, this'll wait until the core's queue lenght is lower than this value
    // before enqueuing new data.
    int defaultCoreThreshold = 214;
    
    // time in milliseconds to wait before killing an empty logicore.
    int killThresholdMilliseconds = 10000;

    
//<editor-fold defaultstate="collapsed" desc="getters and setters">
    public int getQueueMaxLenghtDefault()
    {
        return DefaultQueueMaxLenght;
    }
    
    public void setQueueMaxLenghtDefault( int queueMaxLenghtDefault )
    {
        this.DefaultQueueMaxLenght = queueMaxLenghtDefault;
    }
    
    public int getCoreThreshold()
    {
        return defaultCoreThreshold;
    }
    
    public void setCoreThreshold( int coreThreshold )
    {
        this.defaultCoreThreshold = coreThreshold;
    }
    
    public int getKillThresholdMilliseconds()
    {
        return killThresholdMilliseconds;
    }
    
    public void setKillThresholdMilliseconds( int killThresholdMilliseconds )
    {
        this.killThresholdMilliseconds = killThresholdMilliseconds;
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
    
    
    
    public PCPManager() { }
    
    
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
            // TODO: set interpreter's common incomplete list
            
        // run the logicore on a new thread
        Thread thr = new Thread( core );
        synchronized (cores)
            { this.cores.add( core ); }
        thr.start();
        
        return core;
    }
    

    /**
     * safely kills a core 
     * @param toKill 
     */
    void killCore ( IPCPLogicCore toKill )
    {
        throw new UnsupportedOperationException();
    }
    
    void requeueAll( Collection<byte[]> bytes )
    {
        
    }
    

    @Override
    public void cleanCache()
    {
        throw new UnsupportedOperationException();
    }
    
    
    
    
    @Override
    public void accept( byte[] data, IPCPChannel from )
    {
        //TODO: implement mechanism to direct channel data to preferred logicore
        
        // checks if the recieved data comes from a new connection
        boolean isNew = !this.channelsExecutionMap.containsKey(from);
        PCP.Versions version = null;
        
            switch (data[1])
            {
                case 0:
                    version = PCP.Versions.Min;
                    if (isNew)
                        try
                        {
                            throw new PCPException( ErrorCode.ServerExploded );
                            
                            //TODO: run through a temporary interpreter in another socket
                            //TODO: assign values to the new socket

                        }
                        catch ( PCPException e )
                        {
                            try
                            {
                                send(new ErrorMsg(e), from );
                            }
                            catch(Exception e1)
                            {
                                //TODO: log error
                            }
                            
                            close( from );
                            return;
                        }
            }
            
        IPCPLogicCore core = 
            getCoreByVersion(version);
            core.enqueue(data);
        channelsExecutionMap.put(from, core);
        
            
        return;
        
    }

 
    

    @Override
    public void send( IPCPData data, String destination ) throws IOException
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
    public void sendBroadcast( IPCPData data, Collection<String> destinations ) throws IOException
    {
        for (String str : destinations)
            send(data, str);
    }
    
    @Override
    public synchronized void send( IPCPData data, IPCPChannel destination ) throws IOException
    {
        //TODO: put this on an executorService
        destination.send(data.toBytes());
    }
    
    
    
    
    @Override
    public void close( String alias )
    {
        close
        (
            this.getChannels()
                .stream()
                .filter( pcps -> pcps.getUserInfo().getAlias().equals(alias) )
                .findFirst()
                .get()
        );
    }

    @Override
    public synchronized void close( IPCPChannel pcpchannel )
    {
        this.channelsExecutionMap.remove(pcpchannel);
        
        try
        {
            pcpchannel.getChannel().close();
        }
        catch (IOException ioe)
        {
            //TODO: log
        }
        
    }
    
    
    
}
