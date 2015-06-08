package adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.concon.talkabout.talkabout.CustomRulesFragment;
import com.concon.talkabout.talkabout.R;
import com.concon.talkabout.talkabout.ServerRules;

/**
 * Created by Pablitoh on 02/06/2015.
 */
public class RulesPageAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private Context context;
    private String tabTitles[];

    public RulesPageAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        this.context = context;
        tabTitles = new String[] { context.getString(R.string.myRulesTabName), context.getString(R.string.PopularToday), context.getString(R.string.Recent) };
    }

    @Override
    public int getCount()
    {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new CustomRulesFragment();
            case 1:
                return ServerRules.newInstance(0);
            case 2:
                return ServerRules.newInstance(1);
        }
        return null;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


}
