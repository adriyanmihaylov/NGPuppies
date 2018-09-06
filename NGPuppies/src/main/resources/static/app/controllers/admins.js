angular.module('NGPuppies')
// Creating the Angular Controller
.controller('UsersController', function($scope,$http,$timeout, AuthService) {
	var oldUsername="";
	var edit = false;
	$scope.buttonText = 'Create';
	var init = function() {
		$http.get('api/admin/all').success(function(res) {
			$scope.admins = res;
            $timeout( function() {
                $scope.message = '';
            },1000);
			$scope.username = null;
            $scope.email = null;
            $scope.password = null;
			
		}).error(function(error) {
			$scope.message = error.message;
		});
	};
	$scope.initEdit = function(Admin) {
		edit = true;
		oldUsername = Admin.username;
		$scope.username = Admin.username;
		$scope.email = Admin.email;
		$scope.message='';
	};
	$scope.deleteUser = function(admin) {
		$http.delete('api/user/delete?username=' + admin.username).success(function(res) {
			$scope.deleteMessage ="Success!";
			init();
		}).error(function(error) {
			$scope.deleteMessage = error.message;
		});
	};
	$scope.update = function() {
        var url = "api/admin/update?username=" + oldUsername;
        var adminDTO = {username: $scope.username, password: $scope.password, email: $scope.email};
        $http.put(url,adminDTO).success(function(result) {
        	$scope.message = $scope.oldUsername + " updated!";
        	init();
        }).error(function(error) {
        	$scope.message = error.message;
        });
    };
	$scope.submit = function() {
        if (edit) {
            updateAdmin();
        }
    };
	init();

});
