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
        };
        var initClient = function () {
            $http({
                url : '/api/client/all',
                method : "GET",
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).success(function(res) {
                $scope.searchedSubscriberClients = res;
                $scope.Newclients = res;
            }).error(function(error) {
                $scope.error = error.message;
            });
        };
        var phoneNumber;
        $scope.search = function (subscriberPhone) {
            phoneNumber= subscriberPhone;
            $scope.subscriber = null;
            $http({
                url: 'api/subscriber?phone=' + subscriberPhone,
                method: "GET",
                dataType: "json"
            }).success(function (result) {
                $scope.message = null;
                if (result!=="") {
                    $scope.error = "";
                    $scope.subscriberSearch = result;
                }else{
                    $scope.errorMessage = "Please try another number!";
                }
            }).error(function (err) {
                $scope.message = err.message;
            });
            initClient();
        };
        $scope.deleteSubscriber = function () {
            $http({
                url: 'api/subscriber/delete?phone=' + $("#subscriberPhone").val(),
                method: "DELETE",
                dataType: "json"
            }).success(function (result) {
                $scope.message = result.message;
                $scope.subscriberSearch = null;

                $scope.subscriberSearch = null;
            }).error(function (err) {
                $scope.message = err.message;
            });
        };
        $scope.initEdit = function (subscriber) {
            var data = {phone: subscriber.subscriberPhone,
                firstName: subscriber.firstName,
                lastName: subscriber.lastName,
                egn: subscriber.EGN,
                client: subscriber.client};
            $http({
                url: 'api/subscriber/' + phoneNumber +"/update",
                method: "PUT",
                data : JSON.stringify(data),
                dataType: "json"
            }).success(function (result) {
                $scope.successUpdating = result.message;
            }).error(function (err) {
                $scope.errorUpdating = err.message;
            })
        };
        initClient();

        $scope.reset = function () {
            $scope.errorUpdating = null;
            $scope.successUpdating = null;
            $scope.message = null;
            $scope.errorMessage = null;
            $scope.error = null;
            $scope.success = null;
        }

    });