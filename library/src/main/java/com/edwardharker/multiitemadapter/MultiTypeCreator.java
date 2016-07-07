package com.edwardharker.multiitemadapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Responsible for creating the view holder for this type
 */
public interface MultiTypeCreator {

    /**
     * Get the ViewType that this binder represents
     *
     * @return the ViewType
     */
    @NonNull
    ViewType getViewType();

    /**
     * The equivalent of Adapter.onCreateViewHolder(). Create the view holder for this item type.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @return A new ViewHolder that holds a View for this type
     */
    RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent);

}