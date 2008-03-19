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
package com.volantis.mcs.accessors.xml.jibx;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.PseudoElementSelector;
import com.volantis.mcs.themes.PseudoElementTypeEnum;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import org.jibx.runtime.IAliasable;
import org.jibx.runtime.IMarshaller;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshaller;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.impl.MarshallingContext;
import org.jibx.runtime.impl.UnmarshallingContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Marshall and unmarshall PseudoElement selectors.
 */
public class MarshallPseudoElementSelector implements IMarshaller, IUnmarshaller,
        IAliasable {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(MarshallPseudoElementSelector.class);

    private String uri;
    private String structureName;
    private int nsIndex;

    public MarshallPseudoElementSelector() {
        structureName = "";
        uri = null;
        nsIndex = 0;
    }

    public MarshallPseudoElementSelector(String givenUri, int givenIndex,
                                         String givenName) {
        structureName = givenName;
        uri = givenUri;
        nsIndex = givenIndex;
    }

    // Javadoc interited from interface
    public boolean isExtension(int index) {
        return false;
    }

    public void marshal(Object obj, IMarshallingContext ictx)
            throws JiBXException {

        if (ictx instanceof MarshallingContext) {
            // make sure the parameters are as expected
            if (!(obj instanceof List)) {
                throw new JiBXException(EXCEPTION_LOCALIZER.format("invalid-object-type",
                        obj.getClass()));
            } else {
                // start by generating start tag for container
                MarshallingContext ctx = (MarshallingContext) ictx;
                List classList = (List) obj;

                Iterator selectorIterator = classList.iterator();
                
                // Only process the elements if we have any
                if (selectorIterator.hasNext()) {
                    ctx.startTagAttributes(nsIndex, structureName).
                            closeStartContent();

                    while (selectorIterator.hasNext()) {
                        PseudoElementSelector selector
                                = (PseudoElementSelector) selectorIterator.next();
                        PseudoElementTypeEnum theEnum
                                = selector.getPseudoElementType();
                        String xmlName = theEnum.getType();

                        ctx.startTagAttributes(nsIndex, xmlName).closeStartEmpty();
                    }

                    ctx.endTag(nsIndex, structureName);
                }
            }
        }
    }

    public boolean isPresent(IUnmarshallingContext ictx) throws JiBXException {
        return ictx.isAt(uri, structureName);
    }

    public Object unmarshal(Object obj, IUnmarshallingContext ictx)
            throws JiBXException {

        // Create a new ArrayList, don't reuse the supplied object
        // as it may contain something
        ArrayList elementSelectors = new ArrayList();

        if (ictx instanceof UnmarshallingContext) {


            // make sure we're at the appropriate start tag
            UnmarshallingContext ctx = (UnmarshallingContext) ictx;
            if (!ctx.isAt(uri, structureName)) {
                ctx.throwStartTagNameError(uri, structureName);
            }

            ctx.parsePastStartTag(uri, structureName);

            String elementName;

            do {
                elementName = ctx.toStart();

                PseudoElementSelector newSelector
                        = StyleSheetFactory.getDefaultInstance()
                        .createPseudoElementSelector(elementName);

                elementSelectors.add(newSelector);

                ctx.parsePastEndTag(uri, elementName);


            } while (!ctx.isEnd());

            ctx.parsePastEndTag(uri, structureName);
        }
        return elementSelectors;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10504/1	ianw	VBM:2005112312 Fixed pseudoElements in GUI and JIBX

 29-Nov-05	10484/1	ianw	VBM:2005112312 Fixed pseudoElements in GUI and JIBX

 21-Oct-05	9938/1	ianw	VBM:2005101915 Fix up theme accessor issues

 21-Oct-05	9936/1	ianw	VBM:2005101915 Fix up theme accessor issues

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 05-Jul-05	8552/4	pabbott	VBM:2005051902 JIBX Javadoc updates

 29-Jun-05	8552/2	pabbott	VBM:2005051902 JIBX Theme accessors

 ===========================================================================
*/
