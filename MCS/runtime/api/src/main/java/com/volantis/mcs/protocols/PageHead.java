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
 * $Header: /src/voyager/com/volantis/mcs/protocols/PageHead.java,v 1.8 2002/12/13 12:52:27 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Jun-01    Paul            VBM:2001062704 - Added this change history and
 *                              moved from com/volantis/mcs/layouts/CanvasHead.
 * 23-Jul-01    Paul            VBM:2001070507 - Added helper methods
 *                              getAssetsFromImageComponents and
 *                              retrieveRolloverImageComponent.
 * 23-Jul-01    Paul            VBM:2001070507 - Removed the write to page
 *                              method, the functionality has now been moved
 *                              into VolantisProtocol and added a mechanism to
 *                              support protocol specific buffers. Also removed
 *                              body as it was not needed.
 * 02-Oct-01    Paul            VBM:2001100104 - Prevent blank lines being
 *                              added to the head buffer.
 * 19-Nov-01    Paul            VBM:2001110202 - Prevented blank lines being
 *                              added to the initial buffer.
 * 12-Feb-02    Paul            VBM:2002021201 - Renamed from PageHead and
 *                              moved from context package as it is
 *                              string protocol specific.
 * 28-Feb-02    Paul            VBM:2002022804 - Renamed back to PageHead but
 *                              left in protocol as it is general.
 * 08-Mar-02    Paul            VBM:2002030607 - Made PageHead extend
 *                              OutputBufferMap and changed all the get...
 *                              methods which return OutputBuffers to get them
 *                              from that.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 10-Oct-02    Adrian          VBM:2002100405 - Added themeStyleClasses Map,
 *                              and methods add/getThemeStyleClass.  Added
 *                              processedCssThemes list and method
 *                              processedThemeStyleSheet to update it.  Updated
 *                              release to clear both map and list.
 * 10-Oct-02    Adrian          VBM:2002100403 - Added paneStyleClasses Map
 *                              and methods add/getPaneStyleClass.  Added inner
 *                              class FormatStyleClassKey as key into the map.
 *                              Added processedCssLayouts list and method
 *                              processedLayoutStyleSheet to update it. Updated
 *                              release to clear both map and list.
 * 10-Oct-02    Adrian          VBM:2002100404 - Added lists layoutCandidates,
 *                              themeCandidates, urlCandidates and equivalent
 *                              add..Candidate methods. Added writeCssCandidate
 *                              methods to render the cssCandidates.
 * 05-Nov-02    Adrian          VBM:2002100404 - removed redundant processed..
 *                              maps and methods.
 * 08-Nov-02    Byron           VBM:2002110516 - Added defaultCanditates list
 *                              that always output styles at the top of the
 *                              page. Added addDefaultCssCandidate() method.
 *                              Modified writeCssCandidates() to use default-
 *                              canditates. Fixed javadocs.
 * 01-Dec-02    Phil W-S        VBM:2002112901 - Updated the write* methods to
 *                              take a protocol that can be passed to the
 *                              writeCss method on the candidates.
 * 12-Dec-02    Phil W-S        VBM:2002110516 - Refactoring of pane style
 *                              classes so that this functionality is
 *                              applicable to all types of format and not just
 *                              panes. Changes PaneStyleClassKey class and
 *                              refactors addPaneStyleClass, getPaneStyleClass
 *                              and the paneStyleClasses map, each being
 *                              changed to replace the Pane with a Format.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.protocols.css.CssCandidate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class is used for writing the headers to the top of a page.
 * Its main use is for when tag need to write data to the <CODE><head></CODE>
 * section of a page, for instance JavaScript.
 * The CanvasTag controls the setting up of this class, and the output when
 * the page ends.
 *
 * @author mat
 */
public class PageHead
        extends OutputBufferMap {

    /**
     * This buffer is written out first.
     */
    private OutputBuffer initial;

    /**
     * Then this buffer is written out.
     */
    private OutputBuffer head;

    /**
     * This buffer is added to the head buffer before the head buffer is
     * closed.
     */
    private OutputBuffer script;

    private Map attributes;

    /**
     * This is a list of URLCandidate objects
     */
    private List urlCandidates;

    public PageHead() {
    }

    /**
     * Retrieve the initial buffer
     *
     * @return The OutputBuffer.
     */
    public OutputBuffer getInitial() {
        if (initial == null) {
            initial = getBuffer("_initial", true);
        }

        return initial;
    }

    /**
     * Retrieve the head buffer
     *
     * @return The OutputBuffer.
     */
    public OutputBuffer getHead() {
        if (head == null) {
            head = getBuffer("_head", true);
        }

        return head;
    }

    /**
     * Retrieve the script buffer
     *
     * @return The OutputBuffer.
     */
    public OutputBuffer getScript() {
        if (script == null) {
            script = getBuffer("_script", true);
        }

        return script;
    }

    public void setAttribute(String name, Object value) {
        if (attributes == null) {
            attributes = new HashMap();
        }

        attributes.put(name, value);
    }

    public Object getAttribute(String name) {
        if (attributes == null) {
            return null;
        }

        return attributes.get(name);
    }

    /**
     * Add a CssCandidate to the urlCandidates list
     *
     * @param candidate The URLCssCandidate
     */
    public void addURLCssCandidate(CssCandidate candidate) {
        if (urlCandidates == null) {
            urlCandidates = new ArrayList();
        }

        addUniqueCssCandidateToListEnd(candidate, urlCandidates);
    }

    /**
     * Add a CssCandidate to the end of the specified list.  If it already
     * exists in the list then the item is removed and readded at the end.
     *
     * @param candidate The CssCandidate to add to the list
     * @param list      The list to add the CssCandidate into.
     */
    private void addUniqueCssCandidateToListEnd(
            CssCandidate candidate,
            List list) {

        Iterator i = list.iterator();
        int index = -1;
        int count = 0;
        while (i.hasNext() && index == -1) {
            CssCandidate c = (CssCandidate) i.next();
            if (candidate.equals(c)) {
                index = count;
            }
            count++;
        }

        if (index == -1) {
            list.add(candidate);
        } else {
            // Remove the object and add it back in at the end of the list.
            list.add(list.remove(index));
        }
    }

    /**
     * Render the CssCanditate in the layoutCandidates, themeCandidates
     * and urlCandidates lists.
     *
     * @param protocol the protocol for which the CSS is to be written
     */
    public void writeCssCandidates(VolantisProtocol protocol)
            throws IOException {
        if (urlCandidates != null) {
            writeCssCandidates(protocol, urlCandidates);
        }
    }

    /**
     * Render the CssCanditate in the given list to the head OutputBuffer
     *
     * @param candidates The list of CssCandidates
     */
    private void writeCssCandidates(
            VolantisProtocol protocol, List candidates)
            throws IOException {
        if (head != null) {
            writeCssCandidates(protocol, candidates, head);
        }
    }

    /**
     * Render the CssCanditate in the given list to the specified OutputBuffer
     *
     * @param candidates The list of CssCandidates
     * @param buffer     The OutputBuffer
     */
    private void writeCssCandidates(
            VolantisProtocol protocol,
            List candidates,
            OutputBuffer buffer)
            throws IOException {
        Iterator i = candidates.iterator();
        while (i.hasNext()) {
            CssCandidate candidate = (CssCandidate) i.next();
            candidate.writeCss(protocol, head);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 04-Aug-05	9151/3	pduffin	VBM:2005080205 Added back in some code that should not have been removed

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
