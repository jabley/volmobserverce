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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.map.sti.impl;

import com.volantis.map.operation.Operation;
import com.volantis.map.operation.ResourceDescriptor;
import com.volantis.map.operation.Result;
import com.volantis.map.operation.OperationNotFoundException;
import com.volantis.map.retriever.ResourceRetriever;
import com.volantis.map.sti.configuration.Configuration;
import com.volantis.map.sti.configuration.ConfigurationFactory;
import com.volantis.map.sti.impl.transcoder.DefaultTranscoderFactory;
import com.volantis.map.sti.model.TranscodingRequest;
import com.volantis.map.sti.transcoder.TranscoderFactory;
import com.volantis.map.common.param.ParamBuilder;
import com.volantis.map.common.param.MutableParameters;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.common.CommonFactory;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.osgi.boot.BootConstants;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.JiBXException;
import org.osgi.service.component.ComponentContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import java.net.URL;

/**
 * The transcoding operation performing request to the STI service. This
 * implementation is exposed as OSGi Service Component. This implementation is
 * thread-safe.
 */
public final class STIOperation implements Operation {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(STIOperation.class);

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(
            STIOperation.class);
    /**
     * The actual underlying STIOperation.
     */
    private Operation underlyingOperation;

    // These fields are there to support parameter-less constructor for OSGi.
    private final DocumentBuilderFactory documentBuilderFactory;
    private final TransformerFactory transformerFactory;
    private final IBindingFactory bindingFactory;

    private MutableParameters envParams;

    /**
     * the Resource retriever to use
     */
    private ResourceRetriever resourceRetriever;


    /**
     * Activate this component. Called by OSGi container.
     *
     * @param context the component context
     */
    protected void activate(ComponentContext context)
        throws Exception {

        try {
            // Retrieve configuration
            Configuration configuration = ConfigurationFactory.getInstance().createConfiguration(context.getProperties());
            URL serviceURL = configuration.getServiceURL() ;
            String originatorID = configuration.getOriginatorID();

            // Retrieve path to context area needed to resolve local resources
            BundleContext bundleCtx = context.getBundleContext();
            String localRoot = context.getBundleContext().
                    getProperty(BootConstants.CONTEXT_AREA);
            envParams = CommonFactory.getInstance().createMutableParameters();
            envParams.setParameterValue(ParameterNames.ENV_LOCAL_ROOT, localRoot);

            // In typical situation ResourceRetriever should have been set by Declarative Services add-on
            // but in case it didn't happen, we'll get it manually. See VMS-744
            if (null == resourceRetriever) {
                ServiceReference ref = bundleCtx.getServiceReference(ResourceRetriever.class.getName());
                if (null != ref) {
                    resourceRetriever =
                        (ResourceRetriever)bundleCtx.getService(ref);
                    if (null == resourceRetriever) {
                        Object[] params = new Object[] {
                            ref.getProperty("service.pid"),
                            ref.getBundle().getSymbolicName()
                        };
                        throw new IllegalStateException(
                            EXCEPTION_LOCALIZER.format("service-has-been-unregistered", params));
                    }
                } else {
                    throw new IllegalStateException(
                        EXCEPTION_LOCALIZER.format("missing-service", ResourceRetriever.class.getName()));
                }
            }

            // Create an instance of TranscoderFactory for use by the underlying STIOperation.
            TranscoderFactory transcoderFactory = new DefaultTranscoderFactory(
                    serviceURL,
                    documentBuilderFactory, transformerFactory, bindingFactory,
                    resourceRetriever,originatorID);

            // Create an instance of underlying STIOperation.
            this.underlyingOperation = new DefaultOperation(transcoderFactory);

        } catch (Exception x) {
            // We use catch-log-rethrow pattern purposefully here, because
            // exceptions from this method are caught by OSGi container and
            // would not be logged otherwise.
            LOGGER.warn(x);
            throw x;
        }
    }

    /**
     * Deactivate this component
     *
     * @param context the Component context
     */
    protected void deactivate(ComponentContext context) {
        underlyingOperation = null;
    }
      
    /**
     * Initializes this STIOperation with specified configuration.
     */
    public STIOperation() {
        // Create factories which will be used to create an instance of
        // underlying STIOperation when intance of ResourceRetriever is set by
        // OSGi.
        try {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            transformerFactory = TransformerFactory.newInstance();
            bindingFactory = BindingDirectory
                    .getFactory(TranscodingRequest.class);
        } catch (JiBXException e) {
            // Nothing we can do here.
            throw new RuntimeException("Error initializing STIOperation.", e);
        }
    }

    // Javadoc inherited
    public Result execute(ResourceDescriptor descriptor,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // Invoke the underlying Operation, if it's there.
        // Otherwise report that operation is not supported.
        if (underlyingOperation == null) {
            return Result.UNSUPPORTED;
        }

        ParamBuilder builder = new ParamBuilder();
        // We operate on mutable parameteres
        builder.overlayEnvironmentParameters(
                descriptor.getInputParameters(), envParams);
        return underlyingOperation.execute(descriptor, request, response);
    }

    /**
     * Sets ResourceRetriever service, which would retrieve resources to
     * transcode.
     * 
     * @param resourceRetriever the ResourceRetriever service.
     */
    protected void setResourceRetriever(ResourceRetriever resourceRetriever) {
        this.resourceRetriever = resourceRetriever;
    }
}
