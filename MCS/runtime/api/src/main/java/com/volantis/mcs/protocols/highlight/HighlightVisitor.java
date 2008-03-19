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

package com.volantis.mcs.protocols.highlight;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.WalkingDOMVisitorStub;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.styling.Styles;

import java.util.HashMap;

/**
 * Implementation of widget highlight with DOM Visitor template 
 */

public final class HighlightVisitor extends WalkingDOMVisitorStub {

   private HashMap ruleSet = null;
   private DOMProtocol protocol = null;
   private Document document = null;
    
   protected static final HighlightInsertRule INSERT = new HighlightInsertRule();
   
   /**
    * Create a visitor loaded with the supplied ruleset.
    * 
    * @param ruleSet rule set used to transform the DOM. Assumed not to be null
    * @param protocol - needed for getting url for javascript files
    * 
    */   
   public HighlightVisitor(HashMap ruleSet, DOMProtocol protocol) {
        this.ruleSet = ruleSet;
        this.protocol = protocol;
   }
	
   /**
    * Check the given element, if it contains a mcs-effect-style equal "highlight" 
    * look up the defined rule and call the transform method on it.
    *
    * @param element current element
    */
   
   public void visit(Element element) {
	   
       Styles styles = element.getStyles();
       if(styles != null) {
           StyleValue effectTypeValue = styles.getPropertyValues().getComputedValue(StylePropertyDetails.MCS_EFFECT_STYLE);
           if(effectTypeValue != null && effectTypeValue.getStandardCSS().equals(
                   StyleKeywords.HIGHLIGHT.getName())) {
               
        		HighlightRule rule = (HighlightRule)ruleSet.get(element.getName());
    
                if (rule != null) {
                    try {
                        rule.transform(element, this.protocol, this.document);
                    } catch (ProtocolException e) {
                        throw new IllegalStateException("Protocol not applicable for highligh widget");                        
                    }
                }					
            }
        } 
    }

    // Javadoc inherited.    
	public void visit(Document document) {
        this.document = document;
	}
}
