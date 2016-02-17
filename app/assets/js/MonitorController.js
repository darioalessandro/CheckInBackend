/**
 * Created by darioalessandro on 9/5/15.
 */

function MonitorController($scope, $window, error, loginSvc, $state, $websocket) {

    //TODO: hack to change http => ws
    var ws = $websocket(ClientAPIRouter.controllers.ReceiverAPI.monitorSocket().absoluteURL().replace("http", "ws"));

    ws.onOpen(function() {
        console.log("on open");
    });

    ws.onMessage(function(message) {
        console.log("on message");
        $scope.receivers =JSON.parse(message.data);
    });

    $scope.errors = {
        login :  null
    };

    $scope.requests = {
        login : null
    };

    $scope.me = window.me;

    $scope.isLoginWorking = function(){
        return !$scope.isValueInvalid($scope.requests.login) &&
            $scope.requests.login.$$state.status !== 1;
    };

    $scope.isValueInvalid = function(value) {
        return value === undefined || value === null;
    };

}


angular.module("appAuth").controller("MonitorController", MonitorController);