/*
 * this is a school project under "The Unlicence".
 */
package PCP.logic;

import java.util.Queue;


/**
 *
 * @author JacopoWolf
 */
public interface IPCPLogicCore
{
    IPCPCore getCore();
    
    IPCPInterpreter getInterpreter();
    
    void process();
    
    Queue<byte[]> getQueue();
    
    void enqueue( byte[] data );
}
