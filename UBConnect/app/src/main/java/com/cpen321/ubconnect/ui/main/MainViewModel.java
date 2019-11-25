package com.cpen321.ubconnect.ui.main;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cpen321.ubconnect.model.ConstantsUtils;
import com.cpen321.ubconnect.model.IBackEndService;
import com.cpen321.ubconnect.model.NetworkUtil;
import com.cpen321.ubconnect.model.data.AccessTokens;
import com.cpen321.ubconnect.model.data.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainViewModel extends ViewModel implements LifecycleObserver{

    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    private MutableLiveData<Pair<Integer,String>> error = new MutableLiveData<>();

    private IBackEndService mBackEndService;

    public MainViewModel() {
        super();
        initService();
    }

    private void initService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstantsUtils.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mBackEndService = retrofit.create(IBackEndService.class);
    }

    public void getAppUserByFB(AccessTokens accessToken) {

        mBackEndService.postUserByFB(accessToken).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Pair<Integer,String> err = new Pair<Integer, String>(response.code(),NetworkUtil.onServerResponseError(response));
                    error.postValue(err);
                    return;
                }

                if (response.body() == null)
                    return;

                currentUser.postValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Pair<Integer,String> err = new Pair<Integer, String>(-1,"Oops Something Went Wrong! Please Try Again Later!\n" + "more details:\n" + t.toString());
                error.postValue(err);
                Log.d("fff", "onFailure: ");
            }
        });
    }


    public LiveData<User> getCurrentUserByFB() {
        return currentUser;
    }

    public void getAppUser(User user) {
        mBackEndService.loginUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Pair<Integer,String> err = new Pair<Integer, String>(response.code(),NetworkUtil.onServerResponseError(response,"login"));
                    error.postValue(err);
                }

                if (response.body() == null)
                    return;

                currentUser.postValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Pair<Integer,String> err = new Pair<Integer, String>(-1,"Oops Something Went Wrong! Please Try Again Later!\n" + "more details:\n" + t.toString());
                error.postValue(err);
            }
        });
    }

    public void setAppUser(User user) {
        mBackEndService.signUpUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Pair<Integer,String> err = new Pair<Integer, String>(response.code(),NetworkUtil.onServerResponseError(response,"signup"));
                    error.postValue(err);
                }

                if (response.body() == null)
                    return;

                currentUser.postValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Pair<Integer,String> err = new Pair<Integer, String>(-1,"Oops Something Went Wrong! Please Try Again Later!\n" + "more details:\n" + t.toString());
                error.postValue(err);
            }
        });
    }

    public MutableLiveData<Pair<Integer,String>> getError(){
        return error;
    }

}
