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
package com.volantis.testtools.mocks;

import com.volantis.testtools.io.ResourceUtilities;
import com.volantis.synergetics.UndeclaredThrowableException;
import com.volantis.testtools.stubs.FileStub;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A mock object that emulates File based methods on an IFile including
 * getContent(). This class also emulates the marker methods on
 * IResource required by ODOMEditorContext.
 */
public class MockFile extends FileStub {
    /**
     * The name of the file.
     */
    private String fileName;

    /**
     * The File representation of the file name.
     */
    private File file;

    /**
     * A List of markers associated with this resource.
     */
    private List markers = new ArrayList();

    /**
     * Construct a new MockFile with the given filename.
     * @param fileName The path of the file relative to the testsuite/unit
     * directory.
     */
    public MockFile(String fileName) {
        this.fileName = fileName;
    }


    /**
     * Create marker of the specified type.
     * @param type The marker type.
     * @return The created markers.
     */
    public IMarker createMarker(String type) throws CoreException {
        IMarker marker = new MockXPathMarker(type);
        markers.add(marker);

        return marker;
    }

    // javadoc inherited
    public IMarker[] findMarkers(String type, boolean b, int depth)
            throws CoreException {

        List foundMarkers = new ArrayList();
        for (int i = 0; i < markers.size(); i++) {
            IMarker marker = (IMarker) markers.get(i);
            if (marker.getType().equals(type)) {
                foundMarkers.add(marker);
            }
        }

        IMarker[] markers = new IMarker[foundMarkers.size()];
        return (IMarker[]) foundMarkers.toArray(markers);
    }

    /**
     * Override getLocation() to get the File for the IPath using
     * ResourceUtilities.
     * @throws com.volantis.synergetics.UndeclaredThrowableException If there is an IOException
     * creating the file.
     */
    // rest of javadoc inherited
    public IPath getLocation() {
        try {
            if (file == null) {
                createFile();
            }
        } catch (IOException e) {
            throw new UndeclaredThrowableException(e);
        }

        return new Path(file.getAbsolutePath());
    }

    /**
     * Override getProject() to return a MockProject.
     */
    // rest of javadoc inherited
    public IProject getProject() {
        return new MockProject();
    }
    
    /**
     * Override getContents() to return the content of the file.
     * @throws com.volantis.synergetics.UndeclaredThrowableException If there is an IOException
     * creating the file.
     */
    // rest of javadoc inherited
    public InputStream getContents() {
        InputStream is = null;
        try {
            if (file == null) {
                createFile();
            }

            is = new FileInputStream(file);

        } catch (IOException e) {
            throw new UndeclaredThrowableException(e);
        }

        return is;
    }

    /**
     * Create the File using the fileName and ResourceUtilities.
     */
    private void createFile() throws IOException {
        file = ResourceUtilities.getResourceAsFile(fileName);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 12-Dec-03	2123/1	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 ===========================================================================
*/
