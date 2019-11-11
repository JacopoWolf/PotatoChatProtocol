/*
 * this is a school project under "The Unlicence".
 */
package PCP.logic;

import PCP.*;
import PCP.net.*;
import java.util.*;


/**
 *  the logic core of a server. Runs on a single thread and it's managed by the IPCPmanager middlewere.
 * @author JacopoWolf
 */
public interface IPCPLogicCore extends Runnable
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
    Queue<byte[]> getQueue();
    
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
    
//</editor-fold>
    
    /**
     * the main loop of the logic core.
     * Handles elaboration of the queue.
     */
    @Override
    public void run();
    
    /**
     * enqueues a new packet to be elaborated.
     * @param data new byte packet to add to the queue
     * @return true if the queue is not over it's max lenght and the operation completed sucesfully
     */
    boolean enqueue( byte[] data );
    
    
}
