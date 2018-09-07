angular.module('NGPuppies')
// Creating the Angular Controller
.controller('UsersController', function($scope,$http) {
    $scope.error = null;
    $scope.success = null;
	var oldUsername="";
	var edit = false;
	$scope.buttonText = 'Create';
	var init = function() {
		$http.get('api/admin/all').success(function(res) {
			$scope.admins = res;
			$scope.username = null;
            $scope.email = null;
            $scope.password = null;
			
		}).error(function(error) {
			$scope.error = error.message;
		});
	};
	$scope.initEdit = function(Admin) {
		edit = true;
		oldUsername = Admin.username;
		$scope.username = Admin.username;
		$scope.email = Admin.email;
		$scope.success=null;
	};
	$scope.deleteUser = function(admin) {
		$http.delete('api/user/' + admin.username+"/delete").success(function(res) {
			$scope.deleteMessage =res.message;
			init();
		}).error(function(error) {
			$scope.deleteMessage = error.message;
		});
	};
	$scope.update = function() {
        var url = "api/admin/update?username=" + oldUsername;
        var adminDTO = {username: $scope.username, password: $scope.password, email: $scope.email};
        $http.put(url,adminDTO).success(function(result) {
        	$scope.success = oldUsername + " updated!";
        	init();
        }).error(function(error) {
        	$scope.error = error.message;
        });
    };
	$scope.reset = function(){
        $scope.error = null;
        $scope.success = null;
        $scope.deleteMessage = null;
	};
	init();

});
