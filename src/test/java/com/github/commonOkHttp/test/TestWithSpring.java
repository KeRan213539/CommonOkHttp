package com.github.commonOkHttp.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.commonOkHttp.CommonOkHttpClient;
import com.github.commonOkHttp.UploadFile;


/**
 * @ClassName: CustomOkHttpClientTest
 * @Description: 通用OKHttp封装 JUnit5 测试
 * @author klw
 * @date 2018年4月4日 下午4:57:43
 */
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration({"classpath*:applicationContext-testOkHttp.xml"})
public class TestWithSpring {
    
    /**
     * @Fields defaultHttps : 默认CA方式
     */
    @Autowired
    @Qualifier("httpClientDefaultHttps")
    private CommonOkHttpClient defaultHttps;
    
    /**
     * @Fields httpClientNotSafe : 不安全
     */
    @Autowired
    @Qualifier("httpClientNotSafe")
    private CommonOkHttpClient httpClientNotSafe;
    
    /**
     * @Fields httpClientCustomCertificate : 自定义证书 + 不验证host name
     */
    @Autowired
    @Qualifier("httpClientCustomCertificate")
    private CommonOkHttpClient httpClientCustomCertificate;
    
    /**
     * @Title: testDefaultHttps
     * @Description: 测试默认https(使用CA证书)
     */
    @Test
    public void testDefaultHttps() {
	System.out.println(defaultHttps.get("https://www.jianshu.com/p/f5320b1e0287", null));  // 访问https
	System.out.println(defaultHttps.post("https://www.baidu.com", null));  // 访问https
	System.out.println("=====================================================================");
	System.out.println(defaultHttps.post("http://www.123cha.com", null));  // 访问普通http
	System.out.println("=====================================================================");
	
    }
    
    /**
     * @Title: testCustomHttps
     * @Description: 信任证书
     */
    @Test
    public void testCustomHttps() {
	System.out.println(httpClientCustomCertificate.get("https://www.jianshu.com/p/f5320b1e0287", null));  // 访问https-- 不能访问
    }
    
    

    /**
     * @Title: testPostData
     * @Description: 测试post数据
     */
//    @Test
    public void testPostData() {
	// TODO 下面的地址不存在,这里只是保留代码作为示例
	Map<String, String> prarm = new HashMap<>();
	prarm.put("userAccount", "111");
	prarm.put("password", "111");
	System.out.println(httpClientCustomCertificate.post("https://xxx.xxx.xxx/login.do", prarm, null)); // 测试post提交数据
    }
    
    /**
     * @Title: testFileUpload
     * @Description: 测试文件上传
     */
//    @Test
    public void testFileUpload() {
	
	// TODO 以下代码跑不过,因为请求地址是不存在的,这里保留代码作为示例
	
	Map<String, String> prarm = new HashMap<>();
	prarm.put("aaa", "aaa");
	prarm.put("bbb", "bbb");
	List<UploadFile> files = new ArrayList<>();
	UploadFile file1 = new UploadFile();
//	List<UploadByteFile> files = new ArrayList<>();
//	UploadByteFile file1 = new UploadByteFile();
//	
//	String fileNameR = "e:/test2.jpg";
//        try {
//            DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(fileNameR)));
//            int b;
//            ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
//            while ((b = in.read()) != -1) {
//                byteOS.write(b);
//            }
//            in.close();
//            file1.setFileBytes(byteOS.toByteArray());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
	
	file1.setFile(new File("E:/test3.jpg"));
	file1.setMediaType("image/jpeg");
	file1.setPrarmName("file1");
//	file1.setFileName("test1.jpg");
	
	UploadFile file2 = new UploadFile();
	file2.setFile(new File("E:/test.jpg"));
	file2.setMediaType("image/jpeg");
	file2.setPrarmName("file2");
	
	UploadFile file3 = new UploadFile();
	file3.setFile(new File("E:/test2.jpg"));
	file3.setMediaType("image/jpeg");
	file3.setPrarmName("file3");
	
	files.add(file1);
	files.add(file2);
	files.add(file3);
	System.out.println(httpClientCustomCertificate.post("https://xxx.xxx.xxx/fileupload.do", prarm, files, null));  // 测试post上传文件
    }
    
    /**
     * @Title: testPostJson
     * @Description: 测试 post json
     */
//    @Test
    public void testPostJson() {
	// TODO 以下代码跑不过,因为请求地址是不存在的,这里保留代码作为示例
	String json = "{\"id\":\"123123\", \"method\":\"account_login\", \"params\":{\"userName\":\"xxxx\",\"passWord\":\"1\"}}";
	System.out.println(defaultHttps.post("https://xxx.xxx.xxx//test.do", json, null));  // 测试post json
	defaultHttps.post("https://xxx.xxx.xxx//test.do", json, (str) -> System.out.println("str=========== \n" + str));  // 测试post json
    }
    
    /**
     * @Title: testNotSafe
     * @Description: 测试不安全方式访问自签证书
     */
    @Test
    public void testNotSafe() {
	// TODO 由于没有在外网找到合适的测试环境,所以代码已删除,实际已测过
    }
    
//  TODO 指定加/解密证书(微信退款时用,因目前没有环境暂时无法测)
    
    /**
     * @Title: afterAll4Async
     * @Description: 测试异步时使用,阻塞主线程
     */
//    @AfterAll
    public static void afterAll4Async() {
	synchronized (TestWithSpring.class) {
	    while (true) {
		try {
		    TestWithSpring.class.wait();
		} catch (Throwable e) {
		    e.printStackTrace();
		}
	    }
	}
    }

}
