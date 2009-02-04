/**
 * (c) Volantis Systems Ltd 2008. 
 */

//moved from scriptaculous
Effect.PAIRS = {
    'slide':  ['SlideDown','SlideUp'],
    'blind':  ['BlindDown','BlindUp'],
    'appear': ['Appear','Fade']
};

/* ------------- transitions ------------- */

// moved from vfc-base.js as it is used only in this file
// Define additional method on Position class.
Object.extend(Position, {
  /**
   * Method similar to positionOffset but including fixed elements
   * in calculation alghoritms.
   */
  fixedOffset: function(element) {
    var valueT = 0, valueL = 0;
    do {
      valueT += element.offsetTop  || 0;
      valueL += element.offsetLeft || 0;
      element = element.offsetParent;
      if (element) {
        p = Element.getStyle(element, 'position');
        if (p == 'relative' || p == 'absolute' || p == 'fixed') break;
      }
    } while (element);
    return [valueL, valueT];
  }
});

// moved from vfc-base.js as it is used only in this file
Element.addMethods({
  /**
   * return real value of style, not replaced by null when real value is auto.
   */
  getNotNullStyle: function(element, style) {
    element = $(element);
    var value = element.style[style.camelize()];
    var isStyleChanged = false;
    if((Prototype.nokiaOSSBrowser() || Prototype.firefoxBrowser() || Prototype.konquerorBrowser() || Prototype.operaPC()) && element.style.display=='none') {
      element.style.visibility='hidden';
      element.style.display='block';
      isStyleChanged = true;
    }

    if (!value) {
      // this workaround for opera PC older than 9.02 version.
      // The issue is if element change display:none to display:block
      // it is required some time of delay for getting property value which are computed
      // after putting element into DOM (by set display:block)
      // The problem is only with computed values like height, width. It seems that these browsers
      // needs some time about 200 miliseconds to resize document's layout ade refresh its computed style object.
      // There is additional condition with time because getComputedStyle always return 0px
      // in case of really value of property is 0px, before and after time for refresh (about 200ms) in that case it is
      // not possible to recognize whether returned value is correct
      if (document.defaultView && document.defaultView.getComputedStyle) {
        var css = document.defaultView.getComputedStyle(element, null);
        if(/Opera\/9.00|Opera\/9.01|Opera\/9.02/.test(navigator.userAgent)) {
          if (!value) {
            var now = new Date();
            var exitTime = now.getTime() + 300;
            while(true) {
        value = css ? css.getPropertyValue(style) : null;
              now = new Date();
              if(value != '0px' || now.getTime() > exitTime) {
                  break;
              }
            }
          }
        } else {
          value = css ? css.getPropertyValue(style) : null;
        }
      } else if (element.currentStyle) {
        value = element.currentStyle[style.camelize()];
      }
    }

    if (window.opera && ['left', 'top', 'right', 'bottom'].include(style))
      if (Element.getStyle(element, 'position') == 'static') value = 'auto';

    if(isStyleChanged) {
      element.style.visibility='visible';
      element.style.display='none';
    }
    // always return  not null value
    return value;
  },

  getOpacity: function(element){  
    element = $(element);  
    // Faked opacity
    if (Prototype.useEmulatedOpacity()){ 
      return element._fakedOpacity;
    }
  
    var opacity;
    if (opacity = Element.getStyle(element, 'opacity'))  
      return parseFloat(opacity);  
    if (opacity = (Element.getStyle(element, 'filter') || '').match(/alpha\(opacity=(.*)\)/))  
      if(opacity[1]) return parseFloat(opacity[1]) / 100;  
    return 1.0;  
  },

  setOpacity: function(element, value){  
    element= $(element);  
    
    if (typeof element.initialOpacity == 'undefined')
      element.initialOpacity = element.style["opacity"] || 1.0;
  
    // Faked opacity
    if (Prototype.useEmulatedOpacity())
    {
      element._fakedOpacity = value;
      element.style["opacity"] = value.toString();
      Element.fakeOpacity(element, value);
      return;
    }
  
    if (value == 1){
      if((/Gecko/.test(navigator.userAgent) && !/Konqueror|Safari|KHTML/.test(navigator.userAgent))){
        Element.setStyle(element, { opacity: 0.999999 });        
      } else  if(/MSIE/.test(navigator.userAgent)) { 
        Element.setStyle(element, {filter: Element.getStyle(element,'filter').replace(/alpha\([^\)]*\)/gi,'')});  
    } else {  
        Element.setStyle(element, { opacity: 1 });                
      }
    } else {  
      if(value < 0.00001) value = 0;  
      Element.setStyle(element, {opacity: value});
      if(/MSIE/.test(navigator.userAgent))  
      Element.setStyle(element, 
        { filter: Element.getStyle(element,'filter').replace(/alpha\([^\)]*\)/gi,'') +
                  'alpha(opacity='+value*100+')' });  
    }
  }
});


