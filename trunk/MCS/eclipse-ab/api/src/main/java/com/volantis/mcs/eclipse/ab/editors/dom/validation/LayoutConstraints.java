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
package com.volantis.mcs.eclipse.ab.editors.dom.validation;

import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.utilities.FaultTypes;

/**
 * Used to check all the extra constraints applicable to layouts.
 */
public class LayoutConstraints extends DOMConstraints {
    /**
     * Initializes the new instance using the given parameters.
     */
    public LayoutConstraints(boolean stopOnFirstViolation) {
        super(stopOnFirstViolation);
    }

    // javadoc inherited
    protected void initialize() {
        final String LPDM = MCSNamespace.LPDM.getURI();

        // Forms may not appear within a Form
        constraints.put(FormatType.FORM.getElementName(),
                        new AncestorContainment(
                            FormatType.FORM.getElementName(),
                            LPDM,
                            FaultTypes.MUST_NOT_BE_IN,
                            false));

        // Fragments may not appear within spatial and temporal format
        // iterators
        constraints.put(FormatType.FRAGMENT.getElementName(),
                        new DOMConstraint[] {
                            new AncestorContainment(
                                FormatType.SPATIAL_FORMAT_ITERATOR.
                                getElementName(),
                                LPDM,
                                FaultTypes.MUST_NOT_BE_IN,
                                false),
                            new AncestorContainment(
                                FormatType.TEMPORAL_FORMAT_ITERATOR.
                                getElementName(),
                                LPDM,
                                FaultTypes.MUST_NOT_BE_IN,
                                false)
                        });

        // Form Fragments may only appear within Forms and may not appear
        // within spatial and temporal iterators
        constraints.put(FormatType.FORM_FRAGMENT.getElementName(),
                        new DOMConstraint[]{
                            new AncestorContainment(
                                FormatType.FORM.getElementName(),
                                LPDM,
                                FaultTypes.MUST_BE_IN,
                                true),
                            new AncestorContainment(
                                FormatType.SPATIAL_FORMAT_ITERATOR.
                                getElementName(),
                                LPDM,
                                FaultTypes.MUST_NOT_BE_IN,
                                false),
                            new AncestorContainment(
                                FormatType.TEMPORAL_FORMAT_ITERATOR.
                                getElementName(),
                                LPDM,
                                FaultTypes.MUST_NOT_BE_IN,
                                false)
                        });

        // A single Dissecting Pane may only appear within a Fragment
        constraints.put(FormatType.DISSECTING_PANE.getElementName(),
                        new LimitedMandatoryContainment(
                            FormatType.FRAGMENT.getElementName(),
                            LPDM,
                            FaultTypes.MUST_BE_IN,
                            1,
                            FaultTypes.TOO_MANY_IN));

        // Replicas may only appear within a Fragment
        constraints.put(FormatType.REPLICA.getElementName(),
                        new AncestorContainment(
                            FormatType.FRAGMENT.getElementName(),
                            LPDM,
                            FaultTypes.MUST_BE_IN,
                            true));

        // Segments may only appear within segment grids
        constraints.put(FormatType.SEGMENT.getElementName(),
                        new AncestorContainment(
                            FormatType.SEGMENT_GRID.getElementName(),
                            LPDM,
                            FaultTypes.MUST_BE_IN,
                            true));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 ===========================================================================
*/
