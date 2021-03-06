 /**
 * (c) Volantis Systems Ltd 2006.
 */

// moved from vfc-base.js as it is used only in this widget

Element.addMethods({

 /* calculate real dimensions in PX units
  * return object with all needed dimensions .i.e. with borders, padding and content dimensions
  * results.width -
  * result.height
  * result.borderLeftWidth
  * result.borderRightWidth
  * result.borderTopWidth
  * result.borderBottomWidth
  * result.paddingLeft
  * result.paddingRight
  * result.paddingTop
  * result.paddingBottom
  * result.contentWidth
  * result.contentHeight -
  */
  vfcGetRealDimensions: function(element) {
    var result = {};
    //if element has display value none or not none
    var isInDom = true;
    element = $(element);

    // All *Width and *Height properties give 0 on elements with display none,
    // enable the element temporarily
    if (Element.getStyle(element, 'display') == 'none') {
      isInDom = false;

      // display:block is used instead display:'' because in some cases display is set by inline style and additional by CSS class (date-picker).
      // if we change only inline style to display:'' element get display:none from CSS class and it is still impossible to read offsetWidth.
      // this method is used only for reading dimensions and should works good also for inline element, but it is not tested for it
      element.vfcReplaceStyle({visibility: 'hidden', position: 'absolute', display:'block'})
    }

    result.width = element.offsetWidth;
    result.height = element.offsetHeight;

    //get others dimensions

    var borderLeftWidthOld = 0;
    var borderRightWidthOld = 0;
    var borderTopWidthOld = 0;
    var borderBottomWidthOld = 0;

    var paddingLeftOld = 0;
    var paddingRightOld = 0;
    var paddingTopOld = 0;
    var paddingBottomOld = 0;

    //get  borders dimensions
    if(Element.getStyle(element, 'border-left-width') != undefined) {
      borderLeftWidthOld = Element.getStyle(element, 'border-left-width');
      Element.setStyle(element, {borderLeftWidth: '0px'});
    }

    result.borderLeftWidth = result.width - element.offsetWidth;

    if(Element.getStyle(element, 'border-right-width') != undefined) {
      borderRightWidthOld = Element.getStyle(element, 'border-right-width');
      Element.setStyle(element, {borderRightWidth: '0px'});
    }

    result.borderRightWidth = result.width - element.offsetWidth - result.borderLeftWidth;

    if(Element.getStyle(element, 'border-top-width') != undefined) {
      borderTopWidthOld = Element.getStyle(element, 'border-top-width');
      Element.setStyle(element, {borderTopWidth: '0px'});
    }

    result.borderTopWidth = result.height - element.offsetHeight;

    if(Element.getStyle(element, 'border-bottom-width') != undefined) {
      borderBottomWidthOld = Element.getStyle(element, 'border-bottom-width');
      Element.setStyle(element, {borderBottomWidth: '0px'});
    }

    result.borderBottomWidth = result.height - element.offsetHeight - result.borderTopWidth;

    //get  paddings dimensions
    if(Element.getStyle(element, 'padding-left') != undefined) {
      paddingLeftOld = Element.getStyle(element, 'padding-left');
      Element.setStyle(element, {paddingLeft: '0px'});
    }

    result.paddingLeft = result.width - element.offsetWidth - result.borderLeftWidth -  result.borderRightWidth;

    if(Element.getStyle(element, 'padding-right') != undefined) {
      paddingRightOld = Element.getStyle(element, 'padding-right');
      Element.setStyle(element, {paddingRight: '0px'});
    }

    result.paddingRight = result.width - element.offsetWidth - result.borderLeftWidth -  result.borderRightWidth - result.paddingLeft;

    if(Element.getStyle(element, 'padding-top') != undefined) {
      paddingTopOld = Element.getStyle(element, 'padding-top');
      Element.setStyle(element, {'padding-top': '0px'});
    }

    result.paddingTop = result.height - element.offsetHeight - result.borderTopWidth - result.borderBottomWidth;

    if(Element.getStyle(element, 'padding-bottom') != undefined) {
      paddingBottomOld = Element.getStyle(element, 'padding-bottom');
      Element.setStyle(element, {paddingBottom: '0px'});
    }

    result.paddingBottom = result.height - element.offsetHeight - result.borderTopWidth - result.borderBottomWidth - result.paddingTop;

    result.contentWidth = element.offsetWidth;
    result.contentHeight = element.offsetHeight;

    //revert oryginal values

    Element.setStyle(element, {borderLeftWidth: borderLeftWidthOld});
    Element.setStyle(element, {borderRightWidth: borderRightWidthOld});
    Element.setStyle(element, {borderTopWidth: borderTopWidthOld});
    Element.setStyle(element, {borderBottomWidth: borderBottomWidthOld});

    Element.setStyle(element, {paddingLeft: paddingLeftOld});
    Element.setStyle(element, {paddingRight: paddingRightOld});
    Element.setStyle(element, {paddingTop: paddingTopOld});
    Element.setStyle(element, {paddingBottom: paddingBottomOld});

    if(! isInDom) {
      element.vfcRevertStyle();
    }
    return result;
  }
});


