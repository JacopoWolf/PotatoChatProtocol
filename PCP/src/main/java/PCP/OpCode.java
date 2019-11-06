/*
 * this is a school project under "The Unlicence".
 */
package PCP;


/**
 *
 * @author Jacopo_Wolf
 */
public enum OpCode
{
    // Messages
    MsgUserToUser           (01),
    MsgUserToGroup          (05),
    
    // User status
    Registration            (10),
    Disconnection           (11),
    AliasChanghe            (18),
    
    // control messages
    RegAck                  (20),
    GroupUsersListRrq       (50),
    GroupUsersList          (51),
    
    // 
    Error                   (255);
    
    
    
    private byte code;

    private OpCode( int code )
    {
        this.code = (byte)code;
    }
    
    public byte getByte()
    {
        return this.code;
    }
    
}
