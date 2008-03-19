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

package com.volantis.mcs.dom.debug;

import com.volantis.mcs.dom.DOMWalker;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.WalkingDOMVisitor;
import com.volantis.mcs.dom.Comment;
import com.volantis.styling.Styles;
import com.volantis.styling.debug.DebugHelper;
import com.volantis.styling.debug.DebugStyles;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.MutableStylePropertySetImpl;
import com.volantis.styling.properties.StylePropertySet;

/**
 * Output debugging information for a styled document.
 */
public class DebugStyledDocument
    implements WalkingDOMVisitor {

    protected final StringBuffer debug;

    protected String indent;
    protected DebugStyles debugStyles;

    public DebugStyledDocument(StylePropertySet interestingProperties) {

        if (interestingProperties == null) {
            MutableStylePropertySet mutable = new MutableStylePropertySetImpl();
            mutable.addAll();
            interestingProperties = mutable;
        }

        debugStyles = new DebugStyles(interestingProperties, false, true);

        debug = new StringBuffer(1024);

        indent = "";
    }

    public String debug(Document document) {

        DOMWalker walker = new DOMWalker(this);
        walker.walk(document);
        return debug.toString();
    }

    public String debug(Element element) {

        DOMWalker walker = new DOMWalker(this);
        walker.walk(element);
        return debug.toString();
    }

    public void visit(Element element) {
        int column = element.getName().length() + 1;
        debug.append(indent).append(element.getName()).append(" ");
        Styles styles = element.getStyles();
        debug.append(debugStyles.output(styles, DebugHelper.getIndent(column)));
        debug.append("\n");
    }

    public void afterChildren(Element element) {
        indent = indent.substring(4);
    }

    public void beforeChildren(Element element) {
        indent += "    ";
    }

    public void visit(Text text) {
        int length = text.getLength();
        if (length != 0) {
            char [] contents = text.getContents();
            String string = new String(contents, 0, length);
            debug.append(indent).append(string).append("\n");
        }
    }

    public void visit(Comment comment) {
        int length = comment.getLength();
        if (length != 0) {
            char [] contents = comment.getContents();
            String string = new String(contents, 0, length);
            debug.append(indent).append("<!--").append(string).append("-->\n");
        }
    }

    // Javadoc inherited.
    public void visit(Document document) {
    }

    // Javadoc inherited.
    public void beforeChildren(Document element) {
    }

    // Javadoc inherited.
    public void afterChildren(Document element) {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 22-Sep-05	9128/1	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Aug-05	9195/1	emma	VBM:2005080510 Refactoring to create StyledDOMTester

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 ===========================================================================
*/
