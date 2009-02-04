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










