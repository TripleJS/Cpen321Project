package com.cpen321.ubconnect.ui.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cpen321.ubconnect.model.ConstantsUtils;
import com.cpen321.ubconnect.model.ErrorHandlingUtils;
import com.cpen321.ubconnect.model.IBackEndService;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.Swiped;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SuggestionViewModel extends ViewModel {

    private MutableLiveData<List<Question>> questions = new MutableLiveData<>();

    private IBackEndService mBackEndService;

    public SuggestionViewModel() {
        super();
        initService();
    }

    private void initService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstantsUtils.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mBackEndService = retrofit.create(IBackEndService.class);
    }

    public void getSuggestion(String user) {
        Log.d("Suggest", "getSuggestion:1 ");
        mBackEndService.getSuggestedQuestions(user).enqueue(new Callback<List<Question>>() {

            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                Log.d("Suggest", "getSuggestion:2 ");
                if (!response.isSuccessful()) {
                    // to do
                    ErrorHandlingUtils.errorHandling("dummy");
                }

                if (response.body() == null)
                    return;

                questions.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                // to do
            }
        });
    }

    public MutableLiveData<List<Question>> getQuestions() {

        return questions;
    }

    public void sendSwipe(Swiped swiped) {
        Log.d("Suggest", "getSuggestion:1 ");
        mBackEndService.postQuestionSwipe(swiped).enqueue(new Callback<Swiped>() {

            @Override
            public void onResponse(Call<Swiped> call, Response<Swiped> response) {
                Log.d("Suggest", "getSuggestion:2 ");
                if (!response.isSuccessful()) {
                    // to do
                    ErrorHandlingUtils.errorHandling("dummy");
                }

                if (response.body() == null)
                    return;

            }

            @Override
            public void onFailure(Call<Swiped> call, Throwable t) {
                // to do

            }
        });
    }
}