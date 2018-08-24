var $input = $('#inputGroupSelect01');
var $submit = $('#submit');
$input.on('change', function () {
    if ($input.val() === "1") {
        $("#client").css("display", "none");
        $("#admin").css("display", "block");
    } else {
        $("#admin").css("display", "none");
        $("#client").css("display", "block");
    }
});

$(document).load(function () {
    $submit.on("submit", function () {
        if ($input.val() === "1") {
            var username = $("#username").val();
            var password = $("#password").val();
            var email = $("#email").val();
            $.post(" http://localhost:8080/api/secured/admin/register", {
                username: username,
                password: password,
                email: email
            })
                .done(function (data) {
                    console.log(data);
                    $("#success").css("display", "none");
                });
        }else{
            var username = $("#username").val();
            var password = $("#password").val();
            var EIK = $("#EIK").val();
            var description = $("#description").val();
            var friendlyName = $("#friendlyName").val();

        }
    })
});