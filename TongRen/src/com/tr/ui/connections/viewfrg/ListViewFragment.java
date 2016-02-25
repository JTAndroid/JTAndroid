package com.tr.ui.connections.viewfrg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.tr.R;
import com.tr.ui.widgets.viewpagerheaderscroll.delegate.AbsListViewDelegate;


public class ListViewFragment extends BaseViewPagerFragment
        implements AbsListView.OnItemClickListener {

    private ListView mListView;
    private ListAdapter mAdapter;
    private AbsListViewDelegate mAbsListViewDelegate = new AbsListViewDelegate();

    public static ListViewFragment newInstance(int index) {
        ListViewFragment fragment = new ListViewFragment();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_FRAGMENT_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    public ListViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] listArrays = null;
        switch (mFragmentIndex) {
            case 1:
                listArrays = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};
                break;
            case 2:
                listArrays = new String[]{"a","b","c","d","e"};
                break;
            default:
                listArrays = new String[]{};
                break;
        }
        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                android.R.id.text1, listArrays);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setEmptyView(view.findViewById(android.R.id.empty));
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override public boolean isViewBeingDragged(MotionEvent event) {
        return mAbsListViewDelegate.isViewBeingDragged(event, mListView);
    }
}
