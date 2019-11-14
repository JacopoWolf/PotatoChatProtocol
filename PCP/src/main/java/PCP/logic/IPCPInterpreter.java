/*
 * this is a school project under "The Unlicence".
 */
package PCP.logic;

import PCP.*;
import PCP.data.*;
import java.util.*;

/**
 * interface for version-specific interpreters.
 * interpreters do take {@code data[]} and parse it into a {@link PCP.data.IPCPData}
 * @author JacopoWolf
 * @author Alessio789
 * @author gfurri20
 */
public interface IPCPInterpreter
{
    /**
     * @return the version of this interpreter
     */
    PCP.Versions getVersion();
    
    /**
     * interprets the given byte array into the correct {@link PCP.data.IPCPData} of the relative version.
     * @param data the source byte array to process.
     * @return the parsed packet, null if incomplete or partial, 
     * meaning it requires more data to be considered completed.
     * @throws PCP.PCPException when an error during the interpretation occurs.
     */
    IPCPData interpret ( byte[] data ) throws PCPException;
    
    /**
     * gets the common list of uncompleted data for this version of the protocol
     * @return incomplete or partial {@link PCP.data.IPCPData }s
     */
    Set<IPCPData> getIncompleteDataList();
    
    /**
     * used by the middlewere to assign a common List for uncompleted data for this version of the protocol.
     * @param incompleteDataList list composed by uncompleted data
     */
    void setIncompleteDataList( Set<IPCPData> incompleteDataList );
    
    
}
