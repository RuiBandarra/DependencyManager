// @GENERATOR:play-routes-compiler
// @SOURCE:/home/edelivery/untitled1/conf/routes
// @DATE:Thu Oct 24 14:56:54 WEST 2019


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