/**
 * This file contains new implementation of effect.
 */

/**
 * Newer version of executer - in newer prototype there is such version
 * In old (used in Framework Client) there is not.
 */


var UpdatedExecuter = Class.create();
UpdatedExecuter.prototype = {
  interval : null,

  initialize: function(callback, frequency) {
    this.callback = callback;
    this.frequency = frequency;
    this.currentlyExecuting = false;

    this.interval = this.registerCallback();
  },

  registerCallback: function() {
    return setInterval(this.onTimerEvent.bind(this), this.frequency * 1000);
  },

  stop : function(){
    clearInterval(this.interval);
  },

  onTimerEvent: function() {
    if (!this.currentlyExecuting) {
      try {
        this.currentlyExecuting = true;
        this.callback();
      } finally {
        this.currentlyExecuting = false;
      }
    }
  }
};


/**
 * New Namespace CustomEffects that will  hold all effects and effect related objects.
 */
var CustomEffects = {

  /**
   * Some methods needs special treatments when getting and setting
   * like opacity because it is stored by different value on various
   * browsers.
   */
  specialElementGetMethods :
  {
  	opacity : 'getOpacity'
  },

  specialElementSetMethods :
  {
	opacity : 'setOpacity'
  },

  getOpacity : function(element){
  	var result = {};
	result.value = element.getOpacity();
	result.unit = undefined;
  	return result;
  },

  /**
   * Methods below serves for getting property value splitted
   * into value and unit parts. When no value is set default value
   * is returned. These methods helps to retrieve each property value
   * into individual property dependent way, what is importand in case
   * of some effect because of cross-browser incompatibility.
   * Value part necessary for right calculation, separate unit part
   * for setting property value.
   * Below there is map from property name to method for getting this property.
   * There is no need to create set method because it can be set with
   * existnig setStyle method.
   */
  specialEffectMethods :
  {
	width : 'getWidth',
	height : 'getHeight',
	lineHeight : 'getLineHeight',
	fontSize : 'getFontSize',
	top : 'getTop',
	left : 'getLeft',
	opacity : 'getOpacity'
  },


  /**
   * Top, right, left, bottom always 0% - as default value
   */
  getTop : function(element){
    var result = {};
    var position = Element.getStyle(element, 'position');
    if(position == 'static'){
      result.value = 0;
      result.unit = 'px';
    } else if(position == 'relative'){
      // do nothing because when position is relative
      // effects will destroy that, but affter effect finish
      // values will be restored
      result.value = 0;
      result.unit = 'px';
    } else if(position == 'absolute' || position == 'fixed'){
      var offsets;
      if(!Widget.supportsFixed()){
        offsets = Position.positionedOffset(element);
      } else {
        offsets = Position.fixedOffset(element);
      }
      result.value = offsets[1];
      result.unit = 'px';
    }
    return result;
  },

  getLeft : function(element){
    var result = {};
    var position = Element.getStyle(element, 'position');
    if(position == 'static'){
      result.value = 0;
      result.unit = 'px';
    } else if(position == 'relative'){
      // do nothing because when position is relative
      // effects will destroy that, but affter effect finish
      // values will be restored
      result.value = 0;
      result.unit = 'px';
    } else if(position == 'absolute' || position == 'fixed'){
      var offsets;
      if(!Widget.supportsFixed()){
        offsets = Position.positionedOffset(element);
      } else {
        offsets = Position.fixedOffset(element);
      }
      result.value = offsets[0];
      result.unit = 'px';
    }
    return result;
  },

  getRight : function(element){
  	return { value : 0, unit : '%'};
  },

  getBottom : function(element){
  	return { value : 0, unit : '%'};
  },

  /**
   * Try to get property value by height style if not possible use
   * getDimensions().height instead
   * @param {Object} element
   */
  getHeight : function(element){
  	var result = {};
  	var styleValue = Element.getStyle(element, 'height');
	if((!styleValue ) || (styleValue == 'auto')){
      result.value = Element.getDimensions(element).height;
  	  result.unit = 'px';
	} else {
	  result.value = parseFloat(styleValue);
	  result.unit = styleValue.toString().replace(
		result.value.toString(),"");
	}
	return result;
  },

  /**
   * Try to get property value by width style if not possible use
   * getDimensions().width instead
   * @param {Object} element
   */
  getWidth : function(element){
  	var result = {};
  	var styleValue = Element.getStyle(element, 'width');
	if((!styleValue ) || (styleValue == 'auto')){
	  result.value = 100;
	  result.unit = '%';
	} else {
	  result.value = parseFloat(styleValue);
	  result.unit = styleValue.toString().replace(
	    result.value.toString(),"");
	}
	return result;
  },

  /**
   * get line-height style property - if normal get 100%
   */
  getLineHeight : function(element){
  	var result = {};
  	var styleValue = Element.getStyle(element, 'lineHeight');
	if(!styleValue || styleValue === '' ||  styleValue === 'normal'){
	  result.value = 100;
	  result.unit = '%';
	} else {
	  result.value = parseFloat(styleValue);
	  result.unit = styleValue.toString().replace(
	    result.value.toString(),"");
	}
	return result;
  },

  /**
   * get font-size style property - if not number return 100%
   */
  getFontSize : function(element){
  	var result = {};
  	var styleValue = Element.getStyle(element, 'fontSize');
	if(!styleValue){
	  result.value = 100;
	  result.unit = '%';
	} else {
      var value = parseFloat(styleValue);
	  if(isNaN(value)){
	    result.value = 100;
	    result.unit = '%';
	  } else {
	    result.value = value;
	    result.unit = styleValue.toString().replace(
	  	  value.toString(),"");
	  }
	}
	return result;
  },

  /**
   * Common methods for all effects
   */

  /**
   * create outer div with the same position, to allow slide effect
   * there are some calculation to be sure that outer div is placed
   * exactly under transitioned effecd
   */
  _createOuterDiv : function(element){
    var newDiv = document.createElement('div');
    newDiv = $(newDiv);
    var pos = Element.getStyle(element, 'position');
    var dim = Element.getDimensions(element);
    var offset = Position.positionedOffset(element);
    var left = Element.getStyle(element, 'left');
    if(pos == 'absolute' || pos == 'fixed'){
      // when absolute or fixed we need to set top,left to 0
      // because outer element will have the same postion
      // so we must avoid duplicatting top/left values
      element._oldTop = { top : offset[1], left : offset[0]};
      Element.setStyle(element, { top : 0, left : 0});
    }

    // workaround for IE - if static position is set for outer DIV then overflow:hidden not working good in IE
    // so static is change to relative with top = 0px and left = 0px
    if(Prototype.msieBrowser() && pos == 'static') {
      Element.setStyle(newDiv, { position : 'relative', overflow : 'hidden', width : dim.width+'px', height : dim.height+'px', top : '0px', left : '0px'});
    } else {
    Element.setStyle(newDiv, { position : pos, overflow : 'hidden', width : dim.width+'px', height : dim.height+'px', top : offset[1]+'px', left : offset[0]+'px'});
    }
    return newDiv;
  },

  /**
   * remove outer div
   */
  _removeOuterDiv : function(element){
    var parent = $(element.parentNode);
    var mainElement = parent.parentNode;
    mainElement.insertBefore(element,parent);
    mainElement.removeChild(parent);
    var pos = Element.getStyle(element, 'position');
    if(pos == 'absolute' || pos == 'fixed'){
      // when absolute or fixed we need to restore old top and left
      Element.setStyle(element, { top : element._oldTop.top+'px', left : element._oldTop.left+'px'});
    }
  }
};

