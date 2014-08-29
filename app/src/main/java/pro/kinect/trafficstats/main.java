package pro.kinect.trafficstats;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.net.TrafficStats;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;


public class main extends Activity {

    private TextView tvSupported, tvDataUsageWiFi, tvDataUsageMobile, tvDataUsageTotal;
    private ListView lvApplications;

    private long totalCurrent = 0;

    private ArrayList<ApplicationItem> Applications =  new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSupported = (TextView) findViewById(R.id.tvSupported);
        tvDataUsageWiFi = (TextView) findViewById(R.id.tvDataUsageWiFi);
        tvDataUsageMobile = (TextView) findViewById(R.id.tvDataUsageMobile);
        tvDataUsageTotal = (TextView) findViewById(R.id.tvDataUsageTotal);

        lvApplications = (ListView) findViewById(R.id.lvInstallApplication);

        final ArrayAdapter<ApplicationItem> adapterApplications = new ArrayAdapter<ApplicationItem>(getApplicationContext(), R.layout.item_install_application) {
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

                ApplicationItem app =  getItem(position);

                tvAppName.setCompoundDrawablesWithIntrinsicBounds(app.Icon, null , null, null);
                tvAppName.setText(app.Name);
                tvAppTraffic.setText(Long.toString(app.total / 1024) + " Kb");

                return result;
            }
            @Override
            public int getCount() {
                return super.getCount();
            }
        };

        updateAdapter(adapterApplications);

        lvApplications.setAdapter(adapterApplications);

        if (TrafficStats.getTotalTxBytes() != TrafficStats.UNSUPPORTED) {
            handler.postDelayed(runnable, 0);
        } else {
            tvSupported.setText("Not supported");
            tvSupported.setTextColor(ColorStateList.valueOf(0xFFFF0000));
        }
    }

    public void updateAdapter(ArrayAdapter<ApplicationItem> _adapter){
        for (ApplicationInfo app : getApplicationContext().getPackageManager().getInstalledApplications(0)) {
            PackageManager packageManager = getApplicationContext().getPackageManager();
            String name = packageManager.getApplicationLabel(app).toString();
            Drawable icon = packageManager.getApplicationIcon(app);
            ApplicationItem application = ApplicationItem.create(app.uid, "  " + name, icon);
            if(application != null) {
                _adapter.add(application);
            }
        }

        _adapter.sort(new Comparator<ApplicationItem>() {
            @Override
            public int compare(ApplicationItem lhs, ApplicationItem rhs) {
                return (int)(rhs.total - lhs.total);
            }
        });
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

                /*
                ArrayAdapter<Application> adapter = (ArrayAdapter<Application>) lvApplications.getAdapter();
                adapter.clear();
                updateAdapter(adapter);
                adapter.notifyDataSetChanged();
                */

            }

            handler.postDelayed(runnable, 3000);
        }
    };

}
