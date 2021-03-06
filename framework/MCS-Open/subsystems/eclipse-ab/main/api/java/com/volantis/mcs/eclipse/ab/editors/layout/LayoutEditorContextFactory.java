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
package com.volantis.mcs.eclipse.ab.editors.layout;

import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessException;
import com.volantis.mcs.eclipse.ab.common.ClassVersionProperties;
import org.eclipse.core.resources.IFile;

/**
 * Factory for creating layout editor contexts.
 */
public abstract class LayoutEditorContextFactory {
    /**
     * The default factory instance to use for all creation of layout editor
     * contexts.
     */
    private static LayoutEditorContextFactory DEFAULT_INSTANCE =
            (LayoutEditorContextFactory) ClassVersionProperties.
                    getInstance("LayoutEditorContextFactory.class");

    /**
     * Get a default instance of this factory.
     *
     * @return a default instance of this factory
     */
    public static LayoutEditorContextFactory getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Create a layout editor context for this file and editor.
     */
    public abstract LayoutEditorContext createLayoutEditorContext(
            LayoutEditor editor, IFile file) throws PolicyFileAccessException;
}
