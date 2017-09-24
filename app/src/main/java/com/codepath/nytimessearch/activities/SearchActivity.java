package com.codepath.nytimessearch.activities;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.adapters.DocRecyclerAdapter;
import com.codepath.nytimessearch.adapters.EndlessRecyclerViewScrollListener;
import com.codepath.nytimessearch.fragments.FilterFragment;
import com.codepath.nytimessearch.interfaces.FilterDialogListener;
import com.codepath.nytimessearch.interfaces.SearchApiEndpointInterface;
import com.codepath.nytimessearch.models.Doc;
import com.codepath.nytimessearch.models.Filter;
import com.codepath.nytimessearch.models.QueryResult;
import com.codepath.nytimessearch.networks.APIHelper;
import com.codepath.nytimessearch.networks.NetworkUtils;
import com.codepath.nytimessearch.utils.ItemClickSupport;
import com.codepath.nytimessearch.utils.MyUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity implements FilterDialogListener {

    final String BASE_URL = "http://api.nytimes.com/svc/search/v2/";
    @BindView(R.id.rvResults)
    RecyclerView rvResults;
    @BindView(R.id.search_layout)
    RelativeLayout searchLayout;
    ArrayList<Doc> articles;

    DocRecyclerAdapter adapter;
    SearchApiEndpointInterface apiService;
    Filter filter;
    int currentPage = 0;
    String query;
    private EndlessRecyclerViewScrollListener scrollListener;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        setupViews();

        if (NetworkUtils.isNetworkAvailable(this) || NetworkUtils.isOnline()) {
            Toast.makeText(this, "Network is not available. please enable it.", Toast.LENGTH_LONG).show();
        }
        filter = new Filter();
        //initialize Retrofit
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // simplified call to request the news with already initialized service
        apiService = retrofit.create(SearchApiEndpointInterface.class);

    }

    public void setupViews() {

        articles = new ArrayList<>();
        adapter = new DocRecyclerAdapter(this, articles);
        rvResults.setAdapter(adapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        staggeredGridLayoutManager.scrollToPosition(0);
        rvResults.setLayoutManager(staggeredGridLayoutManager);

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page, view);
            }
        };

        // Adds the scroll listener to RecyclerView
        rvResults.addOnScrollListener(scrollListener);

        rvResults.setItemAnimator(new SlideInUpAnimator());

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(rvResults.getContext(), DividerItemDecoration.VERTICAL);
        rvResults.addItemDecoration(itemDecoration);

        ItemClickSupport.addTo(rvResults).setOnItemClickListener(
                (recyclerView, position, v) -> {
                    //create intent
             /*   tranfer to detail activity which use webview to show article
                    Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                    //get article
                    Doc article = articles.get(position);
                    intent.putExtra("article", article);
                    //launch activity
                    startActivity(intent);
                    */
             /* use chrome tab */
                    String url = articles.get(position).getWebUrl();
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

// set toolbar color and/or setting custom actions before invoking build()
                    builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorAccent));
                    builder.addDefaultShareMenuItem();

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_share_black_24dp);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, url);

                    int requestCode = 100;

                    PendingIntent pendingIntent = PendingIntent.getActivity(this,
                            requestCode,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    builder.setActionButton(bitmap, "Share Link", pendingIntent, true);


// Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
                    CustomTabsIntent customTabsIntent = builder.build();
// and launch the desired Url with CustomTabsIntent.launchUrl()
                    customTabsIntent.launchUrl(this, Uri.parse(url));

                }
        );

    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset, View view) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
        final int curSize = adapter.getItemCount();

        // Create the Handler object (on the main thread by default)
        Handler handler = new Handler();
        // Define the code block to be executed
        Runnable runnableCode = () -> {
            // load data using network api
            fetchArticles(query, filter, currentPage, false);

        };
        // Run the above code block on the main thread after 2 seconds
        handler.postDelayed(runnableCode, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String qry) {
                query = qry;
                currentPage = 0;
                //remove background
                searchLayout.setBackgroundResource(0);
                progressDialog = ProgressDialog.show(SearchActivity.this, "", "Loading Data");

                fetchArticles(query, filter, currentPage, true);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            FilterFragment filterFragment;
            FragmentManager fm = getSupportFragmentManager();
            filterFragment = FilterFragment.newInstance(filter);
            filterFragment.show(fm, "fragment_filter");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinishDialog(Filter ft) {
        filter = ft;
    }

    private void fetchArticles(final String query, Filter ft, int page, final boolean isNew) {
        final String BASE_URL = "http://api.nytimes.com/svc/search/v2/";
        Map<String, String> data = new HashMap<>();
        data.put("api-key", "9a01eed7f83a4928bc5949886e052166");
        data.put("page", String.valueOf(page));
        data.put("q", query);
        Log.d("DEBUG", "page:" + String.valueOf(page) + ",query:" + query);
        if (ft.getSort() != null && !ft.getSort().isEmpty()) {
            data.put("sort", ft.getSort());
        }
        if (ft.getBeginDate() != null && !ft.getBeginDate().isEmpty()) {
            data.put("begin_date", MyUtils.convertMDYToYMD(ft.getBeginDate()));
        }
        String extraQuery;
        if (!filter.getFashionStyle() && !filter.getArt() && !filter.getSports()) {
            extraQuery = "";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("news_desk:(");
            if (filter.getSports()) {
                sb.append("\"Sports\" ");
            }
            if (filter.getFashionStyle()) {
                sb.append("\"Fashion & Style\" ");
            }
            if (filter.getArt()) {
                sb.append("\"Arts\" ");
            }
            sb.replace(sb.length() - 1, sb.length(), ")");

            extraQuery = sb.toString();
        }

        if (!extraQuery.isEmpty()) {
            data.put("fq", extraQuery);
        }
        //call Retrofit api
        Call<QueryResult> call = apiService.getResult(data);

        APIHelper.enqueueWithRetry(call, 5, new Callback<QueryResult>() {
            //      call.enqueue(new Callback<QueryResult>() {
            @Override
            public void onResponse(Call<QueryResult> call, Response<QueryResult> response) {
                int statusCode = response.code();
                Log.d("DEBUG", "response code:" + String.valueOf(statusCode));
                //Log.d("DEBUG", "body:" + response.toString());
                if (statusCode != 200) {
                    Toast.makeText(SearchActivity.this, "Search failed!", Toast.LENGTH_LONG).show();

                    return;
                }
                QueryResult queryResult = response.body();
                List<Doc> docs = queryResult.getResponse().getDocs();
                if (isNew) {
                    articles.clear();
                    rvResults.scrollToPosition(0);
                }
                // add new data
                articles.addAll(docs);
                // Notify the adapter of the update
                adapter.notifyDataSetChanged();
                //Reset endless scroll listener when performing a new search
                if (isNew) {
                    scrollListener.resetState();
                    progressDialog.dismiss();
                }
                if (articles.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "No result found!!!", Toast.LENGTH_LONG).show();
                } else {
                    currentPage++;
                }
            }

            @Override
            public void onFailure(Call<QueryResult> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "could not load content. check your network!", Toast.LENGTH_LONG).show();
                // Log error here since request failed
            }
        });

    }

}
