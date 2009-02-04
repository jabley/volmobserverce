

/**
 * (c) Volantis Systems Ltd 2008. 
 */

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
