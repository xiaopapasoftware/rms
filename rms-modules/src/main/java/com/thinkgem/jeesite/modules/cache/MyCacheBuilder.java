package com.thinkgem.jeesite.modules.cache;

import com.thinkgem.jeesite.modules.cache.decorate.MySoftCache;
import com.thinkgem.jeesite.modules.cache.decorate.MyWeakCache;
import com.thinkgem.jeesite.modules.cache.impl.MyBaseCache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyCacheBuilder {

    private Lock lock = new ReentrantLock();

    private volatile Map<String, MyCache> baseCacheMap = new HashMap<>(8);

    private volatile Map<String, MyCache> softCacheMap = new HashMap<>(8);

    //if you want to use this, be cautious!
    private volatile Map<String, MyCache> weakCacheMap = new HashMap<>(2);

    public MyCache getBaseCache(String id) {
        if (baseCacheMap.get(id) == null) {
            lock.lock();
            try {
                baseCacheMap.computeIfAbsent(id, MyBaseCache::new);
            } finally {
                lock.unlock();
            }
        }
        return baseCacheMap.get(id);
    }

    public MyCache getWeakCache(String id) {
        if (weakCacheMap.get(id) == null) {
            lock.lock();
            try {
                weakCacheMap.computeIfAbsent(id, i -> new MyWeakCache(new MyBaseCache(i)));
            } finally {
                lock.unlock();
            }
        }
        return weakCacheMap.get(id);
    }

    public MyCache getSoftCache(String id) {
        if (softCacheMap.get(id) == null) {
            lock.lock();
            try {
                softCacheMap.computeIfAbsent(id, i -> new MySoftCache(new MyBaseCache(i)));
            } finally {
                lock.unlock();
            }
        }
        return softCacheMap.get(id);
    }

    public static MyCacheBuilder getInstance() {
        return innerCacheBuilder.builder;
    }

    private static class innerCacheBuilder {
        final static MyCacheBuilder builder = new MyCacheBuilder();
    }

}
