package com.cpen321.ubconnect.ui.account;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cpen321.ubconnect.model.AuthInterceptor;
import com.cpen321.ubconnect.model.ConstantsUtils;
import com.cpen321.ubconnect.model.ErrorHandlingUtils;
import com.cpen321.ubconnect.model.IBackEndService;
import com.cpen321.ubconnect.model.data.User;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Configuring a new Retrofit instance
public class AccountViewModel extends ViewModel {

    private MutableLiveData<User> userAccount = new MutableLiveData<>();

    private IBackEndService mBackEndService;

    public AccountViewModel() {
        super();
        initService();
    }

    private void initService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstantsUtils.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mBackEndService = retrofit.create(IBackEndService.class);
    }

    public void getUserInfo(String token, String userId) {
        setupRetrofit(token);

        mBackEndService.getUserAccount(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    // to do
                    //ErrorHandlingUtils.errorHandling("dummy");
                    Log.d("XXX", "onResponse: not successful");
                }

                if (response.body() == null)
                    return;

                userAccount.postValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // to do
                Log.d("XXX", "onFailure: failed");
            }
        });
    }

    public MutableLiveData<User> getUserAccount() {
        return userAccount;
    }

    public void setUserInfo(String token, String userId, User user) {
        setupRetrofit(token);
        mBackEndService.postUserAccount(userId, user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    // to do
//                    ErrorHandlingUtils.errorHandling("dummy");
                }

                if (response.body() == null)
                    return;

                userAccount.postValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // to do
            }
        });
    }

    public MutableLiveData<User> setUserAccount() {
        return userAccount;
    }

    void setupRetrofit(String token){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(token))
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstantsUtils.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        mBackEndService = retrofit.create(IBackEndService.class);
    }

}


