angular.module('NGPuppies')
// Creating the Angular Controller
.controller('HomeController', function($http, $scope, AuthService) {
    $scope.username = AuthService.username;
    $scope.isAuthenticated = AuthService.isAuthenticated;
    $scope.role = AuthService.role;
    if(AuthService.role === "ROLE_ADMIN") {
        var init = function () {
            $http.get('api/user/all').success(function (res) {
                $scope.users = res;
            }).error(function (error) {
                $scope.message = error.message;
            });
        };
        $scope.deleteUser = function (user) {
            $http.delete('api/user/delete?username=' + user.username).success(function (res) {
                init();
            }).success(function () {
                $scope.success = "Successful delete!";

            }).error(function (error) {
                $scope.message = "Something went wrong";
                $scope.deletemessage = error.message;
            });

        };
        $(document).ready(function () {
            init();

        })
    }else{
        var initAll = function () {
            $http({
                url: '/api/invoices/unpaid',
                method: "GET",
                dataType: "json"
            }).success(function(result) {
                $scope.invoices = result;
            }).error(function (err) {
                $scope.message = err.message;
            })
        };
        $scope.isNavCollapsed = true;
        $scope.isCollapsed = false;
        $scope.isCollapsedHorizontal = false;
            initAll();
    }

});
