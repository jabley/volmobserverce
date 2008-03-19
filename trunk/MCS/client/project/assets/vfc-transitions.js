/**
 * (c) Volantis Systems Ltd 2006-2007. 
 */

/*------------------ Widget.EffectDesc ----------------------------*/

/**
 * EffectDesc is a complete defintion of effect
 */
Widget.EffectDesc = Class.create();
Object.extend(Widget.EffectDesc.prototype, {
  // name of effect
  name: 'none',
  // duratin in seconds
  duration : 0,
  // effect direction
  direction: null,
  // max. 25fps due to Effect.Queue implementation
  fps: 25.0,
  // custom parameters, depend on effect
  params: null,
  // additional extensions, not listed here, are allowed

  initialize: function(name, direction, duration, fps, params) {
    this.name = name;
    this.direction = direction;
    this.duration = duration;
    this.fps = fps;
    this.params = params;
  }
});


/**
 * EffectRule class represents an effect rule.
 * It contains description of effect that will be provided to
 * effect creation method. EffectDesc contains all data necessary to initialize new
 * effect and additional data used for EffectDesc list.
 *
 * There is no public API in this class because effect queueing should be not
 * visible for widgets.
 */

Widget.EffectRule = Class.create();
Object.extend(Widget.EffectRule.prototype, {

  // effect descriptor
  effectDesc: null,

  // how many times effect should be repeated 'none' for repetititon and time means infinite loop
  repetitions: 'none',

  // period of time in which effect should be repeated
  activeTime: 'none',

  // time when effect was initialized
  timeStamp: 0,

  // how many times effect has been called in current cycle
  counter: 0,


  /**
   * Initialize effect description data
   */
  initialize: function(desc, reps, time) {
    this.effectDesc = desc;
    this.repetitions = reps;
    if (time != 'none') {
      this.activeTime = time * 1000; // in millis
    } else {
      this.activeTime = 'none';
    }
    // We need to start counting time for the first effect on the list when the widget
    // is created - doing it on the first call to getXXXEffect would be too late -
    // it would not satisfy the requirement.
    // So we call reset() in effects's constructor, even if this is needed for the
    // first effect only, as other effects will be reset in nextXXXEffect()
    this.reset();
  },

  /**
   * Reset timestamp or counter.
   */
  reset: function(){
    if (this.repetitions != 'none') {
      this.counter = 0;
    } else if (this.activeTime !='none') {
      this.timeStamp = (new Date()).getTime();
    }
  },

  /**
   * returns true if effect expired and should be replaced with next in the list,
   * false otherwise.
   */
  isExpired: function(){
    var expired = false;
    if (this.repetitions != 'none') {
      // effect counter driven
      if (this.repetitions <= this.counter) {
        expired = true;
      }
    } else if(this.activeTime != 'none') {
      // effect time driven
      if (this.timeStamp + this.activeTime < (new Date()).getTime()) {
        expired = true;
      }
    }
    return expired;
  },

  /**
   * Update effectCounter if effect counter driven. If time-driven do nothing.
   */
  update: function() {
    if (this.repetitions != 'none') {
      // effect counter driven
      this.counter += 1;
    }
  }
});



/**
 * Appearable and Disappearable are mix-in classes. If a widget is derived from
 * such class, it means that the Widget contains list of rules that
 * define effects that should be performed in specified order. Role of Appearable
 * and Disappearable is to provide methods for calling proper effect in proper time.
 *
 * getNextXXXEffect returns effect that should be performed.
 */

/*------------------ Widget.Appearable ----------------------------*/

Widget.Appearable = {
  // list of effects that will be applied to this Appearable
  appRules: [new Widget.EffectRule(
    new Widget.EffectDesc('none', 1, null, 25, null), /* effectDesc */
    'none', /* reps */
    'none'  /* time */ )],

  // indicates index of the current rule
  appRuleIdx: 0,

  // move to the next rule.
  _nextAppearRule: function(){
    this.appRuleIdx = (this.appRuleIdx + 1 + this.appRules.length) % this.appRules.length;
    var rule = this.appRules[this.appRuleIdx];
    rule.reset();
    return rule;
  },

  // method accesible from outside of Appearable. Get applicable effect from list.
  // Switching to next effect is performed if necessary (if current effect expired)
  getAppearEffect: function(){
    var rule = this.appRules[this.appRuleIdx];
    if (rule.isExpired()) {
      rule = this._nextAppearRule();
    }
    rule.update();
    if (rule.isExpired()) {
      this._nextAppearRule();
    }
    return rule.effectDesc;
  }
};

