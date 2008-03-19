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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.context;

import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.layouts.FormatNamespace;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

public class FormatReferenceFinderImpl
        implements FormatReferenceFinder {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    FormatReferenceFinderImpl.class);

    private final MarinerPageContext context;

    public FormatReferenceFinderImpl(MarinerPageContext context) {
        this.context = context;
    }

    //========================================================================
    //    Methods required to implement XFormEmulatingPageContext
    //========================================================================

    // Javadoc inherited.
    public FormatReference getFormatReference(String name, int[] indeces) {
        return new FormatReference(
                name,
                createIndex(name, indeces, FormatNamespace.CONTAINER));
    }

    // Javadoc inherited.
    public FormatReference getFormatReference(
            String name, int[] indeces, FormatNamespace namespace) {
        return new FormatReference(
                name,
                createIndex(name, indeces, namespace));
    }

    /**
     * Construct an NDimensionalIndex based on the set of values and the
     * actual number of dimensions supported by the specified format.
     *
     * @param stem        the stem name for the format
     * @param userIndeces the array of values to be converted.
     * @return the array of indices found
     */
    private NDimensionalIndex createIndex(
            String stem,
            int[] userIndeces,
            FormatNamespace namespace) {

        NDimensionalIndex index = NDimensionalIndex.ZERO_DIMENSIONS;

        Format format = context.getFormat(stem, namespace);

        if (format != null) {
            int dims = format.getDimensions();
            int[] indeces;
            int limit = Math.min(dims, userIndeces.length);
            indeces = new int[dims];
            System.arraycopy(userIndeces, 0, indeces, 0, limit);

            // If there are extra dimensions to be initialized, do so
            // explicitly
            for (int i = limit; i < dims; i++) {
                indeces[i] = 0;
            }

            index = new NDimensionalIndex(indeces, limit);
        }

        return index;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 ===========================================================================
*/
