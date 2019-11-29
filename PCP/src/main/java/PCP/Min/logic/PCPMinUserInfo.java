/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.logic;

import PCP.*;
import PCP.logic.*;



public class PCPMinUserInfo implements IPCPUserInfo
{
    private String alias;
    private String room;
    private byte[] id;
    private PCP.Versions version;

    @Override
    public String getAlias()
    {
        return alias;
    }

    @Override
    public void setAlias( String alias )
    {
        this.alias = alias;
    }

    @Override
    public byte[] getId()
    {
        return id;
    }

    @Override
    public void setId( byte[] id )
    {
        this.id = id;
    }

    @Override
    public PCP.Versions getVersion()
    {
        return PCP.Versions.Min;
    }

    @Override
    public String getRoom()
    {
        return room;
    }

    @Override
    public void setRoom( String room )
    {
        this.room = room;
    }
    
    
       
    
}
