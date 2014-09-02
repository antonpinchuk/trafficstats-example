package pro.kinect.trafficstats;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;

public class ApplicationItem {
    private long tx = 0;
    private long rx = 0;
    private ApplicationInfo app;

    public ApplicationItem(ApplicationInfo _app) {
        app = _app;
        tx = TrafficStats.getUidTxBytes(app.uid);
        rx = TrafficStats.getUidRxBytes(app.uid);
    }

    public  ApplicationItem(ApplicationInfo _app, long _tx, long _rx) {
        app = _app; tx = _tx; rx = _rx;
    }

    public void update() {
        tx = TrafficStats.getUidTxBytes(app.uid);
        rx = TrafficStats.getUidRxBytes(app.uid);
    }

    public static ApplicationItem create(ApplicationInfo _app){
        long _tx = TrafficStats.getUidTxBytes(_app.uid);
        long _rx = TrafficStats.getUidRxBytes(_app.uid);

        if((_tx + _rx) > 0) return new ApplicationItem(_app, _tx, _rx);
        return null;
    }

    public int getUsageKb() {
        return Math.round((tx + rx)/ 1024);
    }

    public String getApplicationLabel(PackageManager _packageManager) {
        return _packageManager.getApplicationLabel(app).toString();
    }

    public Drawable getIcon(PackageManager _packageManager) {
        return _packageManager.getApplicationIcon(app);

    }
}
