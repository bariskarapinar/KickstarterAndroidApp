package com.bkara.poilabs.classes;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.bkara.poilabs.R;


public class Projects extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, ProjectListAdapter.ItemClickListener {

    private static final int POPULAR_KICK_PROJECTS_LOADER = 300;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ProjectListAdapter projectListAdapter;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_projects);

        initView();
        showResponse(null);
    }

    private void initView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_popular_projects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(null);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.v("...", "Last Item Wow !" + totalItemCount);
     /**/                       progressBar.setVisibility(View.VISIBLE);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            Bundle args = new Bundle();
                                            args.putInt("start", 0);
                                            args.putInt("end", totalItemCount + 19);
                                            args.putInt("funding_percentage", 60);
                                            args.putInt("backers_coount", 30000);
                                            args.putString("sort_by", "backers");

                                            getSupportLoaderManager().restartLoader(POPULAR_KICK_PROJECTS_LOADER, args, Projects.this);
                                        }
                                    }, 2000);
                                }
                            });
                        }
                    }
                }
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int start = args.getInt("start");
        int end = args.getInt("end");

        loading = true;

        Uri projects_uri = ProjectDb.KickEntry.CONTENT_URI;
        String selection;
        String[] selectionArgs;
        String sort_order = null;

        int funding = args.getInt("funding_percentage");
        int backersCount = args.getInt("backers_coount");
        selection = ProjectDb.KickEntry.TABLE_NAME + "." + ProjectDb.KickEntry.KICK_SL_NUMBER + " >= ? AND " +
                ProjectDb.KickEntry.TABLE_NAME + "." + ProjectDb.KickEntry.KICK_SL_NUMBER + " <= ? AND " +
                ProjectDb.KickEntry.TABLE_NAME + "." + ProjectDb.KickEntry.KICK_PERCENTAGE_FUNDED + " >= ? AND " +
                ProjectDb.KickEntry.TABLE_NAME + "." + ProjectDb.KickEntry.KICK_BACKERS + " >= ?";
        selectionArgs = new String[]{String.valueOf(start), String.valueOf(end), String.valueOf(funding), String.valueOf(backersCount)};
        sort_order = args.getString("sort_by") + " DESC";


        return new CursorLoader(this,
                projects_uri,
                null,
                selection,
                selectionArgs,
                sort_order);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
 /**/       progressBar.setVisibility(View.GONE);
        if (cursor != null && cursor.getCount() > 0) {
            projectListAdapter.swapCursor(cursor);
            progressBar.setVisibility(View.GONE);
        } else {
            projectListAdapter.swapCursor(null);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        projectListAdapter.swapCursor(null);
    }

    private void showResponse(Cursor cursor) {
        projectListAdapter = new ProjectListAdapter(this, cursor);
        recyclerView.setAdapter(projectListAdapter);
        projectListAdapter.setItemClickListener(this);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void itemClicked(Cursor dataCursor) {
        int serial_number_index = dataCursor.getColumnIndex(ProjectDb.KickEntry.KICK_SL_NUMBER);
        int serial_number = dataCursor.getInt(serial_number_index);

        Intent intent = new Intent(this, ProjectInfo.class);
        intent.putExtra("sl_number", serial_number);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.second_in, R.anim.second_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle args = new Bundle();
        args.putInt("start", 0);
        args.putInt("end", 19);
        args.putInt("funding_percentage", 60);
        args.putInt("backers_coount", 30000);
        args.putString("sort_by", "backers");

        getSupportLoaderManager().initLoader(POPULAR_KICK_PROJECTS_LOADER, args, Projects.this);

    }
}
