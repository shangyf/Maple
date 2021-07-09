package com.seven.net.utils;


import com.google.gson.Gson;
import com.seven.net.bean.ResponseBean;
import com.seven.net.callback.CallBack;
import com.seven.net.gson.MyGsonBuilder;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by SeVen on 2019-11-12 14:40
 */
public class ApiUtils {
    public static OkHttpClient okHttpClient;

    public static void intiOkHttp(HttpLoggingInterceptor httpLoggingInterceptor, Interceptor interceptor) {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(interceptor)
                .sslSocketFactory(com.seven.net.utils.SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(com.seven.net.utils.SSLSocketClient.getHostnameVerifier())
                .build();
    }


    public static void intiOkHttp(HttpLoggingInterceptor httpLoggingInterceptor, Interceptor headInterceptor, Interceptor tokenInterceptor) {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(tokenInterceptor)
                .addInterceptor(headInterceptor)
                .sslSocketFactory( SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier( SSLSocketClient.getHostnameVerifier())
                .build();
    }


    public static HttpLoggingInterceptor getHttpLoggingInterceptor(boolean isDebug) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        if (isDebug) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return httpLoggingInterceptor;
    }

    public static <T> ResponseBean<T> json2Object(Type type, String resp) throws Exception {
        ResponseBean<T> responseBean = new ResponseBean<>();
        Gson gson = MyGsonBuilder.createGson();
        JSONObject jsonObject = new JSONObject(resp);
        responseBean.setStatus(jsonObject.optInt("status"));
        responseBean.setErrorMsg(jsonObject.optString("errorMsg"));
        String data = jsonObject.optString("data");
        responseBean.setResults(jsonObject.optString("results"));
        if (jsonObject.has("access_token")) {
            responseBean.setAccess_token(jsonObject.optString("access_token"));
        }
        responseBean.setOver_time(jsonObject.optString("over_time"));
        responseBean.setCreate_time(jsonObject.optString("create_time"));
        responseBean.setExpire(jsonObject.optString("expire"));
        T bean;
        if (type == String.class) {
            bean = (T) data;
        } else {
            bean = gson.fromJson(data, type);
        }
        responseBean.setData(bean);
        return responseBean;
    }

    public static <T> Disposable toSubscribe(Observable<ResponseBean<T>> observable, final CallBack<T> apiCallBack) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBean<T>>() {
                    @Override
                    public void accept(ResponseBean<T> responseBean) {
                        apiCallBack.onResponse(responseBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        apiCallBack.onFailure(throwable);
                    }
                });
    }

    public static Observable<String> get(final String url) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try {
                    final Request request = new Request.Builder().get().url(url).build();
                    Response response = okHttpClient.newCall(request).execute();
                    emitter.onNext(response.body().string());
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }

    public static Observable<String> patch(final Object object, final String url) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try {
                    String bodyString = MyGsonBuilder.createGson().toJson(object);
                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), bodyString);
                    final Request request = new Request.Builder().patch(body).url(url).build();
                    Response response = okHttpClient.newCall(request).execute();
                    emitter.onNext(response.body().string());
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }

    public static Observable<String> post(final Object object, final String url) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try {
                    String bodyString = MyGsonBuilder.createGson().toJson(object);
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bodyString);
                    final Request request = new Request.Builder().post(body).url(url).build();
                    Response response = okHttpClient.newCall(request).execute();
                    emitter.onNext(response.body().string());
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }

    public static Observable<String> post(final String bodyString, final String url) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try {
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bodyString);
                    final Request request = new Request.Builder().post(body).url(url).build();
                    Response response = okHttpClient.newCall(request).execute();
                    emitter.onNext(response.body().string());
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }

    public static Observable<String> delete(final String url) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try {
                    final Request request = new Request.Builder().delete().url(url).build();
                    Response response = okHttpClient.newCall(request).execute();
                    emitter.onNext(response.body().string());
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }

    public static Observable<String> put(final Object object, final String url) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try {
                    String bodyString = MyGsonBuilder.createGson().toJson(object);
                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), bodyString);
                    final Request request = new Request.Builder().put(body).url(url).build();
                    Response response = okHttpClient.newCall(request).execute();
                    emitter.onNext(response.body().string());
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }

    /**
     * 文件上传
     *
     * @param url       图片上传的url
     * @param fileId    本地文件的存储文件id
     * @param file      文件
     * @param imageType MediaType mediaType = MediaType.parse("image/jpeg");
     * @return
     */
    public static Observable<String> uploadFile(final String url, final String fileId, final File file, final MediaType imageType) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try {
                    String path = file.getAbsolutePath();
                    int idx = path.lastIndexOf(".");
                    String suffix = null;
                    if (idx > -1) {
                        suffix = path.substring(idx);
                    }
                    String fileNameOnRemote = fileId + suffix;
                    RequestBody body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart(fileId, fileNameOnRemote,
                                    RequestBody.create(imageType, file))
                            .build();
                    final Request request = new Request.Builder().post(body).url(url).build();
                    Response response = okHttpClient.newCall(request).execute();
                    emitter.onNext(response.body().string());
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }
}
