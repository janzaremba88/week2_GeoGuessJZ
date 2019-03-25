package com.example.geoguess;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    private RecyclerView mGeoRecyclerView;
    private List<GeoObject> mGeoObjects;
    private GeoObjectAdapter mAdapter;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGeoRecyclerView = findViewById(R.id.imageRecycler);
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        mGeoObjects = new ArrayList<>();

        for (int i = 0; i < GeoObject.PRE_DEFINED_GEO_OBJECT_NAMES.length; i++) {

            mGeoObjects.add(new GeoObject(GeoObject.PRE_DEFINED_GEO_OBJECT_NAMES[i],
                    GeoObject.PRE_DEFINED_GEO_OBJECT_IMAGE_IDS[i],
                    GeoObject.PRE_DEFINED_GEO_OBJECT_ANSWERS[i]));
        }

        mGeoRecyclerView.setLayoutManager(mLayoutManager);
        mGeoRecyclerView.setHasFixedSize(true);
        mAdapter = new GeoObjectAdapter(this, mGeoObjects);
        mGeoRecyclerView.setAdapter(mAdapter);
        mGeoRecyclerView.addOnItemTouchListener(this);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallbackLeft =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        int position = (viewHolder.getAdapterPosition());
                        checkAnswer(true, mGeoObjects.get(position).getmGeoAnswer());
                        mGeoObjects.remove(position);
                        mAdapter.notifyItemRemoved(position);

                    }
                };

        ItemTouchHelper.SimpleCallback simpleItemTouchCallbackRight =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        int position = (viewHolder.getAdapterPosition());
                        checkAnswer(false, mGeoObjects.get(position).getmGeoAnswer());
                        mGeoObjects.remove(position);
                        mAdapter.notifyItemRemoved(position);

                    }
                };

        ItemTouchHelper itemTouchHelperLeft = new ItemTouchHelper(simpleItemTouchCallbackLeft);
        itemTouchHelperLeft.attachToRecyclerView(mGeoRecyclerView);
        ItemTouchHelper itemTouchHelperRight = new ItemTouchHelper(simpleItemTouchCallbackRight);
        itemTouchHelperRight.attachToRecyclerView(mGeoRecyclerView);
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    private void checkAnswer(boolean guess, boolean answer) {
        if ((guess && answer) ||
                (!guess && !answer)) {
            Toast.makeText(this, getString(R.string.correctguess), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.wrongguess), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        int mAdapterPosition = rv.getChildAdapterPosition(child);
        if (child != null && mGestureDetector.onTouchEvent(e)) {
            Toast.makeText(this, mGeoObjects.get(mAdapterPosition).getmGeoName(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
