/**
 * (c) Volantis Systems Ltd 2008. 
 */


/*--------------------------------------------------------------------------*/
//TODO - move these global functions into namespace e.g. Widget.Utils.*
// support for faking opacity - Dave Raggett June 2006
// see also http://www.phpied.com/rgb-color-parser-in-javascript/

// rgbString must be of form "rgb(100,200,0)" or "transparent"
// however this is guaranteed when using computed colors
// but not with NetFront, sigh!!!
function Color(rgbString)
{
  this._rgba = false;
  if (rgbString == "transparent")
  {
    this.toString = function () { return "transparent" };
    this.transparent = true;
    return this;
  }

  // full color table is quite large ...
  var color = namedColors[rgbString];

  if (color)
    rgbString = color;

  if (rgbString[0] == '#')
  {
    var rex = /^#(\w{2})(\w{2})(\w{2})$/;
    var nums = rex.exec(rgbString);

    if (!nums)
    {
      // use of parse color to convert from #xxx to #xxxxxx
      nums = rex.exec(rgbString.parseColor());
    }

    this.red = parseInt(nums[1], 16);
    this.green = parseInt(nums[2], 16);
    this.blue = parseInt(nums[3], 16);
    this.transparent = false;
  } else if (rgbString.indexOf('rgba')!= -1){
    var rex = /^rgba\((\d{1,3}),\s*(\d{1,3}),\s*(\d{1,3}),\s*(\d{1,3})\)$/;
    var nums = rex.exec(rgbString);

    if (!nums)
      throw "invalid color: " + rgbString;

    this.red = parseInt(nums[1]);
    this.green = parseInt(nums[2]);
    this.blue = parseInt(nums[3]);
    this.alpha = parseInt(nums[4]);
    this.transparent = false;
    this._rgba = true;

    this.toString = function () {
      return "rgba(" + this.red + "," + this.green + "," + this.blue + ","+this.alpha+")";
    };
  } else if (rgbString[0] == 'r'){
    var rex = /^rgb\((\d{1,3}),\s*(\d{1,3}),\s*(\d{1,3})\)$/;
    var nums = rex.exec(rgbString);

    if (!nums)
      throw "invalid color: " + rgbString;

    this.red = parseInt(nums[1]);
    this.green = parseInt(nums[2]);
    this.blue = parseInt(nums[3]);
    this.transparent = false;

    this.toString = function () {
      return "rgb(" + this.red + "," + this.green + "," + this.blue + ")";
    };
  }
  return this;
}

// return blended color from foreground color object
// background color object and the opacity (0..1)
// assumes background is itself opaque
function BlendColor(fg, bg, opacity)
{

  if (typeof opacity != "number" || opacity < 0 || opacity > 1){
    // if opacity undefined or ou of bound it wil be set to 1
    // then background is invisible
    // no exception must be thrown because it results unexpected errors
    // on opera platform
    opacity = 1.0;
  }

  if (!fg || opacity < 0.001){
    return bg;
  }
  // when opacity == 1 we still calculate it, because
 // we still need to handle two casess rgb or rgba
  if(fg.transparent){
      this.red = Math.floor( (1 - opacity)* bg.red);
      this.green = Math.floor( (1 - opacity)* bg.green);
      this.blue = Math.floor((1 - opacity)* bg.blue);
  } else if(bg.transparent){
      this.red = Math.floor(opacity * fg.red);
      this.green = Math.floor(opacity * fg.green);
      this.blue = Math.floor(opacity * fg.blue);
  } else {
      this.red = Math.floor(opacity * fg.red + (1 - opacity)* bg.red);
      this.green = Math.floor(opacity * fg.green + (1 - opacity)* bg.green);
      this.blue = Math.floor(opacity * fg.blue + (1 - opacity)* bg.blue);
  }
  if(fg._rgba) {
    this.toString = function () {
      return "rgba(" + this.red + "," + this.green + "," + this.blue + ","+fg.alpha+")";
    }
  } else {
    this.toString = function () {
      return "rgb(" + this.red + "," + this.green + "," + this.blue + ")";
    }
  }
  return this;
}


