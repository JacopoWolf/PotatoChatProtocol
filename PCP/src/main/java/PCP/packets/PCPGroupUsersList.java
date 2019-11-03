/*
 * this is a school project under "The Unlicence".
 */
package PCP.packets;

import PCP.*;
import java.nio.charset.*;
import java.util.*;


/**
 *
 * @author Alessio789
 * @author gfurri20
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
    public byte[] header()
    {
        byte[] buffer = new byte[this.size()];

        int i = 0;
        //OpCode
        buffer[i++] = OpCode.GroupUsersList.getByte();
        //Reason
        buffer[i++] = (byte) type;
        //JSON content lenght
        buffer[i++] = (byte) listLenght;
        
        return buffer;
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
        
        byte[] jsonList = jsonContent.getBytes( StandardCharsets.ISO_8859_1 );
        
        int NpacketsToSent = 
                (
                    this.jsonContent.length() /
                    (PCP.Min.MAX_PACKET_LENGHT - 4)
                ) 
                + 1 ;
        
        int messageRelativeMaxLenght = PCP.Min.MAX_PACKET_LENGHT - 4;
        
        
        int messagePointer = 0;
        for ( int packetN = 0; packetN < NpacketsToSent; packetN++ )
        {
            byte[] buffer = this.header();
            
            //Static index after the header
            int i = 3;
            
            //JSON content
            for 
            ( 
                int relPointer = 0; 
                relPointer < messageRelativeMaxLenght 
                    && 
                messagePointer < jsonList.length;
                relPointer++, messagePointer++
            )
            {
                buffer[i++] = jsonList[ messagePointer ];
            }
            
            //Delimitator
            buffer[i++] = 0;
            

            out.add(buffer);
        }
        
        
        
        return out;
    }   
    
}
