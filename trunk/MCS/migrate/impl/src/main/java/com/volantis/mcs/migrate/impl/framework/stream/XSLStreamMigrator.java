/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.migrate.impl.framework.stream;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.api.framework.StreamMigrator;
import com.volantis.mcs.migrate.api.framework.StepType;
import com.volantis.mcs.migrate.api.notification.Notification;
import com.volantis.mcs.migrate.notification.NotificationFactory;
import com.volantis.mcs.migrate.api.notification.NotificationReporter;
import com.volantis.mcs.migrate.api.notification.NotificationType;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.MCSTransformerMetaFactory;
import com.volantis.shared.content.BinaryContentInput;
import com.volantis.shared.content.ContentInput;
import com.volantis.shared.io.CachingOutputStream;
import com.volantis.shared.io.CachingInputStream;
import com.volantis.xml.sax.VolantisXMLReaderFactory;
import com.volantis.xml.schema.SchemaDefinition;
import com.volantis.xml.schema.validator.SchemaValidator;
import com.volantis.synergetics.log.LogDispatcher;

import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URL;

/**
 * A stream migrator that upgrades an XML resource by applying an XSL
 * transform.
 */
public class XSLStreamMigrator implements StreamMigrator {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(XSLStreamMigrator.class);

    NotificationFactory notificationFactory =
        NotificationFactory.getDefaultInstance();

    /**
     * The meta factory to use for creating JAXP XSL transformer factories.
     *
     * <p>Since we are a CLI tool we always use the repackaged MCS version of
     * Xalan which we ship rather than relying on the standard JAXP dynamic
     * lookup process.</p>
     *
     * <p>If in future this code is to be run from the GUI, a configurable
     * meta-factory may be required.</p>
     */
    private static MCSTransformerMetaFactory transformerMetaFactory =
            new MCSTransformerMetaFactory();

    private VolantisXMLReaderFactory inputReaderFactory;

    private VolantisXMLReaderFactory xslReaderFactory;

    /**
     * The resource path to the XSL to be used for the migration step.
     */
    private String xslResourcePath;

    private NotificationReporter reporter;

    private boolean inputError;

    private boolean strictMode;

    /**
     * An Entity Resolver to be used in conjunction with a migration's input.
     * <p>
     * This is an optional element of an XSL-based migration step.
     */
    private EntityResolver entityResolver;

    private SchemaValidator outputSchemaValidator = new SchemaValidator();

    /**
     * The preprocessed form of the XSLT.
     */
    private final Templates templates;

    /**
     * Constructs a new XSL step that migrates from one version to another by
     * applying an XSL transformation.
     *
     * @param xslResourcePath The resource path to the transform to use
     * @param reporter
     */
    public XSLStreamMigrator(String xslResourcePath,
            NotificationReporter reporter)
            throws ResourceMigrationException {

        this.xslResourcePath = xslResourcePath;
        this.reporter = reporter;
        inputReaderFactory = new VolantisXMLReaderFactory();
        xslReaderFactory = new VolantisXMLReaderFactory();

        TransformerFactory transformerFactory =
                transformerMetaFactory.createTransformerFactory();
        // Would like to be able to use the transformer factory that compiles
        // the templates into Java Byte Codes as it is more efficient. However,
        // at the moment it behaves too differently to the non compiling one to
        // allow it to be used.
        //        transformerMetaFactory.createXsltcTransformerFactory();

        URL xslURL = getClass().getResource(xslResourcePath);
        if (xslURL == null) {
            throw new ResourceMigrationException(
                    "Could not find " + xslResourcePath);
        }

        Source source = new StreamSource(xslURL.toExternalForm());
        try {
            templates = transformerFactory.newTemplates(source);
        } catch (TransformerConfigurationException e) {
            throw new ResourceMigrationException(
                    "Cannot create XSLT transformer", e);
        }

        // Default to Strict mode if not set.
        this.strictMode = true;
    }

