angular.module('NGPuppies')
// Creating the Angular Service for storing logged user details
.service('AuthService', function() {
	return {
		user : null,
        isAdmin: null
	}
});
