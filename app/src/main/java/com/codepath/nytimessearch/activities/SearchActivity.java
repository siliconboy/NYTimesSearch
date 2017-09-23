package com.codepath.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.adapters.ArticleRecyclerAdapter;
import com.codepath.nytimessearch.adapters.EndlessRecyclerViewScrollListener;
import com.codepath.nytimessearch.fragments.FilterFragment;
import com.codepath.nytimessearch.interfaces.FilterDialogListener;
import com.codepath.nytimessearch.models.Article;
import com.codepath.nytimessearch.models.Filter;
import com.codepath.nytimessearch.utils.ItemClickSupport;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

public class SearchActivity extends AppCompatActivity implements FilterDialogListener {

    private EndlessRecyclerViewScrollListener scrollListener;

    @BindView(R.id.rvResults) RecyclerView rvResults;

    ArrayList<Article> articles;
    ArticleRecyclerAdapter adapter;
    Filter filter;
    AsyncHttpClient client;
    final String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    int currentPage = 0;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        setupViews();
        filter = new Filter();
        //create network client
        client = new AsyncHttpClient();
    }

    public void setupViews(){
//        etQuery =  findViewById(R.id.etQuery);
  //      rvResults =  findViewById(R.id.rvResults);
    //    btnSearch =  findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
        adapter = new ArticleRecyclerAdapter(this,articles);

        rvResults.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
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

        ItemClickSupport.addTo(rvResults).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        //create intent
                        Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                        //get article
                        Article article = articles.get(position);

                        intent.putExtra("article", article);
                        //launch
                        startActivity(intent);
                    }
                }
        );
/*        rvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //create intent
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                //get article
                Article article = articles.get(i);

                intent.putExtra("article", article);
                //launch
                startActivity(intent);
            }
        });
  */
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
//        articles.addAll(moreContacts);
        final RequestParams params = buildParams(query, filter, currentPage);

        // Create the Handler object (on the main thread by default)
        Handler handler = new Handler();
// Define the code block to be executed
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread

                client.get(url,params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        log.d("DEBUG", response.toString());
                        JSONArray articleJsonResults;

                        try {
                            articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                            // add new data
                            articles.addAll(Article.fromJSONArray(articleJsonResults));
                            // Notify the adapter of the update
                            adapter.notifyItemRangeInserted(curSize, articles.size() - 1);
                            currentPage++;
                            //Reset endless scroll listener when performing a new search
                            scrollListener.resetState();
                            log.d("DEBUG", articles.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        log.d("DEBUG",String.valueOf(statusCode));
                        log.d("DEBUG",errorResponse.toString());
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });


                Log.d("Handlers", "Called on main thread");
            }
        };
// Run the above code block on the main thread after 2 seconds
        handler.postDelayed(runnableCode, 2000);



        view.post(new Runnable() {
            @Override
            public void run() {
                Log.d("viewpost", "post runnable executed");

//                adapter.notifyItemRangeInserted(curSize, articles.size() - 1);
            }
        });
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
                currentPage =0;

                RequestParams params = buildParams(query, filter, currentPage);
                client.get(url,params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        log.d("DEBUG", response.toString());
                        JSONArray articleJsonResults;

                        try {
                            articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                            //clear data with new search result.
                            articles.clear();
                            // add new data
                            articles.addAll(Article.fromJSONArray(articleJsonResults));
                            // Notify the adapter of the update
                            adapter.notifyDataSetChanged();
                            //Reset endless scroll listener when performing a new search
                            scrollListener.resetState();
                            log.d("DEBUG", articles.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });

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

    public RequestParams buildParams(String query, Filter ft, int page){
        String sort = filter.getSort();
        String beginDate = filter.getBeginDate();

        StringBuilder sb = new StringBuilder();
        sb.append("news_desk:(");
        if(filter.getSports()) {
            sb.append("\"Sports\"");
        }
        if(filter.getFashionStyle()) {
            sb.append(" \"Fashion & Style\"");
        }
        if(filter.getArt()){
            sb.append(" \"Arts\"");
        }
        sb.append(")");
        if(!filter.getFashionStyle() && !filter.getArt() && !filter.getSports()){

            sb.setLength(0);
        }
        String extraQuery =sb.toString();
        // perform query here

        RequestParams params = new RequestParams();
        params.add("api-key","9a01eed7f83a4928bc5949886e052166");
        params.add("page",String.valueOf(page));
        params.add("q",query);
        if(sort != null &&!sort.isEmpty()) {
            params.add("sort", sort);
        }
        if(beginDate!=null && !beginDate.isEmpty()) {
            params.add("begin_date", beginDate);
        }
        if(!extraQuery.isEmpty()) {
            params.add("fq", extraQuery);
        }
        return params;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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

}
