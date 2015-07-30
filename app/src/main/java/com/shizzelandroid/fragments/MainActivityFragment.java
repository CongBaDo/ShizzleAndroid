package com.shizzelandroid.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

import com.shizzelandroid.R;
import com.shizzelandroid.adapter.ItemAdapter;
import com.shizzelandroid.database.AppDataSource;
import com.shizzelandroid.utils.EbayParser;
import com.shizzelandroid.utils.EbayRequestLoader;
import com.shizzelandroid.utils.Listing;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Listing>>, SearchView.OnQueryTextListener {
    private static final String TAG = "MainActivityFragment";

    private ItemAdapter adapter;
    private EbayParser parser;
    private ArrayList<Listing> datas;
    private ProgressDialog loadingDialog;
    private View view;
    private ListView listView;

    @Override
    public void onLoadFinished(Loader<List<Listing>> loader, List<Listing> data) {
        Log.v(TAG, "onLoadFinished " + data.size());

        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new ItemAdapter(getActivity(), data);
        listView.setAdapter(adapter);

        dismissLoading();
    }

    @Override
    public void onLoaderReset(Loader<List<Listing>> loader) {

    }

    @Override
    public Loader<List<Listing>> onCreateLoader(int id, Bundle args) {

        String query = args.getString("query");
        Log.v(TAG, "onCreateLoader " + query);
        EbayRequestLoader requestLoader = new EbayRequestLoader(getActivity(), query);
        return requestLoader;
    }

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main,
                container, false);
        Log.e(TAG, "onCreateView ");


//        AppDBHelper helper = new AppDBHelper(getActivity());
//        SQLiteDatabase database = helper.getWritableDatabase();
//        dataSource = new AppDataSource(database);
////        List list = dataSource.read();
////        dataSource.insert()
//        helper.close();
//        database.close();

        listView = (ListView) view.findViewById(R.id.listView);
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

        Bundle bundle = new Bundle();
        bundle.putString("query", "fantasy");
        getLoaderManager().initLoader(0, bundle, this).forceLoad();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(this);
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

    @Override
    public boolean onQueryTextSubmit(String query) {

        if (query.length() > 0) {
            showLoading();
            Bundle bundle = new Bundle();
            bundle.putString("query", query);
            getLoaderManager().restartLoader(0, bundle, this).forceLoad();
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
