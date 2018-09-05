app.service('AuthService', function($rootScope,$http) {
    this.username = null;
    this.isAuthenticated = false;
    this.role = null;
    this.isTokenExpired = true;


    this.setToken = function(token) {
        if (token !== null) {
            var jwtData = token.split('.')[1];
            var decodedJwtJsonData = window.atob(jwtData);
            var decodedJwtData = JSON.parse(decodedJwtJsonData);

            this.expiration = new Date(decodedJwtData.exp * 1000);

            if(this.expiration > new Date()) {
                $http.defaults.headers.common['Authorization'] = 'Bearer ' + token;
                this.isTokenExpired = false;
                this.username = "username";
                this.isAuthenticated = true;
                this.role = decodedJwtData.role[0].authority;
                $rootScope.$broadcast('LoginSuccessful');
            } else {
                $rootScope.$broadcast("Logout");
            }
        }
    }
    this.getAuthority = function (token) {

        if (token !== null) {
            var jwtData = token.split('.')[1];
            var decodedJwtJsonData = window.atob(jwtData);
            var decodedJwtData = JSON.parse(decodedJwtJsonData);

        }
        return decodedJwtData.role[0].authority;
    }
});
