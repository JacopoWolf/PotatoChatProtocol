/*
 * this is a school project under "The Unlicence".
 */
package PCP.net;

import PCP.logic.*;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * wraps a PCP connection
 * @author Jacopo_Wolf
 */
public class PCPChannel implements IPCPChannel 
{
    final ExecutorService executorServiceRef;
    final AsynchronousSocketChannel channel;
    
    
    private IPCPUserInfo userInfo;
    int timeLeftAwake = 900000; // 15 minutes
    
    public PCPChannel( ExecutorService executorServiceRef, AsynchronousSocketChannel channel, IPCPUserInfo userInfo )
    {
        this.executorServiceRef = executorServiceRef;
        this.channel = channel;
        this.userInfo = userInfo;
    }

    
    
    
    @Override
    public AsynchronousSocketChannel getChannel()
    {
        return this.channel;
    }

    @Override
    public ExecutorService getExecutorService()
    {
        return this.executorServiceRef;
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
        throw new UnsupportedOperationException();
    }
    
}
