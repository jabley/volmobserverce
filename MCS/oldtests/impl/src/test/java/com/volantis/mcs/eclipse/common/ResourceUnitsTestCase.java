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
package com.volantis.mcs.eclipse.common;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Unit test for ResourceUnits
 */
public class ResourceUnitsTestCase extends TestCaseAbstract {

    /**
     * Check normal operation of ResourceUnits by obtaining a selection of instances
     * and checking that identical instance are equal and non identical instances are not equal.
     *
     * This test is dependent on the enumeration values provided by the testee class and should
     * be updated if they are changed.
     *
     * @throws Exception
     */
    public void testNormal() throws Exception {
        ResourceUnits pxInst1 = ResourceUnits.PIXEL;
        ResourceUnits pxInst2 = ResourceUnits.PIXEL;
        ResourceUnits pcInst1 = ResourceUnits.PERCENT;
        ResourceUnits pcInst2 = ResourceUnits.PERCENT;
        ResourceUnits pointInst1 = ResourceUnits.POINT;
        ResourceUnits pointInst2 = ResourceUnits.POINT;
        ResourceUnits degreeInst1 = ResourceUnits.DEGREE;
        ResourceUnits degreeInst2 = ResourceUnits.DEGREE;
        ResourceUnits msInst1 = ResourceUnits.MILLISECOND;
        ResourceUnits msInst2 = ResourceUnits.MILLISECOND;

        assertEquals (pxInst1, pxInst2);
        assertEquals (pcInst1, pcInst2);
        assertEquals (pointInst1, pointInst2);
        assertEquals (degreeInst1, degreeInst2);
        assertEquals (msInst1, msInst2);

        assertFalse ( pxInst1.equals(pcInst1));
        assertFalse ( pxInst1.equals(pointInst1));
        assertFalse ( pxInst1.equals(degreeInst1));
        assertFalse ( pxInst1.equals(msInst1));
    }
    /**
     * Obtain instances of all the enumeration values provided by the
     * testee class and check that their localized values are the same as
     * in the vbm specification.  These values are for the default locale
     * and the test will fail if a different localisation file is provided.
     *
     * This test is dependent on the enumeration values provided by the testee class and on
     * the localisation strings provided in EclipseCommonMessages.  The test may fail if
     * either of these change.
     *
     * @throws Exception
     */
    public void testLocalisedNames() throws Exception {
        ResourceUnits pxInst = ResourceUnits.PIXEL;
        ResourceUnits pcInst = ResourceUnits.PERCENT;
        ResourceUnits pointInst = ResourceUnits.POINT;
        ResourceUnits inchInst = ResourceUnits.INCH;
        ResourceUnits cmInst = ResourceUnits.CENTIMETRE;
        ResourceUnits mmInst = ResourceUnits.MILLIMETRE;
        ResourceUnits picaInst = ResourceUnits.PICA;

        ResourceUnits emInst = ResourceUnits.EM;
        ResourceUnits exInst = ResourceUnits.EX;
        ResourceUnits degreeInst = ResourceUnits.DEGREE;
        ResourceUnits gradInst = ResourceUnits.GRAD;
        ResourceUnits radianInst = ResourceUnits.RADIAN;
        ResourceUnits sInst = ResourceUnits.SECOND;
        ResourceUnits msInst = ResourceUnits.MILLISECOND;

        assertTrue(pxInst.getLocalized().equals("Pixel"));
        assertTrue(pcInst.getLocalized().equals("%"));
        assertTrue(pointInst.getLocalized().equals("Point"));
        assertTrue(inchInst.getLocalized().equals("Inch"));
        assertTrue(cmInst.getLocalized().equals("cm"));
        assertTrue(mmInst.getLocalized().equals("mm"));
        assertTrue(picaInst.getLocalized().equals("Pica"));
        assertTrue(emInst.getLocalized().equals("em"));
        assertTrue(exInst.getLocalized().equals("ex"));
        assertTrue(degreeInst.getLocalized().equals("deg"));
        assertTrue(gradInst.getLocalized().equals("grad"));
        assertTrue(radianInst.getLocalized().equals("rad"));
        assertTrue(sInst.getLocalized().equals("sec"));
        assertTrue(msInst.getLocalized().equals("ms"));
    }
    /**
     * Obtain instances of all the enumeration values provided by the
     * testee class and check that their standard unit values are the same as
     * in the vbm specification.  These values are for the default locale
     * and the test will fail if a different localisation file is provided.
     *
     * This test is dependent on the enumeration values provided by the testee class.
     * The test may fail if those values are changed in ResourceUnits.
     *
     * @throws Exception
     */
    public void testUnitNames() throws Exception {
        ResourceUnits pxInst = ResourceUnits.PIXEL;
        ResourceUnits pcInst = ResourceUnits.PERCENT;
        ResourceUnits pointInst = ResourceUnits.POINT;
        ResourceUnits inchInst = ResourceUnits.INCH;
        ResourceUnits cmInst = ResourceUnits.CENTIMETRE;
        ResourceUnits mmInst = ResourceUnits.MILLIMETRE;
        ResourceUnits picaInst = ResourceUnits.PICA;

        ResourceUnits emInst = ResourceUnits.EM;
        ResourceUnits exInst = ResourceUnits.EX;
        ResourceUnits degreeInst = ResourceUnits.DEGREE;
        ResourceUnits gradInst = ResourceUnits.GRAD;
        ResourceUnits radianInst = ResourceUnits.RADIAN;
        ResourceUnits sInst = ResourceUnits.SECOND;
        ResourceUnits msInst = ResourceUnits.MILLISECOND;

        assertTrue(pxInst.getUnit().equals("px"));
        assertTrue(pcInst.getUnit().equals("%"));
        assertTrue(pointInst.getUnit().equals("pt"));
        assertTrue(inchInst.getUnit().equals("in"));
        assertTrue(cmInst.getUnit().equals("cm"));
        assertTrue(mmInst.getUnit().equals("mm"));
        assertTrue(picaInst.getUnit().equals("pc"));
        assertTrue(emInst.getUnit().equals("em"));
        assertTrue(exInst.getUnit().equals("ex"));
        assertTrue(degreeInst.getUnit().equals("deg"));
        assertTrue(gradInst.getUnit().equals("grad"));
        assertTrue(radianInst.getUnit().equals("rad"));
        assertTrue(sInst.getUnit().equals("s"));
        assertTrue(msInst.getUnit().equals("ms"));
    }
    /**
     * Test the static lookup facility in the testee class - ie lookup
     * localized names from the standard unit names.
     *
     * This test is dependent on the enumeration values provided by the testee class and on
     * the localisation strings provided in EclipseCommonMessages.  The test may fail if
     * either of these change.
     *
     * @throws Exception
     */
    public void testStaticLocalisedNames() throws Exception {
        assertTrue(ResourceUnits.getLocalizedFromUnitName("px").equals("Pixel"));
        assertTrue(ResourceUnits.getLocalizedFromUnitName("%").equals("%"));
        assertTrue(ResourceUnits.getLocalizedFromUnitName("pt").equals("Point"));
        assertTrue(ResourceUnits.getLocalizedFromUnitName("in").equals("Inch"));
        assertTrue(ResourceUnits.getLocalizedFromUnitName("cm").equals("cm"));
        assertTrue(ResourceUnits.getLocalizedFromUnitName("mm").equals("mm"));
        assertTrue(ResourceUnits.getLocalizedFromUnitName("pc").equals("Pica"));
        assertTrue(ResourceUnits.getLocalizedFromUnitName("em").equals("em"));
        assertTrue(ResourceUnits.getLocalizedFromUnitName("ex").equals("ex"));
        assertTrue(ResourceUnits.getLocalizedFromUnitName("deg").equals("deg"));
        assertTrue(ResourceUnits.getLocalizedFromUnitName("grad").equals("grad"));
        assertTrue(ResourceUnits.getLocalizedFromUnitName("rad").equals("rad"));
        assertTrue(ResourceUnits.getLocalizedFromUnitName("s").equals("sec"));
        assertTrue(ResourceUnits.getLocalizedFromUnitName("ms").equals("ms"));
    }
    /**
     * Test the static lookup facility in the testee class - ie lookup
     * the standard unit names from the localized names.
     *
     * This test is dependent on the enumeration values provided by the testee class and on
     * the localisation strings provided in EclipseCommonMessages.  The test may fail if
     * either of these change.
     *
     * @throws Exception
     */
    public void testStaticUnitNames() throws Exception {
        assertTrue(ResourceUnits.getUnitFromLocalizedName("Pixel").equals("px"));
        assertTrue(ResourceUnits.getUnitFromLocalizedName("%").equals("%"));
        assertTrue(ResourceUnits.getUnitFromLocalizedName("Point").equals("pt"));
        assertTrue(ResourceUnits.getUnitFromLocalizedName("Inch").equals("in"));
        assertTrue(ResourceUnits.getUnitFromLocalizedName("cm").equals("cm"));
        assertTrue(ResourceUnits.getUnitFromLocalizedName("mm").equals("mm"));
        assertTrue(ResourceUnits.getUnitFromLocalizedName("Pica").equals("pc"));
        assertTrue(ResourceUnits.getUnitFromLocalizedName("em").equals("em"));
        assertTrue(ResourceUnits.getUnitFromLocalizedName("ex").equals("ex"));
        assertTrue(ResourceUnits.getUnitFromLocalizedName("deg").equals("deg"));
        assertTrue(ResourceUnits.getUnitFromLocalizedName("grad").equals("grad"));
        assertTrue(ResourceUnits.getUnitFromLocalizedName("rad").equals("rad"));
        assertTrue(ResourceUnits.getUnitFromLocalizedName("sec").equals("s"));
        assertTrue(ResourceUnits.getUnitFromLocalizedName("ms").equals("ms"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jan-04	2526/1	doug	VBM:2003112607 Added the StyleValueComposite control

 09-Dec-03	2170/2	pcameron	VBM:2003102103 Added AssetTypeSection

 13-Nov-03	1857/3	tony	VBM:2003110704 added javadocs to unit test for LocalizationUnits

 13-Nov-03	1857/1	tony	VBM:2003110704 ResourceUnits and unit test ready for review

 ===========================================================================
*/
