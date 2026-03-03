export async function inicializarModulo() {
    cargarCompras();
    cargarProveedores();
}

// Cargar proveedores en el select
async function cargarProveedores() {
    try {
        let resp = await fetch("../api/proveedor/getAll");
        let data = await resp.json();
        const select = document.getElementById("selectProveedor");
        select.innerHTML = "";
        data.forEach(p => {
            let opt = document.createElement("option");
            opt.value = p.id;
            opt.textContent = p.nombre;
            select.appendChild(opt);
        });
    } catch (err) {
        console.error("Error cargando proveedores:", err);
        Swal.fire("Error", "No se pudieron cargar los proveedores", "error");
    }
}

// Cargar historial de compras
async function cargarCompras() {
    try {
        let resp = await fetch("../api/compra/getAll");
        let data = await resp.json();
        const tbody = document.getElementById("tbodyCompras");
        tbody.innerHTML = "";

        if (data.length === 0) {
            tbody.innerHTML = "<tr><td colspan='5' class='text-center'>No hay compras registradas</td></tr>";
            return;
        }

        data.forEach(c => {
            const tr = document.createElement("tr");
            tr.style.cursor = "pointer";
            tr.onclick = () => seleccionarCompra(c);

            tr.innerHTML = `
        <td>${c.idCompra}</td>
        <td>${c.fechaCompra}</td>
        <td>${c.proveedor.nombre}</td>
      `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        console.error("Error cargando compras:", err);
        Swal.fire("Error", "No se pudieron cargar las compras", "error");
    }
}

// Seleccionar compra
function seleccionarCompra(c) {
    document.getElementById("txtIdCompra").value = c.idCompra;
    document.getElementById("txtFechaCompra").value = c.fechaCompra;
    document.getElementById("selectProveedor").value = c.proveedor.id;
    // Aquí podrías cargar detalles si el REST los devuelve
}

// Agregar detalle de compra
function agregarDetalle() {
    const tbody = document.getElementById("tbodyDetalles");
    const tr = document.createElement("tr");
    tr.innerHTML = `
    <td><input type="text" class="form-control" placeholder="Producto ID"></td>
    <td><input type="number" class="form-control" placeholder="Kilos"></td>
    <td><input type="number" class="form-control" placeholder="Precio"></td>
    <td><input type="number" class="form-control" placeholder="Descuento"></td>
    <td><button type="button" class="btn btn-danger btn-sm" onclick="this.closest('tr').remove()">X</button></td>
  `;
    tbody.appendChild(tr);
}

// Guardar compra
async function guardarCompra() {
    let compra = {
        idCompra: document.getElementById("txtIdCompra").value,
        fechaCompra: document.getElementById("txtFechaCompra").value,
        proveedor: {id: document.getElementById("selectProveedor").value},
        detalles: []
    };

    document.querySelectorAll("#tbodyDetalles tr").forEach(tr => {
        let inputs = tr.querySelectorAll("input");
        compra.detalles.push({
            producto: {id: inputs[0].value},
            kilos: inputs[1].value,
            precioCompra: inputs[2].value,
            descuento: inputs[3].value
        });
    });

    try {
        let resp = await fetch("../api/compra/insert", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
            body: new URLSearchParams({compra: JSON.stringify(compra)})
        });

        let data = await resp.json();
        if (data.exception) {
            Swal.fire("Error", data.exception, "error");
        } else {
            Swal.fire("Éxito", "Compra guardada correctamente", "success");
            cargarCompras();
            document.getElementById("formCompra").reset();
            document.getElementById("tbodyDetalles").innerHTML = "";
        }
    } catch (err) {
        console.error("Error al guardar compra:", err);
        Swal.fire("Error", "No se pudo conectar con el servidor", "error");
    }
}

// Eliminar compra
async function eliminarCompra() {
    let id = document.getElementById("txtIdCompra").value;
    if (id == 0) {
        Swal.fire("Atención", "Selecciona una compra para eliminar", "info");
        return;
    }

    const confirmacion = await Swal.fire({
        title: "¿Eliminar compra?",
        text: "Esta acción no se puede deshacer",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Sí, eliminar",
        cancelButtonText: "Cancelar"
    });

    if (!confirmacion.isConfirmed)
        return;

    try {
        let resp = await fetch("../api/compra/delete", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded"},
            body: new URLSearchParams({idCompra: id})
        });

        let data = await resp.json();
        if (data.exception) {
            Swal.fire("Error", data.exception, "error");
        } else {
            Swal.fire("Éxito", "Compra eliminada", "success");
            cargarCompras();
            document.getElementById("formCompra").reset();
            document.getElementById("tbodyDetalles").innerHTML = "";
        }
    } catch (err) {
        console.error("Error al eliminar compra:", err);
        Swal.fire("Error", "No se pudo conectar con el servidor", "error");
    }
}

// Ejecutar al cargar la página
document.addEventListener("DOMContentLoaded", () => {
    cargarProveedores();
    cargarCompras();
});
