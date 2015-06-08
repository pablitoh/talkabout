package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.concon.talkabout.talkabout.R;
import com.concon.talkabout.talkabout.dataType.UserRule;

import java.util.ArrayList;

/**
 * Created by OE on 05/06/2015.
 */
public class ServerRuleAdapter extends ArrayAdapter<UserRule> {

    private Context mContext;
    public ServerRuleAdapter(Context mContext, ArrayList<UserRule> data) {
        super(mContext, 0, data);
        this.mContext = mContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        UserRule userRule = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spin_wheel_history_row, parent, false);
        }
        // Lookup view for data population
        TextView textTitle = (TextView) convertView.findViewById(R.id.textTitle);
        TextView textDesc = (TextView) convertView.findViewById(R.id.textDesc);
        TextView idField = (TextView) convertView.findViewById(R.id.idField);
        idField.setText(String.valueOf(userRule.getId()));
        ImageView imageIcon = (ImageView) convertView.findViewById(R.id.imgRow);
        // Populate the data into the template view using the data object
        textTitle.setText(mContext.getString(R.string.customRule));
        textDesc.setText(userRule.getRule());
        imageIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_skull));
        // Return the completed view to render on screen
        return convertView;
    }
}
