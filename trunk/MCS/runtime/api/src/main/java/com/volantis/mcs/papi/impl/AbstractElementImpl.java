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
 * $Header: /src/voyager/com/volantis/mcs/papi/AbstractElement.java,v 1.8 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Oct-01    Paul            VBM:2001111402 - Created
 * 28-Nov-01    Paul            VBM:2001112202 - Updated to reflect changes
 *                              in papi classes.
 * 30-Nov-01    Paul            VBM:2001112909 - Added copyright statement.
 * 28-Feb-02    Paul            VBM:2002022804 - Implemented getContentWriter
 *                              and getDirectWriter.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Added missing PAPIAttributes
 *                              parameter to writeDirect.
 * 23-May-02    Paul            VBM:2002042202 - Made the elementContent and
 *                              elementDirect methods use the writers returned
 *                              from the getContentWriter and getDirectWriter
 *                              methods respectively.
 * 10-Mar-02    Steve           VBM:2003022403 - Added API doclet tags
 * 14-Apr-03    Steve           VBM:2003041501 - Access NativeWriter
 * 23-Apr-03    Steve           VBM:2003041606 - Add isBlock() and 
 *                              hasMixedContent() methods which must be 
 *                              implemented by all PAPI elements. These 
 *                              versions simply return sensible defaults.
 *                              The encoding writer is now an OutputBufferWriter
 *                              so getContentWriter handles this.
 * 19-May-05    Chris W         VBM:2003051902 - isBlock(), hasMixedContent(),
 *                              isPreFormatted() made package protected. Writer
 *                              returned by getContentWriter(), getDirectWriter
 * 28-May-03    Steve           VBM:2003042206 - Patch 2003041501 from Metis
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.DeprecatedPAPIElement;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBufferWriter;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.layouts.ContainerInstance;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.io.IOException;
import java.io.Writer;

/**
 * This class implements functionality which is common across the majority of
 * the PAPI elements.
 */
public abstract class AbstractElementImpl
        implements DeprecatedPAPIElement {


    /**
     * Used to retrieve localized exception messages.
     */
    protected static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    AbstractElementImpl.class);

    // Javadoc inherited from super class.
    public void elementContent(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes,
            String content)
            throws PAPIException {

        Writer writer = getContentWriter(context, papiAttributes);
        try {
            writer.write(content);
        }
        catch (IOException e) {
            throw new PAPIException(e);
        }
    }

    // Configure the writer to the element
    private void configureWriter(OutputBufferWriter writer) {
        writer.setElementHasMixedContent(hasMixedContent());
        writer.setElementIsBlock(isBlock());
        writer.setElementIsPreFormatted(isPreFormatted());
    }

    // Javadoc inherited from super class.
    public Writer getContentWriter(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);
        VolantisProtocol protocol = pageContext.getProtocol();

        OutputBufferWriter writer = protocol.getContentWriter();
        configureWriter(writer);
        return writer;
    }

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {
    }

    // Javadoc inherited from super class.
    boolean isBlock() {
        // return false as a default as most elements are not blocks
        return false;
    }

    // Javadoc inherited from super class.
    boolean hasMixedContent() {
        // return true as a default as most elements have mixed content
        return true;
    }

    // Javadoc inherited from super class.
    boolean isPreFormatted() {
        // return false as a default, <pre> and <code> will override
        return false;
    }

    /**
     * Get the 'insert after' node for a given container and MarinerPageContext.
     * Note that this may return null if the container instance is null.
     *
     * @param containerInstance the container instance to be used to find the DOM OutputBuffer
     * @param pageContext       the page context.
     */
    protected Node getInsertAfterNode(
            ContainerInstance containerInstance,
            MarinerPageContext pageContext) {
        Node result = null;
        if (containerInstance != null) {
            DOMOutputBuffer dom = (DOMOutputBuffer)
                    containerInstance.getCurrentBuffer();

            result = dom.getRoot().getTail();
        }
        return result;
    }

    // Javadoc inherited from super class.
    public void elementDirect(
            MarinerRequestContext context,
            String content)
            throws PAPIException {

        Writer writer = getDirectWriter(context);
        try {
            writer.write(content);
        } catch (IOException e) {
            throw new PAPIException(e);
        }
    }

    // Javadoc inherited from super class.
    public Writer getDirectWriter(MarinerRequestContext context)
            throws PAPIException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        VolantisProtocol protocol = pageContext.getProtocol();
        OutputBufferWriter writer = protocol.getDirectWriter();
        configureWriter(writer);
        return writer;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 05-Nov-04	6112/4	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 29-Jun-04	4713/4	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 16-Jun-04	4704/3	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name. (use generic name for current format index)

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 23-Jun-03	424/3	byron	VBM:2003022825 Fixed javadoc and variable naming

 20-Jun-03	424/1	byron	VBM:2003022825 Enhance behaviour of pane element within xfform

 ===========================================================================
*/
