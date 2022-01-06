package com.example.emos.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * copyright (C), 2021, 运达科技有限公司
 * fileName  SwaggerConfig
 *
 * @author 王玺权
 * date  2021-12-31 17:24
 * description
 * reviser
 * revisionTime
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
   public Docket createRestApi(){
       Docket docket=new Docket(DocumentationType.SWAGGER_2);

       ApiInfoBuilder builder=new ApiInfoBuilder();
       builder.title("EMOS在线办公系统");
       ApiInfo apiInf=builder.build();
       docket.apiInfo(apiInf);

       ApiSelectorBuilder selectorBuilder=docket.select();
       selectorBuilder.paths(PathSelectors.any());
       selectorBuilder.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class));
       docket=selectorBuilder.build();

       ApiKey apiKey=new ApiKey("token","token","header");
       List<ApiKey> apiKeyList=new ArrayList<>();
       apiKeyList.add(apiKey);
       docket.securitySchemes(apiKeyList);

//       作用域
       AuthorizationScope scope=new AuthorizationScope("global","accessEverything");
       AuthorizationScope[] scopes={scope};
       SecurityReference reference=new SecurityReference("token",scopes);
       List refList=new ArrayList();
       refList.add(reference);
       SecurityContext context=SecurityContext.builder().securityReferences(refList).build();
       List cxtList=new ArrayList();
       cxtList.add(context);
       docket.securityContexts(cxtList);

       return docket;
   }
}
