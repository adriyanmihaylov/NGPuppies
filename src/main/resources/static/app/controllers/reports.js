angular.module('NGPuppies')
// Creating the Angular Controller
    .controller('reportsCotroller', function($http, $scope) {
        var initAll = function () {
            $http({
                url: '/api/client/invoices/unpaid',
                method: "GET",
                dataType: "json"
            }).success(function(result) {
                if (result.length !== 0){
                    $scope.invoices = result;
                }
                $("#unpaid").css("display","");
                $("#subscriberDetails").css("display","none");
            }).error(function (err) {
                $scope.message = err.message;
            })
        };

        var initTop10 = function () {
            $http({
                url: '/api/client/subscriber/top10',
                method: "GET",
                dataType: "json"
            }).success(function(result) {
                $("#unpaid").css("display","none");
                $scope.information = "Top 10 subscribers";
                $scope.subscribers = result;
                $scope.b = 1;
                console.log(result);
                $("#subscriberDetails").css("display","");
            }).error(function (err) {
                $scope.message = err.message;
            })
        };
        $scope.initPay = function (invoice) {
            var currency = $("#-"+invoice.id).val();

            var data = {"list": [{
                    id: invoice.id,
                    currency: currency
                }]};
            $http({
                url: '/api/client/invoice/pay',
                method: "PUT",
                data: JSON.stringify(data),
                dataType: "json"
            }).success(function () {
                $scope.success = "Successful payment";
                initAll(invoice.subscriberPhone);
            }).error(function (err) {
                $scope.message = "Unsuccessful payment";

            })
        };

        $scope.search = function (reportType) {
            if (reportType === "All Unpaid") {
                initAll();
            }else{
                initTop10();
            }
        }
    });