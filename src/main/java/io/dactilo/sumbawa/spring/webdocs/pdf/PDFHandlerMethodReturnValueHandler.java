package io.dactilo.sumbawa.spring.webdocs.pdf;

import org.apache.commons.io.IOUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class PDFHandlerMethodReturnValueHandler extends AbstractHttpMessageConverter<String> {
    private final PDFGenerator pdfGenerator;
    PDFHandlerMethodReturnValueHandler(PDFGenerator pdfGenerator) {
        super(
                new MediaType("application", "pdf"),
                new MediaType("application", "pdf", Charset.forName("ISO-8859-1")),
                new MediaType("application", "pdf", Charset.forName("UTF-8"))
        );
        this.pdfGenerator = pdfGenerator;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    protected String readInternal(Class<? extends String> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        throw new UnsupportedOperationException("PDF Parsing is not implemented");
    }

    private void htmlToPdf(OutputStream outputStream, String html) throws Docx4JException, IOException {
        final InputStream inputStream = pdfGenerator.convertHtmlToPDF(html, "--dpi", "300");
        IOUtils.copy(inputStream, outputStream);
    }

    @Override
    protected void writeInternal(String input, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        try {
            htmlToPdf(outputMessage.getBody(), input.toString());
        } catch (Docx4JException e) {
            throw new IOException(e);
        }
    }
}
