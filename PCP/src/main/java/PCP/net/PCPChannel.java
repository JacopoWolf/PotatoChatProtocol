/*
 * this is a school project under "The Unlicence".
 */
package PCP.net;

import PCP.logic.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.util.logging.*;

/**
 * wraps a PCP connection
 * @author Jacopo_Wolf
 */
public class PCPChannel implements IPCPChannel 
{

    final AsynchronousSocketChannel channel;
    
    
    private IPCPUserInfo userInfo;
    int timeLeftAwake = 900000; // 15 minutes
    
    public PCPChannel( AsynchronousSocketChannel channel, IPCPUserInfo userInfo )
    {
        this.channel = channel;
        this.userInfo = userInfo;
    }

    
    
    
    @Override
    public AsynchronousSocketChannel getChannel()
    {
        return this.channel;
    }

    @Override
    public int getTimeLeftAwake()
    {
        return this.timeLeftAwake;
    }
    
    @Override
    public void setTimeLeftAwake(int timeleft)
    {
        this.timeLeftAwake = timeleft;
    }
    

    @Override
    public IPCPUserInfo getUserInfo()
    {
        return this.userInfo;
    }

    @Override
    public synchronized void setUserInfo( IPCPUserInfo info )
    {
        this.userInfo = info;
    }

    

    @Override
    public void send( Collection<byte[]> data )
    {
        for ( byte[] b : data )
        {
            ByteBuffer bb = ByteBuffer.wrap(b);
            try
            {
                this.channel.write(bb);
                
                Logger.getGlobal().log(Level.INFO, "sucessfully sent data to: {0}", this.getChannel().getRemoteAddress().toString());
                Logger.getGlobal().log(Level.FINEST, "data sent to{0}:\n{1}", new Object[]{this.getChannel().getRemoteAddress(), Arrays.toString(b)});
            }
            catch ( Exception e )
            {
                return;
            }
        }
    }
    
}