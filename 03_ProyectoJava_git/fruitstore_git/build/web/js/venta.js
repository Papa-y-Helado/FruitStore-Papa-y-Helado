export async function inicializarModulo() {
    cargarVendedores();
    cargarVentas();
}

// Cargar vendedores
async function cargarVendedores() {
    try {
        let resp = await fetch("../api/vendedor/getAll");
        let data = await resp.json();
        const select = document.getElementById("selectVendedor");
        select.innerHTML = "";
        data.forEach(v => {
            let opt = document.createElement("option");
            opt.value = v.id;
            opt.textContent = v.nombre;
            select.appendChild(opt);
        });
    } catch (err) {
        console.error("Error cargando vendedores:", err);
        Swal.fire("Error", "No se pudieron cargar los vendedores", "error");
    }
}

// Cargar historial de ventas
async function cargarVentas() {
    try {
        let resp = await fetch("../api/venta/getAllResumen");
        let data = await resp.json();
        const tbody = document.getElementById("tbodyVentas");
        tbody.innerHTML = "";

        data.forEach(v => {
            const tr = document.createElement("tr");
            tr.style.cursor = "pointer";
            tr.onclick = () => seleccionarVenta(v);

            tr.innerHTML = `
        <td>${v.id}</td>
        <td>${v.fecha}</td>
        <td>${v.vendedor.nombre}</td>
        <td>${parseFloat(v.total).toFixed(2)}</td>
      `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        console.error("Error cargando ventas:", err);
        Swal.fire("Error", "No se pudieron cargar las ventas", "error");
    }
}

// Seleccionar venta y cargar detalles
async function seleccionarVenta(v) {
    document.getElementById("txtIdVenta").value = v.id;
    document.getElementById("txtFechaVenta").value = v.fecha;
    document.getElementById("selectVendedor").value = v.vendedor.id;

    try {
        let resp = await fetch("../api/venta/getByID?idVenta=" + v.id);
        let data = await resp.json();

        const tbody = document.getElementById("tbodyProductos");
        tbody.innerHTML = "";
        /*
         data.productos.forEach(p => {
         const tr = document.createElement("tr");
         tr.innerHTML = `
         <td>${p.id}</td>
         <td>${p.cantidad}</td>
         <td>${p.precioVenta}</td>
         <td>${p.descuento}</td>
         <td><button type="button" class="btn btn-danger btn-sm" onclick="this.closest('tr').remove()">X</button></td>
         `;
         tbody.appendChild(tr);
         });

         */
    } catch (err) {
        console.error("Error cargando detalles de venta:", err);
        Swal.fire("Error", "No se pudieron cargar los detalles de las ventas", "error");
    }
}

// Agregar producto
function agregarProducto() {
    const tbody = document.getElementById("tbodyProductos");
    const tr = document.createElement("tr");
    tr.innerHTML = `
    <td><input type="text" class="form-control" placeholder="Producto ID"></td>
    <td><input type="number" class="form-control" placeholder="Cantidad"></td>
    <td><input type="number" class="form-control" placeholder="Precio Venta"></td>
    <td><input type="number" class="form-control" placeholder="Descuento"></td>
    <td><button type="button" class="btn btn-danger btn-sm" onclick="this.closest('tr').remove()">X</button></td>
  `;
    tbody.appendChild(tr);
}

// Guardar venta
async function guardarVenta() {
    let productos = [];
    document.querySelectorAll("#tbodyProductos tr").forEach(tr => {
        let inputs = tr.querySelectorAll("input");
        if (inputs.length > 0) {
            productos.push({
                id: inputs[0].value,
                cantidad: parseFloat(inputs[1].value) || 0,
                precioVenta: parseFloat(inputs[2].value) || 0,
                descuento: parseFloat(inputs[3].value) || 0
            });
        }
    });

    if (productos.length === 0) {
        Swal.fire("Atención", "Agrega al menos un producto", "info");
        return;
    }

    let venta = {
        id: document.getElementById("txtIdVenta").value,
        fecha: document.getElementById("txtFechaVenta").value,
        vendedor: {id: document.getElementById("selectVendedor").value},
        productos: productos
    };

    // ... resto igual que antes
}

// Eliminar venta
async function eliminarVenta() {
    let id = document.getElementById("txtIdVenta").value;
    if (id == 0) {
        Swal.fire("Atención", "Selecciona una venta para eliminar", "info");
        return;
    }

    const confirmacion = await Swal.fire({
        title: "¿Eliminar venta?",
        text: "Esta acción no se puede deshacer",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Sí, eliminar",
        cancelButtonText: "Cancelar"
    });

    if (!confirmacion.isConfirmed)
        return;

    try {
        let resp = await fetch("../api/venta/update", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded"},
            body: new URLSearchParams({idVenta: id})
        });

        let data = await resp.json();
        if (data.exception) {
            Swal.fire("Error", data.exception, "error");
        } else {
            Swal.fire("Éxito", "Venta eliminada (estatus=2)", "success");
            cargarVentas();
            document.getElementById("formVenta").reset();
            document.getElementById("tbodyProductos").innerHTML = "";
        }
    } catch (err) {
        console.error("Error al eliminar venta:", err);
        Swal.fire("Error", "No se pudo conectar con el servidor", "error");
    }
}

// Ejecutar al cargar la página
document.addEventListener("DOMContentLoaded", () => {
    cargarVendedores();
    cargarVentas();
});