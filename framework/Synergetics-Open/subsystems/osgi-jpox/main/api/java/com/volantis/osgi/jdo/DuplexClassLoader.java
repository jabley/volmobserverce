/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.osgi.jdo;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class DuplexClassLoader
        extends ClassLoader {

    private ClassLoader other;

    public DuplexClassLoader(ClassLoader parent, ClassLoader other) {
        super(parent);
        this.other = other;
    }

    protected Class findClass(String name) throws ClassNotFoundException {
        return other.loadClass(name);
    }

    protected URL findResource(String name) {
        return other.getResource(name);
    }

    protected Enumeration findResources(String name) throws IOException {
        return other.getResources(name);
    }
}
