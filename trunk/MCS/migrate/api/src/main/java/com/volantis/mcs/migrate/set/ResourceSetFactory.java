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
package com.volantis.mcs.migrate.set;

import com.volantis.mcs.migrate.api.notification.NotificationReporter;

import java.io.File;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * Factory to create classes which migrate entire sets of resources.
 */
public abstract class ResourceSetFactory {

    /**
     * The default instance.
     */
    private static final ResourceSetFactory defaultInstance;

    /**
     * Instantiate the default instance using reflection to prevent
     * dependencies between this and the implementation class.
     */
    static {
        try {
            ClassLoader loader = ResourceSetFactory.class.getClassLoader();
            Class implClass = loader.loadClass("com.volantis.mcs." +
                    "migrate.impl.set.DefaultResourceSetFactory");
            defaultInstance = (ResourceSetFactory) implClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new UndeclaredThrowableException(e);
        } catch (InstantiationException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static ResourceSetFactory getDefaultInstance() {
        return defaultInstance;
    }

    /**
     * Create a resource set migrator for migrating files/directories from
     * an input file, an output file, and the notification reporter to be used
     * to pass information back to the user.
     *
     * @param input The input file/directory to migrate
     * @param output The location for the output migrated file/directory
     * @param reporter The notification reporter to use for feedback
     * @return The corresponding resource set migrator
     */
    public abstract ResourceSetMigrator createFileResourceSetMigrator(
            File input, File output, NotificationReporter reporter);

}
