package com.codepath.nytimessearch.interfaces;

import com.codepath.nytimessearch.models.QueryResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by yingbwan on 9/23/2017.
 */

public interface SearchApiEndpointInterface {

    @GET("articlesearch.json")
    Call<QueryResult> getResult(@QueryMap Map<String, String> options);

}
