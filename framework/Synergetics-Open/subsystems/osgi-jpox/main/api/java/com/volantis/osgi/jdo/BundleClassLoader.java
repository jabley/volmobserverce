/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.osgi.jdo;

import org.osgi.framework.Bundle;

public class BundleClassLoader
        extends ClassLoader {

    private final Bundle bundle;

    public BundleClassLoader(ClassLoader parent, Bundle bundle) {
        super(parent);

        this.bundle = bundle;
    }

    protected Class findClass(String name) throws ClassNotFoundException {
        return bundle.loadClass(name);
    }
}
