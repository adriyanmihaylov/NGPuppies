angular.module('NGPuppies')
    .controller('currenciesController', function($scope,$http) {
        $scope.update = function () {

        };
        var initCurrencies =function () {
            $http.get('api/currency/all').success(function(res) {
                $scope.currencies = res;
            }).error(function(error) {
                $scope.message = error.message;
            });
        };
        $scope.initEdit = function(chosenCurrency){
            $scope.editedCurrency.name = chosenCurrency.name;
            $scope.editedCurrency.fixing = chosenCurrency.fixing;
        };
        initCurrencies();
    });