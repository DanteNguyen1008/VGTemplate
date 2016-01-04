package com.jpay.videograms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jpay.videograms.adapters.ViewPagerAdapter;
import com.jpay.videograms.controllers.JPayMainActivityController;
import com.jpay.videograms.models.Videograms;
import com.jpay.videograms.utils.SyncStatus;
import com.jpay.videograms.viewmodels.ViewModelManager;
import com.jpay.videograms.views.ContactFragmentView;
import com.jpay.videograms.views.VideogramsListFragmentView;
import com.jpay.mvcvm.exceptions.MissingControllerException;
import com.jpay.mvcvm.utils.JLog;
import com.jpay.mvcvm.views.JView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class JPayMainActivity extends AppCompatActivity implements JView {
    private static final String TAG = JPayMainActivity.class.getSimpleName();

    /** End App Structure */

	/* single loading dialog */
    private ProgressDialog mProgressDialog;

    private boolean mIsVisible = false;
    private JPayMainActivityController mController;
    private DrawerLayout mDrawer;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ViewPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            mController = JPayMainActivityController.Instance();
            mController.onCreate(this);
        } catch (MissingControllerException e) {
            JLog.printStrackTrace(e);
        }

		/* start the VM manager */
        ViewModelManager.getInstance().onCreate();

        initializeToolbar();
        initializeDrawers();
        initializeUniversalImageLoader();
    }

    /**
     * Set up toolbar:
     *
     * Because of the bug of toolbar and tablayout so we can not use them together,
     * the toolbar here is a fake one
     */
    private void initializeToolbar() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(VideogramsListFragmentView.instance(SyncStatus.ON_DEVICE), "ON DEVICE");
        mPagerAdapter.addFragment(VideogramsListFragmentView.instance(SyncStatus.NOT_AVAILABLE), "NOT ON DEVICE");

        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * Initial left drawer layout with contact view fragment
     */
    private void initializeDrawers() {
        /* get drawer */
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        /* drawer always open */
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        mDrawer.setScrimColor(Color.TRANSPARENT);
        /* Load drawer fragment */
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.left_drawer, ContactFragmentView.instance());
        ft.commit();
    }

    /**
     * Initial UIL
     */
    private void initializeUniversalImageLoader() {
        /* Initialize UIL */
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(320, 240)
                .diskCacheExtraOptions(240, 320, null)
                .threadPoolSize(10)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSizePercentage(13)
                .defaultDisplayImageOptions(
                        new DisplayImageOptions.Builder()
                                .resetViewBeforeLoading(false)
                                .delayBeforeLoading(0)
                                .cacheInMemory(true)
                                .cacheOnDisk(false)
                                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                                .bitmapConfig(Bitmap.Config.RGB_565)
                                .displayer(new SimpleBitmapDisplayer())
                                .showImageForEmptyUri(R.mipmap.ic_launcher)
                                .showImageOnFail(R.mipmap.ic_launcher)
                                .handler(new Handler())
                                .build())
                .writeDebugLogs().build();

        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.activity_menu_squares, menu);
        return false;
    }

    /**
     * When you successfully handle a menu item, return true. If you don't
     * handle the menu item, you should call the superclass implementation of
     * onOptionsItemSelected() (the default implementation returns false
     *
     * (non-Javadoc)
     *
     * @see Activity#onOptionsItemSelected(MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    /**
     * Show loading dialog with isAllowCancel = false as default
     *
     * @param title The title of the dialog.
     * @param message The body of the dialog.
     * @param isIndeterminate If the dialog should persist forever.
     */
    public void showLoadingDialog(final String title, final String message, final boolean isIndeterminate) {
        showLoadingDialog(title, message, isIndeterminate, false);
    }

    /**
     * Show main loading dialog with given title, message
     *
     * @param title The title of the dialog.
     * @param message The body of the dialog.
     * @param isIndeterminate If the dialog should persist forever.
     * @param isAllowCancel If the dialog can be canceled by the user.
     */
    public void showLoadingDialog(final String title, final String message, final boolean isIndeterminate, final boolean isAllowCancel) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    if (mProgressDialog != null) {
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }

                        mProgressDialog = null;
                    }

                    mProgressDialog = new ProgressDialog(JPayMainActivity.this);
                    mProgressDialog.setTitle(title);
                    mProgressDialog.setMessage(message);
                    mProgressDialog.setIndeterminate(isIndeterminate);
                    mProgressDialog.setCancelable(isAllowCancel);

                    mProgressDialog.show();
                } catch (Exception e) {
                    JLog.printStrackTrace(e);
                }
            }
        });

    }

    /**
     * Hide current dialog
     */
    public void hideLoadingDialog() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    if (mProgressDialog != null) {
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }

                        mProgressDialog = null;
                    }
                } catch (Exception e) {
                    JLog.printStrackTrace(e);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mController != null) {
            mController.onResume();
        }
        mIsVisible = true;
    }

    @Override
    public void onDestroy() {
        if (mController != null) {
            mController.onDestroy();
        }
		/* end processing of VM when app closed */
        ViewModelManager.getInstance().onClose();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsVisible = false;
    }

    public boolean isVisible() {
        return mIsVisible;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public void displayGenericError() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(JPayMainActivity.this, "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Closes the left category menu.
     */
    public void closeLeftMenu() {
        mDrawer.closeDrawers();
    }

    /**
     * Opens the left category menu.
     */
    public void openLeftMenu() {
        if(!mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.openDrawer(GravityCompat.START);
        } else {
            closeLeftMenu();
        }
    }

    public void openAndPlayVideo(Videograms videograms, int position) {
        if(videograms == null) {
            return;
        }

        Intent intent = new Intent(this, VideoPlayerActivity.class);
        Bundle data = new Bundle();
        data.putParcelable(Constants.EXTRA_INTENT_VIDEOGRAMS, videograms);
        data.putInt(Constants.EXTRA_INTENT_VIDEOGRAMS_POSITION, position);
        intent.putExtra(Constants.EXTRA_INTENT_VIDEOGRAMS_BUNDLE, data);
        startActivity(intent);
    }
}
