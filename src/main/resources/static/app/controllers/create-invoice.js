

app.controller('addBillController', function($http, $scope) {
    var phone ;
    var init = function (phoneNumber) {
        $scope.errorService = null;
        $scope.serviceSuccesss = null;
        $scope.success = null;
        $http({
            url: '/api/service/all',
            method: "GET",
            dataType: "json"
        }).success(function (res) {
            $scope.telecomServices = res;
        }).error(function (error) {
            $scope.error = error.message;
        });

        $http({
            url: '/api/subscriber?phone=' + phoneNumber,
            method: "GET",
            dataType: "json"
        }).success(function (res) {
            if(res === ""){
                $scope.error = "";
                return;
            }
            $scope.subscriber = res;
        }).error(function (error) {
            $scope.error = error.message;
        });
    };

    $scope.search = function () {
        phone = $("#phoneNumber").val();
        init(phone);
    };

    $scope.addNewInvoice = function (invoice) {
        if (typeof invoice === undefined) {
            $scope.invoicesError = "Please enter invoice parameters";
        }
        if (typeof  invoice.amount === "undefined"){

            $scope.invoicesError = "Please enter amount";
            return;
        }
        if (typeof invoice.startDate === "undefined"){

            $scope.invoicesError = "Please enter start date";
            return;
        }
        if (typeof invoice.endDate === "undefined"){
            $scope.invoicesError = "Please enter end date";
            return;
        }
        if (typeof invoice.service === "undefined"){

            $scope.invoicesError = "Please enter service";
            return;
        }
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
        console.log(startDate);
        // var amount = $("#amount").val();
        // console.log(invoice.amount.toFixed(2));
        var invoiceData = {"list" : [{
                subscriberPhone: phone,
                startDate: startDate,
                endDate: endDate,
                amountBGN: invoice.amount.toFixed(2),
                service: invoice.service,
            }]
        };
        $http({
            url : '/api/invoice/generate',
            method : "POST",
            data: JSON.stringify(invoiceData),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).success(function () {
            $scope.invoicesError = null;
            $scope.startDate = null;
            $scope.service = null;
            $scope.money = null;
            $scope.invoiceSuccess = "Successful";
            init(phone);
        }).error(function (error) {
            $scope.invoiceSuccess = null;
            $scope.invoicesError = error.message;
        });
    };
    $scope.newService = function (service) {
        $http({
            url : '/api/subscriber/' + phone + '/service?name=' + service,
            method : "PUT",
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).success(function () {
            $scope.errorService = null;
            $scope.service = null;
            $scope.serviceSuccesss = "Successfully added";
            init(phone);
        }).error(function (error) {
            $scope.serviceSuccesss = null;
            $scope.errorService = error.message;
        });
    };
    $scope.resetService=function () {
        $scope.serviceSuccesss = null;
        $scope.errorService = null;
    };
    $scope.invoiceReset = function () {
        $scope.invoiceSuccess = null;
        $scope.invoicesError = null;
    }
});