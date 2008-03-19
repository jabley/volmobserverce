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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/SelectorRendererVisitor.java,v 1.1 2002/04/27 18:26:15 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Apr-02    Allan           VBM:2002042404 - Created. A renderer visitor
 *                              for selectors.
 * 03-Jun-03    Allan           VBM:2003060301 - UndeclaredThrowableException 
 *                              moved to Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.AttributeSelector;
import com.volantis.mcs.themes.ClassSelector;
import com.volantis.mcs.themes.CombinedSelector;
import com.volantis.mcs.themes.IdSelector;
import com.volantis.mcs.themes.InvalidSelector;
import com.volantis.mcs.themes.NthChildSelector;
import com.volantis.mcs.themes.PseudoClassSelector;
import com.volantis.mcs.themes.PseudoElementSelector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.SelectorVisitor;
import com.volantis.mcs.themes.TypeSelector;
import com.volantis.mcs.themes.UniversalSelector;
import com.volantis.mcs.themes.InlineStyleSelector;
import com.volantis.synergetics.UndeclaredThrowableException;

import java.io.IOException;

/**
 * A renderer for selectors. This is a general selector renderer. Where (if)
 * specific versions of css differ from this general renderer in the way
 * that they render one or more selectors, this class should be extended to
 * implement the specific render methods required.
 */
public abstract class SelectorRendererVisitor implements SelectorVisitor {

    /**
     * The renderer for attribute selectors.
     */
    protected SelectorRenderer attributeSelectorRenderer;

    /**
     * The renderer for class selectors.
     */
    protected SelectorRenderer classSelectorRenderer;

    /**
     * The renderer for combined selectors.
     */
    protected SelectorRenderer combinedSelectorRenderer;

    /**
     * The renderer for selector sequences.
     */
    protected SelectorRenderer selectorSequenceRenderer;

    /**
     * The renderer for id selectors.
     */
    protected SelectorRenderer idSelectorRenderer;

    /**
     * The renderer for invalid selectors.
     */
    protected SelectorRenderer invalidSelectorRenderer;

    /**
     * The renderer for nth child selectors.
     */
    protected SelectorRenderer nthChildSelectorRenderer;

    /**
     * The renderer for pseudo element selectors.
     */
    protected SelectorRenderer pseudoElementSelectorRenderer;

    /**
     * The renderer for pseudo class selectors.
     */
    protected SelectorRenderer pseudoClassSelectorRenderer;

    /**
     * The renderer for type selectors.
     */
    protected SelectorRenderer typeSelectorRenderer;

    /**
     * The renderer for universal selectors.
     */
    protected SelectorRenderer universalSelectorRenderer;

    /**
     * The renderer context.
     */
    private RendererContext context;

    /**
     * Create a selector renderer visitor with a specified renderer context.
     *
     * <p>If the renderer context is null, then
     * {@link #setRendererContext(RendererContext)} must be called to provide a
     * non-null context before rendering takes place.</p>
     */
    protected SelectorRendererVisitor(RendererContext rendererContext) {
        context = rendererContext;
    }

    /**
     * Sets the renderer context for this renderer visitor.
     * @param newContext
     */
    public void setRendererContext(RendererContext newContext) {
        context = newContext;
    }

    /**
     * Render an AttributeSelector
     * @param selector the AttributeSelector to render
     */
    public void visit(AttributeSelector selector) {
        try {
            attributeSelectorRenderer.render(selector, context);
        } catch(IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Render an ClassSelector
     * @param selector the ClassSelector to render
     */
    public void visit(ClassSelector selector) {
        try {
            classSelectorRenderer.render(selector, context);
        } catch(IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Render an CombinedSelector
     * @param selector the CombinedSelector to render
     */
    public void visit(CombinedSelector selector) {
        try {
            combinedSelectorRenderer.render(selector, context);
        } catch(IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Render a SelectorSequence
     * @param selector the SelectorSequence to render
     */
    public void visit(SelectorSequence selector) {
        try {
            selectorSequenceRenderer.render(selector, context);
        } catch(IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Render an IDSelector
     * @param selector the IDSelectorSelector to render
     */
    public void visit(IdSelector selector) {
        try {
            idSelectorRenderer.render(selector, context);
        } catch(IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Render an PseudoClassSelector
     * @param selector the PseudoClassSelector to render
     */
    public void visit(PseudoClassSelector selector) {
        try {
            pseudoClassSelectorRenderer.render(selector, context);
        } catch(IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    public void visit(NthChildSelector selector) {
        try {
            nthChildSelectorRenderer.render(selector, context);
        } catch(IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Render an PseudoElementSelector
     * @param selector the PseudoElementSelector to render
     */
    public void visit(PseudoElementSelector selector) {
        try {
            pseudoElementSelectorRenderer.render(selector, context);
        }
        catch(IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Render an TypeSelector
     * @param selector the TypeSelector to render
     */
    public void visit(TypeSelector selector) {
        try {
            typeSelectorRenderer.render(selector, context);
        } catch(IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Render an UniversalSelector
     * @param selector the UniversalSelector to render
     */
    public void visit(UniversalSelector selector) {
        try {
            universalSelectorRenderer.render(selector, context);
        } catch(IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Render an UniversalSelector
     * @param selector the UniversalSelector to render
     */
    public void visit(InvalidSelector selector) {
        try {
            invalidSelectorRenderer.render(selector, context);
        } catch(IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    //javadoc inherited
    public void visit(InlineStyleSelector selector) {
        //If this method is called then an attempt is being made to render
        //the css which shouldn't be happening.
        throw new UnsupportedOperationException();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 05-Sep-05	9407/5	pduffin	VBM:2005083007 Removed old themes model

 02-Sep-05	9407/3	pduffin	VBM:2005083007 Committing resolved conflicts

 01-Sep-05	9412/7	adrianj	VBM:2005083007 CSS renderer using new model

 01-Sep-05	9412/5	adrianj	VBM:2005083007 CSS renderer using new model

 01-Sep-05	9412/3	adrianj	VBM:2005083007 CSS renderer using new model

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Oct-04	5862/1	tom	VBM:2004101909 refactored SelectorVisitor and Selector method names to be inline with Visitor Design Pattern

 13-Oct-04	5498/1	tom	VBM:2004082410 refactored SelectorVisitor.visit() to SelectorVisitor.accept()

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
