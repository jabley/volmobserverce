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
package com.volantis.mcs.servlet;

import com.volantis.charset.EncodingManager;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.http.HttpHeaders;
import com.volantis.mcs.http.servlet.HttpServletFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.stubs.HttpServletRequestStub;

import java.util.HashMap;
import java.util.Map;

/**
 * Test {@link AcceptCharsetSelector}.
 */
public class AcceptCharsetSelectorTestCase extends TestCaseAbstract {

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    private static EncodingManager encManager;

    static {
        // slow so lets just do it once.
        encManager = new EncodingManager();
    }

    private AcceptCharsetSelector selector;

    private HttpServletRequestStub request;

    private TestMarinerPageContext context;

    private InternalDevice internalDevice;

    public AcceptCharsetSelectorTestCase(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        //BasicConfigurator.configure();

        selector = new AcceptCharsetSelector(encManager);
        request = new HttpServletRequestStub();
        context = new TestMarinerPageContext();
        Map policyMap = new HashMap();

        internalDevice = INTERNAL_DEVICE_FACTORY.createInternalDevice(
            new DefaultDevice("Test-Device", policyMap, null));
        context.setDevice(internalDevice);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        //Category.shutdown();
    }


    //
    // Test some basic cases.
    //

    /**
     * If the user provides a just "normal" charset with implicit qvalue of 1,
     * we use that.
     */
    public void testSimple() {
        check(new String[]{"US-ASCII"}, null, "US-ASCII");
    }

    /**
     * If the user provides charsets across multiple headers lines, we use
     * them appropriately.
     */
    public void testMultipleLines() {
        check(new String[]{"US-ASCII;q=0.1", "UTF-16"}, null, "UTF-16");
        check(new String[]{"US-ASCII", "UTF-16;q=0.1"}, null, "US-ASCII");
    }

    /**
     * If the user provides more than one charset on a line we use them
     * appropriately.
     */
    public void testMultipleEntries() {
        check(new String[]{"UTF-16;q=0.1, US-ASCII"}, null, "US-ASCII");
        check(new String[]{"UTF-16, US-ASCII;q=0.1"}, null, "UTF-16");
    }

    /**
     * If the user provides an invalid charset we use the implicit default.
     */
    public void testNotValid() {
        check(new String[]{"<example invalid charset>"}, null, "ISO-8859-1");
    }


    //
    // Test how we deal with the "*" special value.
    //

    /**
     * If user provides *, we use the device default.
     */
    public void testStar() {
        check(new String[]{"*"}, "UTF-16", "UTF-16");
    }

    /**
     * If user provides *, and the device default is missing, we use the
     * system default.
     */
    public void testStarDeviceMissing() {
        check(new String[]{"*"}, null, "UTF-8");
    }

    /**
     * If the user specifies that the implicit, device and system defaults are
     * unacceptable, and then provides *, then we ignore the * and select
     * the next valid charset.
     */
    public void testStarAllDefaultsUnacceptable() {
        check(new String[]{"ISO-8859-1;q=0,UTF-16;q=0,UTF-8;q=0,*,US-ASCII"},
              "UTF-16", "US-ASCII");
    }

    //
    // Test what happens when the client specifies just an empty header(s).
    //

    /**
     * If just an empty header was provided, and device default is missing,
     * we use the implicit default of IS0-8559-1.
     */
    public void testEmptyHeader() {
        check(new String[]{""}, null, "ISO-8859-1");
    }

    //
    // Test what happens when no headers are provided at all.
    //

    /**
     * If no headers are provided, we fall back to the device default.
     */
    public void testNoHeadersFallback() {
        check(new String[]{}, "UTF-16", "UTF-16");
    }

    /**
     * If no headers are provided, and device default is missing, we fall back
     * to system default.
     */
    public void testNoHeadersFallbackDeviceMissing() {
        check(new String[]{}, null, "UTF-8");
    }

    /**
     * If no headers are provided, and device default is invalid, we fall back
     * to system default.
     */
    public void testNoHeadersFallbackDeviceInvalid() {
        check(new String[]{}, "<example invalid charset>", "UTF-8");
    }


    //
    // Test what happens when the client specifies various "not acceptable"
    // values.
    //

    /**
     * If user provides only a "not acceptable" value, we use the implicit
     * default of ISO-8859-1.
     */
    public void testNotAcceptable() {
        check(new String[]{"US-ASCII;q=0"}, null, "ISO-8859-1");
    }

