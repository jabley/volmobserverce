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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.application;

import com.volantis.charset.EncodingManager;
import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.PrerendererPackageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.PrerendererApplicationContext;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.integration.PageURLRewriter;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.builder.NamedProtocolBuilder;
import com.volantis.mcs.protocols.builder.ProtocolRegistry;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.packagers.DefaultPackager;

/**
 * The ApplicationContextFactory for use with the Prerenderer application.
 */
public abstract class PrerendererApplicationContextFactory extends
        AbstractApplicationContextFactory {

    /**
     * An instance of shared encoding manager for use by created application
     * contexts.
     */
    private EncodingManager encodingManager;
    
    /**
     * An instance of shared protocol builder for use by created application
     * contexts.
     */
    private NamedProtocolBuilder protocolBuilder;

    /**
     * Initializes this factory.
     */
    public PrerendererApplicationContextFactory() {
        this.encodingManager = new EncodingManager();

        protocolBuilder = new NamedProtocolBuilder();
        
        ProtocolRegistry registry = new ProtocolRegistry();
        
        registry.register(protocolBuilder);
    }
    
    /**
     * @inheritDoc
     */
    public ApplicationContext createApplicationContext(
            MarinerRequestContext requestContext)
            throws RepositoryException {
        
        // Get the Volantis bean.
        Volantis volantisBean = Volantis.getInstance();
        if (volantisBean == null) {
            throw new IllegalStateException
                    ("Volantis bean has not been initialised");
        }

        // Resolve the device (once for this session).
        InternalDevice device = resolveDevice(
                volantisBean,
                requestContext);

        // Create the application context
        ApplicationContext applicationContext = 
            new PrerendererApplicationContext(requestContext);

        // Initialize the application context.
        initializeApplicationContext(requestContext, volantisBean,
                applicationContext, device);

        return applicationContext;
    }

    /**
     * Provide a template mechanism for subclasses to override the default
     * initialization of this application context. This should typically be
     * called after the application context has been created.
     *
     * @param volantisBean       the volantis bean.
     * @param applicationContext the newly created application context.
     * @param device             the device associated with this request.
     */
    protected void initializeApplicationContext(
            MarinerRequestContext requestContext,
            Volantis volantisBean,
            ApplicationContext applicationContext,
            InternalDevice device) {

        // The protocol policy value may be an ordered list. Here we select the
        // first protocol from the list that we know of. This allows us to
        // ignore any protocols which may added to later device repositories
        // but are not known in this version of the product.
        String protocolName = device.selectSingleKnownPolicyValue("protocol",
                protocolBuilder.getProtocolNames());
        if (protocolName == null) {
            throw new IllegalStateException("Device protocol policy missing");
        }

        // Create a configured protocol.
        VolantisProtocol protocol = protocolBuilder.build(protocolName, device);

        // Complete initialization of the application context
        applicationContext.setDevice(device);
        applicationContext.setProtocol(protocol);
        applicationContext.setPackager(new DefaultPackager());
        applicationContext.setDissectionSupported(false);
        applicationContext.setFragmentationSupported(false);
        applicationContext.setCanvasTagSupported(false);
        applicationContext.setWMLCSupported(false);
        applicationContext.setEncodingManager(encodingManager);
        applicationContext.setPageURLRewriter(getPageURLRewriter(requestContext));
        applicationContext.setAssetURLRewriter(volantisBean.getAssetURLRewriter());
    }

    /**
     * Returns current prerenderer package context.
     * 
     * @param requestContext 
     * @return
     */
    protected abstract PrerendererPackageContext getPrerendererPackageContext(MarinerRequestContext requestContext);
    
    /**
     * Creates new instance of PageURLRewriter for use within created
     * application context.
     * 
     * @param requestContext
     * @return
     */
    private PageURLRewriter getPageURLRewriter(MarinerRequestContext requestContext) {
        return getPrerendererPackageContext(requestContext).getPageURLRewriter();
    }
}