/*------------------ Widget.Disappearable ----------------------------*/

Widget.Disappearable = {
  // list of effects that will be applied to Disappearable
  disRules: [new Widget.EffectRule(
    new Widget.EffectDesc('none', 1, null, 25), /* effectDesc */
    'none', /* reps */
    'none' /* time */ )],

  // indicates index of the current rule
  disRuleIdx: 0,

  _nextDisappearRule: function() {
    this.disRuleIdx = (this.disRuleIdx + 1 + this.disRules.length) % this.disRules.length;
    var effect = this.disRules[this.disRuleIdx];
    effect.reset();
    return effect;
  },

  // method accesible from outside of Disappearable. Get applicable effect from list.
  // Switching to next rule is performed if necessary (if current rule expired)
  getDisappearEffect: function(){
    var rule = this.disRules[this.disRuleIdx];
    if (rule.isExpired()) {
      rule = this._nextDisappearRule();
    }
    rule.update();
    if (rule.isExpired()) {
      this._nextDisappearRule();
    }
    return rule.effectDesc;
  }
};

/*------------------ Widget.Highlightable ----------------------------*/
Widget.Highlightable = {
  time: 1,        //time in seconds
  startColor : '#ffff99',    //lightyellow
  endColor : '#ffffff'    //white
};


/*------------------ Widget.Refreshable ----------------------------*/
// NOTE: Widget have to implement processAJAXResponse callback function

Widget.Refreshable = {
  refreshURL: '',
  refreshInterval: '',

  // This flag controls, whether the Widget using this mixin
  // is treated as using polling or not. If set to 'false',
  // the Widget.isPollingEnabled() flag will not affect it.
  usesPolling: true,

//implemenation
  refreshMode: 'PASSIVE',
  refreshIntervalId: null,

  startRefresh: function() {
    if (this.refreshInterval !== "" && this.refreshInterval !== null &&
      this.refreshURL !== "" && this.refreshURL !== null) {
      this.refreshMode = 'ACTIVE';
      this.refreshIntervalId = setInterval(this.forceUpdateOnTimeout.bind(this), this.refreshInterval * 1000);
    }
  },

  /**
   * Changes widget state to passive so no Ajax updates will be possible.
   *
   * @volantis-api-include-in PublicAPI
   */
  disableUpdates: function() {
    if(this.refreshIntervalId !== null)
    {
      clearInterval(this.refreshIntervalId);
      this.refreshIntervalId=null;
    }
    this.refreshMode = 'PASSIVE';
  },

  /**
   * Forces an AJAX update as a consequence of timeout.
   */
  forceUpdateOnTimeout: function () {
    if (!this.usesPolling || Widget.isPollingEnabled()) {
      this.forceUpdate();
    }
  },

  /**
   * Forces an AJAX update. Does nothing if there was no <widget:refresh> tag for this widget.
   *
   * @volantis-api-include-in PublicAPI
   */
  forceUpdate: function () {
    if (this.refreshMode == "ACTIVE") {

    // NOTE: that you MUST use bind in the onComplete specification, to
    // ensure that the function executes with "this" bound as intended
    // ie. from the Widget we instantiated.

      if (this.processAJAXResponse && typeof this.processAJAXResponse == 'function') {
        // The rid parameter represents unique RequestID.
        // Some malicious browsers may cache the response,
        // and this simple trick helps to avoid caching.
        this.requestId = (this.requestId === null) ? 0 : this.requestId + 1;

        var ar = new Widget.AjaxRequest(
          this.refreshURL,
          { method: 'get',
          parameters: {rid: this.requestId},
          onSuccess: this.processAJAXResponse.bind(this) } );
      } else {
        this.disableUpdates();
      }
    }
  }
}; // Refreshable

