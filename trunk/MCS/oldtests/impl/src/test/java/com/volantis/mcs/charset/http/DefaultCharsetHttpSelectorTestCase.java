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
package com.volantis.mcs.charset.http;

import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.devrep.repository.api.devices.policy.values.PolicyValueFactory;
import com.volantis.devrep.repository.impl.accessors.TestPolicyDescriptorAccessor;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultTextPolicyType;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.devices.Device;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.mcs.http.HttpHeaders;
import com.volantis.mcs.http.servlet.HttpServletFactory;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.stubs.HttpServletRequestStub;

import java.util.HashMap;
import java.util.Map;

public class DefaultCharsetHttpSelectorTestCase extends TestCaseAbstract {

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    private CharsetHttpSelector selector;

    private HttpServletRequestStub request;
    private TestMarinerPageContext context;

    public DefaultCharsetHttpSelectorTestCase(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        //BasicConfigurator.configure();

        //  selector = new AcceptCharsetSelector(encManager);
        request = new HttpServletRequestStub();
        context = new TestMarinerPageContext();
        Map policyMap = new HashMap();
        final InternalDevice internalDevice =
            INTERNAL_DEVICE_FACTORY.createInternalDevice(
                new DefaultDevice("Test-Device", policyMap, null));
        context.setDevice(internalDevice);
    }

    public void tearDown() throws Exception {
        super.tearDown();
        //Category.shutdown();
    }

    /**
     * Ensure that a charset is selected
     * @throws Exception
     */
    public void testGetOutputCharset() throws Exception {

        selector = CharsetHttpFactory.
                getDefaultInstance().getDefaultCharsetHttpSelector();
        check(new String[]{"US-ASCII"}, null, "US-ASCII");

    }

    // utility methods
    private void check(String[] headers, String deviceCharset,
                       String expected) {
        check(headers, deviceCharset, null, expected);
    }

    private void check(String[] headers,
                       String deviceCharset,
                       String forcedCharset,
                       String expected) {
        request.setHeaders(headers);
        context.setDevicePolicyValue(DevicePolicyConstants.DEFAULT_OUTPUT_CHARSET,
                                     deviceCharset);
        context.setDevicePolicyValue(DevicePolicyConstants.FORCED_OUTPUT_CHARSET,
                                     forcedCharset);
        HttpHeaders rHeaders = HttpServletFactory.
                getDefaultInstance().getHTTPHeaders(request);

        try {
            // Create simple accessor for use in the tests
            TestPolicyDescriptorAccessor accessor =
                    new TestPolicyDescriptorAccessor();
            accessor.addPolicyDescriptor(null, "policyname",
                                         new DefaultTextPolicyType());

            // Create the factory
            PolicyValueFactory factory = PolicyValueFactory.createInstance(accessor);

            Device device = context.getDevice().getDevice();
            String charset = selector.getOutputCharset(rHeaders, device);
            if (expected != null) {
                assertEquals("Expected value should match", expected, charset);
            } else {
                assertNull(charset);
            }
        } catch (RepositoryException re) {
            fail("Repository Error " + re.getMessage());
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 03-Sep-04	5408/1	byron	VBM:2004090109 ClassCastException when calling getRealPolicyValue()

 03-Sep-04	5387/1	byron	VBM:2004090109 ClassCastException when calling getRealPolicyValue()

 03-Aug-04	5017/4	matthew	VBM:2004073003 fix merge problems

 02-Aug-04	5017/2	matthew	VBM:2004073003 CharsetHttpFactory and Selector

 ===========================================================================
*/
