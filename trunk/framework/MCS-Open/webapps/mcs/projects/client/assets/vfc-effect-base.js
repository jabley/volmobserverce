/**
 * (c) Volantis Systems Ltd 2008.
 */

Effect.Transitions = {}

Effect.Transitions.linear = Prototype.K;

Effect.Transitions.sinoidal = function(pos) {
  return (((-1000.0*Math.cos(pos*Math.PI)/2.0) + 500.0)/1000.0);
}

/* ------------- core effects ------------- */

Effect.ScopedQueue = Class.create();
Object.extend(Effect.ScopedQueue.prototype, {
  initialize: function() {
    this.effects  = [];
    this.interval = null;
  },
  add: function(effect) {
    var timestamp = new Date().getTime();
    
    var position = (typeof effect.options.queue == 'string') ? 
      effect.options.queue : effect.options.queue.position;

    effect.startOn  += timestamp;
    effect.finishOn += timestamp;

    if(!effect.options.queue.limit || (this.effects.length < effect.options.queue.limit)) {
      this.effects.push(effect);
    }    
    if(!this.interval) 
      this.interval = setInterval(this.loop.bind(this), 40);
  },
  remove: function(effect) {
    var effects =  [];
    for (var i = 0; i < this.effects.length; i++) {
      if(effect != this.effects[i]) {
        effects.push(this.effects[i]);
      }
    }
    this.effects = effects;

    if(this.effects.length == 0) {
      clearInterval(this.interval);
      this.interval = null;
    }
  },
  loop: function() {
    var timePos = new Date().getTime();
    for (var i = 0; i < this.effects.length; i++) {
      this.effects[i]['loop'].apply(this.effects[i], [timePos]);
    }
  }
});


//We use only one global queue in order to not use Enumaration and $H
Effect.Queue = new Effect.ScopedQueue();

Effect.DefaultOptions = {
  transition: Effect.Transitions.sinoidal,
  duration:   1.0,   // seconds
  fps:        25.0,  // max. 25fps due to Effect.Queue implementation
  sync:       false, // true for combining
  from:       0.0,
  to:         1.0,
  delay:      0.0,
  queue:      'parallel'
}

Effect.Base = function() {};
Effect.Base.prototype = {
  position: null,
  start: function(options) {
    this.options      = Object.extend(Object.extend({},Effect.DefaultOptions), options || {});
    this.currentFrame = 0;
    this.state        = 'idle';
    this.startOn      = this.options.delay*1000;
    this.finishOn     = this.startOn + (this.options.duration*1000);
    this.event('beforeStart');
    if(!this.options.sync)
      Effect.Queue.add(this);
  },
  loop: function(timePos) {
    if(timePos >= this.startOn) {
      if(timePos >= this.finishOn) {
        this.render(1.0);
        this.cancel();
        this.event('beforeFinish');
        if(this.finish) this.finish(); 
        this.event('afterFinish');
        return;  
      }
      var pos   = (timePos - this.startOn) / (this.finishOn - this.startOn);
      var frame = Math.round(pos * this.options.fps * this.options.duration);
      if(frame > this.currentFrame) {
        this.render(pos);
        this.currentFrame = frame;
      }
    }
  },
  render: function(pos) {
    if(this.state == 'idle') {
      this.state = 'running';
      this.event('beforeSetup');
      if(this.setup) this.setup();
      this.event('afterSetup');
    }
    if(this.state == 'running') {
      if(this.options.transition) pos = this.options.transition(pos);
      pos *= (this.options.to-this.options.from);
      pos += this.options.from;
      this.position = pos;
      this.event('beforeUpdate');
      if(this.update) this.update(pos);
      this.event('afterUpdate');
    }
  },
  cancel: function() {
    if(!this.options.sync)
      Effect.Queue.remove(this);
    this.state = 'finished';
  },
  event: function(eventName) {
    if(this.options[eventName + 'Internal']) this.options[eventName + 'Internal'](this);
    if(this.options[eventName]) this.options[eventName](this);
  }
}


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

