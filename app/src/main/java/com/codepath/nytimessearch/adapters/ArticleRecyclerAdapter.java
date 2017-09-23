package com.codepath.nytimessearch.adapters;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.models.Article;
import com.codepath.nytimessearch.models.Image;
import com.codepath.nytimessearch.utils.ArticleDiffCallback;
import com.codepath.nytimessearch.utils.GlideApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

//import com.bumptech.glide.Glide;

/**
 * Created by yingbwan on 9/18/2017.
 */

public class ArticleRecyclerAdapter extends RecyclerView.Adapter<ArticleRecyclerAdapter.ViewHolder> {

    Context mContext;
    List<Article> mArticles;


    public ArticleRecyclerAdapter(Context context, List<Article> articles){

        mContext = context;
        mArticles = articles;
    }

    private Context getContext() {
        return mContext;
    }

    public ArticleRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_article_result, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ArticleRecyclerAdapter.ViewHolder holder, int position) {
            Article article = mArticles.get(position);

            // set item
        TextView tvTitle = holder.tvTitle;
        tvTitle.setText(article.getHeadline());
        TextView tvCategory = holder.tvCategory;
        tvCategory.setText(article.getNewsDesk());
        TextView tvSnippet = holder.tvSnippet;
        tvSnippet.setText(article.getSnippet());

        ImageView ivImage = holder.ivImage;
        ivImage.setImageResource(0);
        ArrayList<Image> images = article.getImages();
        String thumbnail ="";
        if(images !=null && images.size()>0){
            Random rd = new Random();
            thumbnail = images.get(rd.nextInt(images.size())).getUrl();
        }
      //  String thumbnail = article.getImages().get(0).getUrl();
        if(!TextUtils.isEmpty(thumbnail)) {
            GlideApp.with(getContext()).load(thumbnail).placeholder(R.drawable.placeholder).override(400,400).fitCenter().apply(RequestOptions.circleCropTransform()).into(ivImage);
        }
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        GlideApp.with(getContext()).clear(holder.ivImage);
    }

    public void swapItems(List<Article> articles) {
        // compute diffs
        final ArticleDiffCallback diffCallback = new ArticleDiffCallback(this.mArticles, articles);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        // clear contacts and add
        this.mArticles.clear();
        this.mArticles.addAll(articles);

        diffResult.dispatchUpdatesTo(this); // calls adapter's notify methods after diff is computed
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
       @BindView(R.id.tvTitle) public TextView tvTitle;
       @BindView(R.id.ivImage) public ImageView ivImage;
       @BindView(R.id.tvSnippet) public TextView tvSnippet;
       @BindView(R.id.tvCategory) public TextView tvCategory;

        public ViewHolder(View view) {
            // Very important to call the parent constructor
            // as this ensures that the imageView field is populated.
            super(view);
            ButterKnife.bind(this, view);
            // Perform other view lookups.
           // tvTitle = (TextView) view.findViewById(R.id.tvTitle);
           // ivImage = (ImageView) view.findViewById(R.id.ivImage);
        }
    }

}