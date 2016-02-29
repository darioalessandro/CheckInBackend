/**
 * Created by darioalessandro on 9/5/15.
 */

function MyHomeController($scope, $window, error, loginSvc, $state, $websocket) {
    $scope.menu.section = 'myhome';

    $scope.beacons = [
        {
            name : "Joseph",
            lastTimeSeen: "At home, 15 seconds ago.",
            icon : "/assets/images/home.png",
            locationName:"Home"
        },
        {
            name : "Gilbert",
            lastTimeSeen: "Unknown, Left home 5 hours ago.",
            icon : "/assets/images/unknown.png",
            locationName:"Unknown"
        },
        {
            name : "Robert",
            lastTimeSeen: "Never seen, please make sure that the beacon app is running.",
            icon : "/assets/images/unknown.png",
            locationName:"Unknown"
        },
        {
            name : "Rita",
            lastTimeSeen: "At home, 43 seconds ago.",
            icon : "/assets/images/home.png",
            locationName:"Home"
        }
    ];

    $scope.configureBeacon = function(beacon) {
        alert("not implemented");
    };

}


angular.module("appAuth").controller("MyHomeController", MyHomeController);