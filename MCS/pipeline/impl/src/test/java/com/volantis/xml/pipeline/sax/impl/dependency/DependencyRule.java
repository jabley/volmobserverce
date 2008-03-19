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

package com.volantis.xml.pipeline.sax.impl.dependency;

import com.volantis.shared.dependency.Dependency;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.dependency.Freshness;
import com.volantis.shared.dependency.Cacheability;
import com.volantis.shared.time.Period;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.rules.DynamicElementRuleImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.HashMap;
import java.util.Map;

public class DependencyRule
        extends DynamicElementRuleImpl {

    public static final Map STRING_2_FRESHNESS;

    static {
        Map map = new HashMap();
        map.put(null, Freshness.FRESH);
        map.put("fresh", Freshness.FRESH);
        map.put("revalidate", Freshness.REVALIDATE);
        map.put("stale", Freshness.STALE);
        STRING_2_FRESHNESS = map;
    }

    public Object startElement(
            DynamicProcess dynamicProcess, ExpandedName element,
            Attributes attributes) throws SAXException {

        XMLPipelineContext context = dynamicProcess.getPipelineContext();
        DependencyContext dependencyContext = context.getDependencyContext();

        // Get the freshness.
        String value = attributes.getValue("freshness");
        final Freshness freshness = (Freshness) STRING_2_FRESHNESS.get(value);
        if (freshness == null) {
            forwardError(dynamicProcess, "Unknown freshness value: " + value);
        }

        // Get the revalidated.
        value = attributes.getValue("revalidated");
        final Freshness revalidated = (Freshness) STRING_2_FRESHNESS.get(value);
        if (revalidated == null) {
            forwardError(dynamicProcess, "Unknown revalidated value: " + value);
        }

        // Get the cacheable.
        value = attributes.getValue("cacheable");
        final boolean cacheable;
        if (value == null) {
            cacheable = true;
        } else {
            cacheable = "true".equalsIgnoreCase(value);
        }
        Cacheability cacheability =
                (cacheable ? Cacheability.CACHEABLE : Cacheability.UNCACHEABLE);

        // Get the time to live.
        value = attributes.getValue("time-to-live");
        final Period timeToLive;
        if (value == null || value.equals("indefinitely")) {
            timeToLive = Period.INDEFINITELY;
        } else {
            timeToLive = Period.inSeconds(Integer.parseInt(value));
        }

        Dependency dependency = new TestDependency(
                cacheability, timeToLive, freshness, revalidated);

        dependencyContext.addDependency(dependency);

        return null;
    }

    public void endElement(
            DynamicProcess dynamicProcess, ExpandedName element, Object object)
            throws SAXException {
    }

}
