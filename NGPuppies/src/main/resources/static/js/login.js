var $loginSubmit = $("#loginSubmit");

$loginSubmit.on("submit", function() {
    var loginUrl = "http://localhost:8080/login"
    var $username = $("#usernameLogin").val();
    var $password = $('#passwordLogin').val();
    $.post(loginUrl, {username: username, password: password})
        .done(function (data) {
            console.log(data);
        });
});
var token;
$.ajax
({
    type: "POST",
    url: 'https://localhost:8443/login',
    dataType: 'json',
    //json object to sent to the authentication url
    data: JSON.stringify({"username": "admin", "password": "123456"}),
    contentType: 'application/json',
    success: function (data) {
        token = data.token;
    }
});
$.ajax
({
    type: "GET",
    url: 'https://localhost:8443/secured/admin/login',
    dataType: 'json',
    beforeSend: function (xhr) {
        xhr.setRequestHeader ("Authorization", "Bearer " + token);
    },
    contentType: 'application/json',
   });


