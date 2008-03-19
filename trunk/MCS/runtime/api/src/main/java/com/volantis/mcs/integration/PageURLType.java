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
package com.volantis.mcs.integration;

/**
 * A type safe enumeration of the different types of URL that may appear within
 * a page.
 *
 * @see PageURLRewriter
 * @see PageURLDetails
 */
public final class PageURLType {

    /**
     * URLs that are provided in the href attribute of &lt;a&gt; elements.
     */
    public static final PageURLType ANCHOR = new PageURLType("anchor");

    /**
     * URLs that are provided in the srcUri attribute of &lt;object&gt; elements.
     */
    public static final PageURLType OBJECT = new PageURLType("object");
    
    /**
     * Form action / submission URLs.
     *
     * <p>This are either the URLs that are provided as the action attribute of
     * &lt;form&gt; elements, or automatically generated URLs that are used as
     * the action of the form, e.g. form fragmentation URLs.</p>
     */
    public static final PageURLType FORM = new PageURLType("form");

    /**
     * URLs to page fragments.
     */
    public static final PageURLType FRAGMENT = new PageURLType("fragment");

    /**
     * URLs that are provided in the href attribute of &lt;menuitem&gt;
     * elements.
     */
    public static final PageURLType MENU_ITEM = new PageURLType("menu item");

    /**
     * URLs to page segments.
     */
    public static final PageURLType SEGMENT = new PageURLType("segment");

    /**
     * URLs that link dissected pane shards.
     */
    public static final PageURLType SHARD = new PageURLType("shard");

    /**
     * URLs to external style sheets.
     */
    public static final PageURLType STYLE_SHEET = new PageURLType("style sheet");

    /**
     * URLs to scripts.
     */
    public static final PageURLType SCRIPT = new PageURLType("script");

    /**
     * URLs provided in src attribute of &lt;image&gt; element.
     */
    public static final PageURLType IMAGE = new PageURLType("image");

    /**
     * URLs from widget elements, including:
     * <li>src attribute from &lt;widget:load&gt; element</li>
     * <li>src attribute from &lt;widget:refresh&gt; element</li>
     * <li>src attribute from &lt;widget:autocomplete&gt; element</li>
     * <li>src attribute from &lt;widget:validate&gt; element</li>
     * <li>url attribute from &lt;ticker:feed-poller&gt; element</li>
     */
    public static final PageURLType WIDGET = new PageURLType("widget");

    private final String name;

    /**
    * Private constructor.
    */ 
    private PageURLType(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-Jul-04	4824/1	pduffin	VBM:2004062102 Documented PageURLRewriter and related classes

 01-Jul-04	4778/1	allan	VBM:2004062912 Use the Volantis.pageURLRewriter to rewrite page urls

 28-Jun-04	4733/3	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 21-Jun-04	4728/1	allan	VBM:2004062101 Classes and interfaces for general url rewriting.

 ===========================================================================
*/
