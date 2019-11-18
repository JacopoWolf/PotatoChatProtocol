/*
 * this is a school project under "The Unlicence".
 */
package PCP;

/**
 * thrown on PCP related errors
 * @author Jacopo_Wolf
 * @author Alessio789
 */
public class PCPException extends Exception
{
    /**
     * all of the possible error codes
     * @author Jacopo_Wolf
     */
    public static enum ErrorCode
    {

        /**
         * the package is malformed, meaning an error in the parsing happened.
         */
        PackageMalformed    (0), 
        
        /**
         * the specified alias is already in use by anohter user
         */
        AliasInUse          (100), 

        /**
         * the alias you sent is invalid. Might exceed maximum lenght or contain invalid character
         */
        InvalidAlias        (101), 

        /**
         * the name of the specified room doesn't exist
         */
        InvalidRoomName     (102), 
        
        /**
         * the chat has been denied for unspecified reasons. Possibly a ban.
         */
        ChatDenied          (200),

        /**
         * Reached maximum amount of clients. No more connections will be accepted.
         */
        MaxClientsReached   (202),
        
        /**
         * OK BOOMER.
         */
        ServerExploded      (254), 
        
        /**
         * 
         */
        Unspecified         (255);
        
        
        private byte code;

        private ErrorCode( int code )
        {
            this.code = (byte) code;
        }

        /**
         *
         * @return
         */
        public byte getByte()
        {
            return this.code;
        }
        
        /**
         *
         * @param code
         * @return
         */
        public static boolean requiresConnectionClose( ErrorCode code )
        {
            return 
                code.getByte() >= 200 || code.getByte() < 100;
        }
        
    }
    
    
    private ErrorCode errorCode;
    private PCP.Versions version;
    
    //<editor-fold defaultstate="collapsed" desc="getter and setters">
    
    /**
     *
     * @param errorCode
     */
        
    public void setErrorCode( ErrorCode errorCode ) 
    {
        this.errorCode = errorCode;
    }
    
    /**
     *
     * @return
     */
    public ErrorCode getErrorCode()
    {
        return errorCode;
    }

    /**
     *
     * @return
     */
    public PCP.Versions getVersion()
    {
        return version;
    }

    /**
     *
     * @param version
     */
    public void setVersion( PCP.Versions version )
    {
        this.version = version;
    }

        //</editor-fold>

    /**
     *
     * @param errorCode
     */
    
    public PCPException( ErrorCode errorCode )
    {
        this.errorCode = errorCode;
    }

    /**
     *
     * @param errorCode
     * @param message
     */
    public PCPException( ErrorCode errorCode, String message )
    {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     *
     * @param errorCode
     * @param version
     * @param message
     */
    public PCPException( ErrorCode errorCode, PCP.Versions version, String message )
    {
        super(message);
        this.errorCode = errorCode;
        this.version = version;
    }

    
    
    
}
