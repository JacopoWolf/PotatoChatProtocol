
/*
 * this is a school project under "The Unlicence".
 */
package PCP;

import PCP.PCPException.ErrorCode;


/**
 * option code, referring to all version of the PCP protocol.
 * - use {@link #getByte() } to get the corresponding byte of an option
 * - use {@link #getOpCodeFromByte(byte) } to do the inverted operation
 * @author Jacopo_Wolf
 * @author Alessio789
 * @author gfurri20
 */
public enum OpCode
{
    // Messages
    MsgUserToUser           (01),
    MsgUserToGroup          (05),
    
    // User status
    Registration            (10),
    Disconnection           (11),
    AliasChange             (18),
    
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
    
    /**
     * Return the opcode enum from byte
     * 
     * @param opcode in byte
     * @return enum opcode for the associated byte
     * @throws PCPException 
     */
    public static OpCode getOpCodeFromByte( byte opcode ) throws PCPException
    {
        switch( opcode )
        {
            case 01:
                return MsgUserToUser;

            case 05:
                return MsgUserToGroup;
                
            case 10:
                return Registration;
                
            case 11:
                return Disconnection;
                
            case 18:
                return AliasChange;
                
            case 20:
                return RegAck;
                
            case 50:
                return GroupUsersListRrq;
                
            case 51:
                return GroupUsersList;
                
            case (byte) 255:
                return Error;
                
            default:
                throw new PCPException(ErrorCode.PackageMalformed);
        }
    }
    
}
