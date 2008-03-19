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
package com.volantis.mcs.repository.xml;

import com.volantis.xml.schema.SchemaDefinition;
import com.volantis.xml.schema.Schemata;
import com.volantis.xml.schema.W3CSchemata;

/**
 * The definitions of the XML Schemata that MCS uses for policy files.
 * <P>
 * Currently this contains RPDM and LPDM schema definitions.
 */
public class PolicySchemas {

    //
    // MARLIN RPDM SCHEMA CONSTANTS
    // (Remote Policy Description Module)
    //
                                      
    // TODO: see RepositoryEntityResolver for previous RPDM versions.

    // Marlin 2-7-1
    public static final SchemaDefinition MARLIN_RPDM_2_7_1_DTD =
            new SchemaDefinition(
                    null,
                    "http://www.volantis.com/dtd/2-7-1/marlin-rpdm.dtd",
                    "com/volantis/mcs/dtd/2-7-1/marlin-rpdm.dtd");

    // Marlin Latest (Actually same as 2-7-1)
    public static final SchemaDefinition MARLIN_RPDM_LATEST_DTD =
            new SchemaDefinition(
                    null,
                    "http://www.volantis.com/dtd/latest/marlin-rpdm.dtd",
                    "com/volantis/mcs/dtd/latest/marlin-rpdm.dtd");

    // Marlin RPDM DTDS
    public static final Schemata MARLIN_RPDM_DTDS;

    // 2005/09 (v3.5)
    public static final SchemaDefinition MARLIN_RPDM_2005_09 =
            new SchemaDefinition(
                    "http://www.volantis.com/xmlns/2005/09/marlin-rpdm",
                    "http://www.volantis.com/schema/2005/09/marlin-rpdm.xsd",
                    "com/volantis/schema/2005/09/marlin-rpdm.xsd");

    // 2005/12 (v3.5)
    public static final SchemaDefinition MARLIN_RPDM_2005_12 =
            new SchemaDefinition(
                    "http://www.volantis.com/xmlns/2005/12/marlin-rpdm",
                    "http://www.volantis.com/schema/2005/12/marlin-rpdm.xsd",
                    "com/volantis/schema/2005/12/marlin-rpdm.xsd");

    // 2006/02 (v3.5)
    public static final SchemaDefinition MARLIN_RPDM_2006_02 =
            new SchemaDefinition(
                    "http://www.volantis.com/xmlns/2006/02/marlin-rpdm",
                    "http://www.volantis.com/schema/2006/02/marlin-rpdm.xsd",
                    "com/volantis/schema/2006/02/marlin-rpdm.xsd");

    // Current = 2006/02
    public static final SchemaDefinition MARLIN_RPDM_CURRENT =
            MARLIN_RPDM_2006_02;

    //
    // MARLIN LPDM SCHEMA CONSTANTS
    // (Local Policy Description Module)
    //

    // v3.0
    public static final SchemaDefinition MARLIN_LPDM_V3_0 =
            new SchemaDefinition(
                    "http://www.volantis.com/xmlns/marlin-lpdm",
                    "http://www.volantis.com/schema/v3.0/marlin-lpdm.xsd",
                    "com/volantis/schema/v3.0/marlin-lpdm.xsd");

    // NOTE: at 3.5 time we moved to date based versioning.
    // This is better as we know ahead of time what to use for the id
    // and also often things do not change for a new release anyway.

    // 2005/09 (v3.5)
    public static final SchemaDefinition MARLIN_LPDM_2005_09 =
            new SchemaDefinition(
                    "http://www.volantis.com/xmlns/2005/09/marlin-lpdm",
                    "http://www.volantis.com/schema/2005/09/marlin-lpdm.xsd",
                    "com/volantis/schema/2005/09/marlin-lpdm.xsd");

    // 2005/12
    public static final SchemaDefinition MARLIN_LPDM_2005_12 =
            new SchemaDefinition(
                    "http://www.volantis.com/xmlns/2005/12/marlin-lpdm",
                    "http://www.volantis.com/schema/2005/12/marlin-lpdm.xsd",
                    "com/volantis/schema/2005/12/marlin-lpdm.xsd");

    // 2006/02
    public static final SchemaDefinition MARLIN_LPDM_2006_02 =
            new SchemaDefinition(
                    "http://www.volantis.com/xmlns/2006/02/marlin-lpdm",
                    "http://www.volantis.com/schema/2006/02/marlin-lpdm.xsd",
                    "com/volantis/schema/2006/02/marlin-lpdm.xsd");

    // Current = 2006/02
    public static final SchemaDefinition MARLIN_LPDM_CURRENT =
            MARLIN_LPDM_2006_02;

    // META DATA TYPES