Element.saveStylesFakeOpacity = function(element){
  element= $(element);
  if(!element._madeFakeOpacity) {
    element._madeFakeOpacity = true;

    element._fo_foreground = Element.getStyle(element,"background-color");
    element._fo_fg = new Color(element._fo_foreground);
    element._fo_text = Element.getStyle(element,"color");
    element._fo_tx = new Color(element._fo_text);
    element._fo_border_left = Element.getStyle(element,"border-left-color");
    element._fo_bo = new Color(element._fo_border_left);
    element._fo_border_right = Element.getStyle(element,"border-right-color");
    element._fo_border_top = Element.getStyle(element,"border-top-color");
    element._fo_border_bottom = Element.getStyle(element,"border-bottom-color");
    element._fo_backgroud = Element.getNonTransparentBackground(element.parentNode);
    element._fo_bg = new Color(element._fo_backgroud);
  }
}


Element.getNonTransparentBackground = function(element){
  var node = element;
  var background = Element.getStyle(node, "background-color");
  // on OperaMobile, NokiaOSS, Netfront (all don't support opacity) when outer element is fixed
  // there were issue with end loop condition, therefore calculation must be end when HTML is reached.
  while(((background == "transparent") || (background=="rgba(0, 0, 0, 0)")) && (node.parentNode != null)
    && (node.nodeName !== "HTML")){
    node = node.parentNode;
    background = Element.getStyle(node, "background-color");
  }
  if((background === "transparent") || (node.nodeName === "HTML")){
    background = "rgb(255,255,255)";
  }
  // when we wound rgba also rgba must be set
  if(background === "rgba(0, 0, 0, 0)"){
    background = "rgba(255,255,255,0)";
  }
  return background;
}

Element.restoreStylesFakeOpacity = function(element){
  element= $(element);
  if(element._madeFakeOpacity) {
    element._madeFakeOpacity = undefined;
    element.style.backgroundColor = element._fo_foreground;
    element.style.color = element._fo_text;
    element.style.borderLeftColor = element._fo_border;
    element.style.borderLeftColor = element._fo_border_left;
    element.style.borderRightColor = element._fo_border_right;
    element.style.borderTopColor = element._fo_border_top;
    element.style.borderBottomColor = element._fo_border_bottom;
  }
}

Element.fakeOpacity = function (element, opacity) {
    element= $(element);
    var i = 0;
    // a markup needs special treatment - this is workaround
    // permanent fix needs reimplementation of fakeOpacity
    var childList = element.getElementsByTagName("a");
    for(i = 0;i<childList.length;i++){
      Element.aFakeOpacity(childList[i],opacity);
    }
    if(opacity){
      Element.normalFakeOpacity(element,opacity);
    }
};

Element.aFakeOpacity = function (element, opacity) {
    element= $(element);
    var faked = new BlendColor(element._fo_tx, element._fo_bg, opacity);
    element.style.color = faked.toString();
    element.style.opacity = opacity.toString();
};


Element.normalFakeOpacity = function (element, opacity) {
    element= $(element);
    Element.saveStylesFakeOpacity(element);
    var faked = new BlendColor(element._fo_fg, element._fo_bg, opacity);
    element.style.backgroundColor = faked.toString();
    faked = new BlendColor(element._fo_tx, element._fo_bg, opacity);
    element.setStyle({ color :faked.toString()});
    faked = new BlendColor(element._fo_bo, element._fo_bg, opacity);
    element.style.borderColor = faked.toString();
    element.style.opacity = opacity.toString();
  };



function SetFakeOpacity(id, opacity)
{
  var el = $(id);

  if (!el)
    throw "unknown element id: " + id;

  if (!el.getStyle)
  {
    el.getStyle = function (style) {
      return computedStyle(this, style);
    };

    el.parentNode.getStyle = function (style) {
      return computedStyle(this, style);
    };
  }

  if (opacity < 0.99)
  {
    Element.fakeOpacity(el, opacity);
  }
}

