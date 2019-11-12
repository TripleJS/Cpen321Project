package com.cpen321.ubconnect.ui.publicaccount;

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

public class PublicUserViewModel extends ViewModel {
    private MutableLiveData<User> publicUser = new MutableLiveData<>();

    private IBackEndService mBackEndService;

    public PublicUserViewModel() {
        super();
        initService();
    }


    private void initService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstantsUtils.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mBackEndService = retrofit.create(IBackEndService.class);
    }

    private void report(String userId, String token) {
        setupRetrofit(token);
        mBackEndService.reportUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    // to do
//                    ErrorHandlingUtils.errorHandling("dummy");
                }

                if (response.body() == null)
                    return;

                publicUser.postValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // to do
            }
        });
    }

    public MutableLiveData<User> userReport(String userId,String token) {

        report(userId, token);

        return publicUser;
    }

    private void rate(String userId, String token) {
        setupRetrofit(token);
        mBackEndService.reteUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    // to do
//                    ErrorHandlingUtils.errorHandling("dummy");
                }

                if (response.body() == null)
                    return;

                publicUser.postValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // to do
            }
        });
    }

    public MutableLiveData<User> userRate(String userId, String token) {

        rate(userId, token);

        return publicUser;
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
