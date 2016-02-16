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


    $scope.submit = function(username, password) {

        var loginPayload = {
            username : username,
            password : password,
            client_id : $window.client_id,
            scope : $window.scope
        };

        $scope.requests.login = loginSvc.execute(loginPayload,
            function(APISuccess) {
                $scope.errors.login = null;
                window.loginData = APISuccess.data;
                $state.go("monitorUI");
            },function(APIError) {
                $scope.errors.login = error.apply(APIError.m, function() {
                    return $scope.submit($scope.username, $scope.password);
                });
            }, function(HTTPError) {
                $scope.errors.login = error.apply("Please verify your internet connection", function() {
                    return $scope.submit($scope.username, $scope.password);
                });
        });
    };

    $scope.isLoginWorking = function(){
        return !$scope.isValueInvalid($scope.requests.login) &&
            $scope.requests.login.$$state.status !== 1;
    };

    $scope.isValueInvalid = function(value) {
        return value === undefined || value === null;
    };

}


angular.module("appAuth").controller("MonitorController", MonitorController);