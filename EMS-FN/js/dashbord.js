//WHEN LOG IN TO THE DASHBOARD CHECK IS THERE HAVE THE EMAIL ON LOCAL STORAGE
securityOption();
function securityOption(){
    if(localStorage.getItem("email")== null){
        window.location.href = "../pages/signin.html";
    }
}

//BUTTONS
let logOut = $("#btnLogOut");
let btnSave = $("#btnSave");
let btnDelete = $("#btnUpdate");
let btnUpdate = $("#btnDelete");
let btnReset = $("#btnReset");

//TEXT FIELDS
let txtName = $("#empName");
let txtAddress = $("#empAddress");
let txtEmail = $("#empEmail");
let txtContact = $("#empContact");


//WHEN btnRESET ON CLICK
btnReset.on('click', function (){
    pageReset();
})



//WHEN btnSave ON CLICK
btnSave.on('click', function (){
    //CREATE OBJECT
    let employee = {
        "name" : txtName.val(),
        "address" : txtAddress.val(),
        "email" : txtEmail.val(),
        "contact" : txtContact.val()
    }

    //THERE NEED SEND THE SERVER REQUEST
    $.ajax({
        method: "POST",
        url : "http://localhost:8080/EMS_Web_exploded/employee",
        contentType: "application/json",
        data : JSON.stringify(employee),
        success : function (response){
            console.log(response);
            alert(response.message);
        },

        error : function (xhr){
            alert("Server Error");
        }
    })
})



//ON CLICK LOG OUT FUNCTION
logOut.on("click", function (){
    //REMOVE THE EMAIL FROM LOCAL STORAGE
    localStorage.removeItem("email");
    //OPEN LOGIN PAGE
    window.location.href = "../pages/signin.html";
})

pageReset();
function pageReset(){
    loadTable();
}

function loadTable(){
    let table = $("#empTBody");
    table.empty();

    //HERE LOAD THE TABLE
    $.ajax({
        method:"GET",
        contentType:"application/json",
        url : "http://localhost:8080/EMS_Web_exploded/employee",
        success : function (response){
            response.forEach(function (employee){
                let row = `
                    <tr>
                        <td> ${employee.id} </td>
                        <td> ${employee.name} </td>
                        <td> ${employee.address} </td>
                        <td> ${employee.email} </td>
                        <td> ${employee.contact} </td>
                    </tr>
                `;

                table.append(row);
            })
        }
    })
}