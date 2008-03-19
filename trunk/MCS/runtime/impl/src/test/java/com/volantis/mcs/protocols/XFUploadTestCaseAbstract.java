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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.protocols.forms.FormDescriptor;
import com.volantis.mcs.protocols.forms.UploadFieldType;
import com.volantis.mcs.protocols.layouts.FormInstanceMock;
import com.volantis.mcs.protocols.layouts.PaneInstanceMock;

/**
 * Base class for testing XFUpload element against different protocols
 * 
 * Derived classes should:
 * <ul>
 * <li>implement createProtocol() method that creates the protocol 
 *     to test against
 * <li>implement testForm() method that runs the test. 
 *     The method should call doTestForm() and then check the results. 
 * </ul>     
 */
abstract public class XFUploadTestCaseAbstract extends MarkupTestCaseAbstract {
            
    protected TestDOMOutputBuffer formDOM = new TestDOMOutputBuffer();
    protected TestDOMOutputBuffer uploadDOM = new TestDOMOutputBuffer();

    protected PaneInstanceMock paneInstance;
    protected FormInstanceMock formInstance;
    
    protected XFFormAttributes formAttrs;
    protected XFUploadAttributes uploadAttrs;
    
    // Javadoc inherited
    protected void setUp() throws Exception {
        
        // Let the base class create basic mocks 
        super.setUp();        


        // Create format instance for a pane that will receive
        // the markup generated for upload element.
        paneInstance = new PaneInstanceMock(
                "paneInstance", expectations, NDimensionalIndex.ZERO_DIMENSIONS);        
        paneInstance
            .expects.getCurrentBuffer()
            .returns(uploadDOM).any();        
        paneInstance
            .expects.endCurrentBuffer();            
        
        
        // Create format instance for the enclosing form 
        formInstance = new FormInstanceMock(
                "mockFormInstance", expectations, NDimensionalIndex.ZERO_DIMENSIONS);                
        formInstance
            .expects.getContentBuffer(true)
            .returns(formDOM).any();                
        formInstance
            .expects.isFragmented()
            .returns(false).any();
        
        marinerPageContextMock
            .fuzzy.getFormatInstance(
                    mockFactory.expectsInstanceOf(Form.class),
                    NDimensionalIndex.ZERO_DIMENSIONS)
            .returns(formInstance).any();


        // Create descriptor for the upload element
        FieldDescriptor uploadDescriptor = new FieldDescriptor(); 
        uploadDescriptor.setType(UploadFieldType.getSingleton());
        uploadDescriptor.setName("fileToUpload");

        // Create descriptor for the enclosing form
        FormDescriptor formDescriptor = new FormDescriptor();
        formDescriptor.addField(uploadDescriptor);

        // Create atributes of the upload element
        uploadAttrs = new XFUploadAttributes();
        uploadAttrs.setFieldDescriptor(uploadDescriptor);
        uploadAttrs.setName(uploadDescriptor.getName());
        uploadAttrs.setEntryContainerInstance(paneInstance);

        // Create attributes of the enclosing form
        formAttrs =  new XFFormAttributes();
        formAttrs.setFormDescriptor(formDescriptor);
        formAttrs.setName("myForm");
        formAttrs.setMethod("post");        
        formAttrs.setFormData(formInstance);
        formAttrs.setFormSpecifier("s0");
        formAttrs.addField(uploadAttrs);
        uploadAttrs.setFormData(formInstance);
        uploadAttrs.setFormAttributes(formAttrs);
        
        // Set additional expectations        
        marinerPageContextMock
            .fuzzy.getBooleanDevicePolicyValue(mockFactory.expectsAny())
            .returns(false).any();

        marinerPageContextMock
            .fuzzy.getDevicePolicyValue(mockFactory.expectsAny())
            .returns(null).any();

        marinerPageContextMock
            .expects.getFormDataManager()
            .returns(null).any();

        marinerPageContextMock
            .expects.getFormFragmentResetState()
            .returns(true).any();
        
        marinerPageContextMock
            .expects.clearFormFragmentResetState();
    }

    /**
     * Executes the actual test 
     */
    protected void doTestForm() throws ProtocolException {
        protocol.doForm(formAttrs);
    }
}
