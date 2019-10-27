
package views.html

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import play.api.mvc._
import play.api.data._
/*1.2*/import helper._
/*2.2*/import forms.DependencySearchForm
/*3.2*/import views.html.helper.inputText
/*4.2*/import forms.DependencySearchForm

object searchForm extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[Form[DependencySearchForm],Messages,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*6.2*/(dependencySearchForm: Form[DependencySearchForm])(implicit messages: Messages):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*6.81*/("""

"""),format.raw/*8.1*/("""<section id="form">
    <div class="wrapper">
        <article>
            <h2 class="sub-header">Search for a dependency</h2>
            """),_display_(/*12.14*/helper/*12.20*/.form(action = routes.DependencyManagerController.handleFormPost())/*12.87*/ {_display_(Seq[Any](format.raw/*12.89*/("""
                """),_display_(/*13.18*/inputText(dependencySearchForm("groupId"),
                '_label -> "Group ID"
                )),format.raw/*15.18*/("""
                """),_display_(/*16.18*/inputText(dependencySearchForm("artifactId"),
                    '_label -> "Artifact ID")),format.raw/*17.46*/("""
                """),_display_(/*18.18*/inputText(dependencySearchForm("version"),
                    '_label -> "Version")),format.raw/*19.42*/("""
                """),format.raw/*20.17*/("""<button type="submit" class="btn btn-primary">Search</button>
                """)))}),format.raw/*21.18*/("""
        """),format.raw/*22.9*/("""</article>
    </div>
</section>

"""))
      }
    }
  }

  def render(dependencySearchForm:Form[DependencySearchForm],messages:Messages): play.twirl.api.HtmlFormat.Appendable = apply(dependencySearchForm)(messages)

  def f:((Form[DependencySearchForm]) => (Messages) => play.twirl.api.HtmlFormat.Appendable) = (dependencySearchForm) => (messages) => apply(dependencySearchForm)(messages)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: 2019-10-27T21:02:42.170
                  SOURCE: /home/edelivery/untitled1/app/views/searchForm.scala.html
                  HASH: 086562060e2b186a3aa44585e994fa855f3b6298
                  MATRIX: 432->1|455->19|496->55|538->92|910->130|1084->209|1114->213|1286->358|1301->364|1377->431|1417->433|1463->452|1584->552|1630->571|1743->663|1789->682|1895->767|1941->785|2052->865|2089->875
                  LINES: 17->1|18->2|19->3|20->4|25->6|30->6|32->8|36->12|36->12|36->12|36->12|37->13|39->15|40->16|41->17|42->18|43->19|44->20|45->21|46->22
                  -- GENERATED --
              */
          