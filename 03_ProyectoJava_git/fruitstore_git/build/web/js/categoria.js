export async function inicializarModulo() {
    cargarCategorias();
}

async function cargarCategorias() {
    try {
        let resp = await fetch("../api/categoria/getAll");
        let data = await resp.json();
        const tbody = document.getElementById("tbodyCategorias");
        tbody.innerHTML = "";

        if (data.length === 0) {
            tbody.innerHTML = "<tr><td colspan='2' class='text-center'>No hay categorías registradas</td></tr>";
            return;
        }

        data.forEach(c => {
            const tr = document.createElement("tr");
            tr.style.cursor = "pointer";
            tr.onclick = () => seleccionarCategoria(c);

            /*
             tr.innerHTML = `
             <td>${c.id}</td>
             <td>${c.nombre}</td>
             `;
             // */
            tr.innerHTML = `
        <td>${c.nombre}</td>
      `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        console.error("Error cargando categorías:", err);
        Swal.fire("Error", "No se pudieron cargar las categorías", "error");
    }
}

function seleccionarCategoria(c) {
    document.getElementById("txtIdCategoria").value = c.id;
    document.getElementById("txtNombreCategoria").value = c.nombre;
}

async function guardarCategoria() {
    let params = {
        id: document.getElementById("txtIdCategoria").value,
        nombre: document.getElementById("txtNombreCategoria").value
    };

    try {
        let resp = await fetch("../api/categoria/insert", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
            body: new URLSearchParams(params)
        });

        let data = await resp.json();
        if (data.exception) {
            Swal.fire("Error", data.exception, "error");
        } else {
            Swal.fire("Éxito", "Categoría guardada correctamente", "success");
            // nuevaCategoria();
            cargarCategorias();
        }
    } catch (err) {
        console.error("Error al guardar:", err);
        Swal.fire("Error", "No se pudo conectar con el servidor", "error");
    }
}

async function eliminarCategoria() {
    let id = document.getElementById("txtIdCategoria").value;
    if (id == 0) {
        Swal.fire("Atención", "Selecciona una categoría para eliminar", "info");
        return;
    }

    const confirmacion = await Swal.fire({
        title: "¿Eliminar categoría?",
        text: "Esta acción no se puede deshacer",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Sí, eliminar",
        cancelButtonText: "Cancelar"
    });

    if (!confirmacion.isConfirmed)
        return;

    try {
        let resp = await fetch("../api/categoria/delete", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded"},
            body: new URLSearchParams({idCategoria: id})
        });

        let data = await resp.json();
        if (data.exception) {
            Swal.fire("Error", data.exception, "error");
        } else {
            Swal.fire("Éxito", "Categoría eliminada", "success");
            nuevaCategoria();
            cargarCategorias();
        }
    } catch (err) {
        console.error("Error al eliminar:", err);
        Swal.fire("Error", "No se pudo conectar con el servidor", "error");
    }
}

function limpiarCategoria() {
    document.getElementById("txtNombreCategoria").value = "";
    document.getElementById("txtIdCategoria").value = "0";
}

document.addEventListener("DOMContentLoaded", () => {
    cargarCategorias();
});
