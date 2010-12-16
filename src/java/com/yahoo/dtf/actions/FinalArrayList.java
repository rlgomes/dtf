package com.yahoo.dtf.actions;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This classes sole purpose is to guarantee that no one will attempt to modify
 * the ArrayList that is returned. It basically implements a read-only ArrayList
 * and I've called it final since that is the keyword for something that is 
 * constant in Java and can't be modified.
 * 
 * @author rlgomes
 * @param <T>
 */
class FinalArrayList<T> extends ArrayList<T> { 
    private static final long serialVersionUID = 1L;
    
    private static final String MESSAGE = 
                               "You can not modify the children ArrayList.";
    
    public FinalArrayList(ArrayList<T> children) { 
        super.addAll(children);
    } 
    
    @Override
    public void add(int index, T element) {
        throw new RuntimeException(MESSAGE);
    }
    
    @Override
    public boolean add(T e) {
        throw new RuntimeException(MESSAGE);
    }
    
    @Override
    public boolean addAll(java.util.Collection<? extends T> c) {
        throw new RuntimeException(MESSAGE);
    }
    
    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new RuntimeException(MESSAGE);
    }
   
    @Override
    public T remove(int index) {
        throw new RuntimeException(MESSAGE);
    }
    
    @Override
    public boolean remove(Object o) {
        throw new RuntimeException(MESSAGE);
    }
    
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new RuntimeException(MESSAGE);
    }
}