package com.cpen321.ubconnect.model;

import com.cpen321.ubconnect.model.data.AccessTokens;
import com.cpen321.ubconnect.model.data.FCMToken;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.SearchResult;
import com.cpen321.ubconnect.model.data.Swiped;
import com.cpen321.ubconnect.model.data.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IBackEndService {

    @POST("/api/user/oauth/facebook")
    Call<User> postUserByFB(@Body AccessTokens accessToken);

    @POST("/api/user/signup")
    Call<User> signUpUser(@Body User user);

    @POST("/api/user/login")
    Call<User> loginUser(@Body User user);

    @GET("/api/user/getuser/{userId}")
    Call<User> getUserAccount(@Path("userId")String userId);

    @POST("/api/user/update-user/{userId}")
    Call<User> postUserAccount(@Path("userId")String userId, @Body User user);

    @GET("/api/user/getuser/{userId}")
    Call<User> getPublicUser(@Path("userId")String userId);

    @POST("/api/user/report/{userId}")
    Call<User> reportUser(@Path("userId")String userId,@Body User user);

    @POST("/api/user/rate/{userId}")
    Call<User> reteUser(@Path("userId")String userId, @Body User user);

//    @GET("/{userId}")
    @GET("api/questions/suggest/{userId}")
    Call<List<Question>> getSuggestedQuestions(@Path("userId")String userId);

    @GET("/api/search")
    Call<SearchResult> getSearchResult(@Query("question") String question);

    @POST("/api/questions/post-question")
    Call<Question> postQuestion(@Body Question question);

    @GET("/api/questions/get-question/{questionId}")
    Call<Question> getQuestionById(@Path("questionId")String questionId);

    @POST("")
    Call<FCMToken> postFCMToken(@Body FCMToken fcmToken);

    @POST("/api/questions/swipe")
    Call<Swiped> postQuestionSwipe(@Body Swiped swiped);

    @GET("api/questions/recent/{userId}")
    Call<Question> getRecentQuestionId(@Path("userId")String userId);
    @GET("api/answers/recent/{userId}")
    Call<Question> getRecentQuestionToAnswerId(@Path("userId")String userId);


}