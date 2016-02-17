/**
 * Created by adminuser on 3/30/15.
 */
var module = angular.module("appAuth");

module.factory('httpInterceptor', ['$log', '$q', '$injector', '$state', function($log, $q, $injector,$state) {

    function canRecover(rejection) {
      return false;
    }

    return {
        // optional method
        'request': function(config) {
            // do something on success
            return config;
        },

        // optional method
        'requestError': function(rejection) {
            $log.error(JSON.stringify(rejection));
            if (canRecover(rejection)) {
                return rejection;
            }
            return $q.reject(rejection);
        },

        // optional method
        'response': function(response) {
            var e = response.data.error;
            if(e !== undefined && e.logout && me !== undefined){
                $injector.get('$state').transitionTo('login',{error:e.m},null);
            }
            return response;
        },

        // optional method
        'responseError': function(rejection) {
            $log.error(JSON.stringify(rejection));
            if(rejection.status == 401){
                $state.transitionTo('login',{error:"Your session has expired, please login again."});
            }
            if (canRecover(rejection)) {
                return rejection;
            }
            return $q.reject(rejection);
        }
    };
}]);