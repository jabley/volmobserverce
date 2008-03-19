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

package com.volantis.xml.pipeline.sax.convert;

import com.volantis.xml.pipeline.sax.config.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Provide a base class configuration that contains shared code for URLToURLC
 * and AbsoluteToRelative configurations.
 */
public class ConverterConfiguration implements Configuration {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
            = "(c) Volantis Systems Ltd 2003. ";

    /**
     * default mode of converter/rewriter
     *
     * Default mode means that ConverterConviguration
     * behaves as before it means set/getTuples
     * works as before changes, for backward
     * compatibility.
     */
    public static String DEFAULT_MODE = "";

    /** html mode of converter/rewriter*/
    public static final String HTML_MODE = "html";

    /** xdime mode of converter/rewriter */
    public static final String XDIME_MODE = "xdime";

    /**
     * The tupleMapsMap stores tupleMap for each mode of Rewriter
     * Rewriter can use different set of tuples relying on it's mode
     * that is independent from schema.
     */
    protected Map tupleMapsMap = new HashMap();

    /**
     * Store the key buffer statically to minimize object creation when
     * obtaining the key.
     */
    private StringBuffer buffer;

    /**
     * Construct a new ConverterConfiguration with an empty typeMap.
     * When using this constructor the setTuples() method must be called
     * before any of the "get" methods will yield a result.
     */
    ConverterConfiguration() {
    }

    /**
     * Populate the tuple map given the array of ConverterTuple objects.
     *
     * <p>Passing list of tuples into this constructor we are
     * putting list into default mode container,
     * for backward compatibility.</p> 
     *
     * @param tuples the array of tuples.
     */
    public ConverterConfiguration(ConverterTuple[] tuples) {
        putTuplesByMode(DEFAULT_MODE,tuples);
    }

    /**
     * Provide an array of ConverterTuples for this ConverterConfiguration
     * to work with. This will replace completely any previous array of
     * ConverterTuples that were provided either during or subsequent to
     * construction.
     * <p>method  has the same effect like
     * putTuplesByMode(DEFAULT_MODE,tuples)</p>
     * @param tuples the array of ConverterTuples
     */
    public void setTuples(ConverterTuple[] tuples) {
        putTuplesByMode(DEFAULT_MODE,tuples);
    }


    /**
     * provide an array of tuples and mode for which this array of
     * tuples should be used
     * @param tuples
     * @param mode
     */
    public void putTuplesByMode(String mode, ConverterTuple [] tuples){
        Map tupleMap = convertTupleArrayToTupleMap(tuples);
        if(mode == null || mode.equals(DEFAULT_MODE)){
            this.tupleMapsMap.put(DEFAULT_MODE, tupleMap);
        } else {
            this.tupleMapsMap.put(mode, tupleMap);
        }
    }

    /**
     * Initialize the tupleMap field.
     * @param tuples the ConverterTuple arrary with which to initialize
     * tupleMap.
     */
    private Map convertTupleArrayToTupleMap(ConverterTuple[] tuples) {
        Map tupleMap = new HashMap();
        String key;
        List options;
        Iterator iterator;
        String element;
        String namespaceURI;

        if (tuples == null) {
            throw new IllegalArgumentException(
                    "At least one tuple must be specified");
        }

        buffer = new StringBuffer();

        // Set up the internal map based on the given array of tuples. The
        // required end-result is a map keyed on namespace/element (or just
        // element for "all namespaces" entries) and containing arrays of
        // tuples that have the given namespace URI/element pairings. This
        // allows for multiple rules against the given namespace/element
        // combinations, though generally we would expect only one per
        // namespace/element combination
        for (int i = 0;
             i < tuples.length;
             i++) {
            namespaceURI = tuples[i].getNamespaceURI();
            element = tuples[i].getElement();

            if (namespaceURI != null) {
                key = getKey(element, namespaceURI);
            } else {
                key = element;
            }

            // During population of the map, we use Lists to allow dynamic
            // sizing
            if ((options = (List)tupleMap.get(key)) == null) {
                options = new ArrayList();
                tupleMap.put(key, options);
            }

            options.add(tuples[i]);
        }

        // Once the map population is complete, we turn the array lists into
        // ConverterTuple[] arrays
        iterator = tupleMap.keySet().iterator();

        while (iterator.hasNext()) {
            key = (String)iterator.next();

            tupleMap.put(key,
                         ((List)tupleMap.get(key)).toArray(getTemplate()));
        }
        return tupleMap;
    }

