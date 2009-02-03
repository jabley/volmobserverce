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
package com.volantis.xml.pipeline;

import com.volantis.xml.schema.W3CSchemata;

import java.util.HashMap;

/**
 * This extended typesafe enumeration defines the standard pipeline namespaces
 * available (though it can be extended to hold additional namespaces by
 * deriving from this class). Each literal's internal name must be unique
 * across all instances of this and any derived classes used within a given
 * JVM class loader execution.
 * <p/>
 * The symbolic enumeration literals store the actual namespace URI that they
 * are equivalent to. A default namespace URI will be constructed for any
 * literals for which a namespace isn't explicitly defined. This is constructed
 * from the {@link #defaultPrefix} and the literal's internal name.
 * <p/>
 * An application can either define its own default prefix via {@link
 * #setDefaultPrefix} and accept that the literal's internal names are used to
 * complete the URI or it can define (fully independent) URIs for each
 * enumeration literal.
 */
public class Namespace {
    /**
     * The set of all literals. Keyed on the internal string version of the
     * enumeration, mapping to the Namespace equivalent.
     * <p/>
     * NB: This static member *must* appear before the enumeration literals
     * for this to work. If it does not, the access of this variable within
     * the literal construction (within this class's constructor) will find
     * this variable to be null (i.e. it won't have been initialized yet).
     * The Java Language Spec second edition, section 8.7, specifically
     * states that initialization is performed in "textual order".
     *
     * @associates <{Namespace}>
     * @supplierRole entries
     * @supplierCardinality 1
     * @link aggregationByValue
     */
    private static HashMap entries = new HashMap();

    /**
     * Default prefix which can be restored by setting default prefix to null.
     */
    private static final String DEFAULT_PREFIX = "http://www.volantis.com/xmlns/marlin-";

    /**
     * Namespace URIs can be collectively defined using a common prefix
     * settable on this class. If a non-null URI is assigned to a given
     * enumeration literal, this value will be ignored for that enumeration
     * literal.
     */
    protected static String defaultPrefix = DEFAULT_PREFIX;

    /**
     * @supplierRole PIPELINE
     * @supplierCardinality 1
     * @link aggregationByValue
     */
    public static final Namespace PIPELINE = new Namespace("pipeline");

    /**
     * @supplierRole URID
     * @supplierCardinality 1
     * @link aggregationByValue
     */
    public static final Namespace URID = new Namespace("uri-driver");

    /**
     * @supplierRole SQL_DRIVER
     * @supplierCardinality 1
     * @link aggregationByValue
     */
    public static final Namespace SQL_DRIVER = new Namespace("sql-driver");

    /**
     * @supplierRole DRIVER_CONDITIONERS
     * @supplierCardinality 1
     * @link aggregationByValue
     */
    public static final Namespace DRIVER_CONDITIONERS =
            new Namespace("driver-conditioners");

    /**
     * @supplierRole TEMPLATE
     * @supplierCardinality 1
     * @link aggregationByValue
     */
    public static final Namespace TEMPLATE = new Namespace("template");

    /**
     * @supplierRole SERVLET
     * @supplierCardinality 1
     * @link aggregationByValue
     */
    public static final Namespace SERVLET = new Namespace("servlet");

    /**
     * @supplierRole WEB_SERVICE_DRIVER
     * @supplierCardinality 1
     * @link aggregationByValue
     */
    public static final Namespace WEB_SERVICE_DRIVER =
            new Namespace("web-service-driver");

    /**
     * The marlin web driver namespace - webd for short.
     */
    public static final Namespace WEB_DRIVER =
            new Namespace("web-driver");

    /**
     * XMLSchema-instance namespace. We pass in "xsi" as the name (this will
     * really be used as a default prefix). The URI is the full namespace
     * URI
     */
    public static final Namespace XMLSCHEMA_INSTANCE =
            new Namespace("xsi", W3CSchemata.XSI_NAMESPACE);

    /**
     * Picasa namespace (with the full namespace URI)
     */
    public static final Namespace PICASA =
            new Namespace("picasa", "http://www.volantis.com/xmlns/2008/08/picasa");

    /**
     * Flickr namespace (with the full namespace URI)
     */
    public static final Namespace FLICKR =
            new Namespace("flickr", "http://www.volantis.com/xmlns/2008/08/flickr");

    /**
     * Google Docs namespace (with the full namespace URI)
     */
    public static final Namespace GOOGLE_DOCS =
            new Namespace("gdocs", "http://www.volantis.com/xmlns/2008/08/gdocs");

