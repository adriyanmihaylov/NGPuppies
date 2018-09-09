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
        $scope.initEdit = function(currency){
            $scope.editedCurrency = currency;
        };
        $scope.edit = function(currency){
            var data = {"list" : [{
                  name: currency.name,
                    fixing: currency.fixing
                }]
            };
            $http({
                url : '/api/currency/update',
                method : "PUT",
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).success(function (res) {
                $scope.currencySuccess = "Success!";
                initCurrencies();
            }).error(function (error) {
                $scope.currencyError = error.message;
            });
        };
        initCurrencies();
        $scope.reset = function () {
            $scope.currencyError = null;
            $scope.currencySuccess = null;
        }
    });