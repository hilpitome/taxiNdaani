package com.ndaani.taxi.taxindaani.networking;


import com.ndaani.taxi.taxindaani.model.APIError;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Lawrence on 10/17/16.
 */

public class ErrorUtils {

    public static APIError parseError(Response<?> response) {
        NetworkService service = new NetworkService();
        Converter<ResponseBody, APIError> converter =
                service.getRetrofit()
                        .responseBodyConverter(APIError.class, new Annotation[0]);

        APIError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new APIError();
        }

        return error;
    }
}
