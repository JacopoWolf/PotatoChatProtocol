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
        InvalidAlias        (101), 
        InvalidRoomName     (102), 
        
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
        
        /**
         * return the associated ErrorCode to the byte
         * 
         * @param error in byte
         * @return associated ErrorCode enums to the byte
         * @throws PCPException 
         */
        public static ErrorCode getErrorCodeFromByte( byte error ) throws PCPException
        {
            switch( error )
            {
                case 0:
                    return PackageMalformed;
                case 100:
                    return AliasInUse;
                case 101:
                    return InvalidAlias;
                case 102:
                    return InvalidRoomName;
                case (byte) 200:
                    return ChatDenied;
                case (byte) 202:
                    return MaxClientsReached;
                case (byte) 254:
                    return ServerExploded;
                case (byte) 255:
                    return Unspecified;
                default:
                    throw new PCPException( PackageMalformed );
            }
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