/*------------------ Effect.SlideFromSide ----------------------------*/
/*
SlideFromSide (appear effect)
  requred to have outer object with fixed size
  the only inner object appears in a slade manner from specified direction
  parameters: DOM element or element id
    common script.oculo.us parameters
    direction 'bottom', 'top', 'right', 'left'
*/
Effect.SlideFromSide = function(element, options) {
  this.element = $(element);
    var opt = Object.extend({
        direction: 'top'
      }, options || {});

  Element.cleanWhitespace(this.element);

  Element.makePositioned(this.element);
  Element.makePositioned(this.element.firstChild);
  Element.makeClipping(this.element);

  element.style.display='block';
  element.style.visibility = 'hidden';
  var dims = element.getDimensions();
  element.style.display='none';
  element.style.visibility = 'visible';

  var initialMoveX, initialMoveY;
  var moveX, moveY;

  switch (options.direction) {
  case 'top':
    initialMoveX = 0;
    initialMoveY = -dims.height;
    moveX = 0;
    moveY = dims.height;
  break;
  case 'bottom':
    initialMoveX = 0;
    initialMoveY = dims.height;
    moveX = 0;
    moveY = -dims.height;
  break;
  case 'left':
    initialMoveX = -dims.width;
    initialMoveY = 0;
    moveX= dims.width;
    moveY = 0;
  break;
  case 'right':
    initialMoveX = dims.width;
    initialMoveY = 0;
    moveX= -dims.width;
    moveY = 0;
  break;
  }

  return new Effect.Move(this.element.firstChild, {
    x: initialMoveX,
    y: initialMoveY,
    duration: 0.01,
    beforeSetup: function(effect) {
      effect.element.hide();
      effect.element.makeClipping();
      effect.element.makePositioned();
    },
    afterFinishInternal: function(effect) {
      new Effect.Move(effect.element,
          Object.extend({
            x: moveX,
            y: moveY,
            beforeSetup: function(effect) {
              effect.element.parentNode.show();
              effect.element.show();
            },
            afterFinishInternal: function(effect) {
              effect.element.undoClipping();
              effect.element.parentNode.undoClipping();
              effect.element.undoPositioned();
            }
          }, opt));
    }
  });
};

/*------------------ Effect.SlideToSide ----------------------------*/

/*
SlideToSide (disappear effect)
  requred to have outer object with fixed size
  the only inner object disappears in a slade manner into specified direction
  parameters: common script.oculo.us parameters
          direction 'bottom', 'top', 'right', 'left'
*/
Effect.SlideToSide = function(element, options) {

  this.element = $(element);
  var opt = Object.extend({
         direction: 'top'
      }, options || {});
  Element.cleanWhitespace(this.element);

  Element.makePositioned(this.element);
  Element.makePositioned(this.element.firstChild);
  Element.makeClipping(this.element);

  var dims = this.element.getDimensions();
  var childDims = this.element.firstChild.getDimensions();
  var initialMoveX, initialMoveY;
  var moveX, moveY;

  switch (opt.direction) {
  case 'top':
    moveX = 0;
    moveY = -dims.height;
    break;
  case 'bottom':
    moveX = 0;
    moveY = dims.width;
    break;
  case 'left':
    moveX= -dims.width;
    moveY = 0;
    break;
  case 'right':
    moveX= dims.width;
    moveY = 0;
    break;
  }

  return new Effect.Move(this.element.firstChild, Object.extend({
    x: moveX,
    y: moveY,

    afterFinishInternal: function(effect) {

    effect.element.hide();
    effect.element.parentNode.hide();
    effect.element.setStyle({left:'0px', top:'0px'});
    effect.element.show();
    effect.element.undoClipping();
    effect.element.undoPositioned();
    }
  }, opt));
};

/*------------------ Effect.InstantBase ----------------------------
 *
 * Base class for instant effects.
 * The only parameter for constructor is the DOM element to act upon.
 * No options accepted.
 */
Effect.InstantBase = Class.create();
Object.extend(Object.extend(Effect.InstantBase.prototype, Effect.Base.prototype), {
  initialize: function(element) {
    this.element = $(element);
    // it was this.start({duration: 0}); funtion call has only one agrument in it's declaration, but can have more and they are inside the argument table
    // starting only with duration = 0 disables other effects properties like afterFinish method.
    Object.extend(arguments[1], {duration:0} );
    this.start(arguments[1]);
  },
  update: function(position) {
  }
});


/*------------------ Effect.InstantAppear ----------------------------
 *
 * Shows element immediatly
 */
Effect.InstantAppear = Class.create();
Object.extend(Object.extend(Effect.InstantAppear.prototype, Effect.InstantBase.prototype), {
  finish: function(position) {
    this.element.show();
  }
});

/*------------------ Effect.InstantDisappear ----------------------------
 *
 * Hides element immediatly
 */
Effect.InstantDisappear = Class.create();
Object.extend(Object.extend(Effect.InstantDisappear.prototype, Effect.InstantBase.prototype), {
  finish: function(position) {
    this.element.hide();
  }
});

