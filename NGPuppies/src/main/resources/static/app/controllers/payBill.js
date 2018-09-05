angular.module('NGPuppies')
    .controller('payBillController', function($http, $scope, AuthService) {
        var init = function (phoneNumber) {
            $http({
                url: '/api/subscriber?phoneNumber=' + phoneNumber,
                method: "GET",
                dataType: "json"
            })
            $scope.search = function () {

            }

        }
    });

