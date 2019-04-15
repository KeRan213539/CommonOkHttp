package com.github.keran213539.commonOkHttp.starter;

import javax.annotation.Resource;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.keran213539.commonOkHttp.CommonOkHttpClient;
import com.github.keran213539.commonOkHttp.CommonOkHttpClientBuilder;

/**
 * @ClassName: CommonOkHttpSpringBootCfg
 * @Description: CommonOkHttp spring boot 配制
 * @author klw
 * @date 2019年4月14日 下午4:07:37
 */
@Configuration
@EnableConfigurationProperties(CommonOkHttpSpringBootCfgBean.class)
public class CommonOkHttpSpringBootCfg {

    @Resource
    private CommonOkHttpSpringBootCfgBean cfgBean;
    
    @Bean
    public CommonOkHttpClient commonOkHttpClient() throws Exception {
	return new CommonOkHttpClientBuilder(cfgBean.getReadTimeoutMilliSeconds(), cfgBean.getWriteTimeout(), cfgBean.getConnectTimeout(), cfgBean.isUnSafe(), 
		cfgBean.isCheckHostname(), cfgBean.getCertificateFilePaths(), cfgBean.getPkcsFile(), cfgBean.getPkcsFilePwd()).build();
    }
    
}
