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

package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.Namespace;

public class WebDriverElements {

    public static final ImmutableExpandedName PARAMETERS = createName("parameters");

    public static final ImmutableExpandedName PARAMETER  = createName("parameter");

    public static final ImmutableExpandedName HEADERS  = createName("headers");

    public static final ImmutableExpandedName HEADER  = createName("header");

    public static final ImmutableExpandedName COOKIES = createName("cookies");

    public static final ImmutableExpandedName COOKIE = createName("cookie");

    public static final ImmutableExpandedName CONTENT = createName("content");

    public static final ImmutableExpandedName SCRIPT = createName("script");

    public static final ImmutableExpandedName PROXY = createName("proxy");

    public static final ImmutableExpandedName GET = createName("get");

    public static final ImmutableExpandedName POST = createName("post");

    public static final ImmutableExpandedName NAME = createName("name");
    public static final ImmutableExpandedName VALUE = createName("value");
    public static final ImmutableExpandedName FROM = createName("from");
    public static final ImmutableExpandedName TARGET = createName("target");

    private static ImmutableExpandedName createName(String localName) {
        return new ImmutableExpandedName(Namespace.WEB_DRIVER.getURI(),
                localName);
    }
}
