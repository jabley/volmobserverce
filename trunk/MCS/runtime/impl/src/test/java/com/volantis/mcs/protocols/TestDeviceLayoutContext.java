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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/TestDeviceLayoutContext.java,v 1.2 2003/04/24 16:42:23 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Apr-03    Byron           VBM:2003040903 - Ported necessary functionality
 *                              from METIS.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.mcs.protocols.renderer.shared.layouts.FormatRendererContextImpl;
import com.volantis.styling.StylingFactory;

/**
 * TestDeviceLayoutContext.
 * Extends DeviceLayoutContext and change setFormatInstance from private
 * to public. 
 */
public class TestDeviceLayoutContext extends DeviceLayoutContext {


    private TestDOMOutputBuffer outputBuffer = null;

    private FormatRendererContext formatRendererContext;

    public TestDeviceLayoutContext(MarinerPageContext pageContext) {
        this();

        formatRendererContext = new FormatRendererContextImpl(
                pageContext, StylingFactory.getDefaultInstance());
        this.pageContext = pageContext;
    }

    public TestDeviceLayoutContext() {
        setCurrentFormatIndex(NDimensionalIndex.ZERO_DIMENSIONS);
        initialiseFormatInstanceContainer(10);

    }

    /**
     * Set the entry in the format instances array for the specified format to
     * the specified FormatInstance.
     * @param format The format whose context is being set.
     * @param formatContext The FormatInstance to associate with the specified
     * format.
     */
    public void setFormatInstance(Format format, NDimensionalIndex index,
                                  FormatInstance formatContext) {

        getFormatInstancesContainer(format).set(index, formatContext);
    }

    public void setOutputBuffer(TestDOMOutputBuffer outputBuffer) {
        this.outputBuffer = outputBuffer;
    }

    public OutputBuffer allocateOutputBuffer() {
        if (outputBuffer != null) {
            return outputBuffer;
        }
        return new TestDOMOutputBuffer();
    }

    public FormatRendererContext getFormatRendererContext() {
        return formatRendererContext;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10504/1	ianw	VBM:2005112312 Fixed pseudoElements in GUI and JIBX

 29-Nov-05	10484/1	ianw	VBM:2005112312 Fixed pseudoElements in GUI and JIBX

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10394/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 22-Nov-05	10381/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 15-Nov-05	10326/5	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10326/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 05-Nov-04	6112/4	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/5	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 16-Jun-04	4704/3	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name. (use generic name for current format index)

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 ===========================================================================
*/
