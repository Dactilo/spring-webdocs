package io.dactilo.sumbawa.spring.webdocs.pdf;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class PDFGenerator {
    InputStream convertHtmlToPDF(String htmlFileContent, String... args) {
        try {
            final File executable = File.createTempFile("wkhtmltopdf", "");
            final File library = File.createTempFile("wkhtmltopdf", ".so");
            final File tmpHtmlFile = File.createTempFile("wkhtmltopdf", ".html");

            writeExecutable(executable);
            writeLibrary(library);

            try(final OutputStream htmlOutput = new FileOutputStream(tmpHtmlFile)) {
                IOUtils.write(htmlFileContent, htmlOutput, Charset.forName("UTF-8"));
                executable.deleteOnExit();
                library.deleteOnExit();
                tmpHtmlFile.deleteOnExit();

                executable.setExecutable(true);

                final List<String> arguments = new ArrayList<>();
                arguments.addAll(Collections.singletonList(
                        executable.getAbsolutePath()
                ));
                arguments.addAll(Arrays.asList(args));
                arguments.addAll(Arrays.asList(tmpHtmlFile.getAbsolutePath(), "/dev/stdout"));

                final ProcessBuilder processBuilder = new ProcessBuilder(arguments);
                final Process process = processBuilder.start();

                return process.getInputStream();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void writeLibrary(File library) throws IOException {
        writeResource(fetchLibraryResource(), library);
    }

    private InputStream fetchLibraryResource() {
        final OperatingSystem operatingSystem = fetchOS();
        switch (operatingSystem) {
            case LINUX:
                return getClass().getResourceAsStream("/io/dactilo/sumbawa/spring/webdocs/pdf/linux/libwkhtmltox.so.0.12.4");
            case MAC:
                return getClass().getResourceAsStream("/io/dactilo/sumbawa/spring/webdocs/pdf/osx/libwkhtmltox.0.12.4.dylib");
        }

        throw new IllegalArgumentException("Unsupported OS");
    }

    private OperatingSystem fetchOS() {
        final String platformSystem = System.getProperty("os.name").toLowerCase();

        if(platformSystem.toLowerCase().contains("mac")) {
            return OperatingSystem.MAC;
        }

        if(platformSystem.toLowerCase().contains("linux")) {
            return OperatingSystem.LINUX;
        }

        throw new IllegalArgumentException("Unsupported OS");
    }

    private void writeExecutable(File executable) throws IOException {
        writeResource(fetchExecutableResource(), executable);
    }

    private void writeResource(InputStream inputStream, File executable) throws IOException {
        try(FileOutputStream fileOutputStream = new FileOutputStream(executable)) {
            IOUtils.copy(inputStream, fileOutputStream);
        }
    }

    private InputStream fetchExecutableResource() {
        final OperatingSystem operatingSystem = fetchOS();
        switch (operatingSystem) {
            case LINUX:
                return getClass().getResourceAsStream("/io/dactilo/sumbawa/spring/webdocs/pdf/linux/wkhtmltopdf");
            case MAC:
                return getClass().getResourceAsStream("/io/dactilo/sumbawa/spring/webdocs/pdf/osx/wkhtmltopdf");
        }

        throw new IllegalArgumentException("Unsupported OS");
    }
}
