package adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.concon.talkabout.talkabout.R;

/**
 * Created by OE on 28/04/2015.
 */
public class SecondTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View android = inflater.inflate(R.layout.second_tab, container, false);
        ((TextView) android.findViewById(R.id.textView)).setText("Second" +
                "");
        return android;
    }
}