export async function inicializarModulo() {
    cargarVendedores();
}

// Cargar todos los vendedores
async function cargarVendedores() {
    try {
        let resp = await fetch("../api/vendedor/getAll");
        let data = await resp.json();
        const tbody = document.getElementById("tbodyVendedores");
        tbody.innerHTML = "";

        if (data.length === 0) {
            tbody.innerHTML = "<tr><td colspan='6' class='text-center'>No hay vendedores registrados</td></tr>";
            return;
        }

        data.forEach(v => {
            const tr = document.createElement("tr");
            tr.style.cursor = "pointer";
            tr.onclick = () => seleccionarVendedor(v);

            tr.innerHTML = `
        <td>${v.id}</td>
        <td>${v.nombre}</td>
        <td>${v.email}</td>
        <td>${v.telefono}</td>
        <td>${v.fechaNacimiento}</td>
        <td>${v.fechaAlta}</td>
      `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        console.error("Error cargando vendedores:", err);
        Swal.fire("Error", "No se pudieron cargar los vendedores", "error");
    }
}

// Seleccionar un vendedor de la tabla
function seleccionarVendedor(v) {
    document.getElementById("txtIdVendedor").value = v.id;
    document.getElementById("txtNombreVendedor").value = v.nombre;
    document.getElementById("txtEmailVendedor").value = v.email;
    document.getElementById("txtTelefonoVendedor").value = v.telefono;
    document.getElementById("txtFechaNacimiento").value = v.fechaNacimiento;
    document.getElementById("txtFechaIngreso").value = v.fechaAlta;
}

// Guardar vendedor (insertar o actualizar)
async function guardarVendedor() {
    let params = {
        id: document.getElementById("txtIdVendedor").value,
        nombre: document.getElementById("txtNombreVendedor").value,
        email: document.getElementById("txtEmailVendedor").value,
        telefono: document.getElementById("txtTelefonoVendedor").value,
        fechaNacimiento: document.getElementById("txtFechaNacimiento").value,
        fechaIngreso: document.getElementById("txtFechaIngreso").value
    };

    try {
        let resp = await fetch("../api/vendedor/insert", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
            body: new URLSearchParams(params)
        });

        let data = await resp.json();
        if (data.exception) {
            Swal.fire("Error", data.exception, "error");
        } else {
            Swal.fire("Éxito", "Vendedor guardado correctamente", "success");
            nuevoVendedor();
            cargarVendedores();
        }
    } catch (err) {
        console.error("Error al guardar:", err);
        Swal.fire("Error", "No se pudo conectar con el servidor", "error");
    }
}

// Eliminar vendedor
async function eliminarVendedor() {
    let id = document.getElementById("txtIdVendedor").value;
    if (id == 0) {
        Swal.fire("Atención", "Selecciona un vendedor para eliminar", "info");
        return;
    }

    const confirmacion = await Swal.fire({
        title: "¿Eliminar vendedor?",
        text: "Esta acción no se puede deshacer",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Sí, eliminar",
        cancelButtonText: "Cancelar"
    });

    if (!confirmacion.isConfirmed)
        return;

    try {
        let resp = await fetch("../api/vendedor/delete", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded"},
            body: new URLSearchParams({idVendedor: id})
        });

        let data = await resp.json();
        if (data.exception) {
            Swal.fire("Error", data.exception, "error");
        } else {
            Swal.fire("Éxito", "Vendedor eliminado", "success");
            nuevoVendedor();
            cargarVendedores();
        }
    } catch (err) {
        console.error("Error al eliminar:", err);
        Swal.fire("Error", "No se pudo conectar con el servidor", "error");
    }
}

// Limpiar formulario
function limpiarVendedor() {
    document.getElementById("formVendedor").reset();
    document.getElementById("txtIdVendedor").value = "0";
}

// Ejecutar al cargar la página
document.addEventListener("DOMContentLoaded", () => {
    cargarVendedores();
});