    /**
     * Carry out migration by transforming the input stream to the output
     * stream using the XSL specified when the step was instantiated.
     * <p>
     * The XSL transformation will use a (SAX) EntityResolver
     * <strong>if</strong> one was
     * specified when creating the XSLStreamMigrator.
     * </p>
     * @see StreamMigrator#migrate
     * @param input The input to transform
     * @param output The output to transform to
     * @param stepType of the step invoking this method
     */
    public void migrate(InputStream input, OutputStream output,
        StepType stepType)
        throws ResourceMigrationException {

        if (stepType.isFirst()) {
            if (logger.isDebugEnabled()) {
                // dump the input to the first step for debugging
                logger.debug("Input to migrator");
                CachingInputStream is = new CachingInputStream(input);
                dumpStream(is);
                input = is.getCacheInputStream();
            }
        } else {
            // disable input validation on all steps except the first
            inputReaderFactory.disableValidation();
        }

        // We don't currently have the XSL schema available locally so we
        // must disable validation on the XSL for now.
        xslReaderFactory.disableValidation();

        inputError = false;

        // Input validation failures should be informational only (if not in strictMode), as the user
        // is allowed to have dodgy old data.
        // Output validation should cause the migration to fail if the
        // input was valid to ensure we are not trashing the input data.
        //
        // Input validation will only be perfromed if this is the first step
        // in a sequence. Output validation will only be done on the last
        // step.
        try {
            // NOTE: we are *not* allowed to use StreamSource as that will
            // potentially use the customers XML parser rather than our
            // "volantisized" XML parser.

            // Create a source for the document to be transformed.
            Source inputSource = null;
            XMLReader inputReader = inputReaderFactory.create();
            if (getEntityResolver() != null) {
                inputReader.setEntityResolver(getEntityResolver());
            }
            inputReader.setErrorHandler(new InputErrorHandler());
            inputSource = new SAXSource(inputReader, new InputSource(input));

            // Create the result.
            final OutputStream actualOutput;
            if (stepType.isLast() || logger.isDebugEnabled()) {
                // Caching a copy of the marshalled data for validation or dumping
                actualOutput = new CachingOutputStream(output);
            } else {
                actualOutput = output;
            }
            Result outputResult = new StreamResult(actualOutput);

            // Do the transform.
            Transformer transform = templates.newTransformer();
            transform.setErrorListener(new LoggingListener());
            transform.transform(inputSource, outputResult);

            // Dump the output of this migrator for debugging
            if (logger.isDebugEnabled()) {
                logger.debug("Output from migrator");

                CachingOutputStream cachedOutput =
                        ((CachingOutputStream) actualOutput);
                InputStream is = cachedOutput.getCacheInputStream();
                dumpStream(is);
            }

            if (stepType.isLast()) {
                // Do validation of the output.

                CachingOutputStream cachedOutput =
                        ((CachingOutputStream) actualOutput);
                InputStream reader = cachedOutput.getCacheInputStream();
                ContentInput content = new BinaryContentInput(reader);

                try {
                    outputSchemaValidator.validate(content);
                } catch (SAXException e) {
                    if (inputError) {
                        // Validation errors are informational since we are not
                        // sure if they are a result of dodgy input or not.
                        Notification notification =
                            notificationFactory.createLocalizedNotification(
                                    NotificationType.ERROR,
                                    "unexpected-exception", e);
                        reporter.reportNotification(notification);
                    } else {
                        // Validation errors are fatal since we introduced them.
                        throw new ResourceMigrationException(e);
                    }
                } catch (IOException e) {
                    // Should never get an IO error since we are reading from memory
                    throw new ResourceMigrationException(e);
                }
            }

        } catch (TransformerConfigurationException tce) {
            throw new ResourceMigrationException(
                "Cannot create XSLT transformer", tce);
        } catch (TransformerException te) {
            throw new ResourceMigrationException("XSLT Transformation failed", te);
        } catch (SAXException e) {
            throw new ResourceMigrationException(e);
        }
    }

    // Javadoc inherited
    public String toString() {
        return "[XSL Stream Migrator - " + getXslResourcePath() + "]";
    }

    /**
     * Return path to XSL stylesheet
     * <p>
     * </p>
     *
     * @return Path to the transformation stylesheet.
     */
    public String getXslResourcePath() {
        return xslResourcePath;
    }

    /**
     * Return an associated Entity Resolver
     * <p>
     * </p>
     *
     * @return The entity resolver, or null if not specified on object-creation.
     */
    public EntityResolver getEntityResolver() {
        return entityResolver;
    }

    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    public void addOutputSchema(SchemaDefinition schema) {
        // Add schema for output validation.
        this.outputSchemaValidator.addSchema(schema);
    }

    public void addInputSchema(SchemaDefinition schema) {
        this.inputReaderFactory.addSchema(schema);
    }

    public void setStrictMode(boolean strictMode) {
            this.strictMode = strictMode;
    }

    /**
     * Helper method to dump the contents of an input stream for debugging.
     * We assume that the contents is encoded in UTF-8.
     *
     * @param reader to read from.
     */
    private void dumpStream(InputStream reader) {
        try {
            InputStreamReader isr =
                new InputStreamReader(reader, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            if (logger.isDebugEnabled()) {
                while (br.ready()) {
                    logger.debug(br.readLine());
                }
            }
        } catch (IOException e) {
            // This should never happen
            logger.error(e);
        }

    }

    private class InputErrorHandler implements ErrorHandler {
        public void warning(SAXParseException exception) {
            reportError("warning", exception);
        }

        public void error(SAXParseException exception)
            throws SAXException {
            reportError("error", exception);
            if(strictMode)
                throw exception;
        }

        public void fatalError(SAXParseException exception)
            throws SAXException {

            reportError("fatal", exception);
            throw exception;
        }

        private void reportError(String type, SAXParseException exception) {
            inputError = true;
            String systemId = exception.getSystemId();

            String message;
            if (systemId == null) {
                message = exception.getLocalizedMessage();
            } else {
                message = systemId + ":" +
                    exception.getLineNumber() + ":" +
                    exception.getColumnNumber() + ":" +
                    exception.getLocalizedMessage();
            }
            reporter.reportNotification(
                notificationFactory.createLocalizedNotification(
                        NotificationType.ERROR, "xml-parser-" + type, message));
        }

    }

    /**
     * Private class used to capture warning and error messages during
     * transformation.
     */
    private static class LoggingListener implements ErrorListener {

        /**
         * Called by the parser when a non-serious error is encountered in
         * the XML file.
         *
         * @param exception A SAXParseException describing the problem
         */
        public void warning(TransformerException exception) {
            logger.warn(exception);
        }

        /**
         * Called by the parser when a serious error is encountered in the
         * XML file.
         *
         * @param exception A SAXParseException describing the problem
         */
        public void error(TransformerException exception) {
            logger.error(exception);
        }

        /**
         * Called by the parser when a fatal error is encountered in the
         * XML file.
         *
         * @param exception A SAXParseException describing the problem
         */
        public void fatalError(TransformerException exception)
            throws TransformerException {

            logger.fatal(exception);

            throw exception;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 18-May-05	8181/6	adrianj	VBM:2005050505 XDIME/CP Migration CLI

 18-May-05	8036/12	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/10	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 13-May-05	8181/1	adrianj	VBM:2005050505 XSL for theme migration

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
