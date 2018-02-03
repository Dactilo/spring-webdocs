package io.dactilo.sumbawa.spring.webdocs.pdf;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletContext;

import static org.hamcrest.core.IsNot.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {
        SamplePDFConfiguration.class
})
public class PDFHandlerMethodReturnValueHandlerTest {
    private MockMvc mockMvc;

    private static ServletContext servletContext() {
        return new MockServletContext("/", new FileSystemResourceLoader());
    }

    @Before
    public void setUp() {
        try (AnnotationConfigWebApplicationContext configurableWebApplicationContext = new AnnotationConfigWebApplicationContext()) {
            configurableWebApplicationContext.setServletContext(servletContext());
            configurableWebApplicationContext.register(SamplePDFConfiguration.class);
            configurableWebApplicationContext.refresh();


            mockMvc = MockMvcBuilders.webAppContextSetup(configurableWebApplicationContext)
                    .build();
        }
    }

    @Test
    public void testSampleController_htmlWorks() throws Exception {
        mockMvc.perform(get("/html"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/html;charset=utf-8"))
                .andExpect(content().string("test"));
    }

    @Test
    public void testSampleController_pdfWorks() throws Exception {
        mockMvc.perform(get("/pdf"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/pdf;charset=utf-8"))
                .andExpect(content().string(not("test")));
    }
}

@Configuration
@EnableWebMvc
@EnablePDFFormatter
class SamplePDFConfiguration extends WebMvcConfigurerAdapter {
    @Bean
    public SampleController sampleController() {
        return new SampleController();
    }
}


@Controller
@RequestMapping("/")
@ResponseStatus(HttpStatus.OK)
class SampleController {
    @RequestMapping(value = "html", method = RequestMethod.GET, produces = "text/html;charset=utf-8")
    public ResponseEntity<String> html() {
        return new ResponseEntity<>("test", HttpStatus.OK);
    }

    @RequestMapping(value = "pdf", method = RequestMethod.GET, produces = "application/pdf;charset=utf-8")
    public ResponseEntity<String> pdf() {
        return new ResponseEntity<>("test", HttpStatus.OK);
    }
}