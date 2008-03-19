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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.editors.layout;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;

/**
 * This method is a hack to create an IFile implementation for a byte array
 * @todo get rid of this class
 */
public class LayoutIFile implements IFile {
    private final byte[] content;
    private final IFile enclosingFile;

    public LayoutIFile(final byte[] content, final IFile enclosingFile) {

        this.content = content;
        this.enclosingFile = enclosingFile;
    }

    public void appendContents(InputStream inputStream, boolean b, boolean b1, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void appendContents(InputStream inputStream, int i, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void create(InputStream inputStream, boolean b, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void create(InputStream inputStream, int i, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void createLink(IPath iPath, int i, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void delete(boolean b, boolean b1, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public String getCharset() throws CoreException {
        throw new UnsupportedOperationException();
    }

    public String getCharset(boolean b) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public String getCharsetFor(Reader reader) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public IContentDescription getContentDescription() throws CoreException {
        throw new UnsupportedOperationException();
    }

    public InputStream getContents() throws CoreException {
        final InputStream is;
        if (content == null) {
            is = null;
        } else {
            is = new ByteArrayInputStream(content);
        }
        return is;
    }

    public InputStream getContents(boolean b) throws CoreException {
        return getContents();
    }

    public int getEncoding() throws CoreException {
        throw new UnsupportedOperationException();
    }

    public IPath getFullPath() {
        throw new UnsupportedOperationException();
    }

    public IFileState[] getHistory(IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public String getName() {
        throw new UnsupportedOperationException();
    }

    public boolean isReadOnly() {
        throw new UnsupportedOperationException();
    }

    public void move(IPath iPath, boolean b, boolean b1, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void setCharset(String path) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void setCharset(String path, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void setContents(IFileState iFileState, boolean b, boolean b1, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void setContents(IFileState iFileState, int i, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void setContents(InputStream inputStream, boolean b, boolean b1, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void setContents(InputStream inputStream, int i, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void accept(IResourceProxyVisitor iResourceProxyVisitor, int i) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void accept(IResourceVisitor iResourceVisitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void accept(IResourceVisitor iResourceVisitor, int i, boolean b) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void accept(IResourceVisitor iResourceVisitor, int i, int i1) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void clearHistory(IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void copy(IPath iPath, boolean b, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void copy(IPath iPath, int i, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void copy(IProjectDescription iProjectDescription, boolean b, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void copy(IProjectDescription iProjectDescription, int i, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public IMarker createMarker(String path) throws CoreException {
        return enclosingFile.createMarker(path);
    }

    public void delete(boolean b, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void delete(int i, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void deleteMarkers(String path, boolean b, int i) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public boolean exists() {
        return true;
    }

    public IMarker findMarker(long l) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public IMarker[] findMarkers(String path, boolean b, int i) throws CoreException {
        return new IMarker[]{};
    }

    public String getFileExtension() {
        throw new UnsupportedOperationException();
    }

    public long getLocalTimeStamp() {
        throw new UnsupportedOperationException();
    }

    public IPath getLocation() {
        return enclosingFile.getLocation();
    }

    public IMarker getMarker(long l) {
        throw new UnsupportedOperationException();
    }

    public long getModificationStamp() {
        throw new UnsupportedOperationException();
    }

    public IContainer getParent() {
        throw new UnsupportedOperationException();
    }

    public String getPersistentProperty(QualifiedName qualifiedName) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public IProject getProject() {
        return enclosingFile.getProject();
    }

    public IPath getProjectRelativePath() {
        throw new UnsupportedOperationException();
    }

    public IPath getRawLocation() {
        throw new UnsupportedOperationException();
    }

    public Object getSessionProperty(QualifiedName qualifiedName) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public int getType() {
        throw new UnsupportedOperationException();
    }

    public IWorkspace getWorkspace() {
        return getProject().getWorkspace();
    }

    public boolean isAccessible() {
        throw new UnsupportedOperationException();
    }

    public boolean isDerived() {
        throw new UnsupportedOperationException();
    }

    public boolean isLinked() {
        throw new UnsupportedOperationException();
    }

    public boolean isLocal(int i) {
        throw new UnsupportedOperationException();
    }

    public boolean isPhantom() {
        throw new UnsupportedOperationException();
    }

    public boolean isSynchronized(int i) {
        throw new UnsupportedOperationException();
    }

    public boolean isTeamPrivateMember() {
        throw new UnsupportedOperationException();
    }

    public void move(IPath iPath, boolean b, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void move(IPath iPath, int i, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void move(IProjectDescription iProjectDescription, boolean b, boolean b1, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void move(IProjectDescription iProjectDescription, int i, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void refreshLocal(int i, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void setDerived(boolean b) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void setLocal(boolean b, int i, IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public long setLocalTimeStamp(long l) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void setPersistentProperty(QualifiedName qualifiedName, String path) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void setReadOnly(boolean b) {
        throw new UnsupportedOperationException();
    }

    public void setSessionProperty(QualifiedName qualifiedName, Object o) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void setTeamPrivateMember(boolean b) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public void touch(IProgressMonitor iProgressMonitor) throws CoreException {
        throw new UnsupportedOperationException();
    }

    public Object getAdapter(Class aClass) {
        throw new UnsupportedOperationException();
    }

    public boolean contains(ISchedulingRule iSchedulingRule) {
        throw new UnsupportedOperationException();
    }

    public boolean isConflicting(ISchedulingRule iSchedulingRule) {
        throw new UnsupportedOperationException();
    }
}
