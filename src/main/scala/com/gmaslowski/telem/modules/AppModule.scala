package com.gmaslowski.telem.modules

import com.gmaslowski.telem.{Bootstrap, WebSocketHandler}
import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport

class AppModule extends AbstractModule with AkkaGuiceSupport{
  override def configure() = {
    bind(classOf[Bootstrap]).asEagerSingleton()
  }
}
