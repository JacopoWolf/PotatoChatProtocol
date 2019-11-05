/*
 * this is a school project under "The Unlicence".
 */
package PCP.packets;

import PCP.*;


/**
 *
 * @author Jacopo_Wolf
 */
public abstract class PCPMessage implements IPCPpacket
{
    private OpCode opCode;
    private String message;

    //<editor-fold defaultstate="collapsed" desc="get set">
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage( String message )
    {
        this.message = message;
        
    }
    
    @Override
    public OpCode getOpCode()
    {
        return opCode;
    }

    void setCode( OpCode code )
    {
        if ( code != OpCode.MsgUserToGroup && code != OpCode.MsgUserToUser )
            throw new IllegalArgumentException("message can't have not message opcode!");
        
        this.opCode = code;
    }
    
    
    
    //</editor-fold>

    public PCPMessage( String message )
    {
        this.message = message;
    }

    public PCPMessage( OpCode code, String message )
    {
        this.setCode(code);
        this.message = message;
    }
  
}