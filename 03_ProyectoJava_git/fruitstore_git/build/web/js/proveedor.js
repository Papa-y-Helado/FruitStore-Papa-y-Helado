export async function inicializarModulo() {
    cargarProveedores();
}

// Cargar todos los proveedores
async function cargarProveedores() {
    try {
        let resp = await fetch("../api/proveedor/getAll");
        let data = await resp.json();
        const tbody = document.getElementById("tbodyProveedores");
        tbody.innerHTML = "";

        if (data.length === 0) {
            tbody.innerHTML = "<tr><td colspan='5' class='text-center'>No hay proveedores registrados</td></tr>";
            return;
        }

        data.forEach(p => {
            const tr = document.createElement("tr");
            tr.style.cursor = "pointer";
            tr.onclick = () => seleccionarProveedor(p);

            tr.innerHTML = `
        <td>${p.id}</td>
        <td>${p.nombre}</td>
        <td>${p.email}</td>
        <td>${p.telefono}</td>
        <td>${p.ciudad}</td>
      `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        console.error("Error cargando proveedores:", err);
        Swal.fire("Error", "No se pudieron cargar los proveedores", "error");
    }
}

// Seleccionar un proveedor de la tabla
function seleccionarProveedor(p) {
    document.getElementById("txtIdProveedor").value = p.id;
    document.getElementById("txtNombreProveedor").value = p.nombre;
    document.getElementById("txtEmailProveedor").value = p.email;
    document.getElementById("txtTelefonoProveedor").value = p.telefono;
    document.getElementById("txtCiudadProveedor").value = p.ciudad;
}

// Guardar proveedor (insertar o actualizar)
async function guardarProveedor() {
    let params = {
        id: document.getElementById("txtIdProveedor").value,
        nombre: document.getElementById("txtNombreProveedor").value,
        email: document.getElementById("txtEmailProveedor").value,
        telefono: document.getElementById("txtTelefonoProveedor").value,
        ciudad: document.getElementById("txtCiudadProveedor").value
    };

    try {
        let resp = await fetch("../api/proveedor/insert", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
            body: new URLSearchParams(params)
        });

        let data = await resp.json();
        if (data.exception) {
            Swal.fire("Error", data.exception, "error");
        } else {
            Swal.fire("Éxito", "Proveedor guardado correctamente", "success");
            nuevoProveedor();
            cargarProveedores();
        }
    } catch (err) {
        console.error("Error al guardar:", err);
        Swal.fire("Error", "No se pudo conectar con el servidor", "error");
    }
}

// Eliminar proveedor
async function eliminarProveedor() {
    let id = document.getElementById("txtIdProveedor").value;
    if (id == 0) {
        Swal.fire("Atención", "Selecciona un proveedor para eliminar", "info");
        return;
    }

    const confirmacion = await Swal.fire({
        title: "¿Eliminar proveedor?",
        text: "Esta acción no se puede deshacer",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Sí, eliminar",
        cancelButtonText: "Cancelar"
    });

    if (!confirmacion.isConfirmed)
        return;

    try {
        let resp = await fetch("../api/proveedor/delete", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded"},
            body: new URLSearchParams({idProveedor: id})
        });

        let data = await resp.json();
        if (data.exception) {
            Swal.fire("Error", data.exception, "error");
        } else {
            Swal.fire("Éxito", "Proveedor eliminado", "success");
            nuevoProveedor();
            cargarProveedores();
        }
    } catch (err) {
        console.error("Error al eliminar:", err);
        Swal.fire("Error", "No se pudo conectar con el servidor", "error");
    }
}

// Limpiar formulario
function agregarProveedor() {
    document.getElementById("formProveedor").reset();
    document.getElementById("txtIdProveedor").value = "0";
}

// Evitar letras en los números telefónicos
function soloNumeros(input) {
    input.value = input.value.replace(/[^0-9]/g, '');
}

document.getElementById("txtTelefonoFijo").addEventListener("input", function () {
    soloNumeros(this);
});

document.getElementById("txtTelefonoMovil").addEventListener("input", function () {
    soloNumeros(this);
});
