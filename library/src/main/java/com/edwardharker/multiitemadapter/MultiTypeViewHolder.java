package com.edwardharker.multiitemadapter;

/**
 * Must be implemented by the ViewHolder.
 * Allows the view holder to be notified when attached/detached from the window
 */
public interface MultiTypeViewHolder {

    /**
     * The ViewHolder is attached to the window
     */
    void onViewAttachedToWindow();

    /**
     * The ViewHolder is detached from the window
     */
    void onViewDetachedToWindow();

}
