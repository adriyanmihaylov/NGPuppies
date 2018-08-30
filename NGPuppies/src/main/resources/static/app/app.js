var app = angular.module('NGPuppies', [ 'ui.router' ])


app.run(function(AuthService, $rootScope,$state,$window,$http) {
    $rootScope.$on('$stateChangeStart', function (event, toState) {
        // checking the user is logged in or not
        console.log('stateChangeStart()');
        console.log(toState.name);
        if (!AuthService.user) {
            // To avoiding the infinite looping of state change we have to add a
            // if condition.
            if (toState.name !== 'login') {
                event.preventDefault();
                $state.go('login');
            }
        }
    });
    function getToken() {
        if ($window.localStorage.getItem('token') !== null) {
            $http.defaults.headers.common['Authorization'] = $window.localStorage.getItem('token');
            AuthService.user = 'success';
            AuthService.isAdmin = $window.localStorage.getItem('isAdmin');
            $rootScope.$broadcast('LoginSuccessful');
            $rootScope.user = AuthService.user;
            $rootScope.isAdmin = AuthService.isAdmin;
        }
    }

    getToken();
});



//
// app.run(function(AuthService, $rootScope,$state,$window,$http) {
//     $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
//         // checking the user is logged in or not
//         console.log(toState.name);
//         if (!AuthService.user) {
//             // To avoiding the infinite looping of state change we have to add a
//             // if condition.
//             if (toState.name !== 'login') {
//                 event.preventDefault();
//                 $state.go('login');
//             }
//         } else {
//             // $window.localStorage.clear();
//             if (toState.name == 'login' || toState.name == 'page-not-found') {
//                 $rootScope.$broadcast('LoginSuccessful');
//                 $state.go('home');
//             }
//         }
//     });
// });