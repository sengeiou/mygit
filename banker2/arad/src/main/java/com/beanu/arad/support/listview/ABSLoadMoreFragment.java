package com.beanu.arad.support.listview;

import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.beanu.arad.base.ToolBarFragment;

/**
 * listview 加载更多
 * Created by beanu on 13-11-29.
 */
public abstract class ABSLoadMoreFragment extends ToolBarFragment implements ILoadMoreListener, AbsListView.OnScrollListener {

    protected int lastItem;

    protected abstract void loadMore();

    protected abstract BaseAdapter getAdapter();

    protected abstract boolean hasMore();

    protected abstract boolean isError();

    @Override
    public boolean hasMoreResults() {
        return hasMore();
    }

    @Override
    public boolean hasError() {
        return isError();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastItem = firstVisibleItem + visibleItemCount;
        if (lastItem == getAdapter().getCount() && hasMoreResults() && visibleItemCount != 0
                && firstVisibleItem + visibleItemCount >= totalItemCount - 1) {
            loadMore();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }
}