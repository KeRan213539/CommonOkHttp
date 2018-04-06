/*
 * Copyright 2018 klw(213539@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.commonOkHttp;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.github.commonOkHttp.callback.IAsyncCallback;
import com.github.commonOkHttp.utils.HttpsUtils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @ClassName: CommonOkHttpClient
 * @Description: 通用 OkHttp 封装
 * @author klw
 * @date 2018年4月3日 下午3:25:06
 */
public final class CommonOkHttpClient {
    
    private OkHttpClient okHttpClient;
    
    CommonOkHttpClient(long readTimeout, long writeTimeout, long connectTimeout, HttpsUtils.SSLParams sslParams) {
	OkHttpClient.Builder builder = new OkHttpClient.Builder();
	builder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
        builder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        builder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        
        // sslParams 如果为null只是不设置证书相关的参数,而使用默认的CA认证方式
	if (sslParams != null) {
	    builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
	    if(sslParams.hostnameVerifier != null) {
		builder.hostnameVerifier(sslParams.hostnameVerifier);
	    }
	}
	okHttpClient = builder.build();
    }
    
    
    /**
     * @Title: get
     * @Description: 发送 get 请求, 有 callback为异步,callback传null为同步;异步时返回null
     * @param url
     * @param callback
     * @return
     */
    public String get(String url, IAsyncCallback callback) {
	Request request = new Request.Builder().get().url(url).build();
	return sendRequest(request, callback);
    }
    
    /**
     * @Title: post
     * @Description: 使用无参方式发送post请求, 有 callback为异步,callback传null为同步;异步时返回null
     * @param url
     * @param callback
     * @return
     */
    public String post(String url, IAsyncCallback callback) {
	return doPost(url, null, null, callback);
    }
    
    /**
     * @Title: post
     * @Description: 使用json方式发送post请求, 有 callback为异步,callback传null为同步;异步时返回null
     * @param url
     * @param jsonStr
     * @param callback
     * @return
     */
    public String post(String url,String jsonStr, IAsyncCallback callback) {
	return doPost(url, null, jsonStr, callback);
    }
    
    /**
     * @Title: post
     * @Description: 使用传统参数方式发送post请求, 有 callback为异步,callback传null为同步;异步时返回null
     * @param url
     * @param prarm
     * @param callback
     * @return
     */
    public String post(String url, Map<String, String> prarm, IAsyncCallback callback) {
	return doPost(url, prarm, null, callback);
    }
    
    /**
     * @Title: post
     * @Description: 文件上传(支持多文件)
     * @param url
     * @param prarm
     * @param files
     * @param callback
     * @return
     */
    public <T extends UploadFileBase> String post(String url, Map<String, String> prarm, List<T> files, IAsyncCallback callback) {
	okhttp3.MultipartBody.Builder builder = new MultipartBody.Builder();
	builder.setType(MultipartBody.FORM);
	prarm.forEach((k, v) -> builder.addFormDataPart(k, v));
	files.stream().forEach((file) -> {
	    if(file instanceof UploadFile) {
		UploadFile fileTmp = (UploadFile)file;
		builder.addFormDataPart(file.getPrarmName(), fileTmp.getFile().getName(), RequestBody.create(MediaType.parse(fileTmp.getMediaType()), fileTmp.getFile()));
	    } else if(file instanceof UploadByteFile) {
		UploadByteFile fileTmp = (UploadByteFile)file;
		builder.addFormDataPart(file.getPrarmName(), fileTmp.getFileName(), RequestBody.create(MediaType.parse(fileTmp.getMediaType()), fileTmp.getFileBytes()));
	    }
	});
	MultipartBody uploadBody = builder.build();
	Request request = new Request.Builder()
                .post(uploadBody)
                .url(url).
                build();
	return sendRequest(request, callback);
    }
    
    /**
     * @Title: doPost
     * @Description: 执行post
     * @param url
     * @param prarm  传统参数方式
     * @param jsonStr  json参数方式
     * @param callback
     * @return
     */
    private String doPost(String url, Map<String, String> prarm, String jsonStr, IAsyncCallback callback) {
	RequestBody body = okhttp3.internal.Util.EMPTY_REQUEST;
	if(StringUtils.isNotBlank(jsonStr)) {
	    body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);
	} else if(!CollectionUtils.isEmpty(prarm)) {
	    Builder builder = new FormBody.Builder();
	    prarm.forEach((k, v) -> builder.add(k, v));
	    body = builder.build();
	}
	Request request = new Request.Builder()
                .post(body)
                .url(url).
                build();
	return sendRequest(request, callback);
    }
    
    /**
     * @Title: sendRequest
     * @Description: 发送请求
     * @param request
     * @param callback
     * @return
     */
    private String sendRequest(Request request, IAsyncCallback callback) {
	if (callback == null) {
	    // 同步
	    try {
		Response response = okHttpClient.newCall(request).execute();
		return response.body().string();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	} else {
	    // 异步
	    okHttpClient.newCall(request).enqueue(new Callback() {
		@Override
		public void onFailure(Call call, IOException e) {
		    callback.doCallback(null);
		}

		@Override
		public void onResponse(Call call, Response response) {
		    try {
			callback.doCallback(response.body().string());
		    } catch (IOException e) {
			callback.doCallback(null);
			// e.printStackTrace();
		    }
		}
	    });
	}
	return null;
    }
    
}
