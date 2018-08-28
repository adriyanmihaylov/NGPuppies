angular.module('NGPuppies')
// Creating the Angular Controller
.controller('RegisterController', function($http, $scope, AuthService) {
	$scope.submit = function() {
		$http.post('register', $scope.appAdmin).success(function(res) {
			$scope.appAdmin = null;
			$scope.confirmPassword = null;
			$scope.register.$setPristine();
			$scope.message = "Registration successfull !";
		}).error(function(error) {
			$scope.message = error.message;
		});
	};
});
