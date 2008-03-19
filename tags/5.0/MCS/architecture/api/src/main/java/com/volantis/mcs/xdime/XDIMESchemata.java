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

package com.volantis.mcs.xdime;

import com.volantis.xml.schema.SchemaDefinition;
import com.volantis.xml.schema.Schemata;

/**
 * Encapsulates information about the different XDIME-CP / 2 schemata.
 *
 * @todo add simple initialisation schemata. XDIME MCS Schemata
 */
public class XDIMESchemata {

    /**
     * The default CDM prefix, this is used in selectors to associate a prefix
     * with a namespace, do not use this for anything else.
     */
    public static final String DEFAULT_CDM_PREFIX = "cdm";

    /**
     * The default XHTML 2 prefix, this is used in selectors to associate a
     * prefix with a namespace, do not use this for anything else.
     */
    public static final String DEFAULT_XHTML2_PREFIX = "xhtml2";

    /**
     * The default XForms prefix, this is used in selectors to associate a
     * prefix with a namespace, do not use this for anything else.
     */
    public static final String DEFAULT_XFORMS_PREFIX = "xforms";
    
    /**
     * The default Widgets prefix, this is used in selectors to associate a
     * prefix with a namespace, do not use this for anything else.
     */
    public static final String DEFAULT_WIDGETS_PREFIX = "widget";
    
    /**
     * The default Widgets prefix, this is used in selectors to associate a
     * prefix with a namespace, do not use this for anything else.
     */
    public static final String DEFAULT_RESPONSE_PREFIX = "widget-response";
    
    /**
     * The default Ticker prefix, this is used in selectors to associate a
     * prefix with a namespace, do not use this for anything else.
     */
    public static final String DEFAULT_TICKER_PREFIX = "ticker";

    /**
     * The default Ticker prefix, this is used in selectors to associate a
     * prefix with a namespace, do not use this for anything else.
     */
    public static final String DEFAULT_TICKER_RESPONSE_PREFIX = "ticker-response";

    /**
     * The default Gallery Widget prefix, this is used in selectors to associate a
     * prefix with a namespace, do not use this for anything else.
     */
    public static final String DEFAULT_GALLERY_PREFIX = "gallery";
    
    /**
     * The marlin CDM (XDIME 1) namespace.
     */
    public static final String CDM_NAMESPACE =
            "http://www.volantis.com/xmlns/marlin-cdm";

    /**
     * The DISelect name space.
     */
    public static final String DISELECT_NAMESPACE =
            "http://www.w3.org/2004/06/diselect";
    /**
     * The XForms name space.
     */
    public static final String XFORMS_NAMESPACE =
            "http://www.w3.org/2002/xforms";

    /**
     * The XDIME CP Simple Initialisation namespace.
     */
    public static final String XDIME_CP_SIMPLE_INITIALISATION_NAMESPACE =
            "http://www.volantis.com/xmlns/2004/06/xdimecp/si";

    /**
     * The XDIME CP Interim Simple Initialisation namespace.
     */
    public static final String XDIME_CP_INTERIM_SIMPLE_INITIALISATION_NAMESPACE =
            "http://www.volantis.com/xmlns/2004/06/xdimecp/interim/si";
    /**
     * The XHTML2 namespace. Required when querying attribute values etc.
     */
    public static final String XHTML2_NAMESPACE =
            "http://www.w3.org/2002/06/xhtml2";


    public static final String XDIME2_SIMPLE_INITIALISATION_NAMESPACE =
            "http://www.volantis.com/xmlns/2006/01/xdime2/si";

    /**
     * The XDIME 2 MCS Namespace.
     */
    public static final String XDIME2_MCS_NAMESPACE =
            "http://www.volantis.com/xmlns/2006/01/xdime/mcs";


    /**
     * The Widgets of Framework Client name space.
     */
    public static final String WIDGETS_NAMESPACE =
            "http://www.volantis.com/xmlns/2006/05/widget";

