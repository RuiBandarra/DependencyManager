package startup

import com.google.inject.AbstractModule

/**
  *
  * A module to register bindings.
  *
  */
class EagerLoaderModule extends AbstractModule {
  override def configure() = {
    bind(classOf[StartupService]).asEagerSingleton //The startup code
  }
}
