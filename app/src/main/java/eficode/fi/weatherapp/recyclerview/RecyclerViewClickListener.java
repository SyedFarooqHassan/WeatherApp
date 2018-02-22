package eficode.fi.weatherapp.recyclerview;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import eficode.fi.weatherapp.interfaces.IOnItemClickListener;


public class RecyclerViewClickListener implements RecyclerView.OnItemTouchListener {
    private IOnItemClickListener listener;

    GestureDetector gestureDetector;

    public RecyclerViewClickListener(@NonNull Context context, @NonNull final RecyclerView recyclerView, @NonNull final IOnItemClickListener listener) {
        this.listener = listener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
            @Override
            public void onLongPress(MotionEvent e) {
                View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (view != null && listener != null) {
                    listener.onLongItemClick(view, recyclerView.getChildAdapterPosition(view));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent e) {
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null && listener != null && gestureDetector.onTouchEvent(e)) {
            listener.onItemClick(view, recyclerView.getChildAdapterPosition(view));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }
}
