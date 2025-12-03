<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Asistencia - Colegio Altair Sevilla</title>
<link rel="stylesheet" href="Css/Estilo.css">
<!-- Bootstrap CSS (solo una vez) -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet" crossorigin="anonymous">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
	crossorigin="anonymous"></script>


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
<main class="asistencia-main d-flex flex-column align-items-center justify-content-center">
    <section class="tabla-asistencia container shadow p-4 rounded">

        <h2 class="text-center mb-4 text-altair">Asistencia del día</h2>

        <!-- Contenedor donde se insertará la tabla dinámica -->
        <div id="contenedorTablaAsistencia" class="table-responsive"></div>

        <!-- Botones -->
        <div class="text-center mt-4">
            <button class="boton-modificar me-3">
                <i class="bi bi-pencil-square"></i> Modificar
            </button>
            <a href="Index.jsp" class="boton">Volver al inicio</a>
        </div>

    </section>
</main>


		<!-- Modal para editar asistencia -->
		<div class="modal fade" id="modalEditarAsistencia" tabindex="-1"
			aria-labelledby="modalEditarAsistenciaLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<form id="formEditarAsistencia">
						<div class="modal-header">
							<h5 class="modal-title" id="modalEditarAsistenciaLabel">Editar
								Asistencia</h5>
							<button type="button" class="btn-close" data-bs-dismiss="modal"
								aria-label="Cerrar"></button>
						</div>
						<div class="modal-body">
							<input type="hidden" id="idAsistencia">

							<div class="mb-3">
								<label for="horaEntrada" class="form-label">Hora de
									Entrada</label> <input type="time" class="form-control"
									id="horaEntrada" required>
							</div>

							<div class="mb-3">
								<label for="horaSalida" class="form-label">Hora de
									Salida</label> <input type="time" class="form-control" id="horaSalida">
							</div>

							<div class="mb-3">
								<label for="estadoAsistencia" class="form-label">Estado</label>
								<select id="estadoAsistencia" class="form-select">
									<option value="PRESENTE">PRESENTE</option>
									<option value="FALTA">FALTA</option>
									<option value="COMPLETA">COMPLETA</option>
									<option value="SIN SALIDA">SIN SALIDA</option>
								</select>
							</div>
							<div class="mb-3">
								<label for="justificarModificacion" class="form-label">Justificación</label>
								<textarea id="justificarModificacion" class="form-control"
									rows="3"
									placeholder="Escribe aquí la justificación de la modificación"
									required></textarea>
							</div>

						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-secondary"
								data-bs-dismiss="modal">Cancelar</button>
							<button type="submit" class="btn btn-primary">Guardar
								cambios</button>
						</div>
					</form>
				</div>
			</div>
		</div>

	</main>

	<!-- FOOTER -->
	<footer class="altair-footer mt-auto">
		<div class="container">
			<div class="row">
				<div class="col-12" style="margin-top: 1vw"></div>
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

	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
	
	<script>
