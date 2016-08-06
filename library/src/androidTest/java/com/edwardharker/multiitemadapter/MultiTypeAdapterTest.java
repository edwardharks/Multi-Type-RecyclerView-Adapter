package com.edwardharker.multiitemadapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
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

    private MultiTypeBinder itemTypeFooterBinder;
    private MultiTypeCreator itemTypeFooterCreator;
    private ViewType itemTypeFooterViewType;

    private ThreadHelper alwaysUiThreadHelper;
    private ThreadHelper neverUiThreadHelper;

    @Before
    public void setup() {
        itemTypeOneBinder = mock(MultiTypeBinder.class);
        itemTypeOneCreator = mock(MultiTypeCreator.class);
        itemTypeOneViewType = mock(ViewType.class);
        when(itemTypeOneBinder.getViewType()).thenReturn(itemTypeOneViewType);
        when(itemTypeOneCreator.getViewType()).thenReturn(itemTypeOneViewType);
        when(itemTypeOneCreator.onCreateViewHolder(any(ViewGroup.class)))
                .thenReturn(new MockViewHolder(mock(View.class)));
        when(itemTypeOneViewType.getType()).thenReturn(1);

        itemTypeTwoBinder = mock(MultiTypeBinder.class);
        itemTypeTwoCreator = mock(MultiTypeCreator.class);
        itemTypeTwoViewType = mock(ViewType.class);
        when(itemTypeTwoBinder.getViewType()).thenReturn(itemTypeTwoViewType);
        when(itemTypeTwoCreator.getViewType()).thenReturn(itemTypeTwoViewType);
        when(itemTypeTwoCreator.onCreateViewHolder(any(ViewGroup.class)))
                .thenReturn(new MockViewHolder(mock(View.class)));
        when(itemTypeTwoViewType.getType()).thenReturn(2);

        itemTypeFooterBinder = mock(MultiTypeBinder.class);
        itemTypeFooterCreator = mock(MultiTypeCreator.class);
        itemTypeFooterViewType = mock(ViewType.class);
        when(itemTypeFooterBinder.getViewType()).thenReturn(itemTypeFooterViewType);
        when(itemTypeFooterCreator.getViewType()).thenReturn(itemTypeFooterViewType);
        when(itemTypeFooterCreator.onCreateViewHolder(any(ViewGroup.class)))
                .thenReturn(new MockViewHolder(mock(View.class)));
        when(itemTypeFooterViewType.getType()).thenReturn(3);

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
    public void testClear() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(alwaysUiThreadHelper)
                .build();

        adapter.add(itemTypeOneBinder);
        adapter.add(itemTypeTwoBinder);
        adapter.setFooter(itemTypeFooterBinder);

        assertEquals(3, adapter.getItemCount());

        adapter.clear();

        assertEquals(0, adapter.getItemCount());
        assertNull(adapter.getFooter());

    }

    @Test
    public void testClearNotOnUiThread() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(neverUiThreadHelper)
                .build();

        try {
            adapter.clear();
            fail();
        } catch (IllegalStateException expected) {
        }

    }

    @Test
    public void testAddAtPosition() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(alwaysUiThreadHelper)
                .build();

        adapter.add(0, itemTypeOneBinder);
        assertEquals(itemTypeOneBinder, adapter.getBinder(0));
        assertEquals(1, adapter.getItemCount());

        adapter.add(0, itemTypeTwoBinder);
        assertEquals(itemTypeTwoBinder, adapter.getBinder(0));
        assertEquals(2, adapter.getItemCount());

        adapter.add(0, itemTypeOneBinder);
        assertEquals(itemTypeOneBinder, adapter.getBinder(0));
        assertEquals(3, adapter.getItemCount());

    }

    @Test
    public void testAddAtPositionNotOnUiThread() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(neverUiThreadHelper)
                .build();

        try {
            adapter.add(0, itemTypeOneBinder);
            fail();
        } catch (IllegalStateException expected) {
        }

    }

    @Test
    public void testAddAtInvalidPosition() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(alwaysUiThreadHelper)
                .build();

        try {
            adapter.add(-1, itemTypeOneBinder);
            fail();
        } catch (IndexOutOfBoundsException expected) {
        }

        try {
            adapter.add(1, itemTypeOneBinder);
            fail();
        } catch (IndexOutOfBoundsException expected) {
        }

    }

    @Test
    public void testRemove() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(alwaysUiThreadHelper)
                .build();

        adapter.add(itemTypeOneBinder);
        adapter.add(itemTypeTwoBinder);
        adapter.add(itemTypeOneBinder);
        adapter.add(itemTypeTwoBinder);

        assertEquals(4, adapter.getItemCount());
        assertEquals(itemTypeOneBinder, adapter.getBinder(0));
        assertEquals(itemTypeTwoBinder, adapter.getBinder(1));
        assertEquals(itemTypeOneBinder, adapter.getBinder(2));
        assertEquals(itemTypeTwoBinder, adapter.getBinder(3));

        adapter.remove(1);
        assertEquals(3, adapter.getItemCount());
        assertEquals(itemTypeOneBinder, adapter.getBinder(0));
        assertEquals(itemTypeOneBinder, adapter.getBinder(1));
        assertEquals(itemTypeTwoBinder, adapter.getBinder(2));

        adapter.remove(2);
        assertEquals(2, adapter.getItemCount());
        assertEquals(itemTypeOneBinder, adapter.getBinder(0));
        assertEquals(itemTypeOneBinder, adapter.getBinder(1));

        adapter.remove(0);
        assertEquals(1, adapter.getItemCount());
        assertEquals(itemTypeOneBinder, adapter.getBinder(0));

    }

    @Test
    public void testRemoveAtInvalidPosition() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(alwaysUiThreadHelper)
                .build();

        try {
            adapter.remove(-1);
            fail();
        } catch (IndexOutOfBoundsException expected) {
        }

        try {
            adapter.remove(1);
            fail();
        } catch (IndexOutOfBoundsException expected) {
        }

    }

    @Test
    public void testRemoveNotOnUiThread() throws Exception {

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
    public void testRemoveAllOfType() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(alwaysUiThreadHelper)
                .build();

        adapter.add(itemTypeOneBinder);
        adapter.add(itemTypeTwoBinder);
        adapter.add(itemTypeOneBinder);
        adapter.add(itemTypeTwoBinder);

        assertEquals(4, adapter.getItemCount());
        assertEquals(itemTypeOneBinder, adapter.getBinder(0));
        assertEquals(itemTypeTwoBinder, adapter.getBinder(1));
        assertEquals(itemTypeOneBinder, adapter.getBinder(2));
        assertEquals(itemTypeTwoBinder, adapter.getBinder(3));

        adapter.removeAllOf(itemTypeOneViewType);
        assertEquals(2, adapter.getItemCount());
        assertEquals(itemTypeTwoBinder, adapter.getBinder(0));
        assertEquals(itemTypeTwoBinder, adapter.getBinder(1));

    }

    @Test
    public void testRemoveAllOfTypeNotOnUiThread() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(neverUiThreadHelper)
                .build();

        try {
            adapter.removeAllOf(itemTypeOneViewType);
            fail();
        } catch (IllegalStateException expected) {
        }

    }

    @Test
    public void testGetBinders() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(alwaysUiThreadHelper)
                .build();

        adapter.add(itemTypeOneBinder);
        adapter.add(itemTypeTwoBinder);
        adapter.add(itemTypeOneBinder);
        adapter.add(itemTypeTwoBinder);

        List<MultiTypeBinder> result = adapter.getBinders();
        assertEquals(4, result.size());
        assertEquals(itemTypeOneBinder, result.get(0));
        assertEquals(itemTypeTwoBinder, result.get(1));
        assertEquals(itemTypeOneBinder, result.get(2));
        assertEquals(itemTypeTwoBinder, result.get(3));

    }

    @Test
    public void testGetBindersNotOnUiThread() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(neverUiThreadHelper)
                .build();

        try {
            adapter.getBinders();
            fail();
        } catch (IllegalStateException expected) {
        }

    }

    @Test
    public void testGetBindersOfType() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(alwaysUiThreadHelper)
                .build();

        adapter.add(itemTypeOneBinder);
        adapter.add(itemTypeTwoBinder);
        adapter.add(itemTypeOneBinder);
        adapter.add(itemTypeTwoBinder);

        List<MultiTypeBinder> typeOneResult = adapter.getBinders(itemTypeOneViewType);
        assertEquals(2, typeOneResult.size());
        assertEquals(itemTypeOneBinder, typeOneResult.get(0));
        assertEquals(itemTypeOneBinder, typeOneResult.get(1));

        List<MultiTypeBinder> typeTwoResult = adapter.getBinders(itemTypeTwoViewType);
        assertEquals(2, typeTwoResult.size());
        assertEquals(itemTypeTwoBinder, typeTwoResult.get(0));
        assertEquals(itemTypeTwoBinder, typeTwoResult.get(1));

    }

    @Test
    public void testGetBindersOfTypeNotOnUiThread() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(neverUiThreadHelper)
                .build();

        try {
            adapter.getBinders(itemTypeOneViewType);
            fail();
        } catch (IllegalStateException expected) {
        }

    }

    @Test
    public void testSetFooter() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(alwaysUiThreadHelper)
                .build();

        adapter.add(itemTypeOneBinder);
        adapter.add(itemTypeTwoBinder);
        adapter.setFooter(itemTypeFooterBinder);
        adapter.add(itemTypeOneBinder);
        adapter.add(itemTypeTwoBinder);

        assertEquals(5, adapter.getItemCount());
        assertEquals(itemTypeOneBinder, adapter.getBinder(0));
        assertEquals(itemTypeTwoBinder, adapter.getBinder(1));
        assertEquals(itemTypeOneBinder, adapter.getBinder(2));
        assertEquals(itemTypeTwoBinder, adapter.getBinder(3));
        assertEquals(itemTypeFooterBinder, adapter.getBinder(4));
        assertEquals(itemTypeFooterBinder, adapter.getFooter());

        adapter.setFooter(itemTypeFooterBinder);

        assertEquals(5, adapter.getItemCount());

        adapter.add(itemTypeOneBinder);
        assertEquals(itemTypeOneBinder, adapter.getBinder(4));
        assertEquals(itemTypeFooterBinder, adapter.getBinder(5));
        assertEquals(itemTypeFooterBinder, adapter.getFooter());

        adapter.setFooter(itemTypeFooterBinder);

        assertEquals(6, adapter.getItemCount());

        adapter.add(adapter.getItemCount(), itemTypeTwoBinder);
        assertEquals(itemTypeTwoBinder, adapter.getBinder(5));
        assertEquals(itemTypeFooterBinder, adapter.getBinder(6));
        assertEquals(itemTypeFooterBinder, adapter.getFooter());

        adapter.setFooter(itemTypeFooterBinder);

        assertEquals(7, adapter.getItemCount());

        adapter.addAll(Arrays.asList(itemTypeOneBinder, itemTypeTwoBinder));
        assertEquals(itemTypeOneBinder, adapter.getBinder(6));
        assertEquals(itemTypeTwoBinder, adapter.getBinder(7));
        assertEquals(itemTypeFooterBinder, adapter.getBinder(8));
        assertEquals(itemTypeFooterBinder, adapter.getFooter());

        assertEquals(9, adapter.getItemCount());

        adapter.clear();

        adapter.setFooter(itemTypeFooterBinder);
        assertEquals(1, adapter.getItemCount());
        assertEquals(itemTypeFooterBinder, adapter.getBinder(0));
        assertEquals(itemTypeFooterBinder, adapter.getFooter());

    }

    @Test
    public void testSetFooterNotOnUiThread() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(neverUiThreadHelper)
                .build();

        try {
            adapter.setFooter(itemTypeOneBinder);
            fail();
        } catch (IllegalStateException expected) {
        }

    }

    @Test
    public void testGetFooter() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(alwaysUiThreadHelper)
                .build();

        adapter.setFooter(itemTypeFooterBinder);
        assertEquals(itemTypeFooterBinder, adapter.getFooter());

    }

    @Test
    public void testGetFooterNotOnUiThread() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(neverUiThreadHelper)
                .build();

        try {
            adapter.getFooter();
            fail();
        } catch (IllegalStateException expected) {
        }

    }

    @Test
    public void testClearFooter() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(alwaysUiThreadHelper)
                .build();

        adapter.add(itemTypeOneBinder);
        adapter.add(itemTypeTwoBinder);
        adapter.setFooter(itemTypeFooterBinder);
        adapter.add(itemTypeOneBinder);
        adapter.add(itemTypeTwoBinder);

        assertEquals(5, adapter.getItemCount());
        assertEquals(itemTypeOneBinder, adapter.getBinder(0));
        assertEquals(itemTypeTwoBinder, adapter.getBinder(1));
        assertEquals(itemTypeOneBinder, adapter.getBinder(2));
        assertEquals(itemTypeTwoBinder, adapter.getBinder(3));
        assertEquals(itemTypeFooterBinder, adapter.getBinder(4));
        assertEquals(itemTypeFooterBinder, adapter.getFooter());

        adapter.clearFooter();

        assertNull(adapter.getFooter());

        assertEquals(4, adapter.getItemCount());
        assertEquals(itemTypeOneBinder, adapter.getBinder(0));
        assertEquals(itemTypeTwoBinder, adapter.getBinder(1));
        assertEquals(itemTypeOneBinder, adapter.getBinder(2));
        assertEquals(itemTypeTwoBinder, adapter.getBinder(3));

    }


    @Test
    public void testClearFooterNotOnUiThread() throws Exception {

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .threadHelper(neverUiThreadHelper)
                .build();

        try {
            adapter.clearFooter();
            fail();
        } catch (IllegalStateException expected) {
        }

    }

    private static final class MockViewHolder extends RecyclerView.ViewHolder implements MultiTypeViewHolder {

        public MockViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onViewAttachedToWindow() {

        }

        @Override
        public void onViewDetachedToWindow() {

        }
    }

}
