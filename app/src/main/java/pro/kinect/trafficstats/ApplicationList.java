package pro.kinect.trafficstats;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
    private boolean isMobilEnabled = false;

    private List<ApplicationItem> mApplicationItemList = new ArrayList<ApplicationItem>();

    public ApplicationList(Context _context) {
        mContext = _context;
        updateNetworkState();
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
        updateNetworkState();
        if(mApplicationItemList != null) {
            for (int i = 0, l = mApplicationItemList.size(); i < l; i++) {
                mApplicationItemList.get(i).setMobilTraffic(isMobilEnabled);
                mApplicationItemList.get(i).update();
            }
        } else {
            for (ApplicationInfo app : mContext.getPackageManager().getInstalledApplications(0)) {
                ApplicationItem item = new ApplicationItem(app);
                item.setMobilTraffic(isMobilEnabled);

                mApplicationItemList.add(item);
            }
        }
    }

    private void updateNetworkState() {
        isWifiEnabled = isConnectedWifi();
        isMobilEnabled = isConnectedMobile();
    }

    public List<ApplicationItem> getList() {
        return mApplicationItemList;
    }

    public boolean isConnectedWifi(){
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public boolean isConnectedMobile(){
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

}
