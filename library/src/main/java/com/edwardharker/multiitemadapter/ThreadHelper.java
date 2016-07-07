package com.edwardharker.multiitemadapter;

import android.os.Looper;

/**
 * Handy thread related functions
 */
public class ThreadHelper {

    public static final ThreadHelper DEFAULT = new ThreadHelper();

    /**
     * @return true if the current thread is the UI thread
     */
    public boolean isUiThread() {
        return Looper.getMainLooper().equals(Looper.myLooper());
    }

}