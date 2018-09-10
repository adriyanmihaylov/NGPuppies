app.controller('LoginController', function($http, $scope, $state, AuthService, $rootScope) {
    if(AuthService.role!=="ROLE_INITIAL") {
        $state.go('home');
    }

	// login method
	$scope.login = function() {
        // requesting the token by usename and passoword
        if ($scope.username == null || $scope.password == null) {
			$scope.message = "Please enter username and password";
        } else {
            var loginFormData = {username: $scope.username, password: $scope.password};
            $http({
                url: '/login',
                method: "POST",
                data: JSON.stringify(loginFormData),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).success(function (result) {
                $scope.password = null;
                // checking if the token is available in the response
                if (result.token) {
                    AuthService.setToken(result.token);
                    if(AuthService.role === "ROLE_INITIAL"){
                        $state.go('changePassword');
                        return;

                    }else {
                        localStorage.setItem('token', result.token);
                        localStorage.setItem("user", $scope.username);
                        $state.go('home');
                    }
                } else {
                    // if the token is not present in the response then the
                    // authentication was not successful. Setting the error message.
                    $scope.message = 'Authetication Failed !';
                }
            }).error(function (error) {
                // if authentication was not successful. Setting the error message.
                $scope.message = 'Authetication Failed !';
            });
        }
    };
	$scope.change = function () {
        $scope.message = null;
    }
});