    /**
     * The Widgets Response of Framework Client name space.
     */
    public static final String RESPONSE_NAMESPACE =
            "http://www.volantis.com/xmlns/2006/05/widget/response";

    /**
     * The Ticker Client name space.
     */
    public static final String TICKER_NAMESPACE =
            "http://www.volantis.com/xmlns/2006/09/ticker";

    /**
     * The Ticker Client Response name space.
     */
    public static final String TICKER_RESPONSE_NAMESPACE =
            "http://www.volantis.com/xmlns/2006/09/ticker/response";

    /**
     * The XML events namespace.
     */
    public static final String XML_EVENTS_NAMESPACE =
            "http://www.w3.org/2001/xml-events";

    /**
     * The Gallery Widget name space.
     */
    public static final String GALLERY_NAMESPACE =
            "http://www.volantis.com/xmlns/2006/10/gallery-widget";
    
    /**
     * The schema definition for XDIME 2 schema which has the same namespace
     * as XHTML 2 but includes some other schemata.
     */
    public static final SchemaDefinition XDIME2 = new SchemaDefinition(
            XHTML2_NAMESPACE,
            "http://www.volantis.com/schema/2006/01/xdime2/xdime2.xsd",
            "com/volantis/schema/2006/01/xdime2/xdime2.xsd");

    /**
     * The schema definition for the subset of XHTML 2 used by XDIME 2.
     */
    public static final SchemaDefinition XDIME2_XHTML2 = new SchemaDefinition(
            XHTML2_NAMESPACE,
            "http://www.volantis.com/schema/2006/01/xdime2/xhtml2.xsd",
            "com/volantis/schema/2006/01/xdime2/xhtml2.xsd");

    /**
     * The schema definition for the subset of XForms used by XDIME 2.
     */
    public static final SchemaDefinition XDIME2_XFORMS = new SchemaDefinition(
            XFORMS_NAMESPACE,
            "http://www.volantis.com/schema/2006/01/xdime2/xforms.xsd",
            "com/volantis/schema/2006/01/xdime2/xforms.xsd");

    /**
     * The schema definition for XDIME 2 instance of the DI Select schema.
     */
    public static final SchemaDefinition XDIME2_DI_SELECT = new SchemaDefinition(
            DISELECT_NAMESPACE,
            "http://www.volantis.com/schema/2006/01/xdime2/diselect.xsd",
            "com/volantis/schema/2006/01/xdime2/diselect.xsd");

    /**
     * The schema definition for XDIME 2 instance of the MCS schema.
     */
    public static final SchemaDefinition XDIME2_MCS = new SchemaDefinition(
            XDIME2_MCS_NAMESPACE,
            "http://www.volantis.com/schema/2006/01/xdime2/xdime2-mcs.xsd",
            "com/volantis/schema/2006/01/xdime2/xdime2-mcs.xsd");

    /**
     * The schema definition for XDIME CP schema which has the same namespace
     * as XHTML 2 but includes some other schemata.
     */
    public static final SchemaDefinition XDIME_CP = new SchemaDefinition(
            XHTML2_NAMESPACE,
            "http://www.volantis.com/schema/2004/06/xdime-cp/xdime-cp.xsd",
            "com/volantis/schema/2004/06/xdime-cp/xdime-cp.xsd");

    /**
     * The schema definition for the subset of XHTML 2 used by XDIME CP.
     */
    public static final SchemaDefinition XDIME_CP_XHTML2 = new SchemaDefinition(
            XHTML2_NAMESPACE,
            "http://www.volantis.com/schema/2004/06/xdime-cp/xhtml2.xsd",
            "com/volantis/schema/2004/06/xdime-cp/xhtml2.xsd");

    /**
     * The schema definition for the subset of XForms used by XDIME CP.
     */
    public static final SchemaDefinition XDIME_CP_XFORMS = new SchemaDefinition(
            XHTML2_NAMESPACE,
            "http://www.volantis.com/schema/2004/06/xdime-cp/xforms.xsd",
            "com/volantis/schema/2004/06/xdime-cp/xforms.xsd");

