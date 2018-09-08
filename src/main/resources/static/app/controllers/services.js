angular.module('NGPuppies')
    .controller('servicesController', function($http, $scope) {
        $scope.addNewService = function (service) {
            var data = {name: service};
            $http({
                url: 'api/service/create',
                method: "POST",
                data: JSON.stringify(data),
                dataType: "json"
            }).success(function () {
                $scope.serviceSuccess = "Service created successfully"
                init();
            }).error(function (err) {
                $scope.serviceError = err.message;
            })
        };
        var init = function () {
            $http({
                url: 'api/service/all',
                method: "GET",
                dataType: "json"
            }).success(function (res) {
                $scope.services = res;
            }).error(function (err) {
                $scope.serviceError = err.message;
            })
        };
        init();
    });