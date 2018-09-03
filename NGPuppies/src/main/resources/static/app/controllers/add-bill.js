

app.controller('addBillController', function($http, $scope) {
    $(document).ready(function () {
        $(".btn-pref .btn").click(function () {
            $(".btn-pref .btn").removeClass("btn-primary").addClass("btn-default");
            // $(".tab").addClass("active"); // instead of this do the below
            $(this).removeClass("btn-default").addClass("btn-primary");
        });
    });
    $scope.search = function () {
        var phoneNumber = $("#phoneNumber").val();
        $http({
            url: '/api/subscriber?phoneNumber=' + phoneNumber,
            method: "GET",
            dataType: "json"
        }).success(function (res) {
            $scope.name = res.firstName + " "  + res.lastName;
            $scope.egn = res.EGN;
            if (res.address!=null && res.address!=undefined){
                $scope.address = res.address;
            } else {
                $scope.address = "Not Presented";
            }
            if (res.client.credentials.username!=null && res.client.credentials.username!==undefined){
                $scope.bank = res.client.credentials.username;
            } else {
                $scope.bank = "Not Presented";
            }
            $scope.phoneNumber = res.phoneNumber;
            $("#subscriber").css("display","");
            if (res.billingRecordList.length == 0){
                $("#billingRecords").css("display", "none");
                $("#billingRecord").css("display", "none");
                $scope.bill = "No billing records";
            }else {
                $scope.records = res.billingRecordList;
                $("#billingRecords").css("display", "");
            }
        }).error(function (error) {
            $("#subscriber").css("display","none");
            $("#content").append($("<h1>No such subscriber</h1>"));
        });

    }
});