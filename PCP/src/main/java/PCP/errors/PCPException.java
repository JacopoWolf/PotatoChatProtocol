/*
 * this is a school project under "The Unlicence".
 */
package PCP.errors;

import PCP.packets.*;


/**
 *
 * @author Jacopo_Wolf
 */
public class PCPException extends Exception
{
    private ErrorCode cause;

    public PCPException( ErrorCode cause )
    {
        this.cause = cause;
    }

    public PCPException( ErrorCode cause, String message )
    {
        super(message);
        this.cause = cause;
    }
    
    
    public PCPErrorPacket generatePacket()
    {
        throw new UnsupportedOperationException();
    }
    
    
}
