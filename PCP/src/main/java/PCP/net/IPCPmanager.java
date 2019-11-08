/*
 * this is a school project under "The Unlicence".
 */
package PCP.net;

import PCP.*;
import PCP.data.*;
import PCP.logic.*;
import java.util.*;


/**
 *
 * @author JacopoWolf
 */
public interface IPCPmanager
{
    
    List<IPCPCore> getCores();
    
    List<IPCPSocket> getSockets();
    

    void initLogicCore( PCP.Versions version );
    
    
    void send( IPCPdata data, String destination );
    void sendBroadcast( IPCPdata data, Collection<String> destinations );
    
    
}
