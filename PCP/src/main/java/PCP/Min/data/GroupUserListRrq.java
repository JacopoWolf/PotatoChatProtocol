/*
 * this is a school project under "The Unlicence".
 */

package PCP.Min.data;
import PCP.data.IPCPdata;
import PCP.*;
import java.util.*;

/**
 *
 * @author gfurri20
 * @author Alessio789
 */
public class GroupUserListRrq implements IPCPdata
{
    private byte[] senderId;

    public GroupUserListRrq( byte[] senderId )
    {
        this.senderId = senderId;
    }

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
    
    @Override
    public OpCode getOpCode()
    {
        return OpCode.GroupUsersListRrq;
    }

    @Override
    public PCP.Versions getVersion()
    {
        return PCP.Versions.Min;
    }
    
    @Override
    public byte[] header()
    {
        byte[] buffer = new byte[this.size()];
        
        int i = 0;
        //Opcode
        buffer[i++] = OpCode.GroupUsersListRrq.getByte();
        //SenderId
        for(byte b : senderId)
            buffer[i++] = b;
        
        return buffer;
    }

    @Override
    public int size()
    {
        return 3;
    }

    @Override
    public Collection<byte[]> toBytes()
    {
        Collection<byte[]> out = new ArrayList<>();

        //Adds the header to the package
        out.add(this.header());
        
        return out;
    }
    
}
