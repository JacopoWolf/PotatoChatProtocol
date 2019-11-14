/*
 * this is a school project under "The Unlicence".
 */
package PCP.net;

import PCP.*;
import PCP.data.*;
import PCP.logic.*;
import java.util.*;


/**
 * the middlewere level managing and sorting incoming 
 * @author JacopoWolf
 */
public interface IPCPManager
{
    /**
     * 
     * @return logic cores initialized by this manager
     */
    List<IPCPLogicCore> getCores();
    
    /**
     * 
     * @return list of all active sockets
     */
    Set<IPCPChannel> getChannels();
    

    /**
     * initialized a new logic core of the specifies version on a new thread.
     * @param version 
     * @return  
     */
    IPCPLogicCore initLogicCore( PCP.Versions version );
    
    /**
     * call for a cache cleaning of this layer, optimizing logic cores usage and socekt assignment.
     */
    void clearCache();
    
    /**
     * accept and sort the recieved data
     * @param data the recieved byte array
     * @param from the source socket
     */
    void accept( byte[] data, IPCPChannel from );
    
    
    
    /**
     * send the IPCPData to the relative destinary
     * @param data the IPCPData to send
     * @param destination the alias of the destination
     */
    void send( IPCPData data, String destination );
    
    /**
     * send the IPCPData to multiple destinataries
     * @param data the data to send
     * @param destinations the collection of destinations aliases 
     */
    void sendBroadcast( IPCPData data, Collection<String> destinations );
    
    /**
     * send the IPCPData to the relative destinary
     * @param data the IPCPData to send
     * @param destination socket to send back data to
     */
    void send( IPCPData data, IPCPChannel destination );
    
    
    
    /**
     * closes the connection with the specified alias
     * @param alias
     */
    void close( String alias );
    
    /**
     * closes the connection with the specified socekt
     * @param channel
     */
    void close( IPCPChannel channel );
    
    
    
}
