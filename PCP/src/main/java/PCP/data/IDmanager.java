/*
 * this is a school project under "The Unlicence".
 */
package PCP.data;

import java.util.*;
import java.util.function.*;

/**
 * class used to manage IDs
 * @author Jacopo_Wolf
 * @param <T>
 */
public final class IDmanager<T>
{
    /**
     * the set of IDs
     */
    public final HashSet<T> IDset = new HashSet<>();
    
    /**
     * function used to generate new ids.
     * <p>
     * takes in a reference to the IDset and updates it with a new key and returns it.
     */
    public final Function< Set<T>, T > generator;
    
    
    
    public IDmanager( Function< Set<T>, T > generator )
    {
        this.generator = generator;
    }
    
    
    /**
     * generates a new unique id
     * @return the generated id
     */
    public T generateID()
    {
        return this.generator.apply(IDset);
    }
    
    /**
     * frees the specified id
     * @param ID 
     */
    public void freeID( T ID )
    {
        IDset.remove(ID);
    }
    
    
}
