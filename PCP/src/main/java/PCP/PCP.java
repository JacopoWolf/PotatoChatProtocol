/*
 * this is a school project under "The Unlicence".
 */
package PCP;

import PCP.Min.logic.*;
import PCP.logic.*;


/**
 * static class utilities and most used values in the PCP protocol.
 * @author Jacopo_Wolf
 */
public final class PCP
{
    
    /**
    * primary port to check for the server
    */
    public static final int PORT = 53101;

    /**
     * utilities for the major PCP versions.
     */
    public enum Versions
    {
        // versions
        
        /**
         * PCP-Minimal
         */
        Min ( 2048, "Minimal", 0  ),
        
        
        /**
         * general data for every version.
         */
        ALL ( 2048, null, Integer.MAX_VALUE );
        
        
        
        // variables
        /**
         * maximumm lenght of a packet
         */
        public final int MAX_PACKET_LENGHT;
        
        /**
         * full qualified name of the version
         */
        public final String FULL_NAME;
        
        /**
         * code used to identify this version
         */
        public final byte VERSION_CODE;
        
        

        private Versions( int MAX_LENGHT, String NAME, int code )
        {
            this.MAX_PACKET_LENGHT = MAX_LENGHT;
            this.FULL_NAME = NAME;
            this.VERSION_CODE = (byte)code;
        }

        
    }
    
   
    // private constructor to avoid initialization
    private PCP(){}
    
    
    /**
     * given the specific version, returs an {@link PCP.logic.IPCPLogicCore} for the specific version
     * @param version
     * @return 
     */
    public static IPCPLogicCore getLogicCore_ByVersion( Versions version )
    {
        switch ( version )
        {
            case Min:
                return new PCPMinLogicCore();
            default:
                return null;
        }
    }
    
}
