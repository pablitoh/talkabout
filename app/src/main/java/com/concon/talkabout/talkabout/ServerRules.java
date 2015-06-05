package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.concon.talkabout.talkabout.communication.ListManager;
import com.concon.talkabout.talkabout.dataType.Rules;
import com.concon.talkabout.talkabout.dataType.UserRule;
import com.concon.talkabout.talkabout.utils.DbManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import adapters.ServerRuleAdapter;

/**
 * Created by Pablitoh on 02/06/2015.
 */
public class ServerRules extends ListFragment {

    private ServerRuleAdapter adapter;
    private DbManager db;
    private ListManager mCallback;

    public static ServerRules newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("TYPE", type);
        ServerRules fragment = new ServerRules();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.server_rules, container, false);
        TextView sectionTitle = (TextView) view.findViewById(R.id.myrulestitle);
        TelephonyManager tm = (TelephonyManager) getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        switch (getArguments().getInt("TYPE")) {
            case 0:
                sectionTitle.setText("Popular Today ");
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url = "http://embriagados.herokuapp.com/rules?country="+tm.getNetworkCountryIso()+"&section=popular";

// Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                ArrayList<UserRule> ruleArray = new Gson().fromJson(response, new TypeToken<List<UserRule>>(){}.getType());
                                adapter = new ServerRuleAdapter(getActivity(), ruleArray);
                                setListAdapter(adapter);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(),"There was an error processing the response, please try again later ",Toast.LENGTH_LONG).show();
                    }
                });
// Add the request to the RequestQueue.
                queue.add(stringRequest);
                break;
            case 1:
                sectionTitle.setText("Recent");
                queue = Volley.newRequestQueue(getActivity());
                 url = "http://embriagados.herokuapp.com/rules?country="+tm.getNetworkCountryIso()+"&section=recent";

// Request a string response from the provided URL.
                stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                ArrayList<UserRule> ruleArray = new Gson().fromJson(response, new TypeToken<List<UserRule>>(){}.getType());
                                adapter = new ServerRuleAdapter(getActivity(), ruleArray);
                                setListAdapter(adapter);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(),"There was an error procesing the response, please try again later ",Toast.LENGTH_LONG).show();
                    }
                });
// Add the request to the RequestQueue.
                queue.add(stringRequest);
                break;
            default:
                throw new RuntimeException("No valid type");
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new DbManager(getActivity());
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                db.insertPhrase(((TextView) view.findViewById(R.id.textDesc)).getText().toString());
                mCallback.updateDB();
            }
        });
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (ListManager) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnReward");
        }
    }

}
