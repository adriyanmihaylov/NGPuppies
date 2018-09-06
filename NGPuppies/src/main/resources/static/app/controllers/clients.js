angular.module('NGPuppies')
// Creating the Angular Controller
    .controller('clients', function($http, $scope, AuthService) {
        var edit = false;
        var oldUsername = "";
        $scope.error = null;
        $scope.buttonText = 'Create';
        var init = function() {
            $http.get('api/client/all').success(function(res) {
                $scope.clients = res;
                $scope.message='';
                $scope.appAdmin = null;

            }).error(function(error) {
                $scope.message = error.message;
            });
        };
        $scope.initEdit = function(client) {
            $scope.error = null;
            edit = true;
            oldUsername = client.username;
            $scope.username = client.username;
            $scope.eik = client.eik;
            if (client.details.description===null){
                $scope.description = "No description";
            } else {
                $scope.description = client.details.description;
            }
            $scope.message='';

        };
        $scope.deleteUser = function(client) {
            $scope.error = null;
            $http.delete('api/user/'+ client.username + "/delete").success(function(res) {
                $scope.deleteMessage = res.message;
                init();
            }).error(function(error) {
                $scope.deleteMessage = error.message;
            });
        };
        $scope.update = function(){
            $scope.error = null;
            var password;
            var description;
            var username = $("#username").val();
            if ($("#password").val() === "") {
                password = null;
            }else{
                password = $("#password").val();
            }

            if ($("#description").val() === "") {
                description = null;
            }else{
                description = $("#description").val();
            }
            var updateFormData = {username : username, password : password,
                eik : $scope.eik, details : {description: description}};
            var url = 'api/client/update?username='+ oldUsername;
            $http({
                url : url,
                method : "PUT",
                data: JSON.stringify(updateFormData),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).success(function(res) {
                $scope.error = null;
                $scope.confirmPassword = null;
                $scope.eik = null;
                $scope.username = null;
                $scope.description = null;
                $scope.updated = res.message;
                init();
            }).error(function(error) {
                $scope.error =error.message;
            });
        };

        $scope.reset = function(){
            $scope.error = null;
            $scope.updated = null;
        };
        init();

    });
