package io.dactilo.sumbawa.spring.webdocs.docx;

import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;

public class DOCXHandlerMethodReturnValueHandler extends AbstractHttpMessageConverter<Object> {
    DOCXHandlerMethodReturnValueHandler() {
        super(new MediaType("application", "vnd.openxmlformats-officedocument.wordprocessingml.document"));
    }

    public void htmlToWord(OutputStream outputStream, String html) throws Docx4JException, IOException {
        if(Context.jc == null) {
            //Context.jc;
        }

        final WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
        final XHTMLImporterImpl xhtmlImporter = new XHTMLImporterImpl(wordMLPackage);

        wordMLPackage.getMainDocumentPart().getContent()
                .addAll(xhtmlImporter.convert(html, null));

        wordMLPackage.save(outputStream);
    }


    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        throw new UnsupportedOperationException("DOCX Parsing is not implemented");
    }

    @Override
    protected void writeInternal(Object input, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        try {
            htmlToWord(outputMessage.getBody(), input.toString());
        } catch (Docx4JException e) {
            throw new IOException(e);
        }
    }
}
