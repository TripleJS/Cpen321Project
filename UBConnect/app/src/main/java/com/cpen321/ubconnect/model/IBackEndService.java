package com.cpen321.ubconnect.model;

import com.cpen321.ubconnect.model.data.PublicUser;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IBackEndService {

    @GET("")
    Call<User> getUser();

    @GET("")
    Call<User> getUserAccount(@Path("userId")String userId);

    @GET("/user/public/{userId}")
    Call<PublicUser> getPublicUser(@Path("userId")String userId);

    @POST("")
    Call<PublicUser> reportUser();

    @POST("")
    Call<PublicUser> reteUser();

    @GET("")
    Call<List<Question>> getSuggestedQuestions();

    @GET("")
    Call<List<Question>> getSearchResult();

    @POST("")
    Call<Question> postQuestion();

    @GET("")
    Call<Question> getAnsweredQuestion();

}