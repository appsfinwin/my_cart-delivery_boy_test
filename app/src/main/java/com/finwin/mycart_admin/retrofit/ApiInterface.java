package com.finwin.mycart_admin.retrofit;

import com.finwin.mycart_admin.login.pojo.LoginResponse;
import com.finwin.mycart_admin.sign_up.pojo.GetBranchResponse;
import com.finwin.mycart_admin.sign_up.pojo.sign_up_response.SignUpResponse;

import io.reactivex.Single;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("Login_user")
    Single<LoginResponse> login(@Body RequestBody body);


    @POST("CustomerManagement_Cart_Api")
    Single<SignUpResponse> signUp(@Body RequestBody body);

  @POST("Branch_registration")
    Single<GetBranchResponse> getBranches(@Body RequestBody body);



}