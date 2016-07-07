package com.edwardharker.multiitemadapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
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
        if (!mThreadHelper.isUiThread()) {
            throw new IllegalStateException("MultiTypeAdapter should only be used from the UI thread");
        }
        mBinders.add(binder);
        // TODO: replace with notifyItemChanged/inserted
        notifyDataSetChanged();
    }

    /**
     * Add all the items to the adapter
     *
     * @param binders the binders to add
     * @throws IllegalStateException if not called from the UI thread
     */
    public void addAll(@NonNull Collection<MultiTypeBinder> binders) {
        if (!mThreadHelper.isUiThread()) {
            throw new IllegalStateException("MultiTypeAdapter should only be used from the UI thread");
        }
        mBinders.addAll(binders);
        // TODO: replace with notifyRangeChanged
        notifyDataSetChanged();
    }

    /**
     * Get the binder at the position
     *
     * @param adapterPosition the position in the adapter to get
     * @return the MultiTypeBinder or null if nothing at that position
     */
    @Nullable
    public MultiTypeBinder getBinder(int adapterPosition) {
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
        return creator.onCreateViewHolder(parent);
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

    /**
     * Builder used to create the adapter
     */
    public final static class Builder {

        private final List<MultiTypeCreator> mCreators = new ArrayList<>();
        private ThreadHelper mThreadHelper = ThreadHelper.DEFAULT;

        /**
         * Register a MultiTypeCreator to be used by the adapter. You must register all creators you want to use
         *
         * @param creator the creator to add
         * @return this for method chaining
         */
        public Builder addCreator(@NonNull MultiTypeCreator creator) {
            mCreators.add(creator);
            return this;
        }

        /**
         * Set the thread helper - useful for testing
         *
         * @param threadHelper the thread helper
         * @return this for method chaining
         */
        public Builder threadHelper(@NonNull ThreadHelper threadHelper) {
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