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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.ics.imageprocessor.impl;

import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.common.streams.NoCloseSeekableInputStream;
import com.volantis.map.common.streams.NoFlushSeekableOutputStream;
import com.volantis.map.ics.configuration.Configuration;
import com.volantis.map.ics.configuration.ConfigurationParserFactory;
import com.volantis.map.ics.imageprocessor.ImageProcessor;
import com.volantis.map.ics.imageprocessor.ImageProcessorFactory;
import com.volantis.map.ics.imageprocessor.parameters.ICSParamBuilder;
import com.volantis.map.common.param.ParameterBuilderException;
import com.volantis.map.common.param.MutableParameters;
import com.volantis.map.common.CommonFactory;
import com.volantis.map.ics.imageprocessor.parameters.impl.ImageLoader;
import com.volantis.map.ics.imageprocessor.parameters.impl.PreservationParamsBuilder;
import com.volantis.map.ics.imageprocessor.tool.ToolFactory;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterFactory;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.Operation;
import com.volantis.map.operation.ResourceDescriptor;
import com.volantis.map.operation.Result;
import com.volantis.map.retriever.Representation;
import com.volantis.map.retriever.ResourceRetriever;
import com.volantis.shared.net.url.http.CachedHttpContentState;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.osgi.boot.BootConstants;
import com.volantis.synergetics.path.Path;
import org.osgi.service.component.ComponentContext;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * An Operation that encapsualtes an image transcoding service.
 */
public class ICSOperation implements Operation {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(ICSOperation.class);

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(ICSOperation.class);


    /**
     * Cache control header name
     */
    private static final String CACHE_CONTROL_HEADER = "Cache-Control";

    /**
     * Pragma header name
     */
    private static final String PRAGMA_HEADER = "pragma";

    /**
     * Reference to the ICS configuration
     */
    private Configuration config;

    /**
     * Parameters describing local runtime environment 
     */
    private MutableParameters envParams;

    /**
     * The location of the main ICS configuration file
     */
    public static final String CONFIG_FILE_LOCATION = "WEB-INF/ics-config.xml";

    /**
     * The OSGI component context
     */
    private ComponentContext context;

    /**
     * The resource retriever to use.
     */
    private ResourceRetriever retriever;

    /**
     * Used as a mutex
     */
    private final Object lock = new Object();

    /**
     * This is needed as the ImageIO plugins may not have been loaded when this
     * operation is started.
     */
    static {
        // this is a horrible hack to supress the
        // "Error: Could not find mediaLib accelerator wrapper classes. Continuing in pure Java mode."
        // message
        System.setProperty("com.sun.media.jai.disableMediaLib", "true");
        ImageIO.scanForPlugins();
    }

