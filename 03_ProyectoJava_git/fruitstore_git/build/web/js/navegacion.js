let cm = null;

// Categorías
async function irACategorias() {
    let resp = await fetch('categoria.html');
    let cont = await resp.text();
    document.getElementById("contenedorPrincipal").innerHTML = cont;
    cm = await import('../js/categoria.js');
    cm.inicializarModulo();
}

// Compras
async function irACompras() {
    let resp = await fetch('compra.html');
    let cont = await resp.text();
    document.getElementById("contenedorPrincipal").innerHTML = cont;
    cm = await import('../js/compra.js');
    cm.inicializarModulo();
}

// Productos
async function irAProductos() {
    let resp = await fetch('producto.html');
    let cont = await resp.text();
    document.getElementById("contenedorPrincipal").innerHTML = cont;
    cm = await import('../js/producto.js');
    cm.inicializarModulo();
}

// Vendedores
async function irAVendedores() {
    let resp = await fetch('vendedor.html');
    let cont = await resp.text();
    document.getElementById("contenedorPrincipal").innerHTML = cont;
    cm = await import('../js/vendedor.js');
    cm.inicializarModulo();
}

// Proveedores
async function irAProveedores() {
    let resp = await fetch('proveedor.html');
    let cont = await resp.text();
    document.getElementById("contenedorPrincipal").innerHTML = cont;
    cm = await import('../js/proveedor.js');
    cm.inicializarModulo();
}

// Ventas
async function irAVentas() {
    let resp = await fetch('venta.html');
    let cont = await resp.text();
    document.getElementById("contenedorPrincipal").innerHTML = cont;
    cm = await import('../js/venta.js');
    cm.inicializarModulo();
}

// Cerrar sesión
function cerrarSesion() {
    Swal.fire({
        title: "¿Cerrar sesión?",
        text: "Se cerrará tu sesión actual",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Sí, salir",
        cancelButtonText: "Cancelar"
    }).then(result => {
        if (result.isConfirmed) {
            window.location.href = "../index.html";
        }
    });
}

// \\ Cargar navbar en páginas \\ //
fetch("navbar.html")
        .then(resp => resp.text())
        .then(html => {
            document.getElementById("navbarContainer").innerHTML = html;
        });