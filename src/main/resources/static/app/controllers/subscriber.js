app.controller('subscribersController', function($http, $scope) {
    $scope.subscriber = null;
    $scope.money = '';
    $scope.search = function (subscriberPhone) {
        $scope.subscriber = null;
        $http({
            url: 'api/client/subscriber?phone=' + subscriberPhone,
            method: "GET",
            dataType: "json"
        }).success(function (result) {
            if (result!=="") {
                $scope.error = "";
                $scope.subscriber = result;
            }else{
                $scope.error = "Error";
            }
        }).error(function (err) {
            $scope.message = err.message;
        })
    };
    $scope.report = function (subscriberPhone) {
        var phone = subscriberPhone.substr(7);
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
        var isPayment = true;
        $http({
            url: 'api/client/subscriber/' + phone + '/average?from=' + startDate + "&to=" +endDate,
            method: "GET",
            dataType: "json"
        }).success(function (result) {
            if (result!==0.0) {
               $scope.average = result.message;
               isPayment = true;
            }else{
                isPayment = false;
            }
        }).error(function (err) {
            $scope.errorDate = err.message;
            $scope.money = null;
        });
        $http({
            url: 'api/client/subscriber/' + phone + '/invoice/max?from=' + startDate + "&to="+endDate,
            method: "GET",
            dataType: "json"
        }).success(function (result) {
            if (result.id!==0) {
                console.log(result);
                $scope.maximum = result.amount;
                isPayment = true;
            }else{
                isPayment = false;
            }
            if (isPayment===true){
                $scope.money = "true";
            }else{
                $scope.money  = null;
            }
        }).error(function (err) {
            $scope.money = null;
            $scope.errorDate = err.message;
        });

    };
    $scope.reset = function () {
        $scope.errorDate = null;
        $scope.error = null;
    }

});