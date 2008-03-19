<?xml version="1.0" encoding="UTF-8"?>
<div
  xmlns="http://www.w3.org/2002/06/xhtml2"
  xmlns:widget="http://www.volantis.com/xmlns/2006/05/widget"
  xmlns:urid="http://www.volantis.com/xmlns/marlin-uri-driver">

  <widget:block id="myEffectsBlock" class="widget">
    <widget:block-content>
     <urid:fetch href="form.inc"/>
    </widget:block-content>
  </widget:block>

  <div class="buttons">
    <widget:button action="myEffectsBlock#hide">hide</widget:button>
    <widget:button action="myEffectsBlock#show">show</widget:button>
  </div>
</div>
