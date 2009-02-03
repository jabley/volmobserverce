/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generator;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaSource;
import com.volantis.testtools.mock.generator.model.MockableClass;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;
import java.net.URL;

public class MockObjectPlugin {

    private GeneratorLogger generatorLogger;

    private String outputEncoding;
    private UpToDateChecker pluginChecker;
    private File outputDir;
    private QDox129 qdox129;

    public MockObjectPlugin(
            String outputEncoding,
            File outputDir, UpToDateChecker upToDateChecker,
            ClassLoader dependencyClassLoader,
            GeneratorLogger generatorLogger) {

        this.outputEncoding = outputEncoding;
        this.outputDir = outputDir;
        this.pluginChecker = upToDateChecker;
        this.generatorLogger = generatorLogger;
        qdox129 = new QDox129(dependencyClassLoader);
    }

    public void start(JavaClass[] classes) {
        ClassRenamer renamer = new ClassRenamer();

        long start;
        long end;

        start = System.currentTimeMillis();
        generateMocks(classes, renamer);
        end = System.currentTimeMillis();
        generatorLogger.info("Code took " + (end - start) + "ms");
    }

    private void generateMocks(JavaClass[] metadata, ClassRenamer renamer) {
        List<JavaClass> libraries = new ArrayList<JavaClass>();
        List<JavaClass> sources = new ArrayList<JavaClass>();
        for (JavaClass meta : metadata) {
            DocletTag tag = meta.getTagByName("mock.generate");
            if (tag != null) {
                String value = tag.getNamedParameter("library");
                if ("true".equalsIgnoreCase(value)) {
                    libraries.add(meta);
                } else {
                    sources.add(meta);
                }
            }
        }

        boolean generatedAny;

        if (!sources.isEmpty()) {
            generatorLogger.info("");
            generatorLogger.info("  Generating Mocks");

            generatedAny = false;
            for (JavaClass source : sources) {

                UpToDateChecker sourceChecker = new UpToDateChecker(
                        getLastModified(source));

                DocletTag tag = source.getTagByName("mock.generate");
                generatedAny |= generateMock(source, tag, renamer,
                        sourceChecker);
            }

            if (!generatedAny) {
                generatorLogger.info("    Nothing to do");
            }
        }


        for (JavaClass library : libraries) {

            UpToDateChecker sourceChecker = new UpToDateChecker(
                    getLastModified(library));
            generatedAny = false;

            generatorLogger.info("");
            generatorLogger.info("  Generating Mock Library (" +
                    library.getFullyQualifiedName() + ")");

            // It is a library so iterate over the fields.
            JavaField[] javaFields = library.getFields();
            for (JavaField javaField : javaFields) {
                DocletTag fieldTag =
                        javaField.getTagByName("mock.generate");
                generatorLogger.debug("Generating code for field " +
                        javaField.getName() + " with type " +
                        javaField.getType().getValue());
                JavaClass javaClass =
                        javaField.getType().getJavaClass();
                generatedAny |= generateMock(javaClass, fieldTag, renamer,
                        sourceChecker);
            }

            if (!generatedAny) {
                generatorLogger.info("    Nothing to do");
            }
        }
    }

    private long getLastModified(JavaClass javaClass) {
        JavaSource source = javaClass.getSource();
        URL url = source.getURL();
        File file = new File(url.getFile());
        return file.lastModified();
    }

    private boolean generateMock(
            JavaClass javaClass, DocletTag tag, ClassRenamer renamer,
            UpToDateChecker sourceChecker) {
        try {

            JavaSource javaSource = javaClass.getParentSource();
            if (javaSource.getURL() == null) {
                // Assume that this was created from a binary source and so
                // we will need to recreate it to work around
                // http://jira.codehaus.org/browse/QDOX-129
                javaClass = qdox129.recreateBinary(javaClass);
            }

            String qualifiedClassName = javaClass.getFullyQualifiedName();
            String dstClassName = renamer.rename(qualifiedClassName);
            File file = new File(outputDir,
                    dstClassName.replace('.', '/') + ".java");

            long destinationLastModified = file.lastModified();
            if (pluginChecker.isUpToDate(destinationLastModified) &&
                    sourceChecker.isUpToDate(destinationLastModified)) {
                generatorLogger.debug("    " + dstClassName + " up to date");
                return false;
            }

            generatorLogger
                    .info("    " + qualifiedClassName + " --> " + dstClassName);

            MockableClass mockableClass =
                new MockableClass(javaClass, tag, renamer, qdox129);
            File dir = file.getParentFile();
            dir.mkdirs();

            OutputStream os = new FileOutputStream(file);
            Writer writer = new OutputStreamWriter(os, outputEncoding);
            writer = new BufferedWriter(writer, 4096);
            Generator generator = new Generator(mockableClass, writer);
            generator.generate();
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException("Couldn't generate content", e);
        }

        return true;
    }
}
