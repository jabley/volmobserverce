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
package com.volantis.testtools.stubs;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import java.util.Map;

/**
 * A stub implementation of IMarker.
 */
public class MarkerStub implements IMarker {
    public void delete() throws CoreException {
    }

    public boolean exists() {
        return false;
    }

    public Object getAttribute(String s) throws CoreException {
        return null;
    }

    public int getAttribute(String s, int i) {
        return 0;
    }

    public String getAttribute(String s, String s1) {
        return null;
    }

    public boolean getAttribute(String s, boolean b) {
        return false;
    }

    public Map getAttributes() throws CoreException {
        return null;
    }

    public Object[] getAttributes(String[] strings) throws CoreException {
        return new Object[0];
    }

    public long getCreationTime() throws CoreException {
        return 0;
    }

    public long getId() {
        return 0;
    }

    public IResource getResource() {
        return null;
    }

    public String getType() throws CoreException {
        return null;
    }

    public boolean isSubtypeOf(String s) throws CoreException {
        return false;
    }

    public void setAttribute(String s, int i) throws CoreException {
    }

    public void setAttribute(String s, Object o) throws CoreException {
    }

    public void setAttribute(String s, boolean b) throws CoreException {
    }

    public void setAttributes(String[] strings, Object[] objects) throws CoreException {
    }

    public void setAttributes(Map map) throws CoreException {
    }

    public Object getAdapter(Class aClass) {
        return null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-03	2067/1	allan	VBM:2003111911 Create ODOMEditorContext.

 ===========================================================================
*/
