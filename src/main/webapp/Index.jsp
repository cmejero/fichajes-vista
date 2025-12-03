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
						<div class="col-md-4 col-sm-4 col-4 alineacion" style="">
							<a href="Index.jsp" class="letraNavegacion"
								style="color: #FFD43B; text-decoration: underline">INICIO</a>
						</div>
						<div class="col-md-4 col-sm-4 col-4 alineacion">
							<a href="Asistencias.jsp" class="letraNavegacion">ASISTENCIAS</a>
						</div>
						<div class="col-md-4 col-sm-4 col-4 alineacion">
							<a href="Alumno.jsp" class="letraNavegacion">ALUMNOS</a>
						</div>
					</div>


				</div>
				<div class="col-md-2 col-sm-2 col-2"></div>
			</div>
		</div>
	</header>

	<!-- MAIN -->
	<main
		style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
		<!-- SECCIÓN DE BIENVENIDA -->
		<div class="container">
			<div class="row">
				<div class="col-md-12 col-sm-12 col-12" style="text-align: center">
					<section class="bienvenida">
						<h1 style="color: #032b38">Bienvenido al sistema de control
							de asistencia</h1>
						<p style="color: #015b96">Gestiona y visualiza la asistencia
							de los alumnos del Colegio Altair Sevilla.</p>
					</section>
				</div>
				<!-- SECCIÓN DE SELECCIÓN DE CURSO Y GRUPO -->
				<div class="col-md-12 col-sm-12 col-12 mt-4">
					<section class="selector-curso">
						<h2 style="color: #032b38">Seleccione el curso y grupo</h2>
						<form id="formAsistencia" action="AsistenciaHoy.jsp" method="get">
							<input type="hidden" name="curso" id="nombreCursoHidden">
							<label for="curso">Curso:</label> <select id="curso"></select> <label
								for="grupo">Grupo:</label> <select id="grupo" name="grupo"></select>

							<button type="submit" class="boton-cargar" id="botonCargar" disabled>Cargar asistencia</button>

						</form>
					</section>
				</div>
			</div>
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
    const cursoSelect = document.getElementById("curso");
    const grupoSelect = document.getElementById("grupo");
    const nombreCursoHidden = document.getElementById("nombreCursoHidden");
    const botonCargar = document.getElementById("botonCargar"); // tu botón de enviar
    const tablaCuerpo = document.getElementById("tablaCuerpoAsistencia");

    // Función para habilitar o deshabilitar el botón
    function validarSeleccion() {
        botonCargar.disabled = !(cursoSelect.value && grupoSelect.value);
    }

    // ------------------- CARGA DE CURSOS Y GRUPOS -------------------
    try {
        // Curso por defecto
        const defaultCursoOpt = document.createElement("option");
        defaultCursoOpt.value = "";
        defaultCursoOpt.textContent = "Seleccione...";
        defaultCursoOpt.disabled = true;
        defaultCursoOpt.selected = true;
        cursoSelect.appendChild(defaultCursoOpt);

       
        const respCursos = await fetch('<%= request.getContextPath() %>/curso');
        const cursos = await respCursos.json();
        cursos.forEach(curso => {
            const opt = document.createElement("option");
            opt.value = curso.idCurso;
            opt.dataset.nombre = curso.nombreCurso;
            opt.textContent = curso.nombreCurso;
            cursoSelect.appendChild(opt);
        });

        // Grupo por defecto
        const defaultGrupoOpt = document.createElement("option");
        defaultGrupoOpt.value = "";
        defaultGrupoOpt.textContent = "Seleccione...";
        defaultGrupoOpt.disabled = true;
        defaultGrupoOpt.selected = true;
        grupoSelect.appendChild(defaultGrupoOpt);

        cursoSelect.addEventListener("change", async function() {
            grupoSelect.innerHTML = "";
            const defaultOpt = document.createElement("option");
            defaultOpt.value = "";
            defaultOpt.textContent = "Seleccione...";
            defaultOpt.disabled = true;
            defaultOpt.selected = true;
            grupoSelect.appendChild(defaultOpt);

            if (!cursoSelect.value) return;

            const respGrupos = await fetch("<%= request.getContextPath() %>/grupo/" + encodeURIComponent(cursoSelect.value));
            const grupos = await respGrupos.json();
            grupos.forEach(grupo => {
                const opt = document.createElement("option");
                opt.value = grupo.nombreGrupo;
                opt.textContent = grupo.nombreGrupo;
                grupoSelect.appendChild(opt);
            });

            validarSeleccion();
        });

        grupoSelect.addEventListener("change", validarSeleccion);

        // Antes de enviar el formulario
        document.getElementById("formAsistencia").addEventListener("submit", function() {
            const selectedOption = cursoSelect.selectedOptions[0];
            nombreCursoHidden.value = selectedOption.dataset.nombre;
        });

    } catch (err) {
        console.error("Error cargando cursos o grupos:", err);
    }

 
});
</script>
</body>
</html>
