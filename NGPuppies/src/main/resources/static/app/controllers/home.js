angular.module('NGPuppies')
// Creating the Angular Controller
.controller('HomeController', function($http, $scope, AuthService) {
	$scope.username = AuthService.username;
	$scope.isAuthenticated = AuthService.isAuthenticated
	$scope.role = AuthService.role;
    var init = function () {
        $http.get('api/user/all').success(function(res) {
            $scope.users = res;
        }).error(function(error) {
            $scope.message = error.message;
        });
    };
    $scope.deleteUser = function (user) {
            $http.delete('api/user/delete?username='+ user.username).success(function(res) {
                $scope.Deletemessage ="Success!";
                init();
            }).error(function(error) {
                $scope.deleteMessage = error.message;
            });

    }
    $(document).ready(function () {
        init();

    })
});
