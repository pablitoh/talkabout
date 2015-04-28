package adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by OE on 28/04/2015.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0: Fragment firstTab = new FirstTab();

                return new FirstTab();
            case 1:
                return new SecondTab();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
