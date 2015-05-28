package adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.concon.talkabout.talkabout.R;
import com.concon.talkabout.talkabout.dataType.Rules;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablitoh on 27/05/2015.
 */
public class RulesAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;

    public RulesAdapter(Context context, Cursor c, int flags) {
        super(context,c,flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.spin_wheel_history_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tv = (TextView) view.findViewById(R.id.textDesc);
        tv.setText(cursor.getString(1));
        TextView tvTitle = (TextView) view.findViewById(R.id.textTitle);
        tvTitle.setText("Rule");
    }

}
