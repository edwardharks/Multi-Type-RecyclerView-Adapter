package com.edwardharker.multiitemadapter;

/**
 * Represents a type of view in the adapter. Each ViewType should return a unique value for getType()
 * <p/>
 * Using an enum is helpful for this and return ordinal() in getType()
 */
public interface ViewType {

    /**
     * Get the view type
     *
     * @return the unique value for this view type
     */
    int getType();

}