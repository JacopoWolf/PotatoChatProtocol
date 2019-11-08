/*
 * this is a school project under "The Unlicence".
 */
package PCP.logic;

import PCP.*;
import PCP.data.*;
import java.util.*;

/**
 *
 * @author JacopoWolf
 */
public interface IPCPInterpreter
{
    /**
     * 
     * @param data
     * @return the parsed packet. Null if incomplete.
     * @throws PCP.PCPException
     */
    IPCPdata interpret ( byte[] data ) throws PCPException;
    
    /**
     * 
     * @return incomplete/partial datas.
     */
    List<IPCPdata> getIncompleteDataList();
    
    /**
     * called every minute to clean incomplete list data.
     */
    void cleanCache();
    
}
