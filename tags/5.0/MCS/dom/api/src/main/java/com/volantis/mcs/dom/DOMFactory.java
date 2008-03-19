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

package com.volantis.mcs.dom;

import com.volantis.styling.Styles;
import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * todo Document this.
 * @mock.generate
 */
public abstract class DOMFactory {

    /**
     * Set up the meta default factory instance.
     */
    private static final MetaDefaultFactory metaDefaultFactory =
            new MetaDefaultFactory("com.volantis.mcs.dom.impl.DOMFactoryImpl",
                    DOMFactory.class.getClassLoader());

    /**
     * Get the default instance of this factory.
     * @return The default instance of this factory.
     */
    public static DOMFactory getDefaultInstance() {
        return (DOMFactory) metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * Create Document.
     *
     * @return Newly created Document.
     */
    public abstract Document createDocument();

    /**
     * Create the DocType.
     *
     * @return The DocType.
     */
    public abstract DocType createDocType(
            String root, String publicId, String systemId, String internalDTD,
            MarkupFamily markupFamily);

    /**
     * Create the XML declaration.
     *
     * @return The XML declaration.
     */
    public abstract XMLDeclaration createXMLDeclaration();

    /**
     * Create element node.
     *
     * @return Newly created Element node.
     */
    public abstract Element createElement();

    /**
     * Create element node.
     *
     * @param name The name of the element.
     * @return Newly created Element node.
     */
    public abstract Element createElement(String name);

    /**
     * Create element node.
     *
     * @param styles The styles for the element.
     * @return Newly created Element node.
     */
    public abstract Element createStyledElement(Styles styles);

    /**
     * Create element node.
     *
     * @param name The name of the element.
     * @param styles The styles for the element.
     * @return Newly created Element node.
     */
    public abstract Element createStyledElement(String name, Styles styles);

    /**
     * Create text node.
     *
     * @return Newly created Text node.
     */
    public abstract Text createText();

    /**
     * Create text node.
     *
     * @param contents The contents of the text node.
     *
     * @return Newly created Text node.
     */
    public abstract Text createText(String contents);

    /**
     * Create comment node.
     *
     * @return Newly created Comment node.
     */
    public abstract Comment createComment();

    /**
     * Create comment node.
     *
     * @param contents The contents of the comment node.
     *
     * @return Newly created Comment node.
     */
    public abstract Comment createComment(String contents);

    /**
     * Set the debug flag.
     *
     * <p>Any DOM objects created after this has been set will use the value
     * of this flag to determine whether they perform additional debug
     * checks.</p>
     *
     * @param debug The debug flag.
     */
    public abstract void setDebug(boolean debug);

    /**
     * Get the value of the debug flag.
     *
     * @return The value of the debug flag.
     */
    public abstract boolean isDebug();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Oct-05	9825/1	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 09-Jun-05	8665/1	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info, some tests commented out temporarily

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 ===========================================================================
*/
