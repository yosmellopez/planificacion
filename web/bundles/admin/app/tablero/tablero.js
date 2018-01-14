/* global Metronic, MyApp, TableEditableTareas */

(function () {
    'use strict';

    angular.module('app.tablero').controller('Tablero', Tablero);

    Tablero.$inject = ['$rootScope', '$scope', '$timeout', 'urlPath', 'tableroService', 'cargoService', 'areaService', 'gerenciaService', 'criticidadTareaService', 'usuarioService', 'planService'];

    function Tablero($rootScope, $scope, $timeout, urlPath, tableroService, cargoService, areaService, gerenciaService, criticidadTareaService, usuarioService, planService) {
        var vm = this;
        vm.urlPath = urlPath;

        vm.tablero = tableroService.tablero;
        vm.cargos = cargoService.cargos;
        vm.areas = areaService.areas;
        vm.gerencias = gerenciaService.gerencias;
        vm.criticidades = criticidadTareaService.criticidades;
        vm.planes = planService.planes;

        vm.nuevo = nuevo;
        vm.mostrarNuevo = true;
        $scope.titulo = "Tablero de Tareas";
        $scope.cargo = "";
        $scope.gerencia = "";
        $scope.tituloModal = "";
        $scope.tarea = {};
        $scope.plan = {};
        $scope.antecesoras = new Array();
        vm.usuario = usuarioService.usuario;
        $scope.direccion = "";
        $scope.nivelAlerta = "";
        $scope.mensaje = "";
        vm.eliminar = eliminar;
        vm.modalEliminar = modalEliminar;
        vm.modalEliminarSeleccion = modalEliminarSeleccion;

        vm.cerrarForm = cerrarForm;
        vm.salvarForm = salvarForm;

        vm.nuevoModelo = nuevoModelo;
        vm.salvarModelo = salvarModelo;
        vm.filtarTareasCargo = filtarTareasCargo;
        vm.seleccionarElemento = seleccionarElemento;

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

        function seleccionarElemento(config) {
            var cargoSelect = $("#cargo");
            var gerenciaSelect = $("#gerencia");
            var direccionSelect = $("#direccion");
            if (config.criticidad) {
                cargoSelect.select2("val", "");
                gerenciaSelect.select2("val", "");
                direccionSelect.select2("val", "");
                $scope.gerencia = "";
                $scope.direccion = "";
                $scope.cargo = "";
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
            }
            if (config.direccion) {
                $scope.direccion = $("#direccion").val();
                cargoSelect.select2("val", "");
                gerenciaSelect.select2("val", "");
                $scope.cargo = "";
                $scope.gerencia = "";
                areaService.getAreasGerencia($scope.direccion).then(function (resp) {
                    vm.areas = resp.data.areas;
                }).catch(function () {

                });
                cargoService.getCargosGerencia($scope.direccion).then(function (resp) {
                    vm.cargos = resp.data.cargos;
                }).catch(function () {

                });
                $scope.mensaje = "esta dirección";
            }
        }

        vm.mostrarColor = function (criticidad, tarea) {
            if (tarea.criticidad_id) {
                var contieneCriticidad = false;
                tarea.criticidad_id.forEach(function (elem) {
                    if (parseInt(elem.criticidad_id) === parseInt(criticidad.criticidad_id)) {
                        contieneCriticidad = true;
                        return;
                    }
                })
                var color = contieneCriticidad ? criticidad.color : "#bdbdbd";
                return {
                    "border-color": color,
                    "border-width": "1px",
                    "background-color": color,
                    "padding": "5px"
                };
            }
            return {"background-color": "#bdbdbd"};
        }

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
        }

        vm.seleccionarPlan = function () {
            var planId = $("#plan").val();
            var tam = vm.planes.length;
            var seleccionado = false;
            for (var i = 0; i < tam; i++) {
                if (parseInt(vm.planes[i].plan_id) === parseInt(planId)) {
                    localStorage.setItem("plan", JSON.stringify(vm.planes[i]));
                    toastr.success("Plan - " + vm.planes[i].nombre + " seleccionado", "Exito !!!");
                    seleccionado = true;
                }
            }
            if (!seleccionado) {
                toastr.error("No se ha seleccionado ningún plan", "Error !!!");
            }
        }

        function filtarTareasCargo() {
            Metronic.blockUI({target: '#lista-tarea .portlet-body', animate: true});
            var objeto = {};
            $scope.cargo = $("#cargo").val();
            $scope.gerencia = $("#gerencia").val();
            $scope.direccion = $("#direccion").val();
            if ($scope.cargo !== "") {
                objeto = {cargo: $scope.cargo};
            } else if ($scope.gerencia !== "") {
                objeto = {area: $scope.gerencia};
            } else if ($scope.direccion !== "") {
                objeto = {direccion: $scope.direccion};
            } else if ($scope.nivelAlerta !== "") {
                objeto = {criticidad: $scope.nivelAlerta};
            }
            tableroService.buscarTablero(objeto).then(function (response) {
                $timeout(function () {
                    Metronic.unblockUI('#lista-tarea .portlet-body');
                    if (response.data.board.datos.length === 0) {
                        toastr.error("No se encontraron tareas para " + $scope.mensaje + ".", "Error !!!", {"positionClass": "toast-top-center"});
                    } else {
                        $('#kanban').jqxKanban('destroy');
                        var contenedor = angular.element("#kanbanBox1");
                        contenedor.append(angular.element('<div id="kanban" style="padding: 0 10px 0 10px;"></div>'));
                        iniciarKaban(response.data.board);
                    }
                }, 1000);
            }).catch(function (error) {

            });

        }

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
                    if (vm.usuario.rol === 1) {
                        iniciarKaban(vm.tablero);
                    }
                }, 1000);
            });
        }

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
                + "<div class='jqx-kanban-item-fecha'></div>"
                + "<div class='jqx-kanban-item-cargo' style='height:40px;'></div>"
                + "<div style='display: none;' class='jqx-kanban-item-footer'></div>"
                + "</div></div>",
                width: '75%',
                height: '650px',
                resources: resourcesAdapterFunc(),
                source: dataAdapter,
                columns: board.columnas,
                itemRenderer: function (element, item, recurso) {
                    $(element).find(".jqx-kanban-item-nombre").html("<span style='line-height: 23px; margin-left: 5px;' class='alinear-elementos'><i class='fa fa-tasks usuario'></i><span> " + recurso.nombre + "</span></span>");
                    $(element).find(".jqx-kanban-item-fecha").html("<span style='line-height: 23px; margin-left: 5px;'><i class='fa fa-calendar-check-o usuario'></i> " + recurso.fecha + "</span>");
                    $(element).find(".jqx-kanban-item-cargo").html("<span style='line-height: 23px; margin-left: 5px;'><i class='fa fa-user usuario' style='font-size:18px'></i> " + recurso.name + "</span>");
                    $(element).find(".jqx-kanban-template-icon").html("<button class='btn btn-icon-only subir' data-id='" + item.id + "'><i class='fa fa-eye fa-fw' data-id='" + item.id + "'></i></button>");
                    $(element).find(".fondo").css('border-left-color', item.color);
                    $(element).find(".usuario").css('color', item.color);
                    $(element).find(".fondo").css('height', 'auto');
                    $(element).find(".subir").css('background', item.color);
                    $(element).find(".subir").css('color', "white");
                },
                // render column headers.
                columnRenderer: function (element, collapsedElement, column) {
                    var columnItems = $("#kanban").jqxKanban('getColumnItems', column.dataField).length;
                    // update header's status.
                    element.css("border-color", column.color);
                    element.find(".jqx-kanban-column-header-status").html(" (" + columnItems + "/" + column.maxItems + ")");
                    // update collapsed header's status.
                    collapsedElement.find(".jqx-kanban-column-header-status").html(" (" + columnItems + "/" + column.maxItems + ")");
                }
            });
            $('#kanban').on('itemMoved', function (event) {
                var args = event.args;
                args.itemData.color = args.newColumn.color;
                args.itemData.text = args.newColumn.text;
                args.itemData.className = "mi-clase";
                tableroService.moverTarea(args.itemId, args.newColumn.dataField);
                $('#kanban').jqxKanban('updateItem', args.itemId, args.itemData);
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
                    tableroService.buscarTarea(idTarea, vm.planes[0].plan_id).then(success).catch(failed);
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

    }
})();
