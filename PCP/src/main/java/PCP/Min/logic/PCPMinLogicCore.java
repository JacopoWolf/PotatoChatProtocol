/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.logic;

import PCP.*;
import PCP.logic.*;
import PCP.net.*;
import java.util.*;
import java.util.concurrent.*;


public class PCPMinLogicCore implements IPCPLogicCore
{
    private boolean keepAliveFlag = false;
    
    // main components
    private IPCPManager manager;
    private PCPMinCore core;
    private PCPMinInterpreter interpreter;
    
    private LinkedBlockingQueue<byte[]> queue;
    private int maxQueueLenght;
    private int threshold;
    private boolean waitForThreshold;
    
    //<editor-fold defaultstate="collapsed" desc="getters and setters">
    @Override
    public IPCPCore getCore()
    {
        return this.core;
    }
    
    @Override
    public IPCPInterpreter getInterpreter()
    {
        return this.interpreter;
    }
    
    @Override
    public Queue<byte[]> getQueue()
    {
        return this.queue;
    }
    
    @Override
    public IPCPManager getManager()
    {
        return this.manager;
    }
    
    @Override
    public void setManager( IPCPManager manager )
    {
        this.manager = manager;
    }
    
    
    @Override
    public int getMaxQueueLenght()
    {
        return this.maxQueueLenght;
    }
    
    @Override
    public void setMaxQueueLenght( int lenght )
    {
        this.maxQueueLenght = lenght;
    }
    
    @Override
    public int getThreshold()
    {
        return this.threshold;
    }
    
    @Override
    public void setThreshold( int theshold )
    {
        this.threshold = theshold;
    }
    
    @Override
    public boolean isKeepAlive()
    {
        return this.keepAliveFlag;
    }

    @Override
    public void setKeepAlive( boolean keepAlive )
    {
        this.keepAliveFlag = keepAlive;
    }
    
    
    
    @Override
    public PCP.Versions getVersion()
    {
        return PCP.Versions.Min;
    }
//</editor-fold>

    
    //<editor-fold defaultstate="collapsed" desc="constructor and disposer">

    public PCPMinLogicCore() { }
    
    private boolean disposed = false;
    @Override
    public void dispose()
    {
        if ( !disposed )
        {
            this.queue.clear();
            disposed = true;
        }
    }
//</editor-fold>
      
    
    @Override
    public boolean canAccept()
    {
        if ( waitForThreshold )
            return this.queue.size() <= this.threshold;
        else
            return this.queue.size() < maxQueueLenght;
    } 

    @Override
    public void enqueue( byte[] data )
    {
       this.queue.offer(data);
    }
    
    

    @Override
    public void run()
    {
        throw new UnsupportedOperationException();
    }

  
}
