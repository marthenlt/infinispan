/* 
 * JBoss, Home of Professional Open Source 
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tag. All rights reserved. 
 * See the copyright.txt in the distribution for a 
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use, 
 * modify, copy, or redistribute it subject to the terms and conditions 
 * of the GNU Lesser General Public License, v. 2.1. 
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details. 
 * You should have received a copy of the GNU Lesser General Public License, 
 * v.2.1 along with this distribution; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.infinispan.jsr107.cache;

import java.util.ArrayList;
import java.util.List;

import javax.cache.event.CacheEntryCreatedListener;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryListener;
import javax.cache.event.CacheEntryListenerRegistration;
import javax.cache.event.CacheEntryReadListener;
import javax.cache.event.CacheEntryRemovedListener;
import javax.cache.event.CacheEntryUpdatedListener;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryVisited;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryModifiedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryVisitedEvent;

/**
 * InfinispanCacheListenerAdapter as its name implies adapts Infinispan notification mechanism to
 * JSR 107 notification mechanism.
 * 
 * 
 * @author Vladimir Blagojevic
 * @since 5.3
 */
@Listener
public class InfinispanCacheListenerAdapter<K, V> {
   private final InfinispanCache<K, V> cache;

   public InfinispanCacheListenerAdapter(InfinispanCache<K, V> infinispanCache) {
      this.cache = infinispanCache;
   }

   @CacheEntryCreated
   public void handleCacheEntryCreatedEvent(CacheEntryCreatedEvent<K, V> e) {
      //TODO as CacheEntryEvent does not have value...it will cause NPE in listener invocation tck test
      RICacheEntryEvent<K, V> jsrEvent = new RICacheEntryEvent<K, V>(cache, e.getKey(), null);
      List<CacheEntryEvent<K, V>> events = new ArrayList<CacheEntryEvent<K, V>>();
      events.add(jsrEvent);

      for (CacheEntryListenerRegistration<? super K, ? super V> registration : cache.getListeners()
               .values()) {
         CacheEntryEventFilter<? super K, ? super V> filter = registration.getCacheEntryFilter();
         Iterable<CacheEntryEvent<K, V>> iterable = filter == null ? events
                  : new RICacheEntryEventFilteringIterable<K, V>(events, filter);

         CacheEntryListener<? super K, ? super V> listener = registration.getCacheEntryListener();
         if (listener instanceof CacheEntryCreatedListener) {
            ((CacheEntryCreatedListener) listener).onCreated(iterable);
         }
      }
   }

   @CacheEntryModified
   public void handleCacheEntryModifiedEvent(CacheEntryModifiedEvent<K, V> e) {
      RICacheEntryEvent<K, V> jsrEvent = new RICacheEntryEvent<K, V>(cache, e.getKey(),
               e.getValue());
      List<CacheEntryEvent<K, V>> events = new ArrayList<CacheEntryEvent<K, V>>();
      events.add(jsrEvent);

      for (CacheEntryListenerRegistration<? super K, ? super V> registration : cache.getListeners()
               .values()) {
         CacheEntryEventFilter<? super K, ? super V> filter = registration.getCacheEntryFilter();
         Iterable<CacheEntryEvent<K, V>> iterable = filter == null ? events
                  : new RICacheEntryEventFilteringIterable<K, V>(events, filter);

         CacheEntryListener<? super K, ? super V> listener = registration.getCacheEntryListener();
         if (listener instanceof CacheEntryUpdatedListener) {
            ((CacheEntryUpdatedListener) listener).onUpdated(iterable);
         }
      }
   }

   @CacheEntryRemoved
   public void handleCacheEntryRemovedEvent(CacheEntryRemovedEvent<K, V> e) {
      RICacheEntryEvent<K, V> jsrEvent = new RICacheEntryEvent<K, V>(cache, e.getKey(),
               e.getValue());
      List<CacheEntryEvent<K, V>> events = new ArrayList<CacheEntryEvent<K, V>>();
      events.add(jsrEvent);

      for (CacheEntryListenerRegistration<? super K, ? super V> registration : cache.getListeners()
               .values()) {
         CacheEntryEventFilter<? super K, ? super V> filter = registration.getCacheEntryFilter();
         Iterable<CacheEntryEvent<K, V>> iterable = filter == null ? events
                  : new RICacheEntryEventFilteringIterable<K, V>(events, filter);

         CacheEntryListener<? super K, ? super V> listener = registration.getCacheEntryListener();
         if (listener instanceof CacheEntryRemovedListener) {
            ((CacheEntryRemovedListener) listener).onRemoved(iterable);
         }
      }
   }

   @CacheEntryVisited
   public void handleCacheEntryVisitedEvent(CacheEntryVisitedEvent<K, V> e) {
      RICacheEntryEvent<K, V> jsrEvent = new RICacheEntryEvent<K, V>(cache, e.getKey(),
               e.getValue());
      List<CacheEntryEvent<K, V>> events = new ArrayList<CacheEntryEvent<K, V>>();
      events.add(jsrEvent);

      for (CacheEntryListenerRegistration<? super K, ? super V> registration : cache.getListeners()
               .values()) {
         CacheEntryEventFilter<? super K, ? super V> filter = registration.getCacheEntryFilter();
         Iterable<CacheEntryEvent<K, V>> iterable = filter == null ? events
                  : new RICacheEntryEventFilteringIterable<K, V>(events, filter);

         CacheEntryListener<? super K, ? super V> listener = registration.getCacheEntryListener();
         if (listener instanceof CacheEntryReadListener) {
            ((CacheEntryReadListener) listener).onRead(iterable);
         }
      }
   }

}