/*
 * this is a school project under "The Unlicence".
 */
package PCP;

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
