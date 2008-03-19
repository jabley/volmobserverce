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
package com.volantis.mcs.objects;

import com.volantis.mcs.assets.AssetGroup;
import com.volantis.mcs.assets.TextAsset;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * This class tests PropertyValueLookup.
 */
public class PropertyValueLookUpTestCase extends TestCase {

    public PropertyValueLookUpTestCase(String name) {
        super(name);
    }


    /**
     * This tests the method getXMLAttributeName(Class, String)
     */
    public void testGetXMLAttributeName() throws Exception {
        String name = PropertyValueLookUp.getXMLAttributeName("chartAsset", "xTitle");
        assertEquals(name, "XTitle");
        name = PropertyValueLookUp.getXMLAttributeName("chartAsset", "XTitle");
        assertEquals(name, "XTitle");
        name = PropertyValueLookUp.getXMLAttributeName("chartAsset", "yInterval");
        assertEquals(name, "YInterval");
        name = PropertyValueLookUp.getXMLAttributeName("chartAsset", "heightHint");
        assertEquals(name, "heightHint");
        name = PropertyValueLookUp.getXMLAttributeName("chartAsset", "blahblah");
        assertEquals(name, "blahblah");
    }

    /**
     * This tests the method getPropertyArray(String elementName, String attributeName)
     */
    public void testGetPropertyArray() throws Exception {
        AssetGroup assetGroup = new AssetGroup();
        Object[] expectedArray = PropertyValueLookUp.getPropertyArray(assetGroup.getClass(), "cacheThisPolicy");
        assertNotNull(expectedArray);
        Object[] testArray = PropertyValueLookUp.getPropertyArray("assetGroup", "cacheThisPolicy");
        assertNotNull(testArray);
        assertEquals(expectedArray.length, testArray.length);
        for (int i = 0; i < expectedArray.length; i++) {
            assertEquals(expectedArray[i], testArray[i]);
        }
    }

    /**
     * This tests the method getExternalPropertyArray(String elementName, String attributeName)
     */
    public void testGetExternalPropertyArray() throws Exception {
        AssetGroup assetGroup = new AssetGroup();
        Object[] expectedArray = PropertyValueLookUp.getExternalPropertyArray(assetGroup.getClass(), "cacheThisPolicy");
        assertNotNull(expectedArray);
        Object[] testArray = PropertyValueLookUp.getExternalPropertyArray("assetGroup", "cacheThisPolicy");
        assertNotNull(testArray);
        assertEquals(expectedArray.length, testArray.length);
        for (int i = 0; i < expectedArray.length; i++) {
            assertEquals(expectedArray[i], testArray[i]);
        }
    }

    /**
     * This tests the method getPropertyValue(String elementName, String attributeName, Object key)
     */
    public void testGetPropertyValue() throws Exception {
        TextAsset textAsset = new TextAsset();
        Object expectedValue = PropertyValueLookUp.getPropertyValue(textAsset.getClass(), "valueType", new Integer(1));
        assertNotNull(expectedValue);
        Object testValue = PropertyValueLookUp.getPropertyValue("textAsset", "valueType", new Integer(1));
        assertNotNull(testValue);
        assertEquals(expectedValue, testValue);
    }

    /**
     * This tests the method getPropertyName(String elementName, String attributeName)
     */
    public void testGetPropertyName() throws Exception {
        String propName = PropertyValueLookUp.getPropertyName("chartAsset", "XTitle");
        assertEquals(propName, "xTitle");
        propName = PropertyValueLookUp.getPropertyName("chartAsset", "YTitle");
        assertEquals(propName, "yTitle");
    }

    /**
     * This tests the method getDependentElements(String elementName)
     */
    public void testGetDependentElements() throws Exception {
        Map dependencyMap = new HashMap();
        List deps;
        deps = new Vector();
        deps.add("textAsset");
        dependencyMap.put("textComponent", deps);
        deps = new Vector();
        deps.add("scriptAsset");
        dependencyMap.put("scriptComponent", deps);
        deps = new Vector();
        deps.add("linkAsset");
        dependencyMap.put("linkComponent", deps);
        deps = new Vector();
        deps.add("deviceImageAsset");
        deps.add("genericImageAsset");
        deps.add("convertibleImageAsset");
        dependencyMap.put("imageComponent", deps);
        deps = new Vector();
        deps.add("dynamicVisualAsset");
        dependencyMap.put("dynamicVisualComponent", deps);
        deps = new Vector();
        deps.add("chartAsset");
        dependencyMap.put("chartComponent", deps);
        deps = new Vector();
        deps.add("audioAsset");
        deps.add("deviceAudioAsset");
        dependencyMap.put("audioComponent", deps);

        for (Iterator it = dependencyMap.keySet().iterator(); it.hasNext();) {
            Object key = it.next();
            String element = (String) key;
            List expectedList = (List) dependencyMap.get(key);
            assertNotNull("dependencyMap is not properly constructed " +
                    "since there is no list for element: " + element ,
                    expectedList);
            List testList = PropertyValueLookUp.getDependentElements(element);
            assertNotNull("No dependent elements were found for element: " +
                    element, testList);
            assertEquals(expectedList.size(), testList.size());
            // check that list contents are equal
            for (int j = 0; j < expectedList.size(); j++) {
                assertTrue(testList.contains(expectedList.get(j)));
            }
        }

        List testList = PropertyValueLookUp.getDependentElements("blah");
        assertNull(testList);
        testList = PropertyValueLookUp.getDependentElements("Theme");
        assertNull(testList);
        testList = PropertyValueLookUp.getDependentElements(null);
        assertNull(testList);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-May-05	8123/1	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 28-Feb-05	7149/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 28-Feb-05	7127/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Aug-04	5130/1	doug	VBM:2004080310 Added support for null device assets to GUI

 06-Aug-04	5081/5	geoff	VBM:2004080306 Implement Null Assets: JDBC Accessor (remove chart and dynvis)

 05-Aug-04	5081/3	geoff	VBM:2004080306 Implement Null Assets: JDBC Accessor (make it simpler)

 04-Aug-04	5081/1	geoff	VBM:2004080306 Implement Null Assets: JDBC Accessor

 29-Dec-03	2258/1	allan	VBM:2003121725 Make Layout editor and wizard conform to new layout schema

 27-Nov-03	2013/1	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 10-Nov-03	1821/4	pcameron	VBM:2003110401 Added PropertyValueLookUp methods for getting dependents of elements

 03-Nov-03	1698/2	pcameron	VBM:2003102411 Added some new PolicyValue methods and refactored

 ===========================================================================
*/
