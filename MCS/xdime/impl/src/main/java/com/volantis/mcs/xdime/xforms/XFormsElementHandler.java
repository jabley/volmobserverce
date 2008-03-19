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
package com.volantis.mcs.xdime.xforms;

import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElement;
import com.volantis.mcs.xdime.initialisation.ElementFactory;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapBuilder;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapPopulator;

/**
 * Define mapping between XForm elements and their factories.
 */
public class XFormsElementHandler extends ElementFactoryMapPopulator {

    // Javadoc inherited.
    public void populateMap(ElementFactoryMapBuilder builder) {

        builder.addMapping(XFormElements.MODEL, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new XFModelElementImpl(context);
             }
        });
        builder.addMapping(XFormElements.SUBMISSION, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new XFSubmissionElementImpl(context);
             }
        });
        builder.addMapping(XFormElements.INSTANCE, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new XFInstanceElementImpl(context);
             }
        });
        builder.addMapping(XFormElements.INPUT, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new XFInputElementImpl(context);
             }
        });
        builder.addMapping(XFormElements.ITEM, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new XFItemElementImpl(context);
             }
        });
        builder.addMapping(XFormElements.LABEL, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new XFLabelElementImpl(context);
             }
        });
        builder.addMapping(XFormElements.VALUE, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new XFValueElementImpl(context);
             }
        });
        builder.addMapping(XFormElements.SECRET, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new XFSecretElementImpl(context);
             }
        });
        builder.addMapping(XFormElements.SELECT, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new XFSelectElementImpl(context);
             }
        });
        builder.addMapping(XFormElements.SELECT1, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new XFSelect1ElementImpl(context);
             }
        });        
        builder.addMapping(XFormElements.SETVALUE, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new XFSetValueElementImpl(context);
             }
        });
        builder.addMapping(XFormElements.SUBMIT, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new XFSubmitElementImpl(context);
             }
        });
        builder.addMapping(XFormElements.TEXTAREA, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new XFTextAreaElementImpl(context);
             }
        });
        builder.addMapping(XFormElements.GROUP, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new XFGroupElementImpl(context);
             }
        });
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/3	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 02-Oct-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/
