<template>
<v-container fluid fill-height>
  <v-layout row wrap align-center>
    <v-flex xs6 sm4 offset-xs4>
      <v-card class="text-xs-center">
        <v-toolbar>
          <v-toolbar-title>Log in</v-toolbar-title>
        </v-toolbar>
        <v-container fluid>
          <v-layout row wrap>
            <v-flex xs12>
              <v-text-field label="E-mail" v-model="credentials.username" :rules="[rules.required]"></v-text-field>
            </v-flex>
            <v-flex xs12>
              <v-text-field label="Password" v-model="credentials.password" type="password"></v-text-field>
              <v-btn color="primary" @click="submit()">Log In</v-btn>
              <v-alert color="error" icon="warning" :value="this.showError" transition="scale-transition">
                {{error}}
              </v-alert>
            </v-flex>
          </v-layout>
        </v-container>
      </v-card>
    </v-flex>
  </v-layout>
</v-container>
</template>

<script>
import auth from '@/auth'

export default {

  data() {
    return {
      rules: {
        required: (value) => !!value || 'Required.'
      },
      credentials: {
        username: '',
        password: ''
      },
      error: '',
      showError: false
    }
  },
  methods: {
    submit() {
      this.showError = false
      var credentials = {
        username: this.credentials.username,
        password: this.credentials.password
      }
      auth.login(this, credentials, 'Dashboard')
    }
  },
  watch: {
    error: function() {
      this.showError = true
    }
  }
}
</script>
