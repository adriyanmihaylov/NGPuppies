angular.module('NGPuppies')
// Creating the Angular Controller
.controller('RegisterController', function($http, $scope, AuthService) {

    $scope.changeFunc = function () {
        $option = $("#optionSelect").val();
        if ($option == "Admin") {
            $("#Client").css("display", "none");
            $("#admin").css("display", "");
        } else if ($option == "Client") {
            $("#admin").css("display", "none");
            $("#Client").css("display", "");
        }
    }
	$scope.submit = function(appAdmin) {
    	if($scope.password != $scope.confirmPassword){
    		$scope.message = "Password is not the same!"
		}
        var create = {username : $scope.username, password : $scope.password, email: $scope.email};
    	if ($option == "Admin"){
    		$http({
                url : '/api/admin/register',
                method : "POST",
                data: JSON.stringify(create),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).success(function(res) {
                $scope.appAdmin = null;
                $scope.confirmPassword = null;
                $scope.password = null;
                $scope.username = null;
                $scope.email = null;
                $scope.register.$setPristine();
                $scope.message = "Registration successfull !";
            }).error(function(error) {
                $scope.message = "Bad credentials"
            });
		}

	};
});