/**
 * EmptyEffectStrategy holds basic properties and methods
 * that drive effect execution. This is base mixint for composing
 * effects because it implements all empty methods so no javascript
 * errors are thrown.
 */
CustomEffects.EmptyEffectStrategy =  {

  /**
   * List of style properties that must be stored before effect starts
   * and restored after effect stps.
   */
  affectedStyles : [],

  /**
   * List of effect that will be used in effect calcualtion,
   * These styles must be calculated carefully because effect depends on it.
   */
  calculatedStyles : [],

  /**
   * Styles that must be modified before style starts to it's correct work
   */
  modifiedStyles : {},

  /**
   * function performing style values transformation.
   * arguments are:
   * pos - number from [0,1] that show effect progress
   * stylesStruct - structure containing properties to be changed.
   */
  transition : function(pos, stylesStruct){
  },

  /**
   * When effect configuration needs to gets its own values at
   * the beginning of effect it could be done in this method.
   */
  initInternalValues : function(element){
  }
};


Object.copyAllFields = function (destination, source) {
  for (var property in source) {
    if (!Prototype.hiddenAttr(property)) {
      destination[property] = source[property];
    } 
  }
  return destination;
};

/**
 * EffectExecutor is class that performs effect according to injected effect strategy.
 */
CustomEffects.EffectExecutor = Class.create();
Object.extend( CustomEffects.EffectExecutor, {

  fps : 5,

  duration : 20,

  initialize: function(id,  options) {
    this.element = $(id);
    this._internalValues = {};
    // variables must be instance not static
    // unit values for calculated properties - useful when property
    // value must be set
    this._unitsList = {};
    // calculated properties used for providing values to effect transition
    // calculated properties should not be changed (but this is not obligatory)
    //* beacause they servers as base for property values calculation.
    this._calculatedProperties = {};
    // stores affected properties before effect starts to
    // restore it after effect complete
    this._oldProperties = {};

    // timestamp of start and stop of effect
    this._startTime = null;
    this._stopTime = null;

    this.progress = 0;

    Object.copyAllFields(this,options);
    this._interval = 1/this.fps;
    this._duration = this.duration * 1000;
    this._enableIEOpacity(this.element);
    this._doInit();
    this._start();
  },

  /**
   * return true if element is invisible false otherwise.
   * @param {Object} styles
   */
  _isHidden : function(styles){
  	return (styles.visibility == 'hidden') || (styles.display == 'none');
  },

  /**
   * Perform all initialization activities
   */
  _doInit : function(){
    var oldStyles = {};
    oldStyles.visibility = Element.getStyle(this.element, 'visibility');
    oldStyles.display = Element.getStyle(this.element, 'display');
    if(this._isHidden(oldStyles)){
      Element.setStyle(this.element, { visibility: 'hidden', display : 'block'});
    }
    this.initInternalValues(this.element);
    this.internalStart(this.element);
    this._init();
	// netfront only
    if(this._isHidden(oldStyles)){
      Element.setStyle(this.element, oldStyles);
    }
  },

  /**
   * hide input elements.Used only for NetFront because
   * all input elements cannot be hidden in by upper layer
   */
  hideInputElements : function(){
  	if (Prototype.netFront() || Prototype.netFrontMobile()) {
      var netFrontBadBehaveElementsList = ['area','button','input', 'select','textarea'];
      var nodes;
      for(var i = 0;i<netFrontBadBehaveElementsList.length;i++){
        nodes = this.element.getElementsByTagName(netFrontBadBehaveElementsList[i]);
        for (var j=0;j<nodes.length;j++) {
		  nodes[j]._oldVisibility = nodes[j].style.visibility;
          nodes[j].style.visibility = 'hidden';
        }
      }
    }
  },

  /**
   * hide input elements. Used for NetFront
   */
  showInputElements : function(){
    if (Prototype.netFront() || Prototype.netFrontMobile()) {
      var netFrontBadBehaveElementsList = ['area','button','input', 'select','textarea'];
      var nodes;
      for(var i = 0;i<netFrontBadBehaveElementsList.length;i++){
        nodes = this.element.getElementsByTagName(netFrontBadBehaveElementsList[i]);
        for (var j=0;j<nodes.length;j++) {
		  nodes[j].style.visibility = nodes[j]._oldVisibility;
        }
      }
    }
  },

  /**
   * This must be performed to enable opacity on IE
   * @param {Object} element
   */
  _enableIEOpacity : function(element){
    if(element.currentStyle && (!this.element.currentStyle.hasLayout)){
      Element.setStyle(element, {zoom: 1});
	}
  },

  /**
   * invoked on efect initialization
   * protected method
   */
  _init: function(){
    this._startTime = new Date().getTime();
    this._stopTime = this._startTime + this._duration;
    var it = this;
	// affectedStyles must be restored after effect complete
	// so their values must be stored literaly.
    this.affectedStyles.each(
      function(propertyName,index){
		var propertyValue;
		if(CustomEffects.specialElementGetMethods[propertyName]){
		  propertyValue = it.element[CustomEffects.specialElementGetMethods[propertyName]]();
		} else {
		  propertyValue = it.element.getNotNullStyle(propertyName);
		}
        it._oldProperties[propertyName] = propertyValue;

      }
    );

	// calculated styles values must be get in special way because
	// of calculation needed, so each property that is placed in
	// calculated styles must have its own getXXX method.
    this.calculatedStyles.each(
      function (propertyName, index){
        it._calculatedProperties[propertyName] =
          CustomEffects[CustomEffects.specialEffectMethods[propertyName]](it.element);
        it._unitsList[propertyName] = it._calculatedProperties[propertyName].unit;
      });
    // aply styles that are needed for correct effect working
    var modifiedStyles = this._prepareModifiedStyles();
    Element.setStyle(this.element, modifiedStyles);
  },

  /**
   * prepare styles that will be modified for correct effect working
   */
  _prepareModifiedStyles : function(){
    var localStyles = {};
    for(var property in this.modifiedStyles){
     if (!Prototype.hiddenAttr(property)) {

      var array = this.modifiedStyles[property];
      if(typeof(array) != 'string'){
        // check if property is not on do not replace list
        var value = Element.getStyle(this.element, property);
        if(array.indexOf(value) == -1){
          // this property can be changed
          // so put first element from array
          localStyles[property] = array[0];
	}
      } else {
        localStyles[property] = array;
      }
     }
    }
    return localStyles;
  },

  /**
   * call on effect stop - restore initial style values
   */
  _cleanup: function(){
    var propertyStructure = {};
    for(propertyName in this._oldProperties){
     if (!Prototype.hiddenAttr(propertyName)) {
      if(CustomEffects.specialElementSetMethods[propertyName]){
	  	// if property  needs special treatments.
		this.element[CustomEffects.specialElementSetMethods[propertyName]](
		  this._oldProperties[propertyName]);
	  } else {
	  	// if  not collect all in structure to set it using only one method call.
	    propertyStructure[propertyName] = this._oldProperties[propertyName];
	  }
     }
    }
    Element.setStyle(this.element, propertyStructure);
  },

  /**
   * Executed on effect start - do some initialization things
   * configured by efect user and configured by effect itself
   */
  _start: function(){
	//initial calculation
    var newProperties = this.transition(0.001,this._calculatedProperties);
    this._update(newProperties);
    this.element.show();
	// start executer with specified attributes and method run to be invoked
    this.executor = new UpdatedExecuter(this._run.bind(this),this._interval);
  },

  /**
   * run project as long as finish condition is not fulfilled
   */
  _run: function(){
    var currentTime = new Date().getTime();
    var progress = (currentTime-this._startTime)/(this._stopTime-this._startTime);
    if(currentTime < this._stopTime){
	  // use transition provided by EffectStrategy to calculate new properties values
      var newProperties = this.transition(progress,this._calculatedProperties);
      this._update(newProperties);
    } else {
      this.executor.stop();
      this._finish();
    }
  },

  /**
   * Update style properties that needs special treatment - has special methdos for set/get value
   * @param {Object} properties
   */
  _updateSpecialStyles : function(properties){
  	var simpleProperties = {};
    for(property in properties){
     if (!Prototype.hiddenAttr(property)) {
	  if(CustomEffects.specialElementSetMethods[property]){
		var pValue = properties[property];
		this.element[CustomEffects.specialElementSetMethods[property]](pValue);
      } else {
	  	simpleProperties[property] = properties[property];

      }
     }
    }
	return simpleProperties;
  },

  /**
   * Update calculated properties values.
   * @param {Object} properties
   */
  _update : function(properties){
    var simpleProperties = this._updateSpecialStyles(properties);
    Element.setStyle(this.element, simpleProperties);
  },

  /**
   * Perform some action after effect finish
   */
  _finish: function(){
    // internal cleanup
    this._cleanup();
    // effect defined things to do
    this.internalFinish(this.element);

    // context defined things to do - because of backwart compatibility
    // name of method is afterFinish
    this.afterFinish();
  },

  /**
   * Executed on effect finish. Uset do define context-related activity.
   * Independent on effect. Name because of backward compatibility.
   */
  afterFinish : function(){
  },

  /**
   * Executed on effect start used to perform some initialization things
   * in dynamic way, for instance from point where effect is created.
   */
  internalStart : function(element){
  },

  /**
   * Executed on effect finish. Usage similar to internalStart
   * but used for some cleanup activities from outside the effect.
   */
  internalFinish : function(element){
  }
} );


/**
 * Short way to create effects. It is different from Widget.define because of
 * different base class and in general simpler implementation.
 */
CustomEffects.createEffect = function() {
    // Create a new effect.
    var effect = Class.create();

    Object.extend(effect.prototype, CustomEffects.EmptyEffectStrategy);
    // Extend defined class with all subclasses
    // passed in arguments.
    for (var i = 0; i < arguments.length; i++) {
      //  if (!Prototype.hiddenAttr(arguments[i])) {
            Object.extend(effect.prototype, arguments[i]);
      //  }
    }
    // Return defined effect
    return effect;
};