    /**
     * The schema definition for XDIME CP instance of the DI Select schema.
     */
    public static final SchemaDefinition XDIME_CP_DI_SELECT = new SchemaDefinition(
            DISELECT_NAMESPACE,
            "http://www.volantis.com/schema/2004/06/xdime-cp/diselect.xsd",
            "com/volantis/schema/2004/06/xdime-cp/diselect.xsd");

    /**
     * The schema definition for XDIME 2 instance of the Widgets schema.
     */
    public static final SchemaDefinition XDIME2_WIDGETS = new SchemaDefinition(
            WIDGETS_NAMESPACE,
            "http://www.volantis.com/schema/2006/01/xdime2/widgets.xsd",
            "com/volantis/schema/2006/01/xdime2/widgets.xsd");
    
    /**
     * The schema definition for XDIME-2 instance of the AJAX schema.
     */
    public static final SchemaDefinition XDIME2_RESPONSE = new SchemaDefinition(
            WIDGETS_NAMESPACE,
            "http://www.volantis.com/schema/2006/01/xdime2/widgets-response.xsd",
            "com/volantis/schema/2006/01/xdime2/widgets-response.xsd");

    /**
     * The schema definition for XDIME 2 instance of the Widgets schema.
     */
    public static final SchemaDefinition XDIME2_TICKER = new SchemaDefinition(
            TICKER_NAMESPACE,
            "http://www.volantis.com/schema/2006/09/xdime2/ticker.xsd",
            "com/volantis/schema/2006/09/xdime2/ticker.xsd");

    /**
     * The schema definition for XDIME 2 instance of the Widgets schema.
     */
    public static final SchemaDefinition XDIME2_GALLERY = new SchemaDefinition(
            GALLERY_NAMESPACE,
            "http://www.volantis.com/xmlns/2006/10/xdime2/gallery-widget.xsd",
            "com/volantis/schema/2006/09/xdime2/gallery-widget.xsd");
    
    /**
     * The schema definition for XDIME-2 instance of the AJAX schema.
     */
    public static final SchemaDefinition XDIME2_TICKER_RESPONSE = new SchemaDefinition(
            TICKER_RESPONSE_NAMESPACE,
            "http://www.volantis.com/schema/2006/09/xdime2/ticker-response.xsd",
            "com/volantis/schema/2006/09/xdime2/ticker-response.xsd");

    /**
     * The schema definition for XML Events
     */
    public static final SchemaDefinition XDIME2_XML_EVENTS = new SchemaDefinition(
            XML_EVENTS_NAMESPACE,
            "http://www.volantis.com/schema/2006/01/xdime2/xml-events.xsd",
            "com/volantis/schema/2006/01/xdime2/xml-events.xsd");

    /**
     * A collection of all the XDIME 2 related schemata.
     */
    public static final Schemata ALL_XDIME2_SCHEMATA;

    /**
     * A collection of all the XDIME CP related schemata.
     */
    public static final Schemata ALL_XDIME_CP_SCHEMATA;

    static {
        Schemata schemata;

        schemata = new Schemata();
        schemata.addSchema(XDIME2);
        schemata.addSchema(XDIME2_XHTML2);
        schemata.addSchema(XDIME2_XFORMS);
        schemata.addSchema(XDIME2_DI_SELECT);

        schemata.addSchema(XDIME2_WIDGETS);
        schemata.addSchema(XDIME2_GALLERY);
        schemata.addSchema(XDIME2_XML_EVENTS);
        ALL_XDIME2_SCHEMATA = schemata;

        schemata = new Schemata();
        schemata.addSchema(XDIME_CP);
        schemata.addSchema(XDIME_CP_XHTML2);
        schemata.addSchema(XDIME_CP_XFORMS);
        schemata.addSchema(XDIME_CP_DI_SELECT);
        ALL_XDIME_CP_SCHEMATA = schemata;
    }

}
