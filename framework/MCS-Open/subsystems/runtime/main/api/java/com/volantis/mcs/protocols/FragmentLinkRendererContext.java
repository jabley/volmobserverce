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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;



/**
 * The root of the tree of fragment link renderer contexts.
 * <p>
 * Fragment link renderers contexts are used by fragment links renderers as 
 * proxy classes to access the services (currently) provided by the Protocol.
 * <p> 
 * Hopefully in future those services will be separated out of the Protocols 
 * so that the renderers can use those services directly rather than through
 * the Protocols.
 * <p>
 * Fragment link renderer contexts will have an inheritance structure which is 
 * a sparse version of the existing Protocol inheritance structure.
 * <p>
 * This context in particular is a bit interesting in that it sometimes 
 * provides access to methods at a slightly higher level than they actually 
 * exist in the protocol. For example, <code>doAnchor</code> is only 
 * implemented at the DOM and String protocol level, and yet we have defined it 
 * here. This is possible since both protocols use the same patterns in the 
 * methods they implement, and thus the subclasses can implement them OK.
 * We only do this "temporarily" so we can share renderer instances between 
 * the StringProtocol MMLBasic and the other DOM Protocols. This will go away 
 * when StringProtocols go away.
 */ 
public abstract class FragmentLinkRendererContext {

    /**
     * The protocol that this context is proxying.
     */ 
    protected final VolantisProtocol protocol;

    /**
     * Protected constructor for this class, to be called by subclasses.
     * 
     * @param protocol
     */ 
    protected FragmentLinkRendererContext(VolantisProtocol protocol) {
        
        this.protocol = protocol;
        
    }

    /**
     * Proxy for the protocol's <code>doAnchor</code> method.
     * 
     * @see DOMProtocol#doAnchor
     */
    public abstract void doAnchor(OutputBuffer outputBuffer, 
            AnchorAttributes attributes) throws ProtocolException;

    /**
     * Proxy for the protocol's <code>doLineBreak</code> method.
     * 
     * @see DOMProtocol#doLineBreak
     */ 
    public abstract void doLineBreak(OutputBuffer outputBuffer, 
            LineBreakAttributes attributes) throws ProtocolException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 17-Sep-03	1412/5	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 ===========================================================================
*/
