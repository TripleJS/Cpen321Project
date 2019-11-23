package com.cpen321.ubconnect.ui.account;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cpen321.ubconnect.model.AuthInterceptor;
import com.cpen321.ubconnect.model.ConstantsUtils;
import com.cpen321.ubconnect.model.IBackEndService;
import com.cpen321.ubconnect.model.NetworkUtil;
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
    private MutableLiveData<String> error = new MutableLiveData<>();

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
                    error.postValue(NetworkUtil.onServerResponseError(response));
                    return;
                }

                if (response.body() == null)
                    return;

                userAccount.postValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                error.postValue("Oops Something Went Wrong! Please Try Again Later!\n" + "more details:\n" + t.toString());
            }
        });
    }

    public MutableLiveData<User> getSetUserAccount() {
        return userAccount;
    }

    public void setUserInfo(String token, String userId, User user) {
        setupRetrofit(token);
        mBackEndService.postUserAccount(userId, user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    error.postValue(NetworkUtil.onServerResponseError(response));
                    return;
                }

                if (response.body() == null)
                    return;

                userAccount.postValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                error.postValue("Oops Something Went Wrong! Please Try Again Later!\n" + "more details:\n" + t.toString());
            }
        });
    }

    private void setupRetrofit(String token){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(token))
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstantsUtils.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        mBackEndService = retrofit.create(IBackEndService.class);
    }

    public MutableLiveData<String> getError(){
        return error;
    }

}


