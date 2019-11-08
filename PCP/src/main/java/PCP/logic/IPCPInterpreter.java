/*
 * this is a school project under "The Unlicence".
 */
package PCP.logic;

import PCP.data.IPCPdata;

/**
 *
 * @author JacopoWolf
 */
public interface IPCPInterpreter
{
    IPCPdata interpret ( byte[] data );  
    
}
