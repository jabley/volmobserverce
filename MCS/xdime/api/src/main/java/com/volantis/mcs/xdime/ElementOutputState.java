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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime;

/**
 * Determines whether the element whose state is described by this class should
 * generate output markup, and if so where that markup should appear.
 * <p/>
 * An element's output markup should be suppressed if its styles specify a new
 * layout (or if one of its ancestors did) and no subsequent element has
 * specified a valid container.
 * <p/>
 * The element's output markup should also be suppressed if the element to
 * which this state applies either is, or is contained by, an inactive
 * group.
 *
 * @mock.generate
 */
public interface ElementOutputState {

    /**
     * Indicate whether the element to which this state applies should
     * currently be suppressing or generating output markup.
     * <p/>
     * An element's output markup should be suppressed if it specifies a new
     * layout (or if one of its ancestors specified a new layout) and no
     * subsequent element has specified a valid container.
     * <p/>
     * The element's output markup should also be suppressed if the element to
     * which this state applies either is, or is contained by, an inactive
     * group.
     *
     * @return true if this state determines that the element to which it is
     * applied should suppress output markup, false otherwise
     */
    boolean isSuppressing();

    /**
     * Indicate whether the element to which this state applies, and all its descendants,
     * should currently be suppressing or generating output markup.
     * <p/>
     * An element and its descendants should be supressed if it's going to be removed,
     * but prior to doing that this element and its descendants should be validated.
     *
     * @return true if element and its descendants should be removed, false otherwise
     */
    boolean isSuppressingDescendants();

    /**
     * Indicate whether the element to which this state applies appears in a
     * layout which is completely suppressed.
     *
     * This occurs when a new layout is specified while the parent state is
     * suppressing output markup. All the elements which are targetted at that
     * layout should remain suppressed even if they target valid containers.
     * This is because the layout itself wasn't correctly targetted at a valid
     * container.
     *
     * @return true if the element to which this state applies appears in a
     * layout which is completely suppressed, false otherwise
     */
    boolean isInSuppressedLayout();

    /**
     * Configures this state to indicate whether or not it applies to a group
     * element which is currently inactive (or one of its descendants).
     * <p/>
     * An element should not generate output markup if either it is, or is
     * contained by, an inactive group (regardless of whether its styles
     * specify a valid container). This has an impact on the value of
     * {@link #isSuppressing()}.
     *
     * @param isInactiveGroup   true if this state applies to an group element
     *                          which is currently inactive (or one of its
     *                          descendants), false otherwise
     */
    void setIsInactiveGroup(boolean isInactiveGroup);

    /**
     * Returns true if this state applies to a group element which is currently
     * inactive (or one of its descendants), false otherwise.
     *
     * @return true if this state applies to a group element which is currently
     * inactive (or one of its descendants), false otherwise.
     */
    boolean isInactiveGroup();

    /**
     * Calculates if the element to which this state applies specifies a valid
     * layout and container, and updates this state and the current page
     * context to reflect the result.
     * <p/>
     *
     * @return FormattingResult which indicates whether the element to which
     * this state applies should be processed, skipped or suppressed.
     * @throws XDIMEException if there was a problem applying the state
     */
    FormattingResult apply() throws XDIMEException;

    /**
     * Revert any changes that were made by calling {@link #apply}.
     */
    void revert();
}
