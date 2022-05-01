package com.takeaway.gameofthree.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public ViewResolver thymeleafResolver() {
        ThymeleafViewResolver ivr = new ThymeleafViewResolver();
        ivr.setTemplateEngine(templateEngine());
        ivr.setOrder(0);
        return ivr;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver srtr = new SpringResourceTemplateResolver();
        srtr.setApplicationContext(applicationContext);
        srtr.setPrefix("classpath:/templates/");
        srtr.setSuffix(".html");
        return srtr;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }
}
