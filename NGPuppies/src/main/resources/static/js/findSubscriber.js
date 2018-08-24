var phonenumber;
var $submit = $("#submit");
var $warning = $("#warning");
var $content = $("#mainContainer");
$submit.click(function search() {
    phonenumber = $("#search").val();
    var isnum = /^\d+$/.test(phonenumber);
    if (phonenumber !== "" && isnum) {
        $.get("../../../api/subscriber/get?phoneNumber=" + phonenumber, function(data) {
            if (data.length == 0){
                $warning.css("display","block");
            }else{
                var $divBody = $('<div class="card-body"></div>')
                var $title = $('<h5 class="card-title">' + "Subscriber with phone number: " + data.phoneNumber + '</h5>');
                var $name = $('<p class="card-text">' + "First Name: " + data.firstName + '</p> <p class="card-text">' + "Last Name: " + data.lastName + '</p>');
                var $header = $('<div class="card-header"> Add new bill </div>')
                var $button = $('<a href="#" class="btn btn-primary">Create new bill</a>')
                var $mainDivClass = $('<div class="card text-center"></div>')
                $mainDivClass.append($header);
                $divBody.append($title);
                $divBody.append($name);
                $divBody.append($button);
                $mainDivClass.append($divBody);
                $content.append($mainDivClass);
            }
        });
    }else{
        $warning.css("display","block");
    }
});
