package models

import java.net.URL

import dependencymanagement.loading.DependencyResolverClassLoader

class Dependency(val groupId:String,val artifactId:String,val version:String,val classLoader:DependencyResolverClassLoader=null){
  override def toString(): String ={
    val dependency=this.groupId+":"+this.artifactId+":"+this.version
    dependency
  }
}
