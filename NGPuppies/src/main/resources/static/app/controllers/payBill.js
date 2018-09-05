angular.module('NGPuppies')
    .controller('payBillController', function($http, $scope) {
        $scope.cases = function () {
            if($scope.invoiceType === "Payed Invoices"){
                $("#dates").css("display","");
            }
        };

        var initPayed = function (phoneNumber) {
            var startDate = $("#startDate").val();
            var endDate = $("#endDate").val();
            $http({
                url: '/api/subscriber/' + phoneNumber + '/invoices/paid?from=' + startDate +"&to=" + endDate,
                method: "GET",
                dataType: "json"
            }).success(function(result) {
                $scope.subscriber = "Paid invoices for: " + $scope.phoneNumber;
                $scope.invoices = result;
                console.log(result);
                $("#invoiceDitails").css("display","");
            }).error(function (err) {
                $scope.message = err.message;
            })
        };

        var initNotPayed = function (phoneNumber) {
            $http({
                url: '/api/subscriber/' + phoneNumber + '/invoices/unpaid',
                method: "GET",
                dataType: "json"
            }).success(function(result) {
                $scope.subscriber = "Unpaid invoices for: " + $scope.phoneNumber;
                $scope.invoices = result;
                console.log(result);
                $("#invoiceDitails").css("display","");
            }).error(function (err) {
                $scope.message = err.message;
            })
        };

        var initAll = function (phoneNumber) {
            $http({
                url: '/api/subscriber/' + phoneNumber + '/invoices',
                method: "GET",
                dataType: "json"
            }).success(function(result) {
                $scope.subscriber = "All invoices for: " + $scope.phoneNumber;
                $scope.invoices = result;
                console.log(result);
                $("#invoiceDitails").css("display","");
            }).error(function (err) {
                $scope.message = err.message;
            })

        };

        $scope.initPay = function (invoice) {
                $http({
                    url: '/api/pay/invoice?id=' + invoice.id,
                    method: "PUT",
                    dataType: "json"
                }).success(function (result) {
                    $scope.message = "Successful payment"
                }).error(function (err) {
                    $scope.message = err.message;
                    init(invoice.subscriberPhone);
                })
            };

        $scope.search = function () {
            var phoneNumber = $scope.phoneNumber;
            if (phoneNumber!=="") {
                if ($scope.invoiceType === "All Invoices") {
                    initAll(phoneNumber);
                } else if ($scope.invoiceType === "Payed Invoices") {
                    initPayed(phoneNumber);
                } else {
                    initNotPayed(phoneNumber);
                }
            }else{
                $scope.message = "Please enter a phone number";
            }
        };

    });

