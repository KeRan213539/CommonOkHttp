package com.github.keran213539.commonOkHttp.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: CommonOkHttpSpringBootCfgBean
 * @Description: CommonOkHttp spring boot 配制信息
 * @author klw
 * @date 2019年4月14日 下午4:12:57
 */
@ConfigurationProperties(prefix="common-okhttp")
@Getter
@Setter
public class CommonOkHttpSpringBootCfgBean {

    /**
     * @Fields readTimeoutMilliSeconds : 读超时
     */
    private long readTimeoutMilliSeconds = 100000;
    
    /**
     * @Fields writeTimeout : 写超时
     */
    private long writeTimeout = 10000;
    
    /**
     * @Fields connectTimeout : 连接超时
     */
    private long connectTimeout = 15000;
    
    
    //=========以下为https相关参数,如果不请求https,或者要使用默认CA方式,可以不用设置==============
    
    /**
     * @Fields unSafe : 是否使用不安全的方式(不对证书做任何效验), 如果此参数为默认值,并且没有添加信人证书,则使用默认CA方式验证
     */
    boolean unSafe = false;
    
    /**
     * @Fields checkHostname : 是否验证域名/IP, 仅对添加自签证书为信任时生效
     */
    boolean checkHostname = true;
    
    /**
     * @Fields certificateFilePaths : 用含有服务端公钥的证书校验服务端证书(添加自签证书为信任证书)
     */
    private Resource[] certificateFilePaths;
    
    
    /**
     * @Fields pkcsFile : 使用 指定 PKCS12 证书加密解密数据(应对支付宝,微信支付等)
     */
    private String pkcsFile = null;
    
    /**
     * @Fields pkcsFilePwd : PKCS12 证书的密码
     */
    private String pkcsFilePwd = null;
    
}
