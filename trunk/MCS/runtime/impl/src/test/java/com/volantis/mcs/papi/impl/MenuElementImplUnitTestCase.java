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
package com.volantis.mcs.papi.impl;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilderMock;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilder;
import com.volantis.mcs.protocols.ShortcutProperties;
import com.volantis.styling.StylesMock;
import com.volantis.styling.PseudoElements;
import com.volantis.styling.Styles;
import com.volantis.styling.NestedStylesMock;
import com.volantis.styling.values.MutablePropertyValuesMock;
import junitx.util.PrivateAccessor;

import java.util.List;
import java.util.ArrayList;

/**
 * Unit test for the MenuElementImpl class.
 */
public class MenuElementImplUnitTestCase extends TestCaseAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * The parameter classes used for the private access call to
     * setShortcutPropertiesOnBuilder.
     */
    private static final Class[]
            SET_SHORTCUT_PROPERTIES_ON_BUILDER_PARAM_CLASSES =
            { MarinerPageContext.class,
              MenuModelBuilder.class,
              Styles.class};

    /**
     * Tests the setShortcutPropertiesOnBuilder method of MenuElementImpl.
     * @throws Throwable if a problem occurs.
     */
    public void testSetShortcutPropertiesOnBuilder() throws Throwable {

        // set up the mocks
        MarinerPageContextMock pageContext =
                new MarinerPageContextMock("pageContext", expectations);

        MenuModelBuilderMock menuModelBuilder =
                new MenuModelBuilderMock("menuModelBuilder",
                                         expectations);

        StylesMock elementMock = new StylesMock("elementMock",
                                                expectations);

        final NestedStylesMock shortcutMock =
                new NestedStylesMock("shortcutMock", expectations);

        final NestedStylesMock afterMock =
                new NestedStylesMock("afterMock", expectations);

        MutablePropertyValuesMock mutablePropertyValuesMock =
                new MutablePropertyValuesMock("mutablePropertyValuesMock",
                                              expectations);

        List styles = new ArrayList();
        styles.add(STYLE_VALUE_FACTORY.getString(null, "testvalue"));
        StyleList styleList = STYLE_VALUE_FACTORY.getList(styles);

        // setup expectations
        elementMock.expects.findNestedStyles(PseudoElements.MCS_SHORTCUT).
                returns(shortcutMock);
        shortcutMock.expects.findNestedStyles(PseudoElements.AFTER).
                returns(afterMock);
        afterMock.expects.getPropertyValues().
                returns(mutablePropertyValuesMock);
        mutablePropertyValuesMock.expects.
                getComputedValue(StylePropertyDetails.CONTENT).
                returns(styleList);
        menuModelBuilder.fuzzy.setShortcutProperties(mockFactory.
                expectsInstanceOf(ShortcutProperties.class));

        // test the method
        MenuElementImpl menuElement = new MenuElementImpl();
        PrivateAccessor.invoke(menuElement,
                               "setShortcutPropertiesOnBuilder",
                               SET_SHORTCUT_PROPERTIES_ON_BUILDER_PARAM_CLASSES,
                               new Object[]{ pageContext,
                                             menuModelBuilder,
                                             elementMock});
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 25-Aug-05	9377/1	schaloner	VBM:2005071102 Migrated mcs-shortcut-after to mcs-shortcut and after

 ===========================================================================
*/
