/* ----------------------------------------------------------------------------
 * $Header: /src/voyager/webapp/internal/webtest/scripts/eventWindow.js,v 1.1 2002/03/11 15:56:50 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-02    Paul            VBM:2001122105 - Added this header.
 * ----------------------------------------------------------------------------
 */

var console = null;
var scrollto = 400;

function debug(msg)
{
	if ((console == null) || (console.closed)) {
		console = window.open("","console",
			"width=300, height=200, resizable");
	}

	var date = new Date();
	var hour = date.getHours();
	var mins = date.getMinutes();
	var secs = date.getSeconds();
	console.document.writeln("<p>"+hour+":"+mins+":"+secs+" -"+msg+"<br /></p>");
	console.scroll(0,scrollto);
	scrollto = scrollto + 50;
}
