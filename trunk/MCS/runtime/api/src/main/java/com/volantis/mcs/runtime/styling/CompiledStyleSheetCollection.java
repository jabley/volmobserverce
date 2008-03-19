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
package com.volantis.mcs.runtime.styling;

import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.styling.sheet.StyleSheetContainer;
import com.volantis.styling.engine.StylingEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of related compiled style sheets.
 * <p>
 * The entire collection may be pushed or popped from a styling engine in one
 * operation. Order is maintained so that they are pushed in the order of
 * addition and popped in the reverse order of addition.
 * <p>
 * Once the collection has been pushed onto the styling engine it is immutable,
 * apart from the case described below.
 * <p>
 * To support XDIME1 and it's backwards way of allowing a style element inside
 * the canvas (since there is no head), we support a "back door" way of adding
 * style sheets after the collection has been added to the styling engine.
 * This is nasty but is a result of the nastiness described above.
 */
public class CompiledStyleSheetCollection {

    /**
     * collection of compiled style sheet
     */
    private List compiledStyleSheets = new ArrayList();

    private boolean immutable;

    /**
     * add the specified style sheet to the collection of style sheets.
     * @param compiledStyleSheet
     * @return
     */
    public boolean addStyleSheet(CompiledStyleSheet compiledStyleSheet) {
        if (immutable) {
            throw new IllegalStateException();
        }
        return compiledStyleSheets.add(compiledStyleSheet);
    }

    /**
     * Add an additional style sheet to the list after all the others in the
     * collection have already been added to the styling engine.
     * <p>
     * This should only be required for XDIME 1 where we have to deal with the
     * nasty form of input markup where style elements are the first elements
     * in the body (canvas) rather than being in the head (since there is none
     * in XDIME1).
     *
     * @param stylingEngine the styling engine to push the style sheet into.
     * @param compiledStyleSheet the style sheet to add to the collection.
     */
    public void pushAdditionalStyleSheet(StylingEngine stylingEngine,
            CompiledStyleSheet compiledStyleSheet) {
        compiledStyleSheets.add(compiledStyleSheet);
        stylingEngine.pushStyleSheet(compiledStyleSheet);
    }

    /**
     * DO NOT USE IN PRODUCTION CODE.
     *
     * @deprecated only used by test - remove
     * @todo remove this and fix up the test
     */
    public List getCompiledStyleSheets() {
        return compiledStyleSheets;
    }

    /**
     * Push all the style sheets in the collection into the supplied styling
     * engine.
     *
     * @todo better: use mutable/immutable objects pattern instead?
     * So we have an object to create collections of style sheets, and then an
     * object which can be pushed into the style sheet and popped off the
     * style sheet.
     *
     * @param stylingEngine
     */
    public void pushAll(StyleSheetContainer stylingEngine) {

        for (int i = 0; i < compiledStyleSheets.size(); i++) {
            CompiledStyleSheet styleSheet = (CompiledStyleSheet)
                    compiledStyleSheets.get(i);
            stylingEngine.pushStyleSheet(styleSheet);
        }

        immutable = true;
    }

    /**
     * Pop all the style sheets in the collection from the supplied styling
     * engine.
     *
     * @param stylingEngine
     */
    public void popAll(StyleSheetContainer stylingEngine) {

        if (!immutable) {
            throw new IllegalStateException();
        }

        for (int i = compiledStyleSheets.size() - 1; i >= 0; i--) {
            CompiledStyleSheet styleSheet = (CompiledStyleSheet)
                    compiledStyleSheets.get(i);
            stylingEngine.popStyleSheet(styleSheet);
        }
    }

}
