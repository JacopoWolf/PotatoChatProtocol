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
public class PCPManager implements IPCPmanager
{

    private LinkedList<IPCPLogicCore> cores = new LinkedList<>();
    
    // sockets are mapped on the core they are being executed on.
    private HashMap<IPCPSocket,IPCPLogicCore> sockets = new HashMap<>();
    
    
    int queueMaxLenghtDefault = 512;
    
    // once a core's queue is full, this'll wait until the core's queue lenght is lower than this value
    // before enqueuing new data.
    int coreThreshold = 214;
    
    // time in milliseconds to wait before killing an empty logicore.
    int killThresholdMilliseconds = 10000;

    
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

    
    
    
    public PCPManager() { }
    
    public PCPManager( int coreThreshold )
    {
        this.coreThreshold = coreThreshold;
    }    
    
    
    
    /**
     * optimized core getter.
     * @param version
     * @return the reference to the next core
     */
    public IPCPLogicCore geCoreByVersion( PCP.Versions version )
    {
        return cores
                .stream()
                .filter( core -> core.getVersion() == version )
                .findFirst()
                .orElse(initLogicCore(version));
    }
    
    
    
    @Override
    public IPCPLogicCore initLogicCore( PCP.Versions version )
    {
        IPCPLogicCore core = PCP.getLogicCore_ByVersion(version);
            core.setManager(this);
        
        Thread logcoreT = new Thread( core );
        
        synchronized (cores)
        {
            this.cores.add( core );
        }
        
        logcoreT.start();
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
        boolean isNew = !this.sockets.containsKey(from);
        PCP.Versions version = PCP.Versions.Min;
        
            switch (data[1])
            {
                case 0:
                    version = PCP.Versions.Min;
                    if (isNew)
                        try
                        {
                            throw new PCPException( ErrorCode.ServerExploded );
                            //TODO: run through a temporary interpreter in another socket

                        }
                        catch ( PCPException e )
                        {
                            send(new ErrorMsg(e), from );
                            close( from );
                            return;
                        }
            }
            
        // todo: complete
        geCoreByVersion(version).enqueue(data);
        return;
        
    }

 
    

    @Override
    public void send( IPCPdata data, String destination )
    {
        
    }

    @Override
    public void send( IPCPdata data, IPCPSocket destination )
    {
        
    }
    
    @Override
    public void sendBroadcast( IPCPdata data, Collection<String> destinations )
    {
        
    }
    
    
    @Override
    public void close( String alias )
    {
    }

    @Override
    public void close( IPCPSocket socket )
    {
    }
    
    
    
}
