<template>
<v-container>
  <main>
    <v-content>
      <v-container grid-list-md text-xs-center fluid>
        <v-layout row wrap fill-height>
          <v-flex d-flex xs6 sm12 md4 fill-height>
            <v-layout row wrap>

              <v-flex d-flex>
                <v-card>
                  <v-card-text>
                    <div id="speed" style="font-size: 1.5em;">{{ liveData.speed }} kmh</div>
                  </v-card-text>
                </v-card>
              </v-flex>

              <v-flex d-flex>
                <v-card>
                  <v-card-text>
                    <div id="gear" style="font-size: 1.5em;">Gear: {{ liveData.gear }}</div>
                    <div>
                      <gauge identifier="speed-gauge" :min="0 " :max="13000" :value="liveData.revs" title="Revs" :levelColors="['#008000',  '#F27C07', '#ff0000']" :hideValue="0 " />
                    </div>
                  </v-card-text>
                </v-card>
              </v-flex>

              <v-flex d-flex>
                <v-card>
                  <v-card-text>
                    <table>
                      <tr>
                        <td>
                          <gauge identifier="throttle-gauge " :min="0 " :max="1 " :value="liveData.throttle " :levelColors="[ '#008000'] " title="Throttle " />
                        </td>
                        <td>
                          <gauge identifier="brake-gauge " :min="0 " :max="1 " :value="liveData.break " :levelColors="[ '#871919'] " title="Brake " />
                        </td>
                      </tr>
                    </table>
                  </v-card-text>
                </v-card>
              </v-flex>

              <v-flex d-flex>
                <v-card md12>
                  <v-card-text>
                    <table width="100%" style="margin: 0px; width: 100%;">
                      <tr>
                        <td>Tyre:</td>
                        <td>{{ carData.tyre.compoundName }}</td>
                      </tr>
                      <tr>
                        <td>Car</td>
                        <td>{{ carData.team.teamName }}</td>
                      </tr>
                    </table>
                    </table>
                  </v-card-text>
                </v-card>
              </v-flex>

              <v-flex d-flex>
                <v-card>
                  <v-card-text>
                    <table width="100%" style="margin: 0px; ">
                      <tr>
                        <th>S1</th>
                        <th>S2</th>
                        <th>S3</th>
                        <th>Lap</th>
                      </tr>
                      <tr>
                        <td id="sector_1 ">{{ lapData.sector1 | formatDuration }}</td>
                        <td id="sector_2 ">{{ lapData.sector2 | formatDuration }}</td>
                        <td id="sector_3 ">{{ lapData.sector3 | formatDuration }}</td>
                        <td id="lap ">{{ lapData.lap | formatDuration }}</td>
                      </tr>
                    </table>
                  </v-card-text>
                </v-card>
              </v-flex>


            </v-layout>
          </v-flex>

          <v-flex md8>
            <v-card>
              <v-card-title primary-title>
                <div>
                  <h3 class="headline mb-0">Track</h3>
                </div>
                <v-spacer/> {{ liveData.lapTime | formatDuration }}
              </v-card-title>
              <v-card-text>
                <trackMap identifier="track" :point="point" />
              </v-card-text>
            </v-card>
          </v-flex>

        </v-layout>
      </v-container>
    </v-content>
  </main>
</v-container>
</template>

<script>
import Gauge from '@/components/Gauge/Gauge'
import TrackMap from '@/components/Track/Track'
import moment from 'moment'
import 'moment-duration-format'

export default {
  components: {
    Gauge,
    TrackMap
  },
  data() {
    return {
      liveData: {
        revs: 1.0
      },
      point: {},
      carData: {
        tyre: {},
        team: {},
        engine: {}
      },
      lapData: {}
    }
  },
  filters: {
    formatDuration: function(value) {
      if (value) {
        return moment.duration(value * 1000).format('m:ss:SSS')
      }
    }
  },
  mounted() {
    var socket = new WebSocket(process.env.TELEMETRY_WS)

    socket.onopen = () => {
      console.log('WebSocket is open')
      socket.send(localStorage.getItem('oauth_token'))
    }

    socket.onmessage = (message) => {
      var json = JSON.parse(message.data)
      if (json.dataType === 'LiveData') {
        this.liveData = json
        this.point = {
          x: json.x,
          y: json.y
        }
      } else if (json.dataType === 'LapData') {
        this.lapData = json
      } else if (json.dataType === 'CarConfiguration') {
        this.carData = json
      }
    }
  }
}
</script>
