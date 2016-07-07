package com.edwardharker.multiitemadapter;

import android.util.SparseArray;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test {@link ImmutableSparseArray}
 */
public class ImmutableSparseArrayTest {

    private SparseArray<String> mWrappedSparseArray;
    private ImmutableSparseArray<String> mImmutableSparseArray;

    @Before
    public void setup() {
        mWrappedSparseArray = mock(SparseArray.class);
        mImmutableSparseArray = new ImmutableSparseArray(mWrappedSparseArray);
    }

    @Test
    public void testGet() throws Exception {
        when(mWrappedSparseArray.get(0)).thenReturn("Hello");
        assertEquals("Hello", mImmutableSparseArray.get(0));
        verify(mWrappedSparseArray, times(1)).get(eq(0));
    }

    @Test
    public void testGetWithValueIfKeyNotFound() throws Exception {
        when(mWrappedSparseArray.get(0, "World")).thenReturn("Hello");
        assertEquals("Hello", mImmutableSparseArray.get(0, "World"));
        verify(mWrappedSparseArray, times(1)).get(eq(0), eq("World"));
    }

    @Test
    public void testSize() throws Exception {
        when(mWrappedSparseArray.size()).thenReturn(22);
        assertEquals(22, mImmutableSparseArray.size());
        verify(mWrappedSparseArray, times(1)).size();
    }

    @Test
    public void testKeyAt() throws Exception {
        when(mWrappedSparseArray.keyAt(0)).thenReturn(22);
        assertEquals(22, mImmutableSparseArray.keyAt(0));
        verify(mWrappedSparseArray, times(1)).keyAt(eq(0));
    }

    @Test
    public void testValueAt() throws Exception {
        when(mWrappedSparseArray.valueAt(0)).thenReturn("Hello");
        assertEquals("Hello", mImmutableSparseArray.valueAt(0));
        verify(mWrappedSparseArray, times(1)).valueAt(eq(0));
    }

    @Test
    public void testIndexOfKey() throws Exception {
        when(mWrappedSparseArray.indexOfKey(0)).thenReturn(22);
        assertEquals(22, mImmutableSparseArray.indexOfKey(0));
        verify(mWrappedSparseArray, times(1)).indexOfKey(eq(0));
    }

    @Test
    public void testIndexOfValue() throws Exception {
        when(mWrappedSparseArray.indexOfValue("Hello")).thenReturn(22);
        assertEquals(22, mImmutableSparseArray.indexOfValue("Hello"));
        verify(mWrappedSparseArray, times(1)).indexOfValue(eq("Hello"));
    }

}