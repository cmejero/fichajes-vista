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
							<a href="Asistencias.jsp" class="letraNavegacion">ASISTENCIAS</a>
						</div>
						<div class="col-md-3 col-sm-3 col-3 alineacion">
							<a href="Alumno.jsp" class="letraNavegacion"
								style="color: #FFD43B; text-decoration: underline">ALUMNOS</a>
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
		<section class="tabla-asistencia container shadow p-4 rounded">
			<div class="row">
				<div class="col-12">
					<h2 class="text-gestion">GESTIÓN DE ALUMNOS</h2>
				</div>
			</div>

			<!-- Botones de navegación -->
			<div class="row d-flex justify-content-center">
				<div class="col-12 d-flex justify-content-center mt-4 mb-2">
					<button id="btnVerAlumno" class="boton-modificar">Alumno</button>
					<button id="btnAgregarAlumno" class="boton-modificar"
						style="margin-left: 10vw">Agregar alumno</button>
					<button id="btnMatricularAlumno" class="boton-modificar"
						style="margin-left: 10vw">Matricular</button>

				</div>
			</div>

			<!-- Formulario Agregar Alumno -->
			<div id="formAgregarAlumno" class="form-container mt-3">
				<div class="row d-flex justify-content-center">
					<div class="col-12 d-flex justify-content-center mb-3">
						<h3 class="text-registrar mt-2">
							<u>REGISTRAR NUEVO ALUMNO</u>
						</h3>
					</div>
				</div>

				<form id="formAlumno1" onsubmit="return guardarAlumno();">
					<input type="hidden" name="accion" value="guardar">

					<div class="row d-flex justify-content-center mb-4">
						<div class="col-12 d-flex justify-content-center">
							<label for="nombre" class="label-formulario"><strong>Nombre:</strong></label>
							<input class="input-formulario2" type="text" id="nombre"
								name="nombre" required> <label for="apellidos"
								class="label-formulario"><strong>Apellidos:</strong></label> <input
								class="input-formulario2" type="text" id="apellidos"
								name="apellidos" required>
						</div>
					</div>

					<div class="row d-flex justify-content-center mb-3">
						<div class="col-12 d-flex justify-content-center">
							<h4 class="datos">
								<u>DATOS DE MATRICULACIÓN</u>
							</h4>
						</div>
					</div>

					<div class="row d-flex justify-content-center mb-4">
						<div class="col-12 d-flex justify-content-center">
							<label for="curso" class="label-formulario"><strong>Curso:</strong></label>
							<select id="curso" name="curso" class="input-formulario" required>
								<option value="">Seleccione...</option>
							</select> <label for="grupo" class="label-formulario"><strong>Grupo:</strong></label>
							<select id="grupo" name="grupo" class="input-formulario" required>
								<option value="">Seleccione...</option>
							</select> <label for="anioEscolar" class="label-formulario"><strong>Año
									escolar:</strong></label> <select id="anioEscolar" name="anioEscolar"
								class="input-formulario" required>
								<option value="">Seleccione...</option>
							</select> <label for="uidLlave" class="label-formulario"><strong>UID:</strong></label>
							<input class="input-formulario" type="text" id="uidLlave"
								name="uidLlave" required>
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
			<div id="formMatricular" class="form-container mt-3"
				style="display: none;">
				<div class="row d-flex justify-content-center">
					<div class="col-12 d-flex justify-content-center mb-3">
						<h3 class="text-registrar mt-2">
							<u>REGISTRAR NUEVA MATRÍCULA</u>
						</h3>
					</div>
				</div>

				<form id="formAlumno2" onsubmit="return guardarMatriculacion();">
					<input type="hidden" name="accion" value="guardar"> <input
						type="hidden" id="idAlumnoSeleccionado"
						name="idAlumnoSeleccionado">

					<div class="row d-flex justify-content-center mb-4">
						<div
							class="col-12 d-flex align-items-center justify-content-center"
							style="gap: 1rem; flex-wrap: wrap; position: relative;">
							<label for="nombreYApellidos" class="label-formulario"
								style="margin-right: -0.8vw"><strong>Nombre
									completo:</strong></label>
							<div class="input-wrapper"
								style="position: relative; display: inline-block;">
								<input type="text" id="nombreYApellidos" name="nombreYApellidos"
									placeholder="Escribe el nombre del alumno" autocomplete="off"
									style="padding: 0.06rem 0.8rem; width: 18rem;">

								<div id="lista-autocompletar-alumno"
									style="position: absolute; top: 100%; left: 0; width: 100%; background: #fff; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); z-index: 1000; max-height: 150px; overflow-y: auto;">
								</div>


							</div>

							<button type="button" id="btnSeleccionarAlumno"
								class="boton-buscar">Seleccionar</button>
						</div>
					</div>

					<div class="row d-flex justify-content-center mb-3">
						<div class="col-12 d-flex justify-content-center">
							<h4 class="datos">
								<u>DATOS DE MATRICULACIÓN</u>
							</h4>
						</div>
					</div>

					<div class="row d-flex justify-content-center mb-4">
						<div class="col-12 d-flex justify-content-center">
							<label for="cursoMat" class="label-formulario"><strong>Curso:</strong></label>
							<select id="cursoMat" name="curso" class="input-formulario"
								required>
								<option value="">Seleccione...</option>
							</select> <label for="grupoMat" class="label-formulario"><strong>Grupo:</strong></label>
							<select id="grupoMat" name="grupo" class="input-formulario"
								required>
								<option value="">Seleccione...</option>
							</select> <label for="anioEscolarMat" class="label-formulario"><strong>Año
									escolar:</strong></label> <select id="anioEscolarMat" name="anioEscolar"
								class="input-formulario" required>
								<option value="">Seleccione...</option>
							</select> <label for="uidLlaveMat" class="label-formulario"><strong>UID:</strong></label>
							<input class="input-formulario" type="text" id="uidLlaveMat"
								name="uidLlave" required>
						</div>
					</div>

					<div class="row d-flex justify-content-center mb-3">
						<div class="col-12 d-flex justify-content-center">
							<button type="submit" class="boton">Guardar Matrícula</button>
						</div>
					</div>
				</form>
			</div>


			<div id="formVerAlumno" class="form-container mt-3"
				style="display: none;">
				<div class="row d-flex justify-content-center mb-3">
					<h3 class="text-registrar mt-2 mb-4">
						<u>ACCIONES ALUMNO</u>
					</h3>
					<div
						class="col-12 d-flex flex-wrap justify-content-center align-items-start"
						style="gap: 0.5rem;">
						<label for="nombreVerAlumno" class="label-formulario"
							style="margin-top: 0.4rem;"><strong>Nombre
								completo:</strong></label>

						<div class="d-flex" style="position: relative; gap: 0.5rem;">
							<input type="text" id="nombreVerAlumno"
								placeholder="Escribe el nombre del alumno" autocomplete="off"
								style="padding: 0.4rem 0.8rem; min-width: 18rem; box-sizing: border-box;">

							<button type="button" id="btnSeleccionarAlumnoVer"
								class="boton-buscar"
								style="padding: 0.4rem 1rem; margin-left: 1.2vw">Seleccionar</button>

							<div id="lista-autocompletar-ver-alumno"
								style="position: absolute; top: 100%; left: 0; width: 100%; background: #fff; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); z-index: 1000; max-height: 150px; overflow-y: auto;">
							</div>
						</div>
					</div>

				</div>

				<div id="resultadoAlumno" style="margin-top: 2rem;"></div>
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
    const btnVerAlumno = document.getElementById('btnVerAlumno');
    const formVerAlumno = document.getElementById('formVerAlumno');

    const resultadoAlumno = document.getElementById('resultadoAlumno');
    let alumnosCache = [];
    let idAlumnoSeleccionadoVer = null;

    function mostrarFormulario(form, btn) {
        formAgregar.style.display = 'none';
        formMatricular.style.display = 'none';
        formVerAlumno.style.display = 'none';

        form.style.display = 'block';

        btnAgregar.classList.remove('activo');
        btnMatricular.classList.remove('activo');
        btnVerAlumno.classList.remove('activo');
        btn.classList.add('activo');
    }

    // --- Event listeners de los botones ---
    btnAgregar.addEventListener('click', () => {
        mostrarFormulario(formAgregar, btnAgregar);
    });

    btnMatricular.addEventListener('click', () => {
        mostrarFormulario(formMatricular, btnMatricular);
    });

    btnVerAlumno.addEventListener('click', () => {
        resultadoAlumno.innerHTML = '';
        nombreInputVer.value = '';
        idAlumnoSeleccionadoVer = null;
        mostrarFormulario(formVerAlumno, btnVerAlumno);
    });

    // --- Al cargar la página, mostrar Ver Alumno por defecto ---
    mostrarFormulario(formVerAlumno, btnVerAlumno);

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

    // --- AUTOCOMPLETADO GENÉRICO ---
    function crearAutocompletado(inputElement, listaElement, idHidden) {
        inputElement.addEventListener('input', () => {
            const valorUsuario = inputElement.value.trim();
            idHidden.value = '';
            listaElement.innerHTML = '';
            if (!valorUsuario) return;

            const valorLower = valorUsuario.toLowerCase();
            const coincidencias = alumnosCache
                .map(a => ({ id: a.idAlumno, nombre: a.nombreAlumno + ' ' + a.apellidoAlumno }))
                .filter(a => a.nombre.toLowerCase().includes(valorLower))
                .sort((a,b) => a.nombre.localeCompare(b.nombre))
                .slice(0, 3);

            coincidencias.forEach(a => {
                const item = document.createElement('div');
                item.textContent = a.nombre;
                item.style.padding = '5px';
                item.style.cursor = 'pointer';

                item.addEventListener('mousedown', () => {
                    inputElement.value = a.nombre;
                    idHidden.value = a.id;
                    listaElement.innerHTML = '';
                });

                listaElement.appendChild(item);
            });
        });

        inputElement.addEventListener('blur', () => {
            setTimeout(() => listaElement.innerHTML = '', 100);
        });
    }

    // --- ELEMENTOS AUTOCOMPLETADO ---
    const nombreInput = document.getElementById('nombreYApellidos');
    const idAlumnoHidden = document.getElementById('idAlumnoSeleccionado');
    const listaAutocomplete = document.getElementById('lista-autocompletar-alumno');

    const nombreInputVer = document.getElementById('nombreVerAlumno');
    const listaAutocompleteVer = document.getElementById('lista-autocompletar-ver-alumno');
    const idAlumnoVer = document.createElement('input'); // hidden temporal si no existe
    idAlumnoVer.type = 'hidden';
    formVerAlumno.appendChild(idAlumnoVer);

    // --- CARGAR ALUMNOS ---
    fetch('<%=request.getContextPath()%>/alumno')
        .then(res => res.json())
        .then(data => { alumnosCache = data; })
        .catch(err => console.error('Error cargando alumnos:', err));

    // --- INICIALIZAR AUTOCOMPLETADOS ---
    crearAutocompletado(nombreInput, listaAutocomplete, idAlumnoHidden);
    crearAutocompletado(nombreInputVer, listaAutocompleteVer, idAlumnoVer);

    // --- BOTONES SELECCIONAR ALUMNO ---
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

    document.getElementById('btnSeleccionarAlumnoVer').addEventListener('click', () => {
        const valor = nombreInputVer.value.trim().toLowerCase();
        const seleccionado = alumnosCache
            .map(a => ({id: a.idAlumno, nombre: a.nombreAlumno + ' ' + a.apellidoAlumno}))
            .find(a => a.nombre.toLowerCase() === valor);

        if (!seleccionado) {
            alert('Selecciona un alumno válido de la sugerencia inline');
            idAlumnoVer.value = '';
            return;
        }

        idAlumnoSeleccionadoVer = seleccionado.id;
        idAlumnoVer.value = seleccionado.id;
        alert('Alumno seleccionado: ' + seleccionado.nombre);
        mostrarDatosAlumno(seleccionado.id);
    });

    // --- FUNCIONES MOSTRAR Y ELIMINAR ALUMNO/MATRÍCULA ---
