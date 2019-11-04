package com.cpen321.ubconnect.ui.otheranswers;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cpen321.ubconnect.model.Constants;
import com.cpen321.ubconnect.model.IBackEndService;
import com.cpen321.ubconnect.model.data.Question;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OtherAnswersViewModel extends ViewModel {
    private MutableLiveData<Question> question = new MutableLiveData<>();

    private IBackEndService mBackEndService;

    public OtherAnswersViewModel() {
        super();
        initService();
    }

    private void initService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mBackEndService = retrofit.create(IBackEndService.class);
    }

    public void getQuestionById(String questionId) {

        mBackEndService.getQuestionById(questionId).enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                Log.d("Fuck" , "onResponse: postqqqqq ");
                if (!response.isSuccessful()) {
                    Log.d("Fuck" , "onResponse: ffffffffffffffffffff");
                }

                if (response.body() == null) {
                    Log.d("Fuck" , "onResponse: null null nulll null");
                    return;
                }


                question.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                Log.d("Fuck" , "onfail: postqqqqq ");
            }
        });
    }

    public MutableLiveData<Question> getQuestionData() {
        return question;
    }

}
