
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
/*1.2*/import java.net.URL

object result extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[Dependency,Array[URL],play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*2.2*/(dependency:Dependency,dependencyClassPathUrls: Array[URL]):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*2.61*/("""

"""),format.raw/*4.1*/("""<div class="container-fluid">
    <div class="row">
        <h2 class="table-header">Results for """),_display_(/*6.47*/dependency),format.raw/*6.57*/(""" """),format.raw/*6.58*/(""":</h2>
        <div class="table-responsive">
            <table class="table table-striped table-sm">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Group Id</th>
                        <th>Artifact Id</th>
                        <th>Version</th>
                        <th>Download URL</th>
                    </tr>
                </thead>
                <tbody>
                    """),_display_(/*19.22*/for((url,index) <- dependencyClassPathUrls.toList.zipWithIndex) yield /*19.85*/{_display_(Seq[Any](format.raw/*19.86*/("""
                    """),format.raw/*20.21*/("""<tr>
                        <td>"""),_display_(/*21.30*/index),format.raw/*21.35*/("""</td>
                        <td>"""),_display_(/*22.30*/dependency/*22.40*/.groupId),format.raw/*22.48*/("""</td>
                        <td>"""),_display_(/*23.30*/dependency/*23.40*/.artifactId),format.raw/*23.51*/("""</td>
                        <td>"""),_display_(/*24.30*/dependency/*24.40*/.version),format.raw/*24.48*/("""</td>
                        <td>"""),_display_(/*25.30*/url/*25.33*/.getPath),format.raw/*25.41*/("""</td>
                    </tr>
                """)))}),format.raw/*27.18*/("""
            """),format.raw/*28.13*/("""</table>
        </div>
    </div>
</div>
"""))
      }
    }
  }

  def render(dependency:Dependency,dependencyClassPathUrls:Array[URL]): play.twirl.api.HtmlFormat.Appendable = apply(dependency,dependencyClassPathUrls)

  def f:((Dependency,Array[URL]) => play.twirl.api.HtmlFormat.Appendable) = (dependency,dependencyClassPathUrls) => apply(dependency,dependencyClassPathUrls)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: 2019-10-27T18:33:17.855
                  SOURCE: /home/edelivery/untitled1/app/views/result.scala.html
                  HASH: 6ba36fcdbd242d9a251ddd083929c28c30150407
                  MATRIX: 432->1|772->23|926->82|956->86|1082->186|1112->196|1140->197|1637->667|1716->730|1755->731|1805->753|1867->788|1893->793|1956->829|1975->839|2004->847|2067->883|2086->893|2118->904|2181->940|2200->950|2229->958|2292->994|2304->997|2333->1005|2415->1056|2457->1070
                  LINES: 17->1|22->2|27->2|29->4|31->6|31->6|31->6|44->19|44->19|44->19|45->20|46->21|46->21|47->22|47->22|47->22|48->23|48->23|48->23|49->24|49->24|49->24|50->25|50->25|50->25|52->27|53->28
                  -- GENERATED --
              */
          