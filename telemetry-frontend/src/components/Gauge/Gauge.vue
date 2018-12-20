<template>
<div :id="identifier"></div>
</template>

<script>
import JustGage from 'justgage'

export default {
  name: 'gauge',
  props: {
    'identifier': {
      type: String,
      require: true
    },
    'min': {
      type: Number,
      require: true
    },
    'max': {
      type: Number,
      require: true
    },
    'title': {
      type: String,
      default: 'Gauge'
    },
    'value': {
      type: Number,
      default: 0
    },
    'hideMinMax': {
      type: Number,
      default: 1
    },
    'hideValue': {
      type: Number,
      default: 1
    },
    'levelColors': {
      type: Array,
      default: function() {
        return ['#a9d70b', '#F27C07', '#ff0000']
      }
    }
  },
  data() {
    return {
      gauge: {}
    }
  },
  methods: {},
  mounted() {
    this.gauge = new JustGage({
      id: this.identifier,
      value: this.value,
      min: this.min,
      max: this.max,
      label: this.title,
      levelColors: this.levelColors,
      startAnimationTime: 20,
      startAnimationType: 'linear',
      refreshAnimationTime: 20,
      refreshAnimationType: 'linear',
      hideMinMax: this.hideMinMax === 1,
      hideValue: this.hideValue === 1,
      relativeGaugeSize: true,
      pointer: true
    })
  },
  watch: {
    value: function() {
      if (this.gauge != null) {
        this.gauge.refresh(this.value)
      }
    }
  }
}
</script>
