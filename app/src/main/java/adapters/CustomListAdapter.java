package adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.concon.talkabout.talkabout.R;
import com.concon.talkabout.talkabout.dataType.RewardCard;

import java.util.ArrayList;

/**
 * Created by Pablito on 30/04/2015.
 */
public class CustomListAdapter extends ArrayAdapter<RewardCard> {

    Context mContext;
    int layoutResourceId;
    RewardCard data[] = null;

    public CustomListAdapter(Context mContext, ArrayList<RewardCard> data) {

        super(mContext, 0, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RewardCard reward = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spin_wheel_history_row, parent, false);
        }
        // Lookup view for data population
        TextView textTitle = (TextView) convertView.findViewById(R.id.textTitle);
        TextView textDesc = (TextView) convertView.findViewById(R.id.textDesc);
        ImageView imageIcon = (ImageView) convertView.findViewById(R.id.imgRow);
        // Populate the data into the template view using the data object
        textTitle.setText(reward.cardTitle);
        textDesc.setText(reward.cardDescription);
        imageIcon.setImageDrawable(getContext().getResources().getDrawable(reward.cardIcon));
        // Return the completed view to render on screen
        return convertView;
    }
}
