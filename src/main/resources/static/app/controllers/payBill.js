angular.module('NGPuppies')
    .controller('payBillController', function($http, $scope) {
        var phoneNumber;
        $scope.cases = function () {
            if($scope.invoiceType === "Unpaid Invoices" || $scope.invoiceType === "All Invoices"){
                $("#dates").css("display","none");
            }else{
                $("#dates").css("display","");
            }

        };

        var initPayed = function (phoneNumber) {
            var startDate = $("#startDate").val();
            var endDate = $("#endDate").val();
            $http({
                url: '/api/client/invoice/' + phoneNumber + '/paid?from=' + startDate +"&to=" + endDate,
                method: "GET",
                dataType: "json"
            }).success(function(result) {
                if (result === ""){
                    $scope.message  = "No such subscriber!";
                    return;
                }
                $scope.subscriber = "Paid invoices for: " + $scope.phoneNumber;
                $scope.invoices = result;
                $("#invoiceDitails").css("display","");
            }).error(function (err) {
                $scope.message = err.message;
            })
        };

        var initNotPayed = function (phoneNumber) {
            $http({
                url: '/api/client/subscriber/' + phoneNumber + "/invoices/unpaid",
                method: "GET",
                dataType: "json"
            }).success(function(result) {
                if (result === ""){
                    $scope.message  = "No such subscriber!";
                    return;
                }
                $scope.subscriber = "Unpaid invoices for: " + $scope.phoneNumber;
                $scope.invoices = result;
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
                $scope.message = null;
                if (result === ""){
                    $scope.message  = "No such subscriber!";
                    return;
                }
                $scope.subscriber = "All invoices for: " + $scope.phoneNumber;
                $scope.invoices = result;

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
        $scope.reset = function () {
            $scope.message = null;
            $scope.success=null;
        }

    });

