package jp.ac.it_college.std.ikemen.reachable_client;


import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class DrawerItemClickListener implements AdapterView.OnItemClickListener {

    private final Activity mActivity;
    private final ListView mDrawerList;
    private final DrawerLayout mDrawerLayout;
    private final Toolbar mToolbar;
    private final String[] mSideMenuTitles;

    public DrawerItemClickListener(Activity activity, ListView drawerList,DrawerLayout drawerLayout,
                                   Toolbar toolbar, String[] sideMenuTitles) {
        this.mActivity = activity;
        this.mDrawerList = drawerList;
        this.mDrawerLayout = drawerLayout;
        this.mToolbar = toolbar;
        this.mSideMenuTitles = sideMenuTitles;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        selectItem(SideMenuItems.values()[position]);
    }

    /**
     * SideMenuItems列挙子に応じた処理をする
     * @param items
     */
    private void selectItem(SideMenuItems items) {
        mDrawerLayout.closeDrawers();
    }
}
