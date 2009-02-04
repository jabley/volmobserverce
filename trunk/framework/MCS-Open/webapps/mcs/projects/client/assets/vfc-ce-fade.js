/**
 * (c) Volantis Systems Ltd 2008. 
 */


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

CustomEffects.FadeEffect = CustomEffects.createEffect(CustomEffects.FadeEffectStrategy,CustomEffects.EffectExecutor,
{
  internalStart: function(element){
    element.setOpacity(0.9999999);
  },

  internalFinish: function(element){
    element.hide();
  }
});
