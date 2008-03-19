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
package com.volantis.mcs.dom2theme.impl.model;

import com.volantis.mcs.themes.MutableStyleProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An adaption of {@link com.volantis.styling.Styles} which is suitable for
 * output processing.
 * <p>
 * This differs from {@link com.volantis.styling.Styles} in the following ways:
 * <ul>
 *   <li>It contains {@link com.volantis.mcs.themes.MutableStyleProperties} rather than
 *      {@link com.volantis.styling.values.PropertyValues}. This can be done
 *      because the property values have already been normalized down to the
 *      properties by the normalization process. This allows us to define a
 *      sensible implementation of equals and hashcode for the entire styles
 *      hierarchy which is required during stylesheet generation.
 *   <li>It models the nested style entities as a list of
 *      {@link PseudoStylePath}s through the hiearachy rather than an actual
 *      tree. This is more suitable for iteration which is the primary use of
 *      the content during output.
 * </ul>
 * Note that the way that the topmost properties (i.e. that associated with
 * the element rather than any contained pseudo class or element) is accessed
 * is slightly unusual. It is stored along with the other properties associated
 * with pseudo classes and elements, keyed by an empty {@link PseudoStylePath}.
 * <p>
 * Note that the order of the path entries is defined externally, but users of
 * this class rely on the ordering. In particular, they require that the empty
 * path properties are provided as the initial object in an iteration, if
 * present. For this reason, this class currently requires that the empty
 * path is added as the first entry, if at all, as a first step to dealing with
 * ordering overall. Currently
 * {@link com.volantis.mcs.dom2theme.impl.optimizer.StylesOptimizer}
 * provides the ordering correctly. However, it would be nice if we could
 * ensure correct ordering for the entire iteration in here so we could specify
 * exactly what that ordering would be to the clients of this class.
 *
 * @mock.generate
 */
public class OutputStyles {

    /**
     * A map of {@link PseudoStylePath} to {@link com.volantis.mcs.themes.MutableStyleProperties}.
     * <p>
     * Since the map does not preserve order we keep an additonal list
     * of the keys {@link listPaths} to preserve input ordering.
     */
    private final Map mapPathsToProperties = new HashMap();

    /**
     * The additional list to preserve input ordering of the keys of
     * {@link #mapPathsToProperties}
     */
    private final List listPaths = new ArrayList();

    /**
     * Initialise.
     */
    public OutputStyles() {
    }

    /**
     * Add a properties to this styles, using the path provided as the key.
     * <p>
     * <strong>Note</strong> input ordering here controls the ordering of
     * any later iteration.
     * <p>
     * <strong>Note</strong> the empty pseudo path may only be added as the
     * first entry.
     *
     * @param pseudoPath the path that acts as the key to the properties.
     * @param styleProperties the properties to add.
     */
    public void addPathProperties(PseudoStylePath pseudoPath,
            MutableStyleProperties styleProperties) {

        if (pseudoPath.isEmpty() && !listPaths.isEmpty()) {
            throw new IllegalStateException(
                    "empty pseudo path can only be the first entry");
        }
        if (styleProperties.isEmpty()) {
            throw new IllegalArgumentException("properties are empty");
        }

        listPaths.add(pseudoPath);
        mapPathsToProperties.put(pseudoPath, styleProperties);
    }

    /**
     * Remove a properties from this styles, using the path provided as the key.
     *
     * @param pseudoPath the path that acts as the key to the properties.
     */
    public void removePathProperties(PseudoStylePath pseudoPath) {

        listPaths.remove(pseudoPath);
        mapPathsToProperties.remove(pseudoPath);
    }

    /**
     * Return the properties from this styles that is stored for the path
     * provided.
     *
     * @param pseudoPath the path that acts as the key to the properties.
     * @return the properties associated with the path.
     */
    public MutableStyleProperties getPathProperties(
            PseudoStylePath pseudoPath) {

        return (MutableStyleProperties) mapPathsToProperties.get(pseudoPath);
    }

    /**
     * Iterate over the paths of this styles, in the order they were added.
     * <p>
     * <strong>Note</strong> the empty pseudo path, if present, will always
     * be the first object in the iteration.
     *
     * @param iteratee the object called to process each path.
     */
    public void iterate(PseudoStylePathIteratee iteratee) {

        // ordered iteration using the list.
        for (int i = 0; i < listPaths.size(); i++) {
            PseudoStylePath path =
                    (PseudoStylePath) listPaths.get(i);

            iteratee.next(path);
        }
    }

    /**
     * Check if this styles is empty, returning true if so and false if not.
     * <p>
     * <strong>Note:</strong> Currently empty is defined as having no path
     * properties registered. In particular, if the styles only has path
     * properties registered which are themselves empty, this will still return
     * false. This means that any client code which removes values from a
     * properties needs to explicit remove the path properties when the
     * properties is empty. We may revisit this decision in future if it is
     * decided that this check should check it's contained path properties as
     * well.
     *
     * @return true if the styles is empty, or false if it is not empty.
     */
    public boolean isEmpty() {

        return listPaths.isEmpty();
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {

        // NOTE: this does not take into account ordering. Hopefully this is
        // not an issue. Given that order is consistent it should not.

        if (obj instanceof OutputStyles) {
            OutputStyles other = (OutputStyles) obj;
            return mapPathsToProperties.equals(other.mapPathsToProperties);
        } else {
            return false;
        }
    }

    // Javadoc inherited.
    public int hashCode() {

        // NOTE: this does not take into account ordering. Hopefully this is
        // not an issue. Given that order is consistent it should not.

        int result = 17;
        result = 37 * result + mapPathsToProperties.hashCode();
        return result;
    }

    // javadoc inherited
    public String toString() {
        return "[" + getClass().getName()
                + ": listPaths=" + listPaths
                + ", mapPathsToProperties=" + mapPathsToProperties
                + "]";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-Jul-05	8668/14	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
