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

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.shared.content.CachingContentInput;
import com.volantis.shared.content.ContentInput;
import com.volantis.shared.jibx.ContentUnmarshaller;
import com.volantis.shared.throwable.ExtendedIOException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.schema.validator.SchemaValidator;
import org.jibx.runtime.JiBXException;
import org.xml.sax.SAXException;

import java.io.IOException;


/**
 * Used to read a repository object structure from a given Reader object.
 *
 * @mock.generate 
 */
public class JiBXReader {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(JiBXReader.class);

    private final Class expectedClass;

    private SchemaValidator schemaValidator;

    /**
     * Initialise.
     * <p>
     * Note that no validation will be done by default.
     * <p>
     * JiBXReader needs an expected class to initiate the reading process and
     * to check the result of read. This class cannot be <code>null</code>.
     *
     * @param expectedClass the class of objects this reader will read
     */
    public JiBXReader(Class expectedClass) {
        this(expectedClass, null);
    }

    /**
     * Initialise.
     *
     * @param expectedClass     the class that will be read by this reader
     * @param validator         the SchemaValidator to use
     */
    public JiBXReader(Class expectedClass, SchemaValidator validator) {

        if (expectedClass == null) {
            throw new IllegalArgumentException(
                    "Expected class cannot be null.");
        }
        this.expectedClass = expectedClass;
        this.schemaValidator = validator;
    }

    /**
     * Unmarshall a Theme or ComponentContainer object structure using JIBX.
     * JIBX will expected to be able to read valid XML from the supplied
     * Reader. The reader should provide the complete XML document including
     * the header and namespace.
     *
     * @param content from which the XML will be read
     * @param name
     * @return a Theme or ComponentContainer object
     * @throws IOException
     */
    public Object read(ContentInput content, String name)
            throws IOException {

        try {

            if (schemaValidator != null) {
                // Before we start unmarshalling, we validate the input as a
                // separate step. This is required as JiBX does not do schema
                // validation during marshalling so we use Xalan to do the
                // validation for us.

                // Create a buffer that will let us read the content twice.
                CachingContentInput cachingContent = new CachingContentInput(
                        content);

                // Validate the content to ensure it is valid LPDM.
                schemaValidator.validate(cachingContent);
                // If the validation fails, an exception will be thrown here
                // and the reading will thus fail.

                content = cachingContent.getCachedContent();
            }

            Object readObject = null;

            ContentUnmarshaller contentUnmarshaller =
                    new ContentUnmarshaller(expectedClass);
            readObject = contentUnmarshaller.unmarshallContent(content, name);

            // Check that we got what we expected, it should be if jibx is
            // working correctly.
//            if (!expectedClass.isAssignableFrom(readObject.getClass())) {
//                throw new RepositoryException(exceptionLocalizer.format(
//                                "wrong-object-type-read",
//                                readObject));
//            }

            return readObject;
        } catch (JiBXException e) {
            throw new ExtendedIOException(exceptionLocalizer.format(
                    "cannot-read-object", new Object[]{
                        expectedClass.getName(), content}), e);
        } catch (SAXException e) {
            throw new ExtendedIOException(exceptionLocalizer.format(
                    "cannot-read-object", new Object[]{
                        expectedClass.getName(), content}), e);
        }

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 09-Dec-05	10756/1	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 09-Dec-05	10738/1	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 16-Nov-05	9896/6	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 16-Nov-05	9896/4	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 14-Nov-05	10287/2	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.
 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 26-Oct-05	9961/1	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 28-Sep-05	9445/8	gkoch	VBM:2005090603 Introduced ComponentContainers to bring components and their assets together

 15-Sep-05	9512/2	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 05-Jul-05	8552/3	pabbott	VBM:2005051902 JIBX Javadoc updates

 09-Jun-05	8552/1	pabbott	VBM:2005051902 Version 1 of JIBX implementation

 ===========================================================================
*/
