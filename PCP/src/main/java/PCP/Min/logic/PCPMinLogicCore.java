/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.logic;

import PCP.Min.data.*;
import PCP.*;
import PCP.Min.data.*;
import PCP.data.*;
import PCP.logic.*;
import PCP.net.*;
import java.util.*;
import org.javatuples.*;

/**
 * 
 * @author Jacopo_Wolf
 */
public class PCPMinLogicCore implements IPCPLogicCore
{
    private boolean keepAliveFlag = false;
    
    // main components
    private IPCPManager manager;
    private PCPMinCore core;
    private PCPMinInterpreter interpreter;
    
    private LinkedList<Pair<byte[],IPCPUserInfo>> queue = new LinkedList<>();
    private boolean waitForThreshold = false;
    // those must be initialized externally
    private int maxQueueLenght = -1; 
    private int threshold = -1;
    
    
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
    public Queue<Pair<byte[],IPCPUserInfo>> getQueue()
    {
        return this.queue;
    }
    
    @Override
    public IPCPManager getManager()
    {
        return this.manager;
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

    public PCPMinLogicCore()
    {
        this.core = new PCPMinCore();
        this.interpreter = new PCPMinInterpreter();
    }
    
    private boolean disposed = false;
    @Override
    public void dispose()
    {
        if ( !disposed )
        {
            this.queue.clear();
            this.disposed = true;
        }
    }
    
    //</editor-fold>
    
    
    @Override
    public void setManager( IPCPManager manager )
    {
        this.manager = manager;
        this.core.setManager(manager);
        // todo: complete
        //this.interpreter.setIncompleteDataList();
    }
    
    @Override
    public boolean canAccept()
    {
        if ( waitForThreshold )
            return this.queue.size() <= this.threshold;
        else
            return this.queue.size() < maxQueueLenght;
    } 

    @Override
    public void enqueue( Pair<byte[],IPCPUserInfo> data )
    {
        synchronized ( queue )
        {
            this.queue.add(data);
            this.queue.notify();
        }
    }
    
    

    @Override
    public void run()
    {
        Pair<byte[],IPCPUserInfo> next;
        
        try
        {
            while ( !disposed ) // while this has not been disposed
            {
                if ( this.queue.size() < 1 ) // if queue is empty, wait for it to have some elemnts
                {
                    synchronized ( queue )
                    {
                        this.queue.wait(500);
                    }
                }
                else
                {
                    // poll first element from the queue
                    synchronized ( queue ) 
                    {
                        next = queue.poll(); 
                    }
                    
                    try
                    {
                        // interpret and accept the next element
                        IPCPData data = this.interpreter.interpret(next.getValue0());
                        
                        if (data != null) // ensures data is ready to be accepted
                            this.core.accept ( data, next.getValue1() );
                    }
                    catch ( PCPException pcpe )
                    {
                        manager.send( new ErrorMsg(pcpe), next.getValue1().getAlias() );
                    }
                    finally
                    {
                        next = null; // remove reference of now useless object
                    }
                }
            }
        }
        catch (InterruptedException ie)
        {
            this.dispose();
            return;
        }
    }

  
}
