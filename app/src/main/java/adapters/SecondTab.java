package adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.concon.talkabout.talkabout.R;

/**
 * Created by OE on 28/04/2015.
 */
public class SecondTab extends Fragment {

    // Defined Array values to show in ListView
    String[] values = new String[] { "Android List View",
            "Adapter implementation",
            "Simple List View In Android",
            "Create List View Android",
            "Android Example",
            "List View Source Code",
            "List View Array Adapter",
            "Android Example List View"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View android = inflater.inflate(R.layout.second_tab, container, false);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.simple_list_item_1, R.id.textSample, values);


        // Assign adapter to ListView
        ListView listView = (ListView) android.findViewById(R.id.historyList);
        listView.setAdapter(adapter);
        return android;
    }
}