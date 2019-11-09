/*
 * this is a school project under "The Unlicence".
 */
package PCP.net;

import PCP.*;
import java.net.*;

/**
 *
 * @author Alessio789
 */
public class PCPSocket implements IPCPSocket 
{
    private Socket socket;
    private String alias;
    private byte[] id;
    private PCP.Versions version;

    public PCPSocket( Socket socket, String alias, byte[] id, PCP.Versions version ) 
    {
        this.socket = socket;
        this.alias = alias;
        this.id = id;
        this.version = version;
    }

    //<editor-fold defaultstate="collapsed" desc="getters and setters">
    
    public void setSocket( Socket socket ) 
    {
        this.socket = socket;
    }

    public void setAlias( String alias ) 
    {
        this.alias = alias;
    }

    public void setId( byte[] id ) 
    {
        this.id = id;
    }

    public void setVersion(PCP.Versions version) {
        this.version = version;
    }
    
    @Override
    public Socket getSocket() 
    {
        return this.socket;
    }

    @Override
    public String getAlias() 
    {
        return this.alias;
    }

    @Override
    public byte[] getId() 
    {
        return this.id;
    }

    @Override
    public PCP.Versions getVersion() 
    {
        return this.version;
    }
    
    //</editor-fold>
    
}
