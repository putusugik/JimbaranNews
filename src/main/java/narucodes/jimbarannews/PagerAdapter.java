package narucodes.jimbarannews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by User on 8/1/2018.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int noTab;

    public PagerAdapter (FragmentManager fm, int NoTab){
        super(fm);
        this.noTab = NoTab;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Main_Pusat mainPusat = new Main_Pusat();
                return mainPusat;
            case 1:
                Main_Desa mainDesa = new Main_Desa();
                return mainDesa;
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return noTab;
    }
}
