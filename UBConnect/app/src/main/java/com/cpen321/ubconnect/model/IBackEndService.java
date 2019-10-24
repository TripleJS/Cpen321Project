package com.cpen321.ubconnect.model;

import com.cpen321.ubconnect.model.data.AccessTokens;
import com.cpen321.ubconnect.model.data.FCMToken;
import com.cpen321.ubconnect.model.data.PublicUser;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.Swiped;
import com.cpen321.ubconnect.model.data.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IBackEndService {

    @GET("/user/public/{userId}")
    Call<User> getUser();

    @POST("/api/user/oauth/facebook")
    Call<User> postUserByFB(@Body AccessTokens accessToken);

    @POST("/user/public/{userId}")
    Call<User> postUser(@Body User user);

    @GET("")
    Call<User> getUserAccount(@Path("userId")String userId);

    @POST("")
    Call<User> postUserAccount(@Path("userId")String userId);

    @GET("/user/public/{userId}")
    Call<PublicUser> getPublicUser(@Path("userId")String userId);

    @POST("")
    Call<User> reportUser(@Path("userId")String userId);

    @POST("")
    Call<User> reteUser(@Path("userId")String userId);

    @GET("/api/questions/suggest/{userId}")
    Call<List<Question>> getSuggestedQuestions(@Path("userId")String userId);

    @GET("")
    Call<List<Question>> getSearchResult();

    @POST("/api/questions/post-question")
    Call<Question> postQuestion(@Body Question question);

    @GET("/api/questions/get-question/{questionId}")
    Call<Question> getQuestionById(@Path("questionId")String questionId);

    @POST("")
    Call<FCMToken> postFCMToken(@Body FCMToken fcmToken);

    @POST("/api/questions/swipe")
    Call<Swiped> postQuestionSwipe(@Body Swiped swiped);

}