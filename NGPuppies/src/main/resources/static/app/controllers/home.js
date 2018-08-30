angular.module('NGPuppies')
// Creating the Angular Controller
.controller('HomeController', function($http, $scope, AuthService) {
	console.log("Home controller")
	$scope.user = AuthService.user;
	$scope.isAdmin = AuthService.isAdmin;
});
