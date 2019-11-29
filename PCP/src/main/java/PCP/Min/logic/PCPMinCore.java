/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.logic;

import PCP.data.*;
import PCP.logic.*;
import PCP.net.*;
import java.util.logging.*;


/**
 *
 * @author Jacopo_Wolf
 */
class PCPMinCore implements IPCPCore 
{
    private IPCPManager manager;

    @Override
    public void accept( IPCPData data, IPCPUserInfo from )
    {
        Logger.getGlobal().info("accepted something. But unfortunately I'm not yet implemented");
        throw new UnsupportedOperationException();
    }

    @Override
    public IPCPManager getManager()
    {
        return manager;
    }

    @Override
    public void setManager( IPCPManager manager )
    {
        this.manager = manager;
    }
    
}
