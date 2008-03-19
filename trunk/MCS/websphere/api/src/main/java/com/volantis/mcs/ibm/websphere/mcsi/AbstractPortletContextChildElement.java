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
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.mcs.context.MarinerPageContext;

/**
 * An abstract class with common infrastructure for all children of the
 * portlet-context element.
 */
public abstract class AbstractPortletContextChildElement
        extends MCSIElement {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(AbstractPortletContextChildElement.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                            AbstractPortletContextChildElement.class);

    /**
     * Find the parent portlet-context element in the page context.
     * <p>
     * This method will log and throw an exception using the element name
     * supplied if the parent element is incorrect.
     *
     * @param pageContext used to find the parent element.
     * @param elementName the name of the the searching element.
     * @return the portlet-context element.
     * @throws MCSIException if there was no parent or it was of the incorrect
     *      type.
     */
    protected PortletContextElement findParent(MarinerPageContext pageContext,
            Object[] elementName) throws MCSIException {

        PortletContextElement parent = null;
        try {
            parent = (PortletContextElement) pageContext.peekMCSIElement();
        } catch (Exception e) {
            logger.error("mcsi-element-parent-missing", elementName, e);
            throw new MCSIException(
                        exceptionLocalizer.format(
                                    "AbstractPortletContextChildElement",
                                    elementName),
                        e);
        }

        if (parent == null) {
            logger.error("mcsi-element-parent-missing", elementName);
            throw new MCSIException(
                        exceptionLocalizer.format(
                                    "AbstractPortletContextChildElement",
                                    elementName));
        }
        return parent;
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

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 28-Oct-04	5897/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 ===========================================================================
*/
