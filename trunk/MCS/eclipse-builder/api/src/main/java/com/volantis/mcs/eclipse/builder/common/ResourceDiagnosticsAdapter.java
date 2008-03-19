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
package com.volantis.mcs.eclipse.builder.common;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;

import java.util.List;
import java.util.Iterator;

import com.volantis.mcs.interaction.diagnostic.ProxyDiagnostic;

/**
 * An adapter that allows diagnostics to be associated with a specific resource,
 * generating error markers as appropriate.
 */
public class ResourceDiagnosticsAdapter {
    /**
     * The attribute in IMarker to use as a key for storing the path.
     */
    public static final String DIAGNOSTIC_PATH_KEY =
            "com.volantis.mcs.builder.diagnosticPath";

    /**
     * The resource on which diagnostics will be set
     */
    private final IResource resource;

    /**
     * Create a diagnostics adapter for the specified resource.
     *
     * @param resource The resource on which diagnostics will be set
     */
    public ResourceDiagnosticsAdapter(IResource resource) {
        this.resource = resource;
    }

    /**
     * Sets a list of diagnostics against the resource.
     *
     * @param diagnostics The list of diagnostics
     * @throws CoreException If an error occurs setting the diagnostics.
     */
    public void setDiagnostics(List diagnostics) throws CoreException {
        resource.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);

        if (diagnostics != null && !diagnostics.isEmpty()) {
            Iterator it = diagnostics.iterator();
            while (it.hasNext()) {
                ProxyDiagnostic diagnostic = (ProxyDiagnostic) it.next();
                IMarker marker = resource.createMarker(IMarker.PROBLEM);
                marker.setAttribute(IMarker.MESSAGE,
                        diagnostic.getMessage().getMessage());
                marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
                marker.setAttribute(DIAGNOSTIC_PATH_KEY,
                        diagnostic.getProxy().getPathFromRoot().getAsString());
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Nov-05	10150/1	adrianj	VBM:2005110437 Validation in incremental builder

 ===========================================================================
*/
