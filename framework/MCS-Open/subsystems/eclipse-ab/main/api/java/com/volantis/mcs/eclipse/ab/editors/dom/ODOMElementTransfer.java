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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.editors.dom;

import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.eclipse.common.odom.input.VolantisSAXBuilder;
import com.volantis.synergetics.UndeclaredThrowableException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * A class used for clipboard transfers of ODOMElements.
 */
public class ODOMElementTransfer extends ByteArrayTransfer {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(ODOMElementTransfer.class);
    

    /**
     * A root element to allow us to read in mulitple copied odom elements.
     */
    private static final String COPIED_ROOT_START = "<copied_element>"; //$NON-NLS-1$
    private static final String COPIED_ROOT_END = "</copied_element>"; //$NON-NLS-1$

    /**
     * The type names for ODOMElementTransfer.
     */
    private final String[] typeNames;

    /**
     * The ids of the ODOMElement type.
     */
    private final int[] typeIDs;

    /**
     * The ODOMFactory used to read in ODOMElements.
     */
    private final ODOMFactory factory;

    /**
     * Construct a new ODOMElementTransfer with a specified ODOMFactory.
     *
     * @param factory     The ODOMFactory.
     * @param elementName the element name that will be used to uniquely
     *                    identify this transfer type.
     */
    public ODOMElementTransfer(ODOMFactory factory, String elementName) {
        this.factory = factory;
        typeNames = new String[]{elementName};
        typeIDs = new int[]{registerType(elementName)};
    }

    /**
     * Convert an ODOMElement to native bytes using an XMLOutputter.
     */
    // rest of javadoc inherited
    public void javaToNative(Object object, TransferData transferData) {

        if (logger.isDebugEnabled()) {
            logger.debug("javaToNative: object=" + object + //$NON-NLS-1$
                    ", transferData=" + transferData); //$NON-NLS-1$
            logger.debug("javaToNative: transferData supported=" + //$NON-NLS-1$
                    isSupportedType(transferData));
            logger.debug("javaToNative: object class=" + object.getClass()); //$NON-NLS-1$
        }

        if (object != null && object instanceof ODOMElement[]) {
            if (isSupportedType(transferData)) {
                ODOMElement [] elements = (ODOMElement []) object;
                try {
                    // Write data to a byte array and then ask super to convert
                    // to pMedium (copied this java doc - don't know what
                    // pmedium is!)
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    out.write(COPIED_ROOT_START.getBytes());
                    XMLOutputter outputter = new XMLOutputter();

                    for(int i=0; i<elements.length; i++) {
                        outputter.output(elements[i], out);
                    }
                    out.write(COPIED_ROOT_END.getBytes());

                    byte[] buffer = out.toByteArray();
                    out.close();

                    super.javaToNative(buffer, transferData);

                } catch (IOException e) {
                    throw new UndeclaredThrowableException(e);
                }
            }
        }
    }

    /**
     * Convert native bytes to an ODOMElement a SAXBuilder.
     */
    // rest of javadoc inherited
    public Object nativeToJava(TransferData transferData) {
        if (logger.isDebugEnabled()) {
            logger.debug("nativeToJava: transferData=" + transferData); //$NON-NLS-1$
            logger.debug("nativeToJava: transferData supported=" + //$NON-NLS-1$
                    isSupportedType(transferData));
        }

        ODOMElement[] elements = null;

        if (isSupportedType(transferData)) {

            byte[] buffer = (byte[]) super.nativeToJava(transferData);

            if (buffer != null) {
                try {
                    ByteArrayInputStream in = new ByteArrayInputStream(buffer);

                    // Create a non-validating builder. Validation is not
                    // required here since it is the target document that needs
                    // validation and not the clipboard content.
                    // Note that this builder is JRE 1.4 and Eclipse friendly
                    SAXBuilder builder = new VolantisSAXBuilder(false);

                    builder.setFactory(factory);

                    Document document = builder.build(in);

                    Element rootElement = document.getRootElement();


                    List children = rootElement.getChildren();
                    elements = new ODOMElement[children.size()];
                    int i=0;
                    while(children.size()>0) {
                        ODOMElement element = (ODOMElement) children.get(0);
                        elements[i] = element;
                        element.detach();
                        i++;
                    }
                } catch (IOException e) {
                    throw new UndeclaredThrowableException(e);
                } catch (JDOMException e) {
                    throw new UndeclaredThrowableException(e);
                }
            }
        }

        return elements;
    }

    // javadoc inherited
    protected String[] getTypeNames() {
        return typeNames;
    }

    // javadoc inherited
    protected int[] getTypeIds() {
        return typeIDs;
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

 13-Feb-04	3023/1	philws	VBM:2004010901 Ensure that the Volantisized XERCES parser is explicitly used for compatibility with Eclipse under JRE 1.4

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 06-Jan-04	2391/3	byron	VBM:2003121726 Addressed variable naming and garbage creation

 06-Jan-04	2391/1	byron	VBM:2003121726 Assets can be pasted into components where they are not valid

 16-Dec-03	2213/1	allan	VBM:2003121401 More editors and fixes for presentable values.

 12-Dec-03	2123/1	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 ===========================================================================
*/
