package com.edwardharker.demo;

import com.edwardharker.multiitemadapter.ViewType;

public enum DemoViewTypes implements ViewType {

    VIEW_TYPE_ONE, VIEW_TYPE_TWO, FOOTER;

    @Override
    public int getType() {
        return ordinal();
    }
    
}
