package com.yahoo.dtf.util;

import java.util.Iterator;

/**
 * A simple implementation of a peekable Iterator which allows you to peek one
 * element into the iterator and then retrieve it on the next next() call.
 * 
 * @author rlgomes
 * @param <E>
 */
public class PeekableIterator<E> implements Iterator<E> {
   
    Iterator<E> iterator = null;
    E peeked = null;
    
    public PeekableIterator(Iterator<E> iterator) {
        this.iterator = iterator;
    }

    public boolean hasNext() {
        return iterator.hasNext() || peeked != null;
    }
    
    public E next() {
        E result = null;
        if ( peeked != null ) { 
            result = peeked;
            peeked = null;
        } else { 
            result = iterator.next();
        }
        return result;
    }
    
    public void remove() {
        if ( peeked != null ) { 
            peeked = null;
        } else { 
            iterator.remove();
        }
    }
    
    /**
     * return the next available element without moving the iterator forward.
     * @return
     */
    public E peek() { 
        if ( peeked == null ) 
            peeked = iterator.next();
        
        return peeked;
    }
}
