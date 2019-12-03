/*
 * this is a school project under "The Unlicence".
 */
package PCP.data;

import PCP.OpCode;
import PCP.data.PCPVariablePayload;


/**
 *
 * @author Jacopo_Wolf
 */
public abstract class PCPMessage extends PCPVariablePayload
{
    private OpCode opCode;
    private String message;

    //<editor-fold defaultstate="collapsed" desc="get set">
    
    @Override
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