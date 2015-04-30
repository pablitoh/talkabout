package com.concon.talkabout.talkabout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.concon.talkabout.talkabout.dataType.RewardCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapters.CustomListAdapter;

/**
 * Created by OE on 28/04/2015.
 */
public class SpinWheelHistoryTAB extends Fragment {

    // Defined Array values to show in ListView
    private ArrayList<RewardCard> lista = new ArrayList<>();
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View android = inflater.inflate(R.layout.spin_wheel_history_tab, container, false);

        CustomListAdapter adapter = new CustomListAdapter(getActivity(),lista);
        listView = (ListView) android.findViewById(R.id.historyList);
        listView.setAdapter(adapter);
        return android;
    }

    public void addToList(RewardCard rewardCard) {
        if(!rewardCard.cardTitle.equals(getString(R.string.cleanseTitle)))
        {
            lista.add(rewardCard);
            Collections.reverse(lista);
        }
        else
        {
            lista.clear();
        }
        listView.invalidateViews();
    }


}