<template>
<v-container grid-list-md text-xs-center fluid>
  <v-layout row>
    <v-flex xs3>
      <v-card>
        <v-card-title primary-title>
          <div>
            <h3 class="headline mb-0">Live Data</h3>
          </div>
        </v-card-title>
        <v-card-text>
          <div id="speed" style="font-size: 25px; width: 200px; margin: 0px auto; margin-bottom: 30px;">{{ liveData.speed }}</div>
          <div>
            <div id="gauge" class="200x160px"></div>
            <div id="gauge-throttle" class="50x40px"></div>
            <div id="gauge-break" class="50x40px"></div>
          </div>
          <div id="gear" style="font-size: 30px; width: 200px; margin: 0px auto; margin-top: 30px;">{{ liveData.gear }}</div>
        </v-card-text>
      </v-card>
    </v-flex>

    <v-flex xs3>
      <v-card>
        <v-card-title primary-title>
          <div>
            <h3 class="headline mb-0">Car</h3>
          </div>
        </v-card-title>
        <v-card-text>
          <div id="tyre_compound" style="font-size: 30px; width: 200px; margin: 0px auto; margin-top: 30px;">{{ carData.tyre.compoundName }}</div>
          <div id="team" style="font-size: 30px; width: 200px; margin: 0px auto; margin-top: 30px;">{{ carData.team.teamName }}</div>
        </v-card-text>
      </v-card>
    </v-flex>

    <v-flex xs3>
      <v-card>
        <v-card-title primary-title>
          <div>
            <h3 class="headline mb-0">Lap Data</h3>
          </div>
        </v-card-title>
        <v-card-text>
          <table>
            <tr>
              <td>Sector 1: </td>
              <td id="sector_1">{{ lapData.sector1 }}</td>
            </tr>
            <tr>
              <td>Sector 2: </td>
              <td id="sector_2">{{ lapData.sector2 }}</td>
            </tr>
            <tr>
              <td>Lap: </td>
              <td id="lap">{{ lapData.lap }}</td>
            </tr>
          </table>
        </v-card-text>
      </v-card>
    </v-flex>
  </v-layout>

  <v-layout row>
    <v-flex xs12>
      <v-card>
        <v-card-title primary-title>
          <div>
            <h3 class="headline mb-0">Track</h3>
          </div>
        </v-card-title>
        <v-card-text>
          <canvas id="track" style="border: 1px solid black;" width="500px" height="500px"></canvas>
        </v-card-text>
      </v-card>
    </v-flex>

  </v-layout>
</v-container>
</template>

<script>
export default {
  data() {
    return {
      liveData: {},
      carData: {
        tyre: {},
        team: {},
        engine: {}
      },
      lapData: {}
    }
  },
  mounted() {
    var socket = new WebSocket('ws://localhost:8081/ws')

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
h1,
h2 {
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
