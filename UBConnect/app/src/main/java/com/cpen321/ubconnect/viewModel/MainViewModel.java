package com.cpen321.ubconnect.viewModel;

import android.util.Log;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cpen321.ubconnect.model.Constants;
import com.cpen321.ubconnect.model.IBackEndService;
import com.cpen321.ubconnect.model.data.AccessTokens;
import com.cpen321.ubconnect.model.data.FCMToken;
import com.cpen321.ubconnect.model.data.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainViewModel extends ViewModel implements LifecycleObserver{

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

    public void getAppUserByFB(AccessTokens accessToken) {
        mBackEndService.postUserByFB(accessToken).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {

                }

                if (response.body() == null)
                    return;



                currentUser.postValue(response.body());

                Log.d("Fuck", "onResponse: "+ response.body().getUserId());

//                Log.d("hi", "onResponse: ");
//                if(response.body().getUserId() == null){
//                    Log.d("fuck", "onChangedUserIdByFB:  " + "fucked");
//                }
//                else {Log.d("fucl", "onChangedUserIdByFB:  " + currentUser.getValue().getUserId());}

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }


    public LiveData<User> getCurrentUserByFB() {

        return currentUser;
    }

    private void getAppUser() {
        mBackEndService.getUser().enqueue(new Callback<User>() {
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

    public MutableLiveData<User> getCurrentUser() {

        getAppUser();

        return currentUser;
    }

    private void setAppUser(User user) {
        mBackEndService.postUser(user).enqueue(new Callback<User>() {
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

    public MutableLiveData<User> setCurrentUser(User user) {

        setAppUser(user);

        return currentUser;
    }


    public void sendRegistrationToServer(FCMToken tokenFCM) {
        mBackEndService.postFCMToken(tokenFCM).enqueue(new Callback<FCMToken>() {
            @Override
            public void onResponse(Call<FCMToken> call, Response<FCMToken> response) {
                if (!response.isSuccessful()) {

                }

                if (response.body() == null)
                    return;


            }

            @Override
            public void onFailure(Call<FCMToken> call, Throwable t) {

            }
        });
    }

}
