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
package com.volantis.mcs.ibm.websphere.mcsi;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.runtime.configuration.project.GeneratedResourcesConfiguration;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;

/**
 * The generated-resources IAPI element.
 * <p>
 * This collects the base-dir attribute of the generated-resources element,
 * creates a GeneratedResourcesConfiguration object to store it in and stores
 * that into the parent element for later use. It follows a similar pattern to
 * the other related elements.
 */
public class GeneratedResourcesElement
        extends AbstractPortletContextChildElement {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The name of the element in an array for use in error messages.
     */
    private static final Object[] elementName = {"generated-resources"};

    /**
     * The "fake" configuration object which we create from the incoming XDIME.
     */
    private GeneratedResourcesConfiguration generatedResourcesConfiguration;

    /**
     * The parent element, where we store the data we create from the incoming
     * XDIME.
     */
    private PortletContextElement parent;

    // Javadoc inherited
    public int elementStart(MarinerRequestContext context,
                            PAPIAttributes mcsiAttributes)
            throws PAPIException {

        GeneratedResourcesAttributes attrs = (GeneratedResourcesAttributes)
                mcsiAttributes;
        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);

        generatedResourcesConfiguration = new GeneratedResourcesConfiguration();
        generatedResourcesConfiguration.setBaseDir(attrs.getBaseDir());

        parent = findParent(pageContext, elementName);

        pageContext.pushMCSIElement(this);

        return PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited
    public int elementEnd(MarinerRequestContext context,
                          PAPIAttributes mcsiAttributes)
            throws PAPIException {
        if (parent != null) {
            parent.setGeneratedResourcesConfiguration(
                    generatedResourcesConfiguration);
            // only pop ourselves of the stack if there was a parent.  if there
            // wasn't we would not have pushed ourselves onto the stack.
            MarinerPageContext pageContext =
                    ContextInternals.getMarinerPageContext(context);
            pageContext.popMCSIElement();
        }

        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited
    public void elementReset(MarinerRequestContext context) {
        parent = null;
        generatedResourcesConfiguration = null;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/2	ianw	VBM:2004090605 New Build system

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 28-Oct-04	5897/2	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 ===========================================================================
*/
