package com.ndaani.taxi.taxindaani.auth;



import com.ndaani.taxi.taxindaani.model.APIError;
import com.ndaani.taxi.taxindaani.model.AuthData;
import com.ndaani.taxi.taxindaani.model.AuthResponse;
import com.ndaani.taxi.taxindaani.networking.ErrorUtils;
import com.ndaani.taxi.taxindaani.networking.NetworkService;

import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lawrence on 11/04/2017.
 */

public class AuthPresenter {

    private final NetworkService service;
    private final AuthView view;
    private CompositeSubscription subscriptions;


    public AuthPresenter(NetworkService service, AuthView view) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
    }


    public void verifyAgent(String phone) {

        view.showWait();
        Observable<Response<AuthResponse>> observable = service.getAPI().postPhoneForVerification(new AuthData(phone));
        Subscription subscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<AuthResponse>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("Checkpoint 6");
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.removeWait();
                    }

                    @Override
                    public void onNext(Response<AuthResponse> authResponse) {
                        view.removeWait();
                        if (authResponse.isSuccessful()) {
                            view.setAuthResponse(authResponse.body());
                        } else {

                            APIError error = ErrorUtils.parseError(authResponse);
                            view.onFailure(error.getMessage(), error.getDescription(), error.getCode());
                        }
                    }
                });

        subscriptions.add(subscription);
    }

    void onStop() {
        subscriptions.unsubscribe();
    }

}