    // Javadoc inherited
    public Result execute(ResourceDescriptor descriptor,
                          HttpServletRequest request,
                          HttpServletResponse response) throws Exception {

        Result result = Result.UNSUPPORTED;
        NoCloseSeekableInputStream inputStream = null;
        NoCloseSeekableInputStream watermarkStream = null;
        NoFlushSeekableOutputStream outputData = null;
        final String resourceType = descriptor.getResourceType();
        if ("image".equals(resourceType) || resourceType.startsWith("image.")) {

            try {
                synchronized(lock) {
                    if (config == null) {
                        config = createConfiguration();
                    }
                }
                // merge the parameters in the ICS request with those in the
                // configuration file.
                ICSParamBuilder icsBuilder = new ICSParamBuilder();
                icsBuilder.overlayConfigurationParameters(
                        descriptor.getInputParameters(), config);
                icsBuilder.overlayEnvironmentParameters(
                        descriptor.getInputParameters(), envParams);

                // @todo perform check for supported operation type here.
                ImageProcessor processor =
                    ImageProcessorFactory.getInstance().
                        createImageProcessor(ToolFactory.getInstance(),
                                             ImageWriterFactory.getInstance());

                Representation representation =
                    ImageLoader.load(descriptor.getInputParameters().getParameterValue(ParameterNames.SOURCE_URL),
                            retriever, request, descriptor);
                inputStream = new NoCloseSeekableInputStream(representation.getSeekableInputStream());
                new PreservationParamsBuilder().process(request, descriptor);

                // now get the input stream for the watermark image.
                // @todo later this could probably be cached.
                if (descriptor.getInputParameters().containsName(
                    ParameterNames.WATERMARK_URL)) {
                    Representation watermarkRepresentation =
                        ImageLoader.load(descriptor.getInputParameters().getParameterValue(ParameterNames.WATERMARK_URL), 
                                retriever, request, descriptor);
                    watermarkStream =
                        new NoCloseSeekableInputStream(watermarkRepresentation.getSeekableInputStream());
                    descriptor.getInputParameters().setObject(
                        ParameterNames.WATERMARK_INPUT_STREAM, watermarkStream);
                }

                outputData =
                    new NoFlushSeekableOutputStream(response.getOutputStream());

                processor.process(new NoCloseSeekableInputStream(inputStream),
                                  outputData,
                                  descriptor.getInputParameters());

                // Set the expiry header for the cache
                setCacheData(response, representation.getCacheInfo());

                // Set the content type header
                response.setContentType(
                        descriptor.getInputParameters().getParameterValue(
                                ParameterNames.OUTPUT_IMAGE_MIME_TYPE));
                response.setContentLength((int)outputData.getStreamPosition());


                // Now we may safely flush and close the output
                // yes we really do need to flush it AND close it.
                outputData.flushDelegate();
                outputData.closeDelegate();

                return Result.SUCCESS;
            } finally {
                // make sure we close these streams as they could be holding
                // files open

                // Output stream (response) should be flushed only in case of success.
                // In case of exception, we allow the container to handle response,
                // therefore we must not flush nor close the output stream here.

                try {
                    if (null != inputStream) {

                        inputStream.flush();
                        inputStream.closeDelegate();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(
                            EXCEPTION_LOCALIZER.format("processing-error", descriptor.getExternalID()), e);
                }
                try {
                    if (null != watermarkStream) {
                        watermarkStream.flush();
                        watermarkStream.closeDelegate();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(
                            EXCEPTION_LOCALIZER.format("processing-error", descriptor.getExternalID()), e);
                }
            }
        }

        return result;
    }

    /**
     * Set any headers required by the squid cache. This is known to be Expiry:
     * which sets the expiry date and time. We set the cache to expire 24 hours
     * after the image is loaded.
     */
    private void setCacheData(HttpServletResponse response,
                              CachedHttpContentState cacheState) {

        if (null != cacheState.getAge()) {
            response.addIntHeader("Age", (int) cacheState.getAge().inSecondsTreatIndefinitelyAsZero());
        }

        if (null != cacheState.getExpires()) {
            response.addDateHeader("Expires", cacheState.getExpires().inMillis());
        }

        if (null != cacheState.getCcMaxAge()) {
            response.addHeader(CACHE_CONTROL_HEADER, "max-age=" +
                cacheState.getCcMaxAge().inSecondsTreatIndefinitelyAsZero());
        }

        if (null != cacheState.getCcSMaxAge()) {
            response.addHeader(CACHE_CONTROL_HEADER, "s-maxage=" +
                cacheState.getCcSMaxAge().inSecondsTreatIndefinitelyAsZero());
        }

        if (cacheState.isCcNoCache()) {
            response.addHeader(CACHE_CONTROL_HEADER, "no-cache");
        }
        if (cacheState.isCcNoStore()) {
            response.addHeader(CACHE_CONTROL_HEADER, "no-store");
        }
        if (cacheState.isCcPrivate()) {
            response.addHeader(CACHE_CONTROL_HEADER, "private");
        }
        if (cacheState.isCcPublic()) {
            response.addHeader(CACHE_CONTROL_HEADER, "public");
        }
        if (cacheState.isPragmaNoCache()) {
            response.addHeader(PRAGMA_HEADER, "no-cache");
        }
        if (cacheState.getLastModified() != null) {
            // Set the last modified header
            response.addDateHeader("Last-Modified",
                cacheState.getLastModified().inMillis());
        }


    }

    /**
     * Activate this component
     *
     * @param context the component context
     */
    public void activate(ComponentContext context)
            throws Exception {
        try {
            this.context = context;
            this.config = createConfiguration();
            this.envParams = createEnvParams();
        } catch (Exception x) {
            // We use catch-log-rethrow pattern purposefully here, because
            // exceptions from this method are caught by OSGi container and
            // would not be logged otherwise.
            LOGGER.error(x);
            throw x;
        }
    }

    /**
     * Deactivate this component
     *
     * @param context the Component context
     */
    public void deactivate(ComponentContext context) {
        this.context = null;
    }

    /**
     * Sets the resource retriever to use.
     * @param retriever
     */
    protected void setResourceRetriever(ResourceRetriever retriever) {
            this.retriever = retriever;
    }

    /**
     * Load the configuraiton
     *
     * @throws ParameterBuilderException
     */
    private Configuration createConfiguration() throws ParameterBuilderException {

        Configuration config = null;
        InputStream is = getInputStream(CONFIG_FILE_LOCATION);
        if (is != null) {
            try {
                config = ConfigurationParserFactory.getInstance()
                      .createConfigurationParser().unmarshal(is);

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("config-loaded");
                }

            } catch (Throwable e) {
                LOGGER.error("config-loading-error");
                throw new ParameterBuilderException(e);
            } finally {
                // is != null because we are inside the if
                try {
                    is.close();
                } catch (IOException e) { /* ignore */ }
            }
        } else {
            LOGGER.error("config-name-error", CONFIG_FILE_LOCATION);
        }

         if (config == null) {
             LOGGER.warn("using-default-configuration");
             config = ConfigurationParserFactory.getInstance()
                     .createConfigurationParser().createDefaultConfiguration();
        }

        return config;
    }

    /**
     * Attempt to load the configuration file using the specified string as a
     * relative file path. 
     *
     * @param location either a resource id or file path
     * @return an InputStream for the configuration file, null if the location
     *         could not be resolved to an InputStream
     */
    private InputStream getInputStream(String location) {

        InputStream is = null;

        // If in OSGI container, load configuration using OSGi mechanism
        if (context != null) {
            String configArea = context.getBundleContext().
                getProperty(BootConstants.CONTEXT_AREA);
            Path configPath = Path.parse(configArea);
            configPath = configPath.resolve(location);
            File config = new File(configPath.asPlatformSpecificString());

            try {
                is = new FileInputStream(config);

                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("config-found", config.getAbsolutePath());
                }

            } catch (FileNotFoundException e) {
                LOGGER.error("config-name-error", config.getAbsolutePath());
            }
        }
        return is;
    }

    private MutableParameters createEnvParams() {
        MutableParameters params = CommonFactory.getInstance().createMutableParameters();

        String localRoot = context.getBundleContext().
                getProperty(BootConstants.CONTEXT_AREA);
        params.setParameterValue(ParameterNames.ENV_LOCAL_ROOT, localRoot);
        
        return params;
    }

    /**
     * unsets the resource retriever
     *
     * @param retriever
     */
    protected void unsetResourceRetriever(ResourceRetriever retriever) {
        this.retriever = null;
    }
}
