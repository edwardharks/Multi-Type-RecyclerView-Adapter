package com.edwardharker.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.edwardharker.multiitemadapter.MultiTypeAdapter;
import com.edwardharker.multiitemadapter.MultiTypeBinder;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private int mViewTypeOneCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        MultiTypeAdapter adapter = new MultiTypeAdapter.Builder()
                .addCreator(new ViewTypeOne.Creator())
                .addCreator(new ViewTypeTwo.Creator())
                .addCreator(new FooterViewType.Creator())
                .build();

        recyclerView.setAdapter(adapter);

        adapter.add(newViewTypeOneBinder());
        adapter.add(newViewTypeTwoBinder());
        adapter.add(newViewTypeOneBinder());
        adapter.add(newViewTypeTwoBinder());
        adapter.add(newViewTypeOneBinder());
        adapter.add(newViewTypeTwoBinder());
        adapter.add(newViewTypeOneBinder());
        adapter.add(newViewTypeTwoBinder());

        // Setting footer. It will always be the last item in the adapter
        adapter.setFooter(new FooterViewType.Binder());

        adapter.add(newViewTypeOneBinder());
        adapter.add(newViewTypeTwoBinder());
        adapter.add(newViewTypeOneBinder());
        adapter.add(newViewTypeTwoBinder());
        adapter.add(newViewTypeOneBinder());
        adapter.add(newViewTypeTwoBinder());
        adapter.add(newViewTypeOneBinder());
        adapter.add(newViewTypeTwoBinder());
        adapter.add(newViewTypeOneBinder());
        adapter.add(newViewTypeTwoBinder());
        adapter.add(newViewTypeOneBinder());
        adapter.add(newViewTypeTwoBinder());
        adapter.add(newViewTypeOneBinder());
        adapter.add(newViewTypeTwoBinder());
        adapter.add(newViewTypeOneBinder());
        adapter.add(newViewTypeTwoBinder());

        adapter.addAll(Arrays.asList(newViewTypeOneBinder(), newViewTypeTwoBinder()));

    }

    private MultiTypeBinder newViewTypeOneBinder() {
        return new ViewTypeOne.Binder("View type one: " + ++mViewTypeOneCount);
    }

    private MultiTypeBinder newViewTypeTwoBinder() {
        return new ViewTypeTwo.Binder("View type two: " + ++mViewTypeOneCount);
    }

}
