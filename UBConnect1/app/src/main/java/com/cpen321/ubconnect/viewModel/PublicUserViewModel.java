package com.cpen321.ubconnect.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cpen321.ubconnect.model.Constants;
import com.cpen321.ubconnect.model.IBackEndService;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PublicUserViewModel extends ViewModel {
    private MutableLiveData<User> publicUser = new MutableLiveData<>();

    private IBackEndService mBackEndService;

    public PublicUserViewModel() {
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

    private void report(String userId) {
        mBackEndService.reportUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {

                }

                if (response.body() == null)
                    return;

                publicUser.postValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<User> userReport(String userId) {

        report(userId);

        return publicUser;
    }

    private void rate(String userId) {
        mBackEndService.reteUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {

                }

                if (response.body() == null)
                    return;

                publicUser.postValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<User> userRate(String userId) {

        rate(userId);

        return publicUser;
    }
}
