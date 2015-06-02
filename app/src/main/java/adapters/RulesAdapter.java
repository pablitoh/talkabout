package adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.concon.talkabout.talkabout.R;
import com.concon.talkabout.talkabout.dataType.Rules;
import com.concon.talkabout.talkabout.utils.DbManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablitoh on 27/05/2015.
 */
public class RulesAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;
    private DbManager db;

    public RulesAdapter(Context context, Cursor c, int flags) {
        super(context,c,flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = cursorInflater.inflate(R.layout.custom_rules_row, parent, false);
        v.setTag(cursor.getPosition());
        return v;
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        TextView tv = (TextView) view.findViewById(R.id.textDesc);
        tv.setText(cursor.getString(1));
        TextView tvTitle = (TextView) view.findViewById(R.id.textTitle);
        tvTitle.setText(context.getString(R.string.CustomRuleTitle));

        ((ImageView) view.findViewById(R.id.trashCan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new DbManager(context);
                if(cursor.moveToFirst() )
                {
                    cursor.moveToPosition((int)view.getTag());
                    db.deletePhrase(cursor.getInt(0));
                    changeCursor(db.getAllPhrases());
                    db.close();
                }
            }
        });


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
