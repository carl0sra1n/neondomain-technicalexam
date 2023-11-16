$(document).ready(function() {
    $("#signInButton").click(async function() {
        const userName = $("#txtUserName").val();
        const password = $("#txtPassword").val();

        try {
            const token = await getToken(userName, password);
            $("#signInButton").prop("disabled", true);
            
            localStorage.setItem("jwtToken", token.jwt);
            
            window.location.href = "/public/users";
        } catch (error) {
            let errText = error.jqXHR.responseText;
            
            if(isJSON(error.jqXHR.responseText))
            {
                const response = JSON.parse(error.jqXHR.responseText);
                errText = response.error;
            }

            Swal.fire({
                icon: "error",
                title: "Error",
                text: errText,
                toast: true
            });
        }
    });
});

$('#registerModal').on('hidden.bs.modal', function () {
    $('#userName').val(null);
    $('#firstName').val(null);
    $('#lastName').val(null);
    $('#age').val(null);
    $('#password').val(null);
});

function openRegisterModal() {    
    $('#registerModal').modal('toggle');
}

async function saveForm() {
    const payload = getFormPayload();

    try {
        const user = await userActions(payload);

        showSuccessToast("Success", "User created successfully.");

        closeRegisterModal();
    } catch (error) {
        handleSaveFormError(error);
    }
}

function handleSaveFormError(error) {
    showErrorToast("There was an error", !JSON.parse(error.jqXHR.responseText).details ? JSON.parse(error.jqXHR.responseText).message : JSON.parse(error.jqXHR.responseText).message + "\n\n" + JSON.parse(error.jqXHR.responseText).details.join("\n"));
}

function showErrorToast(title, text) {
    Swal.fire({
        icon: "error",
        title: title,
        text: text,
        toast: true
    });
}

function showSuccessToast(title, text) {
    Swal.fire({
        icon: "success",
        title: title,
        text: text,
        toast: true
    });
}

async function userActions(user) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: "/auth/register",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(user),
            success: function (data) {
                resolve(data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                reject({ jqXHR, textStatus, errorThrown });
            }
        });
    });
}

function getFormPayload() {
    return {
        "userName": $('#userName').val(),
        "firstName": $('#firstName').val(),
        "lastName": $('#lastName').val(),
        "age": $('#age').val(),
        "password": $('#password').val(),
    };
}

function closeRegisterModal() {
    $('#registerModal').modal('toggle');
}

function getToken(userName, password) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: "/auth/login",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify({ userName: userName, password: password }),
            success: function(data) {
                resolve(data);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                reject({ jqXHR, textStatus, errorThrown });
            }
        });
    });
}

function isJSON(str) {
    try {
        JSON.parse(str);
        return true;
    } catch (e) {
        return false;
    }
}