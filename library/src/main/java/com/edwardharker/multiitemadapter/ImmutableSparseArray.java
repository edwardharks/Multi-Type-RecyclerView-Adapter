package com.edwardharker.multiitemadapter;

import android.util.SparseArray;

/**
 * Wraps a {@link android.util.SparseArray} making it immutable (exposing only the get methods)
 */
final class ImmutableSparseArray<T> {

    private final SparseArray<T> mSparseArray;

    /**
     * Create a new immutable sparse array using the given sparse array
     *
     * @param sparseArray the sparse array to make immutable
     */
    public ImmutableSparseArray(SparseArray<T> sparseArray) {
        mSparseArray = sparseArray;
    }

    /**
     * Gets the Object mapped from the specified key, or <code>null</code>
     * if no such mapping has been made.
     */
    public T get(int key) {
        return mSparseArray.get(key);
    }

    /**
     * Gets the Object mapped from the specified key, or the specified Object
     * if no such mapping has been made.
     */
    @SuppressWarnings("unchecked")
    public T get(int key, T valueIfKeyNotFound) {
        return mSparseArray.get(key, valueIfKeyNotFound);
    }

    /**
     * Returns the number of key-value mappings that this SparseArray
     * currently stores.
     */
    public int size() {
        return mSparseArray.size();
    }


    /**
     * Given an index in the range <code>0...size()-1</code>, returns
     * the key from the <code>index</code>th key-value mapping that this
     * SparseArray stores.
     * <p/>
     * <p>The keys corresponding to indices in ascending order are guaranteed to
     * be in ascending order, e.g., <code>keyAt(0)</code> will return the
     * smallest key and <code>keyAt(size()-1)</code> will return the largest
     * key.</p>
     */
    public int keyAt(int index) {
        return mSparseArray.keyAt(index);
    }

    /**
     * Given an index in the range <code>0...size()-1</code>, returns
     * the value from the <code>index</code>th key-value mapping that this
     * SparseArray stores.
     * <p/>
     * <p>The values corresponding to indices in ascending order are guaranteed
     * to be associated with keys in ascending order, e.g.,
     * <code>valueAt(0)</code> will return the value associated with the
     * smallest key and <code>valueAt(size()-1)</code> will return the value
     * associated with the largest key.</p>
     */
    @SuppressWarnings("unchecked")
    public T valueAt(int index) {
        return mSparseArray.valueAt(index);
    }

    /**
     * Returns the index for which {@link #keyAt} would return the
     * specified key, or a negative number if the specified
     * key is not mapped.
     */
    public int indexOfKey(int key) {
        return mSparseArray.indexOfKey(key);
    }

    /**
     * Returns an index for which {@link #valueAt} would return the
     * specified key, or a negative number if no keys map to the
     * specified value.
     * <p>Beware that this is a linear search, unlike lookups by key,
     * and that multiple keys can map to the same value and this will
     * find only one of them.
     * <p>Note also that unlike most collections' {@code indexOf} methods,
     * this method compares values using {@code ==} rather than {@code equals}.
     */
    public int indexOfValue(T value) {
        return mSparseArray.indexOfValue(value);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * <p>This implementation composes a string by iterating over its mappings. If
     * this map contains itself as a value, the string "(this Map)"
     * will appear in its place.
     */
    @Override
    public String toString() {
        return mSparseArray.toString();
    }

}