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

package com.volantis.mcs.papi;

/**
 * This class allows the overriding of link text used within dissecting panes
 * at runtime. The class can only be obtained via the MarinerRequestContext
 * class and for this reason it does not have a publicly accessible constructor.
 * @see com.volantis.mcs.context.MarinerRequestContext#getDissectingPane
 */
public interface DissectingPane {
    /**
     * Return the name of the dissecting pane
     * @return the name
     */
    String getName();

    /**
     * Return the text used when a link is generated to the current shard from
     * another. This text is initially taken from the
     * layout but can be overridden by overrideLinkToText.
     * @return the link text
     * @see #overrideLinkToText
     */
    String getLinkToText();

    /**
     * Return the text used when a link is generated from the current shard to
     * another. This text is initially taken from the
     * layout but can be overridden by overrideLinkFromText.
     * @return the link text
     * @see #overrideLinkFromText
     */
    String getLinkFromText();

    /**
     * Change the text used when a link is generated to the current shard
     * from another. The initial text is taken from the
     * layout containing the dissecting pane.
     * @param linkToText the text to use for the link
     */
    void overrideLinkToText(String linkToText);

    /**
     * Change the text used when a link is generated from the current shard
     * to another. The initial text is taken from the
     * layout containing the dissecting pane.
     * @param linkFromText the text to use for the link
     */
    void overrideLinkFromText(String linkFromText);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8196/4	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 ===========================================================================
*/
