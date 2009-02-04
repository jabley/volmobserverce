


/**
 * (c) Volantis Systems Ltd 2008. 
 */

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

CustomEffects.ShakeDisappearEffect = CustomEffects.createEffect(CustomEffects.ShakeEffectStrategy,CustomEffects.EffectExecutor,
{
  internalFinish: function(element){
    element.hide();
  }
});

CustomEffects.ShakeAppearEffect = CustomEffects.createEffect(CustomEffects.ShakeEffectStrategy, CustomEffects.EffectExecutor);
