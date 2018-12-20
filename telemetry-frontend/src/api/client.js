export default {

  getLapTimes(context) {
    return context.$http.get(process.env.LAP_TIMES_API, {}, {
      headers: {
        'oauth_token': localStorage.getItem('oauth_token')
      }
    })
  }
}
