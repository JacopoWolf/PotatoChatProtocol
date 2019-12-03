/*
 * this is a school project under "The Unlicence".
 */
package PCP.logic;

import PCP.*;
import PCP.net.*;
import java.util.*;
import org.javatuples.*;

/**
 * the logic core of a server. 
 * Runs on a single thread and it's managed by an {@link IPCPManager}.
 * @author JacopoWolf
 */
public interface IPCPLogicCore extends Runnable, IDisposable
{
    
    //<editor-fold desc="getters and setters">
    
    /**
     * 
     * @return the version of the current logic core
     */
    PCP.Versions getVersion();

    
    /**
     *
     * @return the manager
     */
    IPCPManager getManager();
    
    /**
     * set the manager of this logicore
     * @param manager 
     */
    void setManager( IPCPManager manager );
    
    /**
     *
     * @return the core of this virtual instance
     */
    IPCPCore getCore();
    
    /**
     *
     * @return the interpreter of this
     */
    IPCPInterpreter getInterpreter();
    
    /**
     *
     * @return the queue of packets to parse
     */
    Queue< Pair<byte[],IPCPUserInfo> > getQueue();
    
    /**
     *
     * @param lenght the new maximum lenght
     */
    void setMaxQueueLenght(int lenght);
    
    /**
     *
     * @return the maximum quque lenght
     */
    int getMaxQueueLenght();
    
    
    /**
     * {@link IPCPLogicCore#getThreshold() }
     * @param theshold new threshold value
     */
    void setThreshold( int theshold );
    
    /**
     * once a core's queue is full, this'll wait until the core's queue lenght is lower than this value
     * before enqueuing new data.
     * @return 
     */
    int getThreshold();
    
//</editor-fold>
    
    /**
     * the main loop of the logic core.
     * Handles elaboration of the queue.
     */
    @Override
    public void run();
    
    /**
     * stops {@link IPCPLogicCore#run()} from accepting new data and terminates the thread this object runs on.
     */
    @Override
    public void dispose();
    
    /**
     * enqueues a new packet to be elaborated.
     * @param data new byte packet to add to the queue and information of the sender
     */
    void enqueue( Pair<byte[],IPCPUserInfo> data );
    
    /**
     * 
     * @return if the queue will accept new data.
     */
    boolean canAccept();
    
    /**
     * 
     * @return if this LogicCore has been set to be keepen alive
     */
    boolean isKeepAlive();
    
    /**
     * @see IPCPLogicCore#isKeepAlive() 
     * @param keepAlive 
     */
    void setKeepAlive( boolean keepAlive );
    
}
