package com.shizzelandroid.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
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
import com.shizzelandroid.database.AppContentProvider;
import com.shizzelandroid.database.TodoTable;
import com.shizzelandroid.utils.EbayParser;
import com.shizzelandroid.utils.EbayRequestLoader;
import com.shizzelandroid.utils.Listing;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements SearchView.OnQueryTextListener {
    private static final String TAG = "MainActivityFragment";

    private static final int ID_LOADER_DATA = 0;
    private static final int ID_LOADER_CURSOR = 1;

    private ItemAdapter adapter;
    private EbayParser parser;
    private ArrayList<Listing> datas;
    private ProgressDialog loadingDialog;
    private View view;
    private ListView listView;
    private String currentQuery;
    private boolean startSearch = false;

    private LoaderManager.LoaderCallbacks<List<Listing>> loaderData = new LoaderManager.LoaderCallbacks<List<Listing>>() {
        @Override
        public Loader<List<Listing>> onCreateLoader(int id, Bundle args) {
            String query = args.getString("query");
            EbayRequestLoader requestLoader = new EbayRequestLoader(getActivity(), query);
            return requestLoader;
        }

        @Override
        public void onLoadFinished(Loader<List<Listing>> loader, List<Listing> data) {

            for (int i = 0; i < data.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(TodoTable.COLUMN_CURRENT_COST, data.get(i).getCurrentPrice());
                values.put(TodoTable.COLUMN_ITEM_ID, data.get(i).getId());
                values.put(TodoTable.COLUMN_THUMB_URL, data.get(i).getImageUrl());
                values.put(TodoTable.COLUMN_TITLE, data.get(i).getTitle());
                values.put(TodoTable.COLUMN_SHIPPING_COST, data.get(i).getShippingCost());

                String query = "SELECT 1 FROM "+TodoTable.TABLE_NAME+" WHERE "+ TodoTable.COLUMN_ITEM_ID +" = ?";
                Cursor cursor = getActivity().getContentResolver().query(AppContentProvider.CONTENT_URI, TodoTable.projection, query, new String[]{data.get(i).getId()}, null);

                if(cursor.getCount() > 0){
                    getActivity().getContentResolver().update(AppContentProvider.CONTENT_URI, values, TodoTable.COLUMN_ITEM_ID + " =?", new String[]{data.get(i).getId()});
                }else{
                    getActivity().getContentResolver().insert(AppContentProvider.CONTENT_URI, values);
                }
            }

            getLoaderManager().initLoader(ID_LOADER_CURSOR, null, loaderCursor);
            dismissLoading();

            if(startSearch){
                startSearch = false;
                runable.run();
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Listing>> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> loaderCursor = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            Log.v(TAG, "Cursor onCreateLoader " + id);
            CursorLoader cursorLoader = new CursorLoader(getActivity(),
                    AppContentProvider.CONTENT_URI, TodoTable.projection, null, null, null);
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

            List<Listing> datas = new ArrayList<Listing>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Listing item = new Listing();
                    item.setCurrentPrice(cursor.getString(cursor.getColumnIndex(TodoTable.COLUMN_CURRENT_COST)));
                    item.setId(cursor.getString(cursor.getColumnIndex(TodoTable.COLUMN_ITEM_ID)));
                    item.setImageUrl(cursor.getString(cursor.getColumnIndex(TodoTable.COLUMN_THUMB_URL)));
                    item.setShippingCost(cursor.getString(cursor.getColumnIndex(TodoTable.COLUMN_SHIPPING_COST)));
                    item.setTitle(cursor.getString(cursor.getColumnIndex(TodoTable.COLUMN_TITLE)));

                    if(currentQuery == null || item.getTitle().toLowerCase().contains(currentQuery.toLowerCase())){
                        datas.add(item);
                    }

                } while (cursor.moveToNext());
            }

            listView = (ListView)view.findViewById(R.id.listView);
            adapter = new ItemAdapter(getActivity(), datas);
            listView.setAdapter(adapter);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

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

        listView = (ListView) view.findViewById(R.id.listView);
        //showLoading();
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
//        getLoaderManager().initLoader(ID_LOADER_DATA, bundle, loaderData).forceLoad();

        getLoaderManager().initLoader(ID_LOADER_CURSOR, null, loaderCursor);

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
            currentQuery = query;

            Bundle bundle = new Bundle();
            bundle.putString("query", query);
            getLoaderManager().restartLoader(ID_LOADER_DATA, bundle, loaderData).forceLoad();
            handler.removeCallbacks(runable);

            startSearch = true;
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private Runnable runable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
//            Log.e(TAG, "onQueryTextChange RUN RUN "+query);
            handler.sendEmptyMessage(100);
            handler.postDelayed(this, 10000);
        }
    };

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if(currentQuery != null && currentQuery.length() > 0){
                Bundle bundle = new Bundle();
                bundle.putString("query", currentQuery);
                getLoaderManager().restartLoader(ID_LOADER_DATA, bundle, loaderData).forceLoad();
            }
            //Log.e("Got a new message", "Start Update HERE " + msg.arg1);
        }
    };
}
