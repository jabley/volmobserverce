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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/xml/digester/MarinerDigester.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; a digester that 
 *                              handles Enabled objects, and the requirements 
 *                              of parsing the existing "bogus" 
 *                              mariner-config.xml file.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml.digester;

import our.apache.commons.digester.Digester;
import our.apache.commons.digester.ObjectCreationFactory;
import our.apache.commons.digester.Rule;
import our.apache.commons.digester.ObjectCreateRule;
import our.apache.commons.beanutils.ConvertUtils;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.volantis.mcs.runtime.configuration.xml.digester.EnabledCallMethodRule;

/**
 * A {@link Digester} that handles {@link Enabled} objects, and the 
 * requirements of parsing the existing "bogus" mcs-config.xml file.
 * <p>
 * This ensures that objects which implement the {@link Enabled} marker 
 * interface are not created or used by the digester if they do not have 
 * an attribute enabled="true".
 * <p>
 * NOTE: this disables a lot of Digester methods deliberately, because the 
 * design requires that existing rules may have to be modified for Enabled 
 * support before they can be used with this digester. This way is safer, and 
 * we can validate and allow more rules on an individual basis as required.
 */ 
public class MarinerDigester extends Digester {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    static {
        // Make sure we convert "enabled" and "disabled" to Boolean.
        ConvertUtils.register(new MarinerXmlBooleanConverter(), Boolean.class);
    }
    
    /**
     * Construct an instance of this class, allowing an XMLReader to be passed 
     * in. This manner of constructing the class is supposedly safe in 
     * unfriendly (i.e. appserver) environments.
     *  
     * @param reader the reader to use to parse the XML file.
     */ 
    public MarinerDigester(XMLReader reader) {
        super(reader);
        // Ensure we fail on errors; 
        // Note that errors are logged by Digester already.
        setErrorHandler(new ErrorHandler() {
            public void warning(SAXParseException e) throws SAXException {
            }

            public void error(SAXParseException e) throws SAXException {
                throw e;
            }

            public void fatalError(SAXParseException e) throws SAXException {
                throw e;
            }
        });
    }

    // Inherit Javadoc
    public void addCallMethod(String pattern, String methodName,
            int paramCount, Class paramTypes[]) {
        addRule(pattern, 
                new EnabledCallMethodRule(methodName, paramCount, paramTypes));
    }

    /**
     * Add a "bogus call parameter literal" rule for the specified parameters.
     *
     * @param pattern Element matching pattern
     * @param paramIndex Zero-relative parameter index to set
     *  (from the body of this element)
     * @param value bogus hardcoded value to use
     */ 
    public void addBogusCallParamLiteral(String pattern, int paramIndex,
            String value) {
        addRule(pattern, new BogusCallParamLiteralRule(paramIndex, value));
    }

    // Inherit Javadoc
    public void addObjectCreate(String pattern, Class clazz) {
        Rule objectCreateRule;
        if (Enabled.class.isAssignableFrom(clazz)) {
            // Create Enabled objects specially.
            objectCreateRule = new EnabledObjectCreateRule(clazz,
                AlwaysEnabled.class.isAssignableFrom(clazz));
        } else {
            // Create other objects as per normal. 
            objectCreateRule = new ObjectCreateRule(clazz);
        }
        addRule(pattern, objectCreateRule);
    }

    // Inherit Javadoc
    public void addSetProperties(String pattern, String attributeName,
            String propertyName) {
        addRule(pattern,
                new EnabledSetPropertiesRule(attributeName, propertyName));
    }

    // Inherit Javadoc
    public void addSetProperties(String pattern, String[] attributeNames,
            String[] propertyNames) {
        addRule(pattern,
                new EnabledSetPropertiesRule(attributeNames, propertyNames));
    }

    // Inherit Javadoc
    public void addSetNext(String pattern, String methodName) {
        addRule(pattern, new EnabledSetNextRule(methodName));
    }

    /**
     * Add a "bogus set next parent" rule for the specified parameters.
     *
     * @param pattern Element matching pattern
     * @param methodName Method name to call on the parent element
     * @param parentIndexHack
     */ 
    public void addBogusSetNextParentIndex(String pattern, String methodName, 
            int parentIndexHack) {
        BogusSetNextRule next = new BogusSetNextRule(methodName);
        next.setBogusParentIndex(parentIndexHack);
        addRule(pattern, next);
    }

    // Inherit Javadoc
    public void addRule(String pattern, Rule rule) {
        // all the safe rules
        if (rule instanceof EnabledCallMethodRule ||
                rule instanceof ObjectCreateRule || 
                rule instanceof EnabledObjectCreateRule ||
                rule instanceof BogusObjectCreateRule ||
                rule instanceof EnabledSetPropertiesRule ||
                rule instanceof BogusCallParamLiteralRule || 
                rule instanceof EnabledSetNextRule ||
                rule instanceof BogusSetNextRule) {
            super.addRule(pattern, rule);
        } else {
            throw new UnsupportedOperationException("not implemented");
        }
    }
    
    // These are disabled since they may not work correctly with our design.
    
    public void addBeanPropertySetter(String pattern) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addBeanPropertySetter(String pattern,
            String propertyName) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addCallMethod(String pattern, String methodName) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addCallMethod(String pattern, String methodName,
            int paramCount) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addCallMethod(String pattern, String methodName,
            int paramCount, String paramTypes[]) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addCallParam(String pattern, int paramIndex) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addCallParam(String pattern, int paramIndex,
            String attributeName) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addFactoryCreate(String pattern, String className) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addFactoryCreate(String pattern, Class clazz) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addFactoryCreate(String pattern, String className,
            String attributeName) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addFactoryCreate(String pattern, Class clazz,
            String attributeName) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addFactoryCreate(String pattern,
            ObjectCreationFactory creationFactory) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addObjectCreate(String pattern, String className) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addObjectCreate(String pattern, String className,
            String attributeName) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addObjectCreate(String pattern,
            String attributeName,
            Class clazz) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addSetNext(String pattern, String methodName,
            String paramType) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addSetRoot(String pattern, String methodName) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addSetRoot(String pattern, String methodName,
            String paramType) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addSetProperties(String pattern) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addSetProperty(String pattern, String name, String value) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addSetTop(String pattern, String methodName) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addSetTop(String pattern, String methodName,
            String paramType) {
        throw new UnsupportedOperationException("not implemented");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 ===========================================================================
*/
