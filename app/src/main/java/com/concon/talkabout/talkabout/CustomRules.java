package com.concon.talkabout.talkabout;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.concon.talkabout.talkabout.dataType.Rules;
import com.concon.talkabout.talkabout.utils.DbManager;
import com.concon.talkabout.talkabout.utils.LanguageHelper;

import java.util.ArrayList;
import java.util.List;

import adapters.RulesAdapter;

/**
 * Created by Pablitoh on 27/05/2015.
 */
public class CustomRules extends ListActivity {

    RulesAdapter customAdapter;
    private Cursor mCursor;
    private ListView listView;
    private DbManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageHelper.loadApplicationLanguage(getBaseContext());
        setContentView(R.layout.custom_rules);

        listView = getListView();
        db =  new DbManager(this);
        mCursor = db.getAllPhrases();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                db.deletePhrase((int)id);
                customAdapter.changeCursor(db.getAllPhrases());
            }
        });
        
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                customAdapter = new RulesAdapter(
                        CustomRules.this,
                        mCursor,
                        0);
                listView.setAdapter(customAdapter);
            }
        });
    }

    public void addRule(View view) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.dialog_input_rule, null);
        dialogBuilder.setView(dialog);
        final AlertDialog alertDialog = dialogBuilder.create();
        Button dialogOkButton = (Button) dialog.findViewById(R.id.addRule);
        Button dialogCancelButton = (Button) dialog.findViewById(R.id.cancelRule);
        final EditText editableField = (EditText) dialog.findViewById(R.id.ruleInput);

        dialogOkButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                db.insertPhrase(editableField.getText().toString());
                alertDialog.dismiss();
                customAdapter.changeCursor(db.getAllPhrases());
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
}
