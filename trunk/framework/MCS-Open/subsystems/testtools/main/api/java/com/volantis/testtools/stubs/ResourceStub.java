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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.QualifiedName;

/**
 * A stub implementation of IResource.
 */
public class ResourceStub extends SchedulingRuleStub implements IResource {
    public void accept(IResourceProxyVisitor iResourceProxyVisitor, int i) throws CoreException {
    }

    public void accept(IResourceVisitor iResourceVisitor) throws CoreException {
    }

    public void accept(IResourceVisitor iResourceVisitor, int i, boolean b) throws CoreException {
    }

    public void accept(IResourceVisitor iResourceVisitor, int i, int i1) throws CoreException {
    }

    public void clearHistory(IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void copy(IPath iPath, boolean b, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void copy(IPath iPath, int i, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void copy(IProjectDescription iProjectDescription, boolean b, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void copy(IProjectDescription iProjectDescription, int i, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public IMarker createMarker(String s) throws CoreException {
        return null;
    }

    public void delete(boolean b, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void delete(int i, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void deleteMarkers(String s, boolean b, int i) throws CoreException {
    }

    public boolean exists() {
        return false;
    }

    public IMarker findMarker(long l) throws CoreException {
        return null;
    }

    public IMarker[] findMarkers(String s, boolean b, int i) throws CoreException {
        return new IMarker[0];
    }

    public String getFileExtension() {
        return null;
    }

    public IPath getFullPath() {
        return null;
    }

    public long getLocalTimeStamp() {
        return 0;
    }

    public IPath getLocation() {
        return null;
    }

    public IMarker getMarker(long l) {
        return null;
    }

    public long getModificationStamp() {
        return 0;
    }

    public String getName() {
        return null;
    }

    public IContainer getParent() {
        return null;
    }

    public String getPersistentProperty(QualifiedName qualifiedName) throws CoreException {
        return null;
    }

    public IProject getProject() {
        return null;
    }

    public IPath getProjectRelativePath() {
        return null;
    }

    public IPath getRawLocation() {
        return null;
    }

    public Object getSessionProperty(QualifiedName qualifiedName) throws CoreException {
        return null;
    }

    public int getType() {
        return 0;
    }

    public IWorkspace getWorkspace() {
        return null;
    }

    public boolean isAccessible() {
        return false;
    }

    public boolean isDerived() {
        return false;
    }

    public boolean isLocal(int i) {
        return false;
    }

    public boolean isLinked() {
        return false;
    }

    public boolean isPhantom() {
        return false;
    }

    public boolean isReadOnly() {
        return false;
    }

    public boolean isSynchronized(int i) {
        return false;
    }

    public boolean isTeamPrivateMember() {
        return false;
    }

    public void move(IPath iPath, boolean b, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void move(IPath iPath, int i, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void move(IProjectDescription iProjectDescription, boolean b, boolean b1, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void move(IProjectDescription iProjectDescription, int i, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void refreshLocal(int i, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public void setDerived(boolean b) throws CoreException {
    }

    public void setLocal(boolean b, int i, IProgressMonitor iProgressMonitor) throws CoreException {
    }

    public long setLocalTimeStamp(long l) throws CoreException {
        return 0;
    }

    public void setPersistentProperty(QualifiedName qualifiedName, String s) throws CoreException {
    }

    public void setReadOnly(boolean b) {
    }

    public void setSessionProperty(QualifiedName qualifiedName, Object o) throws CoreException {
    }

    public void setTeamPrivateMember(boolean b) throws CoreException {
    }

    public void touch(IProgressMonitor iProgressMonitor) throws CoreException {
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

 17-Aug-04	5107/1	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 29-Nov-03	2067/1	allan	VBM:2003111911 Create ODOMEditorContext.

 ===========================================================================
*/