/*------------------ Widget.TransitionFactory ----------------------------*/
Widget.TransitionFactory = {

  // Note: this list could be moved to the server side
  appearEffects: [
    { name: "none", direction: null },
    { name: "grow", direction: "top-left"},
    { name: "grow", direction: "bottom-left"},
    { name: "grow", direction: "bottom-right"},
    { name: "grow", direction: "top-right"},
    { name: "grow", direction: "center"},
    { name: "slide", direction: "left"},
    { name: "slide", direction: "bottom"},
    { name: "slide", direction: "right"},
    { name: "slide", direction: "top"},
    { name: "wipe", direction: "top"},
    { name: "fade", direction: null },
    { name: "shake", direction: null },
    { name: "pulsate", direction: null }
  ],

  // Note: this list could be moved to the server side
  disappearEffects: [
    { name: "none", direction: null },
    { name: "shrink", direction: "top-left"},
    { name: "shrink", direction: "bottom-left"},
    { name: "shrink", direction: "bottom-right"},
    { name: "shrink", direction: "top-right"},
    { name: "shrink", direction: "center"},
    { name: "slide", direction: "left"},
    { name: "slide", direction: "bottom"},
    { name: "slide", direction: "right"},
    { name: "slide", direction: "top"},
    { name: "wipe", direction: "top"},
    { name: "fade", direction: null },
    { name: "shake", direction: null },
    { name: "pulsate", direction: null },
    { name: "puff", direction: null },
    { name: "dropout", direction: null }
  ],

  createAppearEffect: function(element, appearable, extensions) {
    var el = $(element);
    var effectDesc = appearable.getAppearEffect();

    var effect = new Widget.EffectDesc();
    // we should not modify the received effectDesc
    // so we copy its properties to a newly created object
    Object.extend(effect, effectDesc);
    // add user-specified extensions
    Object.extend(effect, extensions);

    return this._doAppear(el, effect);

  },

  createDisappearEffect: function(element, disappearable, extensions) {
    var el = $(element);
    var effectDesc = disappearable.getDisappearEffect();

    var effect = new Widget.EffectDesc();
    // we should not modify the received effectDesc
    // so we copy its properties to a newly created object
    Object.extend(effect, effectDesc);
    // add user-specified extensions
    Object.extend(effect, extensions);

    return this._doDisappear(el, effect);
  },

  _doAppear: function(el, effect) {
    switch(effect.name)
    {
      case 'none':
        return new Effect.InstantAppear(el, effect);
      case 'fade':
        return new CustomEffects.AppearEffect(el, effect);
      case 'grow':
        if(this.supportsScale()) {
          return new CustomEffects.GrowEffect(el, effect);
        } else {
          effect.direction = 'top';
          return new CustomEffects.SlideAppearEffect(el, effect);
		    }
      case 'wipe':
        if(this.supportsWipe()) {
		      return new CustomEffects.BlindDownEffect(el, effect);
        } else {
          effect.direction = 'top';
          return new CustomEffects.SlideAppearEffect(el, effect);
        }
      case 'shake':
		    return new CustomEffects.ShakeAppearEffect(el, effect);
      case 'pulsate':
		    return new CustomEffects.AppearPulsateEffect(el, effect);
      case 'slide':
        return new CustomEffects.SlideAppearEffect(el, effect);
      case 'random':
        var allowed = (effect.params) ? (effect.params.allowed) : this.appearEffects;
        this._makeRandom(effect, allowed);
        return this._doAppear(el, effect);
      default:
        // Overwrite name and direction of this effect
        // with those of the default effect, but preserve the rest,
        // in particular onFinish function in extensions
        Object.extend(effect, this.appearEffects[0]);
        return this._doAppear(el, effect);
    }
  },

  _doDisappear: function(el, effect) {
    switch(effect.name)
    {
      case 'none':
        return new Effect.InstantDisappear(el, effect);
      case 'fade':
        return new CustomEffects.FadeEffect(el, effect);
      case 'shrink':
        if(this.supportsScale()) {
          return new CustomEffects.ShrinkEffect(el, effect);
        } else {
          effect.direction = 'top';
          return new CustomEffects.SlideDisppearEffect(el, effect);
        }
      case 'puff':
		return new CustomEffects.PuffEffect(el,effect );
      case 'dropout':
        return new CustomEffects.DropOutEffect(el, effect);
      case 'wipe':
        if(this.supportsWipe()) {
		  return new CustomEffects.BlindUpEffect(el,effect);
        } else {
          effect.direction = 'top';
          return new CustomEffects.SlideDisppearEffect(el,effect);
        }
      case 'fold':
        if(this.supportsScale()) {
          return new CustomEffects.FoldEffect(el, effect);
        } else {
          effect.direction = 'top';
          return new CustomEffects.ShakeDisappearEffect(el, effect);
        }
      case 'shake':
        return new CustomEffects.ShakeDisappearEffect(el, effect);
      case 'pulsate':
		    return new CustomEffects.DisappearPulsateEffect(el, effect);
      case 'slide':
        return new CustomEffects.SlideDisppearEffect(el, effect);
      case 'random':
        var allowed = (effect.params) ? (effect.params.allowed) : this.disappearEffects;
        this._makeRandom(effect, allowed);
        return this._doDisappear(el, effect);
      default:
        // Overwrite name and direction of this effect
        // with those of the default effect, but preserve the rest,
        // in particular onFinish function in extensions
        Object.extend(effect, this.disappearEffects[0]);
        return this._doDisappear(el, effect);
    }
  }, // createDisappearEffect

  createHighlightEffect: function(element, highlightable, extension) {
    var el = $(element);
    var hl = highlightable || {};
    var ext = extension || {};
    //default disappear options
    var opt = {
      duration: hl.time,
      startcolor: hl.startColor,
      endcolor: hl.endColor,
      restorecolor: hl.endColor
    };
    return new Effect.Highlight(el, Object.extend(opt, ext));
  }, // createHighlightEffect

  /**
   * Copies, to the specified EffectDesc, data from an EffectDesc
   * randomly chosen from the list of allowed ones
   *
   * effect - EffectDesc describing the base effect
   * allowed - list of EffectDesc describing allowed effects
   */
  _makeRandom: function(effect, allowed) {
      var chosen = allowed[Math.floor(Math.random() * allowed.length)];

      // Name, direction and parameters are taken from the chosen effect
      // Duration and fps should be derived from the base effect, so we
      // leave them unmodified
      effect.name = chosen.name;
      effect.direction = (chosen.direction) ? (chosen.direction) : null;
      effect.params = (chosen.params) ? (chosen.params) : null;
  },

  // TODO: should be moved to Capabilities
  supportsScale: function() {
    return (!Prototype.operaMobile());
  },

  supportsWipe: function() {
	// it seems that wipe is working on netfront
	return (this.supportsScale());
  }
  // end of code that should be moved to capabilities

};// TransitionFactory


