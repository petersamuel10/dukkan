package com.example.dukkanexpress;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.dukkanexpress.mainFragments.AboutApp;
import com.example.dukkanexpress.mainFragments.Home;
import com.example.dukkanexpress.mainFragments.MyList;
import com.example.dukkanexpress.mainFragments.Offers;
import com.example.dukkanexpress.mainFragments.User;

import java.util.HashMap;
import java.util.Locale;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mainFragment)
    FrameLayout mainFragment;

    private HashMap<String, Stack<Fragment>> mStacks;
    private String mCurrentTab;
    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocal();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Common.mActivity = this;
        if(Paper.book("dukkan").contains("current_user"))
            Common.currentUser = Paper.book("dukkan").read("current_user");

        mStacks = new HashMap<String, Stack<Fragment>>();
        mStacks.put(Constant.TAB_HOME, new Stack<Fragment>());
        mStacks.put(Constant.TAB_OFFERS, new Stack<Fragment>());
        mStacks.put(Constant.TAB_MYLIST, new Stack<Fragment>());
        mStacks.put(Constant.TAB_ABOUTAPP, new Stack<Fragment>());
        mStacks.put(Constant.TAB_USER, new Stack<Fragment>());

        navView.setSelectedItemId(R.id.navigation_home);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectTap(Constant.TAB_HOME);
                    return true;
                case R.id.navigation_offers:
                    selectTap(Constant.TAB_OFFERS);
                    return true;
                case R.id.navigation_myList:
                    selectTap(Constant.TAB_MYLIST);
                    return true;
                case R.id.navigation_about_app:
                    selectTap(Constant.TAB_ABOUTAPP);
                    return true;
                case R.id.navigation_user:
                    selectTap(Constant.TAB_USER);
                    return true;
            }
            return false;
        }
    };

    private void selectTap(String tabId) {

        // if press twice in the same tab
        if (mCurrentTab == tabId) {

            Fragment fragment = mStacks.get(mCurrentTab).elementAt(0);
            mStacks.get(tabId).clear();
            pushFragments(tabId, fragment, true);

        } else {

            mCurrentTab = tabId;

            if (mStacks.get(tabId).size() == 0) {
                /*
                 *    First time this tab is selected. So add first fragment of that tab.
                 *    Dont need animation, so that argument is false.
                 *    We are adding a new fragment which is not present in stack. So add to stack is true.
                 */
                if (tabId.equals(Constant.TAB_HOME)) {
                    pushFragments(tabId, new Home(), true);
                } else if (tabId.equals(Constant.TAB_OFFERS)) {
                    pushFragments(tabId, new Offers(), true);
                } else if (tabId.equals(Constant.TAB_MYLIST)) {
                    pushFragments(tabId, new MyList(), true);
                } else if (tabId.equals(Constant.TAB_ABOUTAPP)) {
                    pushFragments(tabId, new AboutApp(), true);
                } else if (tabId.equals(Constant.TAB_USER)) {
                    pushFragments(tabId, new User(), true);
                }
            } else {

                /*
                 *    We are switching tabs, and target tab is already has atleast one fragment.
                 *    No need of animation, no need of stack pushing. Just show the target fragment
                 */
                pushFragments(tabId, mStacks.get(tabId).lastElement(), false);
            }
        }

    }

    public void pushFragments(String tag, Fragment fragment, boolean shouldAdd) {
        if (shouldAdd)
            mStacks.get(tag).push(fragment);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.mainFragment, fragment);
        ft.commit();
    }

    public void popFragments() {
        /*
         *    Select the second last fragment in current tab's stack..
         *    which will be shown after the fragment transaction given below
         */
        Fragment fragment = mStacks.get(mCurrentTab).elementAt(mStacks.get(mCurrentTab).size() - 2);

        /*pop current fragment from stack.. */
        mStacks.get(mCurrentTab).pop();

        /* We have the target fragment in hand.. Just show it.. Show a standard navigation animation*/
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.mainFragment, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (mStacks.get(mCurrentTab).size() == 1) {
            if (mCurrentTab == Constant.TAB_HOME) {
                // We are already showing first fragment of current tab, so when back pressed, we will finish this activity..
                finish();
                return;
            } else {
                navView.setSelectedItemId(R.id.home);
            }
        } else
            // Goto previous fragment in navigation stack of this tab
            popFragments();
    }

    public void setNewLocale(String language) {
        Paper.book("dukkan").write("language", language);

        Intent i = this.getPackageManager()
                .getLaunchIntentForPackage(this.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i);
    }
    public void setLanguage(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        Paper.book("villa_vanilia").write("language", lang);

    }
    public void loadLocal() {
        Paper.init(this);
        String lan = Paper.book("dukkan").read("language");
        if (!TextUtils.isEmpty(lan)) {
            setLanguage(lan);
            if (lan.equals("ar"))
                Common.isArabic = true;
            else
                Common.isArabic = false;
        } else {
            setLanguage("en");
            Common.isArabic = false;
        }
    }
}
