package io.dactilo.sumbawa.spring.webdocs.pdf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class PDFFormatterConfiguration extends WebMvcConfigurerAdapter {
    @Bean
    public PDFHandlerMethodReturnValueHandler pdfHandlerMethodReturnValueHandler() {
        return new PDFHandlerMethodReturnValueHandler(pdfGenerator());
    }

    @Bean
    PDFGenerator pdfGenerator() {
        return new PDFGenerator();
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, pdfHandlerMethodReturnValueHandler());
    }
}
