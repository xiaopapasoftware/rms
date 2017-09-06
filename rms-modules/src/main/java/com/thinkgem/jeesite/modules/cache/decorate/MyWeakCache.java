package com.thinkgem.jeesite.modules.cache.decorate;

import com.thinkgem.jeesite.modules.cache.MyCache;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.LinkedList;

public class MyWeakCache implements MyCache{

    private MyCache delegate;

    private final LinkedList<Object> hardLinksToAvoidGarbageCollection;

    private final ReferenceQueue<Object> queueOfGarbageCollectedEntries;

    private int numberOfHardLinks;

    public MyWeakCache(MyCache delegate) {
        this(delegate, 512);
    }

    public MyWeakCache(MyCache delegate, int size) {
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
    public int getSize() {
        return delegate.getSize();
    }

    public void setSize(int size) {
        removeGarbageCollectedItems();
        this.numberOfHardLinks = size;
    }

    @Override
    public void putObject(Object key, Object value) {
        removeGarbageCollectedItems();
        delegate.putObject(key, new WeakEntry(key, value, queueOfGarbageCollectedEntries));
        if (value instanceof String) {
            delegate.putObject(key, new WeakEntry(key, new String(value.toString()), queueOfGarbageCollectedEntries));
        } else {
            delegate.putObject(key, new WeakEntry(key, value, queueOfGarbageCollectedEntries));
        }
    }

    @Override
    public Object getObject(Object key) {
        Object result = null;
        @SuppressWarnings("unchecked")
        WeakReference<Object> weakReference = (WeakReference<Object>) delegate.getObject(key);
        if (weakReference != null) {
            result = weakReference.get();
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

    private void removeGarbageCollectedItems() {
        WeakEntry sv;
        while ((sv = (WeakEntry) queueOfGarbageCollectedEntries.poll()) != null) {
            delegate.removeObject(sv.key);
        }
    }

    private static class WeakEntry extends WeakReference<Object> {

        private final Object key;

        private WeakEntry(Object key, Object value, ReferenceQueue<? super Object> queue) {
            super(value, queue);
            this.key = key;
        }
    }

}
