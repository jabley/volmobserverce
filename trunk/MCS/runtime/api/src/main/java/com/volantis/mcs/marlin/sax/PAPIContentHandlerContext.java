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
 * $Header: /src/voyager/com/volantis/mcs/marlin/sax/PAPIContentHandlerContext.java,v 1.1 2002/11/23 01:04:28 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Nov-02    Paul            VBM:2002112214 - Created to encapsulate the
 *                              shared state of the MarinerContentHandler and
 *                              the MarinerElementHandlers.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.marlin.sax;

import com.volantis.mcs.context.MarinerRequestContext;

import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIElement;

import java.util.ArrayList;
import java.util.Map;

import org.xml.sax.ContentHandler;

/**
 * This object contains the state of the content handler. It is separated
 * out from the ContentHandler so that it can be made available to
 * other internal classes without exposing internal details of this class.
 * <p>
 * The state could be stored in the MarlinContentHandler class and made
 * available through package private methods but that requires that all users
 * of this class belong to the same package which may not be what is desired.
 */
public class PAPIContentHandlerContext {


  /**
   * Stack of MarinerRequestContext instances.
   * <p>
   * This could be eliminated if the MarinerRequestContext had a method to
   * return the including MarinerRequestContext.
   */
  private ArrayList requestContextStack;

  /**
   * Stack of PAPIElement instances.
   * <p>
   * This could be eliminated if the PAPIElement has a method to set and get
   * the enclosing PAPIElement.
   */
  private ArrayList elementStack;

  /**
   * Stack of PAPIAttributes instances.
   * <p>
   * This could be eliminated if the PAPIAttributes has a method to set and get
   * the enclosing PAPIAttributes.
   */
  private ArrayList attributesStack;

  /**
   * Stack of MarlinElementHandler instances.
   */
  private ArrayList handlerStack;

  /**
   * The URL mapping rules.
   */
  private Map urlMappingRules;
  
  /**
   * Counter that determines whether we are within a nativemarkup element
   * or not. We need to use an int rather than a boolean as it is valid 
   * (although unlikely) to have a tag called nativemarkup inside a
   * nativemarkup element.
   */
  private int nativeMarkupDepth;

  /**
   * Create a new <code>PAPIContentHandlerContext</code>.
   */
  public PAPIContentHandlerContext () {
    requestContextStack = new ArrayList ();
    elementStack = new ArrayList ();
    attributesStack = new ArrayList ();
    handlerStack = new ArrayList ();
  }

  /**
   * Push the object onto the ArrayList.
   * @param list The ArrayList onto which the object should be added.
   * @param object The Object to add.
   */
  private void push (ArrayList list, Object object) {
    list.add (object);
  }

  /**
   * Pop the object from the ArrayList.
   * @param list The ArrayList from which the object should be removed.
   * @return The removed Object.
   */
  private Object pop (ArrayList list) {
    return list.remove (list.size () - 1);
  }

  /**
   * Peek at the object on the ArrayList.
   * @param list The ArrayList whose last object should be returned.
   * @return The last Object.
   */
  private Object peek(ArrayList list) {
      Object result = null;

      if (list.size() != 0) {
         result = list.get(list.size() - 1);
      }

      return result;
  }

  /**
   * Return the current MarinerRequestContext, this is the one on the top of
   * the stack of MarinerRequestContexts.
   * @return The current MarinerRequestContext.
   */
  public MarinerRequestContext getRequestContext () {
    return (MarinerRequestContext) peek (requestContextStack);
  }

  /**
   * Push the specified MarinerRequestContext onto the stack of
   * MarinerRequestContexts.
   * @param requestContext The MarinerRequestContext to push onto the stack.
   */
  public void pushRequestContext (MarinerRequestContext requestContext) {
    push (requestContextStack, requestContext);
  }

  /**
   * Pop the current MarinerRequestContext off the stack of
   * MarinerRequestContexts.
   * @return The popped MarinerRequestContext.
   */
  public MarinerRequestContext popRequestContext () {
    return (MarinerRequestContext) pop (requestContextStack);
  }

  /**
   * Push the specified ElementStackEntry onto the stack of
   * ElementStackEntries.
   * @param requestContext The ElementStackEntry to push onto the stack.
   */
  public void pushElementEntry (ElementStackEntry entry) {
    push (handlerStack, entry.handler);
    push (elementStack, entry.element);
    push (attributesStack, entry.attributes);
  }

  /**
   * Pop the current ElementStackEntry off the stack of
   * ElementStackEntries.
   * @return The popped ElementStackEntry.
   */
  public void popElementEntry (ElementStackEntry entry) {
    entry.handler = (MarlinElementHandler) pop (handlerStack);
    entry.element = (PAPIElement) pop (elementStack);
    entry.attributes = (PAPIAttributes) pop (attributesStack);
  }

  /**
   * Return the current ElementStackEntry, this is the one on the top of
   * the stack of ElementStackEntries.
   * @return The current ElementStackEntry.
   */
  public void getElementEntry (ElementStackEntry entry) {
    entry.handler = (MarlinElementHandler) peek (handlerStack);
    entry.element = (PAPIElement) peek (elementStack);
    entry.attributes = (PAPIAttributes) peek (attributesStack);
  }

  /**
   * Set the url mapping rules.
   * <p>FOR INTERNAL USE ONLY
   * @param urlMappingRules The map of url mapping rules.
   */
  public void setURLMappingRules (Map urlMappingRules) {
    this.urlMappingRules = urlMappingRules;
  }

  /**
   * Get the url mapping rules.
   * <p>FOR INTERNAL USE ONLY
   * @return The map of url mapping rules.
   */
  public Map getURLMappingRules () {
    return urlMappingRules;
  }
  
    /**
     * Get the native markup counter.
     * 
     * @return The native markup counter
     */
    public int getNativeMarkupDepth() {
        return nativeMarkupDepth;
    }
    
    /**
     * Set the native markup counter.
     * @param i The count
     */
    public void setNativeMarkupDepth(int i) {
        nativeMarkupDepth = i;
    }
    
    /**
     * Get the native markup counter.
     * 
     * @return The native markup counter
     */
    public void incrementNativeMarkupDepth() {
        nativeMarkupDepth++;
    }
    
    /**
     * Set the native markup counter.
     * @param i The count
     */
    public void decrementNativeMarkupDepth() {
        nativeMarkupDepth--;
    }

}

/*
 * Local variables:
 * c-basic-offset: 2
 * end:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Aug-03	1111/2	chrisw	VBM:2003081306 Move fields in AbstractMarlinContentHandler to MarlinContentHandlerContext

 30-Jun-03	552/4	philws	VBM:2003062507 Fix merge problems

 30-Jun-03	552/1	philws	VBM:2003062507 Provide JSP and XML variants of the vt:usePipeline and vt:include markup

 23-Jun-03	459/3	mat	VBM:2003061910 Changed marlin-canvas-schema to 2003061910

 23-Jun-03	459/1	mat	VBM:2003061910 Change getContentWriter() to return correct nativeWriter for Native markup elements

 ===========================================================================
*/
