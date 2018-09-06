angular.module('NGPuppies')
// Creating the Angular Controller
.controller('RegisterController', function($http, $scope) {
    $option = $("#optionSelect").val();
    if ($option === "Admin") {
        $("#client").css("display", "none");
        $("#admin").css("display", "");
    } else if ($option === "Client") {
        $("#admin").css("display", "none");
        $("#client").css("display", "");
    }
    $("#optionSelect").on("change",function () {
        $option = $("#optionSelect").val();
        if ($option === "Admin") {
            $("#client").css("display", "none");
            $("#admin").css("display", "");
        } else if ($option === "Client") {
            $("#admin").css("display", "none");
            $("#client").css("display", "");
        }
    });
    $scope.submit = function () {
        if ($scope.password !== $scope.confirmPassword) {
            $scope.error = "Passwords does not match";
            return;
        }
        if ($scope.password===""||$scope.username === ""||$scope.confirmPassword===""){
            $scope.error = "Opps! Please fill all fields";
            return;
        }

        if ($option === "Admin") {
            if ($scope.email === ""){
                $scope.error = "Opps! Please enter email address";
                return;
            }
            var credentials = {username: $scope.username, password: $scope.password, email: $scope.email};
            $http({
                url: '/api/admin/register',
                method: "POST",
                data: JSON.stringify(credentials),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).success(function (res) {
                $scope.error=null;
                $scope.confirmPassword = null;
                $scope.password = null;
                $scope.username = null;
                $scope.email = null;
                $scope.success =res.message;
            }).error(function (error) {
                $scope.success = null;
                $scope.error = error.message;
            });
        } else if ($option === "Client") {
            var description;
            if($scope.eik === ""){
                $scope.error = "Please enter Eik for this client";
                return;
            }
            if ($("#description").val() === "") {
                description = null;
            }else{
                description = $("#description").val();
            }
            var create = {username: $scope.username, password: $scope.password, eik: $scope.eik, details: description

            };
            $http({
                url: '/api/client/register',
                method: "POST",
                data: JSON.stringify(create),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).success(function (res) {

                $scope.confirmPassword = null;
                $scope.password = null;
                $scope.username = null;
                $scope.email = null;
                $scope.eik  = null;
                $scope.description = null;
                $scope.success = res.message;
            }).error(function (error) {
                $scope.error = error.message;
            });

        }
    };
    $scope.reset = function () {
        $scope.error = null;
        $scope.success = null;
    }
});
