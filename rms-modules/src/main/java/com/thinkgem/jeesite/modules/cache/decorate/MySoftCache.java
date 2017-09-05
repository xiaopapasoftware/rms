package com.thinkgem.jeesite.modules.cache.decorate;

import com.thinkgem.jeesite.modules.cache.MyCache;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.LinkedList;

public class MySoftCache implements MyCache{

    private MyCache delegate;

    private final LinkedList<Object> hardLinksToAvoidGarbageCollection;

    private final ReferenceQueue<Object> queueOfGarbageCollectedEntries;

    private int numberOfHardLinks;

    public MySoftCache(MyCache delegate) {
        this(delegate, 256);
    }

    public MySoftCache(MyCache delegate, int size) {
        this.delegate = delegate;
        this.numberOfHardLinks = size;
        this.hardLinksToAvoidGarbageCollection = new LinkedList<>();
        this.queueOfGarbageCollectedEntries = new ReferenceQueue<>();
    }

    @Override
    public String getId() {
        return delegate.getId();
    }

    @Override
    public void putObject(Object key, Object value) {
        removeGarbageCollectedItems();
        if (value instanceof String) {
            delegate.putObject(key, new SoftEntry(key, new String(value.toString()), queueOfGarbageCollectedEntries));
        } else {
            delegate.putObject(key, new SoftEntry(key, value, queueOfGarbageCollectedEntries));
        }
    }

    @Override
    public Object getObject(Object key) {
        Object result = null;
        @SuppressWarnings("unchecked")
        SoftReference<Object> softReference = (SoftReference<Object>) delegate.getObject(key);
        if (softReference != null) {
            result = softReference.get();
            if (result == null) {
                delegate.removeObject(key);
            } else {
                if (!hardLinksToAvoidGarbageCollection.contains(result)) {
                    synchronized (hardLinksToAvoidGarbageCollection) {
                        if (!hardLinksToAvoidGarbageCollection.contains(result)) {
                            hardLinksToAvoidGarbageCollection.addFirst(result);
                        }
                    }
                }
                while (hardLinksToAvoidGarbageCollection.size() > numberOfHardLinks) {
                    hardLinksToAvoidGarbageCollection.removeLast();
                }
            }
        }
        return result;
    }

    @Override
    public Object removeObject(Object key) {
        removeGarbageCollectedItems();
        return delegate.removeObject(key);
    }

    @Override
    public void clear() {
        synchronized (hardLinksToAvoidGarbageCollection) {
            hardLinksToAvoidGarbageCollection.clear();
        }
        removeGarbageCollectedItems();
        delegate.clear();
    }

    @Override
    public int getSize() {
        return delegate.getSize();
    }

    public void setSize(int size) {
        removeGarbageCollectedItems();
        this.numberOfHardLinks = size;
    }

    private void removeGarbageCollectedItems() {
        SoftEntry sv;
        while ((sv = (SoftEntry) queueOfGarbageCollectedEntries.poll()) != null) {
            delegate.removeObject(sv.key);
        }
    }

    private static class SoftEntry extends SoftReference<Object> {

        private final Object key;

        private SoftEntry(Object key, Object value, ReferenceQueue<? super Object> queue) {
            super(value, queue);
            this.key = key;
        }
    }

}
