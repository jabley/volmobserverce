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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.builder;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.devrep.repository.api.devices.DefaultDevice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Object to define CSS support for a given device. The main elements of
 * interest (at the time of writing) are expressions that have been defined to
 * generate attributes on various elements, and then dropping CSS styles that
 * the device does not understand.
 */
public class CSSSupportConfigurator {

    private static final Pattern EXPRESSION_DEFINING_POLICY = Pattern.compile(
            "^x-element\\.([^\\.]+)\\.attribute\\.([^\\.]+)\\.expression$");

    private final InternalDevice device;

    private Map remappableElements = null;

    private final Object lock = new Object();

    public CSSSupportConfigurator(InternalDevice device) {
        this.device = device;
    }

    /**
     * Return a <code>Map</code> keyed by element name that contains Maps keyed
     * by attribute name that contain expressions used to define how the
     * attribute values are calculated.
     *
     * @return a <code>Map</code>
     */
    public Map getFallbackAttributeExpressions() {
        synchronized (lock) {
            if (null == remappableElements) {
                buildInternalStructures();
            }
        }
        return remappableElements;
    }


    private void buildInternalStructures() {

        // We could filter the effective policies as we build the structure.
        //
        // Rationale:
        //
        // getEffectivePolicies returns n policies, and we are only interested
        // in 'm' policies in total.
        //
        // filter does 'a x n' policy name comparisons, where 'a' is the number
        // of Specification objects passed to filter.
        //
        // If we filter getEffectivePolicies as it is constructed, we do at
        // worst 'i x n' comparisons there, to get the 'm' policies that are of
        // interest. Then filter will do 'a x m' comparisons.
        //
        // To inject some real numbers, say 'n' is 650 total policies and we are
        // interested in 'm' = 10 of them. If we don't filter the effective
        // policies as it is built, then we do 650a comparisons in filter,
        // which is 1300 when 'a' = 2, say.
        // If we filter the effective policies, then we do 650 comparisons to
        // get 10 polices, and then do 10a comparisons, which is another 20.
        // That gives 670 comparisons - a large saving in string comparisons,
        // and means that we have a smaller data structure being used here for
        // the effective policies map.
        //
        // So for n > 1, it is worthwhile filtering the effective policies if
        // possible. We don't do that currently, since n = 1.


        Map effectivePolicies = getEffectivePolicies(device);

        // Filter the effectivePolicies. It is intended that this does more
        // work at a later date, and we will want a few different data
        // structures, and want to iterate the effectivePolicies data
        // structure as little as possible. So we do a general filter
        // operation, where each Collecting Parameter has an associated
        // Specification that must be satisfied for the item to be placed
        // in the data structure.

        remappableElements = new HashMap();

        filter(effectivePolicies,
                new Specification[]
                        {new CSSRemappingExpressionSpecification()},
                new Map[]{remappableElements});
    }

    /**
     * Return the <code>Map</code> stored within the <code>source<code>
     * <code>Map</code> under the specified <code>key</code>. If there is
     * no such <code>Map</code>, one is created and returned.
     *
     * @param source the <code>Map</code> being searched.
     * @param key    the key used to look in the source <code>Map</code>.
     * @return a <code>Map</code> - not null.
     */
    private static Map getMapAndCreateIfAbsent(Map source, String key) {
        Map result = (Map) source.get(key);
        if (null == result) {
            result = new HashMap();
            source.put(key, result);
        }
        return result;
    }

    /**
     * Filter the source <code>Map</code>, comparing each entry to each
     * <code>Specification</code> and passing the correspondingly indexed
     * <code>maps</code> item into the
     * {@link com.volantis.mcs.protocols.builder.CSSSupportConfigurator.Specification#handle(String, java.util.Map, java.util.Map)}
     * implementation.
     *
     * @param source         the <code>Map</code> being filtered. Not null.
     * @param specifications a non-null array of <code>Specification</code>
     *                       implementations.
     * @param maps           a non-null array of <code>Map</code> objects
     *                       that will collect the result for the
     *                       corresponding <code>Specification</code>.
     * @throws IllegalArgumentException if any of the parameters are
     *                                  <code>null</code>, or the arrays
     *                                  aren't the same length.
     */
    private void filter(Map source, Specification[] specifications, Map[] maps) {

        // This isn't public, so strictly speaking these checks aren't
        // required, since we can be sure that the method is being used
        // correctly. But in case this method is ever moved and made more
        // public, we have the pre-requisite checks already written.
        if (null == source) {
            throw new IllegalArgumentException("source cannot be null");
        }

        if (null == specifications) {
            throw new IllegalArgumentException(
                    "specifications cannot be null");
        }

        if (null == maps) {
            throw new IllegalArgumentException("maps cannot be null");
        }

        if (maps.length != specifications.length) {
            throw new IllegalArgumentException(
                    "maps and specifications are not the same length");
        }

        for (Iterator keys = source.keySet().iterator();
             keys.hasNext();) {
            final String key = (String) keys.next();

            for (int i = 0; i < specifications.length; i++) {
                specifications[i].handle(key, source, maps[i]);
            }
        }
    }


