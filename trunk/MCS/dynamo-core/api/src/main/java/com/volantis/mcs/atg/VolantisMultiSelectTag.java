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
 * $Header: /src/voyager/com/volantis/mcs/atg/VolantisMultiSelectTag.java,v 1.6 2003/03/20 15:15:29 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 *		Steve		VBM 2001090306 - Initial Release
 * 06-Dec-01    Mat             VBM:2001113002 - Removed call to 
 *                              MarinerPageContext.abort(), as 
 *                              it no longer exists.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 31-Jan-02    Paul            VBM:2001122105 - Fixed compile error in
 *                              logging.
 * 13-Mar-02    Paul            VBM:2002030104 - Removed unnecessary and
 *                              invalid import of the tags package.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.atg;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.XFMultipleSelectAttributes;
import com.volantis.mcs.papi.XFTextInputAttributes;
import com.volantis.mcs.papi.XMLWriter;
import com.volantis.mcs.servlet.MarinerServletRequestContext;
import com.volantis.synergetics.log.LogDispatcher;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Properties;

/**
 *
 * @author  steve
 */
public class VolantisMultiSelectTag extends atg.taglib.dspjsp.SelectTag
{
    
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(VolantisMultiSelectTag.class);

    private XFMultipleSelectAttributes selectAttributes;
    private String vname;
    private MarinerServletRequestContext requestContext;
    
    /** Creates new VolantisInputTag */
    public VolantisMultiSelectTag() {
        super();
        selectAttributes = new XFMultipleSelectAttributes();
    }

    public void setPageContext( PageContext context )
    {
        super.setPageContext( context );
        requestContext = null;
    }
    
    private MarinerServletRequestContext getMarinerServletRequestContext() {
        if( requestContext == null) {
            ServletRequest request = getPageContext().getRequest();
            requestContext = (MarinerServletRequestContext)
                    MarinerServletRequestContext.getCurrent(request);
        }
        return requestContext;
    }

    public void callSetter( String attribute, String value )
    {
        String method = "setInherited" + attribute.substring(0,1).toUpperCase() +
        attribute.substring(1, attribute.length());
        if(logger.isDebugEnabled()){
            logger.debug ("calling " + method + "( " + value + ")" );
        }
        
        Class params[] = new Class[1];
        params[0] = value.getClass();
        try {
            Method m = this.getClass().getMethod(method, params);
            Object args[] = new Object[1];
            args[0] = value;
            m.invoke(this, args);
        }
        catch (NoSuchMethodException nsm) 
        {
            logger.warn("method-not-found", new Object[]{method});
        }
        catch (IllegalAccessException ia) 
        {
            logger.warn("unexpected-illegal-access-exception", ia);
        }
        catch (InvocationTargetException it) 
        {
            logger.warn("unexpected-invocation-target-exception", it);
        }
    }

    public void renderTag(java.lang.String pTagName,Properties pAttributes) throws IOException 
    {
        if(logger.isDebugEnabled()){
            logger.debug ("VolantisSelectTag.renderTag( " + pTagName + " )" );
        }

        if( pTagName.equals("input") )
        {
            XFTextInputAttributes inputAttributes = new XFTextInputAttributes();
            
            try {
                inputAttributes.setName( pAttributes.getProperty( "name" ) );
                inputAttributes.setType( pAttributes.getProperty( "type", "hidden" ) );
                inputAttributes.setInitial( pAttributes.getProperty( "value" ) );
                VolantisInputTagRenderer.renderTag(
                        getMarinerServletRequestContext(), getPageContext(),
                        pTagName, vname, inputAttributes );
            }
            catch (Exception ex) 
            {
                // Had a problem so log it and throw a JspTagException to report it
                abort(ex);
                throw(new IOException("Exception caused by previously logged error"));
            }

        }    
    }
    
    public void renderTagAttributes(Properties pAttributes) throws IOException 
    {
        if(logger.isDebugEnabled()){
            logger.debug ("VolantisSelectTag.renderTagAttributes()" );
        }
        
        Enumeration e = pAttributes.propertyNames();
        while(e.hasMoreElements()) {
            String property = (String)e.nextElement();
            String value = pAttributes.getProperty(property);
            if(logger.isDebugEnabled()){
                logger.debug ("Property " + property + " = " + value);
            }
        }
        
    }
    
