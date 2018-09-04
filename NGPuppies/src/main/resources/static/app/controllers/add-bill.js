

app.controller('addBillController', function($http, $scope) {
    $(document).ready(function () {
        $(".btn-pref .btn").click(function () {
            $(".btn-pref .btn").removeClass("btn-primary").addClass("btn-default");
            // $(".tab").addClass("active"); // instead of this do the below
            $(this).removeClass("btn-default").addClass("btn-primary");
        });

    });
    var init = function (phoneNumber) {
        $http({
            url: '/api/subscriber?phoneNumber=' + phoneNumber,
            method: "GET",
            dataType: "json"
        }).success(function (res) {
            $scope.name = res.firstName + " " + res.lastName;
            $scope.egn = res.EGN;
            if (res.address != null && res.address != undefined) {
                $scope.address = res.address;
            } else {
                $scope.address = "Not Presented";
            }
            if (res.client.credentials.username != null && res.client.credentials.username !== undefined) {
                $scope.bank = res.client.credentials.username;
            } else {
                $scope.bank = "Not Presented";
            }
            $scope.phoneNumber = res.phoneNumber;
            $("#subscriber").css("display", "");
            if (res.billingRecordList.length == 0) {
                $("#billingRecords").css("display", "none");
                $("#billingRecord").css("display", "none");
                $scope.bill = "No billing records";
            } else {
                $scope.records = res.billingRecordList;
                $("#billingRecords").css("display", "");
            }
        }).error(function (error) {
            $("#subscriber").css("display", "none");
            $("#content").append($("<h1>No such subscriber</h1>"));
        });
    };

    $scope.search = function () {
        var phoneNumber = $("#phoneNumber").val();
        init(phoneNumber);
        };

    $scope.addNewInvoice = function () {
        var startDate =  $("#StartDate").val();
        var endDate =  $("#endDate").val();
        var phoneNumber = $scope.phoneNumber;
        console.log($scope.money);
        console.log(phoneNumber);
        var invoiceData = {subscriberPhone : phoneNumber,
            startDate : startDate,
            endDate : endDate,
            amount : $scope.money,
            service : $scope.service,
            currency : $scope.currency
        };
        $http({
            url : '/api/generate/bill',
            method : "POST",
            data: JSON.stringify(invoiceData),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).success(function (res) {
            $scope.startDate = null;
            $scope.service = null;
            $scope.money = null;
            $scope.currency = null;
            init(phoneNumber);
            $scope.message = "Successful added";
        });



    }
});