    /**
     * Return the effective policies defined for this Device. The ancestor
     * tree is walked to get all of those properities as well.
     *
     * @param device the context Device
     * @return a Map of effective policies
     */
    private Map getEffectivePolicies(InternalDevice device) {

        final Map effectivePolicies = new HashMap();

        for (InternalDevice ancestorOrSelf = device;
             ancestorOrSelf != null;
             ancestorOrSelf = ancestorOrSelf.getFallbackDevice()) {

            // @todo should we filter these policies here? How big is this
            // structure likely to get and is it an issue? See the comment in
            // buildInternalStructures
            putAllIfAbsent(effectivePolicies, ancestorOrSelf);
        }

        return effectivePolicies;
    }

    /**
     * Acts like {@link java.util.Map#putAll(java.util.Map)} } except it checks
     * first to see if {@link Map#containsKey(Object)} on the
     * <code>target</code> returns <code>true</code>. If that is
     * <code>true</code>, then it does not replace the old value with the new
     * mapping.
     *
     * @param target the <code>Map</code> that is being populated.
     * @param device the <code>DefaultDevice</code> whose policies are being
     * copied.
     */
    private void putAllIfAbsent(final Map target, final InternalDevice device) {
        for (Iterator iterator = device.getPolicyNames(); iterator.hasNext();) {
            final String key = (String) iterator.next();
            if (!target.containsKey(key)) {
                target.put(key, device.getSpecifiedPolicyValue(key));
            }
        }
    }

    /**
     * <p>Specification implementation that checks to see if the policy name is
     * an expression-defining policy;</p>
     * <p>e.g.</p>
     *
     * <pre>x-element.img.attribute.width.expression</pre>
     *
     * <p>and more generally:</p>
     *
     * <pre>x-element.%element-name%.attribute.%attribute-name%.expression</pre>
     *
     * <p>If the constraints of the specification are satisfied, the Collecting
     * Parameter Map will be updated. It will contain an entry which is a Map,
     * and the key for this entry will be the element name. The Map which is
     * referenced will have an entry added for the expression, keyed by
     * attribute name.
     *
     * e.g.
     *
     *
     *
     * <p>Thread-safe</p>
     */
    private class CSSRemappingExpressionSpecification implements Specification {

        // javadoc inherited
        public void handle(String key, Map policies, Map result) {
            final Matcher isExpressionDefiningPolicy =
                    EXPRESSION_DEFINING_POLICY.matcher(key);

            if (isExpressionDefiningPolicy.matches()) {

                // We have an expression.
                String element = isExpressionDefiningPolicy.group(1);
                String attribute = isExpressionDefiningPolicy.group(2);
                String expression = (String) policies.get(key);

                // We don't validate the expression here, so may see runtime
                // failures. We could validate, log failures and don't bother
                // putting them into the set of items used at runtime, but not
                // doing that for now.

                Map expressions = CSSSupportConfigurator.
                        getMapAndCreateIfAbsent(result, element);

                // Sanity check - never expecting this to happen, so check for
                // it.
                if (expressions.containsKey(attribute)) {
                    throw new IllegalStateException(
                            "Already contains expression for attribute '"
                                    + attribute + "'");
                }

                expressions.put(attribute, expression);
            }
        }
    }

    /**
     * Simple interface used for filtering Map contents
     */
    interface Specification {

        /**
         * An implementation should decide whether the item passed the
         * specification, and then typically put an item in the Collecting
         * Parameter result Map. Exactly what gets stored is deferred to the
         * implementation.
         *
         * @param key      the current key into the <code>Map</code>
         * @param policies the <code>Map</code> being filtered
         * @param result   the Collecting Parameter <code>Map</code>
         */
        void handle(final String key, final Map policies, final Map result);
    }
}