    /**
     * If the user provides only a "not acceptable" value which matches the
     * implicit default of ISO-8859-1, and the device default is missing, then
     * we use the system default.
     * <p>
     * NOTE: this behaviour goes against the spec which says we SHOULD
     * return null in this case (i.e. generate a 406).
     */
    public void testNotAcceptableISO_8859_1() {
        check(new String[]{"ISO-8859-1;q=0"}, null, "UTF-8");
    }

    /**
     * If forced charset set use it.
     */
    public void testForcedOutputCharsetSet() {
        check(new String[]{"ISO-8859-1;q=0"}, null, "UTF-16", "UTF-16");
    }

    /**
     * If forced charset empty, don't use it.
     */
    public void testForcedOutputCharsetEmpty() {
        // UTF-8 is default system charset.
        check(new String[]{"ISO-8859-1;q=0"}, null, "", "UTF-8");
    }

    /**
     * If forced charset is null, don't use it - use default (it is set).
     */
    public void testForcedOutputCharsetNull() {
        check(new String[]{"ISO-8859-1;q=0"}, "UTF-16", null, "UTF-16");
    }

    /**
     * If forced charset set, use it in preference to valid default.
     */
    public void testForcedOutputCharsetSetAndHasDefault() {
        check(new String[]{"ISO-8859-1;q=0"}, "UTF-16", "ForcedCS", "ForcedCS");
    }

    /**
     * Forced charset set, empty headers and has default => use forced charset.
     */
    public void testForcedOutputCharsetSetEmptyHeaders() {
        check(new String[]{""}, "UTF-16", "ForcedCS", "ForcedCS");
    }

    /**
     * Forced charset set, no headers and has default => use forced charset.
     */
    public void testForcedOutputCharsetNoHeaders() {
        check(new String[]{}, "UTF-16", "ForcedCS", "ForcedCS");
    }

    /**
     * Forced charset set, no headers and no default => use forced charset.
     */
    public void testForcedOutputCharsetNoHeadersEmptyDefault() {
        check(new String[]{}, "", "ForcedCS", "ForcedCS");
    }

    /**
     * Forced charset set, no headers and has default => use default.
     */
    public void testForcedOutputCharsetHasDefault() {
        check(new String[]{}, "UTF-16", "", "UTF-16");
    }

    /**
     * Forced charset set, no headers no default => use system charset.
     */
    public void testForcedOutputCharsetNoSettings() {
        // UTF-8 is default system charset.
        check(new String[]{}, "", "", "UTF-8");
    }

    //
    // Infrastructure.
    //

    private void check(String[] headers, String deviceCharset,
                       String expected) {
        check(headers, deviceCharset, null, expected);
    }

    private void check(String[] headers,
                       String deviceCharset,
                       String forcedCharset,
                       String expected) {
        request.setHeaders(headers);
        final DefaultDevice defaultDevice =
            (DefaultDevice) internalDevice.getDevice();
        defaultDevice.setPolicyValue(DevicePolicyConstants.DEFAULT_OUTPUT_CHARSET,
                                      deviceCharset);
        defaultDevice.setPolicyValue(DevicePolicyConstants.FORCED_OUTPUT_CHARSET,
                                      forcedCharset);
        HttpHeaders rHeaders = HttpServletFactory.
                getDefaultInstance().getHTTPHeaders(request);
        String charset = selector.selectCharset(rHeaders,
            (DefaultDevice) context.getDevice().getDevice());
        if (expected != null) {
            assertEquals("Expected value should match", expected, charset);
        } else {
            assertNull(charset);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Aug-04	5017/7	matthew	VBM:2004073003 merge problems

 03-Aug-04	5017/5	matthew	VBM:2004073003 fix merge problems

 02-Aug-04	5017/1	matthew	VBM:2004073003 CharsetHttpFactory and Selector

 10-Mar-04	3374/1	byron	VBM:2004030807 Specify which Character Set to use if multiple available

 25-Jul-03	858/4	geoff	VBM:2003071405 detect invalid charset properly

 25-Jul-03	858/2	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 24-Jul-03	807/5	geoff	VBM:2003071405 use fallbacks more often and allow user to set it themselves if we can't

 24-Jul-03	807/3	geoff	VBM:2003071405 now with fixed architecture

 23-Jul-03	807/1	geoff	VBM:2003071405 works and tested but no design review yet

 ===========================================================================
*/
