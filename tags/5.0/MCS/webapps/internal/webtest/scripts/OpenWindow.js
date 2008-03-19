var eventWindow = window.open ("", "event_window");
var writer = eventWindow.document;
writer.open ();
writer.write ("<p>Starting</p>");
writer.close ();
