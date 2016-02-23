/**
 * Created by darioalessandro on 9/5/15.
 */

function MyHomeController($scope, $window, error, loginSvc, $state, $websocket) {
    $scope.menu.section = 'myhome';
}


angular.module("appAuth").controller("MyHomeController", MyHomeController);