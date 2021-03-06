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
import com.volantis.mcs.protocols.forms.FormDataManagerMock;
import com.volantis.mcs.protocols.forms.FormDescriptor;
import com.volantis.mcs.protocols.forms.SessionFormDataMock;
import com.volantis.mcs.protocols.html.XHTMLBasic;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.context.EnvironmentContextMock;
import com.volantis.mcs.runtime.URLConstants;

/**
 * Verifies that {@link XFormEmulationTransformer} transforms XHTML markup
 * as expected.
 */
public class XHTMLBasicXFormEmulationTransformerTestCase extends
                    XFormEmulationTransformerTestAbstract {

    /**
     * Initialize a new instance.
     */
    public XHTMLBasicXFormEmulationTransformerTestCase() {

        inputSimpleSingleFormFileName = "simpleSingleFormHTML.xml";
        inputSimpleMultipleFormsFileName = "simpleMultipleFormsHTML.xml";
        expectedSimpleSingleFormFileName = "expectedSimpleSingleFormHTML.xml";
        expectedSimpleMultipleFormsFileName = "expectedSimpleMultipleFormsHTML.xml";
    }



    // Javadoc inherited.
    public DOMProtocol createProtocol(ProtocolConfiguration protocolConfig,
            ProtocolSupportFactory psf) {
        return new XHTMLBasic(psf, protocolConfig) {
            protected MarinerURL rewriteFormURL(MarinerURL url) {
                return url;
            }
        };
    }

    // Javadoc inherited.
    protected void setSimpleSingleExpectations() {
        
        //super.setSimpleSingleExpectations();
        protocolConfig.expects.supportsEvents().returns(true);

        EnvironmentContextMock environmentContextMock = new EnvironmentContextMock("environmentContextMock", expectations);
        FormDataManagerMock fdm = new FormDataManagerMock("fdm", expectations);
        FormDescriptor fdesc = new FormDescriptor();
        SessionFormDataMock sessionFormData = new SessionFormDataMock(
                "formData", expectations, "s0", fdesc);

        pageContext.expects.getEnvironmentContext().returns(environmentContextMock).any();
        environmentContextMock.expects.getContextPathURL().returns(new MarinerURL("/")).any();

        pageContext.expects.getFormDataManager().returns(fdm).any();

        fdm.expects.getFormSpecifier(null).returns("s0").any();

        pageContext.expects.getFormDataManager().returns(fdm).any();

        fdm.expects.getSessionFormData("s0").returns(sessionFormData);
        sessionFormData.expects.setFieldValue(URLConstants.ACTION_FORM_FRAGMENT, "http://www.volantis.com");
        
        for (int i = 0; i < SINGLE_DEFAULT_FIELDS.length; i++ ) {
            FieldDescriptor fd = new FieldDescriptor();
            fd.setName((String) SINGLE_DEFAULT_FIELDS[i][0]);
            fd.setType((FieldType) SINGLE_DEFAULT_FIELDS[i][1]);
            default_FD.addField(fd);
        }
    }

    // Javadoc inherited.
    protected void setSimpleMultipleExpectations() {
        
        //super.setSimpleMultipleExpectations();
        protocolConfig.expects.supportsEvents().returns(true).any();

        EnvironmentContextMock environmentContextMock = new EnvironmentContextMock("environmentContextMock", expectations);
        FormDataManagerMock fdm = new FormDataManagerMock("fdm", expectations);
        FormDescriptor fdesc = new FormDescriptor();
        SessionFormDataMock sessionFormData = new SessionFormDataMock(
                "formData", expectations, "s0", fdesc);

        pageContext.expects.getEnvironmentContext().returns(environmentContextMock).any();
        environmentContextMock.expects.getContextPathURL().returns(new MarinerURL("/")).any();

        pageContext.expects.getFormDataManager().returns(fdm).any();

        fdm.expects.getFormSpecifier(null).returns("s0").any();

        pageContext.expects.getFormDataManager().returns(fdm).any();

        fdm.expects.getSessionFormData("s0").returns(sessionFormData);
        sessionFormData.expects.setFieldValue(URLConstants.ACTION_FORM_FRAGMENT, "http://www.volantis.com");

        pageContext.expects.getFormDataManager().returns(fdm).any();
        fdm.expects.getSessionFormData("s0").returns(sessionFormData);
        sessionFormData.expects.setFieldValue(URLConstants.ACTION_FORM_FRAGMENT, "http://www.smudge.com");

        fdm.expects.getSessionFormData("s0").returns(sessionFormData);
        sessionFormData.expects.setFieldValue(URLConstants.ACTION_FORM_FRAGMENT, "/delivery");
                
        for (int i = 0; i < MULTIPLE_DEFAULT_FIELDS.length; i++ ) {
            FieldDescriptor fd = new FieldDescriptor();
            fd.setName((String) MULTIPLE_DEFAULT_FIELDS[i][0]);
            fd.setType((FieldType) MULTIPLE_DEFAULT_FIELDS[i][1]);
            default_FD.addField(fd);
        }

        for (int i = 0; i < MULTIPLE_MODEL2_FIELDS.length; i++ ) {
            FieldDescriptor fd = new FieldDescriptor();
            fd.setName((String) MULTIPLE_MODEL2_FIELDS[i][0]);
            fd.setType((FieldType) MULTIPLE_MODEL2_FIELDS[i][1]);
            FD2.addField(fd);
        }

        for (int i = 0; i < MULTIPLE_MODEL3_FIELDS.length; i++ ) {
            FieldDescriptor fd = new FieldDescriptor();
            fd.setName((String) MULTIPLE_MODEL3_FIELDS[i][0]);
            fd.setType((FieldType) MULTIPLE_MODEL3_FIELDS[i][1]);
            FD3.addField(fd);
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
