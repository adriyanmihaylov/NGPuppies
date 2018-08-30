app.service('AuthService', function($rootScope,$http) {
    this.username = null;
    this.isAuthenticated = false;
    this.role = null;
    this.expiration = null;


    this.setToken = function(token) {
        console.log("AuthService");
        if (token !== null) {
            var jwtData = token.split('.')[1];
            var decodedJwtJsonData = window.atob(jwtData);
            var decodedJwtData = JSON.parse(decodedJwtJsonData);
            console.log("We are setting the AuthService")
            // this.expiration = new Date(decodedJwtData.iat /1000);
            // console.log(expiration);
            $http.defaults.headers.common['Authorization'] = 'Bearer ' + token;
            this.username = decodedJwtData.sub;
            this.isAuthenticated = true;
            this.role = decodedJwtData.role[0].authority;
            $rootScope.$broadcast('LoginSuccessful');
        }
    }
});
