f1game-telemetry
================

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/fdf98753c2b94cc298cdbab140249635)](https://www.codacy.com/app/gmaslowski/f1game-telemetry?utm_source=github.com&utm_medium=referral&utm_content=gmaslowski/f1game-telemetry&utm_campaign=badger)

### Development

There's a demo file recorder using the `DemoRecorder` actor. By using the `DemoPlayer` actor it's
possible to play dumped UDP telemetry from this run: (todo: add youtube link)

*Note*: For recording and playing recorded files, please see `demo` configuration section in `application.conf`:
```
demo {
  record = false
  play = true
  filename = demo.f1t
}
```