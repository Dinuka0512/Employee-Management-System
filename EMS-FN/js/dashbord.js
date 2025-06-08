securityOption();
function securityOption(){
    if(localStorage.getItem("email")== null){
        window.location.href = "../pages/signin.html";
    }
}

//BUTTONS
let logOut = $("#btnLogOut");

logOut.on("click", function (){
    //REMOVE THE EMAIL FROM LOCAL STORAGE
    localStorage.removeItem("email");

    //OPEN LOGIN PAGE
    window.location.href = "../pages/signin.html";
})

