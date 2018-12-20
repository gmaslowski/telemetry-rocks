var merge = require('webpack-merge')
var prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  TELEMETRY_WS: '"ws://localhost:8083/frontend/ws"',
  AUTH_API: '"http://localhost:8082/auth/"',
  LAP_TIMES_API: '"http://localhost:8081/lapTimes/"'
})
