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
 * $Header: /src/voyager/com/volantis/mcs/protocols/dissection/ElementAnnotation.java,v 1.11 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Feb-02    Paul            VBM:2002021802 - Created. See class comment
 *                              for details.
 * 03-May-02    Paul            VBM:2002042203 - Wrapped all debug calls
 *                              which create a new String with a check to make
 *                              sure that debug is enabled.
 * 23-May-02    Paul            VBM:2002042202 - Used the ProtocolOutputter to
 *                              generate the open and close tags rather than
 *                              try and do it ourselves and removed the divide
 *                              hint code as that is all handled in
 *                              NodeAnnotation now.
 * 05-Jun-02    Adrian          VBM:2002021103 - Added method isKeeptogether..
 *                              ..Element and modified markSharNodesImpl such
 *                              that all nodes within a KEEPTOGETHER_ELEMENT
 *                              will refuse to fit in a shard if the whole
 *                              element cannot fit on the first attempt to add
 *                              the content.
 * 10-Jun-02    Adrian          VBM:2002021103 - Changed keeptogether to
 *                              keepTogether.
 * 14-Oct-02    Sumit           VBM:2002070803 - Changed generateTags to get
 *                              a RSB from the pool via the protocol
 * 20-Jan-03    Adrian          VBM:2003011605 - updated isKeepTogetherElement 
 *                              to only return true on the first call. added 
 *                              the member field checkedKeepTogether to track 
 *                              which is set to true after the first call to 
 *                              isKeepTogetherElement. removed all updates to 
 *                              markShardNodesImpl added under 2002021103. 
 * 06-Feb-03    Byron           VBM:2003020610 - Modified generateTags to add
 *                              the encoded url candidate to the package
 *                              resources.
 * 21-Feb-03    Phil W-S        VBM:2003022006 - Remove call to
 *                              PackageResources.addCandidateElementAssetURLs
 *                              from generateTags and inserted same call into
 *                              generateDissectedContents and
 *                              generateShardContentsImpl.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for  
 *                              ProtocolException where necessary.
 * 23-May-03    Mat             VBM:2003042907 - Changed instantiation of
 *                              ProtocolOutputter
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.dissection;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.dom.Attribute;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.dtd.DTD;
import com.volantis.mcs.dom.output.CharacterEncoder;
import com.volantis.mcs.dom.output.DocumentWriter;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.utilities.ReusableStringBuffer;
import com.volantis.mcs.utilities.ReusableStringWriter;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;

/**
 * This class is used to annotate elements in the dom tree which is being
 * dissected.
 */
