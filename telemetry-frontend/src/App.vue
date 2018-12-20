<template>
<v-app>
  <v-navigation-drawer clipped persistent v-model="drawer" enable-resize-watcher app permanent floating light :mini-variant.sync="mini" v-show="isAuthenticated()">
    <v-list dense>
      <v-list-tile @click="" v-for="item in sidebarItems">
        <v-list-tile-action>
          <v-icon color="blue darken-2">{{item.icon}}</v-icon>
        </v-list-tile-action>
        <v-list-tile-content>
          <v-list-tile-title>
            <router-link :to="item.destination">{{item.title}}</router-link>
          </v-list-tile-title>
        </v-list-tile-content>
      </v-list-tile>
    </v-list>
  </v-navigation-drawer>
  <v-toolbar app fixed clipped-left>
    <v-toolbar-title>F1 2017 Game Telemetry</v-toolbar-title>
    <v-spacer></v-spacer>
    <v-btn color="primary" v-show="isAuthenticated()" @click="logout()">Log Out</v-btn>
  </v-toolbar>
  <router-view></router-view>
  <v-footer app>
    <v-spacer></v-spacer>
    <div>Copyright @ 2017</div>
  </v-footer>
</v-app>
</template>

<script>
import auth from '@/auth'

export default {
  mounted() {
    if (auth.isAuthenticated()) {
      this.$router.push('Dashboard')
    } else {
      this.$router.push('/')
    }
  },
  methods: {
    logout() {
      auth.logout()
      this.$router.push('/')
    },
    isAuthenticated() {
      return auth.isAuthenticated()
    }
  },
  data() {
    return {
      drawer: true,
      sidebarItems: [{
        title: 'Live Data',
        icon: 'event',
        iconColor: 'blue darken-2',
        destination: 'Dashboard'
      },
      {
        title: 'Analysis',
        icon: 'info',
        iconColor: 'orange darken-2',
        destination: 'Analysis'
      },
      {
        title: 'Local Challenge',
        icon: 'gavel',
        iconColor: 'purple darken-2',
        destination: 'LocalChallenge'
      }],
      mini: true,
      right: null
    }
  }
}
</script>
