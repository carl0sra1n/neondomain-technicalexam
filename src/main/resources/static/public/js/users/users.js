let table;

let isUpdate = false;
let isDelete = false;

const dataTableColumns = [
    { data: 'id' },
    { data: 'userName' },
    { data: 'firstName' },
    { data: 'lastName' },
    { data: 'age' },
    {
        data: null,
        defaultContent: '<button class="btn btn-warning btn-sm edit-button">Edit</button>',
    },
];

$(document).ready(async function () {
    $('input').each(function () {
        $(this).attr({
            'autocomplete': 'new-password',
        });
    });

    table = initializeDataTable('#tblUsuarios', {
        columns: dataTableColumns
    });

    await loadTable();
});

$("#openModal").click(async function () {
    $('#registerModal').modal('toggle');
});

$("#btnLogout").click(async function () {
    window.location.href = "/public/login?logout";
});

$('#tblUsuarios tbody').on('click', 'tr', function () {
    const data = $('#tblUsuarios').DataTable().row( this ).data();
    openEditModal(data);
});

$('#registerModal').on('hidden.bs.modal', function () {
    isUpdate = false;
    
    $('#id').val(null);
    $('#userName').val(null);
    $('#firstName').val(null);
    $('#lastName').val(null);
    $('#age').val(null);
    $('#password').val(null);

    $("#deleteButton").html("");
});

async function loadTable() {
    destroyDataTable('#tblUsuarios');

    const table = initializeDataTable('#tblUsuarios', {
        columns: dataTableColumns
    });

    try {
        const token = localStorage.getItem('jwtToken');
        const users = await getUsers(token);

        updateDataTable(table, users);
    } catch (error) {
        handleDataTableError(error);
    }
}

async function getUsers(token) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: "/api/users",
            method: "GET",
            headers: {
                'Authorization': 'Bearer ' + token,
            },
            success: function (data) {
                resolve(data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                reject({ jqXHR, textStatus, errorThrown });
            }
        });
    });
}

async function saveForm() {
    const payload = getFormPayload();

    const token = localStorage.getItem('jwtToken');

    try {
        const user = await userActions(token, payload);

        showSuccessToast("Success", isDelete ? "User deleted successful" : isUpdate ? "User updated successful" : "User created successful");

        isUpdate = false;
        isDelete = false;

        closeRegisterModal();
        await loadTable();
    } catch (error) {
        handleSaveFormError(error);
    }
}

async function userActions(token, user) {
    let { id, ...payload } = user;

    return new Promise((resolve, reject) => {
        $.ajax({
            url: isUpdate ? `/api/users/${user.id}` : "/api/users",
            method: isDelete ? "DELETE" : isUpdate ? "PATCH" : "POST",
            headers: {
                'Authorization': 'Bearer ' + token,
            },
            data: payload,
            success: function (data) {
                resolve(data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
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

function destroyDataTable(selector) {
    if ($.fn.DataTable.isDataTable(selector)) {
        $(selector).DataTable().destroy();
    }
}

function initializeDataTable(selector, options) {
    return new DataTable(selector, options);
}

function updateDataTable(table, data) {
    table.clear();
    table.rows.add(data);
    table.draw();
}

function handleDataTableError(error) {
    if (error.jqXHR.status === 403) {
        window.location.href = "/public/login";
    } else {
        const errText = extractErrorMessage(error);
        showErrorToast("There was an error", errText);
    }
}

function getFormPayload() {
    return {
        "id": $('#id').val(),
        "userName": $('#userName').val(),
        "firstName": $('#firstName').val(),
        "lastName": $('#lastName').val(),
        "age": $('#age').val(),
        "password": $('#password').val(),
    };
}

function handleSaveFormError(error) {
    console.log(error);
    if (error.jqXHR.status === 403) {
        window.location.href = "/public/login";
    } else {
        showErrorToast("There was an error", !JSON.parse(error.jqXHR.responseText).details ? JSON.parse(error.jqXHR.responseText).message : JSON.parse(error.jqXHR.responseText).message + "<br><br>" + JSON.parse(error.jqXHR.responseText).details.join("<br>"));
    }
}

function extractErrorMessage(error) {
    let errText = error.jqXHR.responseText;

    if (isJSON(error.jqXHR.responseText)) {
        const response = JSON.parse(error.jqXHR.responseText);
        errText = response.error;
    }

    return errText;
}

function showSuccessToast(title, text) {
    Swal.fire({
        icon: "success",
        title: title,
        text: text,
        toast: true
    });
}

function showErrorToast(title, text) {
    Swal.fire({
        icon: "error",
        title: title,
        html: text,
        toast: true
    });
}

function openEditModal(data) {
    isUpdate = true;

    $('#id').val(data.id);
    $('#userName').val(data.userName);
    $('#firstName').val(data.firstName);
    $('#lastName').val(data.lastName);
    $('#age').val(data.age);

    $("#deleteButton").html("<button type=\"button\" class=\"btn btn-danger\" onclick=\"isDelete=true;saveForm();\" id=\"btnDelete\">Delete</button>");
    
    $('#registerModal').modal('toggle');
}

function closeRegisterModal() {
    $('#registerModal').modal('toggle');
}
