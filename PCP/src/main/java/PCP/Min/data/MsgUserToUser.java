/*
 * this is a school project under "The Unlicence".
 */

package PCP.Min.data;

import PCP.*;
import PCP.data.PCPMessage;
import java.nio.charset.*;

/**
 *
 * @author gfurri20
 * @author Jacopo_Wolf
 * @author Alessio789
 */
public class MsgUserToUser extends PCPMessage
{
    private byte[] senderId;
    private String destinationAlias;


//<editor-fold defaultstate="collapsed" desc="getters and setters">
    
    public byte[] getSenderId()
    {
        return senderId;
    }
    
    public void setSenderId( byte[] senderId )
    {
        this.senderId = senderId;
    }
    
    public String getDestinationAlias()
    {
        return destinationAlias;
    }
    
    public void setDestinationAlias( String destinationAlias )
    {
        this.destinationAlias = destinationAlias;
    }
    
    
//</editor-fold>

    public MsgUserToUser( byte[] senderId, String destinationAlias, String message )
    {
        super(OpCode.MsgUserToUser, message);
        this.senderId = senderId;
        this.destinationAlias = destinationAlias;
    }
    

    @Override
    public PCP.Versions getVersion()
    {
        return PCP.Versions.Min;
    }
    
    @Override
    public byte[] header()
    {
        byte[] buffer = new byte[4 + destinationAlias.length()];
        int i = 0;
        // opcode
        buffer[i++] = OpCode.MsgUserToUser.getByte();

        // senderID
        for(byte b : senderId)
            buffer[i++] = b;
        
        // destination alias
        for (byte b : destinationAlias.getBytes(StandardCharsets.ISO_8859_1))
            buffer[i++] = b;
        buffer[i++] = 0;

        return buffer;
    }

    @Override
    public int size()
    {
        return 5 + destinationAlias.length() + getMessage().length();
    }
    
}
