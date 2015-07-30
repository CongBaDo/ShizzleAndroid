package com.shizzelandroid.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

import com.shizzelandroid.R;
import com.shizzelandroid.adapter.ItemAdapter;
import com.shizzelandroid.database.AppDBHelper;
import com.shizzelandroid.database.AppDataSource;
import com.shizzelandroid.utils.AppRequestTask;
import com.shizzelandroid.utils.EbayParser;
import com.shizzelandroid.utils.Listing;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final String TAG = "MainActivityFragment";
    private ListView listView;
    private ItemAdapter adapter;
    private EbayParser parser;
    private ArrayList<Listing> datas;
    private AppRequestTask task;
    private ProgressDialog loadingDialog;
    private SearchView searchView;
    private AppDataSource dataSource;

    public void showLoading() {
        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setMessage("Loading...");
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    public void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }


    public MainActivityFragment() {
    }

    private AppRequestTask.RequestCallback callback = new AppRequestTask.RequestCallback() {
        @Override
        public void onResultPost(JSONObject statusObj) {
            Log.v(TAG, "onResultPost " + statusObj);

            //hideKeyboard();
            dismissLoading();
            datas = parser.parseData(statusObj);

            if (datas.size() == 0) {
                return;
            }
            adapter = new ItemAdapter(getActivity(), datas);
            listView.setAdapter(adapter);
        }

        @Override
        public void fail() {
            dismissLoading();
            Toast.makeText(getActivity(), "Your request fail, please try again", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main,
                container, false);
        Log.e(TAG, "onCreateView ");


        AppDBHelper helper = new AppDBHelper(getActivity());
        SQLiteDatabase database = helper.getWritableDatabase();
        dataSource = new AppDataSource(database);
//        List list = dataSource.read();
//        dataSource.insert()
        helper.close();
        database.close();

        listView = (ListView) view.findViewById(R.id.listView);
        parser = new EbayParser(getActivity());

        task = new AppRequestTask("fantasy", callback);
        task.execute();

        showLoading();
        listView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                //Log.v(TAG, "onScrollState ");
                hideKeyboard();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                //Log.i(TAG, "onScroll ");
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e(TAG, "onQueryTextSubmit" + query);

                if (task != null) {
                    showLoading();
                    task.onCancelled();
                    task = new AppRequestTask(query.trim(), callback);
                    task.execute();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e(TAG, "onQueryTextChange" + newText);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:
                // do s.th.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }
}
