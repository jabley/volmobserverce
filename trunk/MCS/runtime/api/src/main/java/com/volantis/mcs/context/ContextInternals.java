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
 * $Header: /src/voyager/com/volantis/mcs/context/ContextInternals.java,v 1.4 2003/02/11 12:50:17 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Nov-01    Paul            VBM:2001112202 - Created
 * 29-Nov-01    Paul            VBM:2001112906 - Added methods to get the
 *                              EnvironmentContext from the
 *                              MarinerRequestContext.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 17-Oct-02    Ian             VBM:2002091806 - Added methods to set and get
 *                              the ApplicationContext.
 * 02-Dec-02    Byron           VBM:2002103009 - Added method constructURL().
 * 29-Jan-03    Byron           VBM:2003012712 - Modified constructURL
 *                              signature. 
 * 11-Feb-03    Ian             VBM:2003020607 - Ported changes from Metis for
 *                              ApplicationContext.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.context;

import com.volantis.mcs.application.MarinerApplication;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.utilities.PreservedArea;

/**
 * This class provides methods which allow other packages in mariner to
 * access context package methods. It is not part of the public API but has
 * to be a public class to allow other mariner packages to use its methods.
 */
public class ContextInternals {
    
    /**
     * Store the specified <code>MarinerPageContext</code> in the specified
     * <code>MarinerRequestContext</code>.
     * @param requestContext The <code>MarinerRequestContext</code>.
     * @param marinerPageContext The <code>MarinerPageContext</code>.
     */
    public static
    void setMarinerPageContext (MarinerRequestContext requestContext,
            MarinerPageContext marinerPageContext) {
        requestContext.setMarinerPageContext (marinerPageContext);
    }
    
    /**
     * Get the <code>MarinerPageContext</code> from the specified
     * <code>MarinerRequestContext</code>.
     * @param requestContext The <code>MarinerRequestContext</code>.
     * @return The <code>MarinerPageContext</code>.
     */
    public static
    MarinerPageContext getMarinerPageContext (MarinerRequestContext requestContext) {
        return requestContext.getMarinerPageContext ();
    }
    
    /**
     * Store the specified <code>EnvironmentContext</code> in the specified
     * <code>MarinerRequestContext</code>.
     * @param requestContext The <code>MarinerRequestContext</code>.
     * @param environmentContext The <code>EnvironmentContext</code>.
     */
    public static
    void setEnvironmentContext (MarinerRequestContext requestContext,
            EnvironmentContext environmentContext) {
        requestContext.setEnvironmentContext (environmentContext);
    }
    
    /**
     * Get the <code>EnvironmentContext</code> from the specified
     * <code>MarinerRequestContext</code>.
     * @param requestContext The <code>MarinerRequestContext</code>.
     * @return The <code>EnvironmentContext</code>.
     */
    public static
    EnvironmentContext getEnvironmentContext (MarinerRequestContext requestContext) {
        return requestContext.getEnvironmentContext ();
    }
    
    /**
     * Store the specified <code>ApplicationContext</code> in the specified
     * <code>MarinerRequestContext</code>.
     * @param requestContext The <code>MarinerRequestContext</code>.
     * @param applicationContext The <code>ApplicationContext</code>.
     */
    public static
    void setApplicationContext (MarinerRequestContext requestContext,
            ApplicationContext applicationContext) {
        requestContext.setApplicationContext (applicationContext);
    }
    
    /**
     * Store the specified <code>MarinerApplication</code> in the specified
     * <code>MarinerRequestContext</code>.
     * @param requestContext The <code>MarinerRequestContext</code> to store the
     *        <code>MarinerApplication</code> in.
     * @param marinerApplication The <code>MarinerApplication</code> to store in the
     *        <code>MarinerRequestContext</code>.
     */
    public static void setMarinerApplication(MarinerRequestContext requestContext,
            MarinerApplication marinerApplication) {
        requestContext.setMarinerApplication(marinerApplication);
    }
    
    /**
     * Get the <code>ApplicationContext</code> from the specified
     * <code>MarinerRequestContext</code>.
     * @param requestContext The <code>MarinerRequestContext</code>.
     * @return The <code>MarinerApplicationContext</code>.
     */
    public static
    ApplicationContext getApplicationContext (MarinerRequestContext requestContext) {
        return requestContext.getApplicationContext ();
    }
    
    /**
     * Return a url as a <code>String</code> that has been built up using the
     * marinerPageContext's constructURL method.
     *
     * @param requestContext the request context
     * @param url            the url to be processed
     * @return a url as a <code>String</code> that has been built up using the
     *         marinerPageContext's constructURL method.
     * @throws RepositoryException if a problem occurs building the URL
     * @todo better the constructURL method in MPC could be better elsewhere
     * @see MarinerPageContext#constructImageURL(String,PreservedArea)
     */
    public static String constructImageURL(MarinerRequestContext requestContext,
            String url) throws RepositoryException {
        return constructImageURL(requestContext,url,null);
    }
    /**
     * Return a url as a <code>String</code> that has been built up using the
     * marinerPageContext's constructURL method.
     *
     * @param requestContext the request context
     * @param url            the url to be processed
     * @param preservedArea the optional protected area of the asset
     * @return a url as a <code>String</code> that has been built up using the
     *         marinerPageContext's constructURL method.
     * @throws RepositoryException if a problem occurs building the URL
     * @todo better the constructURL method in MPC could be better elsewhere
     * @see MarinerPageContext#constructImageURL(String,PreservedArea)
     */    
    public static String constructImageURL(MarinerRequestContext requestContext,
            String url,PreservedArea preservedArea) throws RepositoryException {
        return requestContext.getMarinerPageContext().constructImageURL(url,preservedArea);
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Nov-05	10168/1	ianw	VBM:2005102504 port forward web clipping

 07-Nov-05	10170/1	ianw	VBM:2005102504 port forward web clipping

 04-Nov-05	9999/2	pszul	VBM:2005102504 preserver area implemented in ConvertibleImageAsset
 
 17-Jan-05	6693/1	allan	VBM:2005011403 Remove MPS specific image url parameters
 
 23-Dec-04	6518/3	tom	VBM:2004122001 Added remote repository pre loading and cache fulshing API's to MarinerApplication
 
 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build
 
 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build
 
 26-Sep-03	1454/2	philws	VBM:2003092401 Provide asset transcoder plugin API and configuration-selectable standard implementations
 
 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas
 
 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes
 
 23-Jul-03	807/1	geoff	VBM:2003071405 works and tested but no design review yet
 
 ===========================================================================
 */
