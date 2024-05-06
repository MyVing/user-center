package com.ving.usercenter.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
 
/**
 * 自定义Swagger接口文档配置
 *
 * @author ving
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 这里需要将改为controller位置便于自动生成接口
                .apis(RequestHandlerSelectors.basePackage("com.ving.usercenter.controller"))
                .paths(PathSelectors.any())
                .build();
    }
 
    //基本信息的配置，信息会在api文档上显示
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("用户中心")
                .description("接口文档")
                .termsOfServiceUrl("https://github.com/ving")
                .contact(new Contact("ving","https://github.com/ving","2960532192@qq.com"))
                .version("1.0")
                .build();
    }
}