window.mostrarDatosAlumno = function(idAlumno) {
    fetch('<%=request.getContextPath()%>/alumno?id=' + idAlumno)
        .then(res => res.json())
        .then(alumno => {

            fetch('<%=request.getContextPath()%>/matriculacion?idAlumno=' + idAlumno)
                .then(res => res.json())
                .then(matriculas => {
                    alumno.matriculas = matriculas;

                    const resultado = document.createElement('div');

                    // --- Tabla de Alumno ---
                    const tableAlumno = document.createElement('table');
                    tableAlumno.classList.add('table', 'table-striped', 'table-hover', 'table-bordered', 'shadow-sm');
                    tableAlumno.style.width = '80%';
                    tableAlumno.style.margin = '1rem auto';

                    const theadAlumno = document.createElement('thead');
                    const headerRowAlumno = document.createElement('tr');
                    ['Nombre', 'Apellidos', 'Acción'].forEach(h => {
                        const th = document.createElement('th');
                        th.textContent = h;
                        th.style.backgroundColor = "#032b38";
                        th.style.color = "#ffffff";
                        th.style.textAlign = 'center';
                        th.style.position = 'sticky';
                        th.style.top = '0';
                        headerRowAlumno.appendChild(th);
                    });
                    theadAlumno.appendChild(headerRowAlumno);
                    tableAlumno.appendChild(theadAlumno);

                    const tbodyAlumno = document.createElement('tbody');
                    const trAlumno = document.createElement('tr');
                    trAlumno.style.backgroundColor = "#ffffff";
                    [alumno.nombreAlumno, alumno.apellidoAlumno].forEach(val => {
                        const td = document.createElement('td');
                        td.textContent = val;
                        td.style.textAlign = 'center';
                        trAlumno.appendChild(td);
                    });

                 
                    const tdAccion = document.createElement('td');
                    tdAccion.style.textAlign = 'center';
                    tdAccion.style.verticalAlign = 'middle';
                    const btnEliminar = document.createElement('button');
                    btnEliminar.classList.add('btn', 'btn-danger');
                    btnEliminar.style.padding = '0.25rem 0.4rem'; 
                    btnEliminar.style.fontSize = '1.2rem'; 
                    btnEliminar.style.lineHeight = '1'; 
                    btnEliminar.innerHTML = '<i class="bi bi-trash"></i>'; 
                    btnEliminar.onclick = () => eliminarAlumno(alumno.idAlumno);
                    tdAccion.appendChild(btnEliminar);
                    trAlumno.appendChild(tdAccion);


                    tbodyAlumno.appendChild(trAlumno);
                    tableAlumno.appendChild(tbodyAlumno);
                    resultado.appendChild(tableAlumno);

                    // --- Tabla de Matriculas ---
                    if (alumno.matriculas && alumno.matriculas.length > 0) {
                        const tableMatriculas = document.createElement('table');
                        tableMatriculas.classList.add('table', 'table-striped', 'table-hover', 'table-bordered', 'shadow-sm');
                        tableMatriculas.style.width = '80%';
                        tableMatriculas.style.margin = '2rem auto'; 

                        const theadMat = document.createElement('thead');
                        const headerRowMat = document.createElement('tr');
                        ['Curso', 'Grupo', 'Año escolar', 'UID', 'Acción'].forEach(h => {
                            const th = document.createElement('th');
                            th.textContent = h;
                            th.style.backgroundColor = "#032b38";
                            th.style.color = "#ffffff";
                            th.style.textAlign = 'center';
                            th.style.position = 'sticky';
                            th.style.top = '0';
                            headerRowMat.appendChild(th);
                        });
                        theadMat.appendChild(headerRowMat);
                        tableMatriculas.appendChild(theadMat);

                        const tbodyMat = document.createElement('tbody');
                        alumno.matriculas.forEach((m, index) => {
                            const tr = document.createElement('tr');
                            tr.style.backgroundColor = index % 2 === 0 ? "#ffffff" : "#f1f3f5";

                            [m.nombreCurso, m.nombreGrupo, m.anioEscolar, m.uidLlave].forEach(val => {
                                const td = document.createElement('td');
                                td.textContent = val;
                                td.style.textAlign = 'center';
                                tr.appendChild(td);
                            });

                            const tdAcc = document.createElement('td');
                            tdAcc.style.textAlign = 'center';
                            tdAcc.style.verticalAlign = 'middle';
                            const btnElim = document.createElement('button');
                            btnElim.classList.add('btn', 'btn-danger');
                            btnElim.style.padding = '0.25rem 0.4rem';
                            btnElim.style.fontSize = '1.2rem';
                            btnElim.style.lineHeight = '1';
                            btnElim.innerHTML = '<i class="bi bi-trash"></i>';
                            btnElim.onclick = () => eliminarMatricula(m.idMatriculacion);
                            tdAcc.appendChild(btnElim);
                            tr.appendChild(tdAcc);


                            tbodyMat.appendChild(tr);
                        });

                        tableMatriculas.appendChild(tbodyMat);
                        resultado.appendChild(tableMatriculas);
                    }

                    resultadoAlumno.innerHTML = '';
                    resultadoAlumno.appendChild(resultado);

                }).catch(err => console.error('Error cargando matriculas:', err));

        }).catch(err => console.error('Error cargando alumno:', err));
}