public class ElementAnnotation
  extends NodeAnnotation
  implements DissectionConstants {

  /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(ElementAnnotation.class);

  protected Element element;

  /**
   * Determines whether the tags have been generated. If true then the
   * open and close tags have been generated, otherwise they have not been.
   */
  protected boolean generatedTags;

    /**
   * The open tag, this is generated when it is needed.
   */
  private ReusableStringBuffer openTag;

  /**
   * The close tag, this is generated at the same time as the open tag if
   * it is needed.
   */

  private ReusableStringBuffer closeTag;

  /**
   * Whether a dividehint is active
   */
  private boolean divideHintActive;

  /**
   * Flag which indicates whether the element is always empty.
   */
  private boolean alwaysEmpty;

  /**
   * Flag which indicates whether we have checked if this element is a
   * keepTogether element.  If true then the method isKeepTogether will always
   * return false.
   */ 
  private boolean checkedKeepTogether = false;
    
  public ElementAnnotation () {
  }

  /**
   * Implement this method to return the element.
   */
  protected Node getNode () {
    return element;
  }

  /**
   * Set the value of the element property.
   * @param element The new value of the element property.
   */
  public void setElement (Element element) {
    this.element = element;
  }

  /**
   * Get the value of the element property.
   * @return The value of the element property.
   */
  public Element getElement () {
    return element;
  }

  /**
   * Set the value of the always empty property.
   * @param alwaysEmpty The new value of the always empty property.
   */
  public void setAlwaysEmpty (boolean alwaysEmpty) {
    this.alwaysEmpty = alwaysEmpty;
  }

  /**
   * Get the value of the always empty property.
   * @return The value of the always empty property.
   */
  public boolean getAlwaysEmpty () {
    return alwaysEmpty;
  }

    /**
    * Generate the open and close tags from the element name and the attributes.
    */
    protected void generateTags () {
        CharacterEncoder encoder = protocol.getCharacterEncoder();

        openTag = new ReusableStringBuffer();
        ReusableStringWriter writer = new ReusableStringWriter(openTag);
        DTD dtd = protocol.getProtocolConfiguration().getDTD();
        DocumentWriter dw = dtd.createDocumentWriter(writer);
        try {
           if (dw.outputOpenTag(element, encoder)) {

                closeTag = new ReusableStringBuffer();
                writer.setBuffer(closeTag);

                dw.outputCloseTag(element);
            }
        } catch (IOException e) {
            // This should not happen.
          logger.error("tag-generation-error", e);
      }

      // Remember that the tags have been generated.
      generatedTags = true;
  }

  /**
   * Calculate the size of the open and close tags if any.
   * @return The size of the open and close tags.
   */
  protected int calculateTagsSize () {

    if (!generatedTags) {
      generateTags ();
    }

    int size = 0;
    if (openTag != null) {
      size += openTag.length ();
    }
    if (closeTag != null) {
      size += closeTag.length ();
    }

    return size;
  }

  /**
   * Is a divide hint being processed.
   */
  protected boolean isDivideHintActive () {
    return divideHintActive;
  }

  /**
   * Mark the divide hint as seen
   */
  protected void setDivideHintActive (boolean b) {
    divideHintActive = b;
  }

  /**
   * Implement the abstract calculateContentsSize method.
   */
  protected int calculateContentsSize () {
    int size = calculateTagsSize ();

    for (Node child = element.getHead (); child != null;
         child = child.getNext ()) {
      NodeAnnotation annotation = (NodeAnnotation) child.getObject ();

      size += annotation.getContentsSize ();
    }

    return size;
  }

  /**
   * Implement the abstract calculateFixedContentsSize method.
   */
  protected int calculateFixedContentsSize () {
    int size = calculateTagsSize ();

    for (Node child = element.getHead (); child != null;
         child = child.getNext ()) {
      NodeAnnotation annotation = (NodeAnnotation) child.getObject ();

      size += annotation.getFixedContentsSize ();
    }

    return size;
  }

  /**
   * Implement the abstract calculateOverheadSize method.
   */
  protected int calculateOverheadSize () throws ProtocolException {
    int size = calculateTagsSize ();

    return size;
  }

  /**
   * Override this method to handle divide hints properly.
   */
  protected boolean isDivideHintElement () {
    String name = element.getName ();
    return (DIVIDE_HINT_ELEMENT.equals (name));
  }

  /**
   * Check to see whether this node is actually a dissection hint element.
   */
  protected boolean isKeepTogetherElement () {
    boolean result = false;
    if (!checkedKeepTogether) {
        result = KEEPTOGETHER_ELEMENT.equals(element.getName());    
        checkedKeepTogether = true; 
    }
    return result;
  }

  /**
   * Implement the abstract markShardNodesImpl method.
   */
  protected int markShardNodesImpl (int shardNumber, int limit)
    throws ProtocolException {

    // Atomic nodes should not be broken down into anything smaller.
    if (atomic) {
      if (logger.isDebugEnabled ()) {
        logger.debug (element.getName () + " cannot be broken down");
      }
      return NODE_CANNOT_FIT;
    } else {
      if (logger.isDebugEnabled ()) {
        logger.debug ("Attempting to break " + this
                      + " into smaller pieces for shard "
                      + shardNumber);
      }
    }

    // Make sure that the overhead of using this node in a shard is less
    // than the limit.
    int overheadSize = getOverheadSize ();
    if (overheadSize > limit) {
      if (logger.isDebugEnabled ()) {
        logger.debug ("Overhead for " + this + " of " + overheadSize
                      + " is greater than the available space "
                      + limit + " left in shard " + shardNumber);
      }
      return NODE_CANNOT_FIT;
    }

    limit -= overheadSize;

    if (logger.isDebugEnabled ()) {
      logger.debug ("Overhead for " + this + " is " + overheadSize
                    + " space remaining is " + limit
                    + " in shard " + shardNumber);
    }

    int consumed = overheadSize;

    // Now check the children if any.
    Node child = element.getHead ();
    if (child != null) {
      for (; child != null; child = child.getNext ()) {

        NodeAnnotation annotation = (NodeAnnotation) child.getObject ();

        int result = annotation.markShardNodes (shardNumber, limit);
        switch (result) {
          case IGNORE_NODE:
            // Continue around.
            break;

          case NODE_CANNOT_FIT:
            // If some of the children fitted into the shard then return
            // SHARD_COMPLETE, otherwise return NODE_CANNOT_FIT.
            if (consumed == overheadSize) {
              // No children nodes have been added to the shard.
              if (logger.isDebugEnabled ()) {
                logger.debug ("No children of " + this
                              + " have been added to shard "
                              + shardNumber);
              }
              return NODE_CANNOT_FIT;
            } else {
              // Some children nodes have been added to the shard.
              if (logger.isDebugEnabled ()) {
                logger.debug ("Some children of " + this
                              + " have been added to shard "
                              + shardNumber);
              }
              return SHARD_COMPLETE;
            }

          case SHARD_COMPLETE:
            // The shard has been completed so return straight away.
            if (logger.isDebugEnabled ()) {
              logger.debug ("A descendant of this " + this
                            + " was the last node to be"
                            + " added to shard "
                            + shardNumber);
            }
            return SHARD_COMPLETE;

          default:
            // The child could fit in the shard and the result is the amount
            // of space that it consumed.

            // Keep track of how much space has been used by this node.
            consumed += result;

            // Reduce the amount of space available in the shard.
            limit -= result;
            break;
        }
      }

      // If we get here then it means that the last child has been
      // added to this shard.
      if (logger.isDebugEnabled ()) {
        logger.debug ("Last child of " + this
                      + " was added to shard "
                      + shardNumber);
      }

      return consumed;
    }

    // If we get here then it means that this is a leaf node and there is
    // not enough space for its contents.
    if (logger.isDebugEnabled ()) {
      logger.debug ("Leaf node " + this
                    + " will not fit in shard "
                    + shardNumber);
    }

    return NODE_CANNOT_FIT;
  }

  /**
   * Implement the abstract generateContents method.
   */
  public void generateContents (ReusableStringBuffer buffer) {

    if (!generatedTags) {
      generateTags ();
    }

    if (openTag != null) {
      buffer.append (openTag);
    }

    for (Node child = element.getHead (); child != null;
         child = child.getNext ()) {
      NodeAnnotation annotation = (NodeAnnotation) child.getObject ();

      annotation.generateContents (buffer);
    }

    if (closeTag != null) {
      buffer.append (closeTag);
    }
  }

  /**
   * Implement the abstract generateFixedContents method.
   */
  public void generateFixedContents (ReusableStringBuffer buffer) {

    if (!generatedTags) {
      generateTags ();
    }

    if (openTag != null) {
      buffer.append (openTag);
    }

    for (Node child = element.getHead (); child != null;
         child = child.getNext ()) {
      NodeAnnotation annotation = (NodeAnnotation) child.getObject ();

      annotation.generateFixedContents (buffer);
    }

    if (closeTag != null) {
      buffer.append (closeTag);
    }
  }

  /**
   * Implement the abstract generateDissectedContents method.
   */
  public void generateDissectedContents (ReusableStringBuffer buffer)
    throws ProtocolException {

    if (!generatedTags) {
      generateTags ();
    }

    if (openTag != null) {
      buffer.append (openTag);
    }

    // Add the candidate to the package resources candidate list if
    // there is a valid candidate to add.
    protocol.getProtocolConfiguration().addCandidateElementAssetURLs(
        element,
        ContextInternals.getApplicationContext(
            protocol.getMarinerPageContext().getRequestContext()).
        getPackageResources());

    for (Node child = element.getHead (); child != null;
         child = child.getNext ()) {
      NodeAnnotation annotation = (NodeAnnotation) child.getObject ();

      annotation.generateDissectedContents (buffer);
    }

    if (closeTag != null) {
      buffer.append (closeTag);
    }

  }

  /**
   * Implement the abstract generateShardContentsImpl method.
   */
  public boolean generateShardContentsImpl (ReusableStringBuffer buffer,
                                            int shardNumber,
                                            boolean all) {

    if (!generatedTags) {
      generateTags ();
    }

    if (openTag != null) {
      buffer.append (openTag);
    }

    // Add the candidate to the package resources candidate list if
    // there is a valid candidate to add.
    protocol.getProtocolConfiguration().addCandidateElementAssetURLs(
        element,
        ContextInternals.getApplicationContext(
            protocol.getMarinerPageContext().getRequestContext()).
        getPackageResources());

    boolean done = false;

    for (Node child = element.getHead (); child != null;
         child = child.getNext ()) {
      NodeAnnotation annotation = (NodeAnnotation) child.getObject ();

      done = annotation.generateShardContents (buffer, shardNumber, all);
      if (done) {
        break;
      }
    }

    if (closeTag != null) {
      buffer.append (closeTag);
    }

    return done;
  }

  /**
   * Implement the abstract generateDebugOutput method.
   */
  protected void generateDebugOutput (ReusableStringBuffer buffer,
                                      String indent) {

    String name = element.getName ();

    if (name != null) {
      Attribute attribute = element.getAttributes ();

      char [] attributeIndent
        = new char [indent.length () + name.length () + 2];

      for (int i = 0; i < attributeIndent.length; i += 1) {
        attributeIndent [i] = ' ';
      }

      buffer.append (indent).append ("<").append (name);

      if (attribute != null) {
        boolean first = true;
        for (; attribute != null; attribute = attribute.getNext ()) {

          if (!first) {
            buffer.append ("\n").append (attributeIndent);
          } else {
            buffer.append (" ");
            first = false;
          }

          buffer
            .append (attribute.getName ())
            .append ("=\"")
            .append (attribute.getValue ())
            .append ("\"");
        }
      }

      if (alwaysEmpty) {
        buffer.append ("/>\n");
        return;
      }

      buffer.append (">\n");
    }

    for (Node child = element.getHead (); child != null;
         child = child.getNext ()) {
      NodeAnnotation annotation = (NodeAnnotation) child.getObject ();

      annotation.generateDebugOutput (buffer, indent + "  ");
    }

    if (name != null) {
      buffer.append (indent).append ("</").append (name).append (">\n");
    }
  }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Nov-05	9708/2	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 08-Aug-05	9205/1	rgreenall	VBM:2005062107 Forward port of VBM:2005062107

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 25-Feb-04	2974/4	steve	VBM:2004020608 supermerged

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics
 17-Feb-04	2974/1	steve	VBM:2004020608 SGML Quote handling
 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)
 05-Feb-04	2794/1	steve	VBM:2004012613 HTML Quote handling

 ===========================================================================
*/
