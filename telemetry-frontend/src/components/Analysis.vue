<template>
<v-container>
  <main>
    <v-content>
      <v-container grid-list-md text-xs-center fluid>

        <v-layout row wrap fill-height>
          <v-flex d-flex xs6 sm12 md16 fill-height>
            <v-layout row wrap>
              <v-flex d-flex>
                <v-card>
                  <v-card-title>
                    Analysis
                  </v-card-title>
                  <v-card-text>
                    <table style="width: 100%">
                      <tr>
                        <th>Track</th>
                        <th>Lap Time</th>
                        <th>Sector 1</th>
                        <th>Sector 2</th>
                        <th>Sector 3</th>
                      </tr>
                      <tr v-for="item in lapTimes">
                        <td>{{item.track}}</td>
                        <td>{{item.lap | formatDuration }}</td>
                        <td>{{item.s1 | formatDuration }}</td>
                        <td>{{item.s2 | formatDuration }}</td>
                        <td>{{item.s3 | formatDuration }}</td>
                      </tr>
                    </table>
                  </v-card-text>
                </v-card>
              </v-flex>
            </v-layout>
          </v-flex>
        </v-layout>

      </v-container>
    </v-content>
  </main>
</v-container>
</template>

<script>
import moment from 'moment'
import 'moment-duration-format'
import client from '../api/client.js'

export default {
  data() {
    return {
      lapTimes: {}
    }
  },
  methods: {
    getLapTimes() {
      client.getLapTimes(this).then(function(response) {
          this.lapTimes = response.data
        },
        function(error) {
          console.log(error)
        })
    }
  },
  created() {
    this.getLapTimes()
  },
  filters: {
    formatDuration: function(value) {
      if (value) {
        return moment.duration(value).format('m:ss:SSS')
      }
    }
  }
}
</script>
