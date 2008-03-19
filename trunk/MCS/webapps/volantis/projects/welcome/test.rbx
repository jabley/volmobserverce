#!/usr/bin/ruby
require 'cgi'
cgi = CGI.new()

cgi.out("x-application/vnd.xdime+xml") { "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n" +

"<html xmlns=\"http://www.w3.org/2002/06/xhtml2\"\n" +
"   xmlns:xf=\"http://www.w3.org/2002/xforms\"\n" +
"   xmlns:sel=\"http://www.w3.org/2004/06/diselect\"\n" +
"   xmlns:pipeline=\"http://www.volantis.com/xmlns/marlin-pipeline\"\n" +
"   xmlns:template=\"http://www.volantis.com/xmlns/marlin-template\"\n" +
"   xmlns:urid=\"http://www.volantis.com/xmlns/marlin-uri-driver\"\n" +
"   xmlns:mcs=\"http://www.volantis.com/xmlns/2006/01/xdime/mcs\" >\n" +
"<head>\n" +
" <title>Hello</title>\n" +
" <link rel=\"mcs:layout\" href=\"/error.mlyt\"/>\n" +
"</head>\n" +
"<body>\n" +
"<div  style=\"mcs-container: 'error'\">Hello MCS, this is Ruby.</div>\n" +
"</body>\n" +
"</html>\n" 
}
