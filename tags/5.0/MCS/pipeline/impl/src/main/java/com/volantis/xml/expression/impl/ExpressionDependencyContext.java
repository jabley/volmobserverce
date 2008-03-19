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

package com.volantis.xml.expression.impl;

import com.volantis.shared.dependency.Dependency;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.dependency.Freshness;
import com.volantis.shared.dependency.Tracking;
import com.volantis.shared.dependency.Validity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The dependency context used within expressions and pipeline.
 */
class ExpressionDependencyContext
        implements DependencyContext {

    /**
     * The expression context.
     */
    private final SimpleExpressionContext context;

    /**
     * The map from {@link Dependency} to {@link Freshness} for those
     * dependencies that have been checked during the processing of the
     * {@link #checkValidity(com.volantis.shared.dependency.Dependency)}.
     */
    private Map checked;

    /**
     * Initialise.
     * @param context         The expression context.
     */
    public ExpressionDependencyContext(SimpleExpressionContext context) {
        this.context = context;
    }

    // Javadoc inherited.
    public void setProperty(Object key, Object value) {
        context.setProperty(key, value, false);
    }

    // Javadoc inherited.
    public Object getProperty(Object key) {
        return context.getProperty(key);
    }

    // Javadoc inherited.
    public Object removeProperty(Object key) {
        return context.removeProperty(key);
    }

    // Javadoc inherited.
    public void pushDependencyTracker(Tracking tracking) {
        DependencyTracker tracker;
        if (tracking == Tracking.DISABLED) {
            tracker = DependencyTrackerImpl.IGNORING_TRACKER;
        } else if (tracking == Tracking.ENABLED) {
            tracker = new DependencyTrackerImpl();
        } else if (tracking == Tracking.INHERIT) {
            DependencyTracker containing = getDependencyTracker();
            tracker = containing.createNestedTracker();
        } else {
            throw new IllegalArgumentException("Unknown tracking " + tracking);
        }

        context.pushObject(tracker, false);
    }

    /**
     * Get the current dependency tracker.
     * @return The current dependency tracker.
     */
    private DependencyTracker getDependencyTracker() {
        return (DependencyTracker) context
                .findObject(DependencyTracker.class);
    }

    // Javadoc inherited.
    public void addDependency(Dependency dependency) {
        getDependencyTracker().addDependency(dependency);
    }

    // Javadoc inherited.
    public boolean isTrackingDependencies() {
        return getDependencyTracker().isTracking();
    }

    // Javadoc inherited.
    public void propagateDependencies() {
        getDependencyTracker().propagateDependencies();
    }

    // Javadoc inherited.
    public void clearDependencies() {
        getDependencyTracker().clearDependencies();
    }

    // Javadoc inherited.
    public void popDependencyTracker() {
        context.popObject(getDependencyTracker());
    }

    // Javadoc inherited.
    public Dependency extractDependency() {
        return getDependencyTracker().extractDependency();
    }

    // Javadoc inherited.
    public Validity checkValidity(Dependency dependency) {

        Freshness freshness;
        checked = new HashMap();
        try {
            freshness = dependency.freshness(this);
            if (freshness == Freshness.REVALIDATE) {

                // It is not clear whether the dependency is valid or not so we
                // need to revalidate those dependencies that have been added
                // to the list.
                freshness = Freshness.FRESH;
                for (Iterator i = checked.entrySet().iterator();
                     freshness == Freshness.FRESH && i.hasNext();) {
                    Map.Entry entry = (Map.Entry) i.next();
                    Dependency nested = (Dependency) entry.getKey();
                    freshness = (Freshness) entry.getValue();
                    if (freshness == Freshness.REVALIDATE) {
                        freshness = nested.revalidate(this);
                    }
                }
            }
        } finally {
            checked = null;
        }

        Validity validity;
        if (freshness == Freshness.FRESH) {
            // The dependency is fresh so is valid.
            validity = Validity.VALID;
        } else if (freshness == Freshness.REVALIDATE) {
            // The dependency is still not sure so treat it as if it were
            // stale.
            validity = Validity.INVALID;
        } else if (freshness == Freshness.STALE) {
            // The dependency is stale so is not valid
            validity = Validity.INVALID;
        } else {
            throw new IllegalStateException("Unknown freshness: " + freshness);
        }

        return validity;
    }

    // Javadoc inherited.
    public Freshness checkFreshness(Dependency dependency) {
        Freshness freshness = null;
        if (checked == null) {
            freshness = dependency.freshness(this);
        } else {
            freshness = (Freshness) checked.get(dependency);
            if (freshness == null) {
                freshness = dependency.freshness(this);
                checked.put(dependency, freshness);
            }
        }

        return freshness;
    }
}
