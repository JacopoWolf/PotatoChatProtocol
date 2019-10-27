/*
 * this is a school project under "The Unlicence".
 */
package PCP.packets;

import PCP.OpCode;
import java.util.*;


/**
 *
 * @author Jacopo_Wolf
 */
public interface IPCPpacket
{
    
    OpCode getOpCode();
    
    Collection<byte[]> toBytes();
    
    int size();
 
}
