package com.welinkdata.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * 启动程序
 *
 * @author ruoyi
 */

@EnableMongoAuditing
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class BizLinkApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(BizLinkApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  比邻（企业数字协同门户）启动成功   ლ(´ڡ`ლ)ﾞ  \n"  );
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//        return args -> {
//
////            System.out.println("Let's inspect the beans provided by Spring Boot:");
////
////            String[] beanNames = ctx.getBeanDefinitionNames();
////            Arrays.sort(beanNames);
////            for (String beanName : beanNames) {
////                System.out.println(beanName);
////            }
//            DPService dpService= (DPService)ctx.getBean("DPService");
//            dpService.setupInitData();
//            dpService.init();
//        };
//    }
}