    /**
     * DISelect namespace (with the full namespace URI)
     */
    public static final Namespace DISELECT =
            new Namespace("sel", "http://www.w3.org/2004/06/diselect");

    /**
     * The internal name of the enumeration literal. Used with
     * {@link #defaultPrefix} to generate a default URI if the URI
     * for a given enumeration literal isn't set.
     */
    private String name;

    /**
     * The assigned URI for the enumeration literal. Can be null.
     */
    private String uri;

    /**
     * Initializes the new instance with the given parameters. A null name
     * is not permitted
     *
     * @param name the internal name of the enumeration literal
     */
    protected Namespace(String name) {
        this(name, null);
    }

    /**
     * Initializes the new instance with the given parameters. A null name
     * is not permitted
     *
     * @param name the internal name of the enumeration literal
     * @param uri the uri of the namespace
     */
    protected Namespace(String name, String uri) {
        if (name != null) {
            this.name = name;
            this.uri = uri;

            if (!entries.containsKey(name)) {
                entries.put(name, this);
            } else {
                Namespace entry = literal(name);

                throw new IllegalStateException(
                        "Literal " + name + " already exists (and is of class " +
                        entry.getClass().getName() + ")");
            }
        } else {
            throw new IllegalArgumentException(
                    "Namespace enumeration literal must be named");
        }
    }

    /**
     * Returns the internal name for the enumeration literal. This must not
     * be used for presentation purposes but is used to generate a
     * default URI along with the {@link #defaultPrefix}.
     *
     * @return internal name for the enumeration literal
     */
    public String toString() {
        return name;
    }

    /**
     * Permits the URI for a given enumeration literal to be set. Passing
     * null reverts the literal to using the {@link #defaultPrefix} and its
     * internal name.
     *
     * @param uri the new URI for the enumeration literal
     */
    public void setURI(String uri) {
        this.uri = uri;
    }

    /**
     * Returns the URI for the given enumeration literal.
     *
     * @return the URI for the enumeration literal
     */
    public String getURI() {
        String result = null;

        if (uri == null) {
            StringBuffer buf = new StringBuffer(defaultPrefix.length() +
                                                name.length());
            buf.append(defaultPrefix).append(name);
            result = buf.toString();
        } else {
            result = uri;
        }

        return result;
    }

    /**
     * Returns the default prefix used for literals for which URIa have not been
     * defined.
     * @return the default prefix for URIs
     */
    public static String getDefaultPrefix() {
        return Namespace.defaultPrefix;
    }

    /**
     * Retrieves the enumeration literal that is equivalent to the given
     * internal name, or null if the name is not recognized.
     *
     * @param name the internal name to be looked up
     * @return the equivalent enumeration literal or null if the name is
     *         not recognized
     */
    public static Namespace literal(String name) {
        return (Namespace)entries.get(name);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 18-May-04	421/2	doug	VBM:2003101601 Fixed merge conflicts

 05-Nov-03	438/4	doug	VBM:2003091803 Addressed some rework issues

 04-Nov-03	438/2	doug	VBM:2003091803 Added parameter value processes

 23-Mar-04	631/1	allan	VBM:2004032205 Patch performance fixes from Pipeline/MCS 3.0GA

 22-Mar-04	626/1	allan	VBM:2004032205 Pipeline performance enhancements.

 08-Aug-03	321/1	byron	VBM:2003080804 Namespace for WebDriver is incorrect (marlin-web-driver)

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 30-Jul-03	238/1	byron	VBM:2003072309 Create the adapter process for parent task - preliminary commit

 30-Jun-03	154/4	sumit	VBM:2003062506 Changed includeServerResources to includeJSP and includeServlet

 30-Jun-03	154/1	sumit	VBM:2003062506 Changed includeServerResources to includeJSP and includeServlet

 30-Jun-03	157/4	philws	VBM:2003062508 Rename sql-connector to sql-driver

 29-Jun-03	98/2	allan	VBM:2003022822 Added some tests for RequestOperationProcess - could do with more though. Added some possibly final touches

 27-Jun-03	127/1	doug	VBM:2003062306 Column Conditioner Modifications

 25-Jun-03	132/1	byron	VBM:2003061808 Allow MCS to configure the pipeline WebService Connector

 24-Jun-03	109/3	philws	VBM:2003061913 Change pipeline:includeURI to urid:fetch and add new TLD for it

 16-Jun-03	88/1	sumit	VBM:2003061303 Changed enumeration constants to be final

 13-Jun-03	68/1	allan	VBM:2003022821 SQL Connector JSP tags.

 11-Jun-03	66/1	philws	VBM:2003061103 Added class for handling Namespace URIs

 ===========================================================================
*/
