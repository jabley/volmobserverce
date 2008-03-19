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
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilder;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuEntry;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuItemGroup;
import com.volantis.mcs.protocols.menu.model.MenuModelVisitor;
import com.volantis.mcs.protocols.menu.shared.builder.ConcreteMenuModelBuilder;
import com.volantis.mcs.protocols.menu.shared.model.AbstractMenuModelHandler;
import com.volantis.mcs.runtime.OutputBufferResolver;
import com.volantis.styling.StylesBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests {@link DefaultMenuBufferLocator}.
 */
public class DefaultMenuBufferLocatorTestCase
        extends MenuBufferLocatorTestAbstract {
    /**
     * A command interface, an instance of which can be passed to the
     * {@link DefaultMenuBufferLocatorTestCase#createMenuGroup} methods in order to populate the defined
     * group. If none is given, a standard single menu item group will
     * be created instead.
     */
    public static interface GroupBuilder {
        /**
         * Called to populate the group. At least one menu item must be
         * created.
         *
         * @param builder the builder that should be called to create and
         *                populate the required menu items
         */
        void build(MenuModelBuilder builder) throws Exception;
    }

    /**
     * Maps MenuEntries to the FormatReferences that they are
     * allocated to. This must be populated by the tested MenuBufferLocator.
     */
    Map formatReferences = new HashMap();

    /**
     * Used to create the actual menu buffer locator to be tested. This must be
     * such that it can populate the {@link #formatReferences} map.
     *
     * @return an appropriate menu buffer locator
     */
    protected MenuBufferLocator createMenuBufferLocator() {
        return new DefaultMenuBufferLocator(createOutputBufferResolver(),
                createMenuBufferFactory()) {
            /**
             * Augmented to record the mapping created by the locator between
             * a menu entry and its allocated format reference.
             */
            protected FormatReference findFormatReferenceFor(MenuEntry entry) {
                FormatReference ref = super.findFormatReferenceFor(entry);

                formatReferences.put(entry, ref);

                return ref;
            }
        };
    }

    /**
     * Permits the tests to use different output buffer resolvers.
     *
     * @return an Output Buffer Resolver appropriate to the testing
     */
    protected OutputBufferResolver createOutputBufferResolver() {
        return new OutputBufferResolver() {
            // javadoc inherited
            public OutputBuffer resolvePaneOutputBuffer(
                    FormatReference paneFormatReference) {
                return createOutputBufferImpl();
            }
        };
    }

    /**
     * Permits the tests to use different output buffer implementations. This
     * is invoked by the default Output Buffer Resolver.
     *
     * @return an Output Buffer appropriate to the testing
     */
    protected OutputBuffer createOutputBufferImpl() {
        return new TestDOMOutputBuffer();
    }

    /**
     * Permits the tests to use different menu buffer factories.
     *
     * @return an Menu Buffer Factory appropriate to the testing
     */
    private MenuBufferFactory createMenuBufferFactory() {
        return new MenuBufferFactory() {
            // javadoc inherited
            public MenuBuffer createMenuBuffer(MenuEntry entry,
                                               FormatReference formatReference,
                                               OutputBuffer outputBuffer) {
                return createMenuBufferImpl(outputBuffer);
            }
        };
    }

    /**
     * Permits the tests to use different menu buffer implementations. This
     * is invoked by the default Menu Buffer Factory.
     *
     * @param buffer the output buffer to be managed by the menu buffer
     * @return a Menu Buffer appropriate to the testing
     */
    protected MenuBuffer createMenuBufferImpl(OutputBuffer buffer) {
        return new ConcreteMenuBuffer(buffer, null);
    }

    /**
     * Tests {@link DefaultMenuBufferLocator#getMenuBuffer} with a menu
     * that is being auto iteration allocated containing menu items, groups
     * and sub-menus.
     */
    public void testGetMenuBufferMainMenuAuto() throws Exception {
        MenuBufferLocator locator = createMenuBufferLocator();

        MenuModelBuilder builder = new ConcreteMenuModelBuilder();
        Menu menu;

        builder.startMenu();

        // Make the menu automatic allocate and ensure that the pane is
        // incompletely specified to ensure automatic iteration allocation
        builder.setElementDetails("menu", null,
                StylesBuilder.getCompleteStyles(
                        "mcs-menu-item-iterator-allocation: automatic"));

        builder.setPane(new FormatReference("pane",
                                            new NDimensionalIndex(
                                                    new int[]{1, 2, 0},
                                                    2)));

        createMenuItem(builder, "item1");
        createMenuItem(builder, "item2");
        createMenuGroup(builder);
        createMenuItem(builder, "item3",
                       new FormatReference("other",
                                           new NDimensionalIndex(
                                                   new int[]{0},
                                                   0)));

        builder.startMenu();
        createMenuItem(builder, "item4.1");
        builder.endMenu();

        menu = builder.endMenu();

        checkAllocations(
                menu,
                locator,
                new FormatReference[]{
                    fr("pane", new int[]{1, 2, 0}), // menu
                    fr("pane", new int[]{1, 2, 0}), // item1
                    fr("pane", new int[]{1, 2, 1}), // item2
                    fr("pane", new int[]{1, 2, 2}), // group
                    fr("pane", new int[]{1, 2, 2}), // group groupitem1
                    fr("other", new int[]{0}), // item3
                    fr("pane", new int[]{1, 2, 3}), // sub-menu
                    fr("pane", new int[]{1, 2, 3}), // sub-menu item4.1
                });
    }

    /**
     * Tests {@link DefaultMenuBufferLocator#getMenuBuffer} with a menu that is
     * not being auto iteration allocated (because its pane is fully specified)
     * containing menu items, groups and sub-menus.
     */
    public void testGetMenuBufferMainMenuFullySpec() throws Exception {
        MenuBufferLocator locator = createMenuBufferLocator();

        MenuModelBuilder builder = new ConcreteMenuModelBuilder();
        Menu menu;

        builder.startMenu();

        // Make the menu automatic allocate and ensure that the pane is
        // fully specified to ensure no automatic iteration allocation
        builder.setElementDetails("menu", null,
                StylesBuilder.getCompleteStyles(
                        "mcs-menu-item-iterator-allocation: automatic"));

        builder.setPane(new FormatReference("pane",
                                            new NDimensionalIndex(
                                                    new int[] {1, 2, 0})));

        createMenuItem(builder, "item1");
        createMenuItem(builder, "item2");
        createMenuGroup(builder);
        createMenuItem(builder, "item3",
                       new FormatReference("other",
                                           new NDimensionalIndex(
                                                   new int[] {0},
                                                   0)));

        builder.startMenu();
        createMenuItem(builder, "item4.1");
        builder.endMenu();

        menu = builder.endMenu();

        checkAllocations(
                menu,
                locator,
                new FormatReference[] {
                    fr("pane", new int[] {1, 2, 0}), // menu
                    fr("pane", new int[] {1, 2, 0}), // item1
                    fr("pane", new int[] {1, 2, 0}), // item2
                    fr("pane", new int[] {1, 2, 0}), // group
                    fr("pane", new int[] {1, 2, 0}), // group groupitem1
                    fr("other", new int[] {0}), // item3
                    fr("pane", new int[] {1, 2, 0}), // sub-menu
                    fr("pane", new int[] {1, 2, 0}), // sub-menu item4.1
                });
    }

    /**
     * Tests {@link DefaultMenuBufferLocator#getMenuBuffer} with a menu that is
     * not being auto iteration allocated (because its iteration allocation
     * style is "none") containing menu items, groups and sub-menus.
     */
    public void testGetMenuBufferMainMenuNoAuto() throws Exception {
        MenuBufferLocator locator = createMenuBufferLocator();

        MenuModelBuilder builder = new ConcreteMenuModelBuilder();
        Menu menu;

        builder.startMenu();

        // Make the menu manually allocate and ensure that the pane is
        // incompletely specified to ensure no automatic iteration allocation
        builder.setElementDetails("menu", null,
                StylesBuilder.getCompleteStyles(
                        "mcs-menu-item-iterator-allocation: none"));

        builder.setPane(new FormatReference("pane",
                                            new NDimensionalIndex(
                                                    new int[]{1, 2, 0},
                                                    2)));

        createMenuItem(builder, "item1");
        createMenuItem(builder, "item2");
        createMenuGroup(builder);
        createMenuItem(builder, "item3",
                       new FormatReference("other",
                                           new NDimensionalIndex(
                                                   new int[]{0},
                                                   0)));

        builder.startMenu();
        createMenuItem(builder, "item4.1");
        builder.endMenu();

        menu = builder.endMenu();

        checkAllocations(
                menu,
                locator,
                new FormatReference[]{
                    fr("pane", new int[]{1, 2, 0}), // menu
                    fr("pane", new int[]{1, 2, 0}), // item1
                    fr("pane", new int[]{1, 2, 0}), // item2
                    fr("pane", new int[]{1, 2, 0}), // group
                    fr("pane", new int[]{1, 2, 0}), // group groupitem1
                    fr("other", new int[]{0}), // item3
                    fr("pane", new int[]{1, 2, 0}), // sub-menu
                    fr("pane", new int[]{1, 2, 0}), // sub-menu item4.1
                });
    }

    /**
     * Tests {@link DefaultMenuBufferLocator#getMenuBuffer} with a menu that is
     * being auto iteration allocated with a sub-menu also being auto iterated.
     */
    public void testGetMenuBufferBothMainMenuAndSubMenuAuto() throws Exception {
        MenuBufferLocator locator = createMenuBufferLocator();

        MenuModelBuilder builder = new ConcreteMenuModelBuilder();
        Menu menu;

        builder.startMenu();

        // Make the menu automatic allocate and ensure that the pane is
        // incompletely specified to ensure automatic iteration allocation
        builder.setElementDetails("menu", null,
                StylesBuilder.getCompleteStyles(
                        "mcs-menu-item-iterator-allocation: automatic"));

        builder.setPane(new FormatReference("pane",
                                            new NDimensionalIndex(
                                                    new int[]{1, 2, 0},
                                                    2)));

        createMenuItem(builder, "item1");

        builder.startMenu();

        // Make this menu automatic allocate and set the pane as incompletely
        // specified to enable automatic iteration allocation
        builder.setElementDetails("submenu", null,
                StylesBuilder.getCompleteStyles(
                        "mcs-menu-item-iterator-allocation: automatic"));

        builder.setPane(new FormatReference("sub",
                                            new NDimensionalIndex(
                                                    new int[]{0, 0, 0},
                                                    0)));

        createMenuItem(builder, "item2.1");
        createMenuItem(builder, "item2.2");
        createMenuItem(builder, "item2.3",
                       fr("other", new int[]{3, 1, 0}));
        createMenuGroup(builder, new GroupBuilder() {
            public void build(MenuModelBuilder builder) throws Exception {
                createMenuItem(builder, "submenu group item1");
                createMenuItem(builder, "submenu group item2");
            }
        });
        builder.endMenu();

        menu = builder.endMenu();

        checkAllocations(
                menu,
                locator,
                new FormatReference[]{
                    fr("pane", new int[]{1, 2, 0}), // menu
                    fr("pane", new int[]{1, 2, 0}), // item1
                    fr("sub", new int[]{0, 0, 0}), // sub-menu
                    fr("sub", new int[]{0, 0, 0}), // sub-menu item2.1
                    fr("sub", new int[]{0, 0, 1}), // sub-menu item2.2
                    fr("other", new int[]{3, 1, 0}), // sub-menu item2.1
                    fr("sub", new int[]{0, 0, 2}), // submenu group
                    fr("sub", new int[]{0, 0, 2}), // submenu group item1
                    fr("sub", new int[]{0, 0, 2}), // submenu group item2
                });
    }

    /**
     * Checks that the menu buffer returned is consistently returned as the
     * same buffer for a given menu entry and that menu entries with the same
     * format reference get the same menu buffer.
     */
    public void testGetMenuBufferConsistent() throws Exception {
        final MenuBufferLocator locator = createMenuBufferLocator();

        MenuModelBuilder builder = new ConcreteMenuModelBuilder();
        Menu menu;

        builder.startMenu();

        // Make the menu automatic allocate and ensure that the pane is
        // incompletely specified to ensure automatic iteration allocation
        builder.setElementDetails("menu", null,
                StylesBuilder.getCompleteStyles(
                        "mcs-menu-item-iterator-allocation: automatic"));

        builder.setPane(new FormatReference("pane",
                                            new NDimensionalIndex(
                                                    new int[]{1, 2, 0},
                                                    2)));

        createMenuItem(builder, "item1");

        builder.startMenu();

        createMenuItem(builder, "item2.1");
        createMenuItem(builder, "item2.2");

        builder.endMenu();

        menu = builder.endMenu();

        final Map buffers = new HashMap();

        MenuModelVisitor setupMapVisitor = new AbstractMenuModelHandler() {
            // javadoc inherited
            protected boolean handle(MenuItem item) {
                buffers.put(item, locator.getMenuBuffer(item));

                return false;
            }

            // javadoc inherited
            protected boolean handle(MenuItemGroup group) {
                buffers.put(group, locator.getMenuBuffer(group));

                return true;
            }

            // javadoc inherited
            protected boolean handle(Menu menu) {
                buffers.put(menu, locator.getMenuBuffer(menu));

                return true;
            }
        };

        setupMapVisitor.visit(menu);

        MenuModelVisitor checkVisitor = new AbstractMenuModelHandler() {
            // javadoc inherited
            protected boolean handle(MenuItem item) {
                assertSame("item's buffer not as",
                           buffers.get(item),
                           locator.getMenuBuffer(item));

                return true;
            }

            // javadoc inherited
            protected boolean handle(MenuItemGroup group) {
                assertSame("group's buffer not as",
                           buffers.get(group),
                           locator.getMenuBuffer(group));

                return true;
            }

            // javadoc inherited
            protected boolean handle(Menu menu) {
                assertSame("menu's buffer not as",
                           buffers.get(menu),
                           locator.getMenuBuffer(menu));

                return false;
            }
        };

        checkVisitor.visit(menu);

        assertSame("menu and first child should share same buffer",
                   buffers.get(menu),
                   buffers.get(menu.get(0)));

        assertNotSame("menu and second child should not share same buffer",
                      buffers.get(menu),
                      buffers.get(menu.get(1)));

        assertSame("sub-menu and its first child should share same buffer",
                   buffers.get(menu.get(1)),
                   buffers.get(((Menu)menu.get(1)).get(0)));

        assertSame("sub-menu and its second child should share same buffer",
                   buffers.get(menu.get(1)),
                   buffers.get(((Menu)menu.get(1)).get(1)));
    }

    /**
     * Used to create a FormatReference populated with the specified values.
     *
     * @param stem the base name for the format being targeted
     * @param indices the indices for the format reference's instance
     * @return a fully specified format reference
     */
    protected FormatReference fr(String stem, int[] indices) {
        return new FormatReference(stem, new NDimensionalIndex(indices));
    }

    /**
     * Used to generate a string representation of an integer array
     *
     * @param indices the integers that need to be output to the string
     * @return a string of the integer array
     */
    protected String toString(int[] indices) {
        StringBuffer buffer = new StringBuffer("[");

        for (int i = 0; i < indices.length; i++) {
            buffer.append(indices[i]);

            if (i < (indices.length - 1)) {
                buffer.append(",");
            }
        }

        return buffer.append("]").toString();
    }

    /**
     * This method is used instead of testing equality between two format
     * references because we're not interested in the "specified" count in
     * the format reference's instance (this is not relevant).
     *
     * @param expectedObject the expected format reference as an object
     * @param actual the actual format reference
     */
    protected void checkSame(Object expectedObject,
                             FormatReference actual) {
        FormatReference expected = (FormatReference)expectedObject;

        if (expected == null) {
            assertNull("actual should be null", actual);
        } else {
            assertNotNull("actual should not be null",
                          actual);

            assertEquals("stems not as",
                         expected.getStem(),
                         actual.getStem());

            if (expected.getIndex() == null) {
                assertNull("actual instance should be null",
                           actual.getIndex());
            } else {
                assertNotNull("actual instance should not be null",
                              actual.getIndex());

                assertTrue("indices don't match (expected: " +
                           toString(expected.getIndex().getIndicies()) +
                           ") (actual: " +
                           toString(actual.getIndex().getIndicies()) + ")",
                           Arrays.equals(expected.getIndex().getIndicies(),
                                         actual.getIndex().getIndicies()));
            }
        }
    }

    /**
     * A helper method used to check that the given menu and its content is
     * given the right set of format references. The specified locator is
     * called in an ordered, pre-traversal, depth-first manner to allocate the
     * format references. These references are compared, long-hand (so as to
     * ignore the "specified" value), with the given references. The latter
     * must therefore be provided in the correct order.
     *
     * @param menu       the menu for which allocations are to be checked
     * @param locator    the locator to be used to perform the allocation
     * @param references the set of expected references; must be in the order
     *                   that the allocations are performed in this method
     *                   (ordered, pre-traversal depth-first)
     */
    protected void checkAllocations(Menu menu,
                                    final MenuBufferLocator locator,
                                    final FormatReference[] references)
            throws Exception {
        /**
         * Used to provide access to the actual number of menu entries visited
         */
        abstract class CountedVisitor extends AbstractMenuModelHandler {
            /**
             * Returns the number of menu entries that have been visited.
             *
             * @return the number of menu entries that have been visited
             */
            public abstract int getCount();
        }

        CountedVisitor visitor = new CountedVisitor() {
            /**
             * Tracks the next reference to be compared.
             */
            int i = 0;

            // javadoc inherited
            public int getCount() {
                return i;
            }

            // javadoc inherited
            protected boolean handle(MenuItem item) {
                locator.getMenuBuffer(item);
                checkSame(formatReferences.get(item), references[i]);

                i++;
                return false;
            }

            // javadoc inherited
            protected boolean handle(MenuItemGroup group) {
                locator.getMenuBuffer(group);
                checkSame(formatReferences.get(group), references[i]);

                i++;
                return true;
            }

            // javadoc inherited
            protected boolean handle(Menu menu) {
                locator.getMenuBuffer(menu);
                checkSame(formatReferences.get(menu), references[i]);

                i++;
                return true;
            }
        };

        visitor.visit(menu);

        assertEquals("Number of menu entries processed not as",
                     references.length,
                     visitor.getCount());
    }

    /**
     * Supporting method that creates a single-item menu group using the given
     * builder.
     *
     * @param builder the builder in which a single item menu group is to be
     *                created
     * @throws Exception if the builder is not in the right state for creating
     *                   a menu item group
     */
    protected void createMenuGroup(MenuModelBuilder builder) throws Exception {
        createMenuGroup(builder, null);
    }

    /**
     * Supporting method that creates a menu item group and populates it with
     * arbitrary content using the given GroupBuilder instance. If the
     * GroupBuilder is null, a single menu item is created in the group
     * instead.
     *
     * @param builder the builder in which the menu group is to be created
     * @param group   the GroupBuilder used to populate the menu group, or
     *                null
     * @throws Exception if the builder is not in the right state for creating
     *                   a menu item group or if the GroupBuilder defines
     *                   invalid menu group content
     */
    protected void createMenuGroup(MenuModelBuilder builder,
                                   GroupBuilder group) throws Exception {
        createMenuGroup(builder, group, null);
    }

    /**
     * Supporting method that creates a menu item group and populates it with
     * arbitrary content using the given GroupBuilder instance. If the
     * GroupBuilder is null, a single menu item is created in the group
     * instead.
     *
     * @param builder the builder in which the menu group is to be created
     * @param group   the GroupBuilder used to populate the menu group, or
     *                null
     * @param pane    the format reference defining the pane to which the menu
     *                group will be assigned
     * @throws Exception if the builder is not in the right state for creating
     *                   a menu item group or if the GroupBuilder defines
     *                   invalid menu group content
     */
    protected void createMenuGroup(MenuModelBuilder builder,
                                   GroupBuilder group,
                                   FormatReference pane) throws Exception {
        builder.startMenuGroup();

        if (pane != null) {
            builder.setPane(pane);
        }

        if (group != null) {
            group.build(builder);
        } else {
            createMenuItem(builder, "groupitem1");
        }

        builder.endMenuGroup();
    }

    /**
     * Supporting method that creates a menu item, populating it with the
     * specified text.
     *
     * @param builder the builder in which the menu item is to be created
     * @param text    the text for the menu item
     * @throws Exception if the builder is not in the right state for creating
     *                   a menu item
     */
    protected void createMenuItem(MenuModelBuilder builder,
                                  String text) throws Exception {
        createMenuItem(builder, text, null);
    }

    /**
     * Supporting method that creates a menu item, populating it with the
     * specified text.
     *
     * @param builder the builder in which the menu item is to be created
     * @param text    the text for the menu item
     * @param pane    the format reference identifying the pane to which the
     *                menu item will be assigned
     * @throws Exception if the builder is not in the right state for creating
     *                   a menu item
     */
    protected void createMenuItem(MenuModelBuilder builder,
                                  String text,
                                  FormatReference pane) throws Exception{
        DOMOutputBuffer buffer = new TestDOMOutputBuffer();

        buffer.appendEncoded(text);

        builder.startMenuItem();

        builder.setHref(new LiteralLinkAssetReference("href"));

        if (pane != null) {
            builder.setPane(pane);
        }

        builder.startLabel();
        builder.startText();
        builder.setText(buffer);
        builder.endText();
        builder.endLabel();
        builder.endMenuItem();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 23-Jun-05	8483/5	emma	VBM:2005052410 Modifications after review

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Mar-05	7022/1	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 12-May-04	4279/2	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 06-May-04	3999/4	philws	VBM:2004042202 Handle automatic iteration allocation in Menus

 ===========================================================================
*/
