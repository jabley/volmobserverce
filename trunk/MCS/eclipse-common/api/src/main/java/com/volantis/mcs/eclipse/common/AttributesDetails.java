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
package com.volantis.mcs.eclipse.common;


/**
 * Definition of objects that provide details of the Attributes for
 * AttributesComposites - for use by AttributesCompositeBuilder.
 */
public interface AttributesDetails {
    /**
     * Get the list of attributes for an AttributesComposite.
     * @return The list of attributes.
     */
    public String [] getAttributes();

    /**
     * Given an attribute get an array of possible values for that
     * attribute e.g. for use in a Combo selection.
     * @param attribute The name of the attribute whose value selection
     * to provide.
     * @return A list of possible values for the attribute.
     */
    public Object [] getAttributeValueSelection(String attribute);

    /**
     * Given an attribute get an array of PresentableItems for that attribute
     * where each PresentableItem consists of a possible real value for the
     * specified attribute and the corresponding presentable value for that
     * real value.
     * @param attribute The attribute.
     * @return The array of PresentableItems for the given attribute.
     */
    public PresentableItem [] getAttributePresentableItems(String attribute);

    /**
     * Given an attribute and a valid value for that attribute provide the
     * presentable value that corresponds to the given value. If no
     * specific presentable version of the value is available then value
     * should be returned as is.
     * @param attribute The attribute.
     * @param value The value of the attribute.
     * @return The presentable value for the given value or value if there
     * is no specific presentable value.
     */
    public String getPresentableValue(String attribute, String value);

    /**
     * Get the ControlType to use for a given attribute.
     * @param attribute The name of the attribute whose ControlType to get.
     */
    public ControlType getAttributeControlType(String attribute);

    /**
     * Get the type of thing that the attribute is e.g. an imageComponent.
     * @param attribute The name of the attribute whose type to get.
     * @return The type of thing that the attribute is or null if this is
     * not defined.
     */
    public String getAttributeType(String attribute);


    /**
     * Get the supplementary value for an attribute. e.g. 'widthUnits'
     *
     * @param attribute The name of the attribute whose supplementary value we
     *                  want to get.
     * @return the supplementary value for an attribute. e.g. 'widthUnits'.
     */
    public String getSupplementaryValue(String attribute);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Jan-04	2752/1	byron	VBM:2004012602 Address issues from review

 16-Dec-03	2213/1	allan	VBM:2003121401 More editors and fixes for presentable values.

 15-Dec-03	2208/7	allan	VBM:2003121201 Include ImageAssetDetails in this fix.

 14-Dec-03	2208/5	allan	VBM:2003121201 Move PresentableItem to eclipse.common

 13-Dec-03	2208/3	allan	VBM:2003121201 Use PresentableItems for presenting attribute values.

 27-Nov-03	2013/2	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 ===========================================================================
*/
