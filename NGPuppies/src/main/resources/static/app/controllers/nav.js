angular.module('NGPuppies')
    .controller('NavController', function($http, $scope, AuthService, $state, $rootScope,$window) {
        $scope.$on('LoginSuccessful', function() {
            console.log("LoginSuccessful");
            $scope.username = AuthService.username;
            $scope.isAuthenticated = AuthService.isAuthenticated;
            $scope.role = AuthService.role;
        });
        $scope.$on('LogoutSuccessful', function() {
            $scope.isAuthenticated = false;
            $scope.role = null;
        });
        $scope.logout = function() {
            AuthService.isAuthenticated = false;
            AuthService.role = null;
            $window.localStorage.clear();
            $rootScope.$broadcast('LogoutSuccessful');
            $state.go('login');
        };
    });
