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
 * $Header: /src/voyager/com/volantis/mcs/layouts/Region.java,v 1.8 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 02-Nov-01    Paul            VBM:2001102403 - Created.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 02-Aug-02    Sumit           VBM:2002080207 - Added destination area attrib
 * 06-Aug-02    Paul            VBM:2002080604 - Added setter and getter for
 *                              destination area.
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * 10-Dec-02    Allan           VBM:2002110102 - Removed setAttribute() and 
 *                              setDeviceLayout().
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException to
 *                              declaration of visit() method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * A format that holds the content of other layouts.
 *
 * @mock.generate base="Format"
 */
public class Region extends Format implements DestinationAreaAttribute {

    /**
     * Used for localizing exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(Region.class);

    /**
     * The set of attributes which are editable by the user.
     */
    private static String[] userAttributes = new String[]{
        FormatConstants.NAME_ATTRIBUTE,
        FormatConstants.DESTINATION_AREA_ATTRIBUTE,
    };

    /**
     * The set of attributes which have default values.
     */
    private static String[] defaultAttributes = new String[]{
    };

    /**
     * The set of attributes which are persistent.
     */
    private static String[] persistentAttributes = userAttributes;

    /**
     * Create a new <code>Region</code>.
     *
     * @param canvasLayout The Layout to which this pane belongs
     */
    public Region(CanvasLayout canvasLayout) {
        super(0, canvasLayout);
    }

    /**
     * Get the name of the format type.
     *
     * @return The name of the format type.
     */
    public FormatType getFormatType() {
        return FormatType.REGION;
    }

    // Javadoc inherited from super class.
    public String[] getUserAttributes() {
        return userAttributes;
    }

    // Javadoc inherited from super class.
    public String[] getDefaultAttributes() {
        return defaultAttributes;
    }

    // Javadoc inherited from super class.
    public String[] getPersistentAttributes() {
        return persistentAttributes;
    }

    /**
     * Set the name of the Region as an attribute
     *
     * @param name The name of the Region
     */
    public void setName(String name) {
        setAttribute(FormatConstants.NAME_ATTRIBUTE,
                     name);
    }

    /**
     * Retrieve the name of the Region
     *
     * @return The name of the Region
     */
    public String getName() {
        return (String) getAttribute(FormatConstants.NAME_ATTRIBUTE);
    }

    public void setDestinationArea(String destinationArea) {
        setAttribute(FormatConstants.DESTINATION_AREA_ATTRIBUTE,
                     destinationArea);
    }

    public String getDestinationArea() {
        return (String) getAttribute(FormatConstants.DESTINATION_AREA_ATTRIBUTE);
    }

    // Javadoc inherited from super class.
    public boolean visit(FormatVisitor visitor, Object object)
            throws FormatVisitorException {
        return visitor.visit(this, object);
    }

    // Javadoc inherited.
    public void validate(ValidationContext context) {

        validateRequiredName(context);

        // destinationAreaAttribute does not require validation

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Oct-05	9652/1	gkoch	VBM:2005092204 completely custom marshalling/unmarshalling of layoutFormat

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 18-May-05	8279/1	matthew	VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

 19-Apr-05	7738/1	philws	VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05	7720/1	philws	VBM:2004102604 Localize RepositoryException messages

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
