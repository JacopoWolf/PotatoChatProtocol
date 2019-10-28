/*
 * this is a school project under "The Unlicence".
 */
package PCP.packets;

import PCP.*;
import java.util.*;


/**
 *  base interface for all packets
 * @author Jacopo_Wolf
 */
public interface IPCPpacket
{
    /**
     * maximum lenght of a single package
     */
    public static final int MAX_PACKET_LENGHT = 2048;
    
    /**
     * @return the opcode of this packet
     */
    OpCode getOpCode();
    
    /**
     * Converts the packet to bytes to be sent.
     * returs multiple byte arrays if the package exceeds maximum lenght.
     * @return the collection of byte arrays payload to send.
     */
    Collection<byte[]> toBytes();
    
    /**
     * @return the total size of this packet
     */
    int size();
 
}
