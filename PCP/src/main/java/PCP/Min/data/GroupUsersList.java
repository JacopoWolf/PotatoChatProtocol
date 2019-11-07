/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.data;

import PCP.*;
import com.google.gson.*;
import java.util.*;


/**
 *
 * @author Alessio789
 * @author gfurri20
 */
public class GroupUsersList extends PCPVariablePayload
{
    public enum UpdateType
    {
        complete        (0),
        joined          (1),
        disconnected    (2);
        
        
        
        private byte code;
        
        private UpdateType( int code )
        {
            this.code = (byte)code;
        }
        public byte getByte()
        {
            return this.code;
        }
    }
    
    
    private UpdateType updateType;
    private ArrayList<String> listOfUsers;
    
    
    //<editor-fold defaultstate="collapsed" desc="getter and setters">

    public UpdateType getType() 
    {
        return updateType;
    }

    public void setType( UpdateType type ) 
    {
        this.updateType = type;
    }
    
    public ArrayList<String> getListOfUsers()
    {
        return listOfUsers;
    }

    public void setListOfUsers( ArrayList<String> listOfUsers )
    {
        this.listOfUsers = listOfUsers;
    }
    
    //</editor-fold>
    
    
    public GroupUsersList( UpdateType type , ArrayList<String> listOfUsers ) 
    {
        this.updateType = type;
        this.listOfUsers = listOfUsers;
    }

    @Override
    public PCP.Versions getVersion()
    {
        return PCP.Versions.Min;
    }
    
    @Override
    public String getMessage()
    {
        return jsonContent();
    }
    
    public String jsonContent()
    {
        return new Gson().toJson(listOfUsers);
    }
    
    @Override
    public OpCode getOpCode() 
    {
        return OpCode.GroupUsersList;
    }

    @Override
    public byte[] header()
    {
        byte[] buffer = new byte[3];
        
        int i = 0;
        
        // opcode
        buffer[i++] = this.getOpCode().getByte();
        
        // updateType
        buffer[i++] = this.getType().getByte();
        
        // list total lenght
        buffer[i++] = (byte) this.listOfUsers.size();
        
        
        return buffer;
        
    }

    @Override
    public int size() 
    {
        return 4 + listOfUsers.size();
    }
    
}