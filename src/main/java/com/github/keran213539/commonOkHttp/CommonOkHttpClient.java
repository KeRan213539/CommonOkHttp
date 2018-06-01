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
package com.github.keran213539.commonOkHttp;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.github.keran213539.commonOkHttp.callback.IAsyncCallback;
import com.github.keran213539.commonOkHttp.callback.IAsyncCallback4Response;
import com.github.keran213539.commonOkHttp.utils.HttpsUtils;

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
import okhttp3.internal.http.HttpMethod;

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
	return (String) sendRequest(request, false, callback, null);
    }
    
    /**
     * @Title: get
     * @Description: 发送 get 请求并返回okhttp3.Response, 有 callback为异步,callback传null为同步;异步时返回null
     * @param url
     * @param callback
     * @return
     */
    public Response get(String url, Map<String, String> headerExt, IAsyncCallback4Response callback) {
	okhttp3.Request.Builder reqBuilder = new Request.Builder().get().url(url);
	if(headerExt != null && headerExt.size() > 0) {
	    headerExt.forEach((key, value) -> {
		reqBuilder.addHeader(key, value);
	    });
	}
	Request request = reqBuilder.build();
	return (Response) sendRequest(request, true, null, callback);
    }
    
    /**
     * @Title: post
     * @Description: 使用无参方式发送post请求, 有 callback为异步,callback传null为同步;异步时返回null
     * @param url
     * @param callback
     * @return
     */
    public String post(String url, IAsyncCallback callback) {
	return (String)doPost(url, null, null, callback, null, false, null);
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
	return (String)doPost(url, null, jsonStr, callback, null, false, null);
    }
    
    public String post(HttpMethod httpMethod, String url, String jsonStr, IAsyncCallback callback) {
//	RequestBody body = okhttp3.internal.Util.EMPTY_REQUEST;
//	if(StringUtils.isNotBlank(postStr)) {
//	    body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), postStr);
//	} else if(!CollectionUtils.isEmpty(prarm)) {
//	    Builder builder = new FormBody.Builder();
//	    prarm.forEach((k, v) -> builder.add(k, v));
//	    body = builder.build();
//	}
//	okhttp3.Request.Builder reqBuilder = new Request.Builder().post(body).url(url);
//	if(headerExt != null && headerExt.size() > 0) {
//	    headerExt.forEach((key, value) -> {
//		reqBuilder.addHeader(key, value);
//	    });
//	}
//	Request request = reqBuilder.build();
//	return sendRequest(request, isNeedResponse, callback, callback4Response);
	return null;
    }
    
    /**
     * @Title: post
     * @Description: 使用xml方式发送post请求, 有 callback为异步,callback传null为同步;异步时返回null
     * @param url
     * @param callback
     * @param xmlStr
     * @return
     */
    public String post(String url, IAsyncCallback callback, String xmlStr) {
	return (String) doPost(url, null, xmlStr, "application/xml", callback, null, false, null);
    }
    
    /**
     * @Title: post
     * @Description: 使用json方式发送post请求并返回okhttp3.Response, 有 callback为异步,callback传null为同步;异步时返回null
     * @param url
     * @param jsonStr
     * @param callback
     * @return
     */
    public Response post(String url,String jsonStr, Map<String, String> headerExt, IAsyncCallback4Response callback) {
	return (Response)doPost(url, null, jsonStr, null, callback, true, headerExt);
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
	return (String)doPost(url, prarm, null, callback, null, false, null);
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
	return (String) sendRequest(request, false, callback, null);
    }
    
    /**
     * @Title: doPost
     * @Description: 执行post
     * @param url
     * @param prarm  传统参数方式
     * @param jsonStr  json参数方式
     * @param callback  异步的回调方法,传null为同步
     * @param isNeedResponse  是否需要Response对象
     * @param callback4Response  传入Response对象的回调
     * @param headerExt  加到请求的header里的参数
     * @return
     */
    private Object doPost(String url, Map<String, String> prarm, String jsonStr, IAsyncCallback callback, IAsyncCallback4Response callback4Response, boolean isNeedResponse, Map<String, String> headerExt) {
	return doPost(url, prarm, jsonStr, "application/json", callback, callback4Response, isNeedResponse, headerExt);
    }
    
    
    /**
     * @Title: doPost
     * @Description: 执行post
     * @param url
     * @param prarm 传统参数方式
     * @param postStr  需要post的字符串
     * @param dataMediaType  需要post的字符串对应的格式: application/json; application/xml; application/text 等
     * @param callback  异步的回调方法,传null为同步
     * @param isNeedResponse  是否需要Response对象
     * @param callback4Response  传入Response对象的回调
     * @param headerExt  加到请求的header里的参数
     * @return
     */
    private Object doPost(String url, Map<String, String> prarm, String postStr, String dataMediaType , IAsyncCallback callback, IAsyncCallback4Response callback4Response, boolean isNeedResponse, Map<String, String> headerExt) {
	RequestBody body = okhttp3.internal.Util.EMPTY_REQUEST;
	if(StringUtils.isNotBlank(postStr)) {
	    body = RequestBody.create(MediaType.parse(dataMediaType + "; charset=utf-8"), postStr);
	} else if(!CollectionUtils.isEmpty(prarm)) {
	    Builder builder = new FormBody.Builder();
	    prarm.forEach((k, v) -> builder.add(k, v));
	    body = builder.build();
	}
	okhttp3.Request.Builder reqBuilder = new Request.Builder().post(body).url(url);
	if(headerExt != null && headerExt.size() > 0) {
	    headerExt.forEach((key, value) -> {
		reqBuilder.addHeader(key, value);
	    });
	}
	Request request = reqBuilder.build();
	return sendRequest(request, isNeedResponse, callback, callback4Response);
    }
    
    /**
     * @Title: sendRequest
     * @Description: 发送请求
     * @param request
     * @param isNeedResponse 是否需要返回okhttp3.Response
     * @param callback  不需要 okhttp3.Response 的 callback
     * @param callback4Response
     * @return
     */
    private Object sendRequest(Request request, boolean isNeedResponse, IAsyncCallback callback, IAsyncCallback4Response callback4Response) {
	if (callback == null) {
	    // 同步
	    try {
		Response response = okHttpClient.newCall(request).execute();
		if(isNeedResponse) {
		    return response;
		} else {
		    return response.body().string();
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	} else {
	    // 异步
	    okHttpClient.newCall(request).enqueue(new Callback() {
		@Override
		public void onFailure(Call call, IOException e) {
		    if(isNeedResponse) {
			callback4Response.doCallback(null);
		    } else {
			callback.doCallback(null);
		    }
		}

		@Override
		public void onResponse(Call call, Response response) {
		    try {
			if(isNeedResponse) {
			    callback4Response.doCallback(response);
			} else {
			    callback.doCallback(response.body().string());
			}
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
