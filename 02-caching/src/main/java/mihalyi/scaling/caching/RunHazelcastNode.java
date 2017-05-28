/****************************************************
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 ****************************************************/

package mihalyi.scaling.caching;

import java.io.IOException;
import javax.cache.Caching;

/**
 *
 * @author Ondrej Mihalyi
 */
public class RunHazelcastNode {
    public static void main(String[] args) throws IOException {
        Caching.getCachingProvider().getCacheManager();
            
        System.out.println("Hazelcast app started.\nHit enter to stop it...");
        System.in.read();
            
    }
}

