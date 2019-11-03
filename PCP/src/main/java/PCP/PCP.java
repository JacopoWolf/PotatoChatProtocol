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
    
    
    /**
     * PCP-Minimal
     * @author Jacopo_Wolf
     */
    public static class Min
    {
        /**
        * maximum lenght of a single package
        */
        public static final int MAX_PACKET_LENGHT = 2048;
    }
    
   
    // private constructor to avoid initialization
    private PCP(){}
    
}
