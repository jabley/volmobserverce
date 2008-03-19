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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-May-03    Byron           VBM:2003051903 - Created. Must be hand-written
 *                              to call writeInitialFocus(..) in
 *                              elementStartImpl(..) method.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.BlockAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.TableDataCellAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The table data cell element.
 */
public final class TableDataCellElementImpl
        extends BlockElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(TableDataCellElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory
                    .createExceptionLocalizer(TableDataCellElementImpl.class);

    /**
     * The attributes to pass to the protocol methods.
     */
    private com.volantis.mcs.protocols.TableCellAttributes pattributes;

    /**
     * Create a new <code>TableDataCellElement</code>.
     */
    public TableDataCellElementImpl() {
        pattributes = new com.volantis.mcs.protocols.TableCellAttributes();
        pattributes.setTagName("td");
    }

    // Javadoc inherited.
    MCSAttributes getMCSAttributes() {
        return pattributes;
    }

    // Javadoc inherited from super class.
    protected int elementStartImpl(
            MarinerRequestContext context,
            BlockAttributes blockAttributes)
            throws PAPIException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        TableDataCellAttributes attributes
                = (TableDataCellAttributes) blockAttributes;

        pattributes.setId(attributes.getId());
        pattributes.setTitle(attributes.getTitle());
        pattributes.setTabindex(attributes.getTabindex());

        // Initialise the general event attributes
        PAPIInternals.initialiseGeneralEventAttributes(pageContext,
                attributes,
                pattributes);

        // Initialise the focus event attributes
        PAPIInternals.initialiseFocusEventAttributes(pageContext,
                attributes,
                pattributes);

        pattributes.setAlign(attributes.getAlign());
        pattributes.setBgColor(attributes.getBgColor());
        pattributes.setColSpan(attributes.getColSpan());
        pattributes.setHeight(attributes.getHeight());
        pattributes.setNoWrap(attributes.getNoWrap());
        pattributes.setRowSpan(attributes.getRowSpan());
        pattributes.setTabindex(attributes.getTabindex());
        pattributes.setVAlign(attributes.getVAlign());
        pattributes.setWidth(attributes.getWidth());

        VolantisProtocol protocol = pageContext.getProtocol();
        String tabindex = (String) pattributes.getTabindex();
        if (tabindex != null) {
            String initialFocus =
                    protocol.getCanvasAttributes().getInitialFocus();
            if ((initialFocus != null)
                    && (initialFocus.equals(attributes.getId()))) {
                protocol.writeInitialFocus(tabindex);
            }
        }
        try {
            protocol.writeOpenTableDataCell(pattributes);
        } catch (ProtocolException e) {
            logger.error("rendering-error", pattributes.getTagName(), e);
            throw new PAPIException(
                    exceptionLocalizer.format("rendering-error",
                            pattributes.getTagName()),
                    e);
        }


        return PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited from super class.
    protected int elementEndImpl(
            MarinerRequestContext context,
            BlockAttributes blockAttributes)
            throws PAPIException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        VolantisProtocol protocol = pageContext.getProtocol();

        protocol.writeCloseTableDataCell(pattributes);

        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {

        pattributes.resetAttributes();
        pattributes.setTagName("td");

        super.elementReset(context);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 18-Nov-04	6183/1	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 19-Feb-04	2789/5	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/3	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Aug-03	958/3	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 08-Aug-03	958/1	chrisw	VBM:2003070704 half way through changes to existing PAPI

 31-Jul-03	868/1	mat	VBM:2003070704 Initial work on this task

 ===========================================================================
*/
