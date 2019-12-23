# CommonOkHttp - 通用OkHttp简单封装

一个用于Java应用(非安卓)的OkHttp的简单封装

[主仓库-github](https://github.com/KeRan213539/CommonOkHttp)  
[国内副本仓库-码云](https://gitee.com/213539/CommonOkHttp)


## 背景

OkHttp,现在很火的一个Apache httpClient的替代品,说替代可能不太合适,至少是同类型的库吧.
最近想学习使用下,并找一个通用工具在新项目中使用(老项目使用httpClient).既然它现在这么火,别人写好的通用工具应该很好找(嗯,拿来主意),结果一番搜索后,确实找到不少,但都是Android的,安卓的组件封装进去了不少,还有下载,下载进度...服务端根本用不到啊...@%#$%#$ 好吧,找不到就自己写...
在这里向大家介绍一个不错的Android的http框架 [OkGo](https://github.com/jeasonlzy/okhttp-OkGo),我参考了其中的https工具还有其他部分代码

## 如何使用

### spring boot2:

maven引入:

```
<dependency>
	<groupId>com.github.keran213539</groupId>
	<artifactId>commonOkHttp-spring-boot-starter</artifactId>
	<version>0.4.1</version>
</dependency>
```

通过spirng获取 com.github.keran213539.commonOkHttp.CommonOkHttpClient 并使用


### 通过spring工厂

maven引入:

```xml
<dependency>
  <groupId>com.github.keran213539</groupId>
  <artifactId>commonOkHttp</artifactId>
  <version>0.4.2</version>
</dependency>
```

参考 com.github.keran213539.commonOkHttp.test.TestWithSpring, 在spring 配制文件中增加:


	<bean id="httpClientDefaultHttps" class="com.github.keran213539.commonOkHttp.CommonOkHttpClientFactory" />
	
	<bean id="httpClientNotSafe" class="com.github.keran213539.commonOkHttp.CommonOkHttpClientFactory">
		<property name="unSafe" value="true" />
	</bean>
	
	<bean id="httpClientCustomCertificate" class="com.github.keran213539.commonOkHttp.CommonOkHttpClientFactory">
		<property name="checkHostname" value="false" />
		<property name="certificateFilePaths">
			<list>
				<value>classpath*:cers/jianshu.cer</value>
			</list>
		</property>
	</bean>

通过spirng获取 com.github.keran213539.commonOkHttp.CommonOkHttpClient 并使用

### 通过Builder
maven引入:

```xml
<dependency>
  <groupId>com.github.keran213539</groupId>
  <artifactId>commonOkHttp</artifactId>
  <version>0.4.2</version>
</dependency>
```

参考 com.github.keran213539.commonOkHttp.test.TestWithBuilder

	// 默认CA方式
	CommonOkHttpClient defaultHttps = new CommonOkHttpClientBuilder().build();
	// 不安全(忽略证书验证)
	CommonOkHttpClient httpClientNotSafe = new CommonOkHttpClientBuilder().unSafe(true).build();
	// 指定信任证书
	List<URL> certificateFilePaths = new ArrayList<>();
	certificateFilePaths.add(ResourceUtils.getURL("classpath:cers/jianshu.cer"));
	CommonOkHttpClient httpClientCustomCertificate = new CommonOkHttpClientBuilder().checkHostname(false).certificateFilePaths(certificateFilePaths).build();

### 方法说明

#### String get(String url, IAsyncCallback callback)

	发送 get 请求, 有 callback为异步,callback传null为同步;异步时返回null

#### Response get(String url, Map<String, String> headerExt, IAsyncCallback4Response callback)

```
发送 get 请求并返回okhttp3.Response, 有 callback为异步,callback传null为同步;异步时返回null
```

#### String post(String url, IAsyncCallback callback)

	使用无参方式发送post请求, 有 callback为异步,callback传null为同步;异步时返回null

#### String post(String url,String jsonStr, IAsyncCallback callback)
	使用json方式发送post请求, 有 callback为异步,callback传null为同步;异步时返回null

#### Response post(String url,String jsonStr, Map<String, String> headerExt, IAsyncCallback4Response callback)

```
使用json方式发送post请求并返回okhttp3.Response, 有 callback为异步,callback传null为同步;异步时返回null
```



#### String post(String url, Map<String, String> prarm, IAsyncCallback callback)
	使用传统参数方式发送post请求, 有 callback为异步,callback传null为同步;异步时返回null

#### String post(String url, IAsyncCallback callback, String xmlStr)

```
使用xml方式发送post请求, 有 callback为异步,callback传null为同步;异步时返回null
```



#### String post(String url, Map<String, String> prarm, List<T> files, IAsyncCallback callback)

	文件上传(支持多文件), 有 callback为异步,callback传null为同步;异步时返回null

#### byte[] download(String url, IAsyncCallback4Download callback)

```
下载文件并返回文件 byte[], 有 callback为异步,callback传null为同步;异步时返回null
```

#### byte[] download(String url, Map<String, String> headerExt, IAsyncCallback4Download callback)

```
下载文件并返回文件 byte[], 有 callback为异步,callback传null为同步;异步时返回null
```

#### Response download(Map<String, String> headerExt, IAsyncCallback4Response callback, String url)

```
下载文件并返回okhttp3.Response(可以通过response获取文件类型,文件大小, InputStream等, 有 callback为异步,callback传null为同步;异步时返回null
```

#### Response download(IAsyncCallback4Response callback, String url)

```
下载文件并返回okhttp3.Response(可以通过response获取文件类型,文件大小, InputStream等, 有 callback为异步,callback传null为同步;异步时返回null
```
#### Object doPost(String url, Map<String, String> prarm, String postStr, String dataMediaType, IAsyncCallback callback, IAsyncCallback4Response callback4Response, boolean isNeedResponse, Map<String, String> headerExt) {
```
执行post, 之前是内部调用的方法,现在放开
/**
* @param url
* @param prarm             传统参数方式
* @param postStr           需要post的字符串
* @param dataMediaType     需要post的字符串对应的格式: application/json; application/xml; application/text 等
* @param callback          异步的回调方法,传null为同步
* @param isNeedResponse    是否需要Response对象
* @param callback4Response 传入Response对象的回调
* @param headerExt         加到请求的header里的参数
* @return
* @Title: doPost
* @Description: 执行post
*/
```

## 关于测试

> Junit测试中的所有都已跑通
> JUnit的测试中删除了部分在公网没有找到合适的测试环境的代码(部分测试环境我是在本机模拟的环境),如使用自签证书的,不安全的方式,上传等,大家可以自签个证书部署为https自测
> 代码中自定义信任证书的部分,删除了一些其他环境的证书,只保留了简书的证书作为代码示例
