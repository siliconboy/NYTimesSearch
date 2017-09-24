package com.codepath.nytimessearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Doc implements Parcelable {

    @SerializedName("web_url")
    @Expose
    private String webUrl;
    @SerializedName("snippet")
    @Expose
    private String snippet;
    @SerializedName("blog")
    @Expose
    private Blog blog;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("multimedia")
    @Expose
    private List<Multimedium> multimedia = null;
    @SerializedName("headline")
    @Expose
    private Headline headline;
    @SerializedName("keywords")
    @Expose
    private List<Keyword> keywords = null;
    @SerializedName("pub_date")
    @Expose
    private String pubDate;
    @SerializedName("document_type")
    @Expose
    private String documentType;
    @SerializedName("new_desk")
    @Expose
    private String newDesk;
    @SerializedName("section_name")
    @Expose
    private String sectionName;
    @SerializedName("byline")
    @Expose
    private Byline byline;
    @SerializedName("type_of_material")
    @Expose
    private String typeOfMaterial;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("score")
    @Expose
    private float score;
    @SerializedName("print_page")
    @Expose
    private String printPage;
    @SerializedName("word_count")
    @Expose
    private int wordCount;

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Multimedium> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<Multimedium> multimedia) {
        this.multimedia = multimedia;
    }

    public Headline getHeadline() {
        return headline;
    }

    public void setHeadline(Headline headline) {
        this.headline = headline;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getNewDesk() {
        return newDesk;
    }

    public void setNewDesk(String newDesk) {
        this.newDesk = newDesk;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Byline getByline() {
        return byline;
    }

    public void setByline(Byline byline) {
        this.byline = byline;
    }

    public String getTypeOfMaterial() {
        return typeOfMaterial;
    }

    public void setTypeOfMaterial(String typeOfMaterial) {
        this.typeOfMaterial = typeOfMaterial;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getPrintPage() {
        return printPage;
    }

    public void setPrintPage(String printPage) {
        this.printPage = printPage;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.webUrl);
        dest.writeString(this.snippet);
       // dest.writeParcelable(this.blog, flags);
        dest.writeString(this.source);
      //  dest.writeList(this.multimedia);
      //  dest.writeParcelable(this.headline, flags);
      //  dest.writeList(this.keywords);
        dest.writeString(this.pubDate);
        dest.writeString(this.documentType);
        dest.writeString(this.newDesk);
        dest.writeString(this.sectionName);
       // dest.writeParcelable(this.byline, flags);
        dest.writeString(this.typeOfMaterial);
        dest.writeString(this.id);
        dest.writeFloat(this.score);
        dest.writeString(this.printPage);
        dest.writeInt(this.wordCount);
    }

    public Doc() {
    }

    protected Doc(Parcel in) {
        this.webUrl = in.readString();
        this.snippet = in.readString();
      //  this.blog = in.readParcelable(Blog.class.getClassLoader());
        this.source = in.readString();
        //this.multimedia = new ArrayList<Multimedium>();
       // in.readList(this.multimedia, Multimedium.class.getClassLoader());
     //   this.headline = in.readParcelable(Headline.class.getClassLoader());
      //  this.keywords = new ArrayList<Keyword>();
      //  in.readList(this.keywords, Keyword.class.getClassLoader());
        this.pubDate = in.readString();
        this.documentType = in.readString();
        this.newDesk = in.readString();
        this.sectionName = in.readString();
     //   this.byline = in.readParcelable(Byline.class.getClassLoader());
        this.typeOfMaterial = in.readString();
        this.id = in.readString();
        this.score = in.readFloat();
        this.printPage = in.readString();
        this.wordCount = in.readInt();
    }

    public static final Parcelable.Creator<Doc> CREATOR = new Parcelable.Creator<Doc>() {
        @Override
        public Doc createFromParcel(Parcel source) {
            return new Doc(source);
        }

        @Override
        public Doc[] newArray(int size) {
            return new Doc[size];
        }
    };
}
