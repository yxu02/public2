package edu.sjsu.xuy87.myapplication;

import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements AdapterView.OnItemClickListener {

        private ArrayAdapter<RunningServiceWrapper> mServices;
        private ListView mServiceList;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            mServices = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mServiceList = (ListView)rootView.findViewById(R.id.service_list);
            mServiceList.setAdapter(mServices);
            mServiceList.setOnItemClickListener(this);
            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            Context ctx = getActivity().getApplicationContext();
            ActivityManager mgr =
                    (ActivityManager)ctx.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> srvInfo =
                    mgr.getRunningServices(Integer.MAX_VALUE);
            mServices.clear();
            for (ActivityManager.RunningServiceInfo curSrv : srvInfo) {
                RunningServiceWrapper wrap =
                        new RunningServiceWrapper(curSrv);
                mServices.add(wrap);
            }
            mServices.notifyDataSetChanged();
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            RunningServiceWrapper curItem = mServices.getItem(position);
            ServiceDetailFragment frag = ServiceDetailFragment.newInstance(curItem.getInfo());
            FragmentManager fm = getActivity().getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.container, frag);
            ft.addToBackStack(frag.getClass().getSimpleName());
            ft.commit();
        }

        private class RunningServiceWrapper {
            private ActivityManager.RunningServiceInfo mInfo;

            public RunningServiceWrapper(ActivityManager.RunningServiceInfo info) {
                mInfo = info;
            }

            public ActivityManager.RunningServiceInfo getInfo() {
                return mInfo;
            }

            public String toString() {
                return mInfo.service.flattenToShortString();
            }
        }
    }
}
/*public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}*/
