package controllers

import java.net.URL
import dependencymanagement.loading.{DependencyManagerClassLoader, DependencyManagerClassLoaderCache}
import forms.DependencySearchForm
import javax.inject.Inject
import play.api.mvc._
import models.Dependency
import play.api.Configuration

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
class DependencyManagerController @Inject()(playConfiguration:Configuration, cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {
  private val logger = play.api.Logger(this.getClass)

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(DependencySearchForm.dependencySearchForm,playConfiguration))
  }

  def retrieveDependencyClassPath(dependency:Dependency):Array[URL]= {
    val classLoader:DependencyManagerClassLoader = DependencyManagerClassLoaderCache.getInstance().getClassLoader(dependency.toString)
    classLoader.getURLs
  }

  def handleFormPost() = Action {  implicit request: Request[AnyContent] =>
    DependencySearchForm.dependencySearchForm.bindFromRequest().fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        BadRequest(views.html.searchForm(formWithErrors))
      },
      formData => {
        // binding success, you get the actual value.
        val dependency:Dependency=new Dependency(formData.groupId, formData.artifactId,formData.version)
        val dependencyClassPathUrls:Array[URL] = retrieveDependencyClassPath(dependency)
        val html=views.html.result.render(dependency,dependencyClassPathUrls)
        Ok(html)
      }
    )
  }

}


