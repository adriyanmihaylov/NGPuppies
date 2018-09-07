angular.module('NGPuppies')
// Creating the Angular Controller
    .controller('reportsCotroller', function($http, $scope) {
        var initRecent = function () {
            $http({
                url: '/api/client/invoice/last10',
                method: "GET",
                dataType: "json"
            }).success(function(result) {
                $("#subscriberDetails").css("display","none");
                $scope.information = "Latest 10 payments";
                $scope.invoices = result;
                $scope.a = 1;
                console.log(result);
                $("#invoiceDitails").css("display","");
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
                $("#invoiceDitails").css("display","none");
                $scope.information = "Top 10 subscribers";
                $scope.subscribers = result;
                $scope.b = 1;
                console.log(result);
                $("#subscriberDetails").css("display","");
            }).error(function (err) {
                $scope.message = err.message;
            })
        };

        $scope.search = function (reportType) {
            if (reportType === "Recent Payments") {
                initRecent();
            }else{
                initTop10();
            }
        }
    });