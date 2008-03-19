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

package com.volantis.mcs.protocols.separator;

/**
 * Used to communicate between the separator manager and the arbitrator.
 *
 * <p>The manager uses this to provide the arbitrator with the information
 * that it needs in order to make its decision. The arbitrator uses it to
 * communicate its decision back to the separator manager.</p>
 *
 * <p>The manager will consult the arbitrator when it needs to make the
 * decision as to whether a separator should be rendered. This occurs if
 * a separator is written immediately before some content, or another
 * separator, or before a flush operation. The following table shows the
 * values that will be returned by the getter methods given particular
 * sequence of operations on the manager.</p>
 *
 * <table border="1">
 *   <thead>
 *     <th>Sequence</th>
 *     <th>{@link com.volantis.mcs.protocols.separator.ArbitratorDecision#getPreviousContent}</th>
 *     <th>{@link com.volantis.mcs.protocols.separator.ArbitratorDecision#getDeferredSeparator}</th>
 *     <th>{@link com.volantis.mcs.protocols.separator.ArbitratorDecision#getTriggeringContent}</th>
 *     <th>{@link com.volantis.mcs.protocols.separator.ArbitratorDecision#getTriggeringSeparator}</th>
 *   </thead>
 *
 *   <tr>
 *     <td>separator<br/>content</td>
 *     <td>null</td>
 *     <td>separator</td>
 *     <td>content</td>
 *     <td>null</td>
 *   </tr>
 *
 *   <tr>
 *     <td>content1<br/>separator<br/>content2</td>
 *     <td>content1</td>
 *     <td>separator</td>
 *     <td>content2</td>
 *     <td>null</td>
 *   </tr>
 *
 *   <tr>
 *     <td>content<br/>separator<br/>flush</td>
 *     <td>content</td>
 *     <td>separator</td>
 *     <td>null</td>
 *     <td>null</td>
 *   </tr>
 *
 *   <tr>
 *     <td>separator1<br/>separator2</td>
 *     <td>null</td>
 *     <td>separator1</td>
 *     <td>null</td>
 *     <td>separator2</td>
 *   </tr>
 * </table>
 *
 * @mock.generate
 */
public interface ArbitratorDecision {

    /**
     * Get the content that was rendered previously.
     *
     * @return The content that was rendered previously, or null if the
     * separator manager is not aware of any content that has been
     * rendered.
     */
    public SeparatedContent getPreviousContent();

    /**
     * Get the separator for which the decision is being made.
     *
     * @return The separator for which the decision is being made, may not
     * be null.
     */
    public SeparatorRenderer getDeferredSeparator();

    /**
     * Get the content that is about to be rendered and which triggered the
     * manager to consult its arbitrator.
     *
     * @return The content that is about to be rendered, or null if content
     * was not the trigger for the arbitrator being consulted.
     */
    public SeparatedContent getTriggeringContent();

    /**
     * Get the separator that triggered the manager to consult its
     * arbitrator.
     *
     * @return The separator that triggered the arbitrator, or null if a
     * separator was not the trigger.
     */
    public SeparatorRenderer getTriggeringSeparator();

    /**
     * The specified renderer should be used immediately.
     * @param separator The renderer to use, may not be null.
     */
    public void use(SeparatorRenderer separator);

    /**
     * The decision as to whether the renderer should be used is to be
     * deferred pending additional information.
     *
     * @param separator The renderer that may be used in future, may not be
     * null.
     */
    public void defer(SeparatorRenderer separator);

    /**
     * No renderer is required.
     */
    public void ignore();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 ===========================================================================
*/
