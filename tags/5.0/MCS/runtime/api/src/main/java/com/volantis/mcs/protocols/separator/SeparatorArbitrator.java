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

package com.volantis.mcs.protocols.separator;

/**
 * Responsible for arbitrating between separators.
 *
 *
 * <p>Implementations MUST NOT maintain, or rely on any historical information
 * about how the manager is used.</p>
 *
 * @mock.generate
 */
public interface SeparatorArbitrator {

    /**
     * Choose the separator renderer to use when there is a possible conflict.
     *
     * <p>Although either of the separators may be null, it is not permitted
     * for them both to be so.</p>
     *
     * <p>If this method is supplied with a single separator then it simply
     * decides whether a renderer should be used or not. If supplied with two
     * then it can either choose one of the renderers, no renderer or create
     * some sort of compromise renderer.</p>
     *
     * @param manager The manager requesting arbitration.
     * @param first The first separator, may be null.
     * @param second The second separator, may be null.
     * @param decision Updated to reflect the arbitrator's decision.
     */
/*
    public void betweenSeparators(SeparatorManager manager,
                                  SeparatorRenderer first,
                                  SeparatorRenderer second,
                                  ArbitratorDecision decision);
*/

    /**
     * Choose the separator renderer to use after the content.
     * @param manager The manager requesting arbitration.
     * @param content The content that precedes the renderer. If null then this
     * indicates that no content has been rendered before the separator.
     * @param separator The renderer, may be null.
     * @param decision Updated to reflect the arbitrator's decision.
     */
/*
    public void afterContent(SeparatorManager manager,
                             SeparatedContent content,
                             SeparatorRenderer separator,
                             ArbitratorDecision decision);
*/

    /**
     * Choose the separator renderer to use before the content.
     *
     * <p>This method must never defer the separator, it must always either use
     * it or ignore it.</p>
     *
     * @param manager The manager requesting arbitration.
     * @param separator The renderer, may be null.
     * @param content The content that will follow the renderer. If null then
     * this indicates that no content will be rendered after the separator.
     * @param decision Updated to reflect the arbitrator's decision.
     */
/*
    public void beforeContent(SeparatorManager manager,
                              SeparatorRenderer separator,
                              SeparatedContent content,
                              ArbitratorDecision decision);
*/

    /**
     * The arbitrator must decide what the manager should do with the
     * deferred separator.
     *
     * @param manager
     * @param decision
     */
    public void decide(SeparatorManager manager,
                       ArbitratorDecision decision);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-May-04	4164/2	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 08-Apr-04	3610/3	pduffin	VBM:2004032509 Added separator API and default implementation

 ===========================================================================
*/
