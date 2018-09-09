angular.module('NGPuppies')
// Creating the Angular Controller
    .controller('changepasswordController', function($http, $scope, $state) {
        $scope.change = function () {
            if($scope.password !== $scope.confirmpassword){
                $scope.message = "Passwords does not match"
            }
            var data = {password:$scope.password, confirmPassword:$scope.confirmpassword};
            $http({
                url: '/first-login',
                method: "POST",
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).success(function () {
                $state.go('login');
        }).error(function (err) {
                $scope.meesage = err.message;
            })
    }
    $scope.reset = function () {
        $scope.message = null;
    }
    });
