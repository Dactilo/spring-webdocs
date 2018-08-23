package io.dactilo.sumbawa.spring.webdocs.docx;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DOCXHandlerMethodReturnValueHandlerTest {
    private final DOCXHandlerMethodReturnValueHandler docxHandlerMethodReturnValueHandler = new DOCXHandlerMethodReturnValueHandler();

    private MockMvc mockMvc;

    private static ServletContext servletContext() {
        return new MockServletContext("/", new FileSystemResourceLoader());
    }

    @Before
    public void setUp() {
        try (AnnotationConfigWebApplicationContext configurableWebApplicationContext = new AnnotationConfigWebApplicationContext()) {
            configurableWebApplicationContext.setServletContext(servletContext());
            configurableWebApplicationContext.register(SampleDOCXConfiguration.class);
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
                .andExpect(content().string("<b>test</b>"));
    }

    @Test
    public void testSampleController_pdfWorks() throws Exception {
        mockMvc.perform(get("/docx"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/vnd.openxmlformats-officedocument.wordprocessingml.document;charset=utf-8"))
                .andExpect(content().string(not("<b>test</b>")));
    }

    @Test
    public void testHtmlToWord_Sample() throws IOException, Docx4JException {
        final String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" +
                "        \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <title>Compte-rendu</title>\n" +
                "\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Calibri;\n" +
                "            font-size:13px;\n" +
                "        }\n" +
                "        table.report {\n" +
                "            width: 100%;\n" +
                "        }\n" +
                "\n" +
                "        table.report p {\n" +
                "            margin: 0;\n" +
                "        }\n" +
                "\n" +
                "        .point {\n" +
                "            color: #34495e;\n" +
                "        }\n" +
                "\n" +
                "        .action {\n" +
                "            color: #3498db;\n" +
                "        }\n" +
                "\n" +
                "        .decision {\n" +
                "            color: #27ae60;\n" +
                "        }\n" +
                "\n" +
                "        .alert {\n" +
                "            color: #c0392b;\n" +
                "        }\n" +
                "\n" +
                "        span.alert {\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "\n" +
                "        .information {\n" +
                "            color: #f1c40f;\n" +
                "        }\n" +
                "\n" +
                "        .point-o {\n" +
                "            background-color: #34495e;\n" +
                "        }\n" +
                "\n" +
                "        .action-o {\n" +
                "            background-color: #3498db;\n" +
                "        }\n" +
                "\n" +
                "        .decision-o {\n" +
                "            background-color: #27ae60;\n" +
                "        }\n" +
                "\n" +
                "        .alert-o {\n" +
                "            background-color: #c0392b;\n" +
                "        }\n" +
                "\n" +
                "        .information-o {\n" +
                "            background-color: #f1c40f;\n" +
                "        }\n" +
                "\n" +
                "        .placeholder-o {\n" +
                "            background-color: #CCCCCC;\n" +
                "        }\n" +
                "\n" +
                "        table.report tr.point-title {\n" +
                "            font-size: 18px;\n" +
                "            color: #34495e;\n" +
                "            border-bottom: 1px solid #34495e;\n" +
                "            padding-bottom: 5px;\n" +
                "        }\n" +
                "\n" +
                "        table.report tr.point-title  td {\n" +
                "            padding-bottom: 5px;\n" +
                "            padding-top: 15px;\n" +
                "            border-bottom: 1px solid #34495e;\n" +
                "        }\n" +
                "\n" +
                "        table.report p {\n" +
                "            margin: 0;\n" +
                "        }\n" +
                "\n" +
                "        table.report tr.item td {\n" +
                "            padding-top: 0;\n" +
                "            padding-bottom: 0;\n" +
                "        }\n" +
                "\n" +
                "        table.report tr.item > td {\n" +
                "            padding-top: 5px;\n" +
                "        }\n" +
                "\n" +
                "        table.report tr.item td.icon {\n" +
                "            vertical-align: top;\n" +
                "            width: 25px;\n" +
                "        }\n" +
                "\n" +
                "        table.report tr.item td.icon table td {\n" +
                "            vertical-align: middle;\n" +
                "            width: 25px;\n" +
                "        }\n" +
                "\n" +
                "        table.report tr.item td.deadline {\n" +
                "            vertical-align: top;\n" +
                "            width: 100px;\n" +
                "            color: #34495e;\n" +
                "            font-weight:bold;\n" +
                "            text-align: right;\n" +
                "        }\n" +
                "\n" +
                "        table.report tr.item td.responsible {\n" +
                "            vertical-align: top;\n" +
                "            width: 200px;\n" +
                "            color: #34495e;\n" +
                "            font-weight:bold;\n" +
                "            text-align: right;\n" +
                "        }\n" +
                "\n" +
                "        table.item-type {\n" +
                "            padding: 0;\n" +
                "            height: 25px;\n" +
                "            width: 50px;\n" +
                "            font-family: arial;\n" +
                "        }\n" +
                "\n" +
                "        table.item-type td.icon {\n" +
                "            width:25px;\n" +
                "            height: 25px;\n" +
                "            text-align: center;\n" +
                "            color: #FFF;\n" +
                "            vertical-align: middle;\n" +
                "            border-radius:10px;\n" +
                "        }\n" +
                "\n" +
                "        table.item-type td.margin {\n" +
                "            width: 5px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "<h1>Nouvelle réunion</h1>\n" +
                "<table class=\"report\">\n" +
                "    <tbody>\n" +
                "    <tr class=\"point-title\">\n" +
                "        <td colspan=\"4\" class=\"title\"><p>test</p></td>\n" +
                "    </tr>\n" +
                "\n" +
                "    <tr class=\"item\">\n" +
                "        <td class=\"icon\">\n" +
                "            <table class=\"item-type\" style=\"\">\n" +
                "                <tbody>\n" +
                "                <tr>\n" +
                "                    <td class=\"action-o icon\">A</td>\n" +
                "                    <td class=\"margin\"></td>\n" +
                "                </tr>\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "        </td>\n" +
                "        <td>\n" +
                "            a\n" +
                "\n" +
                "            <ul>\n" +
                "                \n" +
                "            </ul>\n" +
                "        </td>\n" +
                "        <td class=\"responsible\">\n" +
                "            Quentin Pâris\n" +
                "        </td>\n" +
                "        <td class=\"deadline\">02/08/2018</td>\n" +
                "\n" +
                "    </tr>\n" +
                "    <tr class=\"item\">\n" +
                "        <td class=\"icon\">\n" +
                "            <table class=\"item-type\" style=\"\">\n" +
                "                <tbody>\n" +
                "                <tr>\n" +
                "                    <td class=\"action-o icon\">A</td>\n" +
                "                    <td class=\"margin\"></td>\n" +
                "                </tr>\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "        </td>\n" +
                "        <td>\n" +
                "            <p><br /></p>\n" +
                "\n" +
                "            <ul>\n" +
                "                \n" +
                "            </ul>\n" +
                "        </td>\n" +
                "        <td class=\"responsible\">\n" +
                "            Quentin Pâris\n" +
                "        </td>\n" +
                "        <td class=\"deadline\">02/08/2018</td>\n" +
                "\n" +
                "    </tr>\n" +
                "    </tbody>\n" +
                "</table>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        docxHandlerMethodReturnValueHandler.htmlToWord(outputStream, html);

        byte[] results = outputStream.toByteArray();
        assertTrue(results.length > 0);
    }
}

@Configuration
@EnableWebMvc
@EnableWordFormatter
class SampleDOCXConfiguration extends WebMvcConfigurerAdapter {
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
        return new ResponseEntity<>("<b>test</b>", HttpStatus.OK);
    }

    @RequestMapping(value = "docx", method = RequestMethod.GET, produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document;charset=utf-8")
    public ResponseEntity<String> docx() {
        return new ResponseEntity<>("<b>test</b>", HttpStatus.OK);
    }
}