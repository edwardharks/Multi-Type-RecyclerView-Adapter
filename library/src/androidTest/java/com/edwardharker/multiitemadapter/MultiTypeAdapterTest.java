package com.edwardharker.multiitemadapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link MultiTypeAdapter}
 */
public class MultiTypeAdapterTest {

    private MultiTypeBinder itemTypeOneBinder;
    private MultiTypeCreator itemTypeOneCreator;
    private ViewType itemTypeOneViewType;

    private MultiTypeBinder itemTypeTwoBinder;
    private MultiTypeCreator itemTypeTwoCreator;
    private ViewType itemTypeTwoViewType;

    private ThreadHelper alwaysUiThreadHelper;
    private ThreadHelper neverUiThreadHelper;

    @Before
    public void setup() {
        itemTypeOneBinder = mock(MultiTypeBinder.class);
        itemTypeOneCreator = mock(MultiTypeCreator.class);
        itemTypeOneViewType = mock(ViewType.class);
        when(itemTypeOneBinder.getViewType()).thenReturn(itemTypeOneViewType);
        when(itemTypeOneCreator.getViewType()).thenReturn(itemTypeOneViewType);
        when(itemTypeOneViewType.getType()).thenReturn(1);

        itemTypeTwoBinder = mock(MultiTypeBinder.class);
        itemTypeTwoCreator = mock(MultiTypeCreator.class);
        itemTypeTwoViewType = mock(ViewType.class);
        when(itemTypeTwoBinder.getViewType()).thenReturn(itemTypeTwoViewType);
        when(itemTypeTwoCreator.getViewType()).thenReturn(itemTypeTwoViewType);
        when(itemTypeTwoViewType.getType()).thenReturn(2);

        alwaysUiThreadHelper = mock(ThreadHelper.class);
        when(alwaysUiThreadHelper.isUiThread()).thenReturn(true);

        neverUiThreadHelper = mock(ThreadHelper.class);
        when(neverUiThreadHelper.isUiThread()).thenReturn(false);
    }

    @Test
    public void testAddItemNotOnUiThread() throws Exception {


        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .addCreator(itemTypeOneCreator)
                .threadHelper(neverUiThreadHelper)
                .build();

        try {
            adapter.add(itemTypeOneBinder);
            fail();
        } catch (IllegalStateException expected) {
        }

    }

    @Test
    public void testAddAllItemsNotOnUiThread() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .addCreator(itemTypeOneCreator)
                .threadHelper(neverUiThreadHelper)
                .build();

