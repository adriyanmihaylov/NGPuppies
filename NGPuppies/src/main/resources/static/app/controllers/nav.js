angular.module('NGPuppies')
    .controller('NavController', function($http, $scope, AuthService, $state, $rootScope,$window) {
        $rootScope.$on('LoginSuccessful', function() {
            $scope.username = AuthService.username;
            $scope.isAuthenticated = AuthService.isAuthenticated;
            $scope.role = AuthService.role;
        });
        $scope.$on('Logout', function() {
            AuthService.expiration = null;
            AuthService.username = null;
            AuthService.isAuthenticated = false;
            AuthService.role = null;
            $window.localStorage.clear();
            $http.defaults.headers.common['Authorization'] = null;
            $scope.isAuthenticated = false;
            $scope.role = null;
        });
        $scope.logout = function() {
            $rootScope.$broadcast('Logout');
            $state.go('login');
        };
    });
