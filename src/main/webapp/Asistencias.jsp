<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Inicio - Colegio Altair Sevilla</title>
<link rel="stylesheet" href="Css/Estilo.css">
<!-- Bootstrap CSS (solo una vez) -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet" crossorigin="anonymous">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css"
	rel="stylesheet">

</head>

<body>
	<!-- HEADER -->
	<header>

		<div class="container-fluid ">
			<div class="row">
				<div class="col-md-2 col-sm-2 col-2"
					style="padding-left: 0; padding-right: 0;">
					<div class="logo">
						<img class="logoAltair" src="Imagenes/logoAltair.jpg"
							alt="Colegio Altair Sevilla">
					</div>
				</div>
				<div class="col-md-8 col-sm-8 col-8">
					<div class="row">
						<div class="col-md-3 col-sm-3 col-3 alineacion" style="">
							<a href="Index.jsp" class="letraNavegacion">INICIO</a>
						</div>
						<div class="col-md-3 col-sm-3 col-3 alineacion">
							<a href="Asistencias.jsp" class="letraNavegacion"
								style="color: #FFD43B; text-decoration: underline">ASISTENCIAS</a>
						</div>
						<div class="col-md-3 col-sm-3 col-3 alineacion">
							<a href="Alumno.jsp" class="letraNavegacion">ALUMNOS</a>
						</div>
						<div class="col-md-3 col-sm-3 col-3 alineacion">
							<a href="Cursos.jsp" class="letraNavegacion">CURSOS</a>
						</div>
					</div>


				</div>
				<div class="col-md-2 col-sm-2 col-2"></div>
			</div>
		</div>
	</header>

	<main
		class="asistencia-main d-flex flex-column align-items-center justify-content-center">
		<div class="tabla-asistencia"
			style="width: 85%; padding-bottom: 12vw;">

			<div class="cabeza-titulos">
				<h1 class="titulo-asistencia mb-2"><u>FILTRAR FICHAJES</u></h1>
				<h2 class="subtitulo-asistencia">Consulta los registros de
					asistencia del alumnado por clase, fecha o alumno específico.
					Utiliza los filtros para encontrar rápidamente la información que
					necesites.</h2>
			</div>

			<div class="row mt-4">
				<div class="col-md-12 col-sm-12 col-12 " style="text-align: center">
					<label class="filtrado-asistencias mb-3" for="seleccion-filtro"><b>Filtrar
							por:</b></label> <select class="opciones-asistencias" id="seleccion-filtro">
						<option value="curso-fecha">Curso y Fecha</option>

						<option value="alumno-fecha">Alumno y Fecha</option>
						<option value="alumno-estado">Alumno y Estado</option>
						<option value="estadisticas">Estadísticas</option>
					</select>
				</div>
			</div>

			<!-- Filtro: Curso + Fecha -->
			<div id="seccion-curso-fecha" class="filter-section">
				<div>
					<label for="entrada-curso" class="label-formulario">Curso:</label>
					<select id="entrada-curso"></select>
				</div>
				<div>
					<label for="entrada-grupo" class="label-formulario">Grupo:</label>
					<select id="entrada-grupo"></select>
				</div>
				<div>
					<label for="entrada-fecha" class="label-formulario">Fecha:</label>
					<input type="date" id="entrada-fecha" value="">
				</div>
				<button class="filter-section button" id="boton-curso-fecha"
					disabled>Filtrar</button>
			</div>

			<!-- Filtro: Alumno + Fecha -->
			<div id="seccion-alumno-fecha" class="filter-section"
				style="display: none;">
				<div style="position: relative;">
					<input type="hidden" id="entrada-alumno-fecha-id"> <label
						for="entrada-alumno-fecha" class="label-formulario">Alumno:</label>
					<input type="text" id="entrada-alumno-fecha" value="">
					<div id="lista-autocompletar-fecha" class="autocomplete-items"
						style="position: absolute; top: 100%; left: 0; right: 0; z-index: 1000; background: #fff; max-height: 200px; overflow-y: auto; display: flex; flex-direction: column; align-items: center; padding: 4px 0; border-radius: 0 0 4px 4px;"></div>
				</div>
				<div>
					<label for="inicio-fecha" class="label-formulario">F.Inicio:</label>
					<input type="date" id="inicio-fecha" value="">
				</div>
				<div>
					<label for="fin-fecha" class="label-formulario">F.Fin:</label> <input
						type="date" id="fin-fecha" value="">
				</div>
				<button class="boton-cargar" id="boton-alumno-fecha">Filtrar</button>
			</div>

			<!-- Filtro: Alumno + Estado -->
			<div id="seccion-alumno-estado" class="filter-section"
				style="display: none;">
				<div style="position: relative;">
					<label for="entrada-alumno-estado" class="label-formulario">Alumno:</label>
					<input type="text" id="entrada-alumno-estado" value=""> <input
						type="hidden" id="entrada-alumno-estado-id">
					<div id="lista-autocompletar-estado" class="autocomplete-items"
						style="position: absolute; top: 100%; left: 0; right: 0; z-index: 1000; background: #fff; max-height: 200px; overflow-y: auto; border-top: none;"></div>
				</div>
				<div>
					<label for="seleccion-estado" class="label-formulario">Estado:</label>
					<select id="seleccion-estado">
						<option value="PRESENTE">Presente</option>
						<option value="COMPLETA">Completo</option>
						<option value="SIN SALIDA">Sin salida</option>
						<option value="FALTA">Falta</option>
					</select>
				</div>
				<div>
					<label for="seleccion-anio-escolar" class="label-formulario">Año
						Escolar:</label> <select id="seleccion-anio-escolar"></select>
				</div>
				<button class="boton-cargar" id="boton-alumno-estado">Filtrar</button>
			</div>

			<!-- Filtro: Estadísticas -->
			<div id="seccion-estadisticas" class="filter-section"
				style="display: none;">
				<div style="position: relative;">
					<input type="hidden" id="entrada-alumno-conteo-id"> <label
						for="entrada-alumno-conteo" class="label-formulario">Alumno:</label>
					<input type="text" id="entrada-alumno-conteo" value="">
					<div id="lista-autocompletar-conteo" class="autocomplete-items"
						style="position: absolute; top: 100%; left: 0; right: 0; z-index: 1000; background: #fff; max-height: 200px; overflow-y: auto; display: flex; flex-direction: column; align-items: center; padding: 4px 0; border-radius: 0 0 4px 4px;"></div>
				</div>
				<div>
					<label for="inicio-conteo" class="label-formulario">Fecha
						Inicio:</label> <input type="date" id="inicio-conteo">
				</div>
				<div>
					<label for="fin-conteo" class="label-formulario">Fecha Fin:</label>
					<input type="date" id="fin-conteo">
				</div>
				<button class="boton-cargar" id="boton-alumno-conteo">Filtrar</button>
				<div id="contenedor-tabla-conteo" style="margin-top: 20px;"></div>
			</div>

			<div id="contenedor-tabla-principal" class="mt-3"></div>
		</div>

	</main>






	<!-- FOOTER -->
	<footer class="altair-footer mt-auto">
		<div class="container">
			<div class="row">
				<div class="col-12"></div>
				<p>© Copyright 2022 · Altair Centro Educativo | C/ El Barbero de
					Sevilla, 1, 41006 SEVILLA |</p>
			</div>
			<div class="col-12">
				<p>
					Tfno.: 954 645 800 | <a href="https://altair.edu.es/contacto/"
						style="text-decoration: none; color: #FFD43B">Contacto</a>
				</p>
			</div>
		</div>

	</footer>

	<script>
