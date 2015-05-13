package com.concon.talkabout.talkabout;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.concon.talkabout.talkabout.dataType.RewardCard;

import java.util.ArrayList;
import java.util.Collections;

import adapters.CustomListAdapter;

/**
 * Created by OE on 28/04/2015.
 */
public class SpinWheelHistoryTAB extends ListFragment {

    // Defined Array values to show in ListView
    private ArrayList<RewardCard> lista = new ArrayList<>();
    ListView listView;
    CustomListAdapter adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = getListView();
        adapter = new CustomListAdapter(getActivity(), lista);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View android = inflater.inflate(R.layout.spin_wheel_history_tab, container, false);
        return android;
    }

    public void addToList(RewardCard rewardCard) {
        if (!rewardCard.cardTitle.equals(getString(R.string.cleanseTitle))) {
            lista.add(0,rewardCard);
        } else {
            lista.clear();
        }
        adapter.notifyDataSetChanged();
    }


}