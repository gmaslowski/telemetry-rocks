<template>
<v-app dark="true">
  <v-navigation-drawer clipped persistent v-model="drawer" enable-resize-watcher app class="blue-grey darken-3">
    <v-list dense color="blue-grey darken-1">
      <v-list-tile @click="">
        <v-list-tile-action>
          <v-icon>dashboard</v-icon>
        </v-list-tile-action>
        <v-list-tile-content>
          <v-list-tile-title>Live Data</v-list-tile-title>
        </v-list-tile-content>
      </v-list-tile>
      <v-list-tile @click="">
        <v-list-tile-action>
          <v-icon>settings</v-icon>
        </v-list-tile-action>
        <v-list-tile-content>
          <v-list-tile-title>Lap Comparison</v-list-tile-title>
        </v-list-tile-content>
      </v-list-tile>
    </v-list>
  </v-navigation-drawer>
  <v-toolbar app class="blue-grey darken-2">F1 Telemetry Dashboard - LiveData</v-toolbar>
  <main class="blue-grey">
    <v-content>
      <v-container fluid>
        <router-view></router-view>
      </v-container>
    </v-content>
  </main>
  <v-footer app color="blue-grey darken-3">Copyright @ 2017</v-footer>
</v-app>
</template>

<script>
export default {
  name: 'app',
  data() {
    return {
      drawer: true,
      items: [{
          title: 'Live Data',
          icon: 'dashboard'
        },
        {
          title: 'Lap Comparison',
          icon: 'question_answer'
        }
      ],
      mini: true,
      right: null
    }
  }
}
</script>
