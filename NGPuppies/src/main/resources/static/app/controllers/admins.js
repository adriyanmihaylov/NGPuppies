angular.module('NGPuppies')
// Creating the Angular Controller
.controller('UsersController', function($http, $scope, AuthService) {
	var edit = false;
	$scope.buttonText = 'Create';
	var init = function() {
		$http.get('api/get/admins').success(function(res) {
			$scope.admins = res;
			$scope.userForm.$setPristine();
			$scope.message='';
			$scope.appAdmin = null;
			$scope.buttonText = 'Create';
			
		}).error(function(error) {
			$scope.message = error.message;
		});
	};
	$scope.initEdit = function(appAdmin) {
		edit = true;
		$scope.appAdmin = appAdmin;
		$scope.message='';
		$scope.buttonText = 'Update';
        // $http.get('api/admin/update').success(function(res) {
			// $scope.message = "Successful update"
        //
        // }).error(function(error) {
        //     $scope.message = error.message;
        // });
	};
	$scope.initAddAdmin = function() {
		edit = false;
		$scope.appAdmin = null;
		$scope.userForm.$setPristine();
		$scope.message='';
		$scope.buttonText = 'Create';
	};
	$scope.deleteUser = function(appAdmin) {
		console.log(this);
		$http.delete('api/admin/delete'+ $scope.username).success(function(res) {
			$scope.deleteMessage ="Success!";
			init();
		}).error(function(error) {
			$scope.deleteMessage = error.message;
		});
	};
	var editUser = function(){
		$http.put('api/admins', $scope.appAdmin).success(function(res) {
			$scope.appAdmin = null;
			$scope.confirmPassword = null;
			$scope.userForm.$setPristine();
			$scope.message = "Editting Success";
			init();
		}).error(function(error) {
			$scope.message =error.message;
		});
	};
	var addUser = function(){
		$http.post('api/admins', $scope.appAdmin).success(function(res) {
			$scope.appAdmin = null;
			$scope.confirmPassword = null;
			$scope.userForm.$setPristine();
			$scope.message = "User Created";
			init();
		}).error(function(error) {
			$scope.message = error.message;
		});
	};
	$scope.submit = function() {
		if(edit){
			editUser();
		}else{
			addUser();	
		}
	};
	init();

});