    public static final SchemaDefinition META_DATA_TYPES_2004_12 =
            new SchemaDefinition(
                    "http://www.volantis.com/xmlns/2004/12/meta-data-types",
                    "http://www.volantis.com/schema/2004/12/meta-data-types.xsd",
                    "com/volantis/schema/2004/12/meta-data-types.xsd");

    public static final SchemaDefinition META_DATA_TYPES_CURRENT =
            META_DATA_TYPES_2004_12;

    // META DATA VALUES

    public static final SchemaDefinition META_DATA_VALUES_2004_12 =
            new SchemaDefinition(
                    "http://www.volantis.com/xmlns/2004/12/meta-data-values",
                    "http://www.volantis.com/schema/2004/12/meta-data-values.xsd",
                    "com/volantis/schema/2004/12/meta-data-values.xsd");

    public static final SchemaDefinition META_DATA_VALUES_CURRENT =
            META_DATA_VALUES_2004_12;

    // Collections of schemata.
    public static final Schemata LOCAL_REPOSITORY_2005_09;
    public static final Schemata LOCAL_REPOSITORY_2005_12;
    public static final Schemata LOCAL_REPOSITORY_2006_02;
    public static final Schemata LOCAL_REPOSITORY_CURRENT;
    public static final Schemata REMOTE_REPOSITORY_2005_09;
    public static final Schemata REMOTE_REPOSITORY_2005_12;
    public static final Schemata REMOTE_REPOSITORY_2006_02;
    public static final Schemata REMOTE_REPOSITORY_CURRENT;
    public static final Schemata REPOSITORY_2005_09;
    public static final Schemata REPOSITORY_2005_12;
    public static final Schemata REPOSITORY_2006_02;
    public static final Schemata REPOSITORY_CURRENT;

    static {
        Schemata schemata;

        // --------------------------------------------------------------------
        //     LPDM
        // --------------------------------------------------------------------

        // 2005/09
        schemata = new Schemata();
        schemata.addSchema(MARLIN_LPDM_2005_09);
        schemata.addSchema(META_DATA_VALUES_2004_12);
        schemata.addSchema(META_DATA_TYPES_2004_12);
        LOCAL_REPOSITORY_2005_09 = schemata;

        // 2005/12
        schemata = new Schemata();
        schemata.addSchema(MARLIN_LPDM_2005_12);
        schemata.addSchema(META_DATA_VALUES_2004_12);
        schemata.addSchema(META_DATA_TYPES_2004_12);
        LOCAL_REPOSITORY_2005_12 = schemata;

        // 2006/02
        schemata = new Schemata();
        schemata.addSchema(MARLIN_LPDM_2006_02);
        schemata.addSchema(META_DATA_VALUES_2004_12);
        schemata.addSchema(META_DATA_TYPES_2004_12);
        LOCAL_REPOSITORY_2006_02 = schemata;

        // Current
        LOCAL_REPOSITORY_CURRENT = LOCAL_REPOSITORY_2006_02;

        // --------------------------------------------------------------------
        //     RPDM
        // --------------------------------------------------------------------

        // 2-7-1
        schemata = new Schemata();
        schemata.addSchema(MARLIN_RPDM_2_7_1_DTD);
        schemata.addSchema(MARLIN_RPDM_LATEST_DTD);
        schemata.addSchemata(W3CSchemata.XHTML_ENTITIES);
        MARLIN_RPDM_DTDS = schemata;

        // 2005/09
        schemata = new Schemata();
        schemata.addSchemata(LOCAL_REPOSITORY_2005_09);
        schemata.addSchema(MARLIN_RPDM_2005_09);
        REMOTE_REPOSITORY_2005_09 = schemata;

        // 2005/12
        schemata = new Schemata();
        schemata.addSchemata(LOCAL_REPOSITORY_2005_12);
        schemata.addSchema(MARLIN_RPDM_2005_12);
        REMOTE_REPOSITORY_2005_12 = schemata;

        // 2006/02
        schemata = new Schemata();
        schemata.addSchemata(LOCAL_REPOSITORY_2006_02);
        schemata.addSchema(MARLIN_RPDM_2006_02);
        REMOTE_REPOSITORY_2006_02 = schemata;

        // Current
        REMOTE_REPOSITORY_CURRENT = REMOTE_REPOSITORY_2006_02;

        // --------------------------------------------------------------------
        //     Repository
        // --------------------------------------------------------------------
        REPOSITORY_2005_09 = REMOTE_REPOSITORY_2005_09;
        REPOSITORY_2005_12 = REMOTE_REPOSITORY_2005_12;
        REPOSITORY_2006_02 = REMOTE_REPOSITORY_2006_02;
        REPOSITORY_CURRENT = REMOTE_REPOSITORY_CURRENT;
    }

    /**
     * This class cannot be constructed. It is only for statics.
     */
    private PolicySchemas() {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 13-Nov-05	9896/2	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 ===========================================================================
*/
