/* global Metronic, MyApp, TableEditableTareas */

(function () {
    'use strict';

    angular.module('app.tablero').controller('Tablero', Tablero);
    Tablero.$inject = ['$rootScope', '$scope', '$timeout', '$location', 'urlPath', 'tableroService', 'cargoService', 'areaService', 'gerenciaService', 'criticidadTareaService', 'usuarioService', 'planService'];

    function Tablero($rootScope, $scope, $timeout, $location, urlPath, tableroService, cargoService, areaService, gerenciaService, criticidadTareaService, usuarioService, planService) {
        var vm = this;
        vm.urlPath = urlPath;
        vm.urlPathImagen = urlPath;

        vm.tablero = tableroService.tablero;
        vm.cargos = cargoService.cargos;
        vm.areas = areaService.areas;
        vm.gerencias = gerenciaService.gerencias;
        vm.criticidades = criticidadTareaService.criticidades;
        vm.planes = planService.planes;
        vm.planId = 0;

        vm.nuevo = nuevo;
        vm.selecciono = false;
        vm.mostrarNuevo = true;
        $scope.titulo = "Tablero de Tareas";
        $scope.cargo = "";
        $scope.gerencia = "";
        $scope.tituloModal = "";
        $scope.tarea = {};
        $scope.plan = {};
        $scope.existePlanEjecucion = JSON.parse(localStorage.getItem("existePlan"));
        $scope.antecesoras = new Array();
        vm.usuario = usuarioService.usuario;
        $scope.direccion = "";
        $scope.deEquipo = false;
        $scope.nivelAlerta = "";
        $scope.mensaje = "";
        $scope.existePlanSeleccionado = $scope.existePlanEjecucion;
        console.log($scope.existePlanSeleccionado);
        vm.eliminar = eliminar;
        vm.modalEliminar = modalEliminar;
        vm.modalEliminarSeleccion = modalEliminarSeleccion;

        vm.cerrarForm = cerrarForm;
        vm.salvarForm = salvarForm;

        vm.nuevoModelo = nuevoModelo;
        vm.salvarModelo = salvarModelo;
        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageBodySolid = true;
        $rootScope.settings.layout.pageSidebarClosed = false;

        activate();

        function nuevo() {
            $timeout(function () {
                TableEditableTareas.btnClickNuevo();
            }, 0);
        }

        function eliminar() {
            TableEditableTareas.btnClickEliminar();
        }

        function modalEliminar() {
            TableEditableTareas.btnClickModalEliminar();
        }

        function modalEliminarSeleccion() {
            TableEditableTareas.btnClickModalEliminarSeleccion();
        }

        function cerrarForm() {
            $timeout(function () {
                TableEditableTareas.btnClickCerrarForm();
            }, 0);

        }

        function salvarForm() {
            TableEditableTareas.btnClickSalvarForm();
        }

        vm.seleccionarElemento = function (config) {
            var cargoSelect = $("#cargo");
            var gerenciaSelect = $("#gerencia");
            if (config.criticidad) {
                $scope.mensaje = "este nivel de alerta";
            }
            if (config.cargo) {
                $scope.mensaje = "este cargo";
            }
            if (config.area) {
                $scope.mensaje = "esta gerencia";
                $scope.gerencia = $("#gerencia").val();
                cargoSelect.select2("val", "");
                $scope.cargo = "";
                cargoService.getCargosArea($scope.gerencia).then(function (resp) {
                    vm.cargos = resp.data.cargos;
                }).catch(function () {

                });
                $scope.mensaje = "esta unidad";
            }
            if (config.direccion) {
                $scope.direccion = $("#direccion").val();
                cargoSelect.select2("val", "");
                gerenciaSelect.select2("val", "");
                $scope.cargo = "";
                $scope.gerencia = "";
                areaService.getAreasGerencia($scope.direccion).then(function (resp) {
                    vm.areas = resp.data.areas;
                    cargoSelect.select2("val", "");
                    gerenciaSelect.select2("val", "");
                }).catch(function () {

                });
                cargoService.getCargosGerencia($scope.direccion).then(function (resp) {
                    vm.cargos = resp.data.cargos;
                    cargoSelect.select2("val", "");
                    gerenciaSelect.select2("val", "");
                }).catch(function () {

                });
                $scope.mensaje = "esta dirección";
            }
        };

        vm.filtarTareasCargo = function () {
            Metronic.blockUI({target: '#lista-tarea .portlet-body', animate: true});
            var objeto = {};
            $scope.nivelAlerta = $("#nivel-alerta").val();
            if (parseInt($scope.nivelAlerta))
                $scope.nivelAlerta = parseInt($scope.nivelAlerta);
            else
                $scope.nivelAlerta = "";
            $scope.cargo = $("#cargo").val();
            if (parseInt($scope.cargo))
                $scope.cargo = parseInt($scope.cargo);
            else
                $scope.cargo = "";
            $scope.gerencia = $("#gerencia").val();
            if (parseInt($scope.gerencia))
                $scope.gerencia = parseInt($scope.gerencia);
            else
                $scope.gerencia = "";
            $scope.direccion = $("#direccion").val();
            if (parseInt($scope.direccion))
                $scope.direccion = parseInt($scope.direccion);
            else
                $scope.direccion = "";
            objeto = {
                cargo: $scope.cargo,
                gerencia: $scope.gerencia,
                direccion: $scope.direccion,
                criticidad: $scope.nivelAlerta
            };
            tableroService.buscarTablero(objeto).then(function (response) {
                $timeout(function () {
                    Metronic.unblockUI('#lista-tarea .portlet-body');
                    if (response.data.board.datos.length === 0) {
                        toastr.error("No se encontraron tareas para " + $scope.mensaje + ".", "Error !!!", {"positionClass": "toast-top-center"});
                    } else {
                        $('#kanban').jqxKanban('destroy');
                        var contenedor = angular.element("#kanbanBox1");
                        contenedor.append(angular.element('<div id="kanban" style="padding: 0 10px 0 10px;"></div>'));
                        vm.tablero = response.data.board;
                        iniciarKaban(vm.tablero);
                    }
                }, 1000);
            }).catch(function (error) {

            });
        };

        vm.mostrarColor = function (criticidad, tarea) {
            if (tarea.criticidad_id) {
                var contieneCriticidad = false;
                tarea.criticidad_id.forEach(function (elem) {
                    if (parseInt(elem.criticidad_id) === parseInt(criticidad.criticidad_id)) {
                        contieneCriticidad = true;
                        return;
                    }
                });
                var color = contieneCriticidad ? criticidad.color : "#bdbdbd";
                return {
                    "border-color": color,
                    "border-width": "1px",
                    "background-color": color,
                    "padding": "5px"
                };
            }
            return {"background-color": "#bdbdbd"};
        };

        vm.mostrarColorVentana = function (tarea) {
            if (tarea.criticidad_id) {
                var color = tarea.criticidad_id.length === 1 ? tarea.criticidad_id[0].color : "#bdbdbd";
                return {
                    "border-color": color,
                    "border-width": "2px",
                    "width": "60%",
                    "border-style": "solid"
                };
            }
            return {"background-color": color};
        };

        vm.seleccionarPlan = function () {
            var planId = $("#plan").val();
            var tam = vm.planes.length;
            var seleccionado = false;
            for (var i = 0; i < tam; i++) {
                if (parseInt(vm.planes[i].plan_id) === parseInt(planId)) {
                    vm.planId = planId;
                    localStorage.setItem("planActivo", JSON.stringify(vm.planes[i]));
                    localStorage.setItem("existePlan", true);
                    toastr.success("Plan - " + vm.planes[i].nombre + " seleccionado", "Exito !!!");
                    seleccionado = true;
                    $scope.existePlanSeleccionado = true;
                    var elemento = $(".oculto a.hidden");
                    elemento.attr('class', 'visible');
                    $location.path('/view-plan');
                }
            }
            if (!seleccionado) {
                toastr.error("No se ha seleccionado ningún plan", "Error !!!");
            }
        };

        vm.mostrarMasDetalles = function (idTarea) {
            Metronic.blockUI({target: '#form-diagrama .portlet-body', animate: true});
            $('#modal-tarea-detalle').modal('toggle');
            $('.modal-backdrop').remove();
            $timeout(function () {
                planService.buscarTarea(idTarea).then(success).catch(failed);
            }, 0);

            function success(response) {
                Metronic.unblockUI('#form-diagrama .portlet-body');
                $scope.tarea = response.data.tarea;
                $scope.antecesoras = response.data.antecesoras;
                $scope.sucesoras = response.data.sucesoras;
                $scope.tituloModal = response.data.tarea.nombre;
                $('#modal-tarea-detalle').modal({
                    show: true
                });
                $('#table-modelos-tarea .download').data("title", 'Descargar modelo').tooltip();
                $('#table-modelos-tarea .view').data("title", 'Ver Detalles de la Tarea').tooltip();
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        };

        vm.cerrarModal = function () {
            $('.modal-backdrop').remove();
        };

        function nuevoModelo() {
            TableEditableTareas.btnClickNuevoModelo();
        }

        function salvarModelo() {
            TableEditableTareas.btnClickSalvarModelo();
        }

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                // initialize core components
                Metronic.initAjax();
                MyApp.init();

                $timeout(function () {
                    var idNivel = 5;
                    if ($scope.existePlanEjecucion) {
                        $scope.plan = JSON.parse(localStorage.getItem("planActivo"));
                        console.log($scope.plan);
                        if ($scope.plan.nivelAlerta !== null) {
                            var idNivel = $scope.plan.nivelAlerta.criticidad_id;
                            localStorage.setItem("estadoPlan", idNivel);
                        }
                        $scope.titulo = $scope.plan.nombre;
                        $("#imagen-estado").prop("src", "bundles/admin/img/" + idNivel + ".png");
                    }
                    if (vm.usuario.rol === 1 || $scope.existePlanEjecucion) {
                        iniciarKaban(vm.tablero);
                    }
                }, 1000);
            });
        }

        function buscarItem(itemId) {
            var tam = vm.tablero.datos.length;
            for (var i = 0; i < tam; i++) {
                if (parseInt(vm.tablero.datos[i].id) === parseInt(itemId))
                    return vm.tablero.datos[i];
            }
            return null;
        }

        vm.buscarTareasPlan = function (equipo) {
            $scope.deEquipo = equipo;
            tableroService.buscarTareasMias(equipo).then(function (resp) {
                $timeout(function () {
                    Metronic.unblockUI('#lista-tarea .portlet-body');
                    if (resp.data.board.datos.length === 0) {
                        toastr.error("No se encontraron tareas ", "Error !!!", {"positionClass": "toast-top-center"});
                    } else {
                        $('#kanban').jqxKanban('destroy');
                        var contenedor = angular.element("#kanbanBox1");
                        contenedor.append(angular.element('<div id="kanban" style="padding: 0 10px 0 10px;"></div>'));
                        vm.tablero = resp.data.board;
                        iniciarKaban(vm.tablero);
                    }
                }, 1000);
            }).catch(function () {

            });
        };

        function iniciarKaban(board) {
            var fields = [
                {name: "id", type: "number"},
                {name: "status", map: "state", type: "string"},
                {name: "text", map: "label", type: "string"},
                {name: "tags", type: "string"},
                {name: "position", type: "string"},
                {name: "color", map: "hex", type: "string"},
                {name: "resourceId", type: "number"}
            ];
            var source = {
                localData: board.datos,
                dataType: "array",
                dataFields: fields
            };
            var dataAdapter = new $.jqx.dataAdapter(source);
            var resourcesAdapterFunc = function () {
                var resourcesSource = {
                    localData: board.recursos,
                    dataType: "array",
                    dataFields: [
                        {name: "id", type: "number"},
                        {name: "name", type: "string"},
                        {name: "image", type: "string"},
                        {name: "common", type: "boolean"},
                        {name: "fecha", type: "string"},
                        {name: "nombre", type: "string"},
                        {name: "fechaActual", type: "string"},
                        {name: "usuario", type: "string"},
                        {name: "nombre", type: "string"}
                    ]
                };
                var resourcesDataAdapter = new $.jqx.dataAdapter(resourcesSource);
                return resourcesDataAdapter;
            };

            $('#kanban').jqxKanban({
                template: "<div class='jqx-kanban-item elemento-general'><div class='fondo'>"
                        + "<div style='display: none;' class='jqx-kanban-item-avatar'></div>"
                        + "<div class='jqx-icon jqx-kanban-item-template-content jqx-kanban-template-icon'></div>"
                        + "<div class='jqx-kanban-item-text'><i class='fa fa-hourglass-start usuario'></i>  </div>"
                        + "<div class='jqx-kanban-item-nombre'></div>"
                        + "<div class='jqx-kanban-item-usuario'></div>"
                        + "<div class='jqx-kanban-item-tiempo-transcurrido'></div>"
                        + "<div style='display: none;' class='jqx-kanban-item-footer'></div>"
                        + "</div></div>",
                width: '75%',
                height: '650px',
                resources: resourcesAdapterFunc(),
                source: dataAdapter,
                columns: board.columnas,
                itemRenderer: function (element, item, recurso) {
                    $(element).find(".jqx-kanban-item-nombre").html("<span style='line-height: 23px; margin-left: 5px;' class='alinear-elementos'><i class='fa fa-tasks usuario'></i><span> " + recurso.nombre + "</span></span>");
                    $(element).find(".jqx-kanban-item-usuario").html("<span style='line-height: 23px; margin-left: 5px;' class='alinear-elementos'><i class='fa fa-user usuario' style='font-size:18px'></i><span> " + recurso.usuario + "</span></span>");
                    $(element).find(".jqx-kanban-item-tiempo-transcurrido").html("<span style='line-height: 23px; margin-left: 5px;'><i class='fa fa-calendar-check-o usuario'></i> " + recurso.fechaActual + "</span>");
                    $(element).find(".jqx-kanban-template-icon").html("<button class='btn btn-icon-only subir' data-id='" + item.id + "'><i class='fa fa-eye fa-fw' data-id='" + item.id + "'></i></button>");
                    $(element).find(".fondo").css('border-left-color', item.color);
                    $(element).find(".usuario").css('color', item.color);
                    $(element).find(".fondo").css('height', 'auto');
                    $(element).find(".subir").css('background', item.color);
                    $(element).find(".subir").css('color', "white");
                },
                // render column headers.
                columnRenderer: function (element, collapsedElement, column) {
                    try {
                        var columnItems = $("#kanban").jqxKanban('getColumnItems', column.dataField).length;
                        // update header's status.
                        element.css("border-color", column.color);
                        element.find(".jqx-kanban-column-header-status").html(" (" + columnItems + "/" + column.maxItems + ")");
                        // update collapsed header's status.
                        collapsedElement.find(".jqx-kanban-column-header-status").html(" (" + columnItems + "/" + column.maxItems + ")");
                    } catch (e) {
                        console.log(e.message);
                    }
                }
            });
            $('#kanban').on('itemMoved', function (event) {
                var args = event.args;
                args.itemData.color = args.newColumn.color;
                args.itemData.text = args.newColumn.text;
                args.itemData.className = "mi-clase";
                tableroService.moverTarea(args.itemId, args.newColumn.dataField).then(success).catch(failed);

                function success(response, status, headers, config) {
                    if (response.data.success) {
                        $('#kanban').jqxKanban('updateItem', args.itemId, args.itemData);
                        toastr.success(response.data.message, "Exito !!!");
                    } else {
                        toastr.error(response.data.error, "Error !!!", {"positionClass": "toast-top-center"});
                        $('#kanban').jqxKanban('destroy');
                        var contenedor = angular.element("#kanbanBox1");
                        contenedor.append(angular.element('<div id="kanban" style="padding: 0 10px 0 10px;"></div>'));
                        iniciarKaban(vm.tablero = response.data.board);
                    }
                    if (response.data.subirArchivo) {
                        $('#modal-modelo').modal({
                            show: true
                        });
                    }
                }

                function failed(error) {
                    logger.error('Error !!' + error.data);
                }
            });
            $('#kanban').on('columnCollapsed', function (event) {
                var args = event.args;
                var column = args.column;
                column.collapsedHeaderElement.css("background", column.color);
            });
            $('button.subir').data("title", 'Ver detalles').tooltip();
            angular.element("button.subir").click(function (event) {
                var idTarea = $(this).attr("data-id");
                $timeout(function () {
                    vm.planId = vm.planes[0].plan_id;
                    tableroService.buscarTarea(idTarea, vm.planId).then(success).catch(failed);
                }, 0);

                function success(response) {
                    $('#modal-tarea-detalle').modal({
                        'show': true
                    });
                    $scope.tarea = response.data.tarea;
                    $scope.tituloModal = $scope.tarea.nombre;
                    $scope.antecesoras = response.data.antecesoras;
                    $('#table-modelos-tarea .download').data("title", 'Descargar modelo').tooltip();
                }

                function failed(error) {
                    logger.error('Error !!' + error.data);
                }
            });
        }
        ;

        vm.existePlanSeleccionado = function () {
            return $scope.existePlanSeleccionado;
        };
    }
})();
