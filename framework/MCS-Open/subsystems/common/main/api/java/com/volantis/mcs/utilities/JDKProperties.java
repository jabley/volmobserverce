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

/**
 * This class is responsible for providing helper methods giving details
 * of the JDK being used.
 */
public class JDKProperties {

    /**
     * Constant for the key used to obtain the vendor of the JDK being used
     * from {@link System#getProperty}
     */
    public static final String JAVA_VENDOR = "java.vendor";

    /**
     * Constant for the key used to obtain the specification version of the
     * JDK being used.
     */
    public static final String JAVA_SPECIFICATION_VERSION =
            "java.specification.version";

    /**
     * Constant for the key used to obtain virtual machine information
     * for the JDK being used.
     */
    public static final String JAVA_VM_INFO = "java.vm.info";

    /**
     * Constant value for the property associated with {@link #JAVA_VENDOR}
     * when the JDK is supplied by Sun.
     */
    public static final String SUN = "Sun";

    /**
     * Constant value for the property associated with {@link #JAVA_VENDOR}
     * when the JDK is supplied by IBM.
     */
    public static final String IBM = "IBM Corporation";

    /**
     * Returns the name of the vendor who supplied the JDK being used.
     *
     * @return name of the vendor of the JDK being used.
     */
    public String getVendor() {
        return System.getProperty(JAVA_VENDOR);
    }

    /**
     * Returns the specification version of the JDK being used, eg 1.3, 1.4,
     * 1.5.
     *
     * @return specification version of the JDK being used, eg 1.3, 1.4 ...
     */
    public String getJavaSpecificationVersion() {
        return System.getProperty(JAVA_SPECIFICATION_VERSION);
    }

    /**
     * Returns information about the virtual machine being used.  Typically,
     * this provides information about the Vendor, specification version and
     * specific build details.
     *
     * @return vm information in the form:
     * J2RE 1.3.1 IBM build cxia32131-20041210 (JIT disabled)
     */
    public String getVirtualMachineInfo() {
        return System.getProperty(JAVA_VM_INFO);
    }

    /**
     * Obtains the build date from a string obtained using
     * System.properties("java.vm.info") when an IBM
     * 1.3 JDK is in use.
     * <p>
     * This method expects a string in the form:
     * J2RE 1.3.1 IBM build cxia32131-20041210 (JIT disabled)
     *
     * @param javaVmInfo the JVM information string containing the build date.
     *
     * @return the build date contained in the vm info string.  From the
     * above example this method will return 20041210.
     */
    public String getBuildDateForIBM1_3JDK(String javaVmInfo) {

        // For IBM JDK's this propery is of the form:
        // J2RE 1.3.1 IBM build cxia32131-20041210 (JIT disabled).

        // We are interested in the build date.
        final String IBM_BUILD_DATE_PREFIX = "cxia32131";
        int indexOfBuildDatePrefix = javaVmInfo.indexOf(IBM_BUILD_DATE_PREFIX);

        if (indexOfBuildDatePrefix != -1) {

            int startingIndexOfBuildDate = indexOfBuildDatePrefix +
                (IBM_BUILD_DATE_PREFIX.length() + 1);
            final int buildDateLength = 8;

            return javaVmInfo.substring(startingIndexOfBuildDate,
                                        startingIndexOfBuildDate
                                        + buildDateLength);
        } else {
            // The supplied Java vm info string was not obtained
            // from an IBM 1.3 JDK.

            // Just return the empty string
            return "";
        }
    }

    /**
     * Returns true if IBM's 1.3 JDK build 20041210 (or later) is being
     * used. Note that this build (and later) of the IBM 1.3 JDK fixed
     * the bug in which
     * {@link java.net.ServerSocket#accept} does
     * not detect {@link java.net.ServerSocket#close}.
     *
     * @return true if the JDK in use is IBM's 1.3 JDK build 20041210
     * (or later); otherwise false.
     */
    public boolean isIBM1_3JDKBuild20041210orLater() {

        final int BUILD_DATE = 20041210;

        // Are we using an IBM JDK?
        if (getVendor().startsWith(IBM)) {
            // Is it specification version 1.3?
            if (getJavaSpecificationVersion().startsWith("1.3")) {

                String vmInfo = getVirtualMachineInfo();
                // Lets get the build date for this JDK
                String buildDateString = getBuildDateForIBM1_3JDK(vmInfo);

                int buildDate = Integer.parseInt(buildDateString);

                return ( (buildDate == BUILD_DATE) || buildDate > BUILD_DATE);

            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Returns true if the JDK in use in the 1.3 JDK from Sun.
     *
     * <p>In Sun's JDK 1.3 there exists a bug in which
     * {@link java.net.ServerSocket#accept} does
     * not detect {@link java.net.ServerSocket#close}.  This has the consequence
     * that when MCS is redeployed MarinerAgent will be left in a blocked state
     * and will not reinitialise properly.</p>
     *
     * <p>
     * See
     * <a href="http://bugs.sun.com/bugdatabase/view_bug.do;:YfiG?bug_id=4386498"/>
     * bug 4386498</a> for further details.</p>
     *
     * <p>Note that this bug has been fixed in Sun's 1.4 JDK and above. Also,
     * this bug does not exists in IBM's 1.3 JDK.</p>
     *
     * @return true if Sun's JDK 1.3 is being used; otherwise false.
     */
    public boolean isSun1_3JDK() {
        return (getVendor().startsWith(SUN)) &&
                getJavaSpecificationVersion().startsWith("1.3");
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
