/*
 * this is a school project under "The Unlicence".
 */
package PCP.data;

import PCP.OpCode;
import PCP.PCP;
import java.util.*;


/**
 *  base interface for all packets
 * @author Jacopo_Wolf
 */
public interface IPCPdata
{ 
    /**
     * 
     * @return the current version of this data
     */
    public PCP.Versions getVersion();
    
    /**
     * @return the opcode of this packet
     */
    OpCode getOpCode();
    
    /**
     * generates and returns the header of this particular packet. 
     * header's lenght is never superior to PACKET_MAX_LENGHT
     * @return the header of this packet
     */
    byte[] header();
    
    /**
     * calculates the total size of the packet, headers and payload included.
     * @return the total size of this packet.
     */
    int size();
    
    /**
     * Converts the packet to bytes to be sent.
     * returs multiple byte arrays if the package exceeds maximum lenght.
     * @return the collection of byte arrays payload to send.
     */
    Collection<byte[]> toBytes();
 
}
