package jp.ac.it_college.std.ikemen.reachable_client;

import android.app.LoaderManager;
import android.content.Loader;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.amazonaws.auth.AWSCredentials;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<AWSCredentials> {

    private static final String TAG = "Tag_MainActivity";
    private static final int COGNITO_ASYNC_TASK_LOADER_ID = 0;

    /* DrawerLayout 関連フィールド */
    private String[] mSideMenuTitles;
    private ListView mDrawerList;
    private ArrayAdapter<String> mSideMenuArrayAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    /* Toolbar 関連フィールド */
    private Toolbar mToolbar;

    /* ProgressDialogFragment 関連フィールド */
    private ProgressDialogFragment mProgressDialog;
    private static final String TAG_PROGRESS_DIALOG_FRAGMENT = "progress_dialog";
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container_content, new CouponListFragment())
                    .commit();
        }

        //初期設定
        initSettings();
    }

    /**
     * 初期設定を実行
     */
    private void initSettings() {
        //ToolBarを設定
        setUpToolbar();
        //DrawerListenerにDrawerToggleをセット
        getDrawerLayout().setDrawerListener(getDrawerToggle());
        //サイドメニューを設定
        setUpDrawerList();
        // Credentialを取得するLoaderを起動
        getLoaderManager().initLoader(COGNITO_ASYNC_TASK_LOADER_ID, null, this);
    }

    /**
     * Toolbarをアクションバーとしてセットする
     */
    private void setUpToolbar() {
        setSupportActionBar(getToolbar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    /**
     * サイドメニューを設定する
     */
    private void setUpDrawerList() {
        //サイドメニュー用ArrayAdapterをセット
        getDrawerList().setAdapter(getSideMenuArrayAdapter());
        //サイドメニューのonItemClickListenerをセット
        getDrawerList().setOnItemClickListener(new DrawerItemClickListener(
                this, getDrawerList(), getDrawerLayout(), getToolbar(), getSideMenuTitles()));
    }

    public DrawerLayout getDrawerLayout() {
        if (mDrawerLayout == null) {
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        }

        return mDrawerLayout;
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        if (mDrawerToggle == null) {
            mDrawerToggle = new DrawerToggle(
                    this, getDrawerLayout(), getToolbar(), R.string.drawer_open, R.string.drawer_close);
        }

        return mDrawerToggle;
    }

    public Toolbar getToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
        }

        return mToolbar;
    }

    public String[] getSideMenuTitles() {
        if (mSideMenuTitles == null) {
            mSideMenuTitles = getResources().getStringArray(R.array.side_menu_titles);
        }
        return mSideMenuTitles;
    }

    public ListView getDrawerList() {
        if (mDrawerList == null) {
            mDrawerList = (ListView) findViewById(R.id.side_menu_list);
        }
        return mDrawerList;
    }

    public ArrayAdapter<String> getSideMenuArrayAdapter() {
        if (mSideMenuArrayAdapter == null) {
            mSideMenuArrayAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, getSideMenuTitles());
        }
        return mSideMenuArrayAdapter;
    }

    public ProgressDialogFragment getProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialogFragment.newInstance(
                    getString(R.string.dialog_title_credentials),
                    getString(R.string.dialog_message_credentials));
        }
        return mProgressDialog;
    }

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(getMainLooper());
        }
        return mHandler;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getDrawerToggle().onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (getDrawerToggle().onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /* Implemented LoaderManager.LoaderCallbacks */
    @Override
    public Loader<AWSCredentials> onCreateLoader(int i, Bundle bundle) {
        Log.d(TAG, "onCreateLoader");
        //ProgressDialogを表示
        getProgressDialog().show(getFragmentManager(), TAG_PROGRESS_DIALOG_FRAGMENT);
        return new CognitoAsyncTaskLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<AWSCredentials> loader, AWSCredentials awsCredentials) {
        Log.d(TAG, "onLoadFinished");
        Log.d(TAG, "AccessKeyId = " + awsCredentials.getAWSAccessKeyId());
        Log.d(TAG, "SecretKey = " + awsCredentials.getAWSSecretKey());

        getHandler().post(new Runnable() {
            @Override
            public void run() {
                //ProgressDialogを非表示
                getProgressDialog().dismiss();
            }
        });

        //Loaderを破棄
        getLoaderManager().destroyLoader(COGNITO_ASYNC_TASK_LOADER_ID);
    }

    @Override
    public void onLoaderReset(Loader<AWSCredentials> loader) {

    }
}
