//TEXT FILLED
let email = $("#email");
let password = $("#password");

//BUTTONS
let login = $("#btnLogin");

login.on('click', function (){
    accountLogIn();
})

function accountLogIn(){
    let userLoged = {
        "email" : email.val(),
        "password" : password.val()
    }

    $.ajax({
        url:"http://localhost:8080/EMS_Web_exploded/api/v1/signIn",
        method : "POST",
        contentType : "application/json",
        data: JSON.stringify(userLoged),
        success:function (response){
            if(response.code === "200"){
                alert(response.message);
                clearText();

                //IS OK THERE NOW CAN SAVE ON LOCAL STORAGE
                localStorage.setItem("email", userLoged.email);
                if(localStorage.getItem("email")!= null){
                    //OPEN DASHBOARD
                    window.location.href = "../pages/dashbord.html";
                }
            }else if(response.code ==="401" || response.code === "400"){
                alert(response.message);
            }else {
                alert(response.message);
            }
        },
        error:function (xhr){
            alert("Server Problem !!");
        }
    })
}

function clearText(){
    email.val("");
    password.val("");
}