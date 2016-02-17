/**
 * Created by darioalessandro on 2/6/16.
 */

var app = angular.module("appAuth");

app.factory('loginSvc', ["httpClient", function (httpClient) {
    return {
        login:function(loginPayload,APISuccess,APIError,HTTPError){
            return httpClient.post(LoginAPIRouter.controllers.LoginAPI.login().url, loginPayload,APISuccess,APIError,HTTPError);
        },
        logout:function(APISuccess,APIError,HTTPError){
            return httpClient.post(LoginAPIRouter.controllers.LoginAPI.logout().url,{},APISuccess,APIError,HTTPError);
        }
    };
}]);