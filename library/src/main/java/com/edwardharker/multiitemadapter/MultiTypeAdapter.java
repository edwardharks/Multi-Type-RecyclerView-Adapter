package com.edwardharker.multiitemadapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Adapter to use with MultiTypeBinders for creating a heterogeneous list
 */
public final class MultiTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * The items in the adapter
     */
    private final List<MultiTypeBinder> mBinders = new ArrayList<>();

    /**
     * Maps view types to the view types creator.
     */
    private final ImmutableSparseArray<MultiTypeCreator> mViewCreators;

    private final ThreadHelper mThreadHelper;

    /**
     * The footer of the adapter. This is simply the last item in the adapter and is maintained
     * as the last item when add() is called.
     */
    private MultiTypeBinder mFooter;

    private MultiTypeAdapter(Builder builder) {
        mThreadHelper = builder.mThreadHelper;
        SparseArray<MultiTypeCreator> creators = new SparseArray<>(builder.mCreators.size());
        for (MultiTypeCreator creator : builder.mCreators) {
            creators.put(creator.getViewType().getType(), creator);
        }
        mViewCreators = new ImmutableSparseArray<>(creators);
    }

    /**
     * Add an item to the adapter
     *
     * @param binder the item to add
     * @throws IllegalStateException if not called from the UI thread
     */
    public void add(@NonNull MultiTypeBinder binder) {
        checkMainThread();
        checkNonNull(binder, "binder");
        if (mFooter != null) {
            mBinders.add(mBinders.size() - 1, binder);
        } else {
            mBinders.add(binder);
        }
        // TODO: replace with notifyItemChanged/inserted
        notifyDataSetChanged();
    }

    /**
     * Add an item to the adapter at the position
     *
     * @param position the position to add the binder at
     * @param binder   the binder to add
     * @throws IllegalStateException     if not called from the UI thread
     * @throws IndexOutOfBoundsException if {@code position < 0 || position > size()}
     */
    public void add(int position, @NonNull MultiTypeBinder binder) {
        checkMainThread();
        checkNonNull(binder, "binder");
        if (position < 0 || position > mBinders.size()) {
            throw new IndexOutOfBoundsException("position: " + position + " invalid. " +
                    "Item count is " + getItemCount());
        }
        if (mFooter != null && position == mBinders.size()) {
            position--;
        }
        mBinders.add(position, binder);
        notifyItemInserted(position);
    }

    /**
     * Add all the items to the adapter
     *
     * @param binders the binders to add
     * @throws IllegalStateException if not called from the UI thread
     */
    public void addAll(@NonNull Collection<MultiTypeBinder> binders) {
        checkMainThread();
        checkNonNull(binders, "binders");
        if (mFooter != null) {
            mBinders.addAll(mBinders.size() - 1, binders);
        } else {
            mBinders.addAll(binders);
        }
        // TODO: replace with notifyRangeChanged
        notifyDataSetChanged();
    }

    /**
     * Add the binder to the end of the adapter. Any call to add() will insert before the footer.
     * Calling setFooter() again will overwrite the old footer
     *
     * @param binder the binder to add as the footer.
     * @throws IllegalStateException if not called from the UI thread
     */
    public void setFooter(@NonNull MultiTypeBinder binder) {
        checkMainThread();
        checkNonNull(binder, "binder");
        if (mFooter != null) {
            mBinders.remove(mBinders.size() - 1);
        }
        mBinders.add(binder);
        mFooter = binder;
        // TODO: replace with notifyItemInserted
        notifyDataSetChanged();
    }

    /**
     * Clear the footer from the adapter if one has been set
     *
     * @throws IllegalStateException if not called from the UI thread
     */
    public void clearFooter() {
        checkMainThread();
        if (mFooter != null) {
            mBinders.remove(mBinders.size() - 1);
            mFooter = null;
            // TODO: replace with notifyItemChange
            notifyDataSetChanged();
        }
    }

    /**
     * Get the footer
     *
     * @return the footer or null if no footer has been set
     * @throws IllegalStateException if not called from the UI thread
     */
    @Nullable
    public MultiTypeBinder getFooter() {
        checkMainThread();
        return mFooter;
    }

    /**
     * Remove the item at position.
     * Note if the there is a footer set and {@code position == getItemCount() - 1} the footer will be removed
     *
     * @param position the position to remove
     * @throws IllegalStateException     if not called from the UI thread
     * @throws IndexOutOfBoundsException if {@code position < 0 || position > size()}
     */
    public void remove(int position) {
        checkMainThread();
        if (position < 0 || position > mBinders.size()) {
            throw new IndexOutOfBoundsException("position: " + position + " invalid. " +
                    "Item count is " + getItemCount());
        }
        if (mFooter != null && position == mBinders.size() - 1) {
            clearFooter();
        } else {
            mBinders.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * Remove all of the items of the view type
     *
     * @param viewType the view type to remove all items of
     * @throws IllegalStateException if not called from the UI thread
     */
    public void removeAllOf(@NonNull ViewType viewType) {
        checkMainThread();
        checkNonNull(viewType, "viewType");
        if (mFooter != null && viewType.equals(mFooter.getViewType())) {
            clearFooter();
        }
        Iterator<MultiTypeBinder> iterator = mBinders.iterator();
        while (iterator.hasNext()) {
            MultiTypeBinder binder = iterator.next();
            if (binder.getViewType().equals(viewType)) {
                iterator.remove();
            }
        }
        notifyDataSetChanged();
    }

    /**
     * Clear all items from the adapter
     *
     * @throws IllegalStateException if not called from the UI thread
     */
    public void clear() {
        checkMainThread();
        mBinders.clear();
        mFooter = null;
        notifyDataSetChanged();
    }

    /**
     * Get all the binders in the adapter
     *
     * @return the binders in the adapter.
     * Never null, will be an empty list if there isn't anything in the adapter
     * @throws IllegalStateException if not called from the UI thread
     */
    @NonNull
    public List<MultiTypeBinder> getBinders() {
        checkMainThread();
        return Collections.unmodifiableList(mBinders);
    }

    /**
     * Get all of the binders of a particular view type in the adapter
     *
     * @param type the type of binders to get
     * @return the binders in the adapter of the given type.
     * Never null, will be an empty list if there isn't anything of the given type
     * @throws IllegalStateException if not called from the UI thread
     */
    @NonNull
    public List<MultiTypeBinder> getBinders(@Nullable ViewType type) {
        checkMainThread();
        List<MultiTypeBinder> results = new ArrayList<>(mBinders.size());
        for (MultiTypeBinder binder : mBinders) {
            if (binder.getViewType().equals(type)) {
                results.add(binder);
            }
        }
        return Collections.unmodifiableList(results);
    }

    /**
     * Get the binder at the position
     *
     * @param adapterPosition the position in the adapter to get
     * @return the MultiTypeBinder or null if nothing at that position
     * @throws IllegalStateException if not called from the UI thread
     */
    @Nullable
    public MultiTypeBinder getBinder(int adapterPosition) {
        checkMainThread();
        if (adapterPosition < 0 || adapterPosition >= mBinders.size()) {
            return null;
        }
        return mBinders.get(adapterPosition);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MultiTypeCreator creator = mViewCreators.get(viewType);
        if (creator == null) {
            throw new IllegalArgumentException("Unknown viewType: " + viewType
                    + ". Make sure you call Builder.addCreator()");
        }
        RecyclerView.ViewHolder holder = creator.onCreateViewHolder(parent);
        if (!(holder instanceof MultiTypeViewHolder)) {
            throw new RuntimeException("ViewHolder must implement MultiTypeViewHolder");
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MultiTypeBinder binder = mBinders.get(position);
        binder.onBindViewHolder(holder);
    }

    @Override
    public int getItemCount() {
        return mBinders.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mBinders.get(position).getViewType().getType();
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ((MultiTypeViewHolder) holder).onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ((MultiTypeViewHolder) holder).onViewDetachedToWindow();
    }

    /**
     * Check the current thread is the main (UI) thread and throw an exception if not
     *
     * @throws IllegalStateException if this is not the main thread
     */
    private void checkMainThread() {
        if (!mThreadHelper.isUiThread()) {
            throw new IllegalStateException(
                    "MultiTypeAdapter should only be used from the UI thread");
        }
    }

    private static void checkNonNull(Object check, String objectName) {
        if (check == null) {
            throw new NullPointerException(objectName + " cannot be null");
        }
    }

    /**
     * Builder used to create the adapter
     */
    public final static class Builder {

        private final List<MultiTypeCreator> mCreators = new ArrayList<>();
        private ThreadHelper mThreadHelper = ThreadHelper.DEFAULT;

        /**
         * Register a MultiTypeCreator to be used by the adapter.
         * You must register all creators you want to use
         *
         * @param creator the creator to add
         * @return this for method chaining
         */
        public Builder addCreator(@NonNull MultiTypeCreator creator) {
            checkNonNull(creator, "creator");
            mCreators.add(creator);
            return this;
        }

        /**
         * Set the thread helper - useful for testing
         *
         * @param threadHelper the thread helper
         * @return this for method chaining
         */
        @VisibleForTesting
        public Builder threadHelper(@NonNull ThreadHelper threadHelper) {
            checkNonNull(threadHelper, "threadHelper");
            mThreadHelper = threadHelper;
            return this;
        }

        /**
         * Build the adapter
         *
         * @return the newly created adapter
         */
        public MultiTypeAdapter build() {
            return new MultiTypeAdapter(this);
        }

    }
}
