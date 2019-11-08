/*
 * this is a school project under "The Unlicence".
 */
package PCP.logic;

import PCP.data.IPCPdata;


/**
 *
 * @author JacopoWolf
 * @param <T>
 */
public interface IPCPCore<T extends IPCPdata>
{
    
    void accept ( T data );
    
}
