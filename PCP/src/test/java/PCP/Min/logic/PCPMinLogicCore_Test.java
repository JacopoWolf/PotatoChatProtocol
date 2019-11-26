/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.logic;

import org.javatuples.*;
import org.junit.*;

/**
 *
 * @author Jacopo_Wolf
 */
public class PCPMinLogicCore_Test
{
    PCPMinLogicCore lcore;
    Thread logicCoreThread;
    
    @Before
    public void setUp()
    {
        this.lcore = new PCPMinLogicCore();
            this.lcore.setMaxQueueLenght(10);
            this.lcore.setThreshold(6);
        this.logicCoreThread = new Thread( lcore );
            logicCoreThread.start();
            
        
            
    }
    
    @Test
    public void testAccept() throws InterruptedException
    {
        if ( lcore.canAccept() == false )
            Assert.fail();
        
        PCPMinUserInfo info = new PCPMinUserInfo();
            info.setAlias("test");
            info.setId(new byte[]{1,2});
        
        lcore.enqueue( Pair.with( new byte[] { (byte)255, 0 } , info ) );
        Thread.sleep(200);
    }
    
    
    
    @After
    public void clean()
    {
        this.logicCoreThread.interrupt();
    }
    
}
