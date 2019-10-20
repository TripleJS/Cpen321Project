package com.cpen321.ubconnect.viewModel;

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

public class SearchViewModel extends ViewModel {
<<<<<<< HEAD
    private MutableLiveData<List<Question>> questions = new MutableLiveData<>();

    private IBackEndService mBackEndService;

    private int pageNumber;

=======
    private MutableLiveData<List<Question>> Questions = new MutableLiveData<>();

    private IBackEndService mBackEndService;

>>>>>>> c51a57449b354fece7fd3cbaebe70b9716566b33
    public SearchViewModel() {
        super();
        init();
        initService();
    }

    private void init() {
<<<<<<< HEAD
        pageNumber = 0;
=======

>>>>>>> c51a57449b354fece7fd3cbaebe70b9716566b33
    }

    private void initService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mBackEndService = retrofit.create(IBackEndService.class);
    }

<<<<<<< HEAD
    public void getResults() {
=======
    private void getResults() {
>>>>>>> c51a57449b354fece7fd3cbaebe70b9716566b33
        mBackEndService.getSearchResult().enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (!response.isSuccessful()) {

                }

                if (response.body() == null)
                    return;

<<<<<<< HEAD
                questions.postValue(response.body());
=======
>>>>>>> c51a57449b354fece7fd3cbaebe70b9716566b33
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<List<Question>> getQuestions() {
<<<<<<< HEAD
        if(pageNumber == 0){
            getResults();
        }

        return questions;
=======
        return Questions;
>>>>>>> c51a57449b354fece7fd3cbaebe70b9716566b33
    }
}
