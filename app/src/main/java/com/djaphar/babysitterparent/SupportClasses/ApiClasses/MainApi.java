package com.djaphar.babysitterparent.SupportClasses.ApiClasses;

import com.djaphar.babysitterparent.SupportClasses.LocalDataClasses.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface MainApi {

    @POST("parents/")
    Call<User> requestRegistration(@Body LoginModel loginModel);

    @FormUrlEncoded
    @POST("token/parent/")
    Call<User> requestLogin(@Field("username") String username, @Field("password") String password);

    @GET("parents/me/")
    Call<Parent> requestProfile(@HeaderMap Map<String, String> headers);

    @PUT("parents/")
    Call<Parent> requestUpdateProfile(@HeaderMap Map<String, String> headers, @Body Parent parent);

    @POST("photos/")
    Call<Void> requestUpdatePicture(@HeaderMap Map<String, String> headers, @Body UpdatePictureModel updatePictureModel);

    @HTTP(method = "DELETE", path = "photos/", hasBody = true)
    Call<Parent> requestDeletePicture(@HeaderMap Map<String, String> headers, @Body UpdatePictureModel updatePictureModel);
}
