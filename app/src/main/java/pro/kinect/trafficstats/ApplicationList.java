package com.truumobile.datausage;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.common.Networks;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ApplicationList {

    private Context mContext;
    private Timer mTimer;
    private TimerTask mTask;

    private int TIME_APPLICATION_UPDATE = 3 * 1000; // 3 second

    private boolean isWifiEnabled = false;
    private boolean isMobile = false;

    private List<ApplicationItem> mApplicationItemList = new ArrayList<ApplicationItem>();

    public ApplicationList(Context context) {
        mContext = context;
        updateNetworkState();
    }

    public void start() {
        mTask = new TimerTask() {
            @Override
            public void run() {
                update();
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTask, 0, TIME_APPLICATION_UPDATE);
    }

    public void stop() {
        if (mTimer != null) {
            mTimer.purge();
            mTimer.cancel();
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
    }

    public void update() {
        updateNetworkState();
        if (mApplicationItemList != null) {
            for (int i = 0, l = mApplicationItemList.size(); i < l; i++) {
                mApplicationItemList.get(i).update(isMobile);
            }
        } else {
            for (ApplicationInfo app : mContext.getPackageManager().getInstalledApplications(0)) {
                ApplicationItem item = new ApplicationItem(app);
                mApplicationItemList.add(item);
            }
        }
    }

    private void updateNetworkState() {
        isMobile = Networks.isConnectedMobile(mContext);
    }

    public List<ApplicationItem> getList() {
        return mApplicationItemList;
    }

}
