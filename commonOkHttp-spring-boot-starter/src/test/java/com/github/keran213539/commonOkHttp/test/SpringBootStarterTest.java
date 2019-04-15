package com.github.keran213539.commonOkHttp.test;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.keran213539.commonOkHttp.CommonOkHttpClient;

/**
 * @ClassName: SpringBootStarterTest
 * @Description: SpringBootStarter测试
 * @author klw
 * @date 2019年4月14日 下午4:32:44
 */
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= {JUnit5TestApplication.class})
public class SpringBootStarterTest {

    @Autowired
    private CommonOkHttpClient defaultHttps;
    
    @Test
    public void testDownload() {
	String imageUrl = "http://f.hiphotos.baidu.com/image/pic/item/71cf3bc79f3df8dcfcea3de8c311728b461028f7.jpg";
	System.out.println(defaultHttps.download(imageUrl, null).length);
	System.out.println(defaultHttps.download(null, imageUrl).body().contentLength());
	System.out.println("==============上面是同步,下面是异步============");
	defaultHttps.download(imageUrl, fileBytes -> {
	    System.out.println(fileBytes.length);
	});
	
	defaultHttps.download(fileBytes -> {
	    try {
		System.out.println(fileBytes.body().bytes().length);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}, imageUrl);
    }
    
    /**
     * @Title: afterAll4Async
     * @Description: 测试异步时使用,阻塞主线程
     */
    @AfterAll
    public static void afterAll4Async() {
	synchronized (SpringBootStarterTest.class) {
	    while (true) {
		try {
		    SpringBootStarterTest.class.wait();
		} catch (Throwable e) {
		    e.printStackTrace();
		}
	    }
	}
    }
    
}
