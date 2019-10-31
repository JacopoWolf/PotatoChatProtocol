/*
 * this is a school project under "The Unlicence".
 */
package PCP.packets;

import PCP.*;
import java.util.*;


/**
 *
 * @author Alessio789
 */
public class PCPGroupUsersList implements IPCPpacket 
{
    private int type;
    private int listLenght;
    private String jsonContent;

    public PCPGroupUsersList( int type, int listLenght, String jsonContent ) 
    {
        this.type = type;
        this.listLenght = listLenght;
        this.jsonContent = jsonContent;
    }

     //<editor-fold defaultstate="collapsed" desc="getter and setters">

    public int getType() 
    {
        return type;
    }

    public void setType( int type ) 
    {
        this.type = type;
    }

    public int getListLenght() 
    {
        return listLenght;
    }

    public void setListLenght( int listLenght ) 
    {
        this.listLenght = listLenght;
    }

    public String getJsonContent() 
    {
        return jsonContent;
    }

    public void setJsonContent( String jsonContent ) 
    {
        this.jsonContent = jsonContent;
    }
    
    //</editor-fold>
    
    @Override
    public OpCode getOpCode() 
    {
        return OpCode.GroupUsersList;
    }

    @Override
    public int size() 
    {
        return 4 + jsonContent.length();
    }
    
    @Override
    public Collection<byte[]> toBytes() 
    {
        Collection<byte[]> out = new ArrayList<>();
        
        byte[] buffer = new byte[this.size()];
        
        int i = 0;
        
        buffer[i++] = OpCode.GroupUsersList.getByte();
        
        buffer[i++] = (byte) type;
        
        buffer[i++] = (byte) listLenght;
        
        for( byte b : jsonContent.getBytes())
            buffer[i++] = b;
        
        //Delimitator
        buffer[i++] = 0;
        
        out.add(buffer);
        
        return out;
    }
}
