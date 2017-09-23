package com.codepath.nytimessearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yingbwan on 9/17/2017.
 */

public class Article implements Parcelable {
    String webUrl;
    String headline;
    String snippet;
    String id;    //_id
    String source;
    String pubDate;   //pub_date
    String newDesk;  //new_desk
    String original;   //byline--original

    ArrayList<Image> images;   //multimedia

    public String getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getNewsDesk() {
        return newDesk;
    }

    public String getOriginal() {
        return original;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }


    public Article(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            this.snippet = jsonObject.getString("snippet");
            this.id = jsonObject.getString("_id");
            try {
                this.source = jsonObject.getString("source");
            }catch (JSONException e){
                //skip this
            }
            try {
                this.newDesk = jsonObject.getString("new_desk");
            }catch (JSONException e){
                //skip
            }
            try {
                this.pubDate = jsonObject.getString("pub_date");
            }catch (JSONException e){
                //skip
            }
            try {
                this.original = jsonObject.getJSONObject("byline").getString("original");
            }catch (JSONException e){
                //skip
            }

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            this.images = new ArrayList<>();
            if(multimedia.length()>0){
                for(int x=0;x<multimedia.length();x++) {
                    JSONObject multimediaJson = multimedia.getJSONObject(x);
                    String furl = "http://www.nytimes.com/" + multimediaJson.getString("url");
                    this.images.add(new Image(multimediaJson.getString("type"),multimediaJson.getString("subtype"),furl,multimediaJson.getInt("height"),multimediaJson.getInt("width")));
                }
            }

        } catch (JSONException e){
            e.printStackTrace();
        }
    }
    public static ArrayList<Article> fromJSONArray(JSONArray array){
        ArrayList<Article> results = new ArrayList<>();
        for(int i=0;i<array.length();i++){
            try {
                results.add(new Article(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.webUrl);
        dest.writeString(this.headline);
        dest.writeString(this.snippet);
        dest.writeString(this.id);
        dest.writeString(this.source);
        dest.writeString(this.pubDate);
        dest.writeString(this.newDesk);
        dest.writeString(this.original);
        dest.writeTypedList(this.images);
    }

    protected Article(Parcel in) {
        this.webUrl = in.readString();
        this.headline = in.readString();
        this.snippet = in.readString();
        this.id = in.readString();
        this.source = in.readString();
        this.pubDate = in.readString();
        this.newDesk = in.readString();
        this.original = in.readString();
        this.images = new ArrayList<Image>();
        in.readTypedList(this.images, Image.CREATOR);
    }


    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
