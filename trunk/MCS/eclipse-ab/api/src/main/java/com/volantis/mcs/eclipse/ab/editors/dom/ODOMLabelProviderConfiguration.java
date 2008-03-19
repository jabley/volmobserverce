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
 * A typesafe enumeration for specifying the configuration of an
 * ODOMObservableLabelProvider.
 */
public class ODOMLabelProviderConfiguration {

    /**
     * The OOLabelProviderConfiguration for just showing the individual
     * ODOMObservable i.e. none of its children if it has any.
     */
    public static final ODOMLabelProviderConfiguration JUST_OBSERVABLE =
            new ODOMLabelProviderConfiguration();

    /**
     * The OOLabelProviderConfiguration for showing the ODOMObservable
     * together with its children in the same label should there be any.
     */
    public static final ODOMLabelProviderConfiguration ELEMENT_AND_ATTRIBUTES =
            new ODOMLabelProviderConfiguration();

    /**
     * The OOLabelProviderConfiguration for showing only the children of
     * the ODOMObservable if there are any and all in the same label.
     */
    public static final ODOMLabelProviderConfiguration JUST_ATTRIBUTES =
            new ODOMLabelProviderConfiguration();

    /**
     * Construct a new OOLabelProviderConfiguration.
     */
    private ODOMLabelProviderConfiguration() {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Jan-04	2562/4	allan	VBM:2003112010 Handle outline view showing and closing.

 18-Jan-04	2562/2	allan	VBM:2003112010 ODOMOutlinePage displaying, decorating and tooltipping.

 12-Dec-03	2123/4	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 ===========================================================================
*/
