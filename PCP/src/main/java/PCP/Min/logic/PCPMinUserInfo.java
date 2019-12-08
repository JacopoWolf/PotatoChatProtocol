/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.logic;

import PCP.*;
import PCP.logic.*;



public class PCPMinUserInfo implements IPCPUserInfo
{
    private String alias = null;
    private String room = null;
    private byte[] id = null;
    private PCP.Versions version = null;

    /**
     * initialized a new empty UserInfo.
     */
    public PCPMinUserInfo()
    {
        
    }
    
    
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

    @Override
    public boolean isNew()
    {
        return this.alias == null && this.room == null && this.id == null && this.version == null;
    }

    @Override
    public String toString()
    {
        return "User: " + getAlias() + " - Room: " + getRoom() + " - Version of the protocol: " + getVersion().FULL_NAME;
    }
    
}
