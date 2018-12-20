import router from '@/router/index'
const LOGIN_URL = process.env.AUTH_API + 'oauth/token'

export default {

  authenticated: false,

  login(context, creds, redirect) {
    var options = {
      url: LOGIN_URL,
      method: 'POST',
      headers:
      {
        Authorization: 'Basic ' + btoa(creds.username + ':' + creds.password),
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      data: 'grant_type=client_credentials'
    }

    context.$http.post(options)
      .then(response => {
        localStorage.setItem('oauth_token', response.data.access_token)

        this.authenticated = true

        if (redirect) {
          router.push(redirect)
        }
      }).catch((err) => {
        context.error = err.data.error
      })
  },

  logout() {
    localStorage.removeItem('oauth_token')
    this.authenticated = false
  },

  isAuthenticated() {
    var token = localStorage.getItem('oauth_token')
    if (token) {
      this.authenticated = true
    } else {
      this.authenticated = false
    }
    return this.authenticated
  },

  authHeader() {
    return {
      'Authorization': 'Bearer ' + localStorage.getItem('oauth_token')
    }
  }
}
