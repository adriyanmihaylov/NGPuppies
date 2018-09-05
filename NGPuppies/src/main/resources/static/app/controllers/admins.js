angular.module('NGPuppies')
// Creating the Angular Controller
.controller('UsersController', function($scope,$http, AuthService) {
	var edit = false;
	$scope.buttonText = 'Create';
	var init = function() {
		$http.get('api/get/admins').success(function(res) {
			$scope.admins = res;
			$scope.userForm.$setPristine();
			$scope.message='';
			$scope.appAdmin = null;
			
		}).error(function(error) {
			$scope.message = error.message;
		});
	};
	$scope.initEdit = function(appAdmin) {
		edit = true;
		$scope.oldUsername = appAdmin.credentials.username;
		$scope.username = appAdmin.credentials.username;
        $scope.authority = appAdmin.credentials.authority;
		$scope.email = appAdmin.email;
		$scope.message='';

	};
	$scope.deleteUser = function(admin) {
		$http.delete('api/delete/user?username=' + admin.username).success(function(res) {
			$scope.deleteMessage ="Success!";
			init();
		}).error(function(error) {
			$scope.deleteMessage = error.message;
		});
	};
	var editUser = function() {
        var url = "api/update/admin?username=" + $scope.oldUsername;
        var adminDTO = {username: $scope.username, password: $scope.password, email: $scope.email};
        $http.put(url,adminDTO).success(function(res) {
			console.log("Success");
        }).error(function(error) {
        	console.log("Error");
        });
    };
	// var addUser = function(){
	// 	$http.post('api/admins', $scope.appAdmin).success(function(res) {
	// 		$scope.appAdmin = null;
	// 		$scope.confirmPassword = null;
	// 		$scope.userForm.$setPristine();
	// 		$scope.message = "User Created";
	// 		init();
	// 	}).error(function(error) {
	// 		$scope.message = error.message;
	// 	});
	// };
	$scope.submit = function() {
		if(edit){
			editUser();
		}else{
			addUser();	
		}
	};
	init();

});
