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
package com.volantis.xml.expression.impl.jxpath.compiler;

import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.impl.jxpath.JXPathExpression;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.xml.expression.sequence.Sequence;
import our.apache.commons.jxpath.JXPathException;
import our.apache.commons.jxpath.ri.EvalContext;
import our.apache.commons.jxpath.ri.compiler.Expression;
import our.apache.commons.jxpath.ri.compiler.ExpressionList;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * This ExpressionList subclass allows us to handle a list of expressions
 */
public class JXPathExpressionList extends ExpressionList {

    /**
     * Factory for creating Expression related objects
     */
    private ExpressionFactory factory;  

    /**
     * Creates a new <code>JXPathExpressionList</code> instance
     * @param arguments the list of expressions
     * @param factory an ExpressionFactory instance
     */    
     public JXPathExpressionList(Expression[] arguments, ExpressionFactory factory) {
         super(arguments);
         this.factory = factory;
     }

     /**
      * Computes a list of expressions
      * @param context context to evaluate 
      * @return sequence of expressions
      */     
     public Object compute(EvalContext context) {
    	 Sequence result = Sequence.EMPTY;
    	 if (expList != null) {
    	     ArrayList array = flattenExpressions(context);
    	     ListIterator it = array.listIterator();
    	     ArrayList itemsArray = new ArrayList(array.size()*2);
    	     while (it.hasNext()) {
    		try { 
    		    Sequence seq = JXPathExpression.asValue(factory, it.next()).
							    getSequence();
    		    for (int j=0; j<seq.getLength(); j++) {
    			itemsArray.add(seq.getItem(j+1));
    	  	    }
    		} catch (Exception e) {
    		    throw new JXPathException("Cannot evaluate nested expression " + 
							                  this.toString());
    		}
    	     }
    	     Item[] items = (Item[]) itemsArray.toArray(new Item[] {});
    	     result = factory.createSequence(items);
    		 
    	 }
    	 return result;
     }
     
     /**
      * Computes a list of expressions
      * @param context context to evaluate 
      * @return sequence of expressions
      */      
     public Object computeValue(EvalContext context) {
    	 return compute(context);
     } 
     
     /**
      * Converts expressions to string
      * @param array array of expressions
      * @return string of expressions
      */
     private String convertToString(Expression[] array) {
    	 String result = "";
    	 if (array != null) {
	     result = "(";
    	     for (int i=0; i<array.length; i++) {
    		 if (array[i] instanceof JXPathExpressionList) {
    	   	     result += convertToString((
    			 	(JXPathExpressionList) array[i]).getExpressionList());
    		 }
    		 else {
    		     result += array[i].toString();
    		 }
    		 if (i < array.length) {
    		     result += ", ";
    		 }
    	    }
    	    result += ")";
    		 
    	 }
    	 return result;    	 
     }
     
     /**
      * Gets string of expressions
      * @return string of expressions
      */
     public String toString() {
    	 return convertToString(expList);
     }
}


