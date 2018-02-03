package io.dactilo.sumbawa.spring.webdocs.docx;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Add this annotation to your spring web configuration to enable Docx support
 */
@Retention(value = RUNTIME)
@Target(value = {TYPE})
@Documented
@Import({WordFormatterConfiguration.class})
public @interface EnableWordFormatter {
}
