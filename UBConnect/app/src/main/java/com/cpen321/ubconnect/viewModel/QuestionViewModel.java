package com.cpen321.ubconnect.viewModel;

import android.media.session.MediaSession;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cpen321.ubconnect.model.Constants;
import com.cpen321.ubconnect.model.IBackEndService;
import com.cpen321.ubconnect.model.data.Question;

import java.util.List;

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
        init();
        initService();
    }

    private void init() {

    }

    private void initService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mBackEndService = retrofit.create(IBackEndService.class);
    }

    private void getRequestedQuestion(String questionId) {
        mBackEndService.getQuestionById(questionId).enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (!response.isSuccessful()) {

                }

                if (response.body() == null)
                    return;

                question.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<Question> getQuestion(String questionId) {
        getRequestedQuestion(questionId);

        return question;
    }
}
