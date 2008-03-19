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
package com.volantis.mcs.protocols;

import com.volantis.mcs.context.TranscodableUrlResolver;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.themes.StyleComponentURI;
import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleTranscodableURI;
import com.volantis.mcs.themes.StyleURI;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueVisitorStub;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * The default implementation of a content inserter.
 *
 * When instances of this class are being used to process the 'contents'
 * keyword, the following assumptions are made:
 *
 * 1 - content cannot be duplicated, only the first content keyword has effect
 * on the result
 * 2 - if no content keyword present, the original content will be deleted
 */
public class DefaultContentInserter
        extends StyleValueVisitorStub
        implements Inserter {

    private static final StylingFactory STYLING_FACTORY =
            StylingFactory.getDefaultInstance();

    private final DOMProtocol protocol;

    private final PolicyReferenceResolver referenceResolver;
    private final AssetResolver assetResolver;
    private Element parent;
    private List originalNodes;

    public DefaultContentInserter(DOMProtocol protocol,
                                  PolicyReferenceResolver referenceResolver,
                                  AssetResolver assetResolver) {
        this.protocol = protocol;
        this.referenceResolver = referenceResolver;
        this.assetResolver = assetResolver;
    }

    // Javadoc inherited
    public void insert(Element element, StyleValue value) {
        this.parent = element;
        // store the original nodes
        originalNodes = new LinkedList();
        Node node = parent.getHead();
        while (node != null) {
            final Node next = node.getNext();
            node.remove();
            originalNodes.add(node);
            node = next;
        }

        value.visit(this, null);
    }

    // javadoc inherited.
    public void insertPreservingExistingContent(Element element,
                                                StyleValue value) {
        this.parent = element;
        // store the original nodes
        originalNodes = new LinkedList();
        Node node = parent.getHead();
        while (node != null) {
            final Node next = node.getNext();
            originalNodes.add(node);
            node = next;
        }

        value.visit(this, null);
    }

    // Javadoc inherited.
    public void visit(StyleList value, Object object) {
        List list = value.getList();
        for (int i = 0; i < list.size(); i++) {
            StyleValue styleValue = (StyleValue) list.get(i);
            styleValue.visit(this, null);
        }
    }

    // Javadoc inherited.
    public void visit(StyleString value, Object object) {
        String string = value.getString();
        parent.addText(string);
    }

    // Javadoc inherited.
    public void visit(StyleInteger value, Object object) {
        String string = value.getStandardCSS();
        parent.addText(string);
    }

    // Javadoc inherited.
    public void visit(StyleComponentURI value, Object object) {

        try {
            RuntimePolicyReference reference =
                    referenceResolver.resolvePolicyExpression(
                            value.getExpression());

            String url = assetResolver.retrieveVariantURLAsString(reference,
                    null);
            if (url != null) {
                insertImage(parent, url);
            }
        } catch (ProtocolException e) {
            throw new ExtendedRuntimeException(e);
        }
    }

    // Javadoc inherited.
    public void visit(StyleTranscodableURI value, Object object) {
        try {
            final TranscodableUrlResolver urlResolver =
                protocol.getMarinerPageContext().getTranscodableUrlResolver();
            final String url = urlResolver.resolve(value.getUri());
            if (url != null) {
                insertImage(parent, url);
            }
        } catch (RepositoryException e) {
            throw new ExtendedRuntimeException(e);
        } catch (ProtocolException e) {
            throw new ExtendedRuntimeException(e);
        }
    }

    private void insertImage(Element parent, String url)
            throws ProtocolException {

        // Rewrite the URL with PageURLRewriter.
        url = assetResolver.rewriteURLWithPageURLRewriter(url, PageURLType.IMAGE);
        
        ImageAttributes attributes = new ImageAttributes();
        attributes.setSrc(url);
        Styles parentStyles = parent.getStyles();
        Styles styles = STYLING_FACTORY.createInheritedStyles(parentStyles,
                DisplayKeywords.INLINE);
        attributes.setStyles(styles);

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        protocol.doImage(buffer, attributes);
        Element root = buffer.getRoot();
        root.addChildrenToTail(parent);
    }

    // Javadoc inherited.
    public void visit(StyleURI value, Object object) {
        String url = value.getURI();
        try {
            insertImage(parent, url);
        } catch (ProtocolException e) {
            throw new ExtendedRuntimeException(e);
        }
    }

    /**
     * When processing keywords a check needs to be done to see if it is a
     * 'contents' keyword. If it is the processing will follow the rules
     * specified at the top of this class.
     */
    //javadoc inherited
    public void visit(StyleKeyword value, Object object) {
        if (value == StyleKeywords.CONTENTS) {
            for (Iterator iter = originalNodes.iterator(); iter.hasNext();) {
                final Node node = (Node) iter.next();
                node.addToTail(parent);
            }
            originalNodes.clear();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	9440/2	pduffin	VBM:2005070711 Merged changes from main trunk

 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 ===========================================================================
*/
