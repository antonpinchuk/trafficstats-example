package pro.kinect.trafficstats;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class NetworkEventListener extends PhoneStateListener {

    @Override
    public void onDataConnectionStateChanged(int state) {
        switch(state) {
            case TelephonyManager.DATA_DISCONNECTED: {
                    //3G has been turned OFF
                }
                break;
            case TelephonyManager.DATA_CONNECTING: {
                    //3G is connecting
                }
                break;
            case TelephonyManager.DATA_CONNECTED: {
                    //3G has turned ON
                }
                break;
        }
    }
}
