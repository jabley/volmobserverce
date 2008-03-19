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

package com.volantis.mcs.eclipse.ab.editors;

import com.volantis.mcs.eclipse.common.odom.ChangeQualifier;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import org.jdom.Element;

/**
 * <code>ElementSelectionChangeEvent</code>s should wrap
 * <code>ODOMChangeEvent</code> providing the same interface and delegating the
 * the wrapped <code>ODOMChangeEvent</code> if there is one.
 * <p/>
 * An ElementSelectionChangeEvent that is associated with an
 * <code>ODOMChangeEvent</code> has been derived from one or more
 * <code>ODOMChangeEvent</code>s. The <code>ODOMChangeEvent</code> should be
 * provided at construction time.
 * <p/>
 * All methods (i.e. getSource(), getOldValue(), getNewValue() and
 * getChangeQualifier()) should delegate to the <code>ODOMChangeEvent</code>
 * but where <code>ODOMChangeEvent</code> returns Object,
 * <code>ElementSelectionChangeEvent</code> should return a JDOM Element.
 * <p/>
 * Alternatively, if the <code>ElementSelectionChangeEvent</code> is generated
 * as a result of a selection change then all that is required is the JDOM
 * Element that the selection has changed to. This is the source. A call to
 * getSource() will return this element. All other methods should return null
 * except getChangeQualifier() which should return ChangeQualifier.NONE to
 * indicate that the Element itself has not changed.
 *
 * @see ODOMChangeEvent
 */
public final class ElementSelectionChangeEvent {

    /**
     * Store the source as a JDOM element.
     */
    private final Element source;

    /**
     * Store the oldValue as a JDOM element.
     */
    private final Element oldValue;

    /**
     * Store the newValue as a JDOM element.
     */
    private final Element newValue;

    /**
     * Store the ChangeQualifier.
     */
    private final ChangeQualifier changeQualifier;


    /**
     * Internal constructor used for updating the state of this object
     *
     * @param source          the source JDOM element.
     * @param oldValue        the oldValue JDOM element.
     * @param newValue        the newValue JDOM element.
     * @param changeQualifier the ChangeQualifier object.
     */
    private ElementSelectionChangeEvent(Element source,
                                        Element oldValue,
                                        Element newValue,
                                        ChangeQualifier changeQualifier) {
        this.source = source;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changeQualifier = changeQualifier;
    }

    /**
     * Construct this object with the ODOMChangeEvent event.
     *
     * @param event the ODOMChangeEvent object.
     */
    public ElementSelectionChangeEvent(ODOMChangeEvent event) {
        this((Element)event.getSource(), (Element)event.getOldValue(),
                (Element)event.getNewValue(), event.getChangeQualifier());
    }

    /**
     * Construct the object with the JDOM element source.
     *
     * @param source the JDOM element source.
     */
    public ElementSelectionChangeEvent(Element source) {
        this(source, null, null, ChangeQualifier.NONE);
    }

    /**
     * Get the source JDOM element.
     *
     * @return the source as an <code>JDOM</code> element.
     */
    public Element getSource() {
        return source;
    }

    /**
     * Get the old value JDOM element.
     *
     * @return the old value as a JDOM <code>Element</code> object.
     */
    public Element getOldValue() {
        return oldValue;
    }

    /**
     * Get the new value JDOM element.
     *
     * @return the new value as a JDOM <code>Element</code> object.
     */
    public Element getNewValue() {
        return newValue;
    }

    /**
     * Get the change qualifier as a <code>ChangeQualifier</code> object.
     *
     * @return the change qualifier as a <code>ChangeQualifier</code> object.
     */
    public ChangeQualifier getChangeQualifier() {
        return changeQualifier;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Nov-03	1808/3	byron	VBM:2003110406 ElementSelectionChange event handling - fixed javadoc

 06-Nov-03	1808/1	byron	VBM:2003110406 ElementSelectionChange event handling

 ===========================================================================
*/
