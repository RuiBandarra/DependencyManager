package forms

import play.api.data.Forms._
import play.api.data.Form

case class DependencySearchForm(groupId: String, artifactId: String, version: String)

object DependencySearchForm {

  /**
    * The form definition.
    * It specifies the form fields and their types,
    * as well as how to convert from a DependencySearchForm to form data and vice versa.
    */
  val dependencySearchForm = Form(
    mapping(
      "groupId" -> nonEmptyText,
      "artifactId"  -> nonEmptyText,
      "version"   -> nonEmptyText
    )(DependencySearchForm.apply)(DependencySearchForm.unapply)
  )
}