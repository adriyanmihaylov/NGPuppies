angular.module('NGPuppies')
    .controller('payBillController', function($http, $scope) {
        var phoneNumber;
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
                }).success(function (result) {
                    $scope.success = "Successful payment";
                    initAll(invoice.subscriberPhone);
                }).error(function (err) {
                    $scope.message = "Unsuccessful payment";

                })
            };

        $scope.search = function () {
             phoneNumber = $("#phoneNumber").val();
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
        $scope.paySelected=function () {
            var selected = $(".payed");
            var selectedID = [selected.length];
            var selectedCurrency = [selected.length];
            for (var i = 0; i < selected.length; i++) {
               selectedID[i] = selected[i].id;
               if ($("#-" + selected[i].id).val() === "? undefined:undefined ?") {

               }
               selectedCurrency[i] = $("#-"+selected[i].id).val();
            }
            var o = {};
            var key = "list";
            o[key] = [];

            for (var i = 0; i < selected.length ; i++) {
                var data = {
                    id : selectedID[i],
                    currency : selectedCurrency[i]
                };
                o[key].push(data);
            }
            $http({
                url: '/api/client/invoice/pay',
                method: "PUT",
                data: JSON.stringify(o),
                dataType: "json"
            }).success(function () {
                $scope.success = "Successful paid"
                initAll(phoneNumber)
            }).error(function (err) {
                $scope.message = "Unsuccessful payment";

            })
        }


    });

