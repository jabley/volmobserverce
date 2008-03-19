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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.TemporalFormatIterator;
import com.volantis.mcs.layouts.iterators.IteratorSizeConstraint;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.SlideAttributes;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.layouts.LayoutAttributesFactory;
import com.volantis.mcs.protocols.layouts.LayoutModule;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.StringTokenizer;

/**
 * A format renderer that is used to render temporal format iterators.
 */
public class TemporalFormatIteratorRenderer
        extends AbstractFormatIteratorRenderer {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.
            createLogger(TemporalFormatIteratorRenderer.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                        TemporalFormatIteratorRenderer.class);

    /**
     * The factory to use to create attributes classes.
     */
    private final LayoutAttributesFactory factory;

    /**
     * Initialise.
     *
     * @param factory The factory to use to construct any attributes classes
     * used internally.
     */
    public TemporalFormatIteratorRenderer(LayoutAttributesFactory factory) {
        this.factory = factory;
    }

    // javadoc inherited
    public void render(final FormatRendererContext context, final FormatInstance instance)
            throws RendererException {

        if (!instance.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("TemporalFormatIterator.writeOutput");
            }
            TemporalFormatIterator temporal =
                    (TemporalFormatIterator)instance.getFormat();

            // Get the parent index and hang on to it.
            NDimensionalIndex parentIndex = instance.getIndex();
            if (logger.isDebugEnabled()) {
                logger.debug("Parent index is " + parentIndex);
            }

            // Get this temporal format iterator's properties
            IteratorSizeConstraint cellConstraint =
                    temporal.getMaxCellConstraint();

            int elements;
            // Determine no. of elements to render
            if (cellConstraint.isFixed()) {
                elements = cellConstraint.getMaximumValue();
            } else {
                // Get maximum number of cells to be rendered.
                IteratedFormatInstanceCounter instanceCounter =
                        context.getInstanceCounter();
                int cells = instanceCounter.getMaxInstances(
                        temporal, parentIndex);
                if (logger.isDebugEnabled()) {
                    logger.debug("cells=" + cells);
                }

                elements = cellConstraint.getConstrained(cells);
            }

            SlideAttributes attributes = factory.createSlideAttributes();
            String timeValues = (String) temporal.getAttribute(
                    TemporalFormatIterator.TEMPORAL_ITERATOR_CLOCK_VALUES);

            // If this is a space or (for backwards-compatibility) a comma
            // separated list of time values stick 'em into an array
            String timeValueArray[];
            String separator = null;

            if (timeValues.indexOf(',') > -1) {
                separator = ",";
            } else if (timeValues.indexOf(' ') > -1) {
                separator = " ";
            }

            if (separator != null) {
                StringTokenizer st = new StringTokenizer(timeValues,
                                                         separator);
                int index = 0;
                timeValueArray = new String[st.countTokens()];
                while (st.hasMoreTokens()) {
                    timeValueArray[index++] = st.nextToken();
                }
            } else {
                timeValueArray = new String[]{timeValues};
            }

            NDimensionalIndex childIndex = parentIndex.addDimension();

            // Get the module.
            LayoutModule module = context.getLayoutModule();

            // Retrieve the TemporalIterator's child and ensure that it
            // only has a single child.
            int numChildren = temporal.getNumChildren();
            if (numChildren != 1) {
                throw new RendererException(exceptionLocalizer.format(
                        "render-temporal-iterator-multiple-children"));
            }
            Format child = temporal.getChildAt(0);

            // Use the open/closeSlide method in the protocols to create
            // the slide. Visit each child and then close the slide. Do
            // this temporal.TEMPORAL_ITERATOR_CELL_COUNT times to generate
            // the slides
            for (int rowLoop = 0, index = 0; rowLoop < elements;
                 rowLoop++) {

                context.setCurrentFormatIndex(childIndex);
                attributes.setDuration(timeValueArray[index]);
                module.writeOpenSlide(attributes);

                FormatInstance childInstance = context.getFormatInstance(
                        child, childIndex);
                context.renderFormat(childInstance);

                module.writeCloseSlide(attributes);

                // If this is not the last slide then move onto the next slide.
                if (rowLoop + 1 < elements) {
                    childIndex = childIndex.incrementCurrentFormatIndex(1);
                    // Increment the index if it is less than the array length.
                    // The duration will be the value of the last duration if
                    // there are more slides.
                    index += (index < timeValueArray.length - 1 ? 1 : 0);
                }
            }

            context.setCurrentFormatIndex(parentIndex);
            // Write out the temporal postamble
            if (logger.isDebugEnabled()) {
                logger.debug("Context index is " + parentIndex);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Jul-05	8862/4	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Jan-05	6391/5	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 21-Dec-04	6402/3	philws	VBM:2004120208 Added aligning spatial format iterator renderer

 20-Dec-04	6402/1	philws	Promoting rebuilt jar files

 10-Dec-04	6391/1	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 ===========================================================================
*/
