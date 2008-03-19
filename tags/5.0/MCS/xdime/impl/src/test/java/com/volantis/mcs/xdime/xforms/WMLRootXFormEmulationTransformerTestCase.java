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

import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.protocols.forms.FieldType;
import com.volantis.mcs.protocols.forms.ImplicitFieldType;
import com.volantis.mcs.protocols.wml.WMLRoot;
import com.volantis.mcs.utilities.MarinerURL;

/**
 * Verifies that {@link XFormEmulationTransformer} transforms WML markup
 * as expected.
 */
public class WMLRootXFormEmulationTransformerTestCase extends
        XFormEmulationTransformerTestAbstract{

    /**
     * Initialize a new instance.
     */
    public WMLRootXFormEmulationTransformerTestCase() {
        
        inputSimpleSingleFormFileName = "simpleSingleFormWML.xml";
        inputSimpleMultipleFormsFileName = "simpleMultipleFormsWML.xml";
        expectedSimpleSingleFormFileName = "expectedSimpleSingleFormWML.xml";
        expectedSimpleMultipleFormsFileName = "expectedSimpleMultipleFormsWML.xml";
    }

    // Javadoc inherited.
    public DOMProtocol createProtocol(ProtocolConfiguration protocolConfig,
            ProtocolSupportFactory psf) {
        return new MyWMLRoot(psf, protocolConfig);
    }

    // Javadoc inherited.
    protected void setSimpleSingleExpectations() {
        for (int i = 0; i < SINGLE_DEFAULT_FIELDS.length; i++ ) {
            FieldDescriptor fd = new FieldDescriptor();
            fd.setName((String) SINGLE_DEFAULT_FIELDS[i][0]);
            fd.setType((FieldType) SINGLE_DEFAULT_FIELDS[i][1]);
            if(SINGLE_DEFAULT_FIELDS[i][1] instanceof ImplicitFieldType){
                fd.setInitialValue("Unreferenced value");
            }
            default_FD.addField(fd);
        }
    }

    // Javadoc inherited.
    protected void setSimpleMultipleExpectations() {
        for (int i = 0; i < MULTIPLE_DEFAULT_FIELDS.length; i++ ) {
            FieldDescriptor fd = new FieldDescriptor();
            fd.setName((String) MULTIPLE_DEFAULT_FIELDS[i][0]);
            fd.setType((FieldType) MULTIPLE_DEFAULT_FIELDS[i][1]);
            if(MULTIPLE_DEFAULT_FIELDS[i][1] instanceof ImplicitFieldType){
                fd.setInitialValue("Unreferenced value - value");
            }
            default_FD.addField(fd);
        }

        for (int i = 0; i < MULTIPLE_MODEL2_FIELDS.length; i++ ) {
            FieldDescriptor fd = new FieldDescriptor();
            fd.setName((String) MULTIPLE_MODEL2_FIELDS[i][0]);
            fd.setType((FieldType) MULTIPLE_MODEL2_FIELDS[i][1]);
            if(MULTIPLE_MODEL2_FIELDS[i][1] instanceof ImplicitFieldType){
                fd.setInitialValue("UnreferencedValueTwoValue");
            }
            FD2.addField(fd);
        }

        for (int i = 0; i < MULTIPLE_MODEL3_FIELDS.length; i++ ) {
            FieldDescriptor fd = new FieldDescriptor();
            fd.setName((String) MULTIPLE_MODEL3_FIELDS[i][0]);
            fd.setType((FieldType) MULTIPLE_MODEL3_FIELDS[i][1]);
            FD3.addField(fd);
        }

    }

    class MyWMLRoot extends WMLRoot {

        public MyWMLRoot(ProtocolSupportFactory supportFactory,
                ProtocolConfiguration configuration) {
            super(supportFactory, configuration);
            // reset the variableStyle because the real value ('\u0001') causes
            // the parser to fall over. It will be replaced with ${ and } in
            // real operation at a later stage than we are testing
            variableStyle = '_';
        }

        protected MarinerURL rewriteFormURL(MarinerURL url) {
            // override this because it's not important to our test and would
            // require that we configured a lot more of MCS.
            return url;
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9637/1	emma	VBM:2005092807 Adding tests for XForms emulation

 ===========================================================================
*/
