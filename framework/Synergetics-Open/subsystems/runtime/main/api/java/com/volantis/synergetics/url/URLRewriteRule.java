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
package com.volantis.synergetics.url;

/**
 * This interaface allows a the rewriting of a url. URLs are represents as
 * strings to make them easy to manipulate.
 */
public interface URLRewriteRule {

    /**
     * Exceute this rule on a provided url.
     *
     * @param url        the url upon which to execute this rule.
     * @param contextURL the contextURL for the operation.
     * @return the result of executing this rule on the url.
     */
    String execute(String url, String contextURL);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Oct-04	320/2	doug	VBM:2004092306 Allowed URLRewriteRules to be added that are not bound to a prefix

 01-Oct-04	318/1	doug	VBM:2004092306 Allowed URLRewriteRules to be added that are not bound to a prefix

 ===========================================================================
*/