    /**
     * The set of tuples appropriate to the given namespace URI and element
     * combination are returned.
     *
     * <p>If the namespace/element combination can be found, the tuples defined
     * for that combination are returned.</p>
     *
     * <p>If the element can be found in an "all namespaces" entry, the tuples
     * defined for that element in "all namespaces" are returned</p>
     *
     * <p><strong>For performance reasons the internal storage for the tuples
     * array is returned. The caller must not update the content of the
     * array.</strong></p>
     *
     * <p> Method hasthe same effect like
     *  getTuplesByMode(namespaceURI,element,DEFAULT_MODE)</p>
     *
     * @param namespaceURI the namespace containing the specified element
     * @param element      the element to be searched for
     * @return the set of tuples found
     */
    public ConverterTuple[] getTuples(String namespaceURI,
                                      String element) {
        return getTuplesByMode(namespaceURI,element,null);
    }

    /**
     * Get tuples apropriate to given namespace URI, element and mode.
     * @param namespaceURI
     * @param element
     * @param mode
     * @return
     */
    public ConverterTuple[] getTuplesByMode(String namespaceURI,
                                               String element,
                                               String mode){
        ConverterTuple [] result = null;
        Map tupleMap;
        if(mode == null || mode.equals(DEFAULT_MODE)){
            tupleMap = (Map)this.tupleMapsMap.get(DEFAULT_MODE);
        } else {
            tupleMap = (Map)this.tupleMapsMap.get(mode);
        }
        if(tupleMap != null){
            result = getTupleFromMap(namespaceURI,element,tupleMap);
        }
        return result;
    }


    /**
     * converts tuples from map into array that contains only tuples
     * with apropriate namespace URI and element.
     * @param namespaceURI
     * @param element
     * @param tupleMap set of tuples to choose tuples from
     * @return
     */
    private ConverterTuple[] getTupleFromMap(String namespaceURI,
                                      String element, Map tupleMap){
        String key;
        ConverterTuple[] result;

        // First look to see if the namespace/element entry can be found
        if (namespaceURI != null) {
            key = getKey(element, namespaceURI);
        } else {
            key = element;
        }

        result = (ConverterTuple[])tupleMap.get(key);

        if ((result == null) &&
                (namespaceURI != null)) {
            // If a namespace URI was specified but no tuples found, see if
            // there are any tuples for the "all namespaces" variant of the
            // element
            result = (ConverterTuple[])tupleMap.get(element);
        }

        return result;
    }

    /**
     * Get the key from the element and namespace url using the static
     * StringBuffer.
     *
     * @param  element      the element.
     * @param  namespaceURI the namespace URI
     * @return              the key as a combination of element and the
     *                      namespace.
     */
    private String getKey(String element, String namespaceURI) {

        if (buffer == null) {
            buffer = new StringBuffer(
                    element.length() + namespaceURI.length() + 1);
        } else {
            buffer.setLength(0);
        }
        buffer.append(element).append('@').append(namespaceURI);
        return buffer.toString();
    }

    /**
     * Get the template tuple.
     *
     * @return      the template tuple.
     */
    protected ConverterTuple[] getTemplate() {
        return new ConverterTuple[0];
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Apr-05	7376/1	allan	VBM:2005031101 SmartClient bundler - commit for testing

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 26-May-04	708/1	allan	VBM:2004052102 Provide a URL rewriting process.

 08-Aug-03	308/3	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process - create external Tuple classes

 08-Aug-03	308/1	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process

 ===========================================================================
*/
