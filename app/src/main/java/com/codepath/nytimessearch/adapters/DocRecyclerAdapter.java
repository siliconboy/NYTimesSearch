package com.codepath.nytimessearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.models.Doc;
import com.codepath.nytimessearch.models.Multimedium;
import com.codepath.nytimessearch.utils.GlideApp;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by yingbwan on 9/24/2017.
 */

public class DocRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Doc> items;
    private final int DOC =0, IMAGE = 1;

    Context mContext;
    //List<Doc> mArticles;
    final String IMAGE_BASE ="http://www.nytimes.com/";

    public DocRecyclerAdapter(Context context, List<Doc> items) {
        this.mContext = context;
        this.items = items;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(items.get(position).getMultimedia().isEmpty())
        {
            return DOC;
        } else {
            return IMAGE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case DOC:
                View v1 = inflater.inflate(R.layout.item_doc_result, parent, false);
                viewHolder = new ViewHolder2(v1);
                break;
            case IMAGE:
                View v2 = inflater.inflate(R.layout.item_article_result, parent, false);
                viewHolder = new ViewHolder1(v2);
                break;
            default:
                View v = inflater.inflate(R.layout.item_article_result, parent, false);
                viewHolder = new ViewHolder1(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case DOC:
                ViewHolder2 vh1 = (ViewHolder2) holder;
                configureViewHolder2(vh1, position);
                break;
            case IMAGE:
                ViewHolder1 vh = (ViewHolder1) holder;
                configureViewHolder1(vh, position);
                break;
            default:
                ViewHolder1 vh2 = (ViewHolder1) holder;
                configureViewHolder1(vh2, position);
                break;
        }
    }
    public void configureViewHolder1(ViewHolder1 holder, int position){
        Doc article = items.get(position);

        // set items
        TextView tvTitle = holder.tvTitle;
        tvTitle.setText(article.getHeadline().getMain());
        TextView tvCategory = holder.tvCategory;
        if(article.getNewDesk()!=null && !article.getNewDesk().isEmpty()){
            tvCategory.setText(article.getNewDesk());
        }else {
            tvCategory.setVisibility(View.GONE);
        }
        TextView tvDate = holder.tvDate;
        if (article.getPubDate()!=null &&!article.getPubDate().isEmpty()){
            tvDate.setText(article.getPubDate().substring(0, 10));
        }else{
            tvDate.setVisibility(View.GONE);
        }
        TextView tvSnippet = holder.tvSnippet;
        tvSnippet.setText(article.getSnippet());

        ImageView ivImage = holder.ivImage;
        ivImage.setImageResource(0);
        List<Multimedium> images = article.getMultimedia();
        String thumbnail ="";
        if(images !=null && images.size()>0){
            Random rd = new Random();
            thumbnail = IMAGE_BASE + images.get(rd.nextInt(images.size())).getUrl();
        }
        //  String thumbnail = article.getImages().get(0).getUrl();
        if(!TextUtils.isEmpty(thumbnail)) {
            GlideApp.with(getContext()).load(thumbnail).placeholder(R.drawable.placeholder).override(400,400).fitCenter().apply(RequestOptions.circleCropTransform()).into(ivImage);
        }
    }

    public void configureViewHolder2(ViewHolder2 holder, int position){
        Doc article = items.get(position);

        // set items
        TextView tvTitle = holder.tvTitle;
        tvTitle.setText(article.getHeadline().getMain());
        TextView tvCategory = holder.tvCategory;
        if(article.getNewDesk()!=null && !article.getNewDesk().isEmpty()){
            tvCategory.setText(article.getNewDesk());
        } else {
            tvCategory.setVisibility(View.GONE);
        }
        TextView tvDate = holder.tvDate;
        if (article.getPubDate()!=null &&!article.getPubDate().isEmpty()){
            tvDate.setText(article.getPubDate().substring(0, 10));
        }else{
            tvDate.setVisibility(View.GONE);
        }
        TextView tvSnippet = holder.tvSnippet;
        tvSnippet.setText(article.getSnippet());
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if(holder instanceof ViewHolder1) {
            GlideApp.with(getContext()).clear(((ViewHolder1) holder).ivImage);
        }
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle) public TextView tvTitle;
        @BindView(R.id.ivImage) public ImageView ivImage;
        @BindView(R.id.tvDate) public TextView tvDate;
        @BindView(R.id.tvSnippet) public TextView tvSnippet;
        @BindView(R.id.tvCategory) public TextView tvCategory;

        public ViewHolder1(View view) {
            // Very important to call the parent constructor
            // as this ensures that the imageView field is populated.
            super(view);
            ButterKnife.bind(this, view);
        }

        public TextView getTvTitle() {
            return tvTitle;
        }

        public void setTvTitle(TextView tvTitle) {
            this.tvTitle = tvTitle;
        }

        public ImageView getIvImage() {
            return ivImage;
        }

        public void setIvImage(ImageView ivImage) {
            this.ivImage = ivImage;
        }

        public TextView getTvDate() {
            return tvDate;
        }

        public void setTvDate(TextView tvDate) {
            this.tvDate = tvDate;
        }

        public TextView getTvSnippet() {
            return tvSnippet;
        }

        public void setTvSnippet(TextView tvSnippet) {
            this.tvSnippet = tvSnippet;
        }

        public TextView getTvCategory() {
            return tvCategory;
        }

        public void setTvCategory(TextView tvCategory) {
            this.tvCategory = tvCategory;
        }
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle) public TextView tvTitle;
        @BindView(R.id.tvDate) public TextView tvDate;
        @BindView(R.id.tvSnippet) public TextView tvSnippet;
        @BindView(R.id.tvCategory) public TextView tvCategory;

        public ViewHolder2(View view) {
            // Very important to call the parent constructor
            // as this ensures that the imageView field is populated.
            super(view);
            ButterKnife.bind(this, view);

        }

        public TextView getTvTitle() {
            return tvTitle;
        }

        public void setTvTitle(TextView tvTitle) {
            this.tvTitle = tvTitle;
        }

        public TextView getTvDate() {
            return tvDate;
        }

        public void setTvDate(TextView tvDate) {
            this.tvDate = tvDate;
        }

        public TextView getTvSnippet() {
            return tvSnippet;
        }

        public void setTvSnippet(TextView tvSnippet) {
            this.tvSnippet = tvSnippet;
        }

        public TextView getTvCategory() {
            return tvCategory;
        }

        public void setTvCategory(TextView tvCategory) {
            this.tvCategory = tvCategory;
        }
    }
}