/**
 * An Effect Block widget.
 */
Widget.Internal.EffectBlock = Widget.define(Widget.Appearable, Widget.Disappearable,
{
  appearable: null,
  disappearable: null,

  initialize: function(id, options) {
    this.initializeWidget(id, options);

    this.status = (this.getElement().getStyle("display") == "none") ? "hidden" : "shown";

    this.duringTransition = false;

    // This flag indicates, whether hide or show was requested by the user.
    // It does not represent the actual state of the widget.
    this.hidden = (this.status == "hidden");

    // Indicate, that the default style for the element is "block".
    // This is used by Element.show() method in "prototype.js" library
    // to change the display:none style to display:block.
    this.getElement().vfcDefaultStyle = 'block';
  },

  /**
   * Shows the content, if it's not already shown.
   */
  show: function() {
    this.setHidden(false);
  },

  /**
   * Hides the content, if not already hidden.
   */
  hide: function() {
    this.setHidden(true);
  },

  canShow: function() {
    return this.hidden;
  },

  canHide: function() {
    return !this.hidden;
  },

  getStatus: function() {
    return this.status;
  },

  _setStatus: function(status) {
    if (this.status != status) {
      this.status = status;

      // Notify observers of status change
      this.notifyObservers("statusChanged");

      // Send an event named as the status:
      // "hiding", "hidden", "showing" or "shown".
      this.notifyObservers(status);
    }
  },

  isHidden: function() {
    return this.hidden;
  },

  setHidden: function(hidden) {
    // Don't know why the timer helps here, but without it some effects just fails.
    // Maybe it's due to some asynchronous aspects of the effects.
    // TODO: This is a quick-fix. I believe that this bug needs to be fixed in effects code,
    // which invokes afterFinish notifications.
    setTimeout(this.doSetHidden.bind(this, hidden), 0);
  },

  doSetHidden: function(hidden) {
    if (this.hidden != hidden) {
      this.hidden = hidden;

      if (!this.duringTransition) {
        if (this.hidden) {
          this.startDisappear();
        } else {
          this.startAppear();
        }
      }

      // Notify observers.
      this.notifyObservers('canHideChanged');
      this.notifyObservers('canShowChanged');
      this.notifyObservers('isHiddenChanged');
    }
  },

  /**
   * Starts the disappear transition.
   */
  startDisappear: function() {
    //Widget.log("Widget.Block", "Starting disappear...")

    this._setStatus("hiding");

    // Update the transition flag.
    this.duringTransition = true;

    // Run disappear transition.
    Widget.TransitionFactory.createDisappearEffect(
      this.getElement(),
      this.getDisappearable(),
      { element : this.getElement(),
        afterFinish: this.afterDisappear.bind(this)});
  },

  /**
   * Invoked after the disappear transition finishes.
   */
  afterDisappear: function() {
    //Widget.log("Widget.Block", "Disappear finished...")


    // Update the transition flag.
    this.duringTransition = false;

    // Send the 'disappeared' notification.
    this._setStatus("hidden");

    this.checkForAppear();
  },

  checkForAppear: function() {
    if (!this.hidden) {
      this.startAppear();
    }
  },

  /**
   * Start the appear transition.
   */
  startAppear: function() {
    //Widget.log("Widget.Block", "Starting appear...")

    // Update the transition flag.
    this.duringTransition = true;

    this._setStatus("showing");

    // Run appear transition
    Widget.TransitionFactory.createAppearEffect(
      this.getElement(),
      this.getAppearable(),
      { element : this.getElement(),
        afterFinish: this.afterAppear.bind(this)}
    );
  },

  /**
   * Invoked after the appear transition finishes.
   */
  afterAppear: function() {
    //Widget.log("Widget.Block", "Appear finished...")

    // Update the transition flag.
    this.duringTransition = false;

    // Sent 'appeared' notification.
    this._setStatus("shown");

    // If in the meantime this widget was requested to be hidden
    // (hidden flag is true), start the disappear effect now.
	  this.checkForDisappear();
  },

  checkForDisappear: function() {
    if (this.hidden) {
      this.startDisappear();
    }
  },

  /**
   * Returns true, if this block is currently during transition.
   */
  isDuringTransition: function() {
    return this.duringTransition;
  },

  getAppearable: function() {
    return (this.appearable === null) ? this : this.appearable;
  },

  getDisappearable: function() {
    return (this.disappearable === null) ? this : this.disappearable;
  }
});

