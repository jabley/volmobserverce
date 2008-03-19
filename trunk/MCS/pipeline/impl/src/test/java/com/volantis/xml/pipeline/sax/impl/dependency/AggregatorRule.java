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
import com.volantis.shared.dependency.Tracking;
import com.volantis.shared.dependency.Cacheability;
import com.volantis.shared.dependency.Validity;
import com.volantis.shared.time.Period;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.rules.DynamicElementRuleImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class AggregatorRule
        extends DynamicElementRuleImpl {

    private static final Attributes EMPTY_ATTRIBUTES = new AttributesImpl();

    public Object startElement(
            DynamicProcess dynamicProcess, ExpandedName element,
            Attributes attributes) throws SAXException {

        XMLPipelineContext pipelineContext = dynamicProcess.getPipelineContext();

        String value = attributes.getValue("ignore");
        final boolean ignoring = "true".equalsIgnoreCase(value);

        DependencyContext context = pipelineContext.getDependencyContext();

        context.pushDependencyTracker(ignoring ? Tracking.DISABLED : Tracking.ENABLED);

        return null;
    }

    public void endElement(
            DynamicProcess dynamicProcess, ExpandedName element, Object object)
            throws SAXException {

        XMLPipelineContext pipelineContext = dynamicProcess.getPipelineContext();
        DependencyContext context = pipelineContext.getDependencyContext();

        Dependency dependency = context.extractDependency();

        context.popDependencyTracker();

        XMLProcess target = getTargetProcess(dynamicProcess);

        // Generate an element containing the freshness.
        Freshness freshness = dependency.freshness(context);
        generateSimpleElement(target, freshness.toString(),
                "freshness");

        // Generate an element containing the revalidated freshness.
        if (freshness == Freshness.REVALIDATE) {
            Freshness revalidated = dependency.revalidate(context);
            generateSimpleElement(target, revalidated.toString(),
                    "revalidated");
        }

        Cacheability cacheability = dependency.getCacheability();
        generateSimpleElement(target, cacheability.toString(), "cacheability");

        Period timeToLive = dependency.getTimeToLive();
        generateSimpleElement(target, timeToLive.toString(), "time-to-live");

        Validity validity = context.checkValidity(dependency);
        generateSimpleElement(target, validity.toString(), "validity");
    }

    private void generateSimpleElement(
            XMLProcess process, String value,
            final String localName)
            throws SAXException {

        process.startElement("", localName, localName,
                EMPTY_ATTRIBUTES);
        char[] chars = value.toCharArray();
        process.characters(chars, 0, chars.length);
        process.endElement("", localName, localName);
    }
}
