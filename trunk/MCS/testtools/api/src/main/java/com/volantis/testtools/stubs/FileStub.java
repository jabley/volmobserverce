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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.content.IContentDescription;

import java.io.InputStream;

public class FileStub extends ResourceStub implements IFile {
    public void appendContents(InputStream inputStream, boolean b, boolean b1, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void appendContents(InputStream inputStream, int i, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void create(InputStream inputStream, boolean b, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void create(InputStream inputStream, int i, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void createLink(IPath iPath, int i, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void delete(boolean b, boolean b1, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public String getCharset() throws CoreException {
        return null;
    }

    public String getCharset(boolean b) throws CoreException {
        return null;
    }

    public IContentDescription getContentDescription() throws CoreException {
        return null;
    }

    public InputStream getContents() throws CoreException {
        return null;
    }

    public InputStream getContents(boolean b) throws CoreException {
        return null;
    }

    public int getEncoding() throws CoreException {
        return 0;
    }

    public IFileState[] getHistory(IProgressMonitor iProgressMonitor) throws CoreException {
        return new IFileState[0];
    }

    public void move(IPath iPath, boolean b, boolean b1, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void setCharset(String s) throws CoreException {
    }

    public void setCharset(String s, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void setContents(InputStream inputStream, boolean b, boolean b1, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void setContents(IFileState iFileState, boolean b, boolean b1, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void setContents(InputStream inputStream, int i, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void setContents(IFileState iFileState, int i, IProgressMonitor iProgressMonitor) throws CoreException {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Aug-04	5107/1	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 01-Dec-03	2067/1	allan	VBM:2003111911 Rework design making ODOMEditorContext immutable.

 ===========================================================================
*/