Widget.BlockContent = Widget.define(Widget.Appearable, Widget.Disappearable,
{
  initialize: function(internalContent, options) {
    this.initializeWidget(null, options);

    this.internalContent = internalContent;

    var parentBlock = this.getOption("parentBlock");

    if (parentBlock !== undefined) {
      parentBlock.content = this;
      parentBlock.displayedContent = this;
      parentBlock.innerBlock.appearable = this;
      parentBlock.innerBlock.disappearable = this;
    }
  },

  getInternalContent: function() {
    return this.internalContent;
  }
});

Widget.Block = Widget.define(
{
  load: null,
  fetch: null,
  refresh: null,

  initialize: function(outerEffectBlock, innerEffectBlock, blockContainer, options) {
    this.initializeWidget(null, options);

    if(this.refresh !== null) {
      this.refresh.setOwner(this);
      this.refresh.startRefresh();
    }

    if(this.load !== null) {
      this.load.setOwner(this);
      if(this.load.getWhen() == 'onload') {
        this.load.execute();
      }
    }

    if(this.fetch !== null) {
      this.fetch.setOwner(this);
      if(this.fetch.getWhen() == 'onload') {
        this.fetch.execute();
      }
    }

    this.outerBlock = outerEffectBlock;
    this.innerBlock = innerEffectBlock;
    this.container = blockContainer;
    this.displayedContent = this.content;

    this.contentHidden = false;

    this.observe(this.outerBlock, "canShowChanged", "canShowChanged");
    this.observe(this.outerBlock, "canHideChanged", "canHideChanged");

    this.observe(this.outerBlock, "hiding", "hiding");
    this.observe(this.outerBlock, "hidden", "hidden");
    this.observe(this.outerBlock, "showing", "showing");
    this.observe(this.outerBlock, "shown", "shown");

    this.observe(this.outerBlock, "statusChanged", "statusChanged");

    this.observe(this.innerBlock, "hiding", "innerBlockHiding");
    this.observe(this.innerBlock, "hidden", "innerBlockHidden");
    this.observe(this.innerBlock, "showing", "innerBlockShowing");
    this.observe(this.innerBlock, "shown", "innerBlockShown");

    this.observe(this.innerBlock, "statusChanged", "contentStatusChanged");

    // Initialize initial content
    if (this.getOption("content") !== null) {
      this.setContent(this.getOption("content"));
    }

    // Define actions, properties and events
    // Part 1: Showing/hiding
    this.addAction("show");
    this.addAction("hide");
    this.addEvent("hiding");
    this.addEvent("hidden");
    this.addEvent("showing");
    this.addEvent("shown");
    this.addProperty("status");

    // Part 2: Content setting
    this.addAction("clear-content");
    this.addAction("show-content");
    this.addAction("hide-content");

    this.addProperty("content", {type: "widget"});
    this.addProperty("displayed-content", {type: "widget"});

    this.addEvent("content-hiding");
    this.addEvent("content-hidden");
    this.addEvent("content-showing");
    this.addEvent("content-shown");
    this.addProperty("content-status");

    this.addProperty("load", {type: "widget"});
    this.addProperty("fetch", {type: "widget"});
    this.addProperty("refresh", {type: "widget"});
  },

  getElement: function() {
    return this.outerBlock.getElement();
  },

  show: function() {
    this.outerBlock.show();
  },

  canShow: function() {
    return this.outerBlock.canShow();
  },

  canShowChanged: function() {
    this.notifyObservers("canShowChanged");
  },

  hide: function() {
    this.outerBlock.hide();
  },

  canHide: function() {
    return this.outerBlock.canHide();
  },

  canHideChanged: function() {
    this.notifyObservers("canHideChanged");
  },

  hiding: function() {
    this.notifyObservers("hiding");
  },

  hidden: function() {
    this.notifyObservers("hidden");
  },

  showing: function() {
    this.notifyObservers("showing");
  },

  shown: function() {
    this.notifyObservers("shown");
  },

  getStatus: function() {
    return this.outerBlock.getStatus();
  },

  statusChanged: function() {
    this.notifyObservers("statusChanged");
  },

  setContent: function(content) {
    if (this.content != content) {
      this.content = content;

      if (this.innerBlock.getStatus() == "shown") {
        // If the inner block is currently shown,
        // hide it right now.
		    this.updateInnerBlock();

      } else if (this.innerBlock.getStatus() == "hidden") {
        // If the inner block is currently hidden,
        // and displayed content is not null,
        // show it right now.
        this.updateContainer();

        this.checkForInnerBlockShow();
      }

      this.notifyObservers("contentChanged");
    }
  },

  getContent: function() {
    return this.content;
  },

  clearContent: function() {
    this.setContent(null);
  },

  hideContent: function() {
    if (!this.contentHidden) {
      this.contentHidden = true;

      this.innerBlock.hide();

      this.canHideContentChanged();
      this.canShowContentChanged();
    }
  },

  canHideContent: function() {
    return !this.contentHidden;
  },

  canHideContentChanged: function() {
    this.notifyObservers("canHideContentChanged");
  },

  showContent: function() {
    if (this.contentHidden) {
      this.contentHidden = false;

      this.checkForInnerBlockShow();

      this.canHideContentChanged();
      this.canShowContentChanged();
    }
  },

  canShowContent: function() {
    return this.contentHidden;
  },

  canShowContentChanged: function() {
    this.notifyObservers("canShowContentChanged");
  },

  getDisplayedContent: function() {
    return this.displayedContent;
  },

  getContentStatus: function() {
    if (this.displayedContent === null) {
      return "none";
    } else {
      return this.innerBlock.getStatus();
    }
  },

  contentStatusChanged: function() {
    this.notifyObservers("contentStatusChanged");
  },

  updateDisplayedContent: function() {
    if (this.displayedContent !== this.content) {
      this.displayedContent = this.content;

      this.notifyObservers("displayedContentChanged");
      this.notifyObservers("contentStatusChanged");
      this.notifyObservers("canShowContentChanged");
      this.notifyObservers("canHideContentChanged");
    }
  },

  updateInnerBlock: function() {
    if (this.content !== this.displayedContent) {
      this.innerBlock.hide();
    }
  },

  innerBlockHiding: function() {
    if (this.displayedContent !== null) {
      this.notifyObservers("contentHiding");
      this.notifyObservers("contentStatusChanged");
    }
  },

  innerBlockHidden: function() {
    if (this.displayedContent !== null) {
      this.notifyObservers("contentHidden");
      this.notifyObservers("contentStatusChanged");
    }

    this.updateContainer();

    this.checkForInnerBlockShow();
  },

  innerBlockShowing: function() {
    if (this.displayedContent !== null) {
      this.notifyObservers("contentShowing");
      this.notifyObservers("contentStatusChanged");
    }
  },

  innerBlockShown: function() {
    if (this.displayedContent !== null) {
      this.notifyObservers("contentShown");
      this.notifyObservers("contentStatusChanged");
    }

    this.updateInnerBlock();
  },

  updateContainer: function() {
    if (this.content === null) {
      this.container.setContent(null);
    } else {
      this.container.setContent(this.content.getInternalContent());
    }

    this.updateDisplayedContent();
  },

  checkForInnerBlockShow: function() {
    if (!this.contentHidden && this.displayedContent !== null) {
      this.innerBlock.appearable = this.displayedContent;
      this.innerBlock.disappearable = this.displayedContent;

      this.innerBlock.show();
    }
  },

  getFetch: function() {
    return this.fetch;
  },

  getLoad: function() {
    return this.load;
  },

  getRefresh: function() {
    return this.refresh;
  }
});


