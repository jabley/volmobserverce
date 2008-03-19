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
package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatVisitorAdapter;
import com.volantis.mcs.layouts.FormatVisitorException;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.NDimensionalContainer;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.util.Iterator;

/**
 * This class is responsible for providing an implementation of
 * {@link #isEmptyImpl} for all FormatInstance classes that represent
 * an iteration of some kind, eg Spatial, Temporal etc...
 *
 * @mock.generate base="FormatInstance"
 */
public abstract class AbstractFormatIteratorInstance
        extends FormatInstance {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    AbstractFormatIteratorInstance.class);

    /**
     * Initialises the new instance with the supplied parameters.
     *
     * @param index the index of this context within it's containing
     *              N dimensional container.
     */
    protected AbstractFormatIteratorInstance(NDimensionalIndex index) {
        super(index);
    }

    /**
     * Returns true this FormatInstance is empty.
     *
     * <p>Sub classes of AbstractFormatIteratorInstance are considered empty if
     * and only if {@link FormatInstance}'s associated with child Formats do
     * not contain any content at indexes relative to the current index
     * {@link DeviceLayoutContext#getCurrentFormatIndex}.</p>
     *
     * <p>Suppose the current index is 3, then we are interested in looking
     * for {@link FormatInstance}'s at indexes starting with 3,
     * eg 3.1, 3.2, 3.1.1 etc..</p>
     *
     * @return True if this object is empty and false otherwise.
     */
    protected boolean isEmptyImpl() {
        int children = format.getNumChildren();
        if (children == 0) {
            return true;
        }

        boolean empty = true;

        for (int i = 0; empty && i < children; i++) {
            Format child = format.getChildAt(i);
            if (child != null) {
                ContentFinderVisitor contentFinderVisitor =
                        new ContentFinderVisitor();
                try {
                    // Visit the child format searching for content.
                    // The visitation will return true if content is found
                   empty = !child.visit(contentFinderVisitor, null);
                } catch (FormatVisitorException e) {
                    // We don't expect any exceptions, but just in case...
                    throw new RuntimeException(
                            exceptionLocalizer.format("unexpected-exception"),
                            e);
                }
            }
        }
        return empty;
    }

    /**
     * This class is responsible for visiting the child formats of sub classes
     * of {@link AbstractFormatIteratorInstance} and inspecting Formats
     * encountered for the existence of
     * content relative to the current index
     * {@link
     * com.volantis.mcs.protocols.DeviceLayoutContext#getCurrentFormatIndex}.
     *
     * <p> Consider the following example: <br>
     * Suppose the current index is 3, then we are interested in looking
     * for content at indexes starting with 3, eg 3.1, 3.2, 3.1.1 etc..</p>
     *
     * <p>The visitor approach has been necessary as we are unable to test
     * formats for emptyness recursively from
     * {@link SpatialFormatIteratorInstance#isEmptyImpl} using
     * {@link DeviceLayoutContext#isFormatEmpty}
     * as this method tests for emptyness with respect to the current index and
     * in the context of a Spatial Iterator we are interested in all of the
     * available indexes for a given FormatInstance.</p>
     */
    class ContentFinderVisitor
            extends FormatVisitorAdapter {

        /**
         * Returns true if content is found (relative to the current index -
         * {@link DeviceLayoutContext#getCurrentFormatIndex})
         * during visitation of the supplied format; otherwise false.
         *
         * @param format the format to be tested for content.
         * @param object
         * @return true if content was found during the visitation;
         * otherwise false.
         */
        public boolean visitFormat(Format format, Object object) {

            // Keep visiting until content is found or all Formats
            // have been visited.
            boolean foundContent = false;

            FormatInstance formatInstance =
                    getFormatInstance(format);

            if (formatInstance != null) {
                foundContent = !formatInstance.isEmptyRelativeToCurrentIndex();
            }
            return foundContent;
        }

        /**
         * Returns a FormatInstance associated with the supplied format.
         *
         * @param format the format whose FormatInstance is required.
         * @return FormatInstance associated with the supplied format;
         *         or null if one does not exist.
         */
        private FormatInstance getFormatInstance(Format format) {
            NDimensionalContainer container =
                    context.getFormatInstancesContainer(format);

            Iterator iter = container.iterator();
            FormatInstance formatInstance = null;
            if (iter.hasNext()) {
                formatInstance = (FormatInstance) iter.next();
            }
            return formatInstance;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 04-Jul-05	8927/9	rgreenall	VBM:2005052611 Post review improvements.

 04-Jul-05	8927/7	rgreenall	VBM:2005052611 Post review improvements.

 04-Jul-05	8927/5	rgreenall	VBM:2005052611 Post review improvements.

 01-Jul-05	8927/3	rgreenall	VBM:2005052611 Merge from 331: Fixed SpatialIteratorFormatInstance#isEmptyImpl

 30-Jun-05	8734/3	rgreenall	VBM:2005052611 Post review improvements

 30-Jun-05	8734/1	rgreenall	VBM:2005052611 Fixed SpatialIteratorFormatInstance#isEmptyImpl

 ===========================================================================
*/
