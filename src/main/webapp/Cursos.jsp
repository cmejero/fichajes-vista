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
							<a href="Alumno.jsp" class="letraNavegacion">ALUMNOS</a>
						</div>
						<div class="col-md-3 col-sm-3 col-3 alineacion">
							<a href="Cursos.jsp" class="letraNavegacion"
								style="color: #FFD43B; text-decoration: underline">CURSOS</a>
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

			<!-- Título -->
			<div class="row">
				<div class="col-12">
					<h2 class="titulo-asistencia mt-3"><u>GESTIÓN DE CURSOS</u></h2>
				</div>
			</div>

			<!-- Botones de navegación -->
			<div class="row d-flex justify-content-center">
				<div class="col-12 d-flex justify-content-center mt-4 mb-2">
					<button id="btnVerCurso" class="boton-modificar">Cursos</button>
					<button id="btnAgregarCurso" class="boton-modificar"
						style="margin-left: 10vw;">Agregar Curso</button>
				</div>
			</div>

			<!-- CONTENEDOR FORMULARIOS -->
			<div id="formAgregarCurso" class="form-container mt-3">


				<div class="row">
					<div class="col-12 d-flex justify-content-center mb-3">
						<h3 class="text-registrar mt-2">
							<u>REGISTRAR NUEVO CURSO</u>
						</h3>
					</div>
				</div>

				<div class="row">
					<div class="col-12">
						<!-- FORMULARIO CURSO -->
						<form id="formCurso" onsubmit="return guardarCurso();">
							<input type="hidden" name="accion" value="guardarCurso">

							<div class="row d-flex justify-content-center mb-4">
								<div
									class="col-12 d-flex flex-wrap justify-content-center align-items-start"
									style="gap: .8rem;">

									<label for="nombreCurso" class="label-formulario"><strong>Nombre
											Curso:</strong></label> <input class="input-formulario2" type="text"
										id="nombreCurso" name="nombreCurso" required>

									<!-- Botón -->
									<button type="submit" class="boton"
										style="padding: .2rem .8rem;">Guardar Curso</button>

								</div>
							</div>
						</form>

					</div>
				</div>


				<div class="row">
					<div class="col-12 d-flex justify-content-center mb-3">
						<h4 class="datos">
							<u>REGISTRAR NUEVO GRUPO</u>
						</h4>
					</div>
				</div>

				<div class="row">
					<div class="col-12">

						<form id="formGrupo" onsubmit="return guardarGrupo();">
							<input type="hidden" name="accion" value="guardarGrupo">

							<!-- Fila curso + grupo -->
							<div class="row d-flex justify-content-center mb-4">
								<div
									class="col-12 d-flex flex-wrap justify-content-center align-items-start"
									style="gap: .8rem;">

									<!-- Curso -->
									<label for="curso" class="label-formulario"><strong>Curso:</strong></label>
									<select id="curso" name="curso" class="input-formulario"
										required>
										<option value="">Seleccione...</option>
									</select>

									<!-- Grupo -->
									<label for="nombreGrupo" class="label-formulario"><strong>Nombre
											Grupo:</strong></label> <input class="input-formulario2" type="text"
										id="nombreGrupo" name="nombreGrupo" required>

									<!-- Botón -->
									<button type="submit" class="boton"
										style="padding: .2rem .8rem;">Guardar Grupo</button>

								</div>
							</div>

						</form>

					</div>
				</div>

			</div>


			<div id="formVerCurso" class="form-container mt-3">
				<div class="row d-flex justify-content-center mb-3">
					<h3 class="text-registrar mt-2 mb-4">
						<u>ACCIONES CURSOS Y GRUPOS</u>
					</h3>

					<div id="contenedorCursosYGrupos"
						class="col-12 d-flex justify-content-center align-items-start"
						style="gap: 1rem; flex-wrap: wrap;"></div>
				</div>
			</div>


		</section>
	</main>
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

	    const btnVerCurso = document.getElementById('btnVerCurso');
	    const btnAgregarCurso = document.getElementById('btnAgregarCurso');
	    const formVerCurso = document.getElementById('formVerCurso');
	    const formAgregarCurso = document.getElementById('formAgregarCurso');

	    function mostrarContenedor(contenedorAMostrar, botonActivo) {
	        // Ocultar ambos contenedores primero
	        formVerCurso.style.display = 'none';
	        formAgregarCurso.style.display = 'none';

	        // Quitar el estilo activo de ambos botones
	        btnVerCurso.style.backgroundColor = '';
	        btnAgregarCurso.style.backgroundColor = '';

	        // Mostrar el contenedor seleccionado
	        contenedorAMostrar.style.display = 'block';

	        // Marcar el botón activo en amarillo
	        botonActivo.style.backgroundColor = '#FFD43B';
	    }

	    // Event listeners
	    btnVerCurso.addEventListener('click', () => {
	        mostrarContenedor(formVerCurso, btnVerCurso);
	        // Opcional: cargar las tablas al mostrar "ver cursos"
	        if (typeof cargarCursosYGrupos === 'function') cargarCursosYGrupos();
	    });

	    btnAgregarCurso.addEventListener('click', () => {
	        mostrarContenedor(formAgregarCurso, btnAgregarCurso);
	    });

	    // Mostrar por defecto el contenedor de ver cursos
	    mostrarContenedor(formVerCurso, btnVerCurso);
	});
		function cargarCursosYGrupos() {
			var contenedor = document.getElementById('contenedorCursosYGrupos');
			contenedor.innerHTML = ''; // Limpiar contenedor

			// --- Traer Cursos ---
			var xhrCursos = new XMLHttpRequest();
			xhrCursos.open('GET', 'curso', true);
			xhrCursos.onreadystatechange = function() {
				if (xhrCursos.readyState === 4 && xhrCursos.status === 200) {
					var cursos = [];
					try {
						cursos = JSON.parse(xhrCursos.responseText);
					} catch (e) {
						console.error(e);
					}
					
				    var selectCurso = document.getElementById('curso');
			        selectCurso.innerHTML = '<option value="">Seleccione...</option>'; // Limpiar opciones
			        cursos.forEach(function(c) {
			            var option = document.createElement('option');
			            option.value = c.idCurso;       // enviamos el ID del curso al guardar grupo
			            option.textContent = c.nombreCurso;
			            selectCurso.appendChild(option);
			        });
				

					// --- Traer Grupos ---
					var xhrGrupos = new XMLHttpRequest();
					xhrGrupos.open('GET', 'grupo', true); // Llamamos a /grupo sin ID
					xhrGrupos.onreadystatechange = function() {
						if (xhrGrupos.readyState === 4
								&& xhrGrupos.status === 200) {
							var grupos = [];
							try {
								grupos = JSON.parse(xhrGrupos.responseText);
							} catch (e) {
								console.error(e);
							}
							
							

							// --- Procesar tablas ---
							if ((!cursos || cursos.length === 0)
									&& (!grupos || grupos.length === 0)) {
								var noData = document.createElement('div');
								noData.textContent = "Aún no hay registros disponibles";
								noData.style.textAlign = 'center';
								noData.style.fontStyle = 'italic';
								noData.style.marginTop = '1rem';
								contenedor.appendChild(noData);
								return;
							}
							// --- Tabla Cursos  ---
							if (cursos.length > 0) {
								var colCursos = document.createElement('div');
								colCursos.style.width = '48%';
								colCursos.style.display = 'inline-block';
								colCursos.style.verticalAlign = 'top';

								var tableCursos = document
										.createElement('table');
								tableCursos.className = 'table table-striped table-hover table-bordered shadow-sm';
								tableCursos.style.width = '36%';
								tableCursos.style.margin = '0 auto';
								colCursos.appendChild(tableCursos);

								var thead = document.createElement('thead');
								var headerRow = document.createElement('tr');
								[ 'Curso', 'Acciones' ].forEach(function(h) {
									var th = document.createElement('th');
									th.textContent = h;
									th.style.backgroundColor = "#032b38";
									th.style.color = "#fff";
									th.style.textAlign = 'center';
									headerRow.appendChild(th);
								});
								thead.appendChild(headerRow);
								tableCursos.appendChild(thead);

								var tbody = document.createElement('tbody');
								cursos
										.forEach(function(c, i) {
											var tr = document
													.createElement('tr');
											tr.style.backgroundColor = i % 2 === 0 ? "#fff"
													: "#f1f3f5";

											var tdN = document
													.createElement('td');
											tdN.textContent = c.nombreCurso
													|| '';
											tdN.style.textAlign = 'center';
											var tdAcc = document
													.createElement('td');
											tdAcc.style.textAlign = 'center';
											var btnDel = document
													.createElement('button');
											btnDel.className = 'btn btn-danger';
											btnDel.innerHTML = '<i class="bi bi-trash"></i>';
											btnDel.style.fontSize = '1.2rem';
											btnDel.classList.add('btn',
													'btn-danger');
											btnDel.style.padding = '0.25rem 0.4rem';
											btnDel.style.lineHeight = '1';
											btnDel.onclick = function() {
												eliminarCurso(c.idCurso);
											};
											tdAcc.appendChild(btnDel);

											tr.appendChild(tdN);
											tr.appendChild(tdAcc);
											tbody.appendChild(tr);
										});
								tableCursos.appendChild(tbody);
								contenedor.appendChild(tableCursos);
							}

							// --- Tabla Grupos ---
							if (grupos.length > 0) {
								var colGrupos = document.createElement('div');
								colGrupos.style.width = '48%';
								colGrupos.style.display = 'inline-block';
								colGrupos.style.verticalAlign = 'top';

								var tableGrupos = document
										.createElement('table');
								tableGrupos.className = 'table table-striped table-hover table-bordered shadow-sm';
								tableGrupos.style.width = '60%';
								tableGrupos.style.margin = '0 auto';
								colGrupos.appendChild(tableGrupos);
								var thead = document.createElement('thead');
								var headerRow = document.createElement('tr');
								[ 'Grupo', 'Curso', 'Acciones' ]
										.forEach(function(h) {
											var th = document
													.createElement('th');
											th.textContent = h;
											th.style.backgroundColor = "#032b38";
											th.style.color = "#fff";
											th.style.textAlign = 'center';
											headerRow.appendChild(th);
										});
								thead.appendChild(headerRow);
								tableGrupos.appendChild(thead);

								var tbody = document.createElement('tbody');
								grupos
										.forEach(function(g, i) {
											var tr = document
													.createElement('tr');
											tr.style.backgroundColor = i % 2 === 0 ? "#fff"
													: "#f1f3f5";

											var tdG = document
													.createElement('td');
											tdG.textContent = g.nombreGrupo
													|| '';
											tdG.style.textAlign = 'center';
											var tdC = document
													.createElement('td');
											tdC.textContent = g.nombreCurso
													|| '';
											tdC.style.textAlign = 'center';
											var tdAcc = document
													.createElement('td');
											tdAcc.style.textAlign = 'center';
											var btnDel = document
													.createElement('button');
											btnDel.className = 'btn btn-danger';
											btnDel.innerHTML = '<i class="bi bi-trash"></i>';
											btnDel.onclick = function() {
												eliminarGrupo(g.idGrupo);
											};
											btnDel.style.fontSize = '1.2rem';
											btnDel.classList.add('btn',
													'btn-danger');
											btnDel.style.padding = '0.25rem 0.4rem';
											btnDel.style.lineHeight = '1';
											tdAcc.appendChild(btnDel);

											tr.appendChild(tdG);
											tr.appendChild(tdC);
											tr.appendChild(tdAcc);
											tbody.appendChild(tr);
										});
								tableGrupos.appendChild(tbody);
								contenedor.appendChild(tableGrupos);
							}

						}
					};
					xhrGrupos.send();

				}
			};
			xhrCursos.send();
		}
		function guardarCurso() {
		    var nombreCurso = document.getElementById('nombreCurso').value;

		    var xhr = new XMLHttpRequest();
		    xhr.open('POST', 'curso', true);
		    xhr.setRequestHeader('Content-Type', 'application/json');
		    xhr.onreadystatechange = function() {
		        if(xhr.readyState === 4 && xhr.status === 200){
		            alert('Curso guardado correctamente');
		            cargarCursosYGrupos(); // refrescar tablas
		        }
		    };
		    var data = JSON.stringify({ nombreCurso: nombreCurso, accion: 'guardarCurso' });
		    xhr.send(data);

		    return false; 
		}

		function guardarGrupo() {
		    var nombreGrupo = document.getElementById('nombreGrupo').value;
		    var cursoId = document.getElementById('curso').value;

		    var xhr = new XMLHttpRequest();
		    xhr.open('POST', 'grupo', true);
		    xhr.setRequestHeader('Content-Type', 'application/json');
		    xhr.onreadystatechange = function() {
		        if(xhr.readyState === 4 && xhr.status === 200){
		            alert('Grupo guardado correctamente');
		            cargarCursosYGrupos(); // refrescar tablas
		        }
		    };

		    // JSON con los datos del grupo
		    var data = JSON.stringify({ 
		        nombreGrupo: nombreGrupo,
		        cursoId: Number(cursoId) // convertir a número
		    });

		    xhr.send(data);

		    return false; // evitar submit normal
		}
		
		window.eliminarCurso = async function(idCurso) {
		    const confirmacion = prompt('⚠️ Atención: eliminar este curso también eliminará todos los grupos y matriculaciones asociadas. Escribe "CONFIRMAR" para confirmar la eliminación:');
		    if (!confirmacion || confirmacion.toLowerCase() !== 'confirmar') {
		        alert('Eliminación cancelada');
		        return;
		    }

		    try {
		        const resp = await fetch('<%=request.getContextPath()%>/curso?id=' + idCurso, { method: 'DELETE' });
		        const data = await resp.json();

		        if (data.success) {
		            alert('Curso eliminado correctamente');
		            cargarCursosYGrupos(); // refresca la tabla
		        } else {
		            alert('Error al eliminar curso: ' + (data.mensaje || 'Desconocido'));
		        }
		    } catch (err) {
		        console.error('Error al eliminar curso:', err);
		        alert('Error al eliminar curso');
		    }
		};

		window.eliminarGrupo = async function(idGrupo) {
		    const confirmacion = prompt('⚠️ Atención: eliminar este grupo también eliminará todas las matriculaciones de alumnos asociadas. Escribe "CONFIRMAR" para confirmar la eliminación:');
		    if (!confirmacion || confirmacion.toLowerCase() !== 'confirmar') {
		        alert('Eliminación cancelada');
		        return;
		    }

		    try {
		        const resp = await fetch('<%=request.getContextPath()%>/grupo?id=' + idGrupo, { method: 'DELETE' });
		        const data = await resp.json();

		        if (data.success) {
		            alert('Grupo eliminado correctamente');
		            cargarCursosYGrupos(); // refresca la tabla
		        } else {
		            alert('Error al eliminar grupo: ' + (data.mensaje || 'Desconocido'));
		        }
		    } catch (err) {
		        console.error('Error al eliminar grupo:', err);
		        alert('Error al eliminar grupo');
		    }
		};




		cargarCursosYGrupos();
	</script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		crossorigin="anonymous"></script>

</body>
</html>