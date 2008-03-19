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

package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.mcs.xml.schema.model.Namespace;
import com.volantis.mcs.xml.schema.model.ElementType;

public class XHTML2Elements {

    /**
     * The namespace containing all these elements.
     */
    public static final Namespace NAMESPACE =
            new Namespace(XDIMESchemata.XHTML2_NAMESPACE, "");

    /**
     * Get the element type for the specified local name in the current
     * namespace.
     *
     * @param localName The local name of the element.
     * @return The element type.
     */
    private static ElementType getElement(String localName) {
        return NAMESPACE.addElement(localName);
    }

    public static final ElementType A = getElement("a");
    public static final ElementType ABBR = getElement("abbr");
    public static final ElementType ACCESS = getElement("access");
    public static final ElementType ADDRESS = getElement("address");
    public static final ElementType BLOCKQUOTE = getElement("blockquote");
    public static final ElementType BODY = getElement("body");
    public static final ElementType CAPTION = getElement("caption");
    public static final ElementType CITE = getElement("cite");
    public static final ElementType CODE = getElement("code");
    public static final ElementType DD = getElement("dd");
    public static final ElementType DFN = getElement("dfn");
    public static final ElementType DIV = getElement("div");
    public static final ElementType DL = getElement("dl");
    public static final ElementType DT = getElement("dt");
    public static final ElementType EM = getElement("em");
    public static final ElementType H1 = getElement("h1");
    public static final ElementType H2 = getElement("h2");
    public static final ElementType H3 = getElement("h3");
    public static final ElementType H4 = getElement("h4");
    public static final ElementType H5 = getElement("h5");
    public static final ElementType H6 = getElement("h6");
    public static final ElementType HEAD = getElement("head");
    public static final ElementType HTML = getElement("html");
    public static final ElementType KBD = getElement("kbd");
    public static final ElementType LABEL = getElement("label");
    public static final ElementType LI = getElement("li");
    public static final ElementType LINK = getElement("link");
    public static final ElementType META = getElement("meta");
    public static final ElementType NL = getElement("nl");
    public static final ElementType OBJECT = getElement("object");
    public static final ElementType OL = getElement("ol");
    public static final ElementType P = getElement("p");
    public static final ElementType PARAM = getElement("param");
    public static final ElementType PRE = getElement("pre");
    public static final ElementType QUOTE = getElement("quote");
    public static final ElementType SAMP = getElement("samp");
    public static final ElementType SPAN = getElement("span");
    public static final ElementType STRONG = getElement("strong");
    public static final ElementType STYLE = getElement("style");
    public static final ElementType SUB = getElement("sub");
    public static final ElementType SUP = getElement("sup");
    public static final ElementType TABLE = getElement("table");
    public static final ElementType TBODY = getElement("tbody");
    public static final ElementType TD = getElement("td");
    public static final ElementType TFOOT = getElement("tfoot");
    public static final ElementType TH = getElement("th");
    public static final ElementType THEAD = getElement("thead");
    public static final ElementType TITLE = getElement("title");
    public static final ElementType TR = getElement("tr");
    public static final ElementType UL = getElement("ul");
    public static final ElementType VAR = getElement("var");
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 ===========================================================================
*/
