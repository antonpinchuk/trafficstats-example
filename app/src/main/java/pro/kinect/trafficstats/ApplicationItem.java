package com.truumobile.datausage;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;

public class ApplicationItem {
    /**
     * Absolute since device boot
     */
    private long tx = 0;
    private long rx = 0;

    /**
     * Absolute since device boot
     */
    private long txLast = 0;
    private long rxLast = 0;

    /**
     * Resettable counters
     */
    private long txWifi = 0;
    private long rxWifi = 0;
    private long txMobile = 0;
    private long rxMobile = 0;

    private ApplicationInfo applicationInfo;

    public ApplicationItem(ApplicationInfo _app) {
        applicationInfo = _app;

        // First time get current counter
        rxLast = TrafficStats.getUidRxBytes(applicationInfo.uid);
        txLast = TrafficStats.getUidTxBytes(applicationInfo.uid);
    }

    public void update(boolean isMobile) {
        long rx = TrafficStats.getUidRxBytes(applicationInfo.uid);
        long tx = TrafficStats.getUidTxBytes(applicationInfo.uid);

        long rxDelta = rx - rxLast;
        long txDelta = tx - txLast;

        if (isMobile) {
            rxMobile = rxMobile + rxDelta;
            txMobile = txMobile + txDelta;
        } else {
            rxWifi = rxWifi + rxDelta;
            txWifi = txWifi + txDelta;
        }

        rxLast = rx;
        txLast = tx;
    }



//    public static ApplicationItem create(ApplicationInfo _applicationInfo) {
//        long _tx = TrafficStats.getUidTxBytes(_applicationInfo.uid);
//        long _rx = TrafficStats.getUidRxBytes(_applicationInfo.uid);
//
//        if ((_tx + _rx) > 0) return new ApplicationItem(_applicationInfo);
//        return null;
//    }

    public int getTotalUsageKb() {
        return Math.round((tx + rx) / 1024);
    }
    public int getMobileUsageKb() {
        return Math.round((txMobile + rxMobile) / 1024);
    }
    public int getWifiUsageKb() {
        return Math.round((txWifi + rxWifi) / 1024);
    }

    public String getApplicationLabel(PackageManager packageManager) {
        return packageManager.getApplicationLabel(applicationInfo).toString();
    }

    public Drawable getIcon(PackageManager packageManager) {
        return packageManager.getApplicationIcon(applicationInfo);
    }

}
