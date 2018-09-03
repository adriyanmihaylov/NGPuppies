angular.module('NGPuppies')
// Creating the Angular Controller
.controller('HomeController', function($http, $scope, AuthService) {
	$scope.username = AuthService.username;
	$scope.isAuthenticated = AuthService.isAuthenticated
	$scope.role = AuthService.role;
	(function init() {
        $http.get('api/get/users').success(function(res) {
            $scope.users = res;
        }).error(function(error) {
            $scope.message = error.message;
        });
    })();
});
