package com.concon.talkabout.talkabout;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.concon.talkabout.talkabout.adapters.ListPopulator;
import com.concon.talkabout.talkabout.dataType.UserRule;
import com.concon.talkabout.talkabout.utils.DbManager;
import com.google.gson.Gson;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.RulesAdapter;

/**
 * Created by Pablitoh on 02/06/2015.
 */
public class CustomRulesFragment extends ListFragment {


    RulesAdapter customAdapter;
    private Cursor mCursor;
    private ListView listView;
    private DbManager db;
    private int previousButtonId = -1;

    public static CustomRulesFragment newInstance(int page) {
        CustomRulesFragment fragment = new CustomRulesFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = getListView();
        db =  new DbManager(getActivity());
        mCursor = db.getAllPhrases();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,final long id) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                final View dialog = inflater.inflate(R.layout.dialog_input_rule, null);
                dialogBuilder.setView(dialog);
                final AlertDialog alertDialog = dialogBuilder.create();
                Button dialogOkButton = (Button) dialog.findViewById(R.id.addRule);
                Button dialogCancelButton = (Button) dialog.findViewById(R.id.cancelRule);
                final EditText editableField = (EditText) dialog.findViewById(R.id.ruleInput);
                TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
                title.setText(getString(R.string.changeRule));
                editableField.setText(((TextView)view.findViewById(R.id.textDesc)).getText());
                dialogOkButton.setText(getString(R.string.modify));
                dialogOkButton.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        if(!editableField.getText().toString().equals(""))
                        {
                            db.updatePhrase(id, editableField.getText().toString());
                            alertDialog.dismiss();
                            customAdapter.changeCursor(db.getAllPhrases());
                        }
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

        new Handler().post(new Runnable() {

            @Override
            public void run() {
                customAdapter = new RulesAdapter(
                        getActivity(),
                        mCursor,
                        0);
                listView.setAdapter(customAdapter);
            }
        });



    }

    public void updateDB()
    {
        customAdapter.changeCursor(db.getAllPhrases());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_rules, container, false);
        final TelephonyManager tm = (TelephonyManager) getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        ImageView addButton = (ImageView) view.findViewById(R.id.addIcon);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialog = inflater.inflate(R.layout.dialog_input_rule, null);
                dialogBuilder.setView(dialog);
                final AlertDialog alertDialog = dialogBuilder.create();
                Button dialogOkButton = (Button) dialog.findViewById(R.id.addRule);
                Button dialogCancelButton = (Button) dialog.findViewById(R.id.cancelRule);
                final EditText editableField = (EditText) dialog.findViewById(R.id.ruleInput);

                dialogOkButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (!editableField.getText().toString().equals("")) {
                            db.insertPhrase(editableField.getText().toString());
                            alertDialog.dismiss();
                            customAdapter.changeCursor(db.getAllPhrases());
                            UserRule userRule = new UserRule(tm.getNetworkCountryIso(), editableField.getText().toString());

                            new Connection(userRule).execute();
                        }
                    }
                });
                dialogCancelButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
        return view;
    }

    private class Connection extends AsyncTask {

        UserRule userRule;

        public Connection(UserRule userRule) {
            this.userRule = userRule;
        }

        @Override
        protected Object doInBackground(Object... arg0) {
            connect(userRule);
            return null;
        }

        private void connect(UserRule user) {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            //url with the post data
            HttpPost httpost = new HttpPost("http://embriagados.herokuapp.com/rules");
            StringEntity se = null;
            try {
                se = new StringEntity(new Gson().toJson(user), HTTP.UTF_8);
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
