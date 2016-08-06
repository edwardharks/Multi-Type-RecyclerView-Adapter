package com.edwardharker.multiitemadapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SimpleMultiTypeViewHolder extends RecyclerView.ViewHolder implements MultiTypeViewHolder {

    public SimpleMultiTypeViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewAttachedToWindow() {

    }

    @Override
    public void onViewDetachedToWindow() {

    }

}
