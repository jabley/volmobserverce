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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-May-03    Geoff           VBM:2003042905 - Created.
 * 30-May-03    Geoff           VBM:2003042918 - Implement dissection 
 *                              WBSAX serialiser.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom.io;

import com.volantis.mcs.wbdom.CodedNameProvider;
import com.volantis.mcs.wbdom.WBDOMElement;
import com.volantis.mcs.wbdom.LiteralNameProvider;
import com.volantis.mcs.wbdom.NameVisitor;
import com.volantis.mcs.wbdom.WBDOMException;
import com.volantis.mcs.wbsax.ElementNameCode;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.ReferenceResolver;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * An implementation of {@link com.volantis.mcs.wbdom.NameVisitor} which 
 * serialises element names. 
 */
public class WBSAXElementNameSerialiser extends AbstractWBSAXSerialiser 
        implements NameVisitor {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                            WBSAXElementNameSerialiser.class);

    /**
     * True if this element has attributes.
     */ 
    private boolean hasAttributes;
    
    /**
     * True if this element should be <i>treated</i> as if it has children.
     * <p>
     * WBSAX allows you to write empty tags as true empty tags or start and 
     * end tags with no content, so this can be true even if there are no
     * actual children.
     */ 
    private boolean hasChildren;

    /**
     * Flag to help us ensure that we don't forget to call use. The author 
     * managed to do this once, so it seemed prudent to add this.
     */ 
    private boolean used;
    
    /**
     * Construct an instance of this class, with the handler and resolver
     * provided.
     * 
     * @param handler the handler to serialise to.
     * @param resolver the reference resolver to use.
     */ 
    public WBSAXElementNameSerialiser(WBSAXContentHandler handler, 
            ReferenceResolver resolver) {
        super(handler, resolver);
    }

    /**
     * NOTE: This method is not very friendly in that it's very easy to forget
     * to call it before you call 
     * {@link com.volantis.mcs.wbdom.NameVisitor.Acceptor#accept} and 
     * there's no easy way to detect  
     * @param hasAttributes
     * @param hasChildren
     */ 
    public void use(boolean hasAttributes, boolean hasChildren) {
        this.hasAttributes = hasAttributes;
        this.hasChildren = hasChildren;
        used = true;
    }

    public void visitCodeProvider(CodedNameProvider code) 
            throws WBDOMException {
        try {
            if (used) {
                handler.startElement((ElementNameCode) code.getCodedName(),
                        hasAttributes, hasChildren);
                used = false;
            } else {
                throw new IllegalStateException("Must call use before this");
            }
        } catch (WBSAXException e) {
            throw new WBDOMException(
                        exceptionLocalizer.format(
                                    "wbdom-attribute-code-write-error"),
                        e);
        }
    }

    public void visitLiteralProvider(LiteralNameProvider literal) 
            throws WBDOMException {
        try {
            if (used) {
                handler.startElement(resolver.resolve(literal.getLiteralName()), 
                        hasAttributes, hasChildren);
                used = false;
            } else {
                throw new IllegalStateException("Must call use before this");
            }
        } catch (WBSAXException e) {            
            throw new WBDOMException(
                        exceptionLocalizer.format(
                                    "wbdom-element-literal-write-error"),
                        e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/2	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 30-Jun-03	605/1	geoff	VBM:2003060607 port from metis to mimas

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 12-Jun-03	385/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 12-Jun-03	368/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
