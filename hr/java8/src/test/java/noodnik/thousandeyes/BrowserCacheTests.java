package noodnik.thousandeyes;

import static noodnik.lib.Common.log;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

import org.junit.Test;

public class BrowserCacheTests {
    
    
    private static final String K1 = "k1";
    private static final String K2 = "k2";
    private static final String K3 = "k3";
    private static final String K4 = "k4";
    
    private static final String V1 = "v1";
    private static final String V2 = "v2";
    private static final String V3 = "v3";
    private static final String V4 = "v4";

    /**
     * 
     * Test cases:
     * 
     * 1. cache of size 1:
     *      a. populate 1 and retrieve
     *      b. populate 1 and expire
     *      c. populate 2 and retrieve 2
     *      
     * 2. cache of size 2:
     *      a. populate 4:
     *          i. retrieve longest-lived
     *          ii. confirm eviction
     *      
     */

    @Test
    public void size1Populate1Retrieval() {
        
        long currentClock = 0;
        BrowserCache browserCache = new BrowserCache(1, () -> currentClock);
        browserCache.set(K1, V1, currentClock + 1);
        
        String retrieval1 = browserCache.get(K1);
        log("retrieval1(%s)", retrieval1);
        
        assertEquals(V1, retrieval1);
    }

    @Test
    public void size1Populate2Retrieval2() {
        
        long currentClock = 0;
        BrowserCache browserCache = new BrowserCache(1, () -> currentClock);
        browserCache.set(K1, V1, currentClock + 1);
        browserCache.set(K2, V2, currentClock + 1);
        
        String retrieval1 = browserCache.get(K1);
        String retrieval2 = browserCache.get(K2);
        log("retrieval1(%s), retrieval2(%s)", retrieval1, retrieval2);

        assertNull(retrieval1);
        assertEquals(V2, retrieval2);
    }

    @Test
    public void size1Expiry() {

        long currentClock = 0;
        BrowserCache browserCache = new BrowserCache(1, () -> currentClock);
        browserCache.set(K1, V1, currentClock);
        
        String retrieval1 = browserCache.get(K1);
        log("retrieval1(%s)", retrieval1);
        
        assertNull(retrieval1);
    }

    @Test
    public void size2Populate4EvictRetrieveLongestLived() {
        
        long currentClock = 0;
        BrowserCache browserCache = new BrowserCache(2, () -> currentClock);
        browserCache.set(K1, V1, currentClock + 1);
        browserCache.set(K2, V2, currentClock + 2); // non-longest-lived later to be evicted
        browserCache.set(K1, V1, currentClock + 3); // update to become longest lived
        browserCache.set(K3, V3, currentClock + 1); // should cause eviction of K2
        browserCache.set(K3, V3, currentClock);     // should purge (now expired) K3
        browserCache.set(K4, V4, currentClock + 1); // already expired entry should be ignored
        browserCache.set(K4, V4, currentClock + 1); // duplicate shouldn't change anythnig
        
        String retrieval1 = browserCache.get(K1);
        String retrieval2 = browserCache.get(K2);
        String retrieval3 = browserCache.get(K3);
        String retrieval4 = browserCache.get(K4);

        log("retrieval1(%s), retrieval2(%s), retrieval3(%s), retrieval4(%s)", retrieval1, retrieval2, retrieval3, retrieval4);
        
        assertEquals(V1, retrieval1);   // survived eviction since longest lived
        assertNull(retrieval2);         // evicted
        assertNull(retrieval3);         // expired
        assertEquals(V4, retrieval4);   // last added
        
        assertEquals(2, browserCache.cacheMap.size());
        assertEquals(2, browserCache.expiryQueue.size());
        
    }

    
    /*
    Acme Corp  wants to implement a new web browser. To improve subsequent page loads, they need to implement a cache
    for the static assets loaded on the page. For their initial implementation they just want to honor the "Expires"
    header provided in the http responses for those assets, so that they never return cached assets after they're expired.

    Can you help Acme Corp implement this cache?

    Goal:
    - Help Acme Corp implement their caching logic

    API:
        Constructor:
            Capacity - the maximum number of values the cache can hold
        Methods:
            get(key) -> value if (value_exists and not expired) else None
            set(key, value, expiryTime) // Will evict the soonest to expire entry if at capacity

        Data Format:
            key: String
            value: String
            expiryTime: Number (Unix timestamp)

    Assumptions:
    - All data supplied to the cache will be provided with an associated expiry timestamp.
    - Our cache does not require any knowledge about HTTP or networking concepts. It will just work off of the supplied expiry time.
    - Assume there are no duplicate expiry times

    */


    class CacheEntry {
        CacheEntry(String value, long expiry) {
            this.expiry = expiry;
            this.value = value;
        }
        long expiry;
        String value;
    }

    class BrowserCache {
      
        int maxSize;
        Supplier<Long> clockFn;
      
        BrowserCache(int maxSize, Supplier<Long> clockFn) {
            this.maxSize = maxSize;
            this.clockFn = clockFn;
        }

        Map<String, CacheEntry> cacheMap = new HashMap<>();
        Map<Long, String> expiryQueue = new TreeMap<>();
      
        String keyOfMostRecentExpiry;
        long mostRecentExpiry;

        String get(String key) {
            CacheEntry entry = cacheMap.get(key);
            if (entry == null) {
                return null;
            }
            if (entry.expiry <= clockFn.get()) {
                purge(key);
                return null;
            }
            return entry.value;
        }

        void set(String key, String value, long expiry) {
            purge(key);
            if (expiry <= clockFn.get()) {
                // expired entry
                return;
            }
            if (cacheMap.size() >= maxSize) {
                // purge next to expire entry to keep cache at max size
                purge(expiryQueue.entrySet().iterator().next().getValue());
            }
            cacheMap.put(key, new CacheEntry(value, expiry));
            expiryQueue.put(expiry, key);
        }

        void purge(String key) {
            CacheEntry mapEntry = cacheMap.remove(key);
            if (mapEntry == null) {
                return;
            }
            long expiry = mapEntry.expiry;
            expiryQueue.remove(expiry);
        }
      
    }

}


