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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.integration.iapi;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.runtime.plugin.markup.MarkupPluginMethod;
import com.volantis.mcs.runtime.plugin.markup.MarkupPluginManager;
import com.volantis.mcs.integration.MarkupPlugin;
import com.volantis.mcs.integration.IntegrationPlugin;
import com.volantis.mcs.integration.iapi.IAPIAttributes;
import com.volantis.mcs.integration.iapi.IAPIElement;
import com.volantis.mcs.integration.iapi.IAPIException;
import com.volantis.mcs.integration.iapi.InvokeAttributes;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.application.ApplicationInternals;
import com.volantis.mcs.application.MarinerApplication;

import java.util.Map;
import java.util.HashMap;

/**
 * The InvokeElement IAPIElement
 */
public class InvokeElement implements IAPIElement {

    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(InvokeElement.class);

    /**
     * The arguments to pass to the invoked MarkupPlugin method.
     */
    private Map arguments;

    // Javadoc inherited from IAPIElement interface
    public int elementStart(MarinerRequestContext context,
                            IAPIAttributes iapiAttributes)
            throws IAPIException {
        // Push this element onto the IAPI stack.
        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);
        pageContext.pushIAPIElement(this);

        return PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited from IAPIElement interface
    public int elementEnd(MarinerRequestContext context,
                          IAPIAttributes iapiAttributes)
            throws IAPIException {
        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);

        MarkupPluginManager manager =
                pageContext.getVolantisBean().getMarkupPluginManager();

        InvokeAttributes attrs = (InvokeAttributes) iapiAttributes;

        final String pluginName = attrs.getName();
        MarkupPlugin plugin = manager.getMarkupPlugin(context, pluginName);

        String name = attrs.getName();
        String methodName = attrs.getMethodName();

        MarkupPluginMethod method = MarkupPluginMethod.literal(methodName);
        if (MarkupPluginMethod.INITIALIZE == method) {
            // Get the MarinerApplication to pass to the plugin initialise
            // method.
            MarinerApplication application
                    = context.getMarinerApplication();
            if (arguments == null) {
                arguments = new HashMap();
            }
            arguments.put(IntegrationPlugin.APPLICATION, application);
            doPluginInitialize(plugin, arguments, name);

        } else if (MarkupPluginMethod.PROCESS == method) {
            doPluginProcess(context, plugin, arguments, name);
        } else if (MarkupPluginMethod.RELEASE == method) {
            doPluginRelease(plugin, name);
        } else {
            logger.warn("plugin-method-invocation-failure",
                        new Object[]{methodName, name,
                                     MarkupPlugin.class.getName()});
        }

        // pop this element off the IAPI stack.
        pageContext.popIAPIElement();

        return CONTINUE_PROCESSING;
    }


    // Javadoc inherited from IAPIElement interface
    public void elementReset(MarinerRequestContext context) {
        arguments = null;
    }

    /**
     * Execute the initialize method on the specified plugin passing the
     * specified map of arguments.
     * @param plugin The MarkupPlugin on which to exectute the initialize
     *               method
     * @param args The Map of arguments to pass into the initialize method.
     * @param pluginName The name of the markup plugin.
     */
    protected void doPluginInitialize(MarkupPlugin plugin, Map args,
                                      String pluginName) {
        try {
            plugin.initialize(args);
        } catch (Exception e) {
            logger.error("plugin-initialization-error", new Object[]{pluginName});
        }
    }

    /**
     * Execute the process method on the specified plugin passing the
     * specified map of arguments.
     * @param context The current MarinerRequestContext for the markup being
     *                processed
     * @param plugin The MarkupPlugin on which to exectute the initialize
     *               method
     * @param args The Map of arguments to pass into the initialize method.
     * @param pluginName The name of the markup plugin.
     */
    protected void doPluginProcess(MarinerRequestContext context,
                                   MarkupPlugin plugin, Map args,
                                   String pluginName) {
        try {
            plugin.process(context, args);
        } catch (Exception e) {
            logger.error("plugin-process-failure", new Object[]{pluginName});
        }
    }

    /**
     * Execute the initialize method on the specified plugin passing the
     * specified map of arguments.
     * @param plugin The MarkupPlugin on which to exectute the initialize
     *               method
     * @param pluginName The name of the markup plugin.
     */
    protected void doPluginRelease(MarkupPlugin plugin, String pluginName) {
        try {
            plugin.release();
        } catch (Exception e) {
            logger.error("plugin-release-exception", new Object[]{pluginName});
        }
    }

    /**
     * Set the Map of arguments.  This method will typically be called by
     * ArgumentElement.
     * @param arguments the map of arguments to pass to the method invoked on
     * the MarkupPlugin.
     */
    void setArguments(Map arguments) {
        this.arguments = arguments;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jan-05	6712/3	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 16-Jul-03	757/1	adrian	VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

 ===========================================================================
*/
