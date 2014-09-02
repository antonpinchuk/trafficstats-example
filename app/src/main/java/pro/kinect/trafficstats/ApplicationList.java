package pro.kinect.trafficstats;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ApplicationList {

    Context mContext;
    private Timer mTimer;
    private TimerTask mTask;

    private int TIME_APPLICATION_UPDATE = 3 * 1000; // 3 second

    private List<ApplicationItem> mApplicationItemList = new ArrayList<ApplicationItem>();

    public ApplicationList(Context _context) {
        mContext = _context;
    }

    public void Start() {
        mTask = new TimerTask() {
            @Override
            public void run() {
                update();
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTask, 0, TIME_APPLICATION_UPDATE);
    }

    public void Stop() {
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
        if(mApplicationItemList != null) {
            for (int i = 0, l = mApplicationItemList.size(); i < l; i++) {
                mApplicationItemList.get(i).update();
            }
        } else {
            for (ApplicationInfo app : mContext.getPackageManager().getInstalledApplications(0)) {
                ApplicationItem item = new ApplicationItem(app);
                mApplicationItemList.add(item);
            }
        }
    }

    public List<ApplicationItem> getList() {
        return mApplicationItemList;
    }
}
