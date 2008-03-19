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
package com.volantis.mcs.protocols.trans;

import java.util.Iterator;

/**
 * <p>An interface to abstract the style emulation element configuration
 * implementation.</p>
 *
 * <p>Note that the methods defined in this interface effectively delineate
 * sets of elements as:</p>
 *
 * <ol>
 *
 * <li>indivisible elements</li>
 *
 * <li>stylistic elements</li>
 *
 * <li>anti elements</li>
 *
 * <li>divisible style elements</li>
 *
 * <li>mergeable elements</li>
 *
 * </ol>
 *
 * <p>It is noted that the sets 1-4 above are mutually exclusive. The
 * stylistic, anti and divisible-style elements are all divisible elements (see
 * {@link #isIndivisibleElement}). The largest set of elements is generally the
 * set of indivisible elements since it includes all elements that aren't
 * stylistic, anti, divisible-style and mergeable elements.</p>
 *
 * <p>The set of indivisible elements is more restrictive and elements that are
  * not explicitly defined elsewhere automatically fall into this set. This is
  * desirable due to the restrictive nature of this set. Note that indivisible
  * elements may contain stylistic elements as children. By default any element
  * that is not explicitly defined will fall into the set of
  * indivisible-may-not-contain-stylistic elements set.</p>
 */
public interface StyleEmulationElementConfiguration {

    /**
     * Return true if the specified element is indivisible, false otherwise. An
     * indivisible element is one that shouldn't be split (e.g. anchor element).
     * Note that indivisible elements may still contain stylistic elements
     * such as <code>b</code> or <code>u</code>.
     *
     * @param element the element to check.
     * @return true if the specified element is indivisible, false otherwise.
     */
    boolean isIndivisibleElement(String element);

    /**
     * Return true if the specified element is a stylistic element, false
     * otherwise. A stylistic element is an element that is a subset of the
     * 'font style' elements that we use to emulate styles. Typically this
     * set will be small and may include: b, u, and i.
     *
     * @param element the element to check.
     * @return true if the specified element is a stylistic element, false
     *         otherwise.
     */
    boolean isStylisticElement(String element);

    /**
     * Return true if the specified element is an anti-element, false
     * otherwise. An anti-element is a stylistic element's counterpart that
     * cancels that stylistic element (e.g. anti-b is the anti element for b).
     *
     * @param element the element to check.
     * @return true if the specified element is an anti-element, false
     *         otherwise.
     */
    boolean isAntiElement(String element);

    /**
     * Return true if the specified element is a divisible style element that
     * may contain stylistic elements as children, false otherwise. This
     * element may be defined as an element that isn't any of the following:
     *
     * <ul>
     *
     * <li>indivisible, Examples. p, select</li>
     *
     * <li>stylistic, Examples: b, u</li>
     *
     * <li>anti-element, Examples. anti-b, anti-u</li>
     *
     * </ul>
     *
     * @param element the element to check.
     * @return true if the specified element is an anti-element, false
     *         otherwise.
     */
    boolean isDivisibleStyleElement(String element);

    /**
     * Return true if the specified element is a mergeable element, false
     * otherwise. A mergeable element is one whose attributes can be merged
     * into another element of the same name (e.g. font).
     *
     * @param element the element to check.
     * @return true if the specified element is a mergable element, false
     *         otherwise.
     */
    boolean isMergeableElement(String element);

    /**
     * Return true if the element may contain the child element, false
     * otherwise. (e.g. underline elements may not contain paragraph
     * elements).
     *
     * @param element the element that may or may not permit the containment of
     *                the child element. This must be a stylistic element.
     * @param childElement
     *                the child element.
     * @return true if the element may contain the child element, false
     *         otherwise.
     */
    boolean isContainmentPermitted(String element, String childElement);

    /**
     * Return true if the style is permitted as child of the element, false
     * otherwise. (e.g. an img element does not permit stylistic content).
     *
     * @param element
     *         the element name used in the check.
     * @return true if the style is permitted as a child element, false
     *         otherwise.
     */
    boolean isStylePermittedInElement(String element);

    /**
     * Get an iterator for  element name set using the anti-element's name (e.g.
     * ANTI-B should return b. For ANTI-SIZE in it may return 'big' and
     * 'small'.
     *
     * @param antiElement
     *         the anti-element name used to establish the counterpart element
     *         names.
     * @return the associated elements (as a Set) for the specified
     *         anti-element.
     */
    Iterator getCounterpartElementNames(String antiElement);

    /**
     * Return true if this element a counterpart for the specified
     * anti-element, false otherwise.
     *
     * @param antiElement
     *         the anti elment names
     * @param elementName
     *         the element to check
     * @return true if this element a counterpart for the specified
     *         anti-element, false otherwise.
     */
    boolean isCounterpart(String antiElement, String elementName);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Oct-04	5877/10	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements - fix comments

 29-Oct-04	5877/8	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements - rework issues

 28-Oct-04	5877/6	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements - rework issues

 26-Oct-04	5877/4	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 21-Jul-04	4752/6	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - fix anti-size in WML

 14-Jul-04	4783/2	geoff	VBM:2004062302 Implementation of theme style options: WML Family (fixes after style inversion approval)

 14-Jul-04	4752/3	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - addressed rework issues

 14-Jul-04	4752/1	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - addressed rework issues

 ===========================================================================
*/
