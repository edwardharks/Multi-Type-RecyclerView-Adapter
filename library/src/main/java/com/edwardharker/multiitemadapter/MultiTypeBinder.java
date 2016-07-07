package com.edwardharker.multiitemadapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * Responsible for binding data to the view holder
 */
public interface MultiTypeBinder {

    /**
     * Get the ViewType that this binder represents
     *
     * @return the ViewType
     */
    @NonNull
    ViewType getViewType();

    /**
     * The equivalent of Adapter.onBindViewHolder(). Bind the data to the view holder
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *               item at the given position in the data set.
     */
    // TODO: Can we use generics here so we can avoid casting the view holder?
    void onBindViewHolder(RecyclerView.ViewHolder holder);

}