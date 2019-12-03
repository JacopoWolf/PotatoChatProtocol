/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.data;

import PCP.*;
import PCP.data.PCPMessage;
import java.nio.charset.*;


/**
 *
 * @author Jacopo_Wolf
 */
public class MsgRecieved extends PCPMessage
{
    private String sourceAlias;

    
    //<editor-fold defaultstate="collapsed" desc="getters and setters">
    public String getSourceAlias()
    {
        return sourceAlias;
    }
    
    public void setSourceAlias( String sourceAlias )
    {
        this.sourceAlias = sourceAlias;
    }
    //</editor-fold>


    //<editor-fold defaultstate="collapsed" desc="constructors">
        public MsgRecieved( String sourceAlias, OpCode code, String message )
        {
            super(code, message);
            this.sourceAlias = sourceAlias;
        }

    //</editor-fold>
    

    @Override
    public PCP.Versions getVersion()
    {
        return PCP.Versions.Min;
    }

    @Override
    public byte[] header()
    {
        byte[] header = new byte[ 2 + this.sourceAlias.length() ];
        
        int i = 0;
        
        header[i++] = this.getOpCode().getByte();
        
        for ( byte b : this.sourceAlias.getBytes(StandardCharsets.ISO_8859_1) )
            header[i++] = b;
        
        header[i++] = 0;
        
        return header;
    }

    @Override
    public int size()
    {
        return 3 + this.sourceAlias.length() + this.getMessage().length();
    }

    
}