window.eliminarAlumno = function(idAlumno) {
    const confirmacion = prompt('Escribe "si" para confirmar la eliminación del alumno:');
    
    if (!confirmacion || confirmacion.toLowerCase() !== 'si') {
        alert('Eliminación cancelada');
        return;
    }

    fetch('<%=request.getContextPath()%>/alumno?id=' + idAlumno, { method: 'DELETE' })
        .then(res => res.json())
        .then(respuesta => {
            if (respuesta.success) {
                alert('Alumno eliminado correctamente');
                resultadoAlumno.innerHTML = '';
                nombreInputVer.value = '';
                
                fetch('<%=request.getContextPath()%>/alumno')
                .then(res => res.json())
                .then(data => { 
                    alumnosCache = data; 
            
                });
            } else {
                alert('Error al eliminar alumno: ' + (respuesta.mensaje || 'Desconocido'));
            }
        })
        .catch(err => {
            console.error('Error al eliminar alumno:', err);
            alert('Error al eliminar alumno');
        });
}


window.eliminarMatricula = function(idMatriculacion) {
    const confirmacion = prompt('Escribe "si" para confirmar la eliminación de la matrícula:');
    if (confirmacion !== 'si') return;

    fetch('<%=request.getContextPath()%>/matriculacion?id=' + idMatriculacion, { method: 'DELETE' })
        .then(res => {
            if (res.ok) {
                alert('Matrícula eliminada correctamente');
                if (idAlumnoSeleccionadoVer) mostrarDatosAlumno(idAlumnoSeleccionadoVer);
            } else {
                alert('Error al eliminar matrícula');
            }
        });
}


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
                    
                    fetch('<%=request.getContextPath()%>/alumno')
                    .then(res => res.json())
                    .then(data => { alumnosCache = data; });
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

});
</script>


	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		crossorigin="anonymous"></script>

	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>


</body>
</html>