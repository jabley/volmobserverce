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
 * $Header: /src/voyager/com/volantis/mcs/protocols/IteratorPaneInstance.java,v 1.6 2003/03/20 15:15:31 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Feb-01    Paul            Created.
 * 27-Jun-01    Paul            VBM:2001062704 - Sorted out the copyright.
 * 09-Jul-01    Paul            VBM:2001062810 - Cleaned up.
 * 29-Oct-01    Paul            VBM:2001102901 - Moved from layouts package.
 * 28-Feb-02    Paul            VBM:2002022804 - Generalised this object to
 *                              allow it to use any type of OutputBuffer.
 * 13-Mar-02    Paul            VBM:2002031301 - Added getOutputBuffer method
 *                              and renamed get/endContentBuffer to
 *                              get/endCurrentBuffer respectively.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                               to string.
 * 31-Oct-02    Sumit           VBM:2002111103 - Removed getOutputBuffer,
 *                              changed getCurrentBuffer to receive a
 *                              FormatInstanceReference as a parameter. Removed
 *                              CompoundBuffers and used the NDimensional 
 *                              container to create sequential buffers instead
 * 21-Nov-02    Chris W         VBM:2002111103 - Organised imports, deal with
 *                              nulls in getCurrentBuffer and isEmpty and added
 *                              logging to release method.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Contains all the state associated with an IteratorPane in a particular
 * MarinerPageContext.
 * <p>
 * NOTE: setting a styleClass on different "instances" of an iterator pane 
 * context will result in the last one set "winning". Fixing it would be 
 * trivial but iterator panes are deprecated and it used to work like this
 * so I can't be bothered. Just extract a buffer and styleclass into an 
 * object and store that in the list to fix this and override setStyleClass.
 *
 * @mock.generate base="AbstractPaneInstance"
 */
public abstract class IteratorPaneInstance
        extends AbstractPaneInstance {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
                LocalizationFactory.createLogger(IteratorPaneInstance.class);


    /**
     * The index of the buffer currently being written to.
     */
    private int currentIndex;

    /**
     * The list of buffers; one for each "iteration" of the pane.
     */
    private final ArrayList buffers = new ArrayList();

    /**
     * Create a new <code>IteratorPaneInstance</code>.
     */
    public IteratorPaneInstance(NDimensionalIndex index) {

        super(index);
    }

    /**
     * Generally returns the current buffer for this context. However, 
     * if we have a new logical buffer index via {@link #endCurrentBuffer},
     * then create a new buffer, add it to the list and then return that.
     */
    public OutputBuffer getCurrentBuffer() {

        OutputBuffer outputBuffer = null;
        if (buffers.size() <= currentIndex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Created new output buffer at " +
                        index + ":" + currentIndex);
            }
            outputBuffer = context.allocateOutputBuffer();
            outputBuffer.setTrim(true);
            buffers.add(outputBuffer);
        } else {
            outputBuffer = (OutputBuffer) buffers.get(currentIndex);
        }
        return outputBuffer;
    }

    /**
     * Increments the logical index of buffers so that a subsequent call to
     * {@link #getCurrentBuffer} will return a new buffer.
     */
    public void endCurrentBuffer() {

        if (logger.isDebugEnabled()) {
            logger.debug("Ending buffer at " + index + ":" + currentIndex);
        }
        currentIndex++;
    }

    /**
     * Get an iterator for the buffers that were created during iteration.
     * 
     * @return an iterator of output buffers, in order of creation.
     */
    public Iterator getBufferIterator() {

        return buffers.iterator();
    }

    // Javadoc inherited.
    public boolean isEmpty() {

        boolean empty = true;

        // check if we have a non-empty buffer
        for (Iterator iter = buffers.iterator(); iter.hasNext() && empty;) {
            OutputBuffer buffer = (OutputBuffer) iter.next();
            empty = buffer.isEmpty();
        }

        return empty;
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 05-Nov-04	6112/5	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/3	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 ===========================================================================
*/
