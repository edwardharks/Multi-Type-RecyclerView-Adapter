package com.edwardharker.demo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edwardharker.multiitemadapter.MultiTypeBinder;
import com.edwardharker.multiitemadapter.MultiTypeCreator;
import com.edwardharker.multiitemadapter.ViewType;

public class ViewTypeOne {

    public static class Creator implements MultiTypeCreator {

        @NonNull
        @Override
        public ViewType getViewType() {
            return DemoViewTypes.VIEW_TYPE_ONE;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_type_one, parent, false);
            return new ViewHolder(view);
        }

    }

    public static class Binder implements MultiTypeBinder {

        private final String mData;

        public Binder(String data) {
            this.mData = data;
        }

        @NonNull
        @Override
        public ViewType getViewType() {
            return DemoViewTypes.VIEW_TYPE_ONE;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder) {
            final ViewHolder typeOneHolder = (ViewHolder) holder;
            typeOneHolder.mText.setText(mData);
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mText;

        public ViewHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.data);
        }

    }

}
