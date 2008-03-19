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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools.stubs;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Stub for the IContainer interface.
 * @since Eclipse v3.0 port
 */
public class ContainerStub extends ResourceStub implements IContainer {
    public boolean exists(IPath iPath) {
        return false;
    }

    public IResource findMember(String s) {
        return null;
    }

    public IResource findMember(String s, boolean b) {
        return null;
    }

    public IResource findMember(IPath iPath) {
        return null;
    }

    public IResource findMember(IPath iPath, boolean b) {
        return null;
    }

    public String getDefaultCharset() throws CoreException {
        return null;
    }

    public String getDefaultCharset(boolean b) throws CoreException {
        return null;
    }

    public IFile getFile(IPath iPath) {
        return null;
    }

    public IFolder getFolder(IPath iPath) {
        return null;
    }

    public IResource[] members() throws CoreException {
        return new IResource[0];
    }

    public IResource[] members(boolean b) throws CoreException {
        return new IResource[0];
    }

    public IResource[] members(int i) throws CoreException {
        return new IResource[0];
    }

    public IFile[] findDeletedMembersWithHistory(int i, IProgressMonitor iProgressMonitor) throws CoreException {
        return new IFile[0];
    }

    public void setDefaultCharset(String s) throws CoreException {
    }

    public void setDefaultCharset(String s, IProgressMonitor iProgressMonitor) throws CoreException {
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

 ===========================================================================
*/