    public void renderTagEnd(java.lang.String pTagName) throws IOException 
    {
        if(logger.isDebugEnabled()) {
            logger.debug ("VolantisSelectTag.renderTagAttributes( " + pTagName + " )" );
        }
        if( pTagName.equals( "select" ) ) {
            try {
                XMLWriter writer = new XMLWriter(getPageContext().getOut());
                writer.closeElement(selectAttributes);
            }
            catch (Exception ex) {
                // Had a problem so logger.debug it and throw a JspTagException to report it
                abort(ex);
                throw(new IOException("Error rendering Select tag"));
            }
        }
    }
    
    public void renderTagStart(java.lang.String pTagName,Properties pAttributes) throws IOException 
    {
        if(logger.isDebugEnabled()){
            logger.debug ("VolantisSelectTag.renderTagStart( " + pTagName + " )" );
        }
        if( pTagName.equals( "select" ) )
        {
            Enumeration en = pAttributes.propertyNames();
            while(en.hasMoreElements()) 
            {
                String property = (String)en.nextElement();
                String value = pAttributes.getProperty(property);
                callSetter(property, value);
            }
            
            try 
            {
                HttpSession session = getPageContext().getSession();
        
                if(logger.isDebugEnabled()){
                    logger.debug ( "Select. Setting session attribute _V" + vname + " to " + selectAttributes.getName() );
                }   
                session.setAttribute( new String( "_V"+vname ), new String( selectAttributes.getName() ) );
                selectAttributes.setName( "i_" + vname );

                XMLWriter writer = new XMLWriter(getPageContext().getOut());
                writer.openElement(selectAttributes);
            }

            catch (Exception ex) {
                // Had a problem so logger.debug it and throw a JspTagException to report it
                abort(ex);
                throw(new IOException("Error rendering Select tag"));
            }
        }
        selectAttributes.reset();
    }
    
    /**
     * This method is called by the server when the tag has been finished with.
     * As some servers (e.g ATG) re-use the tags, the attributes need to be reset.
     *
     */
    public void release() 
    {
        if(logger.isDebugEnabled()){
            logger.debug ( "Releasing VolantisSelectTag" );
        }
        selectAttributes.reset();
        super.release();
    }
    
    private void abort(Exception e) 
    {
        logger.error ("unexpected-exception", e);
    }

    public void setStyleClass(String s) 
    {
        selectAttributes.setStyleClass( s );
    }
    
    public String getStyleClass() 
    {
        return selectAttributes.getStyleClass() ;
    }
    
    public void setInheritedId( String id )
    {
        selectAttributes.setId( id );
    }
    
    public void setVname( String s )
    {
        if(logger.isDebugEnabled()){
            logger.debug ( "*** vname for Select tag is " + s + " ***" );
        }
        vname = new String( s );
    }
    
    public String getVname()
    {
        return vname;
    }
    
    public void setCaptionPane(String pane) 
    {
        selectAttributes.setCaptionPane( pane );
    }
    
    public String getCaptionPane()
    {
        return selectAttributes.getCaptionPane();
    }
    
    public void setEntryPane(String pane) 
    {
        selectAttributes.setEntryPane( pane );
    }
    
    public String getEntryPane()
    {
        return selectAttributes.getEntryPane();
    }
    
    public void setCaption( String s )
    {
        selectAttributes.setCaption( s );
    }
    
    public String getCaption()
    {
        return selectAttributes.getCaption();
    }

    public void setActive( String s )
    {
        selectAttributes.setActive( s );
    }
    
    public String getActive()
    {
        return selectAttributes.getActive();
    }
    
    public void setErrmsg( String s )
    {
        selectAttributes.setErrmsg( s );
    }
    
    public String getErrmsg()
    {
        return selectAttributes.getErrmsg();
    }
    
    public void setHelp( String s )
    {
        selectAttributes.setHelp( s );
    }
    
    public String getHelp()
    {
        return selectAttributes.getHelp();
    }
    
    public void setInitial( String s )
    {
        selectAttributes.setInitial( s );
    }
    
    public String getInitial()
    {
        return selectAttributes.getInitial();
    }
    
    public void setPrompt( String s )
    {
        selectAttributes.setPrompt( s );
    }
    
    public String getPrompt()
    {
        return selectAttributes.getPrompt();
    }
    
    public void setShortcut( String s )
    {
        selectAttributes.setShortcut( s );
    }
    
    public String getShortcut()
    {
        return selectAttributes.getShortcut();
    }
    
    public void setInheritedMultiple( String s )
    {
    }
    
    public void setInheritedName( String s )
    {
        selectAttributes.setName( s );
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/7	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/5	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/3	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
