package pro.kinect.trafficstats;

import android.graphics.drawable.Drawable;
import android.net.TrafficStats;

public class ApplicationItem {

    long tx = 0;
    long rx = 0;
    long dataUsage = 0;
    int uid;
    String name;
    Drawable icon;

    ApplicationItem(int _uid, String _name, long _rx, long _tx, Drawable _icon) {
        uid = _uid;
        name = _name;
        tx = _tx;
        rx = _rx;
        dataUsage = tx + rx;
        icon = _icon;
    }

    public void update() {
        tx = TrafficStats.getUidTxBytes(uid);
        rx = TrafficStats.getUidRxBytes(uid);
        dataUsage = tx + rx;
    }

    public static ApplicationItem create(int _uid, String _name, Drawable _icon) {
        long _rx = TrafficStats.getUidRxBytes(_uid);
        long _tx = TrafficStats.getUidTxBytes(_uid);
        return new ApplicationItem(_uid, _name, _rx, _tx, _icon);
    }

}
