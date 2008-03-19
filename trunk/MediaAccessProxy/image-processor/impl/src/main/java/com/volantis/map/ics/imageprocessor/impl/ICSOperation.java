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

import com.volantis.map.common.param.MissingParameterException;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.ics.configuration.Configuration;
import com.volantis.map.ics.configuration.ConfigurationParserFactory;
import com.volantis.map.ics.imageprocessor.ImageProcessor;
import com.volantis.map.ics.imageprocessor.ImageProcessorException;
import com.volantis.map.ics.imageprocessor.ImageProcessorFactory;
import com.volantis.map.ics.imageprocessor.ImageProcessorFactoryException;
import com.volantis.map.ics.imageprocessor.parameters.ICSParamBuilder;
import com.volantis.map.ics.imageprocessor.parameters.ParameterBuilderException;
import com.volantis.map.ics.imageprocessor.parameters.impl.ImageLoader;
import com.volantis.map.ics.imageprocessor.parameters.impl.PreservationParamsBuilder;
import com.volantis.map.ics.imageprocessor.tool.ToolFactory;
import com.volantis.map.ics.imageprocessor.utilities.NoFlushImageOutputStream;
import com.volantis.map.ics.imageprocessor.writer.ImageWriterFactory;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.Operation;
import com.volantis.map.operation.ResourceDescriptor;
import com.volantis.map.operation.Result;
import com.volantis.map.retriever.Representation;
import com.volantis.map.retriever.ResourceRetriever;
import com.volantis.map.retriever.ResourceRetrieverException;
import com.volantis.synergetics.osgi.boot.BootConstants;
import com.volantis.shared.net.url.http.CachedHttpContentState;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.path.Path;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.ComponentContext;

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
     * The location of the main ICS configuration file - should be specified
     * either as a resource ID or as an absolute path
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
    private Object lock = new Object();

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
                          HttpServletResponse response) {

        Result result = Result.UNSUPPORTED;
        ImageInputStream inputStream = null;
        ImageInputStream watermarkStream = null;
        ImageOutputStream outputData = null;
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
                icsBuilder.overlayConfigutionParameters(
                        config, descriptor.getInputParameters(), context);

                // @todo perform check for supported operation type here.
                ImageProcessor processor =
                    ImageProcessorFactory.getInstance().
                        createImageProcessor(ToolFactory.getInstance(),
                                             ImageWriterFactory.getInstance());

                Representation representation =
                    ImageLoader.load(descriptor.getInputParameters().getParameterValue(ParameterNames.SOURCE_URL),
                            retriever, request, descriptor);
                inputStream = representation.getSeekableInputStream();
                new PreservationParamsBuilder().process(request, descriptor);

                // now get the input stream for the watermark image.
                // @todo later this could probably be cached.
                if (descriptor.getInputParameters().containsName(
                    ParameterNames.WATERMARK_URL)) {
                    Representation watermarkRepresentation =
                        ImageLoader.load(descriptor.getInputParameters().getParameterValue(ParameterNames.WATERMARK_URL), 
                                retriever, request, descriptor);
                    watermarkStream =
                        watermarkRepresentation.getSeekableInputStream();
                    descriptor.getInputParameters().setObject(
                        ParameterNames.WATERMARK_INPUT_STREAM, watermarkStream);
                }

                outputData = ImageIO.createImageOutputStream(
                    response.getOutputStream());
                NoFlushImageOutputStream noFlush =
                        new NoFlushImageOutputStream(outputData);

                processor.process(inputStream,
                                  noFlush,
                                  descriptor.getInputParameters());

                // Set the expiry header for the cache
                setCacheData(response, representation.getCacheInfo());

                // Set the content type header
                response.setContentType(
                        descriptor.getInputParameters().getParameterValue(
                                ParameterNames.OUTPUT_IMAGE_MIME_TYPE));
                response.setContentLength((int)
                    (outputData.getStreamPosition() -
                     outputData.getFlushedPosition()));

                // Set the last modified header
                response.setDateHeader("Last-Modified",
                                       System.currentTimeMillis());

                return Result.SUCCESS;
            } catch (MissingParameterException e) {
                LOGGER.error("processing-error", e);
                throw new RuntimeException(e);
            }catch (ResourceRetrieverException e) {
                // could not find the source image
                sendNotFound(descriptor, request, response);
            } catch (ParameterBuilderException e) {
                LOGGER.error("processing-error", e);
                throw new RuntimeException(e);
            } catch (ImageProcessorException e) {
                // Catch, log and rethrow. The container will send an error for us.
                LOGGER.error("processing-error", e);
                throw new RuntimeException(e);
            } catch (IOException e) {
                LOGGER.error("processing-error", e);
                throw new RuntimeException(e);
            } catch (ImageProcessorFactoryException e) {
                // Catch, log and rethrow. The container will send an error for us.
                LOGGER.error("processing-error", e);
                throw new RuntimeException(e);
            }  catch (RuntimeException e) {
                // Catch, log and rethrow. The container will send an error for us.
                LOGGER.error("processing-error", e);
                throw e;
            } catch (Error e) {
                // Catch, log and rethrow. The container will send an error for us.
                LOGGER.error("processing-error", e);
                throw e;
            } finally {
                // make sure we close these streams as they could be holding
                // files open
                try {
                    if (null != outputData) {
                        // yes we really do need to flush it AND close it.
                        outputData.flush();
                        outputData.close();
                    }
                    if (null != inputStream) {
                        inputStream.flush();
                        inputStream.close();
                    }
                    if (null != watermarkStream) {
                        watermarkStream.flush();
                        watermarkStream.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException("processing-error", e);
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

    }

    /**
     * Activate this component
     *
     * @param context the component context
     */
    protected void activate(ComponentContext context) {
        this.context = context;
        try {
            this.config = createConfiguration();
        } catch(ParameterBuilderException e) {
            // @ todo
            //throw new IllegalStateException(e);
        }
    }

    /**
     * Deactivate this component
     *
     * @param context the Component context
     */
    protected void deactivate(ComponentContext context) {
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
     * Return a copy of the specified configuraiton
     *
     * @return
     *
     * @throws ParameterBuilderException
     */
    private Configuration createConfiguration() throws ParameterBuilderException {
        //Checking for parameter "reload" was added because it is necessary
        //to reload configuration during tests with HTTPUnit which doesn't
        //reinstantiate servlet object between tests and doesn't support
        //adding attributes into request. See WatermarkingTestCase.
        Configuration config = null;
        InputStream is = getInputStream(CONFIG_FILE_LOCATION);
        if (is != null) {
            try {
                config = ConfigurationParserFactory.getInstance()
                            .createConfigurationParser().unmarshal(is);
            } catch (Exception e) {
                LOGGER.error("config-loading-error");
                throw new ParameterBuilderException(e);
            } finally {
                if (null != is) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        LOGGER.error("config-loading-error", e);
                    }
                }
            }
        } else {
            LOGGER.error("config-name-error");
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
     * file path. If this fails, attempt to load as a resource. If this fails,
     * it will then return null.
     *
     * @param location either a resource id or file path
     * @return an InputStream for the configuration file, null if the location
     *         could not be resolved to an InputStream
     */
    private InputStream getInputStream(String location) {

        // Attempting to load as a file path first as the installer used
        // writes an absolute path for the value of the init parameter:
        // config.file.  If we attempt to look up location
        // as a resource first then a warning would be written to log
        // indicating that the configuration file could not be found as
        // a resource. It is not acceptable for such a warning to be issued
        // when the product is freshly installed as it may appear that
        // something has gone wrong during the install even though the config
        // file will be located as a file.

        // Attempt to load as an absolute file path
        File f = new File(location);
        InputStream is = getFileInputStream(f);

        if (is == null && context != null) {
            // location is not an absolute file path, try and obtain
            // as a resource.
            LOGGER.info("location-not-file-path", location);
            String configArea = context.getBundleContext().
                getProperty(BootConstants.CONTEXT_AREA);
            Path configPath = Path.parse(configArea);
            configPath = configPath.resolve(location);
            File config = new File(configPath.asPlatformSpecificString());

            try {
                is = new FileInputStream(config);
            } catch (FileNotFoundException e) {
                LOGGER.error("config-loading-error", config);
            }
        }
        return is;
    }

    /**
     * Utility method which returns a FileInputStream for the supplied file,
     * assuming that the file exists and is readable. Returns null otherwise.
     * Avoids code duplication.
     *
     * @param f the file for which to get a FileInputStream
     * @return a FileInputStream or null
     */
    private FileInputStream getFileInputStream(File f) {
        FileInputStream fis = null;
        if (f != null && f.exists() && f.canRead()) {
            try {
                fis = new FileInputStream(f);
            } catch (FileNotFoundException e) {
                LOGGER.info("location-not-file-path", f.getPath());
            }
        }
        return fis;
    }
 
    /**
     * unsets the resource retriever
     *
     * @param retriever
     */
    protected void unsetResourceRetriever(ResourceRetriever retriever) {
        this.retriever = null;
    }

    /**
     * Used to log and send a 404 to the client
     * @param request
     * @param response
     * @throws IOException
     */
    private static void sendNotFound(ResourceDescriptor descriptor, HttpServletRequest request,
                              HttpServletResponse response) {

        LOGGER.info("resource-definition-not-found", request.getRequestURL());
        try {
            String url =
                descriptor.getInputParameters().getParameterValue(
                    ParameterNames.SOURCE_URL);
            response.sendError(404, EXCEPTION_LOCALIZER.format(
                "resource-definition-not-found", url));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MissingParameterException e) {
            throw new RuntimeException(e);
        }
    }
}
