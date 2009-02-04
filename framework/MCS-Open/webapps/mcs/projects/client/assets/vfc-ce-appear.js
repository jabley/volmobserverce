/**
 * (c) Volantis Systems Ltd 2008. 
 */

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


CustomEffects.AppearEffect = CustomEffects.createEffect(CustomEffects.AppearEffectStrategy,CustomEffects.EffectExecutor,
{
  internalStart: function(element){
    element.setOpacity(0.9999999);
  }
});