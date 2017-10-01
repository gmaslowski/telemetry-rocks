<template>
  <div id="content" style="font-family: 'Orbitron', sans-serif; text-align: center; margin-left: auto;margin-right: auto;">
      <table>
          <tr>
              <td>
                  <div id="speed" style="font-size: 25px; width: 200px; margin: 0px auto; margin-bottom: 30px;">{{ liveData.speed }}</div>
                  <div>
                      <div id="gauge" class="200x160px"></div>
                      <div id="gauge-throttle" class="50x40px"></div>
                      <div id="gauge-break" class="50x40px"></div>
                  </div>
                  <div id="gear" style="font-size: 30px; width: 200px; margin: 0px auto; margin-top: 30px;">{{ liveData.gear }}</div>
              </td>
              <td>
                  <div id="tyre_compound"
                       style="font-size: 30px; width: 200px; margin: 0px auto; margin-top: 30px;">{{ carData.tyre.compoundName }}</div>
                  <div id="team" style="font-size: 30px; width: 200px; margin: 0px auto; margin-top: 30px;">{{ carData.team.teamName }}</div>
              </td>
              <td>
                  <table>
                      <tr>
                          <td>Sector 1: </td>
                          <td id="sector_1" >{{ lapData.sector1 }}</td>
                      </tr>
                      <tr>
                          <td>Sector 2: </td>
                          <td id="sector_2" >{{ lapData.sector2 }}</td>
                      </tr>
                      <tr>
                          <td>Lap: </td>
                          <td id="lap" >{{ lapData.lap }}</td>
                      </tr>
                  </table>
              </td>
              <td>
                  <canvas id="track" style="border: 1px solid black;" width="500px" height="500px"></canvas>
              </td>
          </tr>
      </table>

  </div>

</template>

<script>
export default {
  data () {
    return {
      liveData: {},
      carData: { tyre: {}, team: {}, engine: {} },
      lapData: {}
    }
  },
  mounted () {
    var socket = new WebSocket('ws://localhost:9000/ws')
    socket.onopen = () => {
      console.log('WebSocket is open')
    }
    socket.onmessage = (message) => {
      var json = JSON.parse(message.data)
      if (json.dataType === 'LiveData') {
        this.liveData = json
      } else if (json.dataType === 'LapData') {
        this.lapData = json
      } else if (json.dataType === 'CarConfiguration') {
        this.carData = json
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}
</style>
