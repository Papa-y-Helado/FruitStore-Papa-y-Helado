async function login()
{
    let nombreUsuario = document.getElementById("txtUsuario").value;
    let contrasenia = document.getElementById("txtContrasenia").value;
    let url = "api/usuario/login";
    let params =
            {
                nombre: nombreUsuario,
                contrasenia: contrasenia
            };
    let confServ = {
        method: "POST",
        headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
        body: new URLSearchParams(params)
    };
    let resp = await fetch(url, confServ);
    let data = await resp.json();
    //alert(JSON.stringify(data));

    if (data.error != null)
    {
        Swal.fire('Error', data.error, 'Warning');
        return;
    } else if (data.exception != null)
    {
        Swal.fire('Error en el servidor', data.exception, 'error');
        return;
    } else
    {
        window.location.href = 'modulos/index.html';
    }
}

function logout()
{

}
