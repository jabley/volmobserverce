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

import com.volantis.mcs.accessors.common.ComponentWriter;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.xml.PolicySchemas;
import com.volantis.shared.content.ContentInput;
import com.volantis.shared.content.TextContentInput;
import com.volantis.shared.io.CachingWriter;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.schema.validator.SchemaValidator;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * JIBX implementation of the ComponentWriter interface.
 * <p>Used to write a given Theme or ComponentContainer structure to the given
 * writer in XML.</p>
 *
 * @mock.generate 
 */
public class JiBXWriter implements ComponentWriter {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
                LocalizationFactory.createLogger(JiBXWriter.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(JiBXWriter.class);

    private static SchemaValidator repositorySchemaValidator = new SchemaValidator();
    static {
        repositorySchemaValidator.addSchemata(PolicySchemas.REPOSITORY_CURRENT);
    }

    private boolean validate;

    // Javadoc unecessary
    public JiBXWriter() {
        this(true);
    }

    public JiBXWriter(boolean validate) {
        this.validate = validate;
    }

    /**
     * Write the given object to the writer in XML using JIBX.
     * Generated XML is indented by 4 spaces at each element.
     *
     * @param writer destination for generated XML
     * @param object root of the Theme or ComponentContainer structure
     * @throws RepositoryException
     */
    public void write(Writer writer, Object object)
            throws RepositoryException {

        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null.");
        }

        final Writer actualWriter;
        if (validate) {
            // Set up for validation by caching a copy of the marshalled data.
            actualWriter = new CachingWriter(writer);
        } else {
            actualWriter = writer;
        }

        try {
            IBindingFactory bfact =
                    BindingDirectory.getFactory(object.getClass());

            IMarshallingContext mctx = bfact.createMarshallingContext();
            mctx.setIndent(4);
            mctx.marshalDocument(object, "UTF-8", null, actualWriter);

        } catch (Exception e) {
            throw new RepositoryException(exceptionLocalizer.format(
                    "cannot-write-object",
                    new Object[]{object, actualWriter}), e);
        }

        if (validate) {
            // After we do the marshalling, we validate the output as a
            // separate step. This is required as JiBX does not do schema
            // validation during marshalling so we use Xalan to do the
            // validation for us.
            // TODO: should we actually throw the exception here and force the upper
            // layer to recover if recovery is acceptible?
            Reader reader = ((CachingWriter) actualWriter).getCacheReader();
            ContentInput content = new TextContentInput(reader);

            try {
                repositorySchemaValidator.validate(content);
            } catch (SAXException e) {
                // If we have a validation failure, we log and continue.
                logger.error("unexpected-exception", e);
            } catch (IOException e) {
                // Should never get an IO error since we are reading from memory
                logger.error("unexpected-exception", e);
                throw new RepositoryException(e);
            }
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

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 28-Sep-05	9445/10	gkoch	VBM:2005090603 Introduced ComponentContainers to bring components and their assets together

 16-Sep-05	9513/2	adrianj	VBM:2005091408 Represent style values as elements rather than attributes

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 02-Sep-05	9408/1	pabbott	VBM:2005083007 Move over to using JiBX accessor

 29-Jun-05	8552/1	pabbott	VBM:2005051902 JIBX Theme accessors

 09-Jun-05	8552/2	pabbott	VBM:2005051902 Version 1 of JIBX implementation

 ===========================================================================
*/
