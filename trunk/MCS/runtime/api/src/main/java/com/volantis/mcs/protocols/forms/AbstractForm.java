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
package com.volantis.mcs.protocols.forms;

/**
 * Encapsulates data that is common to XDIME1 and XDIME2 form implementations.
 */
public interface AbstractForm {

    /**
     * Return the name of the form.
     *
     * @return String name of the form
     */
    String getName();

    /**
     * Determines if the form is fragmented (i.e. contains multiple form
     * fragments).
     *
     * @return true if the form is fragmented (i.e. contains multiple form
     * fragments) and false otherwise.
     */
    boolean isFragmented();

    /**
     * Return the fragment before the active one - may be null if there is no
     * previous fragment.
     *
     * @return
     */
    AbstractFormFragment getPreviousFormFragment(AbstractFormFragment current);

        /**
     * Return the fragment after the active fragment - may be null if there is
     * no next fragment.
     *
     * @return
     */
    AbstractFormFragment getNextFormFragment(AbstractFormFragment current);
}
