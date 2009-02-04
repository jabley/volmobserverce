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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.dependency;

/**
 * An enumeration of the different levels of freshness that a dependency may
 * have.
 */
public class Freshness {

    /**
     * The dependency is fresh as the original source has not changed since
     * the dependency was created.
     *
     * <p>e.g. the dependency was a HTTP response that had a calculated time
     * to live of 1 hour and only 20 minutes have elapsed since it was
     * created.</p>
     */
    public static final Freshness FRESH = new Freshness("FRESH", 0);

    /**
     * The dependency is unsure about whether its original source has
     * changed or not since the dependency was created.
     *
     * <p>In this case the dependency needs to revalidate itself with its
     * origin source to see whether it has changed.</p>
     *
     * <p>e.g. the dependency was a HTTP response that had a calculated time
     * to live of 1 hour and 65 minutes have elapsed since it was created.</p>
     */
    public static final Freshness REVALIDATE = new Freshness("REVALIDATE", 1);

    /**
     * The dependency is stale as the original source has changed since the
     * dependency was created.
     *
     * <p>e.g. the dependency was a HTTP response that could not be cached.</p>
     */
    public static final Freshness STALE = new Freshness("STALE", 2);

    /**
     * The name of the enumeration, for debug only.
     */
    private final String name;

    /**
     * The level of enumeration, used by {@link #combine(Freshness)} to
     * determine which enumeration instance to return.
     */
    private final int level;

    /**
     * Initialise.
     *
     * @param name  The name of the enumeration.
     * @param level The level.
     */
    private Freshness(String name, int level) {
        this.name = name;
        this.level = level;
    }

    /**
     * Combine this instance with the supplied one.
     *
     * <p>The rules for combination are as follows:</p>
     * <ol>
     * <li>If either is {@link #STALE} then the result is {@link #STALE}.</li>
     * <li>If either is {@link #REVALIDATE} then the result is {@link #REVALIDATE}.</li>
     * <li>Otherwise the result is {@link #FRESH}.</li>
     * </ol>
     *
     * @param freshness The instance with which this is to be combined.
     * @return The result of combining.
     */
    public Freshness combine(Freshness freshness) {
        if (freshness == null) {
            throw new IllegalArgumentException("freshness cannot be null");
        }

        if (level > freshness.level) {
            return this;
        } else {
            return freshness;
        }
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }
}
