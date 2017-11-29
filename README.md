f1game-telemetry
================

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/fdf98753c2b94cc298cdbab140249635)](https://www.codacy.com/app/gmaslowski/f1game-telemetry?utm_source=github.com&utm_medium=referral&utm_content=gmaslowski/f1game-telemetry&utm_campaign=badger)

## Important!
This project evolved quite a bit. 

It is currently available under [https://f1telemetry.rocks](https://f1telemetry.rocks) !

The source code was moved to [https://gitlab.com/telemetry-rocks](https://gitlab.com/telemetry-rocks) - please contact me if you'd like to support the development!

### Development

Just invoke (defaults to playing provided demo run):

```
sbt run
```

And navigate to: [http://localhost:9000](http://localhost:9000)

__**Note**__: There's a demo file recorder using the `DemoRecorder` actor. By using the `DemoPlayer` actor it's
possible to play dumped UDP telemetry (`demo.f1t`) from this run:

[![F1 Demo Run](https://img.youtube.com/vi/749N3TgkqGU/0.jpg)](https://www.youtube.com/watch?v=749N3TgkqGU)

__**Note**__: For recording and playing recorded files, please see `demo` configuration section in `application.conf`:
```
demo {
  record = false
  play = true
  filename = demo.f1t
}
```
