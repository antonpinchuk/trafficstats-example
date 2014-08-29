package pro.kinect.trafficstats;

import android.graphics.drawable.Drawable;
import android.net.TrafficStats;

public class ApplicationItem {

    long tx = 0;
    long rx = 0;
    long total = 0;
    int uid;
    String Name;
    Drawable Icon;

    ApplicationItem(int _uid, String _name, long _tx, long _rx, Drawable _icon){
        uid = _uid; Name = _name;
        tx = _tx; rx = _rx;
        total = tx + rx;
        Icon = _icon;
    }

    public void update(){
        tx = TrafficStats.getUidTxBytes(uid);
        rx = TrafficStats.getUidRxBytes(uid);
        total = tx + rx;
    }

    public static ApplicationItem create(int _uid ,String _name,Drawable _icon) {
        long _tx = TrafficStats.getUidTxBytes(_uid);
        long _rx = TrafficStats.getUidRxBytes(_uid);
        long total = _tx + _rx;
        if(total / 1024 > 0) {
            return new ApplicationItem(_uid, _name, _rx, _rx, _icon);
        }
        return null;
    }

}
