<template>
<canvas :id="identifier" width="470px" height="470px"></canvas>
</template>

<script>
export default {
  name: 'trackMap',
  props: {
    'identifier': {
      type: String,
      require: true
    },
    'point': {
      type: Object
    }
  },
  watch: {
    point: function() {
      if (this.point != null) {
        var canvas = document.getElementById(this.identifier)
        var ctx = canvas.getContext('2d')
        ctx.fillStyle = '#498dfc'
        ctx.fillRect(this.point.x + 210, this.point.y + 210, 3, 3)
      }
    }
  }
}
</script>
