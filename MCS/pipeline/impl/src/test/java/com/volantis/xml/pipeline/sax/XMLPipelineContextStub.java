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

package com.volantis.xml.pipeline.sax;

import com.volantis.shared.environment.EnvironmentInteractionTracker;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;
import org.xml.sax.Locator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

/**
 * Stub XMLPipelineContext for use in testcases.  Testcases can specialise this
 * class to override the methods they need to test.
 */
public abstract class XMLPipelineContextStub implements XMLPipelineContext {

    public XMLPipelineConfiguration getPipelineConfiguration() {
        return null;
    }

    public XMLPipelineFactory getPipelineFactory() {
        return null;
    }

    public void pushBaseURI(String baseURI)
            throws MalformedURLException {
    }

    public URL popBaseURI() {
        return null;
    }

    public URL getCurrentBaseURI() {
        return null;
    }

    public void pushLocator(Locator locator) {
    }

    public Locator popLocator() {
        return null;
    }

    public Locator getCurrentLocator() {
        return null;
    }

    public Iterator getLocators() {
        return null;
    }

    public EnvironmentInteractionTracker getEnvironmentInteractionTracker() {
        return null;
    }

    public void pushObject(Object object, boolean releaseOnPop) {
    }

    public Object popObject() {
        return null;
    }

    public Object popObject(Object expected) {
        return null;
    }

    public Object getCurrentObject() {
        return null;
    }

    public Object findObject(Class clazz) {
        return null;
    }

    // javadoc inherited
    public String getDebugOutputFilesPrefix() {
        return null;
    }

    // javadoc inherited
    public void setDebugOutputFilesPrefix(String debugOutputPrefix) {
    }


    public NamespacePrefixTracker getNamespacePrefixTracker() {
        return null;
    }

    public FlowControlManager getFlowControlManager() {
        return null;
    }

    public ExpressionContext getExpressionContext() {
        return null;
    }

    public DependencyContext getDependencyContext() {
        return null;
    }

    public void enterErrorRecoveryMode()
            throws IllegalStateException {
    }

    public boolean inErrorRecoveryMode() {
        return false;
    }

    public void exitErrorRecoveryMode()
            throws IllegalStateException {
    }

    public XMLPipelineContext.RecoveryPoint addRecoveryPoint() {
        return null;
    }

    public void removeRecoveryPoint(XMLPipelineContext.RecoveryPoint recoveryPoint,
                                    boolean keepChanges) {
    }

    public void setProperty(Object key, Object value, boolean releaseOnChange) {
    }

    public Object getProperty(Object key) {
        return null;
    }

    public Object removeProperty(Object key) {
        return null;
    }

    public void release() {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	7950/6	allan	VBM:2005041317 Some testcases for smart server

 01-Apr-05	6798/1	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 20-Jan-04	527/3	adrian	VBM:2004011903 Added Copyright statements to new classes

 20-Jan-04	527/1	adrian	VBM:2004011903 Added ContextAnnotationProcess and supporting classes

 ===========================================================================
*/
