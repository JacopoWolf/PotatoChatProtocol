/*
 * this is a school project under "The Unlicence".
 */
package PCP;


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
        Min ( 2048, "Minimal" );
        
        /**
         * maximum lenght of a single package
         */
        private final int MAX_PACKET_LENGHT;
        /**
         * complete name of the version
         */
        private final String FULL_NAME;

        
        //<editor-fold defaultstate="collapsed" desc="assignment">
        
        public int MAX_PACKET_LENGHT()
        {
            return MAX_PACKET_LENGHT;
        }
        
        public String FULL_NAME()
        {
            return FULL_NAME;
        }

        private Versions( int MAX_LENGHT, String NAME )
        {
            this.MAX_PACKET_LENGHT = MAX_LENGHT;
            this.FULL_NAME = NAME;
        }
        //</editor-fold>
        

    }
    
   
    // private constructor to avoid initialization
    private PCP(){}
    
}
