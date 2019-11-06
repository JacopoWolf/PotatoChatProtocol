/*
 * this is a school project under "The Unlicence".
 */
package PCP.packets;

import PCP.*;
import com.google.gson.Gson;
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
    private ArrayList<String> listOfUsers;
    private String jsonContent;

    public PCPGroupUsersList( int type , ArrayList<String> listOfUsers ) 
    {
        this.type = type;
        this.listOfUsers = listOfUsers;
        // len of json content is calculated
        setListLenght(listOfUsers.size());
        // convert arraylist in json format
        this.jsonContent = new Gson().toJson(listOfUsers);
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
        if( listLenght > 255 )
            throw new IllegalArgumentException("Users list can't containt more than 256 users!");
        
        this.listLenght = listLenght;
    }
    
    public ArrayList<String> getListOfUsers()
    {
        return listOfUsers;
    }

    public void setListOfUsers( ArrayList<String> listOfUsers )
    {
        this.listOfUsers = listOfUsers;
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
        byte[] buffer = new byte[3];

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
        // puts message here
        Collection<byte[]> out = new ArrayList<>();
        
        // allocates byte arrays
        byte[] jsonList = this.jsonContent.getBytes( StandardCharsets.ISO_8859_1 );
        byte[] header = this.header();
        
        // necessary to calculation
        int msgRelativeMaxLenght = 
                // MAX - header - payloadDelimitator
                PCP.Min.MAX_PACKET_LENGHT - header.length - 1;
        int nPacketsToSent = 
                // ( contentTOT / msgREL ) + 1
                ( this.getJsonContent().length() / msgRelativeMaxLenght ) + 1 ;
        
                
        int absMsgPointer = 0;  // absolute message pointer
        int packetN = 0;        // current packet number
        for ( ; packetN < nPacketsToSent ; packetN++ )
        {
            int bufferLenght = 
                    ( jsonList.length - absMsgPointer ) > msgRelativeMaxLenght ? 
                        PCP.Min.MAX_PACKET_LENGHT : 
                        (header.length + jsonList.length + 1);
            
            byte[] buffer = new byte[ bufferLenght ];
                       
            int i = 0;
            // appends header
            for( byte b : this.header() )
                buffer[i++] = b;            

            // appends message payload
            for 
            ( 
                int relMsgPointer = 0; 
                relMsgPointer < msgRelativeMaxLenght && absMsgPointer < jsonList.length;
                relMsgPointer++, absMsgPointer++
            )
            {
                buffer[i++] = jsonList[ absMsgPointer ];
            }
            
            // delimitator
            buffer[i++] = 0;
            
            // add current packet to the list
            out.add(buffer);
            
        }
        
        
        
        return out;
    }   
    
}
