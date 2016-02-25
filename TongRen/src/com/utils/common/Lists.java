package com.utils.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Provides static methods for creating {@code List} instances easily, and other
 * utility methods for working with lists.
 */
public final class Lists {

    /** This class is never instantiated */
    public Lists() {
    }

    /**
     * Creates an empty {@code ArrayList} instance.
     * <p>
     * <b>Note:</b> if you only need an <i>immutable</i> empty List, use
     * {@link Collections#emptyList} instead.
     * 
     * @return a newly-created, initially-empty {@code ArrayList}
     */
    public static final <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }

    /**
     * Creates an empty {@code LinkedList} instance.
     * <p>
     * <b>Note:</b> if you only need an <i>immutable</i> empty List, use
     * {@link Collections#emptyList} instead.
     * 
     * @return a newly-created, initially-empty {@code LinkedList}
     */
    public static final <E> LinkedList<E> newLinkedList() {
        return new LinkedList<E>();
    }

}
