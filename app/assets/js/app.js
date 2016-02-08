/**
 * Created by darioalessandro on 9/3/15.
 */

angular.module('appAuth', ['ui.router', 'ui.bootstrap', 'ngIOS9UIWebViewPatch', 'ngWebSocket'])
    .config(["$locationProvider", function ($locationProvider) {
        return $locationProvider.html5Mode(true).hashPrefix("!");
    }])
    .config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise("/login");

    $stateProvider
        .state('login', {
            url: "/login",
            views: {
                "main" : {
                    templateUrl: function(){return LoginUIRouter.controllers.LoginUI.login().url;},
                    controller: "LoginController"
                }
            }
        }).state('monitorUI', {
            url: "/monitorUI",
            views: {
                "main" : {
                    templateUrl: function(){return ClientUIRouter.controllers.ClientUI.monitorUI().url;},
                    controller : "MonitorController"
                }
            }
        });
});