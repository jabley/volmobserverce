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
 * $Header: /src/voyager/com/volantis/mcs/atg/VolantisDSPFormTag.java,v 1.9 2003/03/20 15:15:29 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 *              Steve           VBM 2001090306 - Initial Release
 * 31/10/01     Steve           Changed Layout to DeviceLayout
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 31-Jan-02    Paul            VBM:2001122105 - Fixed compile error in
 *                              logging.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.atg;

import com.volantis.mcs.papi.XFFormAttributes;
import com.volantis.mcs.papi.XFTextInputAttributes;
import com.volantis.mcs.papi.XMLWriter;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.servlet.MarinerServletRequestContext;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Properties;

/**
 *
 * @author  mat
 */
public class VolantisDSPFormTag extends atg.taglib.dspjsp.DSPFormTag {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(VolantisDSPFormTag.class);

    private XFFormAttributes formAttributes = null;
    //private XFFormElement formElement = null;

    private MarinerServletRequestContext requestContext;
    private String formName = null;


    /** Creates new VolantisDSPFormTag */
    public VolantisDSPFormTag() {
        super();
        formAttributes = new XFFormAttributes();
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
    
    public void release() 
    {
        if(logger.isDebugEnabled()){
            logger.debug ( "Releasing VolantisDSPFormTag" );
        }
        formAttributes.reset();
        super.release();
    }
    
    private void callSetter( String attribute, String value )
    {
        String method = "setInherited" + attribute.substring(0,1).toUpperCase() +
        attribute.substring(1, attribute.length());
        if(logger.isDebugEnabled()){
            logger.debug ("calling VolantisDSPFormTag."
                      + method + "( " + value + " )" );
        }
        
        Class params[] = new Class[1];
        params[0] = value.getClass();
        try {
            Method m = this.getClass().getMethod(method, params);
            Object args[] = new Object[1];
            args[0] = value;
            m.invoke(this, args);
        }
        catch (NoSuchMethodException nsm) {
            logger.warn("method-not-found", new Object[]{method});
        }
        catch (IllegalAccessException ia)  {
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
            logger.debug ("VolantisDSPFormTag.renderTag( " + pTagName + " )" );
        // The form always has a hidden input _DARGS field

        }
        if( pTagName.equals("input") )
       {
            XFTextInputAttributes inputAttributes = new XFTextInputAttributes();
            
            try {
                inputAttributes.setName( pAttributes.getProperty( "name", "_DARGS" ) );
                inputAttributes.setType( pAttributes.getProperty( "type", "hidden" ) );
                inputAttributes.setInitial( pAttributes.getProperty( "value" ) );
                VolantisInputTagRenderer.renderTag( getMarinerServletRequestContext(), getPageContext(), pTagName, "_DARGS", inputAttributes );
            }
            catch (Exception ex) 
            {
                // Had a problem so log it and throw a JspTagException to report it
                logger.error ("unexpected-exception", ex);
                throw(new IOException("Exception caused by previously logged error"));
            }
        }
    }
    
    public void renderTagStart( String pTagName, Properties pAttributes) throws IOException 
    {    
        if(logger.isDebugEnabled()){
            logger.debug ("VolantisDSPFormTag.renderTagStart( "
                      + pTagName + " )" );
        }

        if( pTagName.equals( "form" ) ) 
        {
            Enumeration e = pAttributes.propertyNames();
            while(e.hasMoreElements()) 
            {
                String property = (String)e.nextElement();
                String value = pAttributes.getProperty(property);
                callSetter(property, value);
            }

            try 
            {
                HttpSession session = getPageContext().getSession();
                HttpServletResponse response = (HttpServletResponse)getPageContext().getResponse();
                session.setAttribute( new String("_URL"), new String( formAttributes.getAction().toString() ) );
                
                formAttributes.setAction( response.encodeURL( "/Remap" ) );
                formAttributes.setName( formName );

                XMLWriter writer = new XMLWriter(getPageContext().getOut());
                writer.openElement(formAttributes);
            }
            catch (Exception ex) 
            {
                // Had a problem so log it and throw a JspTagException to report it
                logger.error ("unexpected-exception", ex);
                throw(new IOException("Exception caused by previously logged error"));
            }        
        }
    }

    public void renderTagEnd(java.lang.String pTagName) throws IOException 
    {
        if(logger.isDebugEnabled()){
            logger.debug ("VolantisDSPFormTag.renderTagEnd( " + pTagName + " )" );
        }
        if( "form".equals(pTagName) ) {
            try 
                {
                    if(logger.isDebugEnabled()){
                        logger.debug ("Form tag end output starts");
                    }

                    XMLWriter writer = new XMLWriter(getPageContext().getOut());
                    writer.closeElement(formAttributes);
                }
            catch (Exception ex) 
                {
                    // Had a problem so log it and throw a JspTagException to report it
                    logger.error ("unexpected-exception", ex);
                    throw(new IOException("Exception caused by previously logged error"));
                }
        }
    }

    public void setStyleClass(String styleClass) 
    {
        if(logger.isDebugEnabled()){
            logger.debug ("VolantisDSPFormTag.setStyleClass( " + styleClass + " )" );
        }
        formAttributes.setStyleClass(styleClass);
    }
    
    public String getStyleClass() 
    {
       return formAttributes.getStyleClass();
    }

    public void setInheritedId(String s) 
    {
       formAttributes.setId( s );
    }

    public void setInheritedAction(String s) 
    {
        formAttributes.setAction( s );
    }

    public void setInheritedMethod( String s ) 
    {
        formAttributes.setMethod(s);
    }

    public void setFormName(String s) 
    {
        if(logger.isDebugEnabled()){
            logger.debug ("VolantisDSPFormTag.setFormName( " + s + " )" );
        }
        formName = s;
    }
    
    public String getFormName()
    {
        return formName;
    }
    
    
    public void setHelp( String s )
    {
        if(logger.isDebugEnabled()){
            logger.debug ("VolantisDSPFormTag.setHelp( " + s + " )" );
        }
        formAttributes.setHelp( s );
    }
    
    public String getHelp()
    {
        return formAttributes.getHelp().toString();
    }
    
    public void setSegment( String s )
    {
        if(logger.isDebugEnabled()){
            logger.debug ("VolantisDSPFormTag.setSegment( " + s + " )" );
        }
        formAttributes.setSegment( s );
    }
    
    public String getSegment()
    {
        return formAttributes.getSegment().toString();
    }
    public void setPrompt( String s )
    {
        if(logger.isDebugEnabled()){
            logger.debug ("VolantisDSPFormTag.setPrompt( " + s + " )" );
        }
        formAttributes.setPrompt( s );
    }
    
    public String getPrompt()
    {
        return formAttributes.getPrompt().toString();
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