document.addEventListener("DOMContentLoaded", async function() {

    // ------------------------------
    // ELEMENTOS HTML
    // ------------------------------
    const seleccionFiltro = document.getElementById('seleccion-filtro');
    const seccionCursoFecha = document.getElementById('seccion-curso-fecha');
    const seccionAlumnoFecha = document.getElementById('seccion-alumno-fecha');
    const seccionAlumnoEstado = document.getElementById('seccion-alumno-estado');
    const seccionEstadisticas = document.getElementById('seccion-estadisticas');

    const entradaCurso = document.getElementById('entrada-curso');
    const entradaGrupo = document.getElementById('entrada-grupo');
    const entradaFecha = document.getElementById('entrada-fecha');
    const botonCursoFecha = document.getElementById('boton-curso-fecha');

    const entradaAlumnoFecha = document.getElementById('entrada-alumno-fecha');
    const entradaAlumnoFechaId = document.getElementById('entrada-alumno-fecha-id');
    const inicioFecha = document.getElementById('inicio-fecha');
    const finFecha = document.getElementById('fin-fecha');
    const botonAlumnoFecha = document.getElementById('boton-alumno-fecha');

    const entradaAlumnoEstado = document.getElementById('entrada-alumno-estado');
    const entradaAlumnoEstadoId = document.getElementById('entrada-alumno-estado-id');
    const seleccionEstado = document.getElementById('seleccion-estado');
    const seleccionAnioEscolar = document.getElementById('seleccion-anio-escolar');
    const botonAlumnoEstado = document.getElementById('boton-alumno-estado');

    const entradaAlumnoConteo = document.getElementById('entrada-alumno-conteo');
    const entradaAlumnoConteoId = document.getElementById('entrada-alumno-conteo-id');
    const inicioConteo = document.getElementById('inicio-conteo');
    const finConteo = document.getElementById('fin-conteo');
    const botonAlumnoConteo = document.getElementById('boton-alumno-conteo');

    const contenedorTablaPrincipal = document.getElementById('contenedor-tabla-principal');
    const contenedorTablaConteo = document.getElementById('contenedor-tabla-conteo');

    // ------------------------------
    // MOSTRAR / OCULTAR SECCIONES
    // ------------------------------
    seleccionFiltro.addEventListener('change', function() {
        seccionCursoFecha.style.display = 'none';
   
        seccionAlumnoFecha.style.display = 'none';
        seccionAlumnoEstado.style.display = 'none';
        seccionEstadisticas.style.display = 'none';

        switch (seleccionFiltro.value) {
            case 'curso-fecha':
                seccionCursoFecha.style.display = 'flex';
                break;
            case 'alumno-fecha':
                seccionAlumnoFecha.style.display = 'flex';
                break;
            case 'alumno-estado':
                seccionAlumnoEstado.style.display = 'flex';
                break;
            case 'estadisticas':
                seccionEstadisticas.style.display = 'flex';
                break;
        }
    });

    // ------------------------------
    // CARGAR CURSOS Y GRUPOS
    // ------------------------------
    try {
        const defaultCursoOpt = document.createElement("option");
        defaultCursoOpt.value = "";
        defaultCursoOpt.textContent = "Seleccione...";
        defaultCursoOpt.disabled = true;
        defaultCursoOpt.selected = true;
        entradaCurso.appendChild(defaultCursoOpt);

        const respCursos = await fetch('<%=request.getContextPath()%>/curso');
        const cursos = await respCursos.json();

        cursos.forEach(curso => {
            const opt = document.createElement("option");
            opt.value = curso.idCurso;
            opt.dataset.nombre = curso.nombreCurso;
            opt.textContent = curso.nombreCurso;
            entradaCurso.appendChild(opt);
        });

        const defaultGrupoOpt = document.createElement("option");
        defaultGrupoOpt.value = "";
        defaultGrupoOpt.textContent = "Seleccione...";
        defaultGrupoOpt.disabled = true;
        defaultGrupoOpt.selected = true;
        entradaGrupo.appendChild(defaultGrupoOpt);

        entradaCurso.addEventListener("change", async function() {
            entradaGrupo.innerHTML = "";
            const defaultOpt = document.createElement("option");
            defaultOpt.value = "";
            defaultOpt.textContent = "Seleccione...";
            defaultOpt.disabled = true;
            defaultOpt.selected = true;
            entradaGrupo.appendChild(defaultOpt);

            if (!entradaCurso.value) return;

            const respGrupos = await fetch("<%=request.getContextPath()%>/grupo/" + encodeURIComponent(entradaCurso.value));

            const grupos = await respGrupos.json();
            if (!Array.isArray(grupos)) return;

            grupos.forEach(grupo => {
                const opt = document.createElement("option");
                opt.value = grupo.nombreGrupo;
                opt.textContent = grupo.nombreGrupo;
                entradaGrupo.appendChild(opt);
            });
        });

    } catch (err) {
        console.error("Error cargando cursos o grupos:", err);
    }

    // ------------------------------
    // Habilitar botón Curso+Fecha
    // ------------------------------
    function habilitarBotonCursoFecha() {
        botonCursoFecha.disabled = !(entradaCurso.value && entradaGrupo.value && entradaFecha.value);
    }
    entradaCurso.addEventListener('change', habilitarBotonCursoFecha);
    entradaGrupo.addEventListener('change', habilitarBotonCursoFecha);
    entradaFecha.addEventListener('change', habilitarBotonCursoFecha);

    // ------------------------------
    // Autocomplete Alumno
    // ------------------------------
    function setupAlumnoAutocomplete(inputId, hiddenId, dataCache, listId) {
        const input = document.getElementById(inputId);
        const hiddenInput = document.getElementById(hiddenId);
        const listContainer = document.getElementById(listId);

        const sortedData = [...dataCache].sort((a, b) =>
            a.nombreCompleto.localeCompare(b.nombreCompleto)
        );

        input.addEventListener('input', () => {
            const valorUsuario = input.value.trim().toLowerCase();
            hiddenInput.value = '';
            listContainer.innerHTML = '';

            if (!valorUsuario) return;

            const coincidencias = sortedData.filter(a =>
                a.nombreCompleto.toLowerCase().includes(valorUsuario)
            );

            coincidencias.forEach(a => {
                const item = document.createElement('div');
                item.textContent = a.nombreCompleto;
                item.style.padding = "5px";
                item.style.cursor = "pointer";

                item.addEventListener('mousedown', () => {
                    input.value = a.nombreCompleto;
                    hiddenInput.value = a.alumnoId;
                    listContainer.innerHTML = '';
                });

                listContainer.appendChild(item);
            });
        });

        input.addEventListener('blur', () => {
            setTimeout(() => listContainer.innerHTML = '', 100);
        });
    }

    // ------------------------------
    // Cargar alumnos y activar autocomplete
    // ------------------------------
    let alumnosCache = [];
    fetch('<%=request.getContextPath()%>/asistencia?accion=todas')
    .then(res => res.json())
        .then(data => {
            const mapaAlumnos = new Map();

            data.forEach(a => {
                if (!mapaAlumnos.has(a.alumnoId)) {
                    const nombre = (a.nombreCompletoAlumno || "").trim();
                    mapaAlumnos.set(a.alumnoId, {
                        alumnoId: a.alumnoId,
                        nombreCompleto: nombre
                    });
                }
            });

            alumnosCache = Array.from(mapaAlumnos.values())
                .sort((a, b) => a.nombreCompleto.localeCompare(b.nombreCompleto));

            setupAlumnoAutocomplete('entrada-alumno-fecha', 'entrada-alumno-fecha-id', alumnosCache, 'lista-autocompletar-fecha');
            setupAlumnoAutocomplete('entrada-alumno-estado', 'entrada-alumno-estado-id', alumnosCache, 'lista-autocompletar-estado');
            setupAlumnoAutocomplete('entrada-alumno-conteo', 'entrada-alumno-conteo-id', alumnosCache, 'lista-autocompletar-conteo');
        });

    // ------------------------------
    // Función fetch general
    // ------------------------------
    async function fetchAsistencias(urlParams) {
        const url = "http://localhost:8080/fichajes-vista/asistencia?" + urlParams;
        try {
            const response = await fetch(url);
            if (!response.ok) throw new Error("Error en la petición AJAX");
            return await response.json();
        } catch (e) {
            console.error(e);
            alert("❌ Error al obtener datos.");
            return [];
        }
    }

    // ------------------------------
    // Botón Filtrar Curso+Fecha
    // ------------------------------
    botonCursoFecha.addEventListener('click', async function() {
        const cursoNombre = entradaCurso.selectedOptions[0]?.dataset.nombre;
        const grupo = entradaGrupo.value;
        const fecha = entradaFecha.value;
        if (!cursoNombre || !grupo || !fecha) return alert("Selecciona curso, grupo y fecha");

        const params = new URLSearchParams({ accion: 'porCursoGrupoYFecha', curso: cursoNombre, grupo: grupo, fecha });
        const data = await fetchAsistencias(params.toString());
        renderTable(data);
    });


    // ------------------------------
    // Botón Filtrar Alumno+Fecha
    // ------------------------------
    botonAlumnoFecha.addEventListener('click', async function() {
        const alumnoId = entradaAlumnoFechaId.value;
        const desde = inicioFecha.value;
        const hasta = finFecha.value;
        if (!alumnoId || !desde || !hasta) return alert("Rellena todos los campos");

        const params = new URLSearchParams({ 
            accion: 'porAlumnoYRango', 
            alumnoId,
            desde, 
            hasta 
        });

        const data = await fetchAsistencias(params.toString());
        renderTable(data);
    });

    // ------------------------------
    // Botón Filtrar Alumno+Estado
    // ------------------------------
    botonAlumnoEstado.addEventListener('click', async function() {
        const alumnoId = entradaAlumnoEstadoId.value;
        const estado = seleccionEstado.value;
        const anioEscolar = seleccionAnioEscolar.value;

        if (!alumnoId || !estado || !anioEscolar) return alert("Rellena todos los campos");

        const params = new URLSearchParams({
            accion: 'porAlumnoYEstado',
            alumnoId,
            estado,
            anioEscolar
        });

        const data = await fetchAsistencias(params.toString());
        renderTable(data);
    });

    // ------------------------------
    // Botón Filtrar Estadísticas
    // ------------------------------
    botonAlumnoConteo.addEventListener('click', async function() {
        const alumnoId = entradaAlumnoConteoId.value;
        const desde = inicioConteo.value;
        const hasta = finConteo.value;

        if (!alumnoId || !desde || !hasta) return alert("Rellena todos los campos");

        const params = new URLSearchParams({
            accion: 'conteoEstados',
            alumnoId,
            desde,
            hasta
        });

        const data = await fetchAsistencias(params.toString());
        renderEstadisticasTable(data);
    });

    // ------------------------------
    // Funciones render
    // ------------------------------
    function renderTable(data) {
        contenedorTablaConteo.innerHTML = "";
        contenedorTablaPrincipal.style.display = 'block';
        contenedorTablaConteo.style.display = 'none';
        contenedorTablaPrincipal.innerHTML = "";
        contenedorTablaPrincipal.appendChild(generateAttendanceTable(data));
    }

    function generateAttendanceTable(data) {
        const table = document.createElement('table');
        table.classList.add('table', 'table-striped', 'table-hover', 'table-bordered', 'shadow-sm');

        const headers = ['Nombre', 'Curso', 'Grupo', 'Fecha', 'Entrada', 'Salida', 'Estado'];
        const thead = document.createElement('thead');
        const headerRow = document.createElement('tr');
        headers.forEach(h => {
            const th = document.createElement('th');
            th.textContent = h;
            th.style.backgroundColor = "#032b38";
            th.style.color = "#ffffff";
            th.style.textAlign = 'center';
            th.style.position = 'sticky';
            th.style.top = '0';
            th.style.zIndex = '10';
            headerRow.appendChild(th);
        });
        thead.appendChild(headerRow);
        table.appendChild(thead);

        const tbody = document.createElement('tbody');

        if (!data || data.length === 0) {
            const tr = document.createElement('tr');
            const td = document.createElement('td');
            td.textContent = "No hay registros en este rango de fechas";
            td.colSpan = headers.length;
            td.style.textAlign = 'center';
            td.style.fontStyle = 'italic';
            td.style.backgroundColor = "#f8f9fa";
            tr.appendChild(td);
            tbody.appendChild(tr);
        } else {
            data.forEach((a, index) => {
                const tr = document.createElement('tr');
                tr.style.backgroundColor = index % 2 === 0 ? "#ffffff" : "#f1f3f5"; 

                const entrada = a.horaEntrada || "-";
                const salida = a.horaSalida || "-";
                const rowData = [
                    a.nombreCompletoAlumno,
                    a.nombreCurso,
                    a.nombreGrupo,
                    a.fecha,
                    entrada,
                    salida,
                    a.estado || 'Desconocido'
                ];

                rowData.forEach((value, colIndex) => {
                    const td = document.createElement('td');
                    td.textContent = value;
                    td.style.textAlign = 'center';

                    if (colIndex === headers.length - 1 && typeof value === 'string') {
                        switch (value.toUpperCase()) {
                            case 'COMPLETA': td.className = 'estado-rojo fw-bold'; break;
                            case 'PRESENTE': td.className = 'text-success-light fw-bold'; break;
                            case 'SIN SALIDA': td.className = 'text-warning fw-bold'; break;
                            case 'FALTA': td.className = 'text-danger fw-bold'; break;
                            case 'FESTIVO': td.className = 'estado-naranja fw-bold'; break;
                            default: td.className = 'text-secondary fw-bold';
                        }
                    }

                    tr.appendChild(td);
                });

                tbody.appendChild(tr);
            });
        }

        table.appendChild(tbody);
        const wrapper = document.createElement('div');
        wrapper.style.overflowX = 'auto';
        wrapper.appendChild(table);

        return wrapper;
    }

    function renderEstadisticasTable(data) {
        contenedorTablaPrincipal.innerHTML = "";
        contenedorTablaPrincipal.style.display = 'none';
        contenedorTablaConteo.style.display = 'block';
        contenedorTablaConteo.innerHTML = "";

        if (!data || Object.keys(data).length === 0) {
            contenedorTablaConteo.innerHTML = "<p>No hay registros en este rango de fechas.</p>";
            return;
        }

        const table = document.createElement('table');
        table.classList.add('table', 'table-striped', 'table-bordered', 'shadow-sm', 'w-100');
        table.style.tableLayout = "fixed";

        const thead = document.createElement('thead');
        const headerRow = document.createElement('tr');
        const headers = ['PRESENTE', 'COMPLETA', 'SIN SALIDA', 'FALTA'];
        headers.forEach(h => {
            const th = document.createElement('th');
            th.textContent = h;
            th.style.backgroundColor = "#032b38";
            th.style.color = "#fff";
            th.style.textAlign = 'center';
            th.style.width = "25%";
            th.style.position = 'sticky';
            th.style.top = '0';
            th.style.zIndex = '10';
            headerRow.appendChild(th);
        });
        thead.appendChild(headerRow);
        table.appendChild(thead);

        const tbody = document.createElement('tbody');
        const tr = document.createElement('tr');
        headers.forEach(h => {
            const td = document.createElement('td');
            td.textContent = data[h] || 0;
            td.style.textAlign = 'center';
            tr.appendChild(td);
        });
        tbody.appendChild(tr);
        table.appendChild(tbody);

        const wrapper = document.createElement('div');
        wrapper.style.overflowX = 'auto';
        wrapper.style.width = '100%';
        wrapper.style.marginTop = '10px';
        wrapper.appendChild(table);

        contenedorTablaConteo.appendChild(wrapper);
    }

    // ------------------------------
    // Generar años escolares
    // ------------------------------
    function generarAniosEscolares(selectId, rangoAtras = 6) {
        const select = document.getElementById(selectId);
        select.innerHTML = "";

        const fechaActual = new Date();
        const anioActual = fechaActual.getFullYear();

        const defaultOpt = document.createElement("option");
        defaultOpt.value = "";
        defaultOpt.textContent = "Seleccione...";
        defaultOpt.disabled = true;
        defaultOpt.selected = true;
        select.appendChild(defaultOpt);

        for (let i = anioActual; i >= anioActual - rangoAtras; i--) {
            const option = document.createElement("option");
            option.value = i + "/" + (i + 1);
            option.textContent = i + "/" + (i + 1);
            select.appendChild(option);
        }
    }

    generarAniosEscolares("seleccion-anio-escolar");
});
</script>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		crossorigin="anonymous"></script>

	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

</body>
</html>