/**
 * Created by darioalessandro on 9/3/15.
 */

angular.module('appAuth', ['ui.router', 'ui.bootstrap', 'ngIOS9UIWebViewPatch', 'ngWebSocket'])
    .config(["$locationProvider", function ($locationProvider) {
        return $locationProvider.html5Mode(true).hashPrefix("!");
    }])
    .config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise("/home/monitor");

    $stateProvider
        .state('login', {
            url: "/login",
            views: {
                "main" : {
                    templateUrl: function(){return LoginUIRouter.controllers.LoginUI.login().url;},
                    controller: "LoginController"
                }
            }
        }).state('main', {
            url: "/home",
            views: {
                "main" : {
                    templateUrl: function(){return ClientUIRouter.controllers.ClientUI.main().url;},
                    controller : "MainController"
                }
            }
        }).state('main.monitor', {
            url: "/monitor",
            views: {
                "section" : {
                    templateUrl: function(){return ClientUIRouter.controllers.ClientUI.monitorUI().url;},
                    controller : "MonitorController"
                }
            }
        });
});