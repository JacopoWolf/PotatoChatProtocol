/*
 * this is a school project under "The Unlicence".
 */
package PCP.net;

import PCP.PCP;
import java.net.Socket;

/**
 *
 * @author Alessio789
 */
public class PCPSocket implements IPCPSocket {
    
    private Socket socket;
    private String alias;
    private byte[] id;
    private PCP.Versions version;

    public PCPSocket( Socket socket, String alias, byte[] id, 
            PCP.Versions version ) 
    {
        this.socket = socket;
        this.alias = alias;
        this.id = id;
        this.version = version;
    }

    //<editor-fold defaultstate="collapsed" desc="getters and setters">
    
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setId(byte[] id) {
        this.id = id;
    }

    public void setVersion(PCP.Versions version) {
        this.version = version;
    }
    
    @Override
    public Socket getSocket() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAlias() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] getId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PCP.Versions getVersion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //</editor-fold>
    
}
