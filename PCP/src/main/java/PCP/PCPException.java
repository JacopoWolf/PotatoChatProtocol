/*
 * this is a school project under "The Unlicence".
 */
package PCP;

/**
 *
 * @author Jacopo_Wolf
 * @author Alessio789
 */
public class PCPException extends Exception
{
    /**
     *
     * @author Jacopo_Wolf
     */
    public static enum ErrorCode
    {

        PackageMalformed    (0), 
        
        AliasInUse          (100), 
        AliasUnvalid        (101), 
        RoomNameUnvalid     (102), 
        
        ChatDenied          (200),
        MaxClientsReached   (202),
        
        ServerExploded      (254), 
        
        Unspecified         (255);
        
        
        private byte code;

        private ErrorCode( int code )
        {
            this.code = (byte) code;
        }

        public byte getByte()
        {
            return this.code;
        }
    }
    
    
    private ErrorCode errorCode;
    private PCP.Versions version;
    
    //<editor-fold defaultstate="collapsed" desc="getter and setters">
    
    public void setErrorCode( ErrorCode errorCode ) 
    {
        this.errorCode = errorCode;
    }
    
    public ErrorCode getErrorCode()
    {
        return errorCode;
    }

    public PCP.Versions getVersion()
    {
        return version;
    }

    public void setVersion( PCP.Versions version )
    {
        this.version = version;
    }

        //</editor-fold>

    public PCPException( ErrorCode errorCode )
    {
        this.errorCode = errorCode;
    }

    public PCPException( ErrorCode errorCode, String message )
    {
        super(message);
        this.errorCode = errorCode;
    }

    public PCPException( ErrorCode errorCode, PCP.Versions version, String message )
    {
        super(message);
        this.errorCode = errorCode;
        this.version = version;
    }

    
    
    
}
