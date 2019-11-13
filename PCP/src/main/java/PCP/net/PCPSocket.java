/*
 * this is a school project under "The Unlicence".
 */
package PCP.net;

import PCP.logic.*;
import java.io.*;
import java.net.*;

/**
 *
 * @author Alessio789
 */
public class PCPSocket implements IPCPSocket 
{
    private Socket socket;
    private IPCPUserInfo userInfo;
    
    private BufferedInputStream ins;
    private BufferedOutputStream outs;

    public PCPSocket( Socket socket, IPCPUserInfo info ) throws IOException
    {
        this.socket = socket;
        this.userInfo = info;
        
        this.ins = new BufferedInputStream(socket.getInputStream());
        this.outs = new BufferedOutputStream(socket.getOutputStream());
    }

    //<editor-fold defaultstate="collapsed" desc="getters and setters">

    @Override
    public BufferedInputStream getBuffInStream()
    {
        return ins;
    }

    @Override
    public BufferedOutputStream getBuffOutStream()
    {
        return outs;
    }

    @Override
    public Socket getSocket()
    {
        return this.socket;
    }
    
    
    
    
    public void setSocket( Socket socket ) 
    {
        this.socket = socket;
    }

    @Override
    public IPCPUserInfo getUserInfo()
    {
        return this.userInfo;
    }

    @Override
    public void setUserInfo( IPCPUserInfo info )
    {
        this.userInfo = info;
    }

    
    //</editor-fold>
    
}
