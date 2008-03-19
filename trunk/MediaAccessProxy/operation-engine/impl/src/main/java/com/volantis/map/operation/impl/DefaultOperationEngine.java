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
package com.volantis.map.operation.impl;

import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.operation.Operation;
import com.volantis.map.operation.OperationEngine;
import com.volantis.map.operation.OperationNotFoundException;
import com.volantis.map.operation.ResourceDescriptorNotFoundException;
import com.volantis.map.operation.Result;
import com.volantis.synergetics.descriptorstore.ResourceDescriptor;
import com.volantis.synergetics.descriptorstore.ResourceDescriptorStore;
import com.volantis.synergetics.descriptorstore.ResourceDescriptorStoreException;
import com.volantis.synergetics.log.LogDispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;

/**
 * The default operation engine. This engine asks each of its plugins to
 * perform the operation. If a plugin can perform the operation it returns
 * true, if it cannot perform the operation it returns false. They throw
 * exceptions to indicate that a a fatal error occurred.
 *
 * Operations should not read from the servlet request or write to the servlet
 * response unless they can perform the operation.
 */
public class DefaultOperationEngine implements OperationEngine {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(DefaultOperationEngine.class);

    /**
     * The component context
     */
    private ComponentContext context;

    /**
     * The resource descriptor store
     */
    private ResourceDescriptorStore store;

    /**
     * Process the request
     *
     * @param externalID the external ID
     * @param request the servlet request
     * @param response the servlet response
     * @throws ResourceDescriptorNotFoundException
     * @throws OperationNotFoundException
     */
    public void processRequest(String externalID,
                          HttpServletRequest request,
                          HttpServletResponse response)
        throws ResourceDescriptorNotFoundException,
        OperationNotFoundException, Exception {

        com.volantis.map.operation.ResourceDescriptor localDescriptor = null;
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("obtaining descriptor for ID " + externalID);
            }
            ResourceDescriptor descriptor = store.getDescriptor(externalID);
            localDescriptor = new DelegatingResourceDescriptor(descriptor);
        } catch (ResourceDescriptorStoreException e) {
            throw new ResourceDescriptorNotFoundException(e);
        }

        try {

            ServiceReference[] references =
                context.getBundleContext().getServiceReferences(
                    Operation.class.getName(), null);
            if (null == references || references.length < 1) {
                throw new OperationNotFoundException(
                    "operation-type-not-found",
                    localDescriptor.getResourceType());
            } else {
                Result result = Result.UNSUPPORTED;
                for (int i=0; i< references.length &&
                    result == Result.UNSUPPORTED; i++) {

                    Object service =
                        context.getBundleContext().getService(references[i]);
                    if (null == service) {
                        LOGGER.error(
                            "service-has-been-unregistered", references[i]);
                    }
                    Operation operation =
                        (Operation) service;
                    result  = operation.execute(
                        localDescriptor, request, response);
                }
                if (result == Result.UNSUPPORTED) {
                    throw new OperationNotFoundException(
                        "no-plugin-available", null);
                }
            }

        } catch (InvalidSyntaxException e) {
            // this can never happen as we don't use a filter.
            throw new OperationNotFoundException(
                "syntax-error-in-filter", null, e);
        } catch (Exception e) {
            // all other exceptions thrown by the execute method
            LOGGER.error("error-during-plugin-processing", e);
            throw e;
        }
    }

    /**
     * Set the descriptor store
     * @param store
     */
    protected void setStore(ResourceDescriptorStore store) {
        this.store = store;
    }

    /**
     * Unset the store
     *
     * @param store
     */
    protected void unsetStore(ResourceDescriptorStore store) {
        this.store.shutdown();
        this.store= null;
    }

    /**
     * Activate this component
     *
     * @param context the component context
     */
    protected void activate(ComponentContext context) {
        this.context = context;
    }

    /**
     * Deactivate this component
     *
     * @param context the Component context
     */
    protected void deactivate(ComponentContext context) {
        this.context = null;
    }

}