document.addEventListener('DOMContentLoaded', function() {

    cargarAsistencias();

    // --- Botón modificar ---
    var botonesModificar = document.querySelectorAll('.boton-modificar');
    botonesModificar.forEach(function(boton) {
        boton.addEventListener('click', function() {
        	var tabla = document.querySelector('#contenedorTablaAsistencia table');
        	var filaSeleccionada = tabla ? tabla.querySelector('tr.table-active') : null;
            if (!filaSeleccionada) {
                alert('Primero seleccione un alumno.');
                return;
            }

            // Datos de la fila
            var id = filaSeleccionada.getAttribute('data-id');
            console.log("DEBUG: idAsistencia de la fila seleccionada ->", id);
            var horaEntrada = filaSeleccionada.getAttribute('data-horaentrada');
            var horaSalida = filaSeleccionada.getAttribute('data-horasalida');
            var estado = filaSeleccionada.querySelector('td:last-child').innerText.toUpperCase();
            var justificarModificacion = filaSeleccionada.getAttribute('data-justificarModificacion') || '';

            // Set valores en modal
            document.getElementById('idAsistencia').value = id;
            document.getElementById('horaEntrada').value = (horaEntrada !== "-" ? horaEntrada : '');
            document.getElementById('horaSalida').value = (horaSalida !== "-" ? horaSalida : '');
            document.getElementById('estadoAsistencia').value = estado;
            document.getElementById('justificarModificacion').value = justificarModificacion;

            // Mostrar modal
            var modal = new bootstrap.Modal(document.getElementById('modalEditarAsistencia'));
            modal.show();
        });
    });

    // --- Cambiar requerimiento de campos según estado ---
    document.getElementById('estadoAsistencia').addEventListener('change', function() {
        var horaEntrada = document.getElementById('horaEntrada');
        var horaSalida = document.getElementById('horaSalida');
        if (this.value === 'FALTA') {
            horaEntrada.value = '';
            horaSalida.value = '';
            horaEntrada.required = false;
            horaSalida.required = false;
        } else {
            horaEntrada.required = true;
        }
    });

    // --- Guardar cambios del modal ---
    document.getElementById('formEditarAsistencia').addEventListener('submit', function(e) {
        e.preventDefault();

        var id = document.getElementById('idAsistencia').value;
        if (!id) {
            alert('ID de asistencia vacío. Selecciona primero un alumno.');
            return;
        }
        var horaEntrada = document.getElementById('horaEntrada').value || '';
        var horaSalida = document.getElementById('horaSalida').value || '';	
        var estado = document.getElementById('estadoAsistencia').value;
        var justificarModificacion = document.getElementById('justificarModificacion').value;

        // Construir payload
        var payload = 'accion=modificar' +
                      '&idAsistencia=' + encodeURIComponent(id) +
                      '&horaEntrada=' + encodeURIComponent(horaEntrada) +
                      '&horaSalida=' + encodeURIComponent(horaSalida) +
                      '&estado=' + encodeURIComponent(estado) +
                      '&justificarModificacion=' + encodeURIComponent(justificarModificacion);

        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'asistencia', true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');

        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    try {
                        var respuesta = JSON.parse(xhr.responseText);

                        if (respuesta.success) {
                            var asistencia = respuesta.asistencia;
                            var fila = document.querySelector('#contenedorTablaAsistencia table tr.table-active');


                            fila.setAttribute('data-horaentrada', asistencia.horaEntrada || '-');
                            fila.setAttribute('data-horasalida', asistencia.horaSalida || '-');
                            fila.setAttribute('data-justificarModificacion', asistencia.justificarModificacion || '');

                            fila.cells[1].innerText = asistencia.horaEntrada || '-';
                            fila.cells[2].innerText = asistencia.horaSalida || '-';
                            fila.cells[3].innerText = asistencia.estado || '-';

                            fila.cells[3].className = '';
                            switch (asistencia.estado.toUpperCase()) {
                                case 'COMPLETA': fila.cells[3].className = 'estado-rojo fw-bold'; break;
                                case 'PRESENTE': fila.cells[3].className = 'text-success-light fw-bold'; break;
                                case 'SIN SALIDA': fila.cells[3].className = 'text-warning fw-bold'; break;
                                case 'FALTA': fila.cells[3].className = 'text-danger fw-bold'; break;
                                default: fila.cells[3].className = 'text-secondary fw-bold';
                            }

                            var modal = bootstrap.Modal.getInstance(document.getElementById('modalEditarAsistencia'));
                            modal.hide();

                            console.log("✅ Asistencia modificada correctamente:", asistencia);

                        } else {
                            alert('Error al actualizar asistencia: ' + (respuesta.error || 'Desconocido'));
                        }

                    } catch (err) {
                        console.error(err);
                        alert('Respuesta inválida del servidor.');
                    }
                } else {
                    console.error(xhr);
                    alert('Error al conectar con el servidor.');
                }
            }
        };

        console.log("DEBUG: Payload que se envía ->", payload);
        xhr.send(payload);
    });

    // --- Función cargar asistencias ---
    function cargarAsistencias() {
        var params = new URLSearchParams(window.location.search);
        var curso = params.get("curso");
        var grupo = params.get("grupo");

        var contenedor = document.getElementById('contenedorTablaAsistencia');

        if (!curso || !grupo) {
            contenedor.innerHTML = '<tr><td colspan="4">Faltan parámetros de curso o grupo.</td></tr>';
            return;
        }

        var xhr = new XMLHttpRequest();
        xhr.open('GET', 'asistencia?accion=porCursoGrupo&curso=' + encodeURIComponent(curso) + '&grupo=' + encodeURIComponent(grupo), true);
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    try {
                        var data = JSON.parse(xhr.responseText);

                        // Crear tabla completa
                        var table = document.createElement('table');
                        table.classList.add('table', 'table-striped', 'table-hover', 'table-bordered', 'shadow-sm');

                        // Cabecera
                        var headers = ['Alumno', 'Hora de entrada', 'Hora de salida', 'Estado'];
                        var thead = document.createElement('thead');
                        var headerRow = document.createElement('tr');
                        headers.forEach(h => {
                            var th = document.createElement('th');
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

                        // Cuerpo
                        var tbody = document.createElement('tbody');

                        if (!data || data.length === 0) {
                            var tr = document.createElement('tr');
                            var td = document.createElement('td');
                            td.textContent = "No hay asistencias registradas.";
                            td.colSpan = headers.length;
                            td.style.textAlign = 'center';
                            td.style.fontStyle = 'italic';
                            td.style.backgroundColor = "#f8f9fa";
                            tr.appendChild(td);
                            tbody.appendChild(tr);
                        } else {
                            data.forEach((asistencia, index) => {
                                var tr = document.createElement('tr');
                                tr.style.backgroundColor = index % 2 === 0 ? "#ffffff" : "#f1f3f5";

                                tr.setAttribute('data-id', asistencia.idAsistencia || '');
                                tr.setAttribute('data-horaentrada', asistencia.horaEntrada || '-');
                                tr.setAttribute('data-horasalida', asistencia.horaSalida || '-');
                                tr.setAttribute('data-justificarModificacion', asistencia.justificarModificacion || '');

                                var rowData = [
                                    asistencia.nombreCompletoAlumno,
                                    asistencia.horaEntrada || '-',
                                    asistencia.horaSalida || '-',
                                    asistencia.estado || 'Desconocido'
                                ];

                                rowData.forEach(value => {
                                    var td = document.createElement('td');
                                    td.textContent = value;
                                    td.style.textAlign = 'center';
                                    // Colorear la celda de estado
                                    if (value.toUpperCase) {
                                        switch (value.toUpperCase()) {
                                            case 'COMPLETA': td.className = 'estado-rojo fw-bold'; break;
                                            case 'PRESENTE': td.className = 'text-success-light fw-bold'; break;
                                            case 'SIN SALIDA': td.className = 'text-warning fw-bold'; break;
                                            case 'FALTA': td.className = 'text-danger fw-bold'; break;
                                            case 'FESTIVO': td.style = 'color: #f97c00'; break;
                                        }
                                    }
                                    tr.appendChild(td);
                                });

                                tbody.appendChild(tr);
                            });
                        }

                        table.appendChild(tbody);

                        // Limpiar contenedor y agregar tabla
                        contenedor.innerHTML = '';
                        contenedor.appendChild(table);

                        // Selección de fila
                        var filas = table.querySelectorAll('tr');
                        filas.forEach(function(f) {
                            f.addEventListener('click', function() {
                                filas.forEach(ff => ff.classList.remove('table-active'));
                                this.classList.add('table-active');
                            });
                        });

                    } catch (err) {
                        console.error(err);
                        contenedor.innerHTML = '<tr><td colspan="4">Error al cargar asistencias.</td></tr>';
                    }
                } else {
                    console.error(xhr);
                    contenedor.innerHTML = '<tr><td colspan="4">Error al cargar asistencias.</td></tr>';
                }
            }
        };
        xhr.send();
    }


});
</script>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		crossorigin="anonymous"></script>

</body>
</html>
