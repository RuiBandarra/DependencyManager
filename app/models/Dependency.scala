package models

import java.net.URL

import dependencymanagement.loading.DependencyManagerClassLoader

class Dependency(val groupId:String,val artifactId:String,val version:String,val classLoader:DependencyManagerClassLoader=null){
  override def toString(): String ={
    val dependency=this.groupId+":"+this.artifactId+":"+this.version
    dependency
  }
}
