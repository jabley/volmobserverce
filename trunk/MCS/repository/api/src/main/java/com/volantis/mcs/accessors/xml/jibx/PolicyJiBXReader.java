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

import com.volantis.mcs.repository.xml.PolicySchemas;
import com.volantis.xml.schema.validator.SchemaValidator;

/**
 * A subclass of {@link JiBXReader} which hardcodes schema validation using
 * the LPDM and RPDM schemas.
 * <p>
 * All normal usages of JiBX for reading policies must use schema validation.
 *
 * @mock.generate
 */
public class PolicyJiBXReader
        extends com.volantis.mcs.accessors.xml.jibx.JiBXReader {

    private static SchemaValidator schemaValidator = new SchemaValidator();
    static {
        schemaValidator.addSchemata(PolicySchemas.REPOSITORY_CURRENT);
    }

    /**
     * Initialise.
     * <p>
     * This enables policy schema validation by default.
     * <p>
     * JiBXReader needs an expected class to initiate the reading process and
     * to check the result of read. This class cannot be <code>null</code>.
     *
     * @param expectedClass the class of objects this reader will read
     *
     */
    public PolicyJiBXReader(final Class expectedClass) {
        super(expectedClass, schemaValidator);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 16-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 ===========================================================================
*/
