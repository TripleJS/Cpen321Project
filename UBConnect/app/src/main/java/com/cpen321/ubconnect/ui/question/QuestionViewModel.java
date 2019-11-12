package com.cpen321.ubconnect.ui.question;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cpen321.ubconnect.model.AuthInterceptor;
import com.cpen321.ubconnect.model.ConstantsUtils;
import com.cpen321.ubconnect.model.ErrorHandlingUtils;
import com.cpen321.ubconnect.model.IBackEndService;
import com.cpen321.ubconnect.model.data.Question;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuestionViewModel extends ViewModel {
    private MutableLiveData<Question> question = new MutableLiveData<>();

    private IBackEndService mBackEndService;

    public QuestionViewModel() {
        super();
        initService();
    }

    private void initService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstantsUtils.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mBackEndService = retrofit.create(IBackEndService.class);
    }

    private void getRequestedQuestion(String questionId, String token) {
        setupRetrofit(token);
        mBackEndService.getQuestionById(questionId).enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (!response.isSuccessful()) {
                    // to do
                    //ErrorHandlingUtils.errorHandling("dummy");

                }

                if (response.body() == null)
                    return;

                question.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                // to do
            }
        });
    }

    public MutableLiveData<Question> getQuestion(String questionId, String token) {
        getRequestedQuestion(questionId, token);

        return question;
    }

    void setupRetrofit(String token){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(token))
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstantsUtils.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        mBackEndService = retrofit.create(IBackEndService.class);
    }
}
