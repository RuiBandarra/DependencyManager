
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
/*1.2*/import views.html.helper.inputText
/*2.2*/import forms.DependencySearchForm
/*4.2*/import play.api.Configuration

object index extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template3[Form[DependencySearchForm],Configuration,Messages,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*5.2*/(form: Form[DependencySearchForm],playConfiguration:Configuration)(implicit messages: Messages):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*5.97*/("""

"""),_display_(/*7.2*/main("Dependency Manager")/*7.28*/ {_display_(Seq[Any](format.raw/*7.30*/("""
    """),_display_(/*8.6*/defining(play.core.PlayVersion.current)/*8.45*/ { version =>_display_(Seq[Any](format.raw/*8.58*/("""

        """),format.raw/*10.9*/("""<section id="content">
            <div class="wrapper">
                <article>
                    <h2 class="sub-header">What is Dependency Manager</h2>
                    <p>Dependency Manager programmatically retrieves Dependencies from the Maven Central, or the customized private repositories for snapshots and releases, and stores them in the local Maven repository. The classes in these Dependencies then get loaded into a Classloader and stored in cache for faster retrieval.</p>
                    <p>One application for this code is that it can be used when we need to instantiate objects from classes at runtime, which were not available during compilation time, thus not part of the application's classpath.</p>
                    <p>Once the dependency is retrieved, a URLClassLoader subtype will be available in the cache, and any class inside can be loaded and instantiated through Reflection (i.e <code>classLoader.loadClass("org.apache.commons.io.FileUtils").newInstance()</code>)</p>
                    <p>In the interface we can order it to retrieve a Dependency transitively. We are then presented with a table containing the local Maven Repository's Path to each of the artifacts.</p>
                    <h2 class="sub-header">What is included</h2>
                    <p>Version """),_display_(/*19.33*/playConfiguration/*19.50*/.getString("dependencyManagerVersion")),format.raw/*19.88*/(""" """),format.raw/*19.89*/("""of Dependency Manager provides the following features: </p>
                    <ul class="list-group">
                        <li class="list-group-item">- Repository Customization</li>
                        <li class="list-group-item">- Repository Authentication through login in clear using settings.xml</li>
                        <li class="list-group-item">- Repository Authentication through encrypted login using security-settings.xml</li>
                    </ul>
                    <h2 class="sub-header">How it works</h2>
                    <p>This library was written in Java using <a href="https://wiki.eclipse.org/Aether/What_Is_Aether">Eclipse Aether Library</a>, the library used by <a href="https://maven.apache.org/resolver/index.html">Maven's Artifact Resolver</a>.</p>
                    <p>This web interface was created using Play Framework and Scala and runs in Akka.</p>
                    <p>Use the form below to use the library to retrieve the URL for a Dependency of your own choice.</p>
                </article>
            </div>
        </section>
    """),_display_(/*32.6*/searchForm(form)),format.raw/*32.22*/("""
    """)))}),format.raw/*33.6*/("""

""")))}),format.raw/*35.2*/("""

"""))
      }
    }
  }

  def render(form:Form[DependencySearchForm],playConfiguration:Configuration,messages:Messages): play.twirl.api.HtmlFormat.Appendable = apply(form,playConfiguration)(messages)

  def f:((Form[DependencySearchForm],Configuration) => (Messages) => play.twirl.api.HtmlFormat.Appendable) = (form,playConfiguration) => (messages) => apply(form,playConfiguration)(messages)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: 2019-10-27T20:54:14.466
                  SOURCE: /home/edelivery/untitled1/app/views/index.scala.html
                  HASH: 48c0f17ce8512ce9f6331f8f2a77ef8132ff7364
                  MATRIX: 432->1|474->38|515->76|892->108|1082->203|1112->208|1146->234|1185->236|1217->243|1264->282|1314->295|1353->307|2700->1627|2726->1644|2785->1682|2814->1683|3948->2791|3985->2807|4022->2814|4057->2819
                  LINES: 17->1|18->2|19->4|24->5|29->5|31->7|31->7|31->7|32->8|32->8|32->8|34->10|43->19|43->19|43->19|43->19|56->32|56->32|57->33|59->35
                  -- GENERATED --
              */
          