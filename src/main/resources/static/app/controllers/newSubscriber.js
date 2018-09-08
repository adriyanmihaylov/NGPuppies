angular.module('NGPuppies')
    .controller('newSubscriberController', function($scope,$http) {
        var validation = function(){
        };
        $scope.create = function (subscriber) {
            console.log(subscriber);
            var subscriberData = {phone: subscriber.phone,
                firstName:subscriber.firstName,
                lastName:subscriber.lastName,
                egn: subscriber.egn,
                address: {city: subscriber.address.city,
                    street: subscriber.address.street,
                    state:subscriber.address.state,
                    postCode:subscriber.address.zipCode,
                    country: subscriber.address.country},
                client:subscriber.client};
            $http({
                url : '/api/subscriber/create',
                method : "POST",
                data: JSON.stringify(subscriberData),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).success(function(res) {
                $scope.subscriber=null;
                $scope.error = null;
                $scope.success = res.message;
            }).error(function(error) {
                $scope.success = null;
                $scope.error = error.message;
            });
        }
    });