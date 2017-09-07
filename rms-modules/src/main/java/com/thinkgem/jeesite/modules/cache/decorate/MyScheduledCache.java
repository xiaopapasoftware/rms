package com.thinkgem.jeesite.modules.cache.decorate;

import com.thinkgem.jeesite.modules.cache.MyCache;

public class MyScheduledCache implements MyCache{

    private MyCache delegate;

    protected long clearInterval;

    protected long lastClear;

    public long getClearInterval() {
        return clearInterval;
    }

    public void setClearInterval(long clearInterval) {
        this.clearInterval = clearInterval;
    }

    public MyScheduledCache(MyCache delegate) {
        this(delegate, 4 * 60 * 60 * 1000L);
    }

    public MyScheduledCache(MyCache delegate, long time) {
        this.delegate = delegate;
        this.clearInterval = time;
        this.lastClear = System.currentTimeMillis();
    }

    @Override
    public String getId() {
        return delegate.getId();
    }

    @Override
    public void putObject(Object key, Object value) {
        clearWhenStale();
        delegate.putObject(key, value);
    }

    @Override
    public Object getObject(Object key) {
        return clearWhenStale() ? null : delegate.getObject(key);
    }

    @Override
    public Object removeObject(Object key) {
        clearWhenStale();
        return delegate.removeObject(key);
    }

    @Override
    public void clear() {
        lastClear = System.currentTimeMillis();
        delegate.clear();
    }

    @Override
    public int getSize() {
        clearWhenStale();
        return delegate.getSize();
    }

    private boolean clearWhenStale() {
        if (System.currentTimeMillis() - lastClear > clearInterval) {
            clear();
            return true;
        }
        return false;
    }
}
