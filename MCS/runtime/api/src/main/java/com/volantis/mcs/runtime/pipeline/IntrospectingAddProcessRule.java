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
package com.volantis.mcs.runtime.pipeline;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.rules.AbstractAddProcessRule;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * An {@link AbstractAddProcessRule} that uses reflection to create the rule
 * that is added to the pipeline. Note - it would be nice to use Introspection
 * to call map form the attributes passed in via the {@link #createProcess}
 * methods to the appropriate "set" method on the process.
 */
public class IntrospectingAddProcessRule extends AbstractAddProcessRule {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
                LocalizationFactory.createLogger(
                            IntrospectingAddProcessRule.class);

    /**
     * Fully qualified class name of the {@link XMLProcess} that this rule
     * will create, initialize and then add to the pipeline
     */
    private String processClassName;

    /**
     * Initializes a <code>IntrospectingAddProcessRule</code> instance
     * @param processClassName the fully qualified class name of the
     * {@link XMLProcess} that this rule will create, initialize and then add
     * to the pipeline
     */
    public  IntrospectingAddProcessRule(String processClassName) {
        if (processClassName == null) {
            throw new IllegalStateException("processClassName cannot be null");
        }
        this.processClassName = processClassName;
    }

    // javadoc inherited
    protected XMLProcess createProcess(DynamicProcess dynamicProcess,
                                       ExpandedName elementName,
                                       Attributes attributes)
                throws SAXException {
        XMLProcess process = null;
        try {
            // if the rule cannot be created then we log an error and return
            // null. 
            Class processClass = Class.forName(processClassName);
            process = (XMLProcess) processClass.newInstance();
        } catch (ClassNotFoundException e) {
            logger.error("introspecting-rule-class-not-found",
                         processClassName,
                         e);
        } catch (InstantiationException e) {
            logger.error("introspecting-rule-cannot-instantiate-class",
                         processClassName,
                         e);
        } catch (IllegalAccessException e) {
            logger.error("introspecting-rule-illegal-access",
                         processClassName,
                         e);
        }
        return process;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Jun-05	7418/1	doug	VBM:2005021505 Simplified pipeline initialization

 ===========================================================================
*/
