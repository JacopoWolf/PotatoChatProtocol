/*
 * this is a school project under "The Unlicence".
 */
package PCP.errors;


/**
 *
 * @author Jacopo_Wolf
 */
public enum ErrorCode
{
    
    PackageMalformed        (000),
    
    AliasInUse              (100),
    AliasUnvalid            (101),
    RoomNameUnvalid         (102),
    
    ChatDenied              (200),
    MaxClientsReached       (202),
    
    ServerExploded          (250),
    
    Unspecified             (255);
    
    
    
    private byte code;

    private ErrorCode( int code )
    {
        this.code = (byte)code;
    }
    
    public byte toByte()
    {
        return this.code;
    }
    
}
