export async function inicializarModulo() {
    cargarProductos();
    cargarCategorias();
}

async function cargarProductos() {
    try {
        let resp = await fetch("../api/producto/getAll");
        let data = await resp.json();
        const tbody = document.getElementById("tbodyProductos");
        tbody.innerHTML = "";

        if (data.length === 0) {
            tbody.innerHTML = "<tr><td colspan='6' class='text-center'>No hay productos registrados</td></tr>";
            return;
        }

        data.forEach(p => {
            const tr = document.createElement("tr");
            tr.style.cursor = "pointer";
            tr.onclick = () => seleccionarProducto(p);

            tr.innerHTML = `
        <td>${p.id}</td>
        <td>${p.nombre}</td>
        <td>${p.categoria ? p.categoria.nombre : "S/C"}</td>
        <td>$${parseFloat(p.precioCompra).toFixed(2)}</td>
        <td>$${parseFloat(p.precioVenta).toFixed(2)}</td>
        <td>${p.existencia}</td>
        <td>${p.status}</td>
      `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        console.error("Error cargando productos:", err);
        Swal.fire("Error", "No se pudieron cargar los productos", "error");
    }
}

async function cargarCategorias() {
    try {
        let resp = await fetch("../api/categoria/getAll");
        let data = await resp.json();
        const cmb = document.getElementById("cmbCategoria");
        cmb.innerHTML = '<option value="0">-- Seleccionar categoría --</option>';
        data.forEach(c => {
            cmb.innerHTML += `<option value="${c.id}">${c.nombre}</option>`;
        });
    } catch (err) {
        console.error("Error cargando categorías:", err);
        Swal.fire("Error", "No se pudieron cargar las categorías", "error");
    }
}

function seleccionarProducto(p) {
    document.getElementById("txtIdProducto").value = p.id;
    document.getElementById("txtNombreProducto").value = p.nombre;
    document.getElementById("txtPrecioCompra").value = p.precioCompra;
    document.getElementById("txtPrecioVenta").value = p.precioVenta;
    document.getElementById("txtExistencia").value = p.existencia;
    document.getElementById("cmbCategoria").value = p.categoria ? p.categoria.id : "0";
}

/*
 async function guardarProducto() {
 let params = {
 id: document.getElementById("txtIdProducto").value,
 nombre: document.getElementById("txtNombreProducto").value,
 idCategoria: document.getElementById("cmbCategoria").value,
 precioCompra: document.getElementById("txtPrecioCompra").value,
 precioVenta: document.getElementById("txtPrecioVenta").value,
 existencia: document.getElementById("txtExistencia").value
 };

 try {
 let resp = await fetch("../api/producto/insert", {
 method: "POST",
 headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
 body: new URLSearchParams(params)
 });

 let data = await resp.json();
 if (data.exception) {
 Swal.fire("Error", data.exception, "error");
 } else {
 Swal.fire("Éxito", "Producto guardado correctamente", "success");
 nuevoProducto();
 cargarProductos();
 }
 } catch (err) {
 console.error("Error al guardar:", err);
 Swal.fire("Error", "No se pudo conectar con el servidor", "error");
 }
 }
 */

async function eliminarProducto() {
    let id = document.getElementById("txtIdProducto").value;
    if (id == 0) {
        Swal.fire("Atención", "Selecciona un producto para eliminar", "info");
        return;
    }

    const confirmacion = await Swal.fire({
        title: "¿Eliminar producto?",
        text: "Esta acción no se puede deshacer",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Sí, eliminar",
        cancelButtonText: "Cancelar"
    });

    if (!confirmacion.isConfirmed)
        return;

    try {
        let resp = await fetch("api/producto/delete", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded"},
            body: new URLSearchParams({idProducto: id})
        });

        let data = await resp.json();
        if (data.exception) {
            Swal.fire("Error", data.exception, "error");
        } else {
            Swal.fire("Éxito", "Producto eliminado", "success");
            nuevoProducto();
            cargarProductos();
        }
    } catch (err) {
        console.error("Error al eliminar:", err);
        Swal.fire("Error", "No se pudo conectar con el servidor", "error");
    }
}

function limpiarProducto() {
    document.getElementById("formProducto").reset();
    document.getElementById("txtIdProducto").value = "0";
}

async function guardarProducto() {
    let params = {
        nombre: document.getElementById("txtNombreProducto").value,
        idCategoria: document.getElementById("cmbCategoria").value,
        precioCompra: document.getElementById("txtPrecioCompra").value,
        precioVenta: document.getElementById("txtPrecioVenta").value,
        existencia: document.getElementById("txtExistencia").value,
    };

    try {
        let resp = await fetch("api/producto/insert", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
            body: new URLSearchParams(params)
        });

        let data = await resp.json();

        if (data.exception) {
            alert("Error: " + data.exception);
        } else {
            alert("Producto procesado correctamente");
            cargarProductos();
            nuevoProducto();
        }
    } catch (err) {
        console.error("Error al guardar:", err);
        Swal.fire("Error", "Error de conexión con el servidor", "error");
    }
}