let txtName = $("#name");
let txtAddress = $("#address");
let txtEmail = $("#email");
let txtPassword = $("#password");

function SignIn(){
    console.log(txtName.val())
    let user = {
        "name": txtName.val(),
        "address" : txtAddress.val(),
        "email": txtEmail.val(),
        "password": txtPassword.val()
    };

    $.ajax({
        url : "http://localhost:8080/EMS_Web_exploded/api/v1/signup",
        method : "POST",
        contentType : "application/json",
        data : JSON.stringify(user),
        success: function (response) {
            if(response.status === "success"){
                alert("Hey " + user.name + ", You have account Created Successfully..!");
                clearText();
                //NOW NEED TO GO TO THE SIGNING PAGE
                window.location.href = "../pages/signin.html";
            }else{
                alert("Something went Wrong...");
            }
        },

        error:function (xhr) {
            alert("Server Error!!");
        }
    });
}

function clearText(){
    txtName.val("");
    txtEmail.val("");
    txtPassword.val("");
    txtAddress.val("");
}