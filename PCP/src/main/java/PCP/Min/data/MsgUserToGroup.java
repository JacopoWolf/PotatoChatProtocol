/*
 * this is a school project under "The Unlicence".
 */

package PCP.Min.data;

import PCP.data.PCPMessage;
import PCP.*;

/**
 *
 * @author Jacopo_Wolf
 * @author gfurri20
 * @author Alessio789
 */
public class MsgUserToGroup extends PCPMessage
{
    private byte[] senderId;
    
    

    //<editor-fold defaultstate="collapsed" desc="getter and setters">
    
    public byte[] getSenderId()
    {
        return senderId;
    }
    
    public void setSenderId( byte[] senderId ) 
    {
        this.senderId = senderId;
    }

    //</editor-fold>
    
    
    
    //<editor-fold defaultstate="collapsed" desc="constructors">

    
    public MsgUserToGroup( byte[] senderId, String message )
    {
        super(OpCode.MsgUserToGroup, message);
        this.senderId = senderId;
    }

    //</editor-fold>

    @Override
    public PCP.Versions getVersion()
    {
        return PCP.Versions.Min;
    }

    @Override
    public byte[] header()
    {
        byte[] buffer = new byte[3];

        int i = 0;
        // opcode
        buffer[i++] = this.getOpCode().getByte();
        
        //SenderId
        for(byte b : senderId)
            buffer[i++] = b;
    
        return buffer;
    }

    @Override
    public int size()
    {
        return 4 + getMessage().length();
    }
                
}
