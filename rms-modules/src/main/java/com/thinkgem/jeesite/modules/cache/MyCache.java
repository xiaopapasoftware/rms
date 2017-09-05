package com.thinkgem.jeesite.modules.cache;

public interface MyCache {

    String getId();

    void putObject(Object key, Object value);

    Object getObject(Object key);

    Object removeObject(Object key);

    void clear();

    int getSize();

}