Widget.BlockFetch = Class.define(Widget.Fetch, {

	//serviceLocation: "/services/fetch",

  initialize: function(options) {
    this.initializeFetch(options);

    this.load = new Widget.BlockLoad({when: this.when, src: this.prepareRequest()});

    this.observe(this.load, "succeeded", "loadSucceeded");
    this.observe(this.load, "failed", "loadFailed");
  },

  doExecute: function() {

    this.load.setOwner(this.owner);
    this.load.setSrc(this.prepareRequest());
    this.load.execute();
  },

  prepareRequest: function(){
    var urlRequest = this.pageBase + this.service + '?src=' + escape(this.src);
    if(this.transformation !== null) {
      urlRequest += '&trans=' + escape(this.transformation);
    }
    return urlRequest;
  },

  loadSucceeded: function() {
    this.notifyObservers("succeeded");
  },

  loadFailed: function() {
    this.notifyObservers("failed");
  }
});


Widget.BlockLoad = Class.define(Widget.Load, {

  initialize: function(options) {
    this.initializeLoad(options);
  },

  doExecute: function() {
    this.createAjaxRequest(this.src);
  },

  createAjaxRequest : function(requestURL){
    //Widget.log("Widget.Load - createAjaxRequest",requestURL);
		new Widget.AjaxRequest(
			requestURL,
			{ method: 'get',
          	parameters: '',
          	onSuccess: this.processAJAXResponse.bind(this),
          	onFailure: this.processAJAXFailure.bind(this) }
		);
  },

  processAJAXFailure: function(request){
    this.notifyObservers("failed");
  },

  processAJAXResponse: function(request){

    // Get response text out of the request.
    var responseText = request.responseText;

    // Insert response content, stripping unnessecary scripts.
    this.getResponseArea().innerHTML = responseText.stripScripts();

    // Evaluate the JavaScript from the response content.
    eval(responseText.extractScripts()[0]);

    this.owner.setContent(this.blockContent);

    this.notifyObservers("succeeded");
  },

 /*
   * Creates and returns new HTML element, which will act
   * as a placeholder for response content.
   */
  createResponseArea: function() {
    var responseArea = document.createElement("div");
    var responseAreaElement = Element.extend(responseArea);
    responseAreaElement.setStyle({display: 'none'});
    responseAreaElement.setStyle({visibility: 'hidden'});
    document.body.appendChild(responseArea);
    return responseAreaElement;
  },

  /*
   * Returns a HTML element, which acts as a placeholder
   * for response content.
   */
  getResponseArea: function() {
    if (!this.responseArea) {
      this.responseArea = this.createResponseArea();
    }
    return this.responseArea;
  }

});

