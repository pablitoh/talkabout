package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.concon.talkabout.talkabout.adapters.ListPopulator;
import com.concon.talkabout.talkabout.communication.ListManager;
import com.concon.talkabout.talkabout.dataType.CountryData;
import com.concon.talkabout.talkabout.dataType.UserRule;
import com.concon.talkabout.talkabout.utils.DbManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import adapters.ServerRuleAdapter;

/**
 * Created by Pablitoh on 02/06/2015.
 */
public class ServerRules extends ListFragment implements ListPopulator {

    private static final Object TAG = "mytag";
    private ServerRuleAdapter adapter;
    private DbManager db;
    private ListManager mCallback;
    private String url = "";
    private TextView empty;
    private String countryCode;
    private RequestQueue queue = null;


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


        switch (getArguments().getInt("TYPE")) {
            case 0:
                sectionTitle.setText(getString(R.string.PopularToday));
                url = "http://embriagados.herokuapp.com/rules?country={country}&section=popular";
                break;
            case 1:
                 sectionTitle.setText(getString(R.string.Recent));
                 url = "http://embriagados.herokuapp.com/rules?country={country}&section=recent";
                break;
            default:
                throw new RuntimeException("No valid type");
        }
        queue = Volley.newRequestQueue(getActivity());
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new DbManager(getActivity());
        getListView().setEmptyView(getView().findViewById(android.R.id.empty));
        empty = (TextView) getListView().getEmptyView();
        getCountryCode();

            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent,final View view, int position, long id) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
                    final View dialog = inflater.inflate(R.layout.confirmation_popup, null);
                    dialogBuilder.setView(dialog);
                    final AlertDialog alertDialog = dialogBuilder.create();
                    Button dialogOkButton = (Button) dialog.findViewById(R.id.addRule);
                    Button dialogCancelButton = (Button) dialog.findViewById(R.id.cancelRule);
                    TextView desc = (TextView) dialog.findViewById(R.id.dialogText);
                    desc.setText(getString(R.string.ruleAddConfirmation));;
                    dialogOkButton.setText(getString(R.string.yes));
                    dialogOkButton.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            db.insertPhrase(((TextView) view.findViewById(R.id.textDesc)).getText().toString());
                            mCallback.updateDB();
                            alertDialog.dismiss();
                            UserRule userRule = new UserRule(Long.parseLong(((TextView) view.findViewById(R.id.idField)).getText().toString()));

                            new Connection(userRule).execute();
                        }
                    });
                    dialogCancelButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            });

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCountryCode();
                empty.setText("Loading...");
                swipeLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (ListManager) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnReward");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
            if (queue != null) {
                queue.cancelAll(TAG);
            }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    private void getCountryCode() {
        if (countryCode == null) {

            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://ip-api.com/json",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            CountryData country = new Gson().fromJson(response, new TypeToken<CountryData>() {
                            }.getType());

                            countryCode = country.getCountryCode();
                            retrieveData(countryCode);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    empty.setText(getResources().getString(R.string.connectionError));
                }
            });
            stringRequest.setTag(TAG);
            queue.add(stringRequest);
            empty.setText(getResources().getString(R.string.loading));
        } else {
            retrieveData(countryCode);
        }
    }
    @Override
    public void retrieveData(String countryCode)
    {
        /***
         * Request to retrieve Data
         */
        RequestQueue queue = Volley.newRequestQueue(getActivity());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url.replace("{country}",countryCode.toLowerCase()),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ArrayList<UserRule> ruleArray = new Gson().fromJson(response, new TypeToken<List<UserRule>>(){}.getType());
                            adapter = new ServerRuleAdapter(getActivity(), ruleArray, getArguments().getInt("TYPE"));
                            setListAdapter(adapter);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    empty.setText(getResources().getString(R.string.connectionError));
                }
            });
        stringRequest.setTag(TAG);
            queue.add(stringRequest);
            empty.setText(getResources().getString(R.string.loading));


    }
    private class Connection extends AsyncTask {

        UserRule userRule;

        public Connection(UserRule userRule) {
            this.userRule = userRule;
        }

        @Override
        protected Object doInBackground(Object... arg0) {
            connect();
            return null;
        }

        private void connect() {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            //url with the post data
            HttpPut httpost = new HttpPut("http://embriagados.herokuapp.com/rules");
            StringEntity se = null;
            try {
                se = new StringEntity(new Gson().toJson(userRule));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //sets the post request as the resulting string
            httpost.setEntity(se);
            //sets a request header so the page receving the request
            //will know what to do with it
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");

            //Handles what is returned from the page
            ResponseHandler responseHandler = new BasicResponseHandler();
            try {
                httpclient.execute(httpost, responseHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
