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
package com.volantis.mcs.css.version;

import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleSyntax;
import com.volantis.synergetics.cornerstone.utilities.MCSObject;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.properties.StyleProperty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A default implementation of CSSVersion.
 * <p>
 * This implementation has been designed specifically to allow it to
 * be constructed conveniently in two phases. The first phase is the code
 * created during theme schema processing, where all the data which is
 * currently available in the theme schema is extracted and added in. The
 * second phase is the code created manually which "fills in the gaps" and
 * defines that data which is not currently available in the theme schema.
 * <p>
 * This class operates in two modes - write mode and read mode. When an instance
 * of this class is in operation, it is in write mode and only it's mutators
 * may be called. When it has been fully populated, the method
 * {@link #markImmutable} must be called. From this point on, only it's
 * accessors may be called.
 * <p>
 * NOTE: The following is a list of the CSS 2.1 shorthand values (the ones in
 * brackets are not currently supported in MCS themes): background, border,
 * border-width, border-color, border-style, border-bottom, border-left,
 * border-right, border-top, font, list-style, margin, padding (cue, outline,
 * pause). Currently only border-*, list-style, margin and padding have
 * shorthand filtering implemented (mostly as required for CSSWAP).
 */
public class DefaultCSSVersion extends MCSObject implements CSSVersion {

    private Map versionProperties = new HashMap();

    private Set versionShorthands = new HashSet();

    /**
     * The name of the CSS version this class represents.
     */
    private String name;

    /**
     * The set of pseudo selector ids that this CSS version supports.
     */
    private Set pseudoSelectorIds;

    /**
     * The set of syntaxes which are supported by this CSS version.
     * <p/>
     * E.g. If the {@link com.volantis.mcs.themes.StyleSyntaxes.COLOR_TRIPLETS}
     * property is not supported, this means that using hex triplets for RGB
     * color values in the output CSS is not supported (and that the device
     * policy x-css.syntax.supports.color.triplets must have been set to none
     * for the device being used (or one of its ancestors)).
     */
    private Set supportedSyntaxes;

    /**
     * Flag to indicate if this instance is immutable.
     */
    private boolean immutable;

    private final Map remappableElements;

    /**
     * Initialise.
     *
     * @param name the name of the CSS version this class represents.
     */
    public DefaultCSSVersion(String name) {
        this.name = name;
        this.remappableElements = new HashMap();
    }

    /**
     * Returns a mutable CSS property for further configuration, adding it to
     * the set of supported properties if necessary.
     *
     * @param property the MCS property to mark as supported in this CSS
     *      version.
     * @return the CSS property to be configured further with value types, etc.
     */
    public DefaultCSSProperty addProperty(StyleProperty property) {

        ensureMutable();
        DefaultCSSProperty versionProperty =
                (DefaultCSSProperty) versionProperties.get(property);
        if (versionProperty == null) {
            versionProperty =
                    new DefaultCSSProperty(this, property);
            addProperty(property, versionProperty);
        }
        return versionProperty;
    }

    /**
     * Adds a special "permissive" CSS property to the set of supported
     * properties that returns true for all accessors to indicate that all
     * operations are supported, and thus requires no further configuration.
     * <p>
     * This was added especially to support adding properties at runtime from
     * the device repository to those that are already created by code
     * generation at compile time.
     * <p>
     * The device repository currently only supports detail down to individual
     * properties, so the internals of each property are "glossed over" by
     * returning true for all accessors.
     *
     * @param property the MCS property to mark as supported in this CSS
     *      version.
     */
    public void addPermissiveProperty(StyleProperty property) {

        ensureMutable();
        DefaultCSSProperty versionProperty =
                new PermissiveCSSProperty(this, property);
        addProperty(property, versionProperty);
    }

    /**
     * Adds a special "permissive" CSS property to the set of supported
     * properties. This method is the same as addPermissiveProperty(StyleProperty property) but 
     * get others parameters. 
     *
     * @param propertyName the MCS property name to mark as supported in this CSS
     *      version.
     * @param property the MCS property to mark as supported in this CSS
     *      version.    
     */
    public void addPermissiveProperty(String propertyName, StyleProperty property) {

        ensureMutable();
        DefaultCSSProperty versionProperty =
                new PermissiveCSSProperty(this, property);
        addProperty(propertyName, versionProperty);
    }
    
    
    /**
     * Add a property to the set of supported properties.
     *
     * @param property the MCS property to mark as supported in this CSS
     *      version.
     * @param versionProperty the CSS property to add.
     */
    private void addProperty(StyleProperty property,
            DefaultCSSProperty versionProperty) {

        ensureMutable();
        versionProperties.put(property.getName(), versionProperty);
    }

    private void addProperty(String propertyName,
            DefaultCSSProperty versionProperty) {

        ensureMutable();
        versionProperties.put(propertyName, versionProperty);
    }
        
    
    /**
     * Remove a property from the set of supported properties.
     *
     * @param property the MCS property to mark as not supported in this CSS
     *      version.
     */
    public void removeProperty(StyleProperty property) {

        ensureMutable();
        versionProperties.remove(property.getName());
    }

    public void addShorthandProperty(StyleShorthand shorthand) {

        ensureMutable();
        versionShorthands.add(shorthand.getName());
    }
    
    public void addShorthandProperty(String shorthandName)  {
        
        ensureMutable();
        versionShorthands.add(shorthandName);
    }

    public void removeShorthand(StyleShorthand shorthand) {

        ensureMutable();
        versionShorthands.remove(shorthand.getName());
    }

    /**
     * Add the set of pseudo selector ids supplied to this CSS version.
     *
     * @param selectorIds pseudo selector ids which should be rendered for this
     *      version of CSS.
     */
    public void addPseudoSelectorId(String[] selectorIds) {

        ensureMutable();
        if (pseudoSelectorIds == null) {
            pseudoSelectorIds = new HashSet();
        }
        pseudoSelectorIds.addAll(Arrays.asList(selectorIds));
    }


    /**
     * Add a syntax which is explicitly supported by this version of CSS.
     *
     * @param syntax    the supported syntax
     */
    public void addSyntax(StyleSyntax syntax) {
        addSyntax(syntax.getName());
    }

    /**
     * Add a syntax which is explicitly supported by this version of CSS.
     *
     * @param syntaxName    supported syntax name
     */
    public void addSyntax(String syntaxName) {
        if (supportedSyntaxes == null) {
            supportedSyntaxes = new HashSet();
        }

        supportedSyntaxes.add(syntaxName);
    }

    /**
     * Remove the specified syntax (it is explicitly NOT supported by this
     * version of CSS).
     *
     * @param syntaxName    unsupported syntax name
     */
    public void removeSyntax(String syntaxName) {
        if (supportedSyntaxes != null) {
            supportedSyntaxes.remove(syntaxName);
        }
    }

    /**
     * Mark this instance as immutable.
     * <p>
     * This instance must be fully configured before this method is called.
     * <p>
     * This method must be called before this instance may be used in anger
     * (i.e. before any of the support* methods can be called).
     */
    public void markImmutable() {

        immutable = true;
    }

    /**
     * Helper method called by the mutators to ensure we are in the correct
     * "mode" - see the class comment.
     */
    void ensureMutable() {

        if (immutable) {
            throw new IllegalStateException();
        }
    }

    /**
     * Helper method called by the accessors to ensure we are in the correct
     * "mode" - see the class comment.
     */
    void ensureImmutable() {

        if (!immutable) {
            throw new IllegalStateException();
        }
    }

    // Javadoc inherited.
    public CSSProperty getProperty(StyleProperty property) {

        // TODO: disabled temporarily so that we can introspect while building
        // for custom properties. Fix this when we have the time...
        //
        // ensureImmutable();
        return (CSSProperty) versionProperties.get(property.getName());
    }

    /**
     * Returns CSSProperty if exists in this cssVersion. 
     * 
     * This method is useful for reading device dependent properties like -moz-border-radius 
     * 
     * @param propertyName - name of property
     * @return CSSProperty or null
     */
    public CSSProperty getProperty(String propertyName) {
        ensureImmutable();
        return (CSSProperty) versionProperties.get(propertyName);
    }

    /**
     * Returns name of CSS Property correspnding to the specified StyleProperty
     * in this version of CSS.
     * 
     * The implementation iterates the internal map by value to find the key. 
     * If entry is found in this CSSVersion, its associated key (property name) 
     * is returned, otherwise null is returned.
     * 
     * This method is useful for finding CSS property name, which is in alias 
     * for the specifed MCS style property (like mcs-border-top-left-radius).   
     * Depending on current device, different CSS properties (key) may be assigned to 
     * an MCS property (value), so iteration by value is used.
     *       
     * @param property the MCS property to query for (StylePropertyDetails.MCS_BORDER_BOTTOM_RIGHT_RADIUS
     * @return name of CSS property e.g. -webkit-border-top-left-radius  
     */
    public String getPropertyAlias(StyleProperty property) {
        ensureImmutable();
        
        String propertyName = property.getName();        
        String result = null;
        String key = null;
        String cssPropertyName = null;

        Iterator it = versionProperties.entrySet().iterator();
        while (it.hasNext() && (null == result)) {
            Map.Entry entry = (Map.Entry) it.next();
            CSSProperty cssProperty = (CSSProperty) entry.getValue();
            cssPropertyName = cssProperty.getStyleProperty().getName(); 
            if(cssPropertyName.equals(propertyName)) {
                key = (String) entry.getKey();
                
                // We look for an alias, so ignore the entry which is not really an alias
                // TODO: why is this check needed?
                if(!propertyName.equals(key)) { 
                    result = key;
                }  
            }
        }
        return result;                  
    }    
    
    // Javadoc inherited.
    public boolean supportsShorthand(StyleShorthand shorthand) {

        ensureImmutable();
        return versionShorthands.contains(shorthand.getName());
    }

    // Javadoc inherited.
    public boolean supportsShorthand(String shorthandName) {

        ensureImmutable();
        return versionShorthands.contains(shorthandName);
    }
    
    
    // Javadoc inherited.
    public boolean supportsPseudoSelectorId(String selectorId) {

        ensureImmutable();
        if (selectorId != null) {
            // We do not support mcs-* selectors on output
            if (selectorId.startsWith("mcs-")) {
                return false;
            }
        }
        return pseudoSelectorIds == null ||
                pseudoSelectorIds.contains(selectorId);
    }

    // Javadoc inherited.
    public boolean supportsSyntax(StyleSyntax syntax) {
        ensureImmutable();
        return supportedSyntaxes == null ||
                supportedSyntaxes.contains(syntax.getName());
    }

    // Javadoc unnecessary.
    public IterationAction iterate(CSSPropertyIteratee iteratee) {
        IterationAction action = IterationAction.CONTINUE;
        Iterator properties = versionProperties.values().iterator();
        while (action ==  IterationAction.CONTINUE && properties.hasNext()) {
            CSSProperty property = (CSSProperty) properties.next();
            action = iteratee.next(property);
        }
        return action;
    }

    // javadoc inherited
    public Map getRemappableElements() {
        return remappableElements;
    }
    
    // Javadoc inherited.
    protected String toStringData() {

        return "name=" + name;
    }

    // javadoc inherited
    public void setRemappableElements(Map remappableElements) {
        ensureMutable();

        // @todo validate the structure of the Map?
        synchronized(this.remappableElements) {
            synchronized(remappableElements) {
                this.remappableElements.clear();
                this.remappableElements.putAll(remappableElements);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10835/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 14-Dec-05	10829/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (4)

 29-Nov-05	10370/2	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (4)

 18-Nov-05	10370/1	geoff	VBM:2005111405 interim commit

 22-Sep-05	9540/2	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/11	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/9	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
