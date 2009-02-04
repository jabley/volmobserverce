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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.widgets.styles;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.properties.MCSEffectStyleKeywords;
import com.volantis.synergetics.log.LogDispatcher;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents parameters of Random effect 
 */
public final class RandomEffectParameters extends EffectParameters  {
    
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(RandomEffectParameters.class);
    
    /**
     * List of EffectDesc object describing effects to choose from
     */
    private final ArrayList allowedEffects = new ArrayList();
    
    public RandomEffectParameters() {        
    }
    
    public void addAllowedEffect(EffectDescriptor desc) {
        allowedEffects.add(desc);
    }

    /**
     * Generates JavaScript representations of this class.
     * EffectParameters are represented by object literal
     */        
    public String toScript() {
        StringBuffer buffer = new StringBuffer("{ allowed: [");
        Iterator i = allowedEffects.iterator();
        boolean firstEffect = true;
        while (i.hasNext()) {
            if(!firstEffect) {
                buffer.append(',');
            }
            firstEffect = false;
            buffer.append(((EffectDescriptor)i.next()).toScriptConstructor());
        }
        buffer.append("]}");
        return buffer.toString();
    }
        
    public String toString() {
        StringBuffer buffer = new StringBuffer("{ allowed: [");
        Iterator i = allowedEffects.iterator();
        boolean firstEffect = true;
        while (i.hasNext()) {
            if(!firstEffect) {
                buffer.append(',');
            }
            firstEffect = false;
            buffer.append(i.next());
        }
        buffer.append("]}");
        return buffer.toString();
    }

    /**
     * RandomEffectParameters with the same data are considered equal
     */
    public boolean equals(Object obj) {
        
        if (!(obj instanceof RandomEffectParameters)) {
          return false;
        }
     
        if (this == obj) {
          return true;
        }
        
        RandomEffectParameters other = (RandomEffectParameters)obj;            
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(this.allowedEffects, other.allowedEffects);
        return eb.isEquals();
    }

    /**
     * Overriden hashCode so as to maintain the general contract for the hashCode method, 
     * which states that equal objects must have equal hash codes.
     */        
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(this.allowedEffects);
        return hcb.toHashCode();
    }
    
    /**
     * Parser for random effect parameters
     */
    public static class Parser extends EffectParameters.Parser {

        /**
         * Parses array of parameters into RandomEffectParameters class
         */
        protected EffectParameters parse(String[] paramsArray) {
            RandomEffectParameters params = new RandomEffectParameters();
            for (int i = 0; i < paramsArray.length; i++) {
                EffectDescriptor descriptor = EffectDescriptor.parse(paramsArray[i]);
                if (MCSEffectStyleKeywords.RANDOM.getName().equals(descriptor.getName())){
                    //log that random is not allowed inside random
                    logger.warn("unexpected-mcs-effect-random");
                }
                
                params.addAllowedEffect(descriptor);
            }            
            return params;
        }
    }
}
