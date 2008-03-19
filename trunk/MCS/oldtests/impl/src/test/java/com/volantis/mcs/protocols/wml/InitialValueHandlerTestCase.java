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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/wml/InitialValueHandlerTestCase.java,v 1.4 2003/04/16 10:23:22 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Jan-03    Doug            VBM:2003012408 - Created to test the 
 *                              InitailValueHandler class.
 * 29-Jan-03    Doug            VBM:2003012801 - Modified the 
 *                              testGetInitialValues() method to ensure that
 *                              null is returned if no options are selected.
 * 17-Apr-03    Geoff           VBM:2003041505 - Commented out System.out 
 *                              calls which clutter the JUnit console output.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.wml;

import junit.framework.*;

import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.protocols.SelectOptionGroup;
import com.volantis.mcs.protocols.SelectOption;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.wml.InitialValueHandler;
import java.lang.String;

/**
 * This class unit test the InitialValueHandlerclass.
 */
public class InitialValueHandlerTestCase 
    extends TestCase {

    public InitialValueHandlerTestCase(String name) {
        super(name);
    }

    /**
     * This method tests the constructors for
     * the com.volantis.mcs.protocols.wml.InitialValueHandler class.
     */
    public void noTestConstructors() {
        //
        // Test public InitialValueHandler ( ) constructor
        //
        Assert.fail("public InitialValueHandler ( ) not tested.");
    }


    /**
     * Factory method for creating a SelectOption
     * @param value the value of the option
     * @return the new SelectOption instance
     */
    private SelectOption createOption(String value) {
        SelectOption option = new SelectOption();
        option.setValue(value);
        return option;
    }

    /**
     * Factory method for creatin a SelectOptionGroup
     * @param label the label
     * @return the new SelectOptionGroup instance
     */
    private SelectOptionGroup createOptionGroup(String label) {
        SelectOptionGroup optionGroup = new SelectOptionGroup();
        optionGroup.setCaption(new LiteralTextAssetReference(label));
        return optionGroup;
    }

    /**
     * This method tests the method public String 
     * getInitialValues( XFSelectAttributes )
     * for the com.volantis.mcs.protocols.wml.InitialValueHandler class.
     */
    public void testGetInitialValues()
        throws Exception {

        XFSelectAttributes attributes = new XFSelectAttributes();
        attributes.setName("name");
        attributes.setMultiple(true);



        // create a opt / optgroup
        // <optgroup label="1">
        //   <option value="A"/>
        //   <optgroup label="2">
        //     <option value="B"/>
        //     <option value="C"/>
        //   </optgroup>
        //   <optgroup label="3"/>
        //     <option value="D" selected="true"/>
        //     <option value="E" selected="true"/>
        //   </optgroup>
        //   <optgroup label="4">
        //     <option value="F"/>
        //     <option value="G"/>
        //   </optgroup>
        //   <option value="H" selected="true"/>
        // </optgoup>


        // group 1
        SelectOptionGroup group1 = createOptionGroup("1");
        // option A
        group1.addSelectOption(createOption("A"));
        // Option B
        SelectOption B = createOption("B");
        //option C
        SelectOption C = createOption("C");
        // group 2
        SelectOptionGroup group2 = createOptionGroup("2");
        group2.addSelectOption(B);
        group2.addSelectOption(C);
        group1.addSelectOptionGroup(group2);
        // option D
        SelectOption D = createOption("D");
        D.setSelected(true);
        // option E
        SelectOption E = createOption("E");
        E.setSelected(true);
        // group 3
        SelectOptionGroup group3 = createOptionGroup("3");
        group3.addSelectOption(D);
        group3.addSelectOption(E);
        group1.addSelectOptionGroup(group3);
        // option F
        SelectOption F = createOption("F");
        // option G
        SelectOption G = createOption("G");
        // group 4
        SelectOptionGroup group4 = createOptionGroup("4");
        group4.addSelectOption(F);
        group4.addSelectOption(G);
        group1.addSelectOptionGroup(group4);

        // option H
        SelectOption H = createOption("H");
        H.setSelected(true);
        group1.addSelectOption(H);

        // add group1 to the select attributes
        attributes.addOptionGroup(group1);

        InitialValueHandler handler = new InitialValueHandler();
        String initialValues = handler.getInitialValue(attributes);

        //System.out.println("initial values are " + initialValues);
        assertEquals("initial value should be 4;5;8",
                     "4;5;8", initialValues);

        // test single select
        attributes.setMultiple(false);
        attributes.setInitial("D");
        initialValues = handler.getInitialValue(attributes);

        //System.out.println("initial values are " + initialValues);
        assertEquals("initial value should be 4",
                     "4", initialValues);
        
        // test that null is returned when no options are selected
        initialValues = handler.getInitialValue(new XFSelectAttributes());
        assertNull("Initial Value should be null", initialValues);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
