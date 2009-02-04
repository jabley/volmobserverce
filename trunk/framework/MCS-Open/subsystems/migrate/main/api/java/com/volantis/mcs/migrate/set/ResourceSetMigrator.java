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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.migrate.set;

import com.volantis.mcs.migrate.api.framework.ResourceMigrator;

/**
 * A resource set migrator is an object that can iterate through a set of
 * resources, migrating each of them in turn. The current specification places
 * no constraints on the order in which the resources are migrated, and
 * invoking classes should treat the ordering as undefined and subject to
 * change.
 */
public interface ResourceSetMigrator {
    /**
     * Uses the provided resource migrator to migrate30 all resources within the
     * set associated with this instance.
     *
     * @param migrator The migrator to use.
     */
    public void migrate(ResourceMigrator migrator);
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Jun-05	8613/1	geoff	VBM:2005052404 Holding VBM for XDIME CP prior to 3.3.1 release

 18-May-05	8181/3	adrianj	VBM:2005050505 XDIME/CP migration CLI

 18-May-05	8181/1	adrianj	VBM:2005050505 XDIME/CP Migration CLI

 ===========================================================================
*/
