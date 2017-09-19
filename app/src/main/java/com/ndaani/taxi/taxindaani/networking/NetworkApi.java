package com.ndaani.taxi.taxindaani.networking;




import com.ndaani.taxi.taxindaani.model.AuthData;
import com.ndaani.taxi.taxindaani.model.AuthResponse;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface NetworkApi {


    // POST Requests
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("verify/agent")
    Observable<Response<AuthResponse>> postPhoneForVerification(@Body AuthData authData);



}
