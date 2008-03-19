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
package com.volantis.mcs.policies.impl.io;

import com.volantis.mcs.policies.impl.io.JIBXTestAbstract;


/**
 * Test that Layout trees read from supplied XML are correct.
 * <p/>
 *
 * @todo rename this to something more sensible.
 */
public class LayoutReaderTestCase extends JIBXTestAbstract {

    public void testReadingLayoutBig() throws Exception {

        doRoundTrip(RESOURCE_LOADER.getResourceAsString("testReadingLayoutBig.xml"), null);
    }

    public void testReadingLayoutBasicTranscoderForm() throws Exception {

        doRoundTrip(RESOURCE_LOADER.getResourceAsString("testReadingLayoutBasicTranscoderForm.xml"), null);
    }

    public void testReadingLayoutError() throws Exception {

        doRoundTrip(RESOURCE_LOADER.getResourceAsString("testReadingLayoutError.xml"), null);
    }

    public void testReadingLayoutFNavigation() throws Exception {

        doRoundTrip(RESOURCE_LOADER.getResourceAsString("testReadingLayoutFNavigation.xml"), null);
    }

    public void testReadingLayoutLayout() throws Exception {

        doRoundTrip(RESOURCE_LOADER.getResourceAsString("testReadingLayoutLayout.xml"), null);
    }

    public void testReadingLayoutPortletFilter() throws Exception {

        doRoundTrip(RESOURCE_LOADER.getResourceAsString("testReadingLayoutPortletFilter.xml"), null);
    }

    public void testReadingLayoutQ403VolhomeA() throws Exception {

        doRoundTrip(RESOURCE_LOADER.getResourceAsString("testReadingLayoutQ403VolhomeA.xml"), null);
    }

    public void testReadingLayoutSampleDissecting() throws Exception {

        doRoundTrip(RESOURCE_LOADER.getResourceAsString("testReadingLayoutSampleDissecting.xml"), null);
    }

    public void testReadingLayoutTTAlgorithmFromHell() throws Exception {

        doRoundTrip(RESOURCE_LOADER.getResourceAsString("testReadingLayoutTTAlgorithmFromHell.xml"), null);
    }

    public void testReadingLayoutTTMontage() throws Exception {

        doRoundTrip(RESOURCE_LOADER.getResourceAsString("testReadingLayoutTTMontage.xml"), null);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-05	10756/3	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 09-Dec-05	10738/1	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 27-Oct-05	9986/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 03-Oct-05	9590/7	schaloner	VBM:2005092204 Updated formatCount in each format in DeviceLayout

 02-Oct-05	9652/6	gkoch	VBM:2005092204 completely custom marshalling/unmarshalling of layoutFormat

 02-Oct-05	9590/5	schaloner	VBM:2005092204 Migrated XMLLayoutAccessor and XMLDeviceLayoutAccessor to JiBX

 02-Oct-05	9652/2	gkoch	VBM:2005092204 Tests for layoutFormat marshaller/unmarshaller

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 ===========================================================================
*/
