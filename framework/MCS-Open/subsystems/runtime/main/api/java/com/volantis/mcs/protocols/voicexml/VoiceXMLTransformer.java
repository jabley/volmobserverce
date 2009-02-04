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
 * $Header: /src/voyager/com/volantis/mcs/protocols/voicexml/VoiceXMLTransformer.java,v 1.1 2003/04/28 16:14:55 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 23-Apr-03    Allan           VBM:2003042302 - Created. A DOMTransformer for 
 *                              VoiceXML. 
 * 24-Apr-03    Allan           VBM:2003042401 - Made into a 
 *                              VisitorBasedDOMTransformer. 
 * 24-Apr-03    Allan           VBM:2003042207 - Added TreeVisitor constructor 
 *                              to take the protocol. Added test for menu in 
 *                              visit(). Added promoteMenu() and 
 *                              createBlockGotoElement. 
 * 25-Apr-03    Allan           VBM:2003042207 - Updated visit(Element..) so
 *                              that prompts inside menus are handled.
 * 28-Apr-03    Allan           VBM:2003042802 - Moved from protocols package. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.voicexml;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.protocols.AbstractTransformingVisitor;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMVisitorBasedTransformer;
import com.volantis.mcs.protocols.TransformingVisitor;

/**
 * The DOMTransformer for VoiceXML.
 */ 
public final class VoiceXMLTransformer extends DOMVisitorBasedTransformer {

    protected TransformingVisitor getDOMVisitor(DOMProtocol protocol) {
        return new TreeVisitor(protocol.getDOMFactory());
    }

    /**
     * The DOMVisitor for the VoiceXMLTransformer. 
     */ 
    private static final class TreeVisitor
            extends AbstractTransformingVisitor {

        private final DOMFactory factory;

        /**
         * A counter for generating unique ids
         */ 
        int idCount = 0;

        /**
         * Indicates whether the visitor is currently inside a prompt element.
         */
        private boolean insidePrompt;

        /**
         * Construct a new TreeVisitor that may make use of a factory.
         * @param factory
         */ 
        public TreeVisitor(DOMFactory factory) {
            this.factory = factory;
            insidePrompt = false;
        }

        /**
         * Visit the Element and promote if a menu and potentially set name
         * to null if a prompt
         * @param element The element being visited.
         * @return false to continue visiting; otherwise true.
         */
        public void visit(Element element) {
            String name = element.getName();

            boolean savedInsidePrompt = insidePrompt;

            if("menu".equals(name)) {
                promoteMenu(element);
                // we can no longer be inside a prompt
                insidePrompt = false;
            } else {
                if(!insidePrompt &&
                        ("prompt".equals(name) || "choice".equals(name))) {
                    insidePrompt = true;
                } else if(insidePrompt && "prompt".equals(name)) {
                    element.setName(null);
                }
            }

            element.forEachChild(this);

            insidePrompt = savedInsidePrompt;
        }

        /**
         * Promote a menu out of its enclosing form.
         * @param menu
         */ 
        private void promoteMenu(Element menu) {
            Element parent = menu.getParent();
            
            while(parent!=null && !"form".equals(parent.getName())) {
                menu.promote(true);
                parent = menu.getParent();
            }
            
            if("form".equals(parent.getName())) {
                // We need to add a goto to link to the menu we are about
                // to promote out of the form.
                String menuId = menu.getAttributeValue("id");
                if(menuId==null) {
                    // We need to generate a unique id.
                    menuId = generateUniqueId();
                    menu.setAttribute("id", menuId);
                }
                Element blockElement = createGotoBlockElement(menuId);
                blockElement.insertBefore(menu);
                
                // Now we remove all subsequent nodes since these cannot be
                // accessed due to the preceding menu.
                removeSubsequentNodes(menu);
                
                // Finally promote the menu out of the form.
                menu.promote(false); // false because we know there is a goto.
            }            
        }

        /**
         * Create a block element containing a goto element that has a link
         * based on the given String.
         * @param next The basis of next attribute of the goto.
         * @return The created block element.
         */ 
        private Element createGotoBlockElement(String next) {
            Element gotoElement = factory.createElement("goto");
            gotoElement.setAttribute("next", '#' + next);
            
            Element blockElement = factory.createElement("block");
            blockElement.addTail(gotoElement);
            
            return blockElement;
        }
        
        /**
         * Remove all the nodes (siblings) that come after the given element
         * and release them and their children from the pool.
         */
        private void removeSubsequentNodes(Element menu) {

            Node next = menu.getNext();
            while(next!=null) {
                Node nextToRemove = next;
                next = next.getNext();
                nextToRemove.remove();
            }
        }
        
        /**
         * Generate a unique id.
         */ 
        private String generateUniqueId() {
            String id = "VG_" + idCount;
            idCount++;
            return id;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9184/2	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 19-Aug-05	9289/1	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 ===========================================================================
*/
