/**
 * Created by darioalessandro on 9/5/15.
 */

function LoginController($scope, $window, error, loginSvc, $state, $rootScope, $templateCache) {

    var self = $scope;

    $scope.errors = {
        login :  null
    };

    $scope.requests = {
        login : null,
        transitioning : false
    };


    $scope.submit = function(username, password) {

        if($scope.isValueInvalid(username) || $scope.isValueInvalid(password))
            return;

        var loginPayload = {
            username : username,
            password : password,
            client_id : $window.client_id,
            scope : $window.scope
        };

        $scope.requests.login = loginSvc.login(loginPayload,
            function(APISuccess) {
                $scope.errors.login = null;
                window.me = APISuccess.data;
                location.replace(document.cookie);
                console.log();
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

    $rootScope.$on('$stateChangeStart',
        function(event, toState, toParams, fromState, fromParams, options){
            self.requests.transitioning = true;
        });

    $rootScope.$on('$stateChangeSuccess',
        function(event, toState, toParams, fromState, fromParams){
            self.requests.transitioning = false;
        });

    $rootScope.$on('$stateChangeError',
        function(event, toState, toParams, fromState, fromParams, error){
            self.requests.transitioning = false;
        });

    $scope.isLoginWorking = function(){
        return (!$scope.isValueInvalid($scope.requests.login) &&
            $scope.requests.login.$$state.status !== 1) || $scope.requests.transitioning;
    };

    $scope.isValueInvalid = function(value) {
        return value === undefined || value === null;
    };


}


angular.module("appAuth").controller("LoginController", LoginController);