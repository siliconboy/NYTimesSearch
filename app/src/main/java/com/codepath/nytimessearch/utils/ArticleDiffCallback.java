package com.codepath.nytimessearch.utils;

import android.support.v7.util.DiffUtil;

import com.codepath.nytimessearch.models.Article;

import java.util.List;

/**
 * Created by yingbwan on 9/19/2017.
 */

public class ArticleDiffCallback  extends DiffUtil.Callback {
    private List<Article> mOldList;
    private List<Article> mNewList;

    public ArticleDiffCallback(List<Article> oldList, List<Article> newList) {
        this.mOldList = oldList;
        this.mNewList = newList;
    }
    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // add a unique ID property on Article and expose a getId() method
        return mOldList.get(oldItemPosition).getId().equals(mNewList.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Article oldArticle = mOldList.get(oldItemPosition);
        Article newArticle = mNewList.get(newItemPosition);

        return oldArticle.getWebUrl().equals(newArticle.getWebUrl()) && oldArticle.getHeadline().equals(newArticle.getHeadline());
    }
}
