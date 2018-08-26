var $loginSubmit = $("#loginSubmit");

$loginSubmit.on("click", function() {
    console.log("CLICKED")
    var loginUrl = "https://localhost:8443/login";
    var $username = $("#usernameLogin").val();
    var $password = $('#passwordLogin').val();
    var token;
    $.ajax
    ({
        type: "POST",
        url: loginUrl,
        dataType: 'json',
        //json object to sent to the authentication url
        data: JSON.stringify({"username": "admin", "password": "123456"}),
        contentType: 'application/json',
        success: function (data) {
            token = data.token;
            $.ajax
            ({
                type: "GET",
                url: 'https://localhost:8443/secured/admin/login',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Authorization", "Bearer " + token);
                },
                success: function (data) {
                    $("main").html(data);
                }
            });
        }

    });
})


var $client = $("#client");
$client.on("click", function () {
    $.ajax({
        type: "GET",
        url: "https://localhost:8443/secured/admin/create/client",
        beforeSend: function (xhr) {
            xhr.setRequestHeader ("Authorization", "Bearer " + token);
        },

        success: function () {
            $("body").html(data);
        }
    })
})



