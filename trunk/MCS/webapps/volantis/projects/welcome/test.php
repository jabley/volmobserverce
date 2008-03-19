<?php header("Content-type: x-application/vnd.xdime+xml"); ?>
<?php print"<?xml version=\"1.0\" encoding=\"UTF-8\"?>

<html xmlns=\"http://www.w3.org/2002/06/xhtml2\"
   xmlns:xf=\"http://www.w3.org/2002/xforms\"
   xmlns:si=\"http://www.volantis.com/xmlns/2004/06/xdimecp/interim/si\"
   xmlns:sel=\"http://www.w3.org/2004/06/diselect\"
   xmlns:pipeline=\"http://www.volantis.com/xmlns/marlin-pipeline\"
   xmlns:template=\"http://www.volantis.com/xmlns/marlin-template\"
   xmlns:urid=\"http://www.volantis.com/xmlns/marlin-uri-driver\"
   xmlns:mcs=\"http://www.volantis.com/xmlns/2006/01/xdime/mcs\" >
<head>
 <title>Hello</title>
 <link rel=\"mcs:layout\" href=\"/error.mlyt\"/>
</head>
<body>
<div  style=\"mcs-container: 'error'\">Hello MCS, this is PHP.</div>
</body>
</html>"; ?>
