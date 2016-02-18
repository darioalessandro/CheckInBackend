/**
 * Created by darioalessandro on 9/3/15.
 */

angular.module('appAuth', ['ui.router', 'ui.bootstrap', 'ngIOS9UIWebViewPatch', 'ngWebSocket'])
    .config(["$locationProvider", function ($locationProvider) {
        return $locationProvider.html5Mode(true).hashPrefix("!");
    }])
    .config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise("/nativeLogin");

    $stateProvider
        .state('login', {
            url: "/nativeLogin",
            views: {
                "main" : {
                    templateUrl: function(){return LoginUIRouter.controllers.LoginUI.login().url;},
                    controller: "LoginController"
                }
            }
        });
});