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
 * $Header: /src/mps/testsuite/unit/com/volantis/mps/attachment/VolantisKicker.java,v 1.1 2002/12/09 15:05:11 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Dec-02    Sumit           VBM:2002112602 - Created to kick the bean into
 *                              life for unit tests
 * ----------------------------------------------------------------------------
 */
package com.volantis.mps.attachment;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.volantis.mcs.servlet.MarinerServletApplication;

/**
 * 
 */
public class VolantisKicker extends HttpServlet {
    MarinerServletApplication mpsTest;
    public void init(ServletConfig config) throws ServletException{
        super.init();
        mpsTest = MarinerServletApplication.getInstance(
                  config.getServletContext(),true);
    }
    public void doGet(HttpServletRequest req, HttpServletResponse res){
        res.setStatus(HttpServletResponse.SC_OK);
        try {
            res.flushBuffer();
        } catch(Exception e){
        }
    }
}
