package com.edwardharker.demo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edwardharker.multiitemadapter.MultiTypeBinder;
import com.edwardharker.multiitemadapter.MultiTypeCreator;
import com.edwardharker.multiitemadapter.SimpleMultiTypeViewHolder;
import com.edwardharker.multiitemadapter.ViewType;

public class FooterViewType {

    private static final String TAG = ViewTypeTwo.class.getSimpleName();

    public static class Creator implements MultiTypeCreator {

        @NonNull
        @Override
        public ViewType getViewType() {
            return DemoViewTypes.FOOTER;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_type_footer, parent, false);
            return new ViewHolder(view);
        }

    }

    public static class Binder implements MultiTypeBinder {

        public Binder() {

        }

        @NonNull
        @Override
        public ViewType getViewType() {
            return DemoViewTypes.FOOTER;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder) {

        }

    }

    public static class ViewHolder extends SimpleMultiTypeViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);

        }

    }

}
