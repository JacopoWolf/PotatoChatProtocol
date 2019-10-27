/*
 * this is a school project under "The Unlicence".
 */
package PCP.packets;

import PCP.*;
import java.util.*;


/**
 *
 * @author Jacopo_Wolf
 */
public interface IPCPpacket
{
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
     * @return total size of this packet
     */
    int size();
 
}
