package com.appdazzle_innovations.testingcenter;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CircularLinearLayoutManager extends LinearLayoutManager {

    public CircularLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public int getChildCount() {
        return super.getChildCount();
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int direction = dx > 0 ? 1 : -1; // Get the scroll direction

        // Calculate the scroll amount considering the adapter's total count
        int itemCount = getItemCount();
        int currentPosition = findFirstVisibleItemPosition();
        int scrollCount = Math.min(Math.abs(dx), (itemCount + currentPosition) % itemCount);

        // Call the original method with the calculated scroll count
        int scrolled = super.scrollHorizontallyBy(scrollCount * direction, recycler, state);

        // Handle reaching the end or beginning
        if (currentPosition + scrolled == itemCount) {
            scrollToPosition(0); // Loop back to the first item
        } else if (currentPosition == 0 && scrolled < 0) {
            scrollToPosition(itemCount - 1); // Loop back to the last item
        }

        return scrolled;
    }
}
