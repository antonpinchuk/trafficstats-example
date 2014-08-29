package pro.kinect.trafficstats;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.net.TrafficStats;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class main extends Activity {

    private TextView tvSupported, tvDataUsageWiFi, tvDataUsageMobile, tvDataUsageTotal;
    private ListView lvApplications;

    private long totalCurrent = 0;

    private class Application {
        long tx = 0;
        long rx = 0;
        long total = 0;
        int uid;
        String Name;

        Application(int _uid ,String _name){
            uid = _uid; Name = _name;
        }

        public void update(){
            tx = TrafficStats.getUidTxBytes(uid);
            rx = TrafficStats.getUidRxBytes(uid);
            total = tx + rx;
        }
    }

    private ArrayList<Application> Applications =  new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSupported = (TextView) findViewById(R.id.tvSupported);
        tvDataUsageWiFi = (TextView) findViewById(R.id.tvDataUsageWiFi);
        tvDataUsageMobile = (TextView) findViewById(R.id.tvDataUsageMobile);
        tvDataUsageTotal = (TextView) findViewById(R.id.tvDataUsageTotal);

        lvApplications = (ListView) findViewById(R.id.lvInstallApplication);

        if (TrafficStats.getTotalTxBytes() != TrafficStats.UNSUPPORTED) {
            handler.postDelayed(runnable, 0);
        } else {
            tvSupported.setText("Not supported");
            tvSupported.setTextColor(ColorStateList.valueOf(0xFFFF0000));
        }

        final ArrayAdapter<Application> adapterApplications = new ArrayAdapter<Application>(getApplicationContext(), R.layout.item_install_application) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                final View result;

                if (convertView == null) {
                    result = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_install_application, parent, false);
                } else {
                    result = convertView;
                }

                TextView tvAppName = (TextView) result.findViewById(R.id.tvAppName);
                TextView tvAppTraffic = (TextView) result.findViewById(R.id.tvAppTraffic);

                Application app = (Application) getItem(position);

                tvAppName.setText(app.Name);
                tvAppTraffic.setText(Long.toString(app.total / 1024) + " Kb");

                return result;
            }
            @Override
            public int getCount() {
                return super.getCount();
            }
        };

        for (ApplicationInfo app : getApplicationContext().getPackageManager().getInstalledApplications(0)) {
            PackageManager packageManager = getApplicationContext().getPackageManager();
            String name = packageManager.getApplicationLabel(app).toString();
            Application application = new Application(app.uid, name);
            application.update();
            if( (application.total / 1024) > 0) adapterApplications.add(application);
        }

        adapterApplications.sort(new Comparator<Application>() {
            @Override
            public int compare(Application lhs, Application rhs) {
                return (int)(rhs.total - lhs.total);
            }
        });

        lvApplications.setAdapter(adapterApplications);
    }

    public Handler handler = new Handler();

    public Runnable runnable = new Runnable() {
        public void run() {
            long mobile = TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes();
            long total = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
            tvDataUsageWiFi.setText("" + (total - mobile) / 1024 + " Kb");
            tvDataUsageMobile.setText("" + mobile / 1024 + " Kb");
            tvDataUsageTotal.setText("" + total / 1024 + " Kb");

            if(totalCurrent != total) {
                totalCurrent = total;


            }

            handler.postDelayed(runnable, 3000);
        }
    };

}
