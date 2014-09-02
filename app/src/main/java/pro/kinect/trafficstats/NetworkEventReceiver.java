package pro.kinect.trafficstats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class NetworkEventReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener() {
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
        }, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);

    }
}
