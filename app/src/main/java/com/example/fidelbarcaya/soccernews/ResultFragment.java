package com.example.fidelbarcaya.soccernews;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class ResultFragment extends Fragment {

    private static final String LOG_TAG = ResultFragment.class.getSimpleName();
    private ArrayAdapter<String> arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                GetResultTask task = new GetResultTask();
                task.execute();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public ResultFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.result_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        String[] results = {"Wilster 1 - Aurora 1",
                "Wilster 1 - Aurora 1",
                "Wilster 1 - Aurora 1",
                "Wilster 1 - Aurora 1"};
        arrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.result_view,
                R.id.result_view_text,
                new ArrayList<String>(Arrays.asList(results)));
        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
        listView.setAdapter(arrayAdapter);
        return rootView;
    }

    class GetResultTask extends AsyncTask<Void,Void,String[]>{


        @Override
        protected String[] doInBackground(Void... params) {
            String resultString = Utility.getJsonStringFromNetwork();
            Log.v(LOG_TAG, resultString);

            try {
                return Utility.parseFixtureJson(resultString);
                /*
                String[] results = {"Boliviar 1 - tigre 1",
                        "Boliviar 1 - tigre 1",
                        "Boliviar 1 - tigre 1",
                        "Boliviar 1 - tigre 1",
                        "Boliviar 1 - tigre 1",
                        "Boliviar 1 - tigre 1",
                        "Boliviar 1 - tigre 1"
                }; */
                //return results;


           }catch (JSONException e)
            {
                Log.v(LOG_TAG,"Error parsing" + e.getMessage(), e);
                e.printStackTrace();
                return new String[] {"No Data"};
            }

        }
        @Override
        protected void onPostExecute(String[] strings) {
             arrayAdapter.clear();
            for(String result : strings)
            {
                arrayAdapter.add(result);

            }
        }

    }
}

