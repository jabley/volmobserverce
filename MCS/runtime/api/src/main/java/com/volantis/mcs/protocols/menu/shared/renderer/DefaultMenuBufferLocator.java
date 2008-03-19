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

import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuEntry;
import com.volantis.mcs.protocols.menu.shared.MenuInspector;
import com.volantis.mcs.runtime.OutputBufferResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * This locator automatically resolves the MenuBuffer for the given menu
 * entries using a {@link OutputBufferResolver} to translate pane references to
 * output buffers. This involves handling automatic iteration allocation where
 * necessary.
 *
 * <p>Automatic iteration allocation is handled by determining whether the
 * conditions for this are met by an entry's containing menu (see {@link
 * #getAutoAllocatingMenu} and, if so:</p>
 *
 * <ul>
 *
 * <li>allocate iterations of the menu's format reference for each immediate,
 * untargeted child, storing these in the {@link #allocations} map (see {@link
 * #allocateIterations})</li>
 *
 * <li>when determining a format reference for an entry (in {@link
 * #findFormatReferenceFor}) always check for explicit targeting and, if not
 * available, look to see if an allocation for that entry has been recorded in
 * the <code>allocations</code> map and if so explicitly use this allocation
 * (otherwise, fall back to any container as needed)</li>
 *
 * </ul>
 *
 * <p>Each top-level menu should use a separate instance of this class.</p>
 *
 * <p><strong>NOTE:</strong> Automatic iteration allocation cannot be performed
 * on the top-most menu if that menu is inheriting its pane specification from
 * surrounding markup.</p>
 */
public class DefaultMenuBufferLocator
        implements MenuBufferLocator {
    
    /**
     * Provides access to various methods that can be used to derive specific
     * information about a given menu.
     */
    private final MenuInspector inspector;

    /**
     * Provides access to the method needed to resolve output buffers from pane
     * format references.
     */
    private OutputBufferResolver bufferResolver;

    /**
     * A factory used to create the menu buffers from menu entities and output
     * buffers.
     */
    private MenuBufferFactory factory;

    /**
     * This map is keyed on the pane {@link FormatReference FormatReferences}
     * and provides access to the associated {@link MenuBuffer MenuBuffers}.
     * Note that an entry may be created where the key is null (providing
     * access to the default menu buffer - one used when no pane reference
     * has been supplied). In addition, further entries may be created where
     * the value is null (meaning that the requested pane doesn't exist).
     *
     * <p>This is explicitly allocated since a menu buffer locator will
     * always allocate and track menu buffers.</p>
     */
    private final Map menuBuffers = new HashMap();

    /**
     * This map is keyed on the menu entries within automatic iteration
     * allocating menus and provides access to the allocated {@link
     * FormatReference FormatReferences} for those entries. This covers
     * allocations in all parts of a menu, including sub-menus.
     *
     * <p>This map is used to satisfy the automatic iteration allocation rules
     * such that all immediate children of a given automatic iteration
     * allocation menu that are not explicitly targeted at a pane are allocated
     * sequential iterations of the targeted format iterated pane.</p>
     *
     * <p>This is lazily instantiated by {@link #getAllocations} because not
     * all menus include automatic iteration allocating ones.</p>
     */
    private Map allocations;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param bufferResolver the resolver used to translate pane format
     *                       references to output buffers. May not be null
     * @param factory        the factory used to create and initialize the required
     *                       menu buffers. May not be null
     */
    public DefaultMenuBufferLocator(
            OutputBufferResolver bufferResolver,
            MenuBufferFactory factory) {

        if (bufferResolver == null) {
            throw new IllegalArgumentException("resolver may not be null");
        } else if (factory == null) {
            throw new IllegalArgumentException("factory may not be null");
        }

        this.bufferResolver = bufferResolver;
        this.factory = factory;
        this.inspector = new MenuInspector();
    }

    // javadoc inherited
    public MenuBuffer getMenuBuffer(MenuEntry entry) {
        // Determine the format reference that should be used for the given
        // entry
        FormatReference formatReference = findFormatReferenceFor(entry);
        MenuBuffer menuBuffer = null;

        // Look to see if the format reference is already associated with
        // a menu buffer (the format reference could be null)
        menuBuffer = (MenuBuffer) menuBuffers.get(formatReference);

        if (menuBuffer == null) {
            // Determine the output buffer needed for the given format
            // reference (both of which could be null)
            OutputBuffer buffer = getOutputBuffer(formatReference);

            // If the output buffer is null, the specified pane doesn't
            // exist and the content will therefore be ignored
            if (buffer != null) {
                // Construct the required buffer
                menuBuffer = factory.createMenuBuffer(entry,
                        formatReference,
                        buffer);
            }

            // Set up the association between the reference and menu buffer.
            // One, other or both values could be null. A null format reference
            // indicates a missing pane reference while a null menu buffer
            // indicates an invalid pane reference (one that refers to a non-
            // existent pane)
            menuBuffers.put(formatReference, menuBuffer);
        }

        return menuBuffer;
    }

    /**
     * Finds the pane format reference that should be used for the specified
     * menu entry. Note that if the entry and none of its container entries is
     * targeted at a pane then null is returned.
     *
     * <p>Under the following circumstances this method must handle automatic
     * iterator allocation:</p>
     *
     * <ul>
     *
     * <li>the menu entry has no pane format reference defined for it</li>
     *
     * <li>the menu entry's parent menu is directly targeted at a spatially or
     * temporally iterated pane - this is identified by having a
     * FormatReference with more than zero dimensions where not all dimensions
     * have been specified</li>
     *
     * <li>the menu entry's menu's style has
     * <code>mcs-menu-entry-iterator-allocation</code> set to "automatic"</li>
     *
     * </ul>
     *
     * <p>In this situation each menu entry immediately within that menu not
     * explicitly targeted at a pane will need to be targeted at successive
     * iterations of the pane in the most immediately containing iterator (i.e.
     * the last dimension).</p>
     *
     * <p>Note that once a menu entry group or sub-menu has had an automatic
     * allocation of pane, all of its untargeted decendents (where there is no
     * interceding targeted decendent) will be targeted to that same allocated
     * pane. This allocation information is tracked in the {@link #allocations}
     * map.<p>
     *
     * @param entry the menu entry for which the pane format reference is to be
     *              obtained. May not be null
     * @return the pane format reference for the menu entry, or null
     */
    protected FormatReference findFormatReferenceFor(MenuEntry entry) {
        FormatReference reference = null;

        if (entry == null) {
            throw new IllegalArgumentException("entry may not be null");
        }

        // Get the explicitly defined target pane (if any)
        reference = entry.getPane();

        if (reference == null) {
            // Look to see if this entry appears directly within an automatic
            // iteration allocating menu
            Menu menu = getAutoAllocatingMenu(entry);

            if (menu != null) {
                // The menu is automatically allocating iterations. See if this
                // allocation has already been performed
                reference = (FormatReference) getAllocations().get(entry);

                if (reference == null) {
                    // Assign allocations to all the menu's children
                    allocateIterations(menu);

                    // Get the allocation made specifically for this entry by
                    // the above call
                    reference = (FormatReference) getAllocations().get(entry);

                    if (reference == null) {
                        throw new IllegalStateException(
                                "entry needed automatic allocation but was " +
                                        "not allocated a reference");
                    }
                }
            } else {
                // Simply use the closest defined target pane
                reference = findTargetPane(entry);
            }
        }

        return reference;
    }

    /**
     * Ensures that a non-null allocations map is returned, lazily
     * instantiating it as needed.
     *
     * @return the allocations map
     */
    private Map getAllocations() {
        if (allocations == null) {
            allocations = new HashMap();
        }

        return allocations;
    }

    /**
     * Allocates specific pane instances to the otherwise untargeted children
     * of the given menu. These allocations are recorded in the {@link
     * #allocations} map.
     *
     * @param menu the menu that is performing automatic iteration allocation
     */
    private void allocateIterations(Menu menu) {
        // Iterate the menu's immediate children and allocate them specific
        // iterated pane instances if they are not explicitly targeted. Start
        // with the initial iteration associated with the menu
        FormatReference allocation = menu.getPane();

        try {
            for (int i = 0; i < menu.getSize(); i++) {
                MenuEntry child = menu.get(i);

                if (child.getPane() == null) {
                    int[] indices;

                    // Store the current iteration for this child then set up
                    // the next iteration
                    getAllocations().put(child, allocation);

                    // Increment the final dimension index ready
                    // for the next allocation
                    allocation =
                            (FormatReference) allocation.clone();

                    indices = allocation.getIndex().getIndicies();

                    indices[indices.length - 1]++;
                }
            }
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(
                    "FormatReferences must be cloneable");
        }
    }

    /**
     * Returns the entry's immediately containing menu if that menu is:
     *
     * <ol>
     *
     * <li>set to perform automatic iteration allocation</li>
     *
     * <li>targeted at an incompletely specified spatially or temporally
     * iterated pane</li>
     *
     * </ol>
     *
     * @param entry the entry that may be in a menu that is performing
     *              automatic iteration allocation
     * @return the containing menu if it is performing automatic iteration
     *         allocation or null if the entry is not an immediate child of a
     *         menu performing automatic iteration allocation
     */
    private Menu getAutoAllocatingMenu(MenuEntry entry) {
        Menu autoAllocatingMenu = null;

        // Only cover entries that are immediate children of a menu (i.e.
        // ignore menu items within a menu item group - the content of the
        // menu item group will be targeted at the pane allocated to the
        // group)
        if ((entry.getContainer() instanceof Menu) &&
                inspector.isAutoIterationAllocating(
                        (Menu) entry.getContainer())) {
            autoAllocatingMenu = (Menu) entry.getContainer();
        }

        return autoAllocatingMenu;
    }

    /**
     * Finds the pane format reference at which the given entry is (directly or
     * indirectly) targeted. Note that if the entry and none of its container
     * entries is targeted at a pane then null is returned. This takes into
     * account any automatically allocated pane iterations.
     *
     * @param entry the entry for which the pane format reference is to be
     *              obtained, may be null
     * @return the pane format reference for the entry, or null
     */
    private FormatReference findTargetPane(MenuEntry entry) {
        FormatReference reference = null;

        if (entry != null) {
            // Prioritize an explicit pane targeting for this entry
            reference = entry.getPane();

            if (reference == null) {
                // Since this entry was not targeted explicitly, see if it
                // has been allocated a pane in an automatic iteration
                // allocating menu
                reference = (FormatReference) getAllocations().get(entry);

                if (reference == null) {
                    // No pane yet, so check the container for this entry
                    reference = findTargetPane(entry.getContainer());
                }
            }
        }

        return reference;
    }

    /**
     * Finds the output buffer for the pane format reference specified. If the
     * reference is null the current pane's output buffer will be returned. If
     * the referenced pane format doesn't exist null will be returned.
     *
     * @param reference the format reference for which the output buffer is to
     *                  be determined, may be null
     * @return the output buffer associated with the pane format reference
     *         given or null if the given pane doesn't exist
     */
    private OutputBuffer getOutputBuffer(FormatReference reference) {
        return bufferResolver.resolvePaneOutputBuffer(reference);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 09-Mar-05	7022/2	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jun-04	4704/4	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name. (rework issues)

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 07-May-04	4204/1	philws	VBM:2004042810 Suppress Menu Item Group separators when needed

 06-May-04	3999/6	philws	VBM:2004042202 Review updates

 06-May-04	3999/4	philws	VBM:2004042202 Handle automatic iteration allocation in Menus

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 ===========================================================================
*/
