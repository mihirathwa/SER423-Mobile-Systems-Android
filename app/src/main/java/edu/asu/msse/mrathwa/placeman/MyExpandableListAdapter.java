package edu.asu.msse.mrathwa.placeman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Mihir on 02/08/2017.
 */

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> categories;
    private Map<String, List<String>> places;

    public MyExpandableListAdapter(Context context, List<String> categories, Map<String, List<String>> places) {
        this.context = context;
        this.categories = categories;
        this.places = places;
    }

    @Override
    public int getGroupCount() {
        return categories.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return places.get(categories.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return categories.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return places.get(categories.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        String category = (String) getGroup(i);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_parent, null);
        }

        TextView tv_parent = (TextView) view.findViewById(R.id.LP_tvParent);
        tv_parent.setText(category);

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String place = (String) getChild(i, i1);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_child, null);
        }

        TextView tv_child = (TextView) view.findViewById(R.id.LC_tvChild);
        tv_child.setText(place);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
