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

import com.volantis.mcs.layouts.LayoutException;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.TemporalFormatIterator;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.activator.FormatActivator;
import com.volantis.mcs.layouts.iterators.IteratorConstraintFactory;
import com.volantis.mcs.layouts.temporal.TemporalFormatIteratorActivator;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.layouts.TemporalFormatIteratorInstance;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRenderer;
import com.volantis.mcs.repository.RepositoryException;

/**
 * Tests for temporal format iterator renderers.
 *
 * @todo Remove when code coverage indicates that unit tests have same coverage.
 */
public class TemporalFormatIteratorRendererTestCase
        extends AbstractFormatIteratorRendererTestAbstract {
    // Javadoc inherited
    protected FormatRenderer
            createFormatRenderer() {
        return new TemporalFormatIteratorRenderer(layoutAttributesFactory);
    }

    /**
     *  Tests the render method for a temporal format iterator which contains
     *  a pane with indexing direction across down
     *  Verifies against a string which represents this
     *  <slide/>
     *  <slide/>
     *  <slide><pane/></slide>
     *  <slide/>
     *  <slide/>
     *  <slide><pane/></slide>
     *  <slide/>
     *  <slide/>
     *  <slide/>
     *  We should have panes at postions 2 and 5
     *
     * @throws Exception
     *
     * todo XDIME-CP fix this
     */
    public void notestRenderTemporalFormatIteratorFixed() throws Exception {
        TemporalFormatIterator temporal = createTemporalFormatIterator();
        temporal.setAttribute(
                TemporalFormatIterator.TEMPORAL_ITERATOR_CLOCK_VALUES, "3");
        temporal.setAttribute(
            TemporalFormatIterator.TEMPORAL_ITERATOR_CELL_COUNT,"9");
        temporal.setAttribute(
            TemporalFormatIterator.TEMPORAL_ITERATOR_CELLS,"fixed");
        activateTemporal(temporal);

        //Create a new pane
        Pane pane = (Pane) createPane();
        //Pretend to get the buffers for two different format instances
        int[] idx = { 2 };
        PaneInstance ctx2 = (PaneInstance) dlContext.getFormatInstance(pane,
                new NDimensionalIndex(idx));
        ctx2.getCurrentBuffer().writeText("non-empty");
        idx[0] = 5;
        PaneInstance ctx5 = (PaneInstance) dlContext.getFormatInstance(pane,
                new NDimensionalIndex(idx));
        ctx5.getCurrentBuffer().writeText("non-empty");
        temporal.setChildAt(pane, 0);
        //Render the format
        TemporalFormatIteratorInstance sfi =
                new TemporalFormatIteratorInstance(
                        NDimensionalIndex.ZERO_DIMENSIONS);
        sfi.setFormat(temporal);
        sfi.setDeviceLayoutContext(dlContext);
        getTemporalIteratorFormatRenderer().render(formatRendererContext, sfi);

        String verifyString =
                "<slide></slide>" +
                "<slide></slide>" +
                "<slide>" +
                  "<pane borderWidth=\"0\" cellPadding=\"0\" " +
                        "cellSpacing=\"0\" width=\"100\" " +
                        "widthUnits=\"percent\">" +
                  "</pane>" +
                "</slide>" +
                "<slide></slide>" +
                "<slide></slide>" +
                "<slide>" +
                  "<pane borderWidth=\"0\" cellPadding=\"0\" " +
                        "cellSpacing=\"0\" width=\"100\" " +
                        "widthUnits=\"percent\">" +
                  "</pane>" +
                "</slide>" +
                "<slide></slide>" +
                "<slide></slide>" +
                "<slide></slide>";
        compareStringToBuffer(verifyString,
                pageContext.getCurrentOutputBuffer());
    }

    /**
     *  Tests the render method for time values.
     *  If there are more cells than timeValues then the durations for the
     *  extra cells should be the last time value existing.
     *
     *
     * @throws Exception
     *
     * todo XDIME-CP fix this
     */
    public void notestRenderTemporalFormatIteratorFixedTimeValues()
            throws Exception {
        TemporalFormatIterator temporal = createTemporalFormatIterator();
        // Set three time values
        temporal.setAttribute(
                TemporalFormatIterator.TEMPORAL_ITERATOR_CLOCK_VALUES,
                "1,2,3");
        // ... and 5 elements.  Last 2 duration values should be last
        // timeValue (3))
        temporal.setAttribute(
            TemporalFormatIterator.TEMPORAL_ITERATOR_CELL_COUNT,"5");
        temporal.setAttribute(
            TemporalFormatIterator.TEMPORAL_ITERATOR_CELLS,"fixed");
        activateTemporal(temporal);

        //Create a new pane
        Pane pane = (Pane) createPane();
        //Pretend to get the buffers for two different format instances
        int[] idx = { 2 };
        PaneInstance ctx2 = (PaneInstance) dlContext.getFormatInstance(pane,
                new NDimensionalIndex(idx));
        ctx2.getCurrentBuffer().writeText("non-empty");
        idx[0] = 5;
        PaneInstance ctx5 = (PaneInstance) dlContext.getFormatInstance(pane,
                new NDimensionalIndex(idx));
        ctx5.getCurrentBuffer().writeText("non-empty");
        temporal.setChildAt(pane, 0);
        //Render the format
        TemporalFormatIteratorInstance sfi =
                new TemporalFormatIteratorInstance(
                        NDimensionalIndex.ZERO_DIMENSIONS);
        sfi.setFormat(temporal);
        sfi.setDeviceLayoutContext(dlContext);
        getTemporalIteratorFormatRenderer().render(formatRendererContext, sfi);

        String verifyString = "1,2,3,3,3,";
        assertEquals("Time values not correct", verifyString,
                durationBuffer.toString());
    }

    /**
     *  Tests the render method for a temporal format iterator
     *  Verifies against a string which represents this
     *  <slide/>
     *  <slide/>
     *  <slide><pane/></slide>
     *  <slide/>
     *  <slide/>
     *  <slide><pane/></slide>
     *  Having specified a maximum of 6 cells we should have panes at postions
     *  2 and 5
     * @throws Exception
     *
     * todo XDIME-CP fix this
     */
    public void notestRenderTemporalFormatIteratorVariable1() throws Exception {
        TemporalFormatIterator temporal = createTemporalFormatIterator();
        temporal.setAttribute(
                TemporalFormatIterator.TEMPORAL_ITERATOR_CLOCK_VALUES, "3");
        temporal.setAttribute(
            TemporalFormatIterator.TEMPORAL_ITERATOR_CELL_COUNT, "6");
        temporal.setAttribute(
            TemporalFormatIterator.TEMPORAL_ITERATOR_CELLS, "variable");
        activateTemporal(temporal);

        createTemporal(getTemporalIteratorFormatRenderer(), temporal);

        String verifyString =
                "<slide></slide>" +
                "<slide></slide>" +
                "<slide>" +
                  "<pane borderWidth=\"0\" cellPadding=\"0\" " +
                        "cellSpacing=\"0\" width=\"100\" " +
                        "widthUnits=\"percent\">" +
                  "</pane>" +
                "</slide>" +
                "<slide></slide>" +
                "<slide></slide>" +
                "<slide>" +
                  "<pane borderWidth=\"0\" cellPadding=\"0\" " +
                        "cellSpacing=\"0\" width=\"100\" " +
                        "widthUnits=\"percent\">" +
                  "</pane>" +
                "</slide>";
        compareStringToBuffer(verifyString,
                pageContext.getCurrentOutputBuffer());
    }

    /**
     *  Tests the render method for a temporal format iterator
     *  Verifies against a string which represents this
     *  <slide/>
     *  <slide/>
     *  <slide><pane/></slide>
     *  <slide/>
     *  Having specified a maximum of 4 cells we should have panes at postion 2
     * @throws Exception
     *
     * todo XDIME-CP fix this
     */
    public void notestRenderTemporalFormatIteratorVariable2() throws Exception {
        TemporalFormatIterator temporal = createTemporalFormatIterator();
        temporal.setAttribute(
                TemporalFormatIterator.TEMPORAL_ITERATOR_CLOCK_VALUES, "3");
        temporal.setAttribute(
            TemporalFormatIterator.TEMPORAL_ITERATOR_CELL_COUNT, "4");
        temporal.setAttribute(
            TemporalFormatIterator.TEMPORAL_ITERATOR_CELLS, "variable");
        activateTemporal(temporal);

        createTemporal(getTemporalIteratorFormatRenderer(), temporal);

        String verifyString =
                "<slide></slide>" +
                "<slide></slide>" +
                "<slide>" +
                  "<pane borderWidth=\"0\" cellPadding=\"0\" " +
                  "cellSpacing=\"0\" width=\"100\" widthUnits=\"percent\">" +
                  "</pane>" +
                "</slide>" +
                "<slide></slide>";
        compareStringToBuffer(verifyString,
                pageContext.getCurrentOutputBuffer());
    }

    /**
     *  Tests the render method for a temporal format iterator
     *  Verifies against a string which represents this
     *  <slide/>
     *  <slide/>
     *  <slide><pane/></slide>
     *  <slide/>
     *  <slide/>
     *  <slide><pane/></slide>
     *  Having specified a maximum of 4 cells we should have panes at postion 2
     * @throws Exception
     *
     * todo XDIME-CP fix this
     */
    public void notestRenderTemporalFormatIteratorVariable3() throws Exception {
        TemporalFormatIterator temporal = createTemporalFormatIterator();
        temporal.setAttribute(
                TemporalFormatIterator.TEMPORAL_ITERATOR_CLOCK_VALUES, "3");
        temporal.setAttribute(
            TemporalFormatIterator.TEMPORAL_ITERATOR_CELL_COUNT, "0");
        temporal.setAttribute(
            TemporalFormatIterator.TEMPORAL_ITERATOR_CELLS, "variable");
        activateTemporal(temporal);

        createTemporal(getTemporalIteratorFormatRenderer(), temporal);

        String verifyString =
                "<slide></slide>" +
                "<slide></slide>" +
                "<slide>" +
                  "<pane borderWidth=\"0\" cellPadding=\"0\" " +
                        "cellSpacing=\"0\" width=\"100\" " +
                        "widthUnits=\"percent\">" +
                  "</pane>" +
                "</slide>" +
                "<slide></slide>" +
                "<slide></slide>" +
                "<slide>" +
                  "<pane borderWidth=\"0\" cellPadding=\"0\" " +
                        "cellSpacing=\"0\" width=\"100\" " +
                        "widthUnits=\"percent\">" +
                  "</pane>" +
                "</slide>";
        compareStringToBuffer(verifyString,
                pageContext.getCurrentOutputBuffer());
    }

    private TemporalFormatIterator createTemporalFormatIterator()
            throws RepositoryException {

        TemporalFormatIterator temporal =
                new TemporalFormatIterator((CanvasLayout) layout);

        return temporal;
    }

    /**
     * Activate the temporal iteratot.
     *
     * <p>This must be called after all the properties have been set.</p>
     *
     * @param temporal The temporal iterator.
     */
    private void activateTemporal(TemporalFormatIterator temporal)
            throws RepositoryException {

        IteratorConstraintFactory constraintFactory =
                new IteratorConstraintFactory();
        FormatActivator activator =
                new TemporalFormatIteratorActivator(constraintFactory);
        activator.activate(temporal);
    }

    /**
     * Creates a temporal format iterator
     * @param renderer
     * @param temporal
     * @throws com.volantis.mcs.layouts.LayoutException
     */
    private void createTemporal(FormatRenderer renderer,
                                TemporalFormatIterator temporal)
            throws LayoutException, RendererException {
        //Create a new pane
        Pane pane = (Pane) createPane();
        //Pretend to get the buffers for two different format instances
        int[] idx = { 2 };
        PaneInstance ctx2 = (PaneInstance) dlContext.getFormatInstance(pane,
                new NDimensionalIndex(idx));
        ctx2.getCurrentBuffer().writeText("non-empty");
        idx[0] = 5;
        PaneInstance ctx5 = (PaneInstance) dlContext.getFormatInstance(pane,
                new NDimensionalIndex(idx));
        ctx5.getCurrentBuffer().writeText("non-empty");
        temporal.setChildAt(pane, 0);
        //Render the format
        TemporalFormatIteratorInstance sfi =
                new TemporalFormatIteratorInstance(
                        NDimensionalIndex.ZERO_DIMENSIONS);
        sfi.setFormat(temporal);
        sfi.setDeviceLayoutContext(dlContext);
        renderer.render(formatRendererContext, sfi);
    }

    /**
     * Gets a suitably initialised temporal format iterator renderer.
     * @return The temporal format iterator renderer
     */
    private FormatRenderer getTemporalIteratorFormatRenderer() {
        return createFormatRenderer();
    }

    public void testDUMMY() {
        
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10394/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 12-Jul-05	8862/4	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Jan-05	6391/7	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 10-Dec-04	6391/1	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 ===========================================================================
*/
