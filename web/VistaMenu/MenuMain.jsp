<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Menú principal</title>
    <style>
        * { box-sizing: border-box; }

        body {
            font-family: 'Poppins', Arial, sans-serif;
            margin: 0;
            min-height: 100vh;
            background: radial-gradient(circle at top left, #1e3c72, #2a5298 45%, #151b3b 90%);
            color: #f8faff;
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 40px 20px 60px;
        }

        h1 {
            font-size: 42px;
            margin: 0 0 12px;
            letter-spacing: 1px;
            text-align: center;
        }

        .descripcion {
            font-size: 18px;
            margin-bottom: 40px;
            text-align: center;
            max-width: 720px;
            line-height: 1.5;
            color: rgba(248, 250, 255, 0.85);
        }

        .grid {
            width: 100%;
            max-width: 1100px;
            display: grid;
            gap: 28px;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
        }

        .card {
            position: relative;
            background: rgba(255, 255, 255, 0.08);
            backdrop-filter: blur(12px);
            border-radius: 18px;
            padding: 28px 24px;
            text-decoration: none;
            color: inherit;
            box-shadow: 0 18px 40px rgba(12, 22, 60, 0.35);
            transition: transform 0.25s ease, box-shadow 0.25s ease, background 0.25s ease;
            overflow: hidden;
        }

        .card::after {
            content: "";
            position: absolute;
            inset: 0;
            border-radius: 18px;
            border: 1px solid rgba(255, 255, 255, 0.25);
            pointer-events: none;
        }

        .card:hover {
            transform: translateY(-6px);
            box-shadow: 0 24px 50px rgba(10, 18, 45, 0.45);
            background: rgba(255, 255, 255, 0.14);
        }

        .card-icon {
            font-size: 36px;
            margin-bottom: 14px;
        }

        .card h2 {
            margin: 0;
            font-size: 22px;
            font-weight: 700;
        }

        .card p {
            margin-top: 12px;
            font-size: 15px;
            line-height: 1.5;
            color: rgba(248, 250, 255, 0.8);
        }

        @media (max-width: 600px) {
            body { padding: 32px 16px; }
            h1 { font-size: 32px; }
            .descripcion { font-size: 16px; }
        }

        .logout {
            margin-top: 50px;
            color: #ffbaba;
            text-decoration: none;
            font-weight: 600;
            transition: color 0.2s;
        }

        .logout:hover {
            color: #ffffff;
        }
    </style>
</head>
<body>
    <h1>Panel principal</h1>
<p class="descripcion">
    Selecciona el módulo que deseas administrar para gestionar catálogos, productos y toda la información de tu tienda.
</p>

<main class="grid">
    <a class="card" href="<c:url value='/srvCategoria'/>">
        <div class="card-icon">📂</div>
        <h2>Categorías</h2>
        <p>Organiza las categorías disponibles para clasificar tus productos.</p>
    </a>

    <a class="card" href="<c:url value='/srvProducto'/>">
        <div class="card-icon">📦</div>
        <h2>Productos</h2>
        <p>Gestiona el catálogo de productos y su información principal.</p>
    </a>

    <a class="card" href="<c:url value='/srvProductoTalla'/>">
        <div class="card-icon">🧮</div>
        <h2>Producto - Talla</h2>
        <p>Relaciona productos con tallas disponibles y controla su stock.</p>
    </a>

    <a class="card" href="<c:url value='/srvColor'/>">
        <div class="card-icon">🎨</div>
        <h2>Colores</h2>
        <p>Administra la paleta de colores disponibles para los productos.</p>
    </a>

    <a class="card" href="<c:url value='/srvTalla'/>">
        <div class="card-icon">📏</div>
        <h2>Tallas</h2>
        <p>Controla las tallas registradas y su disponibilidad.</p>
    </a>

    <a class="card" href="<c:url value='/srvTipoTalla'/>">
        <div class="card-icon">🧵</div>
        <h2>Tipos de Talla</h2>
        <p>Define los tipos de tallas y cómo se agrupan en el sistema.</p>
    </a>

    <a class="card" href="<c:url value='/srvMarca'/>">
        <div class="card-icon">🏷️</div>
        <h2>Marcas</h2>
        <p>Gestiona las marcas asociadas a los productos de la tienda.</p>
    </a>

    <a class="card" href="<c:url value='/srvModelo'/>">
        <div class="card-icon">🧩</div>
        <h2>Modelos</h2>
        <p>Registra los modelos de productos y sus características.</p>
    </a>

    <a class="card" href="<c:url value='/srvCliente'/>">
        <div class="card-icon">🧑‍🤝‍🧑</div>
        <h2>Clientes</h2>
        <p>Accede al directorio de clientes y gestiona su información.</p>
    </a>

    <a class="card" href="<c:url value='/srvEmpleado'/>">
        <div class="card-icon">👥</div>
        <h2>Empleados</h2>
        <p>Controla los datos de los colaboradores de la organización.</p>
    </a>

    <a class="card" href="<c:url value='/srvCargo'/>">
        <div class="card-icon">💼</div>
        <h2>Cargos</h2>
        <p>Define los cargos o roles que pueden ocupar los empleados.</p>
    </a>
        
        <a class="card" href="<c:url value='/catalogo'/>">
        <div class="card-icon">🛒</div>
        <h2>Catálogo</h2>
        <p>Consulta el catálogo público sincronizado con tu inventario.</p>
    </a>
</main>

    </main>

    <a class="logout" href="<c:url value='/logout'/>">Cerrar sesión</a>
</body>
</html>