Object.extend(Widget,{
 /*
  * Returns dimension in px unit from dimension set by others units -  otherUnitSize
  */
  getPXDimension: function(otherUnitSize) {
    // Width of a single character will be calculated by
    // temporarily creating div element, set size width
    // and measuring its actual width in pixels.
    var resultPX = 0;
    // If width is not already cached, calculate it now.
    // First, create an invisible span element containing 'M' contents.
    this.mElement = document.createElement("div")
    // Make an element invisible.
    this.mElement.style.visibility = "hidden";
    this.mElement.style.display = "block";
    this.mElement.style.position = "absolute";
    this.mElement.style.padding ="0px";
    this.mElement.style.border ="0px";
    this.mElement.style.margin ="0px";
    if(otherUnitSize !== undefined) {
      this.mElement.style.width = otherUnitSize;
    } else {
      this.mElement.style.width = "0px";
    }
    this.mElement.appendChild(document.createTextNode(""));

    // Now, append it to the document body
    document.body.appendChild(this.mElement);
    // Now, measure its width in pixels.
    resultPX = this.mElement.scrollWidth;
    this.mElement.style.display = "none";
    document.body.removeChild(this.mElement)
    return resultPX;
  }
});



Widget.Corners = Class.create();

Object.extend(Widget.Corners.prototype, {

  initialize: function(id, cornersOpt) {
    //id attribute of rendered box element
    this.id = id;
    this.el = $(id);
    // array of dimensions for each corner, index 0 - topleft, 1 - topright, 2 - bottomright, 3 - bottomleft
    this.cornersOpt = cornersOpt;

    // antialiasing will be implemended in next version
    this.antiAliasing = false;

    //array of div elements emulated TOP rounded corners
    this.topLabel = [];

    //array of div elements emulated TOP rounded corners
    this.bottomLabel = [];

    //div element which is wrapper on rendered element content
    this.textContainer = null;

    // the value of height which should be added to height of external DIV
    // this workaround as emulated div not resize e.g. TD in TABLE
    this.resizeY = 0;

    this.setup();

    this.run();
  },

  setup: function() {

    //foreground color for rounded element (e.g. background-color for div)
    this.fc = '#FFFFFF';
    this.fc = this.el.getStyle('background-color') || '#FFFFFF';
    if(this.fc != 'transparent') {
      this.fc = this.fc.parseColor();
    }

    //background color for rounded corners
    this.bc ='#FFFFFF';
    this.bc = Element.getStyle(this.el.parentNode, 'background-color');
    if(this.bc != 'transparent') {
      this.bc = this.bc.parseColor();
    }

    var pos = this.el.getStyle('position');
    if(pos == 'absolute' || pos == 'fixed') {
      this.bc = 'transparent';
    }

    this.el.style.backgroundColor = this.bc;
    if(Prototype.msieBrowser()) {
      this.el.style.background = this.bc;
    }

    //setup operations - e.g. set content in contener
    this.contentWidth = this.el.vfcGetRealDimensions().contentWidth;
    this.elDims = this.el.vfcGetRealDimensions();
    
    var div = document.createElement("div");
    //workaround div element to proper working border-left and right on textContainer div
    var divInner = document.createElement("div");

    if (! Prototype.msieBrowser()) {
	    divInner.style.border = '1px solid ' + this.fc;
	    divInner.style.backgroundColor = 'inherit';
    }

    if(! Prototype.operaMobile()) {
      div.style.width = this.elDims.contentWidth + this.elDims.borderLeftWidth + this.elDims.borderRightWidth + 'px';
    } else {
      div.style.width = this.elDims.contentWidth  + this.elDims.borderLeftWidth + this.elDims.borderRightWidth + this.elDims.paddingLeft + this.elDims.paddingRight + 'px';
    }

    div.style.overflow = 'hidden';
    div.style.height = '100%';
    div.style.backgroundColor = this.fc;
    if (Prototype.msieBrowser()) {
      div.style.background = this.fc;
    }

    //rewrite padding to content DIV
    if(this.elDims.paddingLeft) {
      div.style.paddingLeft = this.elDims.paddingLeft + 'px';
      this.el.style.paddingLeft = '0px';
    }
    if(this.elDims.paddingRight) {
      div.style.paddingRight = this.elDims.paddingRight + 'px';
      this.el.style.paddingRight = '0px';
    }
    if(this.elDims.paddingTop) {
      div.style.paddingTop = this.elDims.paddingTop + 'px';
      this.el.style.paddingTop = '0px';
    }
    if(this.elDims.paddingBottom) {
      div.style.paddingBottom = this.elDims.paddingBottom + 'px';
      this.el.style.paddingBottom = '0px';
    }

    this.el.appendChild(div);
    div.appendChild(divInner);
    Element.cleanWhitespace(this.el);

    for(var i=0; i < this.el.childNodes.length; i++) {
      if(div != this.el.childNodes[i]) {
      	divInner.appendChild(this.el.childNodes[i]);
      }
    }

    this.textContainer = div;
    this.setBordersSiteWidth();

    if(! Prototype.msieBrowser()) {
      //if not render bottom label rewrite borderBottom from this.el to inner DIV
      if(! (this.cornersOpt[2].length || this.cornersOpt[3].length)) {
        div.style.borderBottom = this.elDims.borderBottomWidth + ' ' + Element.getStyle(this.el, 'border-bottom-color') + ' solid';
        this.el.style.borderBottom = 'none';
      }
  
      //if not render top label rewrite borderTop from this.el to inner DIV
      if(! (this.cornersOpt[0].length || this.cornersOpt[1].length)) {
        div.style.borderTop = this.elDims.borderTopWidth + ' ' + Element.getStyle(this.el, 'border-top-color') + ' solid';
        this.el.style.borderTop = 'none';
      }
      div.style.height = this.elDims.contentHeight + 'px'
    }
  },

  /*
   *  run rendering
   */
  run: function() {
    //render top label
    if(this.cornersOpt[0].length || this.cornersOpt[1].length) {
      this.addTopMarkup();
    }

    //render bottom label
    if(this.cornersOpt[2].length || this.cornersOpt[3].length) {
      this.addBottomMarkup();
    } 

    // workaround - resize DIV in order to jostle TD in TABLE for example which wrap rounded element
    // e.i. to the original element is added height of TOP and BOTTOM div (emulated rounded corner label)
    if(! Prototype.msieBrowser()) {
      this.el.style.height = this.elDims.contentHeight 
                                        + this.elDims.borderTopWidth
                                        + this.elDims.paddingTop
                                        + this.elDims.paddingBottom
                                        + this.elDims.borderBottomWidth
                                        + this.resizeY 
                                        + 'px';
    }
  },

  /*
   *  render top label
   */
  addTopMarkup: function() {

    var lX = Widget.getPXDimension(this.cornersOpt[0][0]) || 0;
    var lY = Widget.getPXDimension(this.cornersOpt[0][1]) || 0;
    var rX = Widget.getPXDimension(this.cornersOpt[1][0]) || 0;
    var rY = Widget.getPXDimension(this.cornersOpt[1][1]) || 0;
    var maxY = Math.max(lY, rY);

    if(maxY > 0) {
      this.resizeY += maxY;
    }

    var d = document.createElement("div");
	  d.style.backgroundColor = this.bc;
	  if(Prototype.msieBrowser()) {
  	  d.style.background = this.bc;
  	}

    d.style.width = this.elDims.contentWidth + 2 * this.elDims.borderLeftWidth + 2 * this.elDims.borderRightWidth + this.elDims.paddingLeft + this.elDims.paddingRight + 'px';
    d.className = "rounded";

    var lastlArc = 0;
    var lastrArc = 0;

   // read topBorder
    var borderTopStyle = Element.getStyle(this.el, 'border-top-style');
    var borderTopWidth = Element.getStyle(this.el, 'border-top-width') || 0;
    var borderTopColor = Element.getStyle(this.el, 'border-top-color');

    // new size - it is more about border width
    this.borderPxValue = parseFloat(borderTopWidth) || 0;

    // because IE return borderWidth = medium if border is not set so we
    // have to check also borderStyle. If borderStyle is none set borderPxValue to 0
    if(borderTopStyle == 'none') {
      this.borderPxValue = 0;
    }

    for (var i = 1; i <= maxY; i++) {
      var coverage, arc2, arc3;
      // Find intersection of arc with bottom of pixel row
      lArc = Math.sqrt(1.0 - Math.sqr(1.0 - i / lY)) * lX;
      rArc = Math.sqrt(1.0 - Math.sqr(1.0 - i / rY)) * rX;
      // Calculate how many pixels are bg, fg and blended.
      var l_bg = lX - Math.ceil(lArc);
      var l_fg = Math.floor(lastlArc);
      var l_aa = lX - l_bg - l_fg;

      var r_bg = rX - Math.ceil(rArc);
      var r_fg = Math.floor(lastrArc);
      var r_aa = rX - r_bg - r_fg;

      // Create pixel row wrapper
      var x = document.createElement("div");
      x.style.height = '1px';
      x.style.overflow = 'hidden';
      x.style.fontSize = '0px';

      var y = d;

      //if only left
      if(i < lY) {
        x.style.marginLeft = l_bg +"px";
      }

      //if only right
      if (i < rY) {
        x.style.marginRight = r_bg +"px";
      }

      if(i <= this.borderPxValue && this.borderPxValue) {
        x.style.backgroundColor = borderTopColor;
        this.el.style.borderTopWidth = '0px';
      }

      //set borders for left
      if(this.borderLeft !== 0) {
        if(i <= lY) {
          x.style.borderLeftWidth = l_aa + ((this.borderLeft - 1)) + "px";
        } else {
          x.style.borderLeftWidth = this.borderLeftWidth;
        }
        x.style.borderLeftStyle = this.borderLeftStyle;
        x.style.borderLeftColor = this.borderLeftColor;
      }

    //set borders for right
      if(this.borderRight !== 0) {
        if(i <= rY) {
          x.style.borderRightWidth = r_aa + ((this.borderRight - 1)) + "px";
        } else {
          x.style.borderRightWidth = this.borderRightWidth;
        }
        x.style.borderRightStyle = this.borderRightStyle;
        x.style.borderRightColor = this.borderRightColor;
      }

      if(i > this.borderPxValue) {
        x.style.backgroundColor = this.fc;
        if(Prototype.msieBrowser()) {
          x.style.background = this.fc;
        }
      }
      y.appendChild(x);
      lastlArc = lArc;
      lastrArc = rArc;
    }

    //some operations with borders left and right

    if(this.borderLeft !== 0) {
      this.el.style.borderLeftWidth = '0px';
      this.textContainer.style.borderLeftWidth = this.borderLeftWidth + 'px';
      this.textContainer.style.borderLeftStyle = this.borderLeftStyle;
      this.textContainer.style.borderLeftColor = this.borderLeftColor;
      this.isLeftBorderSet = true;
    }

    if(this.borderRight !== 0) {
      this.el.style.borderRightWidth = '0px';
      this.textContainer.style.borderRightWidth = this.borderRightWidth + 'px';
      this.textContainer.style.borderRightStyle = this.borderRightStyle;
      this.textContainer.style.borderRightColor = this.borderRightColor;
      this.isRightBorderSet = true;
    }

    this.el.insertBefore(d, this.el.firstChild);
  },

  addBottomMarkup: function() {

    var rX = Widget.getPXDimension(this.cornersOpt[2][0]) || 0;
    var rY = Widget.getPXDimension(this.cornersOpt[2][1]) || 0;
    var lX = Widget.getPXDimension(this.cornersOpt[3][0]) || 0;
    var lY = Widget.getPXDimension(this.cornersOpt[3][1]) || 0;
    var maxY = Math.max(lY, rY);

    if(maxY > 0) {
      this.resizeY += maxY;
    }

    var d = document.createElement("div");
	  d.style.backgroundColor = this.bc;
	  if(Prototype.msieBrowser()) {
  	  d.style.background = this.bc;
  	}

    d.style.width = this.elDims.contentWidth + 2 * this.elDims.borderLeftWidth + 2 * this.elDims.borderRightWidth + this.elDims.paddingLeft + this.elDims.paddingRight + 'px';
    d.className = "rounded";

    var lastlArc = 0;
    var lastrArc = 0;

   // read topBorder
    var borderBottomStyle = Element.getStyle(this.el, 'border-bottom-style');
    var borderBottomWidth = Element.getStyle(this.el, 'border-bottom-width') || 0;
    var borderBottomColor = Element.getStyle(this.el, 'border-bottom-color');

    // new size - it is more about border width
    this.borderPxValue = parseFloat(borderBottomWidth) || 0;

    for (var i = 1; i <= maxY; i++) {
      var coverage, arc2, arc3;
      // Find intersection of arc with bottom of pixel row
      lArc = Math.sqrt(1.0 - Math.sqr(1.0 - i / lY)) * lX;
      rArc = Math.sqrt(1.0 - Math.sqr(1.0 - i / rY)) * rX;
      // Calculate how many pixels are bg, fg and blended.
      var l_bg = lX - Math.ceil(lArc);
      var l_fg = Math.floor(lastlArc);
      var l_aa = lX - l_bg - l_fg;

      var r_bg = rX - Math.ceil(rArc);
      var r_fg = Math.floor(lastrArc);
      var r_aa = rX - r_bg - r_fg;

      // Create pixel row wrapper
      var x = document.createElement("div");
      x.style.height = '1px';
      x.style.overflow = 'hidden';
      x.style.fontSize = '0px';

      var y = d;

      //if only left
      if(i < lY) {
        x.style.marginLeft = l_bg +"px";
      }

      //if only right
      if (i < rY) {
        x.style.marginRight = r_bg +"px";
      }

      if(i <= this.borderPxValue && this.borderPxValue) {
        x.style.backgroundColor = borderBottomColor;
        this.el.style.borderBottomWidth = '0px';
      }
      //set borders for left
      if(this.borderLeft !== 0) {
        if(i <= lY) {
          x.style.borderLeftWidth = l_aa + ((this.borderLeft - 1)) + "px";
        } else {
          x.style.borderLeftWidth = this.borderLeftWidth;
        }
        x.style.borderLeftStyle = this.borderLeftStyle;
        x.style.borderLeftColor = this.borderLeftColor;
      }

      //set borders for right
      if(this.borderRight !== 0) {
        if(i <= rY) {
          x.style.borderRightWidth = r_aa + ((this.borderRight - 1)) + "px";
        } else {
          x.style.borderRightWidth = this.borderRightWidth;
        }
        x.style.borderRightStyle = this.borderRightStyle;
        x.style.borderRightColor = this.borderRightColor;
      }

      if(i > this.borderPxValue) {
        x.style.backgroundColor = this.fc;
        if(Prototype.msieBrowser()) {
          x.style.background = this.fc;
        }
      }
      y.insertBefore(x, y.firstChild);
      lastlArc = lArc;
      lastrArc = rArc;
    }

    //some operations with borders left and right

    if(this.borderLeft !== 0 && ! this.isLeftBorderSet) {
      this.el.style.borderLeftWidth = '0px';
      this.textContainer.style.borderLeftWidth = this.borderLeftWidth + 'px';
      this.textContainer.style.borderLeftStyle = this.borderLeftStyle;
      this.textContainer.style.borderLeftColor = this.borderLeftColor;
    }

    if(this.borderRight !== 0 &&  ! this.isRightBorderSet) {
      this.el.style.borderRightWidth = '0px';
      this.textContainer.style.borderRightWidth = this.borderRightWidth + 'px';
      this.textContainer.style.borderRightStyle = this.borderRightStyle;
      this.textContainer.style.borderRightColor = this.borderRightColor;
    }

    this.el.appendChild(d);
  },

  /*
   *  Set  borders for content container DIV element
   */
  setBordersSiteWidth: function() {
   // read leftBorder
    this.borderLeftStyle = 'solid';
    this.borderLeftWidth = this.elDims.borderLeftWidth;
    this.borderLeftColor = Element.getStyle(this.el, 'border-left-color');

   // read rightBorder
   //borderStyle always is solid
    this.borderRightStyle = 'solid';
    this.borderRightWidth = this.elDims.borderRightWidth;
    this.borderRightColor = Element.getStyle(this.el, 'border-right-color');

    this.borderLeft = this.borderLeftWidth;
    this.borderRight = this.borderRightWidth;
  }
});

Math.sqr = function (x) {
  return x*x;
};
