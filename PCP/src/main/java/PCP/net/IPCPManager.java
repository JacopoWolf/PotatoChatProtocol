/*
 * this is a school project under "The Unlicence".
 */
package PCP.net;

import PCP.*;
import PCP.data.*;
import PCP.logic.*;
import java.io.*;
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
    void cleanCache();
    
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
     * @throws java.io.IOException errors while sending data
     */
    void send( IPCPData data, String destination ) throws IOException;
    
    /**
     * send the IPCPData to multiple destinataries
     * @param data the data to send
     * @param destinations the collection of destinations aliases 
     * @throws java.io.IOException 
     */
    void sendBroadcast( IPCPData data, Collection<String> destinations ) throws IOException;
    
    /**
     * send the IPCPData to the relative destinary
     * @param data the IPCPData to send
     * @param destination socket to send back data to
     * @throws java.io.IOException
     */
    void send( IPCPData data, IPCPChannel destination ) throws IOException;
    
    
    
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
