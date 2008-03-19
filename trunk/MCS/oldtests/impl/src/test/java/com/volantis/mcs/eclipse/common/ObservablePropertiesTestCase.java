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
package com.volantis.mcs.eclipse.common;

import com.volantis.mcs.utilities.BooleanObject;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * A test case for ObservableProperties.
 */
public class ObservablePropertiesTestCase extends TestCaseAbstract {

    // javadoc inherited
    public ObservablePropertiesTestCase(String name) {
        super(name);
    }

    /**
     * Tests that the ObservableProperties correctly delegates to its
     * Properties object, that is, behaves as a Properties object.
     * @throws Exception
     */
    public void testProperties() throws Exception {

        Properties props = new Properties();
        props.setProperty("aProp", "one");
        props.setProperty("bProp", "two");
        props.setProperty("cProp", "three");
        props.setProperty("dProp", "four");
        props.setProperty("bProp", "five");
        props.setProperty("eProp", "six");

        ObservableProperties observableProps = new ObservableProperties(props);

        assertTrue(observableProps.getProperty("aProp").equals("one"));
        assertTrue(observableProps.getProperty("bProp").equals("five"));
        assertTrue(observableProps.getProperty("cProp").equals("three"));
        assertTrue(observableProps.getProperty("dProp").equals("four"));
        assertTrue(observableProps.getProperty("eProp").equals("six"));

        Enumeration enumeration = observableProps.propertyNames();

        List propNames = new ArrayList();
        while (enumeration.hasMoreElements()) {
            propNames.add(enumeration.nextElement());
        }

        assertEquals(propNames.size(), 5);

        assertTrue(propNames.contains("aProp"));
        assertTrue(propNames.contains("bProp"));
        assertTrue(propNames.contains("cProp"));
        assertTrue(propNames.contains("dProp"));
        assertTrue(propNames.contains("eProp"));
    }

    /**
     * Tests the PropertyChangeListener mechanism.
     * @throws Exception
     */
    public void testListener() throws Exception {
        final BooleanObject result1 = new BooleanObject();
        final BooleanObject result2 = new BooleanObject();

        Properties props = new Properties();
        props.setProperty("testProp1", "testValue1");
        props.setProperty("testProp2", "testValue2");

        ObservableProperties obsProps = new ObservableProperties(props);

        PropertyChangeListener listener1 = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                result1.setValue(true);
            }
        };

        PropertyChangeListener listener2 = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                result2.setValue(true);
            }
        };

        obsProps.addPropertyChangeListener("testProp1", listener1);
        obsProps.addPropertyChangeListener("testProp2", listener2);

        obsProps.setProperty("testProp1", "newValue1");
        obsProps.setProperty("testProp", "newValue2");

        assertTrue(result1.getValue());
        assertTrue(obsProps.getProperty("testProp1").equals("newValue1"));
        
        assertFalse(result2.getValue());
        assertTrue(obsProps.getProperty("testProp2").equals("testValue2"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Mar-04	3568/4	pcameron	VBM:2004032105 Added test case for ObservableProperties PropertyChangeListeners

 25-Mar-04	3568/1	pcameron	VBM:2004032105 Added ObservableProperties and refactored XMLDeviceRepositoryAccessor and DeviceRepositoryAccessorManager to use Properties

 ===========================================================================
*/
