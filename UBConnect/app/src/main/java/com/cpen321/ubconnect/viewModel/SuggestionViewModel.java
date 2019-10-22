package com.cpen321.ubconnect.viewModel;

import android.util.Log;

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

public class SuggestionViewModel extends ViewModel {

    private MutableLiveData<List<Question>> questions = new MutableLiveData<>();
    private int pageNumber;

    private IBackEndService mBackEndService;

    public SuggestionViewModel() {
        super();
        init();
        initService();
    }

    private void init() {
        pageNumber = 0;
    }

    private void initService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mBackEndService = retrofit.create(IBackEndService.class);
    }

    public void getSuggestion() {
        Log.d("Suggest", "getSuggestion:1 ");
        mBackEndService.getSuggestedQuestions().enqueue(new Callback<List<Question>>() {

            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                Log.d("Suggest", "getSuggestion:2 ");
                if (!response.isSuccessful()) {

                }

                if (response.body() == null)
                    return;

                questions.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.d("Suggest", "getSuggestion:3 ");

            }
        });
    }

    public MutableLiveData<List<Question>> getQuestions() {

        if(pageNumber == 0){
            getSuggestion();
        }

        return questions;
    }
}
