// @GENERATOR:play-routes-compiler
// @SOURCE:/home/edelivery/untitled1/conf/routes
// @DATE:Thu Oct 24 14:56:54 WEST 2019

package controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final controllers.ReverseDependencyManagerController DependencyManagerController = new controllers.ReverseDependencyManagerController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseAssets Assets = new controllers.ReverseAssets(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.javascript.ReverseDependencyManagerController DependencyManagerController = new controllers.javascript.ReverseDependencyManagerController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseAssets Assets = new controllers.javascript.ReverseAssets(RoutesPrefix.byNamePrefix());
  }

}
