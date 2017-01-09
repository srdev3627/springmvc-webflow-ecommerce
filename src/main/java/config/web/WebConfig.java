package config.web;

import net.rossillo.spring.web.mvc.CacheControlHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import persistence.models.Authority;
import web.admin.converters.StringAuthorityConverter;

/**
 * @author sergio
 */
@Configuration
@EnableWebMvc
@Import(value = { ViewConfig.class, i18nConfig.class, WebFlowConfig.class })
@ComponentScan(value = "web")
public class WebConfig extends WebMvcConfigurerAdapter{
    
    @Autowired
    private LocaleChangeInterceptor localeChangeInterceptor;
    @Autowired
    private StringAuthorityConverter stringAuthorityConverter;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/403").setViewName("403");
        registry.addViewController("/admin/users/self").setViewName("admin/dashboard/user/self");
        registry.addViewController("/admin/404").setViewName("admin/errors/404");
        registry.addViewController("/500").setViewName("global/errors/500");
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedHeaders("Access-Control-Allow-Origin", "*")
                .allowedHeaders("Access-Control-Allow-Headers", "x-requested-with")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .maxAge(3600);
    }
    
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        // habilitar procesamiento de contenido estático
       configurer.enable();
    } 

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor);
        registry.addInterceptor(new CacheControlHandlerInterceptor());
    }
    
    @Bean(name="multipartResolver")
    public MultipartResolver provideMultipartResolver(){
        return new StandardServletMultipartResolver();
    }
    
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class, Authority.class, stringAuthorityConverter);
    }
}
