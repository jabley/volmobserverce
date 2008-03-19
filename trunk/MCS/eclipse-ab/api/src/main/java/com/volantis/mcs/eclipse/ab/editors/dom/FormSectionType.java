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
package com.volantis.mcs.eclipse.ab.editors.dom;

/**
 * A typesafe enumerator for FormSections that can go into an ODOM editor
 * form.
 */
public class FormSectionType {
    /**
     * The private constructor.
     */
    private FormSectionType() {
    }

    /**
     * An Alerts and Actions section.
     */
    public final static FormSectionType ALERTS_AND_ACTIONS =
            new FormSectionType();

    /**
     * As Asset Attributes section.
     */
    public final static FormSectionType ASSET_ATTRIBUTES_SECTION =
            new FormSectionType();

    /**
     * An Asset section.
     */
    public final static FormSectionType ASSET_SECTION =
            new FormSectionType();

    /**
     * An Assets section.
     */
    public final static FormSectionType ASSETS_SECTION =
            new FormSectionType();

    /**
     * An Asset Type section.
     */
    public final static FormSectionType ASSET_TYPE_SECTION =
            new FormSectionType();

    /**
     * An Asset Value section.
     */
    public final static FormSectionType ASSET_VALUE_SECTION =
            new FormSectionType();

    /**
     * An Asset Value section that includes a control for specifying the
     * type of the asset value.
     */
    public final static FormSectionType ASSET_VALUE_TYPE_SECTION =
            new FormSectionType();

    /**
     * A section for the attributes of asset parents.
     */
    public final static FormSectionType ATTRIBUTES_SECTION =
            new FormSectionType();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Feb-04	2853/1	byron	VBM:2004012303 Chart Editor should not allow user to add more than 1 asset

 17-Dec-03	2213/1	allan	VBM:2003121401 Basic editor support for all policies. Some bugs remain.

 15-Dec-03	2213/1	allan	VBM:2003121401 Commit to make available to Doug

 ===========================================================================
*/
