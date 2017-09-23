package com.codepath.nytimessearch.models;

import java.util.ArrayList;

/**
 * Created by yingbwan on 9/21/2017.
 */

public class Filter {

    String beginDate;
    String sort;
    boolean art;
    boolean fashionStyle;
    boolean sports;

    public Filter() {

    }

    public Filter(String beginDate, String sort, boolean art, boolean fashionStyle, boolean sports) {
        this.beginDate = beginDate;
        this.sort = sort;
        this.art = art;
        this.fashionStyle = fashionStyle;
        this.sports = sports;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean getArt() {
        return art;
    }

    public void setArt(boolean art) {
        this.art = art;
    }

    public boolean getFashionStyle() {
        return fashionStyle;
    }

    public void setFashionStyle(boolean fashionStyle) {
        this.fashionStyle = fashionStyle;
    }

    public boolean getSports() {
        return sports;
    }

    public void setSports(boolean sports) {
        this.sports = sports;
    }

    public static ArrayList<String> buildQuery(Filter ft){
        return null;
    }
}
