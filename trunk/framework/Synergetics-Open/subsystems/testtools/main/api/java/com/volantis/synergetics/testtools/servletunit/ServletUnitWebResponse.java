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
package com.volantis.synergetics.testtools.servletunit;
/********************************************************************************************************************
 * $Id: ServletUnitWebResponse.java,v 1.1 2002/11/08 12:29:47 sfound Exp $
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
import com.meterware.httpunit.WebResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;


/**
 * A response from to a request from the simulated servlet environment.
 **/
class ServletUnitWebResponse extends WebResponse {

    /**
     * Constructs a response object from a servlet response.
     * @param target the target frame on which the response will be displayed
     * @param url the url from which the response was received
     * @param response the response populated by the servlet
     **/
    ServletUnitWebResponse( ServletUnitClient client, String target, URL url, HttpServletResponse response ) throws IOException {
        super( client, target, url );
        _response = (ServletUnitHttpResponse) response;
        defineRawInputStream( new ByteArrayInputStream( _response.getContents() ) );
    }


    /**
     * Returns the response code associated with this response.
     **/
    public int getResponseCode() {
        return _response.getStatus();
    }


    /**
     * Returns the response message associated with this response.
     **/
    public String getResponseMessage() {
        return _response.getMessage();
    }


    public String[] getHeaderFieldNames() {
        return _response.getHeaderFieldNames();
    }


    /**
     * Returns the value for the specified header field. If no such field is defined, will return null.
     **/
    public String getHeaderField( String fieldName ) {
        return _response.getHeaderField( fieldName );
    }


    public String[] getHeaderFields( String fieldName ) {
        return _response.getHeaderFields( fieldName );
    }


    public String toString() {
        return "[ _response = " + _response + "]";
    }


//-------------------------------------------- private members ------------------------------------------------


    private ServletUnitHttpResponse _response;

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
