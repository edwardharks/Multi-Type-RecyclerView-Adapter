package com.edwardharker.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.edwardharker.multiitemadapter.MultiTypeAdapter;
import com.edwardharker.multiitemadapter.MultiTypeBinder;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private MultiTypeAdapter mAdapter;
    private int mViewTypeOneCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new MultiTypeAdapter.Builder()
                .addCreator(new ViewTypeOne.Creator())
                .addCreator(new ViewTypeTwo.Creator())
                .addCreator(new FooterViewType.Creator())
                .build();

        recyclerView.setAdapter(mAdapter);

        mAdapter.add(newViewTypeOneBinder());
        mAdapter.add(newViewTypeTwoBinder());
        mAdapter.add(newViewTypeOneBinder());
        mAdapter.add(newViewTypeTwoBinder());
        mAdapter.add(newViewTypeOneBinder());
        mAdapter.add(newViewTypeTwoBinder());
        mAdapter.add(newViewTypeOneBinder());
        mAdapter.add(newViewTypeTwoBinder());

        // Setting footer. It will always be the last item in the adapter
        mAdapter.setFooter(new FooterViewType.Binder());

        mAdapter.add(newViewTypeOneBinder());
        mAdapter.add(newViewTypeTwoBinder());
        mAdapter.add(newViewTypeOneBinder());
        mAdapter.add(newViewTypeTwoBinder());
        mAdapter.add(newViewTypeOneBinder());
        mAdapter.add(newViewTypeTwoBinder());
        mAdapter.add(newViewTypeOneBinder());
        mAdapter.add(newViewTypeTwoBinder());
        mAdapter.add(newViewTypeOneBinder());
        mAdapter.add(newViewTypeTwoBinder());
        mAdapter.add(newViewTypeOneBinder());
        mAdapter.add(newViewTypeTwoBinder());
        mAdapter.add(newViewTypeOneBinder());
        mAdapter.add(newViewTypeTwoBinder());
        mAdapter.add(newViewTypeOneBinder());
        mAdapter.add(newViewTypeTwoBinder());

        mAdapter.addAll(Arrays.asList(newViewTypeOneBinder(), newViewTypeTwoBinder()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove:
                if (mAdapter.getItemCount() >= 0) {
                    mAdapter.remove(0);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private MultiTypeBinder newViewTypeOneBinder() {
        return new ViewTypeOne.Binder("View type one: " + ++mViewTypeOneCount);
    }

    private MultiTypeBinder newViewTypeTwoBinder() {
        return new ViewTypeTwo.Binder("View type two: " + ++mViewTypeOneCount);
    }

}
