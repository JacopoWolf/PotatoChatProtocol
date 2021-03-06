/*
 * this is a school project under "The Unlicence".
 */
package PCP.logic;

import PCP.*;


/**
 * information regarding an user.
 * Version specfic.
 * @author Jacopo_Wolf
 */
public interface IPCPUserInfo
{
    
    /**
     * {@link IPCPSocket#getAlias() }
     * @param alias the new 
     */
    void setAlias(String alias);
    
    /**
     * 
     * @return the alias associated whith this connection
     */
    String getAlias();
    
    
    /**
     * 
     * @param id the new id
     */
    void setId (byte[] id);
    
    /**
     * 
     * @return the id associated with this connection
     */
    byte[] getId();


    /**
     * 
     * @param roomName 
     */
    void setRoom(String roomName);
    /**
     * 
     * @return 
     */
    String getRoom();

    
    /**
     * 
     * @return the version of the PCP protocol associated with this connection
     */
    PCP.Versions getVersion();
    
    
    /**
     * 
     * @return if this UserInfo is from a new connection
     */
    boolean isNew();
}
