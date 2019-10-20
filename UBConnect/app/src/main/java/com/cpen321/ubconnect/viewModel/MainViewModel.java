package com.cpen321.ubconnect.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cpen321.ubconnect.model.Constants;
import com.cpen321.ubconnect.model.IBackEndService;
import com.cpen321.ubconnect.model.data.Question;
import com.cpen321.ubconnect.model.data.User;
import com.facebook.AccessToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainViewModel extends ViewModel {

    private MutableLiveData<User> currentUser = new MutableLiveData<>();

    private IBackEndService mBackEndService;

    public MainViewModel() {
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

    private void getAppUser(AccessToken accessToken) {
        mBackEndService.getUser(accessToken).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {

                }

                if (response.body() == null)
                    return;

                currentUser.postValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<User> getCurrentUser(AccessToken accessToken) {

        getAppUser(accessToken);

        return currentUser;
    }

//    private void getAppUser(AccessToken accessToken) {
//        mBackEndService.getUser(accessToken).enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (!response.isSuccessful()) {
//
//                }
//
//                if (response.body() == null)
//                    return;
//
//                currentUser.postValue(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//
//            }
//        });
//    }
//
//    private void setAppUser(AccessToken accessToken) {
//        mBackEndService.getUser(accessToken).enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (!response.isSuccessful()) {
//
//                }
//
//                if (response.body() == null)
//                    return;
//
//                currentUser.postValue(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//
//            }
//        });
//    }
}