Widget.BlockRefresh = Class.define(Widget.Refresh, Widget.Refreshable, {

  initialize: function(options) {
    this.initializeRefresh(options);
  },

  doExecute: function() {
    //do nothing
  },

  processAJAXResponse: function(request) {
    // Get response text out of the request.
    var responseText = request.responseText;

    // Insert response content, stripping unnessecary scripts.
    this.getResponseArea().innerHTML = responseText.stripScripts();

    // Evaluate the JavaScript from the response content.
    eval(responseText.extractScripts()[0]);

    this.owner.setContent(this.blockContent);

    this.notifyObservers("succeeded");
  },

   /*
   * Creates and returns new HTML element, which will act
   * as a placeholder for response content.
   */
  createResponseArea: function() {
    var responseArea = document.createElement("div");
    var responseAreaElement = Element.extend(responseArea);
    responseAreaElement.setStyle({display: 'none'});
    responseAreaElement.setStyle({visibility: 'hidden'});
    document.body.appendChild(responseArea);
    return responseAreaElement;
  },

  /*
   * Returns a HTML element, which acts as a placeholder
   * for response content.
   */
  getResponseArea: function() {
    if (!this.responseArea) {
      this.responseArea = this.createResponseArea();
    }
    return this.responseArea;
  }

});

