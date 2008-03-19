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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/css2/CSSSelectorRendererVisitor.java,v 1.2 2002/05/07 17:28:32 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Apr-02    Allan           VBM:2002042404 - Created. A renderer visitor 
 *                              for css2 selectors.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css.renderer;

/**
 * The CSS version of the SelectorRendererVisitor.
 *
 * todo: merge this together with SelectorRendererVisitor
 */
public class CSSSelectorRendererVisitor extends SelectorRendererVisitor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    /**
     * Get the AttributeSelectorRenderer.
     */
    protected SelectorRenderer getAttributeSelectorRenderer() {
        return new AttributeSelectorRenderer();
    }

    /**
     * Get the ClassSelectorRenderer.
     */
    protected SelectorRenderer getClassSelectorRenderer() {
        return new ClassSelectorRenderer();
    }

    /**
     * Get the CombinedSelectorRenderer.
     */
    protected SelectorRenderer getCombinedSelectorRenderer() {
        return new CombinedSelectorRenderer();
    }

    /**
     * Get the IDSelectorRenderer.
     */
    protected SelectorRenderer getIDSelectorRenderer() {
        return new IdSelectorRenderer();
    }

    /**
     * Get the InvalidSelectorRenderer.
     */
    protected SelectorRenderer getInvalidSelectorRenderer() {
        return new InvalidSelectorRenderer();
    }

    /**
     * Get the NthChildSelectorRenderer.
     */
    protected SelectorRenderer getNthChildSelectorRenderer() {
        return new NthChildSelectorRenderer();
    }

    /**
     * Get the PseudoElementSelectorRenderer.
     */
    protected SelectorRenderer getPseudoElementSelectorRenderer() {
        return new PseudoElementSelectorRenderer();
    }

    /**
     * Get the PseudoClassSelectorRenderer.
     */
    protected SelectorRenderer getPseudoClassSelectorRenderer() {
        return new PseudoClassSelectorRenderer();
    }

    /**
     * Get the SelectorSequenceRenderer.
     */
    protected SelectorRenderer getSelectorSequenceRenderer() {
        return new SelectorSequenceRenderer();
    }

    /**
     * Get the TypeSelectorRenderer.
     */
    protected SelectorRenderer getTypeSelectorRenderer() {
        return new TypeSelectorRenderer();
    }

    /**
     * Get the UniversalSelectorRenderer.
     */
    protected SelectorRenderer getUniversalSelectorRenderer() {
        return new UniversalSelectorRenderer();
    }

    /**
     * Construct a new CSSSelectorRendererVisitor and initialise its
     * rendererers.
     */
    public CSSSelectorRendererVisitor(RendererContext context) {
        super(context);
        initializeRenderers();
    }

    /**
     * Construct a new CSSSelectorRendererVisitor and initialise its
     * rendererers.
     */
    public CSSSelectorRendererVisitor() {
        this(null);
    }

    /**
     * Initialize the rendererer this visitor.
     */
    protected void initializeRenderers() {
        attributeSelectorRenderer = getAttributeSelectorRenderer();
        classSelectorRenderer = getClassSelectorRenderer();
        combinedSelectorRenderer = getCombinedSelectorRenderer();
        selectorSequenceRenderer = getSelectorSequenceRenderer();
        idSelectorRenderer = getIDSelectorRenderer();
        invalidSelectorRenderer = getInvalidSelectorRenderer();
        nthChildSelectorRenderer = getNthChildSelectorRenderer();
        pseudoElementSelectorRenderer = getPseudoElementSelectorRenderer();
        pseudoClassSelectorRenderer = getPseudoClassSelectorRenderer();
        typeSelectorRenderer = getTypeSelectorRenderer();
        universalSelectorRenderer = getUniversalSelectorRenderer();
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

 01-Sep-05	9412/4	adrianj	VBM:2005083007 CSS renderer using new model

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/7	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/5	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
