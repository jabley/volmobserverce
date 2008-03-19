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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.css.emulator.renderer;

/**
 * This class is responsible for providing a wrapper for
 * {@link StyleEmulationPropertyRenderer} instances that wish to be
 * identified as renderers that only use elements to achieve
 * emulation of style properties.
 */
public class ElementOnlyStyleEmulationPropertyRenderer 
    extends AbstractIdentifiableStyleEmulationPropertyRenderer {

    /**
     * Creates a new instance with the supplied parameters.
     *
     * @param renderer the style emulation renderer to be wrapped
     */
    public ElementOnlyStyleEmulationPropertyRenderer(
            StyleEmulationPropertyRenderer renderer) {
        super(renderer);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 17-Nov-05	10251/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 ===========================================================================
*/
