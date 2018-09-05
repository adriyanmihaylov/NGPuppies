var app = angular.module('NGPuppies', [ 'ui.router' ]);

app.run(function(AuthService, $rootScope,$state,$timeout) {
    $rootScope.$on('$stateChangeStart', function (event, toState) {
        if (!AuthService.isAuthenticated) {
            if (toState.name !== 'login') {
                event.preventDefault();
                $state.go('login');
            }
        } 
        if (toState.data && toState.data.role) {
            if (toState.data.role !== AuthService.role) {
                event.preventDefault();
                $state.go('access-denied');
            }
        }
    });

    function getToken() {
        if (localStorage.getItem('token') !== null) {
            AuthService.setToken(localStorage.getItem('token'));
            $timeout( function(){
                $rootScope.$broadcast('LoginSuccessful');
            }, 500 );
        }
    }
    getToken();
});