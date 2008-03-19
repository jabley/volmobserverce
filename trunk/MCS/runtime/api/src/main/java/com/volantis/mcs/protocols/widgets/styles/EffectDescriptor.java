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
import com.volantis.mcs.protocols.widgets.JavaScriptStringFactory;
import com.volantis.mcs.themes.properties.MCSEffectStyleKeywords;
import com.volantis.synergetics.log.LogDispatcher;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Encapsulates description of a single effect 
 */
public class EffectDescriptor {
    private String name = null;
    private String direction = null;
    private double duration = Double.NaN; 
    private double fps = Double.NaN;
    private EffectParameters params = null;
    
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(EffectDescriptor.class);
    
    protected EffectDescriptor() {
    }
    
    public EffectDescriptor(
            String name, 
            String dir, 
            double duration, 
            double fps, 
            EffectParameters params) {
        this.name = name;
        this.direction = dir;
        this.duration = duration; 
        this.fps = fps;
        this.params = params;            
    }
    
    public EffectDescriptor(String name, String dir, RandomEffectParameters params) {
        this(name, dir, Double.NaN, Double.NaN, params);
    }

    public EffectDescriptor(String name, String dir) {
        this(name, dir, null);
    }

    public String getDirection() {
        return direction;
    }

    private void setDirection(String direction) {
        this.direction = direction;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getFps() {
        return fps;
    }

    public void setFps(double fps) {
        this.fps = fps;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public EffectParameters getParams() {
        return params;
    }

    private void setParams(EffectParameters params) {
        this.params = params;
    }

    /**
     * Generates JavaScript representations of this class
     */
    public String toScriptConstructor() {
        JavaScriptStringFactory jsf = JavaScriptStringFactory.getInstance(); 
        StringBuffer ctor = new StringBuffer("new Widget.EffectDesc(");
        ctor.append(jsf.createJavaScriptString(name)).append(", ")            
            .append(jsf.createJavaScriptString(direction)).append(", ")            
            .append(duration).append(", ")            
            .append(fps).append(", ")            
            .append((null != params) ? params.toScript() : "null")
            .append(")");            
        return ctor.toString(); 
    }

    public String toString() {
        JavaScriptStringFactory jsf = JavaScriptStringFactory.getInstance(); 
        StringBuffer out = new StringBuffer("{");
        out.append(jsf.createJavaScriptString(name)).append(", ")            
           .append(jsf.createJavaScriptString(direction)).append(", ")            
           .append(duration).append(", ")            
           .append(fps).append(", ")            
           .append((null != params) ? params.toString() : "null")
           .append("}");            
        return out.toString(); 
    }

    /**
     * EffectDescriptors with the same data are considered equal
     */
    public boolean equals(Object obj) {
        
        if (!(obj instanceof EffectDescriptor)) {
          return false;
        }
     
        if (this == obj) {
          return true;
        }
        
        EffectDescriptor other = (EffectDescriptor)obj;            
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(this.name, other.name)
          .append(this.direction, other.direction)
          .append(this.duration, other.duration)
          .append(this.fps, other.fps)
          .append(this.params, other.params);
        return eb.isEquals();
    }

    /**
     * Overriden hashCode so as to maintain the general contract for the hashCode method, 
     * which states that equal objects must have equal hash codes.
     */
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(this.name)
           .append(this.direction)
           .append(this.duration)
           .append(this.fps)
           .append(this.params);
        return hcb.toHashCode();
    }

    /**
     * Parses EffectDescriptor from a string representation
     */
    public static EffectDescriptor parse(String effect) {
        //keyword may be valid on the begining, remember that
        boolean valid = MCSEffectStyleKeywords.getDefaultInstance().
            isValidKeyword(effect);
        EffectDescriptor desc = new EffectDescriptor();
        
        int paramsStartIdx = effect.indexOf('(');
        int paramsEndIdx = effect.lastIndexOf(')');
        
        String params = null;
        if (paramsStartIdx != -1 && paramsEndIdx > paramsStartIdx) {
            params = effect.substring(paramsStartIdx + 1, paramsEndIdx);
            effect = effect.substring(0, paramsStartIdx).trim();
        }        
        
        int dirStartIdx = effect.indexOf('-');
        if (dirStartIdx != -1) {
            desc.setDirection(effect.substring(dirStartIdx + 1));
            effect = effect.substring(0, dirStartIdx).trim();
        }        
        desc.setName(effect);
        
        if (!valid && !MCSEffectStyleKeywords.getDefaultInstance().
                isValidKeyword(effect)){
            //the keyward was not valid before and after transformatin
            logger.warn("unexpected-mcs-effect-style-type",effect);
        }

        // Params need additional parsing
        if (!StringUtils.isEmpty(params)) {
            EffectParameters.Parser parser = EffectParameters.getParser(desc.getName());
            if (null != parser) {
                desc.setParams(parser.parse(params));
            } // else ignore parameters. Maybe we should throw an exception here?
        }        
        return desc;
    }
}
