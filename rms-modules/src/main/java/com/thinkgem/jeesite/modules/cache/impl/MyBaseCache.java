package com.thinkgem.jeesite.modules.cache.impl;

import com.thinkgem.jeesite.modules.cache.MyCache;

import java.util.HashMap;
import java.util.Map;

public class MyBaseCache implements MyCache{

    private String id;

    private Map<Object, Object> cache = new HashMap<>();

    public MyBaseCache(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        cache.put(key, value);
    }

    public int getSize() {
        return cache.size();
    }

    public Object getObject(Object key) {
        return cache.get(key);
    }

    public Object removeObject(Object key) {
        return cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MyBaseCache))
            return false;
        MyBaseCache otherCache = (MyBaseCache) o;
        return getId().equals(otherCache.getId());
    }

    public int hashCode() {
        return getId().hashCode();
    }

}
