angular.module('NGPuppies')
    .controller('payBillController', function($http, $scope) {
        $scope.cases = function () {
            if($scope.invoiceType === "Payed Invoices"){
                $("#dates").css("display","");
            }else{
                $("#dates").css("display","none");
            }
        };

        var initPayed = function (phoneNumber) {
            var startDate = $("#startDate").val();
            var endDate = $("#endDate").val();
            $http({
                url: '/api/client/subscriber/' + phoneNumber + '/invoices/paid?from=' + startDate +"&to=" + endDate,
                method: "GET",
                dataType: "json"
            }).success(function(result) {
                $scope.subscriber = "Paid invoices for: " + $scope.phoneNumber;
                $scope.invoices = result;
                $("#invoiceDitails").css("display","");
            }).error(function (err) {
                $scope.message = err.message;
            })
        };

        var initNotPayed = function (phoneNumber) {
            $http({
                url: '/api/client/subscriber/' + phoneNumber + '/invoices/unpaid',
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
                url: '/api/client/subscriber/' + phoneNumber + '/invoices',
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
            var data = {"list": [{
                    id: invoice.id,
                    currency: "BGN"
                }]};
                $http({
                    url: '/api/client/invoice/pay',
                    method: "PUT",
                    data: JSON.stringify(data),
                    dataType: "json"
                }).success(function (result) {
                    $scope.message = "Successful payment"
                    initAll(invoice.subscriberPhone);
                }).error(function (err) {
                    $scope.message = err.message;

                })
            };

        $scope.search = function () {
            var phoneNumber = $("#phoneNumber").val();
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

