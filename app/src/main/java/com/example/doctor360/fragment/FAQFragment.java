package com.example.doctor360.fragment;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.example.doctor360.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FAQFragment extends Fragment {

    Context context;
    View rootView;
    CoordinatorLayout coordinatorLayout;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_faq,container,false);

        context = rootView.getContext();
        coordinatorLayout = rootView.findViewById(R.id.coordinatorLayout);
        expListView = rootView.findViewById(R.id.expandableListView);
        prepareListData();

        /*listAdapter = new ExpandableListAdapter(context, listDataHeader, listDataChild) {
            @Override
            public void registerDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public int getGroupCount() {
                return 0;
            }

            @Override
            public int getChildrenCount(int i) {
                return 0;
            }

            @Override
            public Object getGroup(int i) {
                return null;
            }

            @Override
            public Object getChild(int i, int i1) {
                return null;
            }

            @Override
            public long getGroupId(int i) {
                return 0;
            }

            @Override
            public long getChildId(int i, int i1) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
                return null;
            }

            @Override
            public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
                return null;
            }

            @Override
            public boolean isChildSelectable(int i, int i1) {
                return false;
            }

            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public void onGroupExpanded(int i) {

            }

            @Override
            public void onGroupCollapsed(int i) {

            }

            @Override
            public long getCombinedChildId(long l, long l1) {
                return 0;
            }

            @Override
            public long getCombinedGroupId(long l) {
                return 0;
            }
        }

        expListView.setAdapter(listAdapter);
*/
        return rootView;
    }

    private void prepareListData() {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add(getString(R.string.faq0_title));
        listDataHeader.add(getString(R.string.faq1_title));
        listDataHeader.add(getString(R.string.faq2_title));
        listDataHeader.add(getString(R.string.faq3_title));
        listDataHeader.add(getString(R.string.faq4_title));
        listDataHeader.add(getString(R.string.faq5_title));
        listDataHeader.add(getString(R.string.faq6_title));
        listDataHeader.add(getString(R.string.faq7_title));
        listDataHeader.add(getString(R.string.faq8_title));
        listDataHeader.add(getString(R.string.faq9_title));
        listDataHeader.add(getString(R.string.faq10_title));
        listDataHeader.add(getString(R.string.faq11_title));
        listDataHeader.add(getString(R.string.faq12_title));
        listDataHeader.add(getString(R.string.faq13_title));
        listDataHeader.add(getString(R.string.faq14_title));
        listDataHeader.add(getString(R.string.faq15_title));
        listDataHeader.add(getString(R.string.faq16_title));
        listDataHeader.add(getString(R.string.faq17_title));
        listDataHeader.add(getString(R.string.faq18_title));
        listDataHeader.add(getString(R.string.faq19_title));

        List<String> faq0 = new ArrayList<String>();
        faq0.add(getString(R.string.faq0_desc));

        List<String> faq1 = new ArrayList<String>();
        faq1.add(getString(R.string.faq1_desc));

        List<String> faq2 = new ArrayList<String>();
        faq2.add(getString(R.string.faq2_desc));

        List<String> faq3 = new ArrayList<String>();
        faq3.add(getString(R.string.faq3_desc));

        List<String> faq4 = new ArrayList<String>();
        faq4.add(getString(R.string.faq4_desc));

        List<String> faq5 = new ArrayList<String>();
        faq5.add(getString(R.string.faq5_desc));

        List<String> faq6 = new ArrayList<String>();
        faq6.add(getString(R.string.faq6_desc));

        List<String> faq7 = new ArrayList<String>();
        faq7.add(getString(R.string.faq7_desc));

        List<String> faq8 = new ArrayList<String>();
        faq8.add(getString(R.string.faq8_desc));

        List<String> faq9 = new ArrayList<String>();
        faq9.add(getString(R.string.faq9_desc));

        List<String> faq10 = new ArrayList<String>();
        faq10.add(getString(R.string.faq10_desc));

        List<String> faq11 = new ArrayList<String>();
        faq11.add(getString(R.string.faq11_desc));

        List<String> faq12 = new ArrayList<String>();
        faq12.add(getString(R.string.faq12_desc));

        List<String> faq13 = new ArrayList<String>();
        faq13.add(getString(R.string.faq13_desc));

        List<String> faq14 = new ArrayList<String>();
        faq14.add(getString(R.string.faq14_desc));

        List<String> faq15 = new ArrayList<String>();
        faq15.add(getString(R.string.faq15_desc));

        List<String> faq16 = new ArrayList<String>();
        faq16.add(getString(R.string.faq16_desc));

        List<String> faq17 = new ArrayList<String>();
        faq17.add(getString(R.string.faq17_desc));

        List<String> faq18 = new ArrayList<String>();
        faq18.add(getString(R.string.faq18_desc));

        List<String> faq19 = new ArrayList<String>();
        faq19.add(getString(R.string.faq19_desc));

        listDataChild.put(listDataHeader.get(0), faq0);
        listDataChild.put(listDataHeader.get(1), faq1);
        listDataChild.put(listDataHeader.get(2), faq2);
        listDataChild.put(listDataHeader.get(3), faq3);
        listDataChild.put(listDataHeader.get(4), faq4);
        listDataChild.put(listDataHeader.get(5), faq5);
        listDataChild.put(listDataHeader.get(6), faq6);
        listDataChild.put(listDataHeader.get(7), faq7);
        listDataChild.put(listDataHeader.get(8), faq8);
        listDataChild.put(listDataHeader.get(9), faq9);
        listDataChild.put(listDataHeader.get(10), faq10);
        listDataChild.put(listDataHeader.get(11), faq11);
        listDataChild.put(listDataHeader.get(12), faq12);
        listDataChild.put(listDataHeader.get(13), faq13);
        listDataChild.put(listDataHeader.get(14), faq14);
        listDataChild.put(listDataHeader.get(15), faq15);
        listDataChild.put(listDataHeader.get(16), faq16);
        listDataChild.put(listDataHeader.get(17), faq17);
        listDataChild.put(listDataHeader.get(18), faq18);
        listDataChild.put(listDataHeader.get(19), faq19);

    }
}
