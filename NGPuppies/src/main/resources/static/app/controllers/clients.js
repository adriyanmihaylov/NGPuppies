angular.module('NGPuppies')
// Creating the Angular Controller
    .controller('clients', function($http, $scope, AuthService) {
        var edit = false;
        $scope.buttonText = 'Create';
        var init = function() {
            $http.get('api/client/all').success(function(res) {
                $scope.admins = res;
                $scope.userForm.$setPristine();
                $scope.message='';
                $scope.appAdmin = null;

            }).error(function(error) {
                $scope.message = error.message;
            });
        };
        $scope.initEdit = function(appAdmin) {
            edit = true;
            $scope.appAdmin = appAdmin;
            $scope.message='';

        };
        $scope.deleteUser = function(admin) {
            $http.delete('api/user/delete?username='+ admin.credentials.username).success(function(res) {
                $scope.deleteMessage ="Success!";
                init();
            }).error(function(error) {
                $scope.deleteMessage = error.message;
            });
        };
        var editUser = function(){
            console.log($scope.appAdmin.eik);
            var updateFormData = {username : $scope.appAdmin.credentials.username, password : $scope.appAdmin.credentials.password,
                eik : $scope.appAdmin.eik, details : null};
            var url = 'api/client/' + $scope.appAdmin.credentials.username + "/update";
            $http({
                url : url,
                method : "PUT",
                data: JSON.stringify(updateFormData),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).success(function(res) {
                $scope.appAdmin = null;
                $scope.confirmPassword = null;
                // $scope.userForm.$setPristine();
                $scope.message = "Success";
                init();
            }).error(function(error) {
                $scope.message =error.message;
            });
        };
        $scope.submit = function() {
            if(edit){
                editUser();
            }else{
                addUser();
            }
        };
        init();

    });
