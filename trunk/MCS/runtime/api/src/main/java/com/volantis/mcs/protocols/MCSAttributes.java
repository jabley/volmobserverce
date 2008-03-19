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
 * $Header: /src/voyager/com/volantis/mcs/protocols/VolantisAttribute.java,v 1.14 2002/03/18 12:41:17 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Jul-01    Paul            VBM:2001070509 - Added this header and also
 *                              added tagName attribute and a copy method
 *                              which copies these attributes from another
 *                              set of attributes.
 * 26-Jul-01    Paul            VBM:2001071707 - Fixed problems with resetting
 *                              the state of the object.
 * 08-Oct-01    Doug            VBM:2001100401 - Added several public static
 *                              properties that will be used to classify a
 *                              type of attribute. For example an attribute
 *                              could be a link, text help or prompt type
 *                              of attribute.
 * 31-Jan-02    Paul            VBM:2001122105 - Removed unused event and
 *                              usage.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.styling.StyleContainer;
import com.volantis.styling.Styles;

/**
 * @mock.generate
 */
public abstract class MCSAttributes
        implements StyleContainer {

    private String id;

    private String tagName;

    private String title;

    private LinkAssetReference href = null;

    private EventAttributes eventAttributes;
    
    /**
     * The Styles to be applied for these attributes.
     */
    private Styles styles;

    /**
     * This constructor delegates all its work to the initialise method, no
     * extra initialisation should be added here, instead it should be added to
     * the initialise method.
     */
    public MCSAttributes() {
        initialise();
    }

    /**
     * This method should reset the state of this object back to its state
     * immediately after it was constructed.
     */
    public void resetAttributes() {
        // Call this after calling super.resetAttributes to allow initialise to
        // override any inherited attributes.
        initialise();
    }

    /**
     * Initialise all the data members. This is called from the constructor and
     * also from resetAttributes.
     */
    private void initialise() {
        id = null;
        tagName = null;
        title = null;
        href = null;

        if (eventAttributes != null) {
            eventAttributes.reset();
        }

        styles = null;
    }

    /**
     * Copy the standard attributes from the other attribute.
     */
    public void copy(MCSAttributes other) {
        id = other.id;
        tagName = other.tagName;
        title = other.title;

        if (other.styles != null) {
            styles = other.styles.copy();
        }

        // todo: copying id is dodgy, and events possibly as well.
        // see http://mantis:8080/mantis/Mantis_View.jsp?mantisid=2006062312
        // todo: event copy should be deep rather than shallow?
        eventAttributes = other.eventAttributes;
    }

    public EventAttributes getEventAttributes(boolean create) {
        if (eventAttributes == null) {
            eventAttributes = new EventAttributes();
        }

        return eventAttributes;
    }

    /**
     * Set the event attributes associated with this protocol attributes.
     *
     * @param eventsAttributes the event attributes to use.
     */
    public void setEventAttributes(EventAttributes eventsAttributes) {
        this.eventAttributes = eventsAttributes;
    }

    /**
     * Set the id property.
     * 
     * @param id The new value of the id property.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the value of the id property.
     * 
     * @return The value of the id property.
     */
    public String getId() {
        return id;
    }

    /**
     * Set the tagName property.
     *
     * @param tagName The new value of the tagName property.
     * @deprecated Should not be needed anymore
     */
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    /**
     * Get the value of the tagName property. Used by protocols for styling.
     *
     * @return The value of the tagName property.
     * @deprecated Should not be needed anymore
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * Set the title property.
     * 
     * @param title The new value of the title property.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the value of the title property.
     * 
     * @return The value of the title property.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the HREF property.
     *
     * @param href
     */
    public void setHref(LinkAssetReference href) {
        this.href = href;
    }

    public void setHref(String href) {
        setHref(new LiteralLinkAssetReference(href));
    }

    /**
     * Get the value of the HREF property.
     *
     * @return
     */
    public LinkAssetReference getHref() {
        return href;
    }

    // javadoc inherited.
    public Styles getStyles() {
        return styles;
    }

    // javadoc inherited.
    public void setStyles(Styles styles) {
        this.styles = styles;
    }

    /**
     * This is a convenience method which sets tagName, id, styleClass and
     * styles to those specified in the supplied ElementDetails.
     *
     * @param elementDetails used to initialise the MCSAttributes values.
     */
    public void setElementDetails(ElementDetails elementDetails) {
        if (elementDetails != null) {
            tagName = elementDetails.getElementName();
            id = elementDetails.getId();
            styles = elementDetails.getStyles();
        }
    }

    /**
     * Sets the finalizer for elements which needs it.
     * 
     * @param finalizer The finalizer to set.
     */
    protected void setFinalizer(ElementFinalizer finalizer) {
        // Throw an exception to discover invalid calls.
        throw new RuntimeException("setFinalizer() was not expected.");
    }

    /**
     * Returns true, if element described by this attributes needs finalizer.
     * 
     * @return Returns the needsFinalizer flag.
     */
    protected boolean needsFinalizer() {
        return false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Nov-05	10282/1	emma	VBM:2005110902 Forward port: fixing two layout rendering bugs

 11-Nov-05	10253/1	emma	VBM:2005110902 Fixing two layout rendering bugs

 27-Sep-05	9487/3	pduffin	VBM:2005091203 Committing new CSS Parser

 21-Sep-05	9128/5	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/3	pabbott	VBM:2005071114 Add XHTML 2 elements

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 30-Aug-05	9353/2	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 19-Aug-05	9245/4	gkoch	VBM:2005081006 vbm2005081006 attributes to store property values in styles, pt 2.

 19-Aug-05	9245/2	gkoch	VBM:2005081006 vbm2005081006 storing property values in styles

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 20-Jun-05	8483/4	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Jun-05	8665/3	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 03-Mar-05	7277/1	philws	VBM:2005011906 Port pane styling fix from MCS 3.3

 17-Feb-05	6129/12	matthew	VBM:2004102019 push pseudoElement down from MCSAttributes to SpanAttributes

 16-Feb-05	6129/7	matthew	VBM:2004102019 push pseudoElement down from MCSAttributes to SpanAttributes

 16-Feb-05	6129/5	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/3	matthew	VBM:2004102019 yet another supermerge

 23-Nov-04	6129/1	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/3	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 13-Feb-04	2966/1	ianw	VBM:2004011923 Added mcsi:policy function

 ===========================================================================
*/
