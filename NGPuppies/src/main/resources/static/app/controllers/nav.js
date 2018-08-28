angular.module('NGPuppies')
// Creating the Angular Controller
.controller('NavController', function($http, $scope, AuthService, $state, $rootScope) {
	$scope.$on('LoginSuccessful', function() {
		$scope.user = AuthService.user;
		$scope.isAdmin = AuthService.isAdmin;
	});
	$scope.$on('LogoutSuccessful', function() {
		$scope.user = null;
		$scope.isAdmin = null;
	});
	$scope.logout = function() {
		AuthService.user = null;
		AuthService.isAdmin = null;
		$rootScope.$broadcast('LogoutSuccessful');
		$state.go('login');
	};
});
