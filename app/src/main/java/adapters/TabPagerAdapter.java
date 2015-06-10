package adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.concon.talkabout.talkabout.R;
import com.concon.talkabout.talkabout.SpinWheelGameplayTAB;
import com.concon.talkabout.talkabout.SpinWheelHistoryTAB;

/**
 * Created by OE on 28/04/2015.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private Context context;
    private String tabTitles[];

    public TabPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        tabTitles = new String[]{context.getResources().getString(R.string.playTab), context.getString(R.string.historyTab)};
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SpinWheelGameplayTAB();
            case 1:
                return new SpinWheelHistoryTAB();
        }
        return null;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}