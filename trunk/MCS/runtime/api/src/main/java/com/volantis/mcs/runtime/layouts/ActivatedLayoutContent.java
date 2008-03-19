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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.layouts;

import com.volantis.mcs.policies.variants.content.Content;
import com.volantis.mcs.policies.variants.content.ContentType;
import com.volantis.mcs.policies.variants.content.ContentBuilder;
import com.volantis.mcs.layouts.Layout;
import com.volantis.styling.sheet.CompiledStyleSheet;

import java.util.Map;

public class ActivatedLayoutContent
        implements Content {

    private final Layout layout;
    private final CompiledStyleSheet compiledStyleSheet;
    private final Map containerNameToFragments;

    public ActivatedLayoutContent(
            Layout layout, CompiledStyleSheet compiledStyleSheet,
            Map containerNameToFragments) {
        this.layout = layout;
        this.compiledStyleSheet = compiledStyleSheet;
        this.containerNameToFragments = containerNameToFragments;
    }

    public ContentType getContentType() {
        return ContentType.LAYOUT;
    }

    public ContentBuilder getContentBuilder() {
        throw new UnsupportedOperationException();
    }

    public Layout getLayout() {
        return layout;
    }

    public CompiledStyleSheet getCompiledStyleSheet() {
        return compiledStyleSheet;
    }

    public Map getContainerNameToFragments() {
        return containerNameToFragments;
    }
}
