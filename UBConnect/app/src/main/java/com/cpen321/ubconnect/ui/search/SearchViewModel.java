package com.cpen321.ubconnect.ui.search;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cpen321.ubconnect.model.ConstantsUtils;
import com.cpen321.ubconnect.model.IBackEndService;
import com.cpen321.ubconnect.model.data.Question;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<List<Question>> questions = new MutableLiveData<>();

    private IBackEndService mBackEndService;

    private int pageNumber;

    public SearchViewModel() {
        super();
        init();
        initService();
    }

    private void init() {
        pageNumber = 0;
    }

    private void initService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstantsUtils.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mBackEndService = retrofit.create(IBackEndService.class);
    }

    public void getResults() {
        mBackEndService.getSearchResult().enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (!response.isSuccessful()) {
                    // to do
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
        if(pageNumber == 0){
            getResults();
        }

        return questions;
    }
}