        try {
            adapter.addAll(Arrays.asList(itemTypeOneBinder, itemTypeOneBinder));
            fail();
        } catch (IllegalStateException expected) {
        }

    }

    @Test
    public void testGetItemCount() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .addCreator(itemTypeOneCreator)
                .threadHelper(alwaysUiThreadHelper)
                .build();

        adapter.add(itemTypeOneBinder);
        assertEquals(1, adapter.getItemCount());

        adapter.add(itemTypeOneBinder);
        assertEquals(2, adapter.getItemCount());

        adapter.addAll(Arrays.asList(itemTypeOneBinder, itemTypeOneBinder));

        assertEquals(4, adapter.getItemCount());

    }

    @Test
    public void testGetItemViewType() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .addCreator(itemTypeOneCreator)
                .addCreator(itemTypeTwoCreator)
                .threadHelper(alwaysUiThreadHelper)
                .build();

        adapter.add(itemTypeOneBinder);
        adapter.add(itemTypeTwoBinder);
        adapter.add(itemTypeOneBinder);
        adapter.add(itemTypeTwoBinder);

        assertEquals(itemTypeOneViewType.getType(), adapter.getItemViewType(0));
        assertEquals(itemTypeTwoViewType.getType(), adapter.getItemViewType(1));
        assertEquals(itemTypeOneViewType.getType(), adapter.getItemViewType(2));
        assertEquals(itemTypeTwoViewType.getType(), adapter.getItemViewType(3));

    }

    @Test
    public void testBindViewHolder() throws Exception {
        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .addCreator(itemTypeOneCreator)
                .addCreator(itemTypeTwoCreator)
                .threadHelper(alwaysUiThreadHelper)
                .build();

        adapter.addAll(Arrays.asList(
                itemTypeOneBinder, itemTypeOneBinder, itemTypeTwoBinder, itemTypeTwoBinder));

        adapter.onBindViewHolder(null, 0);
        adapter.onBindViewHolder(null, 1);
        adapter.onBindViewHolder(null, 3);
        adapter.onBindViewHolder(null, 3);

        verify(itemTypeOneBinder, times(2)).onBindViewHolder(any(RecyclerView.ViewHolder.class));
        verify(itemTypeTwoBinder, times(2)).onBindViewHolder(any(RecyclerView.ViewHolder.class));
    }

    @Test
    public void testCreateViewHolder() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .addCreator(itemTypeOneCreator)
                .addCreator(itemTypeTwoCreator)
                .threadHelper(alwaysUiThreadHelper)
                .build();

        adapter.onCreateViewHolder(null, itemTypeOneViewType.getType());
        adapter.onCreateViewHolder(null, itemTypeTwoViewType.getType());

        verify(itemTypeTwoCreator, times(1)).onCreateViewHolder(any(ViewGroup.class));
        verify(itemTypeTwoCreator, times(1)).onCreateViewHolder(any(ViewGroup.class));

    }

    @Test
    public void testCreateViewHolderUnknownViewType() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(alwaysUiThreadHelper)
                .build();

        try {
            adapter.onCreateViewHolder(null, itemTypeOneViewType.getType());
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void testGetBinder() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(alwaysUiThreadHelper)
                .build();

        adapter.add(itemTypeOneBinder);
        adapter.add(itemTypeTwoBinder);

        assertEquals(itemTypeOneBinder, adapter.getBinder(0));
        assertEquals(itemTypeTwoBinder, adapter.getBinder(1));

        assertEquals(null, adapter.getBinder(-1));
        assertEquals(null, adapter.getBinder(2));

    }

    @Test
    public void testCLear() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(alwaysUiThreadHelper)
                .build();

        adapter.add(itemTypeOneBinder);
        adapter.add(itemTypeTwoBinder);

        assertEquals(2, adapter.getItemCount());
        adapter.clear();
        assertEquals(0, adapter.getItemCount());

    }

    @Test
    public void testRemoveAtPosition() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(alwaysUiThreadHelper)
                .build();

        adapter.add(itemTypeOneBinder);
        adapter.add(itemTypeTwoBinder);
        adapter.add(itemTypeOneBinder);

        assertEquals(3, adapter.getItemCount());
        assertEquals(itemTypeOneBinder, adapter.getBinder(0));
        assertEquals(itemTypeTwoBinder, adapter.getBinder(1));
        assertEquals(itemTypeOneBinder, adapter.getBinder(2));

        adapter.remove(1);

        assertEquals(2, adapter.getItemCount());
        assertEquals(itemTypeOneBinder, adapter.getBinder(0));
        assertEquals(itemTypeOneBinder, adapter.getBinder(1));

    }

    @Test
    public void testRemoveAtPositionNotOnUiThread() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(neverUiThreadHelper)
                .build();

        try {
            adapter.remove(0);
            fail();
        } catch (IllegalStateException expected) {
        }

    }

    @Test
    public void testRemoveAtPositionOutsideSize() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(alwaysUiThreadHelper)
                .build();

        adapter.add(itemTypeOneBinder);
        adapter.add(itemTypeTwoBinder);
        adapter.add(itemTypeOneBinder);

        assertEquals(3, adapter.getItemCount());
        assertEquals(itemTypeOneBinder, adapter.getBinder(0));
        assertEquals(itemTypeTwoBinder, adapter.getBinder(1));
        assertEquals(itemTypeOneBinder, adapter.getBinder(2));

        adapter.remove(3);

        assertEquals(3, adapter.getItemCount());
        assertEquals(itemTypeOneBinder, adapter.getBinder(0));
        assertEquals(itemTypeTwoBinder, adapter.getBinder(1));
        assertEquals(itemTypeOneBinder, adapter.getBinder(2));

    }


}