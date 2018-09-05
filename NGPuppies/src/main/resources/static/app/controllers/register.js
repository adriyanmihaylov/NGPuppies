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
    $scope.submit = function (appAdmin) {
        if ($scope.password != $scope.confirmPassword) {
            $scope.message = "Password incorrect";
            return;
        }

        if ($option == "Admin") {
            var create = {username: $scope.username, password: $scope.password, email: $scope.email};
            $http({
                url: '/api/register/admin',
                method: "POST",
                data: JSON.stringify(create),
                // contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).success(function (res) {
                $scope.appAdmin = null;
                $scope.confirmPassword = null;
                $scope.password = null;
                $scope.username = null;
                $scope.email = null;
                $scope.register.$setPristine();
                $scope.message =res.responseText;
            }).error(function (error) {
                $scope.message = error.responseText;
            });
        } else if ($option == "Client") {
            var create = {username: $scope.username, password: $scope.password, eik: $scope.eik, details: null

            };
            $http({
                url: '/api/register/client',
                method: "POST",
                data: JSON.stringify(create),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).success(function (res) {
                $scope.appAdmin = null;
                $scope.confirmPassword = null;
                $scope.password = null;
                $scope.username = null;
                $scope.email = null;
                $scope.register.$setPristine();
                $scope.message = "Registration successfull !";
            }).error(function (error) {
                $scope.message = res.response;
            });

        }
    }
});
