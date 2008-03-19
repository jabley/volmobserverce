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
package com.volantis.mcs.utilities;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This class is responsible for testing the behaviour of
 * {@link volantis.mcs.utilities.JDKProperties}
 */
public class JDKPropertiesTestCase extends TestCaseAbstract {

    private JDKProperties jdkProps;

    // Javadoc inherited
    protected void setUp() throws Exception {
        jdkProps = new JDKProperties();
    }

    // Javadoc inherited
    protected void tearDown() throws Exception {
        jdkProps = null;
    }

    public void testGetBuildDateForValidJVMInfoString() {

        final String IBM_JDK1_3JVM_INFO = "J2RE 1.3.1 IBM build " +
                "cxia32131-20041210 (JIT disabled)";

        System.setProperty(JDKProperties.JAVA_VM_INFO,
                           IBM_JDK1_3JVM_INFO);

        String expectedDate = "20041210";
        assertEquals(expectedDate,
                     jdkProps.getBuildDateForIBM1_3JDK(IBM_JDK1_3JVM_INFO));
    }

    public void testGetBuildForNonIBMJVMInfoString() {
        final String JAVA_VM_INFO =
                "Java HotSpot(TM) Client VM (build 1.3.1_13-b03, mixed mode)";

        System.setProperty(JDKProperties.JAVA_VM_INFO,
                           JAVA_VM_INFO);

        String expectedDate = "";
        assertEquals(expectedDate,
                     jdkProps.getBuildDateForIBM1_3JDK(JAVA_VM_INFO));
    }

    public void testIsSun1_3JDKWhenTrue() {
        setJDKVendor(JDKProperties.SUN);
        setJDKSpecificationVersion("1.3");
        assertTrue(jdkProps.isSun1_3JDK());
    }

    public void testIsSun1_3JDKWhenFalse() {
        setJDKVendor(JDKProperties.IBM);
        setJDKSpecificationVersion("1.5");
        assertFalse(jdkProps.isSun1_3JDK());
    }

    public void testIsIBM1_3JDKBuild20041210orLaterWhenBuildMatches() {
        setJDKVendor(JDKProperties.IBM);
        setJDKSpecificationVersion("1.3");
        setJDKVMInfo("J2RE 1.3.1 IBM build cxia32131-20041210 (JIT disabled)");
        assertTrue(jdkProps.isIBM1_3JDKBuild20041210orLater());
    }

    public void testIsIBM1_3JDKBuild20041210orLaterWhenUsingEarlierBuild() {
        setJDKVendor(JDKProperties.IBM);
        setJDKSpecificationVersion("1.3");
        setJDKVMInfo("J2RE 1.3.1 IBM build cxia32131-20031210 (JIT disabled)");
        assertFalse(jdkProps.isIBM1_3JDKBuild20041210orLater());
    }

    public void testIsIBM1_3JDKBuild20041210orLaterWhenUsingLaterBuild() {
        setJDKVendor(JDKProperties.IBM);
        setJDKSpecificationVersion("1.3");
        setJDKVMInfo("J2RE 1.3.1 IBM build cxia32131-20051210 (JIT disabled)");
        assertTrue(jdkProps.isIBM1_3JDKBuild20041210orLater());
    }

    private void setJDKVendor(String vendor) {
        System.setProperty(JDKProperties.JAVA_VENDOR,
                           vendor);
    }

    private void setJDKSpecificationVersion(String version) {
        System.setProperty(JDKProperties.JAVA_SPECIFICATION_VERSION,
                           version);
    }

    private void setJDKVMInfo(String vmInfo) {
        System.setProperty(JDKProperties.JAVA_VM_INFO,
                           vmInfo);
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jun-05	8866/1	rgreenall	VBM:2005062004 Merge from 323: Gaurding MarinerAgent against test case failure when using JDK with bug 4386498.

 21-Jun-05	8854/1	rgreenall	VBM:2005062004 Gaurding MarinerAgent against test case failure when using JDK with bug 4386498.

 ===========================================================================
*/
