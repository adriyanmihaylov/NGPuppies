// Creating angular JWTDemoApp with module name "JWTDemoApp"
angular.module('NGPuppies', [ 'ui.router' ])


// the following method will run at the time of initializing the module. That
// means it will run only one time.
    .run(function(AuthService, $rootScope, $state) {
        // For implementing the authentication with ui-router we need to listen the
        // state change. For every state change the ui-router module will broadcast
        // the '$stateChangeStart'.
        $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
            // checking the user is logged in or not
            if (!AuthService.user) {
                // To avoiding the infinite looping of state change we have to add a
                // if condition.
                if (toState.name != 'login') {
                    event.preventDefault();
                    $state.go('login');
                }
            }
        });
    });