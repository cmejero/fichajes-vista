<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Alumno - Colegio Altair Sevilla</title>
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
							<a href="Index.jsp" class="letraNavegacion">INICIO</a>
						</div>
						<div class="col-md-4 col-sm-4 col-4 alineacion">
							<a href="Asistencias.jsp" class="letraNavegacion">ASISTENCIAS</a>
						</div>
						<div class="col-md-4 col-sm-4 col-4 alineacion">
							<a href="Alumno.jsp" class="letraNavegacion"
								style="color: #FFD43B; text-decoration: underline">ALUMNOS</a>
						</div>
					</div>


				</div>
				<div class="col-md-2 col-sm-2 col-2"></div>
			</div>
		</div>
	</header>
<main class="asistencia-main d-flex flex-column align-items-center justify-content-center">
    <section class="tabla-asistencia container shadow p-4 rounded">
        <div class="row">
            <div class="col-12">
                <h2 class="text-gestion">GESTIÓN DE ALUMNOS</h2>
            </div>
        </div>

        <!-- Botones de navegación -->
        <div class="row d-flex justify-content-center">
            <div class="col-12 d-flex justify-content-center mt-4 mb-2">
                <button id="btnAgregarAlumno" class="boton-modificar">Agregar alumno</button>
                <button id="btnMatricularAlumno" class="boton-modificar" style="margin-left: 18vw">Matricular</button>
            </div>
        </div>

        <!-- Formulario Agregar Alumno -->
        <div id="formAgregarAlumno" class="form-container mt-3">
            <div class="row d-flex justify-content-center">
                <div class="col-12 d-flex justify-content-center mb-3">
                    <h3 class="text-registrar mt-2"><u>REGISTRAR NUEVO ALUMNO</u></h3>
                </div>
            </div>

            <form id="formAlumno1" onsubmit="return guardarAlumno();">
                <input type="hidden" name="accion" value="guardar">

                <div class="row d-flex justify-content-center mb-4">
                    <div class="col-12 d-flex justify-content-center">
                        <label for="nombre" class="label-formulario"><strong>Nombre:</strong></label>
                        <input class="input-formulario2" type="text" id="nombre" name="nombre" required>
                        <label for="apellidos" class="label-formulario"><strong>Apellidos:</strong></label>
                        <input class="input-formulario2" type="text" id="apellidos" name="apellidos" required>
                    </div>
                </div>

                <div class="row d-flex justify-content-center mb-3">
                    <div class="col-12 d-flex justify-content-center">
                        <h4 class="datos"><u>DATOS DE MATRICULACIÓN</u></h4>
                    </div>
                </div>

                <div class="row d-flex justify-content-center mb-4">
                    <div class="col-12 d-flex justify-content-center">
                        <label for="curso" class="label-formulario"><strong>Curso:</strong></label>
                        <select id="curso" name="curso" class="input-formulario" required>
                            <option value="">Seleccione...</option>
                        </select>

                        <label for="grupo" class="label-formulario"><strong>Grupo:</strong></label>
                        <select id="grupo" name="grupo" class="input-formulario" required>
                            <option value="">Seleccione...</option>
                        </select>

                        <label for="anioEscolar" class="label-formulario"><strong>Año escolar:</strong></label>
                        <select id="anioEscolar" name="anioEscolar" class="input-formulario" required>
                            <option value="">Seleccione...</option>
                        </select>

                        <label for="uidLlave" class="label-formulario"><strong>UID:</strong></label>
                        <input class="input-formulario" type="text" id="uidLlave" name="uidLlave" required>
                    </div>
                </div>

                <div class="row d-flex justify-content-center mb-3">
                    <div class="col-12 d-flex justify-content-center">
                        <button type="submit" class="boton">Guardar Alumno</button>
                    </div>
                </div>
            </form>
        </div>

        <!-- Formulario Matricular Alumno -->
        <div id="formMatricular" class="form-container mt-3" style="display: none;">
            <div class="row d-flex justify-content-center">
                <div class="col-12 d-flex justify-content-center mb-3">
                    <h3 class="text-registrar mt-2"><u>REGISTRAR NUEVA MATRÍCULA</u></h3>
                </div>
            </div>

            <form id="formAlumno2" onsubmit="return guardarMatriculacion();">
                <input type="hidden" name="accion" value="guardar">
                <input type="hidden" id="idAlumnoSeleccionado" name="idAlumnoSeleccionado">

                <div class="row d-flex justify-content-center mb-4">
                    <div class="col-12 d-flex align-items-center justify-content-center" style="gap: 1rem; flex-wrap: wrap; position: relative;">
                        <label for="nombreYApellidos" class="label-formulario" style="margin-right: -0.8vw"><strong>Nombre completo:</strong></label>
                        <input type="text" id="nombreYApellidos" name="nombreYApellidos" placeholder="Escribe el nombre del alumno" autocomplete="off" style="padding: 0.06rem 0.8rem; flex: 0 1 18rem; max-width: 100%;">
                        <button type="button" id="btnSeleccionarAlumno" class="boton-buscar">Seleccionar</button>
                    </div>
                </div>

                <div class="row d-flex justify-content-center mb-3">
                    <div class="col-12 d-flex justify-content-center">
                        <h4 class="datos"><u>DATOS DE MATRICULACIÓN</u></h4>
                    </div>
                </div>

                <div class="row d-flex justify-content-center mb-4">
                    <div class="col-12 d-flex justify-content-center">
                        <label for="cursoMat" class="label-formulario"><strong>Curso:</strong></label>
                        <select id="cursoMat" name="curso" class="input-formulario" required>
                            <option value="">Seleccione...</option>
                        </select>

                        <label for="grupoMat" class="label-formulario"><strong>Grupo:</strong></label>
                        <select id="grupoMat" name="grupo" class="input-formulario" required>
                            <option value="">Seleccione...</option>
                        </select>

                        <label for="anioEscolarMat" class="label-formulario"><strong>Año escolar:</strong></label>
                        <select id="anioEscolarMat" name="anioEscolar" class="input-formulario" required>
                            <option value="">Seleccione...</option>
                        </select>

                        <label for="uidLlaveMat" class="label-formulario"><strong>UID:</strong></label>
                        <input class="input-formulario" type="text" id="uidLlaveMat" name="uidLlave" required>
                    </div>
                </div>

                <div class="row d-flex justify-content-center mb-3">
                    <div class="col-12 d-flex justify-content-center">
                        <button type="submit" class="boton">Guardar Matrícula</button>
                    </div>
                </div>
            </form>
        </div>
    </section>
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
document.addEventListener('DOMContentLoaded', () => {
    // --- BOTONES Y FORMULARIOS ---
    const btnAgregar = document.getElementById('btnAgregarAlumno');
    const btnMatricular = document.getElementById('btnMatricularAlumno');
    const formAgregar = document.getElementById('formAgregarAlumno');
    const formMatricular = document.getElementById('formMatricular');

    function setActivo(btnActivo) {
        btnAgregar.classList.remove('activo');
        btnMatricular.classList.remove('activo');
        btnActivo.classList.add('activo');
    }

    btnAgregar.addEventListener('click', () => {
        formAgregar.style.display = 'block';
        formMatricular.style.display = 'none';
        setActivo(btnAgregar);
    });

    btnMatricular.addEventListener('click', () => {
        formAgregar.style.display = 'none';
        formMatricular.style.display = 'block';
        setActivo(btnMatricular);
    });

    formAgregar.style.display = 'block';
    formMatricular.style.display = 'none';
    setActivo(btnAgregar);

    // --- SELECTS ---
    const cursoAgregar = document.getElementById('curso');
    const grupoAgregar = document.getElementById('grupo');
    const anioAgregar = document.getElementById('anioEscolar');
    const cursoMat = document.getElementById('cursoMat');
    const grupoMat = document.getElementById('grupoMat');
    const anioMat = document.getElementById('anioEscolarMat');

    function cargarAnios(selectAnio) {
        selectAnio.innerHTML = '<option value="">Seleccione...</option>';
        const currentYear = new Date().getFullYear();
        for (let i = 0; i < 5; i++) {
            const start = currentYear + i;
            const end = start + 1;
            const option = document.createElement('option');
            option.value = start + "-" + end;
            option.textContent = start + "-" + end;
            selectAnio.appendChild(option);
        }
    }

    async function cargarCursos() {
        try {
            const resp = await fetch('<%=request.getContextPath()%>/curso');
            const cursos = await resp.json();

            cursoAgregar.innerHTML = '<option value="">Seleccione...</option>';
            cursoMat.innerHTML = '<option value="">Seleccione...</option>';

            cursos.forEach(curso => {
                const option1 = document.createElement('option');
                option1.value = curso.idCurso;
                option1.textContent = curso.nombreCurso;
                cursoAgregar.appendChild(option1);

                const option2 = document.createElement('option');
                option2.value = curso.idCurso;
                option2.textContent = curso.nombreCurso;
                cursoMat.appendChild(option2);
            });
        } catch (err) {
            console.error('Error cargando cursos:', err);
        }
    }

    async function cargarGrupos(cursoId, selectGrupo) {
        try {
            selectGrupo.innerHTML = '<option value="">Seleccione...</option>';
            if (!cursoId) return;

            const resp = await fetch('<%=request.getContextPath()%>/grupo/' + cursoId );
            const grupos = await resp.json();

            grupos.forEach(grupo => {
                const option = document.createElement('option');
                option.value = grupo.idGrupo;
                option.textContent = grupo.nombreGrupo;
                selectGrupo.appendChild(option);
            });
        } catch (err) {
            console.error('Error cargando grupos:', err);
        }
    }

    cursoAgregar.addEventListener('change', function() {
        const cursoId = parseInt(this.value);
        cargarGrupos(cursoId, grupoAgregar);
        cargarAnios(anioAgregar);
    });

    cursoMat.addEventListener('change', function() {
        const cursoId = parseInt(this.value);
        cargarGrupos(cursoId, grupoMat);
        cargarAnios(anioMat);
    });

    // --- CARGAR CURSOS INICIALES ---
    cargarCursos();

    // --- FUNCIONES GUARDAR ALUMNO Y MATRICULACIÓN ---
    window.guardarAlumno = function() {
        const formData = {
            accion: 'guardar',
            nombre: document.getElementById('nombre').value,
            apellidos: document.getElementById('apellidos').value,
            curso: document.getElementById('curso').value,
            grupo: document.getElementById('grupo').value,
            anioEscolar: document.getElementById('anioEscolar').value,
            uidLlave: document.getElementById('uidLlave').value
        };

        const params = Object.keys(formData)
            .map(k => encodeURIComponent(k) + '=' + encodeURIComponent(formData[k]))
            .join('&');

        const xhr = new XMLHttpRequest();
        xhr.open('POST', 'alumno', true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    alert('Alumno guardado correctamente');
                    document.getElementById('formAlumno1').reset();
                    grupoAgregar.innerHTML = '<option value="">Seleccione...</option>';
                    anioAgregar.innerHTML = '<option value="">Seleccione...</option>';
                } else {
                    console.error('Error al guardar alumno:', xhr.status, xhr.responseText);
                    alert('Error al guardar alumno');
                }
            }
        };

        xhr.send(params);
        return false;
    }

    window.guardarMatriculacion = function() {
        const formData = {
            accion: 'guardar',
            idAlumnoSeleccionado: document.getElementById('idAlumnoSeleccionado').value,
            curso: document.getElementById('cursoMat').value,
            grupo: document.getElementById('grupoMat').value,
            anioEscolar: document.getElementById('anioEscolarMat').value,
            uidLlave: document.getElementById('uidLlaveMat').value
        };

        if (!formData.idAlumnoSeleccionado) {
            alert('⚠️ Debes seleccionar un alumno antes de guardar la matrícula.');
            return false;
        }

        const params = Object.keys(formData)
            .map(k => encodeURIComponent(k) + '=' + encodeURIComponent(formData[k]))
            .join('&');

        const xhr = new XMLHttpRequest();
        xhr.open('POST', 'matriculacion', true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    alert('✅ Matrícula guardada correctamente.');
                    document.getElementById('formAlumno2').reset();
                    document.getElementById('idAlumnoSeleccionado').value = '';
                    grupoMat.innerHTML = '<option value="">Seleccione...</option>';
                    anioMat.innerHTML = '<option value="">Seleccione...</option>';
                } else {
                    console.error('Error al guardar matrícula:', xhr.status, xhr.responseText);
                    alert('❌ Error al guardar la matrícula.');
                }
            }
        };

        xhr.send(params);
        return false;
    }

    // --- AUTOCOMPLETADO ALUMNOS ---
    const nombreInput = document.getElementById('nombreYApellidos');
    const idAlumnoHidden = document.getElementById('idAlumnoSeleccionado');
    let alumnosCache = [];

    fetch('<%=request.getContextPath()%>/alumno')
        .then(res => res.json())
        .then(data => { alumnosCache = data; })
        .catch(err => console.error('Error cargando alumnos:', err));

    nombreInput.addEventListener('input', () => {
        const valorUsuario = nombreInput.value;
        idAlumnoHidden.value = '';
        if (!valorUsuario) return;

        const valorLower = valorUsuario.toLowerCase();
        const coincidencia = alumnosCache
            .map(a => ({id: a.idAlumno, nombre: a.nombreAlumno + ' ' + a.apellidoAlumno}))
            .filter(a => a.nombre.toLowerCase().includes(valorLower))
            .sort((a,b) => a.nombre.localeCompare(b.nombre))[0];

        if (!coincidencia) return;

        if (coincidencia.nombre.toLowerCase().startsWith(valorLower)) {
            const parteAutocompletada = coincidencia.nombre.substring(valorUsuario.length);
            nombreInput.value = valorUsuario + parteAutocompletada;
            nombreInput.setSelectionRange(valorUsuario.length, coincidencia.nombre.length);
        }
    });

    document.getElementById('btnSeleccionarAlumno').addEventListener('click', () => {
        const valor = nombreInput.value.trim().toLowerCase();
        const seleccionado = alumnosCache
            .map(a => ({id: a.idAlumno, nombre: a.nombreAlumno + ' ' + a.apellidoAlumno}))
            .find(a => a.nombre.toLowerCase() === valor);

        if (!seleccionado) {
            alert('Selecciona un alumno válido de la sugerencia inline');
            idAlumnoHidden.value = '';
            return;
        }

        idAlumnoHidden.value = seleccionado.id;
        alert('Alumno seleccionado: ' + seleccionado.nombre);
    });
});
</script>


	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		crossorigin="anonymous"></script>

	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>


</body>
</html>