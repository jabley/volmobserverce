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
package com.volantis.mcs.devices;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Map;
import java.util.HashMap;

/**
 * This class is responsible for testing the behaviour of
 * {@link DevicePolicyValueUtil}
 */
public class DevicePolicyValueUtilTestCase extends TestCaseAbstract {

    public void testNullPolicyValue() {
        String policyValue = null;
        Map expectedMap = new HashMap();

        testCreateMapFromPolicyValues(policyValue, expectedMap);
    }

    public void testSingleKeyValuePair() {
        String policyValue = "name=content";

        Map expectedMap = new HashMap();
        expectedMap.put("name", "content");

        testCreateMapFromPolicyValues(policyValue, expectedMap);
    }

    public void testSingleKeyValuePairWithCommaAfterLastValue() {
        String policyValue = "name=content,";

        Map expectedMap = new HashMap();
        expectedMap.put("name", "content");

        testCreateMapFromPolicyValues(policyValue, expectedMap);
    }

    public void testTwoKeyValuePairs() {

        String policyValues = "name=content,name2=content2";

        Map expectedMap = new HashMap();
        expectedMap.put("name", "content");
        expectedMap.put("name2", "content2");

        testCreateMapFromPolicyValues(policyValues, expectedMap);
    }

    public void testThreeKeyValuePairs() {
        String policyValues = "name=content,name2=content2,name3=content3";

        Map expectedMap = new HashMap();
        expectedMap.put("name", "content");
        expectedMap.put("name2", "content2");
        expectedMap.put("name3", "content3");

        testCreateMapFromPolicyValues(policyValues, expectedMap);
    }


    public void testTenKeyValuePairs() {
        String policyValues = "name=content,name2=content2,name3=content3," +
                "name4=content4,name5=content5,name6=content6," +
                "name7=content7,name8=content8,name9=content9," +
                "name10=content10";

        Map expectedMap = new HashMap();
        expectedMap.put("name", "content");
        expectedMap.put("name2", "content2");
        expectedMap.put("name3", "content3");
        expectedMap.put("name4", "content4");
        expectedMap.put("name5", "content5");
        expectedMap.put("name6", "content6");
        expectedMap.put("name7", "content7");
        expectedMap.put("name8", "content8");
        expectedMap.put("name9", "content9");
        expectedMap.put("name10", "content10");

        testCreateMapFromPolicyValues(policyValues, expectedMap);
    }

    public void testKeyValuePairWithCommaSeperatedValues() {
        String policyValue = "name=\"a,b,c\"";

        Map expectedMap = new HashMap();
        expectedMap.put("name", "a,b,c");

        testCreateMapFromPolicyValues(policyValue, expectedMap);
    }

    public void testKeyValuePairWithCommaSeperatedValuesWithSpaceAfterEqls() {
        String policyValue = "name   =         \"a,b,c\"";

        Map expectedMap = new HashMap();
        expectedMap.put("name", "a,b,c");

        testCreateMapFromPolicyValues(policyValue, expectedMap);
    }

    public void testSingleValueMultipleValueSingleValue() {
        String policyValue = "name=content,name1=\"a,b,c\",name2=content2";

        Map expectedMap = new HashMap();
        expectedMap.put("name", "content");
        expectedMap.put("name1", "a,b,c");
        expectedMap.put("name2", "content2");

        testCreateMapFromPolicyValues(policyValue, expectedMap);
    }

    public void testMultipleValueSingleValueSingleValue() {
        String policyValue = "name1=\"a,b,c\",name=content,name2=content2";

        Map expectedMap = new HashMap();
        expectedMap.put("name1", "a,b,c");
        expectedMap.put("name", "content");
        expectedMap.put("name2", "content2");

        testCreateMapFromPolicyValues(policyValue, expectedMap);
    }

    public void testSingleValueSingleValueMultipleValue() {
        String policyValue = "name=content,name2=content2,name1=\"a,b,c\"";

        Map expectedMap = new HashMap();
        expectedMap.put("name", "content");
        expectedMap.put("name2", "content2");
        expectedMap.put("name1", "a,b,c");

        testCreateMapFromPolicyValues(policyValue, expectedMap);
    }

    public void testMultipleValueAttributeNotInQuotes() {
        // Attributes with multiple values that are not quoted will
        // be ignored.
        String policyValue = "name=a,b,c";
        Map expectedMap = new HashMap();
        testCreateMapFromPolicyValues(policyValue, expectedMap);
    }

    public void testSingleValueMultipleValueAttributeNotInQuotes() {
        String policyValue = "name=content,name=a,b,c";

        // If attributes have multiple values that are not quoted, the
        // all defined attributes will be ignored.
        Map expectedMap = new HashMap();
        testCreateMapFromPolicyValues(policyValue, expectedMap);
    }

    public void testMultipleValueAttributeNotInQuotesSingleValue() {
        String policyValue = "name=a,b,c,name2=content2";

        // If attributes have multiple values that are not quoted, the
        // all defined attributes will be ignored.
        Map expectedMap = new HashMap();
        testCreateMapFromPolicyValues(policyValue, expectedMap);
    }

    private void testCreateMapFromPolicyValues(String policyValues,
                                               Map expectedMap) {
        DevicePolicyValueUtil policyValueUtil = new DevicePolicyValueUtil();
        Map actualPolicyValueMap = policyValueUtil.
                createMapFromPolicyValues(policyValues);

        assertEquals("Maps should be equal.",
                     expectedMap,
                     actualPolicyValueMap);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10785/1	ianw	VBM:2005071309 Ported forward meta changes

 22-Jul-05	9117/1	rgreenall	VBM:2005071309 Merge from 323: Support device specific META elements.

 20-Jul-05	9085/3	rgreenall	VBM:2005061306 Add support for adding device specific meta elements - rework

 20-Jul-05	9085/1	rgreenall	VBM:2005061306 Add support for adding device specific meta elements

 ===========================================================================
*/
