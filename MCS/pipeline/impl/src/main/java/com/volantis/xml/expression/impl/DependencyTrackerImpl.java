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
import com.volantis.shared.dependency.DependencyImpl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DependencyTrackerImpl
        implements DependencyTracker {

    public static final DependencyTracker IGNORING_TRACKER =
            new DependencyTracker() {
                public boolean isTracking() {
                    return false;
                }

                public void addDependency(Dependency dependency) {
                }

                public void clearDependencies() {
                }

                public Dependency extractDependency() {
                    return NO_DEPENDENCY;
                }

                public DependencyTracker createNestedTracker() {
                    return IGNORING_TRACKER;
                }

                public void propagateDependencies() {
                }
            };

    private static final Dependency NO_DEPENDENCY =
            new DependencyImpl();

    private final DependencyTracker containingTracker;

    private final Set dependencies;

    public DependencyTrackerImpl() {
        this(null);
    }

    public DependencyTrackerImpl(
            DependencyTracker containingTracker) {
        this.containingTracker = containingTracker;
        this.dependencies = new HashSet();
    }

    public boolean isTracking() {
        return true;
    }

    public void addDependency(Dependency dependency) {
        if (dependency != NO_DEPENDENCY) {
            dependencies.add(dependency);
        }
    }

    // Javadoc inherited.
    public void clearDependencies() {
        dependencies.clear();
    }

    public Dependency extractDependency() {
        Dependency result;
        int count = dependencies.size();
        if (count == 0) {
            result = NO_DEPENDENCY;
        } else if (count == 1) {
            result = (Dependency) dependencies.iterator().next();
        } else {
            result = new Dependencies(dependencies);
        }
        Dependency dependency = result;
        clearDependencies();
        return dependency;
    }

    public DependencyTracker createNestedTracker() {
        return new DependencyTrackerImpl(this);
    }

    public void propagateDependencies() {
        for (Iterator i = dependencies.iterator(); i.hasNext();) {
            Dependency dependency = (Dependency) i.next();
            containingTracker.addDependency(dependency);
        }
        clearDependencies();
    }
}
