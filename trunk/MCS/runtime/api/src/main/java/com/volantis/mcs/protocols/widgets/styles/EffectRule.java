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

import com.volantis.mcs.protocols.widgets.JavaScriptStringFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Encapsulates a single effect repeater rule 
 */
public class EffectRule {
    
    private EffectDescriptor effectDesc = null;
    private String repetitions = null;
    private String timeInSeconds = null; 

    protected EffectRule() {
        this.effectDesc = new EffectDescriptor();
    }
        
    public EffectRule(EffectDescriptor effectDesc, String repetitions, String timeInSeconds) {
        this.effectDesc = effectDesc;
        this.repetitions = repetitions;
        this.timeInSeconds = timeInSeconds;
    }
    
    public EffectDescriptor getEffectDesc() {
        return effectDesc;
    }

    public void setEffectDesc(EffectDescriptor effectDesc) {
        this.effectDesc = effectDesc;
    }
    
    public String getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(String repetitions) {
        this.repetitions = repetitions;
    }

    public String getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(String timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public void setDuration(double duration) {
        effectDesc.setDuration(duration);
    }

    public void setFps(double fps) {
        effectDesc.setFps(fps);
    }

    /**
     * Generates JavaScript representations of this class
     */    
    public String toScriptConstructor() {
        JavaScriptStringFactory jsf = JavaScriptStringFactory.getInstance(); 
        StringBuffer ctor = new StringBuffer("new Widget.EffectRule(");                
        ctor.append(effectDesc.toScriptConstructor()).append(", ")
            .append(jsf.createJavaScriptString(repetitions)).append(", ")
            .append(jsf.createJavaScriptString(timeInSeconds))
            .append(")");
        return ctor.toString(); 
    }

    public String toString() {
        JavaScriptStringFactory jsf = JavaScriptStringFactory.getInstance(); 
        StringBuffer out = new StringBuffer("{");                
        out.append(effectDesc.toString()).append(", ")
           .append(jsf.createJavaScriptString(repetitions)).append(", ")
           .append(jsf.createJavaScriptString(timeInSeconds))
           .append("}");
        return out.toString(); 
    }

    /**
     * EffectRules with the same data are considered equal
     */
    public boolean equals(Object obj) {
        
        if (!(obj instanceof EffectRule)) {
          return false;
        }
     
        if (this == obj) {
          return true;
        }
        
        EffectRule other = (EffectRule)obj;            
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(this.effectDesc, other.effectDesc)
          .append(this.repetitions, other.repetitions)
          .append(this.timeInSeconds, other.timeInSeconds);
        return eb.isEquals();
    }

    /**
     * Overriden hashCode so as to maintain the general contract for the hashCode method, 
     * which states that equal objects must have equal hash codes.
     */    
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(this.effectDesc)
           .append(this.repetitions)
           .append(this.timeInSeconds);
        return hcb.toHashCode();
    }

    /**
     * Parses an effect rule exporessed as string into EffectRule class
     */
    public static EffectRule parse(String effectRule) {
        String effect = null;
        String number = null;
        String repetitions = "none";
        String timeInSeconds = "none";

        // Find the first space after the last ')'. It separates 'effect' part
        // from 'number' part of the rule 
        int spaceIndex = effectRule.indexOf(' ', effectRule.lastIndexOf(')'));
        if(spaceIndex != -1) {
            effect = effectRule.substring(0, spaceIndex).trim();
            number = effectRule.substring(spaceIndex + 1).trim();
        } else {
            effect = effectRule.trim();
        }

        if (!StringUtils.isEmpty(number)) {
            // TODO: this is a BUG. We are supposed to support all time units
            // not just seconds. Maybe reuse TimeUnitConverter to convert whatever
            // units the end user used, to seconds.
            if(number.endsWith("s")) {
                // convert time to seconds
                timeInSeconds = number.substring(0, number.length() - 1).trim();
            } else {
                repetitions = number;
            }
        }
        
        return new EffectRule(
                EffectDescriptor.parse(effect),
                repetitions,
                timeInSeconds);
    }    
}

