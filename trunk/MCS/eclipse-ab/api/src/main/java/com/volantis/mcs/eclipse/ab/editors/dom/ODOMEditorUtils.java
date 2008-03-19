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
package com.volantis.mcs.eclipse.ab.editors.dom;

import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.common.odom.input.VolantisSAXBuilder;
import com.volantis.devrep.repository.accessors.DefaultNamespaceAdapterFilter;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.jdom.JDOMException;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for ODOM Editors
 */
public class ODOMEditorUtils {

    /**
     * Create the root element using the ODOMFactory and a IFile
     *
     * @throws org.eclipse.core.runtime.CoreException If there is a problem
     * getting the contents of the policy file.
     * @throws java.io.IOException If there is a problem reading the InputStream
     * on the policy file.
     * @throws org.jdom.JDOMException If the document is not well-formed.
     */
    public static ODOMElement createRootElement(IFile file,
                                                ODOMFactory factory)
                throws CoreException, JDOMException, IOException {

        if (file == null) {
            throw new IllegalArgumentException(
                        "file cannot be null"); //$NON-NLS-1$
        }
        if (factory == null) {
            throw new IllegalArgumentException(
                        "factory cannot be null"); //$NON-NLS-1$
        }
        if (!resourceExists(file)) {
            throw new IllegalStateException(
                    "Resource not found in the file system: " + //$NON-NLS-1$
                    file.getLocation().toOSString());
        }

        final InputStream inputStream = file.getContents();
        final ODOMElement odomElement;
        if (inputStream != null) {

            try {
                // Create a non-validating builder.
                // Note that this builder is JRE 1.4 and Eclipse friendly
                SAXBuilder builder = new VolantisSAXBuilder(false);

                // we require default namespace declarations to be replaced
                // with a prefix binding, the DefaultNamespaceAdapterFilter will
                // do this for us.
                builder.setXMLFilter(new DefaultNamespaceAdapterFilter(
                        MCSNamespace.LPDM.getPrefix()));
                builder.setFactory(factory);
                Document document = builder.build(inputStream);
                odomElement = (ODOMElement) document.getRootElement();
            } finally {
                inputStream.close();
            }
        } else {
            odomElement = null;
        }
        return odomElement;
    }

    /**
     * Returns true if and only if the IResource argument actually exists
     * @param resource the IResource to check
     * @return true if and only if the IResource argument actually exists
     */
    public static boolean resourceExists(IResource resource) {
        if (resource == null) {
            throw new IllegalArgumentException("resource cannot be null");
        }
        IPath path = resource.getLocation();
        boolean exists = path != null;
        if (exists) {
            exists = path.toFile().exists();
        }
        return exists;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 17-May-04	4413/1	doug	VBM:2004051412 Fixed PolicyValueModifier labelling issue

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 16-Apr-04	3743/1	doug	VBM:2004032101 Added a DeviceEditorContext class

 ===========================================================================
*/
