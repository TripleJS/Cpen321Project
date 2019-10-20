package com.cpen321.ubconnect.model;

import com.cpen321.ubconnect.model.data.PublicUser;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.User;
<<<<<<< HEAD
import com.facebook.AccessToken;
=======
>>>>>>> c51a57449b354fece7fd3cbaebe70b9716566b33

import java.util.List;

import retrofit2.Call;
<<<<<<< HEAD
import retrofit2.http.Body;
=======
>>>>>>> c51a57449b354fece7fd3cbaebe70b9716566b33
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IBackEndService {

<<<<<<< HEAD
    @GET("/user/public/{userId}")
    Call<User> getUser(@Body AccessToken accessToken);
=======
    @GET("")
    Call<User> getUser();
>>>>>>> c51a57449b354fece7fd3cbaebe70b9716566b33

    @GET("")
    Call<User> getUserAccount(@Path("userId")String userId);

    @GET("/user/public/{userId}")
    Call<PublicUser> getPublicUser(@Path("userId")String userId);

    @POST("")
    Call<PublicUser> reportUser();

    @POST("")
    Call<PublicUser> reteUser();

    @GET("/api/questions/suggest")
    Call<List<Question>> getSuggestedQuestions();

    @GET("")
    Call<List<Question>> getSearchResult();

    @POST("")
    Call<Question> postQuestion();

    @GET("")
<<<<<<< HEAD
    Call<Question> getQuestionById(@Path("questionId")String questionId);
=======
    Call<Question> getAnsweredQuestion();
>>>>>>> c51a57449b354fece7fd3cbaebe70b9716566b33

}