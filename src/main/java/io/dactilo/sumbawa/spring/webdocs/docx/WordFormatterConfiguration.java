package io.dactilo.sumbawa.spring.webdocs.docx;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class WordFormatterConfiguration extends WebMvcConfigurerAdapter {
    @Bean
    public DOCXHandlerMethodReturnValueHandler docxHandlerReturnValueHandler() {
        return new DOCXHandlerMethodReturnValueHandler();
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, docxHandlerReturnValueHandler());
    }
}
