package controllers

import play.api.Configuration
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.routing.JavaScriptReverseRouter
import javax.inject.Inject
import play.api.http.MimeTypes

class JavascriptRoutesController@Inject()(playConfiguration:Configuration, cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {
  def javascriptRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("jsRoutes")(
        routes.javascript.DependencyManagerController.handleFormSubmit
      )
    ).as(MimeTypes.JAVASCRIPT)
  }
}
