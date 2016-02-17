/**
 * Created by darioalessandro on 9/5/15.
 */

function MainController($scope, $window, error, $state, loginSvc) {

    $scope.errors = {
        logout :  null
    };

    $scope.requests = {
        login : null
    };

    $scope.me = window.me;

    if(window.me === null || window.me === undefined) {
        $state.go("login", null, {location : 'replace'});
    }

    $scope.logout = function() {
        $scope.requests.logout = loginSvc.logout(
            function(APISuccess) {
                $window.me = undefined;
                $scope.errors.logout = null;
                $state.go("login", null, {location : 'replace'});
            },function(APIError) {
                $scope.errors.logout = error.apply(APIError.m, function() {
                    return $scope.logout();
                });
            }, function(HTTPError) {
                $scope.errors.login = error.apply("Please verify your internet connection", function() {
                    return $scope.logout();
                });
            });
    };

    $scope.isLogoutWorking = function(){
        return (!$scope.isValueInvalid($scope.requests.logout) &&
            $scope.requests.logout.$$state.status !== 1);
    };

    $scope.isValueInvalid = function(value) {
        return value === undefined || value === null;
    };

}


angular.module("appAuth").controller("MainController", MainController);