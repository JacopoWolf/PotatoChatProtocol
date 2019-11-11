/*
 * this is a school project under "The Unlicence".
 */
package PCP.net;

import PCP.Min.data.*;
import PCP.*;
import PCP.PCPException.ErrorCode;
import PCP.data.*;
import PCP.logic.*;
import java.util.*;


/**
 *
 * @author Jacopo_Wolf
 */
public class PCPManager implements IPCPManager
{

    private LinkedList<IPCPLogicCore> cores = new LinkedList<>();
    
    // sockets are mapped on the core they are being executed on.
    private HashMap<IPCPSocket,IPCPLogicCore> sockets = new HashMap<>();
    
    
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
    public Set<IPCPSocket> getSockets()
    {
        return this.sockets.keySet();
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
        
        // rund the logicore on a new thread
        Thread thr = new Thread( core );
        synchronized (cores)
            { this.cores.add( core ); }
        thr.start();
        
        return core;
    }
    
    
    @Override
    public void cleanCache()
    {
        throw new UnsupportedOperationException();
    }
    
    
    
    
    @Override
    public void recieve( byte[] data, IPCPSocket from )
    {
        // checks if the recieved data comes from a new connection
        boolean isNew = !this.sockets.containsKey(from);
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
                            send(new ErrorMsg(e), from );
                            close( from );
                            return;
                        }
            }
            
        // todo: complete
        getCoreByVersion(version).enqueue(data);
            
        return;
        
    }

 
    

    @Override
    public void send( IPCPdata data, String destination )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void send( IPCPdata data, IPCPSocket destination )
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void sendBroadcast( IPCPdata data, Collection<String> destinations )
    {
        throw new UnsupportedOperationException();
    }
    
    
    @Override
    public void close( String alias )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close( IPCPSocket socket )
    {
        throw new UnsupportedOperationException();
    }
    
    
    
}
