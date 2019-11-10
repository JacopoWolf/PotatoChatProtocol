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

    
    public enum Versions
    {
        // versions
        Min ( 2048, "Minimal", new PCPMinLogicCore()  );
        
        // variables
        private final int MAX_PACKET_LENGHT;
        
        private final String FULL_NAME;
        
        private final IPCPLogicCore VERSION_CORE;
        
        //<editor-fold defaultstate="collapsed" desc="assignment">
        
        /**
         * 
         * @return maximum lenght of a single package
         */
        public int MAX_PACKET_LENGHT()
        {
            return MAX_PACKET_LENGHT;
        }
        
        /**
         * 
         * @return complete name of the version
         */
        public String FULL_NAME()
        {
            return FULL_NAME;
        }

        /**
         * 
         * @return the logic core of this specific version
         */
        public IPCPLogicCore getVERSION_CORE()
        {
            return VERSION_CORE;
        }
        
        

        private Versions( int MAX_LENGHT, String NAME, IPCPLogicCore core )
        {
            this.MAX_PACKET_LENGHT = MAX_LENGHT;
            this.FULL_NAME = NAME;
            this.VERSION_CORE = core;
        }
        //</editor-fold>
        

    }
    
   
    // private constructor to avoid initialization
    private PCP(){}
    
}
