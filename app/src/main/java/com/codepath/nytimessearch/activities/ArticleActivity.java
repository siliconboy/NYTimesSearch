package com.codepath.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.models.Article;

public class ArticleActivity extends AppCompatActivity {

    private ShareActionProvider miShareAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Article article = (Article) getIntent().getParcelableExtra("article");
        WebView webView = findViewById(R.id.wvArticle);


        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        webView.loadUrl(article.getWebUrl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch reference to the share action provider

        //miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        ShareActionProvider miShare = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        // get reference to WebView
        WebView wvArticle = (WebView) findViewById(R.id.wvArticle);
        // pass in the URL currently being used by the WebView
        shareIntent.putExtra(Intent.EXTRA_TEXT, wvArticle.getUrl());
        miShare.setShareIntent(shareIntent);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // This is the up button
            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);
                // overridePendingTransition(R.animator.anim_left, R.animator.anim_right);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
