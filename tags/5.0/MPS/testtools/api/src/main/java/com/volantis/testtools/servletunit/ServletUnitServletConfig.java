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
package com.volantis.testtools.servletunit;
/********************************************************************************************************************
* $Id: ServletUnitServletConfig.java,v 1.1 2002/12/10 09:50:49 sumit Exp $
*
* Copyright (c) 2000-2002, Russell Gold
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
* documentation files (the "Software"), to deal in the Software without restriction, including without limitation
* the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
* to permit persons to whom the Software is furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all copies or substantial portions
* of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
* THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
* CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
* DEALINGS IN THE SOFTWARE.
*
*******************************************************************************************************************/
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;


/**
 * This class acts as a test environment for servlets.
 **/
class ServletUnitServletConfig implements ServletConfig {


    ServletUnitServletConfig( Servlet servlet, WebApplication application, Hashtable initParams ) {
        _name = servlet.getClass().getName();
        _initParameters = initParams;
        _context = new ServletUnitServletContext( application );
    }


//-------------------------------------------- ServletConfig methods ---------------------------------------------------


    /**
     * Returns the value of the specified init parameter, or null if no such init parameter is defined.
     **/
    public String getInitParameter( String name ) {
        return (String) _initParameters.get( name );
    }


    /**
     * Returns an enumeration over the names of the init parameters.
     **/
    public Enumeration getInitParameterNames() {
        return _initParameters.keys();
    }


    /**
     * Returns the current servlet context.
     **/
    public ServletContext getServletContext() {
        return _context;
    }


    /**
     * Returns the registered name of the servlet, or its class name if it is not registered.
     **/
    public java.lang.String getServletName() {
        return _name;
    }


//----------------------------------------------- private members ------------------------------------------------------


    private String _name;

    private final Hashtable _initParameters;

    private final ServletContext _context;

}