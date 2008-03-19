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
package com.volantis.vdp.configuration.exception;

/**
 * @author Adam Kowalski adam.kowalski@experter.pl, based on Malcolm's Sparks code
 * 
 * Exception wrapper for Throwable.
 * 
 */

import java.io.PrintStream;
import java.io.PrintWriter;

public class ConfigurationException extends Exception {

	private static final long serialVersionUID = 1124780806618649735L;

	/**
	 * The throwable that caused this throwable to get thrown.
	 */
	Throwable cause;

	/**
	 * @param exception cause
	 */
	public ConfigurationException(String msg) {
		super(msg);
	}

	/**
	 * @param msg: exception cause message
	 * @param cause: exception to be wrapped
	 */
	public ConfigurationException(String msg, Throwable cause) {
		super(msg);
		this.cause = cause;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getCause()
	 */
	public Throwable getCause() {
		return cause;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#printStackTrace()
	 */
	public void printStackTrace() {
		super.printStackTrace();
		if (cause != null) {
			System.out.println("Caused by:");
			cause.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
	 */
	public void printStackTrace(PrintStream s) {
		super.printStackTrace(s);
		if (cause != null) {
			s.println("Caused by:");
			cause.printStackTrace(s);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
	 */
	public void printStackTrace(PrintWriter s) {
		super.printStackTrace(s);
		if (cause != null) {
			s.println("Caused by:");
			cause.printStackTrace(s);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		if (cause == null) {
			return super.getMessage();
		} else {
			return super.getMessage() + " (see below for lower-level details)";
		}
	}
}
