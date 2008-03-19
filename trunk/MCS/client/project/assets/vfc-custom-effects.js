/**
 * (c) Volantis Systems Ltd 2007. 
 */

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
    var position = element.getStyle('position');
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
    var position = element.getStyle('position');
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
  	var styleValue = element.getStyle('height');
	if((!styleValue ) || (styleValue == 'auto')){
      result.value = element.getDimensions().height;
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
  	var styleValue = element.getStyle('width');
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
  	var styleValue = element.getStyle('lineHeight');
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
  	var styleValue = element.getStyle('fontSize');
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
    var pos = element.getStyle('position');
    var dim = element.getDimensions();
    var offset = Position.positionedOffset(element);
    var left = element.getStyle('left');
    if(pos == 'absolute' || pos == 'fixed'){
      // when absolute or fixed we need to set top,left to 0
      // because outer element will have the same postion
      // so we must avoid duplicatting top/left values
      element._oldTop = { top : offset[1], left : offset[0]};
      element.setStyle({ top : 0, left : 0});
    }

    // workaround for IE - if static position is set for outer DIV then overflow:hidden not working good in IE
    // so static is change to relative with top = 0px and left = 0px
    if(Prototype.msieBrowser() && pos == 'static') {
      newDiv.setStyle({ position : 'relative', overflow : 'hidden', width : dim.width+'px', height : dim.height+'px', top : '0px', left : '0px'});
    } else {
    newDiv.setStyle({ position : pos, overflow : 'hidden', width : dim.width+'px', height : dim.height+'px', top : offset[1]+'px', left : offset[0]+'px'});
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
    var pos = element.getStyle('position');
    if(pos == 'absolute' || pos == 'fixed'){
      // when absolute or fixed we need to restore old top and left
      element.setStyle({ top : element._oldTop.top+'px', left : element._oldTop.left+'px'});
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

/**
 * Configuration for pulsate effect. This effect is simple it modifies
 * only opacity according to provided formula.
 * @param {Object} pos
 * @param {Object} stylesStruct
 */
CustomEffects.PulsateEffectStrategy = {
  affectedStyles : ['opacity'],

  calculatedStyles : ['opacity'],

  transition : function(pos, stylesStruct){
    var result = {};
    result.opacity = ((Math.floor(pos*10) % 2 === 0 ?
    (pos*10-Math.floor(pos*10)) : (1-(pos*10-Math.floor(pos*10)))));
    return result;
  }
};


/**
 * Strategy for shake effect. Only 'left' property is modified by this effect.
 * @param {Object} pos
 * @param {Object} stylesStruct
 */
CustomEffects.ShakeEffectStrategy = {
  affectedStyles : ['left','position'],

  calculatedStyles : ['left'],

  /* read only  values might be static */
  _moveRange : 20,

  _base : 0.1,

  _turnPoints : [0.1,0.3,0.5,0.7,0.9],

  transition : function(pos, stylesStruct){
    var result = {};
    var offset;
    var left = stylesStruct.left.value;
    if(pos < this._turnPoints[0]){
      offset = -(pos/this._base);
    } else if(pos< this._turnPoints[1]){
      offset = (pos-this._turnPoints[0]-this._base)/this._base;
    } else if(pos< this._turnPoints[2]){
      offset = (this._turnPoints[1]+this._base-pos)/this._base;
    } else if(pos < this._turnPoints[3]){
      offset = (pos-this._turnPoints[2]-this._base)/this._base;
    } else if(pos< this._turnPoints[4]){
      offset = (this._turnPoints[3]+this._base-pos)/this._base;
    } else {
      offset = (pos-this._turnPoints[4]-this._base)/this._base;
    }
    result.left = left + offset* this._moveRange+"px";
    return result;
  },

  modifiedStyles : { position : ['relative','absolute','fixed']}
};

/**
 * Fold effect strategy, it modifies only height and width parameters. Overflow must be
 * set to hidden at the beginning of effect.
 * @param {Object} pos
 * @param {Object} stylesStruct
 */
CustomEffects.FoldEffectStrategy = {
  affectedStyles : ['width','height', 'overflow'],

  calculatedStyles : ['width','height'],

  /* read only attributes */
  _minHeight : 0.05,

  _turnPoint : 0.5,

  transition : function(pos, stylesStruct){
    var newStruct = {};
    if(pos < this._turnPoint){
      newStruct.height = stylesStruct.height.value * (this._turnPoint-pos)/(this._turnPoint)+
      this._minHeight*pos/this._turnPoint+stylesStruct.height.unit;
    } else {
      newStruct.width = stylesStruct.width.value *(1-(pos-this._turnPoint)/(1-this._turnPoint))+stylesStruct.width.unit;
    }
    return newStruct;
  },
  modifiedStyles : { overflow : 'hidden'}
};

/**
 * Shrink effect strategy.
 * @param {Object} pos
 * @param {Object} stylesStruct
 */
CustomEffects.ShrinkEffectStrategy = {
  affectedStyles : ['top','left','width','height','lineHeight','fontSize','position', 'overflow'],

  calculatedStyles : ['top','left','width','height','fontSize','lineHeight'],

  initInternalValues : function(element){
    // _internalValues.x and _internalValues.y from [0,0.5,1]
    // direction determines starting point of transition
    // and step for each direction
    // step = 0 means that no transition is given direction
    // step = 0.5 menas that transition from border to center or from center to border
    // step = 1 transition from border to opposite border
    switch (this.direction) {
      case 'top-left':
        this._internalValues.x = this._internalValues.y = 0;
        break;
      case 'top-right':
        this._internalValues.x = 0;
        this._internalValues.y = 1;
        break;
      case 'bottom-left':
        this._internalValues.x = 1;
        this._internalValues.y = 0;
        break;
      case 'bottom-right':
        this._internalValues.x = this._internalValues.y =  1;
        break;
      case 'center':
        this._internalValues.x = this._internalValues.y = 0.5;
        break;
    }

    this._internalValues._topStep = this._internalValues.x*2;
    this._internalValues._leftStep =this._internalValues.y*2;
    //this._internalValues._startTop = this._internalValues.x*100;
    //this._internalValues._startLeft = this._internalValues.y*100;
  },

  transition : function(pos, stylesStruct){
    var newStruct = {};
    newStruct.top = stylesStruct.top.value+stylesStruct.height.value/2*pos*this._internalValues._topStep+stylesStruct.top.unit;
    newStruct.left = stylesStruct.left.value+stylesStruct.width.value/2*pos*this._internalValues._leftStep+stylesStruct.left.unit;
    newStruct.width = stylesStruct.width.value * (1-pos)+stylesStruct.width.unit;
    newStruct.height = stylesStruct.height.value * (1-pos)+stylesStruct.height.unit;

    newStruct.fontSize = stylesStruct.fontSize.value  * (1-pos)+stylesStruct.fontSize.unit;
    newStruct.lineHeight = stylesStruct.lineHeight.value  * (1-pos)+stylesStruct.lineHeight.unit;

    return newStruct;
  },

  modifiedStyles : { position : ['relative','absolute','fixed'], overflow : 'hidden'}

};

/**
 * BlindDown effect - only height is calculated
 */
CustomEffects.BlindDownEffectStrategy = {

  affectedStyles : ['height','overflow'],

  calculatedStyles : ['height'],

  initInternalValues : function(element){
  },

  transition : function(pos, stylesStruct){
    var newStruct = {};
    newStruct.height= stylesStruct.height.value*pos+stylesStruct.height.unit;
    return newStruct;
  },

  modifiedStyles : { overflow :'hidden'}
};

/**
 * Base strategy for all direction of Slide. Effect is configurable by _offset and direction
 * attributes which determines parameters used to calculation.
 */
CustomEffects.SlideEffectStrategy = {

  affectedStyles : ['top','left','position'],

  calculatedStyles : ['top','left'],

  /**
   * Specify if effect is appear or disappear
   * appear: _offset = 1; disappear: _offset = -1;
   */
  _offset : 1,

  initInternalValues : function(element){
    // step from [-1,1]
    this._internalValues._leftStep = 0;
    this._internalValues._topStep = 0;
    // direction is second parameter that determines transition calculations.
    switch (this.direction) {
      case 'left':
        this._internalValues._leftStep = 1;
        break;
      case 'right':
        this._internalValues._leftStep = -1;
        break;
      case 'bottom':
        this._internalValues._topStep = -1;
        break;
      case 'top':
	this._internalValues._topStep = 1;
        break;
    }

    var dims = element.getDimensions();
    this._internalValues.height = dims.height;
    this._internalValues.width = dims.width;

    if(this._offset == -1){
      // reverse direction
      this._internalValues._offsetLeft = this._internalValues._offsetTop = 0;
    } else {
      this._internalValues._offsetLeft = -1*this._offset*this._internalValues.width*this._internalValues._leftStep;
      this._internalValues._offsetTop = -1*this._offset*this._internalValues.height*this._internalValues._topStep;
    }
    this._internalValues._topStep = this._internalValues._topStep * this._offset;
    this._internalValues._leftStep = this._internalValues._leftStep * this._offset;
  },

  transition : function(pos, stylesStruct){
    var newStruct = {};
    newStruct.top = stylesStruct.top.value+this._internalValues._offsetTop+this._internalValues._topStep*this._internalValues.height*pos+stylesStruct.top.unit;
    newStruct.left = stylesStruct.left.value+this._internalValues._offsetLeft+this._internalValues._leftStep*this._internalValues.width*pos+stylesStruct.left.unit;
    return newStruct;
  },

  modifiedStyles : { position : ['relative','absolute','fixed']}

};

/**
 * BlidnUp strategy - only height is modified.
 */
CustomEffects.BlindUpEffectStrategy = {

  affectedStyles : ['height','overflow'],

  calculatedStyles : ['height'],

  initInternalValues : function(element){
  },

  transition : function(pos, stylesStruct){
    var newStruct = {};
	newStruct.height= stylesStruct.height.value*(1-pos)+''+stylesStruct.height.unit;
    return newStruct;
  },

  modifiedStyles : { overflow :'hidden'}
};

/**
 * Strategy for appear effect.
 * @param {Object} pos
 * @param {Object} stylesStruct
 */
CustomEffects.AppearEffectStrategy = {

  affectedStyles : ['opacity'],

  calculatedStyles : ['opacity'],

  transition : function(pos, stylesStruct){
    var newStruct = {};
    newStruct.opacity = pos;
    return newStruct;
  },

  modifiedStyles : {}
};

/**
 * Fade effect strategy
 * @param {Object} pos
 * @param {Object} stylesStruct
 */
CustomEffects.FadeEffectStrategy = {

  affectedStyles : ['opacity'],

  calculatedStyles : ['opacity'],

  transition : function(pos, stylesStruct){
    var newStruct = {};
    newStruct.opacity = 1-pos;
    return newStruct;
  },

  modifiedStyles : {}
};

/**
 * PuffEffect strategy
 */
CustomEffects.PuffEffectStrategy = {

  affectedStyles : ['top','left','width','height','lineHeight','fontSize','position','opacity'],

  calculatedStyles : ['top','left','fontSize','lineHeight','opacity'],

  initInternalValues : function(element){
    // only for puff we need different way of height/width calculation
    var dims = element.getDimensions();
    this._internalValues.height = dims.height;
    this._internalValues.width = dims.width;
  },

  transition : function(pos, stylesStruct){
    var newStruct = {};
    newStruct.top = stylesStruct.top.value-pos*this._internalValues.height/2+"px";
    newStruct.left = stylesStruct.left.value-pos*this._internalValues.width/2+"px";
    newStruct.height = this._internalValues.height*(1+pos)+"px";
    newStruct.width= this._internalValues.width*(1+pos)+"px";
    newStruct.lineHeight = stylesStruct.lineHeight.value * (1+pos)+stylesStruct.lineHeight.unit;
    newStruct.fontSize = stylesStruct.fontSize.value  *(1+pos)+stylesStruct.fontSize.unit;
    newStruct.opacity = 1-pos;
    return newStruct;
  },

  modifiedStyles : { position : 'absolute', height : 'auto', width : 'auto'}

};

/**
 * GrowEffect strategy
 */
CustomEffects.GrowEffectStrategy = {

  affectedStyles : ['top','left','width','height','lineHeight','fontSize','position','overflow'],

  calculatedStyles : ['top','left','width','height','fontSize','lineHeight'],

  initInternalValues : function(element){
    // step from [0,1,2]
    switch (this.direction) {
      case 'top-left':
        this._internalValues.x = this._internalValues.y = 0;
        break;
      case 'top-right':
        this._internalValues.x = 1;
        this._internalValues.y = 0;
        break;
      case 'bottom-left':
        this._internalValues.x = 0;
        this._internalValues.y = 1;
        break;
      case 'bottom-right':
        this._internalValues.x = this._internalValues.y =  1;
        break;
      case 'center':
        this._internalValues.x = this._internalValues.y = 0.5;
       break;
    }
    this._internalValues._topStep = this._internalValues.y*2;
    this._internalValues._leftStep =this._internalValues.x*2;
  },

  transition : function(pos, stylesStruct){
    var newStruct = {};
    newStruct.top = stylesStruct.top.value+this._internalValues.y*stylesStruct.height.value-stylesStruct.height.value/2*pos*this._internalValues._topStep+stylesStruct.top.unit;
    newStruct.left = stylesStruct.left.value+this._internalValues.x*stylesStruct.width.value-stylesStruct.width.value/2*pos*this._internalValues._leftStep+stylesStruct.left.unit;
    newStruct.width = stylesStruct.width.value * pos+stylesStruct.width.unit;
    newStruct.height = stylesStruct.height.value * pos+stylesStruct.height.unit;
    newStruct.lineHeight = stylesStruct.lineHeight.value * pos+stylesStruct.lineHeight.unit;
    newStruct.fontSize = stylesStruct.fontSize.value  * pos+stylesStruct.fontSize.unit;
    return newStruct;
  },

  modifiedStyles : {  position : ['relative','absolute','fixed'], overflow : 'hidden'}

};

/**
 * DropOUt effect strategy
 * @param {Object} pos
 * @param {Object} stylesStruct
 */
CustomEffects.DropOutEffectStrategy = {

  affectedStyles : ['top','position', 'opacity'],

  calculatedStyles : ['height'],

  transition : function(pos, stylesStruct){
    var newStruct = {};
    newStruct.top = stylesStruct.height.value*pos+stylesStruct.height.unit;
    newStruct.opacity = 1-pos;
    return newStruct;
  },

  modifiedStyles : { position : ['relative','absolute','fixed']}

};

Object.copyAllFields = function (destination, source) {
  for (var property in source) {
    destination[property] = source[property];
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
    oldStyles.visibility = this.element.getStyle('visibility');
    oldStyles.display = this.element.getStyle('display');
    if(this._isHidden(oldStyles)){
      this.element.setStyle({ visibility: 'hidden', display : 'block'});
    }
    this.initInternalValues(this.element);
    this.internalStart(this.element);
    this._init();
	// netfront only
    if(this._isHidden(oldStyles)){
      this.element.setStyle(oldStyles);
    }
  },

  /**
   * hide input elements.Used only for NetFront because
   * all input elements cannot be hidden in by upper layer
   */
  hideInputElements : function(){
  	if (Prototype.netFront()) {
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
    if (Prototype.netFront()) {
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
      element.setStyle({zoom: 1});
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
    this.element.setStyle(modifiedStyles);
  },

  /**
   * prepare styles that will be modified for correct effect working
   */
  _prepareModifiedStyles : function(){
    var localStyles = {};
    for(var property in this.modifiedStyles){
      var array = this.modifiedStyles[property];
      if(typeof(array) != 'string'){
        // check if property is not on do not replace list
        var value = this.element.getStyle(property);
        if(array.indexOf(value) == -1){
          // this property can be changed
          // so put first element from array
          localStyles[property] = array[0];
	}
      } else {
        localStyles[property] = array;
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
      if(CustomEffects.specialElementSetMethods[propertyName]){
	  	// if property  needs special treatments.
		this.element[CustomEffects.specialElementSetMethods[propertyName]](
		  this._oldProperties[propertyName]);
	  } else {
	  	// if  not collect all in structure to set it using only one method call.
	    propertyStructure[propertyName] = this._oldProperties[propertyName];
	  }
    }
    this.element.setStyle(propertyStructure);
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
	  if(CustomEffects.specialElementSetMethods[property]){
		var pValue = properties[property];
		this.element[CustomEffects.specialElementSetMethods[property]](pValue);
      } else {
	  	simpleProperties[property] = properties[property];
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
    this.element.setStyle(simpleProperties);
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
        Object.extend(effect.prototype, arguments[i]);
    }
    // Return defined effect
    return effect;
};


/**
 * Appear pulsate effect composed form PulsateEffect configuration and implementation
 * of internalStart and internalFinish methods.
 */
CustomEffects.AppearPulsateEffect = CustomEffects.createEffect(CustomEffects.PulsateEffectStrategy, CustomEffects.EffectExecutor,
{
  internalStart: function(element){
    element.setOpacity(0.9999999);
  },

  /**
   * Show element after effect.
   * @param {Object} element
   */
  internalFinish: function(element){
    element.show();
  }
});


/**
 * Disappear version of pulsate effect. Hide element after effect finish.
 */
CustomEffects.DisappearPulsateEffect = CustomEffects.createEffect(CustomEffects.PulsateEffectStrategy, CustomEffects.EffectExecutor,
{
  internalStart: function(element){
    element.setOpacity(0.9999999);
  },

  /**
   * Hide element after effect.
   * @param {Object} element
   */
  internalFinish: function(element){
    element.hide();
  }
});

CustomEffects.ShrinkEffect = CustomEffects.createEffect(CustomEffects.ShrinkEffectStrategy, CustomEffects.EffectExecutor,
{
  internalStart: function(element){
    this.hideInputElements();
  },

  internalFinish: function(element){
    this.showInputElements();
    element.hide();
  }
});

CustomEffects.GrowEffect = 	CustomEffects.createEffect(CustomEffects.GrowEffectStrategy, CustomEffects.EffectExecutor,{

  internalStart: function(element){
  	this.hideInputElements();
  },

  internalFinish: function(element){
	this.showInputElements();
  }
});


CustomEffects.PuffEffect = CustomEffects.createEffect(CustomEffects.PuffEffectStrategy, CustomEffects.EffectExecutor,
{
  internalStart: function(element){
    element.setOpacity(0.9999999);
  },

  internalFinish: function(element){
    element.hide();
  }
});

CustomEffects.ShakeDisappearEffect = CustomEffects.createEffect(CustomEffects.ShakeEffectStrategy,CustomEffects.EffectExecutor,
{
  internalFinish: function(element){
    element.hide();
  }
});

CustomEffects.ShakeAppearEffect = CustomEffects.createEffect(CustomEffects.ShakeEffectStrategy, CustomEffects.EffectExecutor);


CustomEffects.BlindDownEffect = 	CustomEffects.createEffect(CustomEffects.BlindDownEffectStrategy, CustomEffects.EffectExecutor,
{
  internalStart: function(element){
    element.show();
    this.hideInputElements();
  },

  internalFinish: function(element){
    this.showInputElements();
  }
});

CustomEffects.BlindUpEffect = CustomEffects.createEffect(CustomEffects.BlindUpEffectStrategy, CustomEffects.EffectExecutor,
{
  internalStart: function(element){
  	this.hideInputElements();
  },

  internalFinish: function(element){
	element.hide();
	this.showInputElements();
  }
});

CustomEffects.SlideAppearEffect 	= CustomEffects.createEffect(CustomEffects.SlideEffectStrategy, CustomEffects.EffectExecutor,
{
  _offset  : 1,

  _hidenDiv : null,

  internalStart: function(element){
    var parent = element.parentNode;

    if(Prototype.operaMobile()){
      // add transparent div at effect start to force
      // refresh  view after effect finish
      // to fix Opera refresh bug
      this._hidenDiv = document.createElement('div');
      this._hidenDiv = $(this._hidenDiv);
      this._hidenDiv.setStyle({height : '200%', width : '200%',
        backgroundColor : 'transparent',position : 'absolute', top : '0', left : '0'});
      parent.insertBefore(this._hidenDiv,element);
    }
    var newDiv = CustomEffects._createOuterDiv(element);
    parent.insertBefore(newDiv,element);
    newDiv.appendChild(element);
  },

  internalFinish: function(element){
    CustomEffects._removeOuterDiv(element);
    if(Prototype.operaMobile()){
      // remove transparent div at effect finish to force
      // refresh  view after effect finish
      // to fix Opera refresh bug
      var mainElement = element.parentNode;
      mainElement.removeChild(this._hidenDiv);
    }
  }
});


CustomEffects.SlideDisppearEffect = CustomEffects.createEffect(CustomEffects.SlideEffectStrategy, CustomEffects.EffectExecutor,
{
  _offset  : -1,

  _hidenDiv : null,

  internalStart: function(element){
  	var parent = element.parentNode;

	if(Prototype.operaMobile()){
      // add transparent div at effect start to force
	  // refresh  view after effect finish
	  // to fix Opera refresh bug
      this._hidenDiv = document.createElement('div');
      this._hidenDiv = $(this._hidenDiv);
	  this._hidenDiv.setStyle({height : '200%', width : '200%',
	     backgroundColor : 'transparent',position : 'absolute', top : '0', left : '0'});
	   parent.insertBefore(this._hidenDiv,element);
	}else if(Prototype.operaPC()){
      // add transparent div at effect start to force
      // refresh  view after effect finish
      // to fix Opera PC refresh bug
      this._hidenDiv = document.createElement('div');
      this._hidenDiv = $(this._hidenDiv);
      this._hidenDiv.setStyle({height : '110%', width : '10%',
        backgroundColor : 'transparent',position : 'fixed', top : '0', left : '0'});
      parent.insertBefore(this._hidenDiv,element);
    }

	var newDiv = CustomEffects._createOuterDiv(element);
 	newDiv = $(newDiv);
  // workaround for IE - if static position is set for outer DIV then overflow:hidden not working good in IE
  // so static is change to relative with top = 0px and left = 0px
  if(Prototype.msieBrowser() && element.getStyle('position') == 'static') {
    newDiv.setStyle({ position : 'relative', left: '0px', top: '0px'});
  } else {
        newDiv.setStyle({ position : element.getStyle('position')});
  }
	parent.insertBefore(newDiv,element);
	newDiv.appendChild(element);
	newDiv.setStyle({overflow : 'hidden'});
  },

  internalFinish: function(element){
    element.hide();
    CustomEffects._removeOuterDiv(element);
    if(Prototype.operaMobile() || Prototype.operaPC()){
      // remove transparent div at effect finish to force
      // refresh  view after effect finish
      // to fix Opera refresh bug
      var mainElement = element.parentNode;
      mainElement.removeChild(this._hidenDiv);
    }
  }
});

CustomEffects.FoldEffect = CustomEffects.createEffect(CustomEffects.FoldEffectStrategy,CustomEffects.EffectExecutor,
{

  internalStart: function(element){
  	this.hideInputElements();
  },

  internalFinish: function(element){
	this.showInputElements();
	element.hide();
  }
});

CustomEffects.DropOutEffect = CustomEffects.createEffect(CustomEffects.DropOutEffectStrategy,CustomEffects.EffectExecutor,
{
  internalStart: function(element){
  	element.setOpacity(0.9999999);
  },

  internalFinish: function(element){
    element.hide();
  }
});

CustomEffects.FadeEffect = CustomEffects.createEffect(CustomEffects.FadeEffectStrategy,CustomEffects.EffectExecutor,
{
  internalStart: function(element){
    element.setOpacity(0.9999999);
  },

  internalFinish: function(element){
    element.hide();
  }
});

CustomEffects.AppearEffect = CustomEffects.createEffect(CustomEffects.AppearEffectStrategy,CustomEffects.EffectExecutor,
{
  internalStart: function(element){
    element.setOpacity(0.9999999);
  }
});
