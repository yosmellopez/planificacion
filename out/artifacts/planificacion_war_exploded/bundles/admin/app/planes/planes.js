/* global go, Metronic */

(function () {
    'use strict';

    angular
        .module('app.planes')
        .controller('Planes', Planes);

    Planes.$inject = ['$rootScope', '$scope', '$timeout', 'urlPath', 'estadoPlanService', 'criticidadPlanService', 'planService',
        'criticidadTareaService', 'gerenciaService', 'estadoTareaService', 'canalService', 'logger'];

    function Planes($rootScope, $scope, $timeout, urlPath, estadoPlanService, criticidadPlanService, planService,
                    criticidadTareaService, gerenciaService, estadoTareaService, canalService, logger) {
        var vm = this;
        vm.urlPath = urlPath;

        vm.estados = estadoPlanService.estados;
        vm.estadosTareas = estadoTareaService.estados;
        vm.criticidades = criticidadPlanService.criticidades;
        vm.planes = planService.planes;
        vm.titulo = "Lista de Planes";
        vm.criticidades_tarea = criticidadTareaService.criticidades;
        vm.gerencias = gerenciaService.gerencias;
        vm.canales = canalService.canales;
        vm.tareas = planService.tareas;
        $scope.tituloDiagrama = "";
        $scope.tareasSeleccionadas = new Array();
        $scope.tareasAntecesoras = new Array();
        $scope.todasTareas = new Array();
        $scope.nombreTarea = "";
        $scope.tituloModal = "";
        $scope.tarea = {};

        vm.nuevo = nuevo;
        vm.eliminar = eliminar;
        vm.modalEliminar = modalEliminar;
        vm.modalEliminarSeleccion = modalEliminarSeleccion;

        vm.cerrarForm = cerrarForm;
        vm.salvarForm = salvarForm;
        vm.filtrar = filtrar;
        vm.addTareaPlan = addTareaPlan;
        //Importar tareas
        vm.mostrarModalImportar = mostrarModalImportar;
        vm.filtrarImportarTareas = filtrarImportarTareas;
        vm.salvarFormTarea = salvarFormTarea;
        vm.salvarArchivo = salvarArchivo;
        //Diagrama
        vm.zoomToFit = zoomToFit;
        vm.centerRoot = centerRoot;
        vm.increaseZoom = increaseZoom;
        vm.decreaseZoom = decreaseZoom;
        vm.salvarDiagrama = salvarDiagrama;
        vm.cerrarPlan = cerrarPlan;
        vm.clonarPlan = clonarPlan;
        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageBodySolid = true;
        $rootScope.settings.layout.pageSidebarClosed = false;

        activate();

        function nuevo() {
            $timeout(function () {
                TableEditablePlanes.btnClickNuevo();
            }, 0);
        }

        function eliminar() {
            TableEditablePlanes.btnClickEliminar();
        }

        function modalEliminar() {
            TableEditablePlanes.btnClickModalEliminar();
        }

        function modalEliminarSeleccion() {
            TableEditablePlanes.btnClickModalEliminarSeleccion();
        }

        function addTareaPlan() {
            TableEditablePlanes.btnClickImportarTarea();
        }

        function cerrarForm() {
            $timeout(function () {
                TableEditablePlanes.btnClickCerrarForm();
            }, 0);

        }

        function clonarPlan() {
            $timeout(function () {
                TableEditablePlanes.btnClickClonarPlan();
            }, 0);

        }

        function cerrarPlan() {
            $timeout(function () {
                TableEditablePlanes.btnClickCerrarPlan();
            }, 0);
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

        function salvarForm() {
            TableEditablePlanes.btnClickSalvarForm();
        }

        function filtrar() {
            TableEditablePlanes.btnClickFiltrar();
        }

        function mostrarModalImportar() {
            TableEditablePlanes.btnClickMostrarModalImportar();
        }

        function filtrarImportarTareas() {
            TableEditablePlanes.btnClickFiltrarImportarTareas();
        }

        function salvarFormTarea() {
            $timeout(function () {
                TableEditablePlanes.btnClickSalvarFormTarea();
            }, 0);
        }

        function salvarArchivo() {
            $timeout(function () {
                TableEditablePlanes.btnClickSalvarArchivo();
            }, 0);
        }

        function zoomToFit() {
            $timeout(function () {
                TableEditablePlanes.btnClickZoomToFit();
            }, 0);
        }

        function centerRoot() {
            $timeout(function () {
                TableEditablePlanes.btnClickCenterRoot();
            }, 0);
        }

        function increaseZoom() {
            $timeout(function () {
                TableEditablePlanes.btnClickIncreaseZoom();
            }, 0);
        }

        function decreaseZoom() {
            $timeout(function () {
                TableEditablePlanes.btnClickDecreaseZoom();
            }, 0);
        }

        vm.borrarDiagrama = function () {
            $timeout(function () {
                TableEditablePlanes.btnClickBorrarDiagrama();
            }, 0);
        }

        function salvarDiagrama() {
            $timeout(function () {
                TableEditablePlanes.btnClickSalvarDiagrama();
            }, 0);
        }

        vm.agregarElemento = function (todos) {
            var checkboxes = $("#cuerpo-tareas-seleccionar input[type=checkbox]");
            var tam = checkboxes.length;
            for (var i = 0; i < tam; i++) {
                if (checkboxes[i].checked) {
                    var elem = buscarElemento(checkboxes[i].id, false);
                    if (elem !== null) {
                        $scope.tareasSeleccionadas.push(elem.item);
                        $(checkboxes[i]).parent().parent().parent().remove();
                    }
                }
            }
        }

        function buscarElemento(id, seleccionados) {
            if (!seleccionados) {
                var tam = $scope.todasTareas.length;
                for (var i = 0; i < tam; i++) {
                    if (parseInt($scope.todasTareas[i].tarea_id) === parseInt(id)) {
                        return {posicion: i, item: $scope.todasTareas[i]};
                    }
                }
                return null;
            } else {
                var tam = $scope.tareasSeleccionadas.length;
                for (var i = 0; i < tam; i++) {
                    if (parseInt($scope.tareasSeleccionadas[i].tarea_id) === parseInt(id)) {
                        return {posicion: i, item: $scope.tareasSeleccionadas[i]};
                    }
                }
                return null;
            }
        }

        vm.eliminarElemento = function (todos) {
            var checkboxes = $("#table-relacionados-cuerpo input[type=checkbox]");
            var tam = checkboxes.length;
            for (var i = 0; i < tam; i++) {
                if (checkboxes[i].checked) {
                    var elem = buscarElemento(checkboxes[i].id, true);
                    if (elem !== null) {
                        $scope.todasTareas.push(elem.item);
                        $scope.tareasSeleccionadas.splice(elem.posicion, 1);
                        $(checkboxes[i]).parent().parent().parent().remove();
                    }
                }
            }
        }

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                // initialize core components
                Metronic.initAjax();
                MyApp.init();

                $timeout(function () {
                    TableEditablePlanes.init(vm.urlPath, planService, $scope, logger);
                }, 1000);
            });
        }

    }

    angular.module('app.planes').controller('PlanesList', PlanesList);

    PlanesList.$inject = ['$rootScope', '$scope', '$timeout', 'planService', 'logger', 'usuarioService', 'cargoService', 'areaService', 'gerenciaService', 'criticidadTareaService'];

    function PlanesList($rootScope, $scope, $timeout, planService, logger, usuarioService, cargoService, areaService, gerenciaService, criticidadTareaService) {
        var vm = this;
        $rootScope.settings.layout.pageBodySolid = true;
        $rootScope.settings.layout.pageSidebarClosed = false;
        vm.cargos = cargoService.cargos;
        vm.areas = areaService.areas;
        vm.gerencias = gerenciaService.gerencias;
        vm.criticidades = criticidadTareaService.criticidades;
        vm.planes = planService.planes;
        vm.filtarTareasCargo = filtarTareasCargo;
        vm.seleccionarElemento = seleccionarElemento;
        $scope.titulo = vm.planes.length === 1 ? vm.planes[0].nombre : "Tareas de Planes";
        $scope.idPlan = 0;
        $scope.tituloModal = "";
        $scope.tituloDiagrama = "";
        $scope.tarea = {};
        var tabla = null;
        $scope.cargo = "";
        $scope.antecesoras = new Array();
        $scope.gerencia = "";
        $scope.direccion = "";
        $scope.criticidad = "";
        vm.usuario = usuarioService.usuario;
        $scope.mostrarTabla = false;
        $scope.mostrarInicial = vm.planes.length === 1;
        $scope.planes = new Array();
        activate();

        vm.mostrarDetalles = function () {
            $scope.planes = new Array();
            var cant = vm.planes.length;
            $scope.idPlan = $("#plan").val();
            if ($scope.mostrarInicial) {
                $scope.idPlan = vm.planes[0].plan_id;
            }
            for (var i = 0; i < cant; i++) {
                if (parseInt(vm.planes[i].plan_id) === parseInt($scope.idPlan)) {
                    $scope.planes.push(vm.planes[i]);
                    break;
                }
            }
        };

        vm.mostrarDetallesTarea = function (idTarea) {
            var tareas = $scope.planes[0].tareas;
            var cant = tareas.length;
            var tieneTarea = false;
            for (var i = 0; i < cant; i++) {
                if (parseInt(tareas[i].tarea_id) === parseInt(idTarea)) {
                    $scope.tarea = tareas[i];
                    $scope.tituloModal = tareas[i].nombre;
                    tieneTarea = true;
                    break;
                }
            }
            if (tieneTarea) {
                $timeout(function () {
                    planService.buscarTarea(idTarea, $scope.planes[0].plan_id).then(success).catch(failed);
                }, 0);
            }

            function success(response) {
                $('#modal-tarea-detalle').modal({
                    'show': true
                });
                $scope.tarea = response.data.tarea;
                $scope.antecesoras = response.data.antecesoras;
                $('#table-modelos-tarea .download').data("title", 'Descargar modelo').tooltip();
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
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

        vm.claseMia = function (esMia) {
            return esMia ? "mi-tarea" : "no-es-mia";
        };

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

        function filtarTareasCargo() {
            Metronic.blockUI({target: '#lista-tarea .portlet-body', animate: true});
            $scope.cargo = $("#cargo").val();
            $scope.gerencia = $("#gerencia").val();
            $scope.direccion = $("#direccion").val();
            $scope.criticidad = $("#nivel-alerta").val();
            tabla.getDataTable().ajax.url("plan/buscarTareasPlan?plan_id=" + $scope.planes[0].plan_id + "&cargo=" + $scope.cargo + "&area=" + $scope.gerencia + "&direccion=" + $scope.direccion + "&criticidad=" + $scope.criticidad);
            tabla.getDataTable().ajax.reload();
        }

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                Metronic.initAjax();
                MyApp.init();
                if ($scope.mostrarInicial) {
                    vm.mostrarDetalles();
                    crearTabla();
                } else {
                    var plan = JSON.parse(localStorage.getItem("plan"));
                    if (plan && vm.usuario.rol !== 1) {
                        vm.planes.push(plan);
                        $("#plan").select2().val(plan);
                        vm.mostrarDetalles();
                        crearTabla();
                    }
                }
            });
        }

        function crearTabla() {
            tabla = new Datatable();
            tabla.init({
                src: $('#tarea-table-editable'),
                onSuccess: function (grid) {
                    // execute some code after table records loaded
                },
                onError: function (grid) {
                    // execute some code on network or other general error
                },
                loadingMessage: 'Por favor espere...',
                dataTable: {
                    "serverSide": false,
                    "lengthMenu": [
                        [15, 25, 30, 50, -1],
                        [15, 25, 30, 50, "Todos"]
                    ],
                    "pageLength": 15, // default record count per page
                    "ajax": {
                        "url": "plan/buscarTareasPlan?plan_id=" + $scope.planes[0].plan_id + "&cargo=" + $scope.cargo + "&area=" + $scope.gerencia + "&direccion=" + $scope.direccion + "&criticidad=" + $scope.criticidad, // ajax source
                    },
                    "order": [[1, "asc"]],
                    responsive: {
                        details: {}
                    },
                    "aoColumns": [
                        {"bSortable": false, "sWidth": '2%', "sClass": 'text-center'},
                        {"bSortable": true, "sWidth": '30%'},
                        {"bSortable": true, "sWidth": '30%'},
                        {"bSortable": true, "sWidth": '30%'},
                        {"bSortable": false, "sWidth": '10%', "sClass": 'text-center'}
                    ]
                }
            });
            $(document).on('click', "#tarea-table-editable a.view", function (e) {
                e.preventDefault();
                vm.mostrarDetallesTarea($(this).data('id'));
            });
        }
    }

    angular.module('app.planes').controller('PlanesDiagrama', PlanesDiagrama);

    PlanesDiagrama.$inject = ['$rootScope', '$scope', '$timeout', 'planService', 'criticidadTareaService', 'logger'];

    function PlanesDiagrama($rootScope, $scope, $timeout, planService, criticidadTareaService, logger) {
        var vm = this;
        $rootScope.settings.layout.pageBodySolid = true;
        $rootScope.settings.layout.pageSidebarClosed = false;
        vm.planes = planService.planes;
        vm.criticidades = criticidadTareaService.criticidades;
        $scope.planId = 0;
        $scope.tituloModal = "";
        $scope.tituloDiagrama = "";
        $scope.titulo = vm.planes.length === 1 ? vm.planes[0].nombre : "Diagramas de Planes";
        $scope.tarea = {};
        $scope.mostrarTabla = false;
        $scope.mostrarInicial = vm.planes.length === 1;
        $scope.planes = new Array();
        $scope.antecesoras = new Array();
        var myDiagram = null;
        var myPalette = null;
        var start = 0;
        activate();

        vm.mostrarDetalles = function () {
            $scope.planes = new Array();
            var cant = vm.planes.length;
            $scope.planId = $("#plan").val();
            for (var i = 0; i < cant; i++) {
                if (parseInt(vm.planes[i].plan_id) === parseInt($scope.planId)) {
                    $scope.planes.push(vm.planes[i]);
                    vm.destroyDiagrama();
                    inicializarPlan(vm.planes[i]);
                    break;
                }
            }
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

        vm.claseMia = function (esMia) {
            return esMia ? "mi-tarea" : "no-es-mia";
        };

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                Metronic.initAjax();
                MyApp.init();
                if ($scope.mostrarInicial) {
                    $scope.planes[0] = vm.planes[0];
                    inicializarPlan(vm.planes[0]);
                } else {
                    var plan = JSON.parse(localStorage.getItem("plan"));
                    if (plan) {
                        vm.planes.push(plan);
                        $scope.planes[0] = vm.planes[0];
                        inicializarPlan(vm.planes[0]);
                    }
                }
            });
        }

        function inicializarPlan(plan) {
            Metronic.blockUI({
                target: '#lista-plan',
                animate: true
            });
            planService.buscarPlan(plan.plan_id).then(function (response) {
                mostrarDiagrama(response.data.plan);
                Metronic.unblockUI('#lista-plan');
            }).catch(function (error) {
                logger.error('Error !!' + error.data);
                Metronic.unblockUI('#lista-plan');
            });
        }

        vm.mostrarDetallesTarea = function (idTarea) {
            var tareas = $scope.planes[0].tareas;
            var cant = tareas.length;
            var tieneTarea = false;
            for (var i = 0; i < cant; i++) {
                if (parseInt(tareas[i].tarea_id) === parseInt(idTarea)) {
                    $scope.tarea = tareas[i];
                    $scope.tituloModal = tareas[i].nombre;
                    tieneTarea = true;
                    break;
                }
            }
            if (tieneTarea) {
                $timeout(function () {
                    planService.buscarTarea(idTarea, $scope.planes[0].plan_id).then(success).catch(failed);
                }, 0);
            }

            function success(response) {
                $('#modal-tarea-detalle').modal({
                    'show': true
                });
                $scope.tarea = response.data.tarea;
                $scope.antecesoras = response.data.antecesoras;
                $('#table-modelos-tarea .download').data("title", 'Descargar modelo').tooltip();
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        };

        function mostrarDiagrama(plan) {
            // define a custom ResizingTool to limit how far one can shrink a row or column
            function LaneResizingTool() {
                go.ResizingTool.call(this);
            }

            go.Diagram.inherit(LaneResizingTool, go.ResizingTool);
            /** @override */
            LaneResizingTool.prototype.computeMinSize = function () {
                var diagram = this.diagram;
                var lane = this.adornedObject.part;  // might be row or column
                var horiz = (lane.category === "Column Header");  // or "Row Header"
                var margin = diagram.nodeTemplate.margin;
                var bounds = new go.Rect();
                diagram.findTopLevelGroups().each(function (g) {
                    if (horiz ? (g.column === lane.column) : (g.row === lane.row)) {
                        var b = diagram.computePartsBounds(g.memberParts);
                        if (b.isEmpty())
                            return;  // nothing in there?  ignore it
                        b.unionPoint(g.location);  // keep any empty space on the left and top
                        b.addMargin(margin);  // assume the same node margin applies to all nodes
                        if (bounds.isEmpty()) {
                            bounds = b;
                        } else {
                            bounds.unionRect(b);
                        }
                    }
                });
                // limit the result by the standard value of computeMinSize
                var msz = go.ResizingTool.prototype.computeMinSize.call(this);
                if (bounds.isEmpty())
                    return msz;
                return new go.Size(Math.max(msz.width, bounds.width), Math.max(msz.height, bounds.height));
            };
            /** @override */
            LaneResizingTool.prototype.resize = function (newr) {
                var lane = this.adornedObject.part;
                var horiz = (lane.category === "Column Header");
                var vertical = (lane.category === "Row Sider");
                var lay = this.diagram.layout;  // the TableLayout
                if (horiz) {
                    var col = lane.column;
                    var coldef = lay.getColumnDefinition(col);
                    coldef.width = newr.width;
                } else {
                    if (vertical) {
                        var row = lane.row;
                        var rowdef = lay.getRowDefinition(row);
                        rowdef.height = newr.height;
                    } else {
                        var shape = lane.resizeObject;
                        if (shape !== null) {  // set its desiredSize length, but leave each breadth alone
                            shape.width = newr.width;
                            shape.height = newr.height;
                        }
                    }
                }

                lay.invalidateLayout();
            };
            // end LaneResizingTool class
            var $ = go.GraphObject.make;
            var nodeSelectionAdornmentTemplate = $(go.Adornment, "Auto", $(go.Shape, {
                fill: null,
                stroke: "deepskyblue",
                strokeWidth: 1.5,
                strokeDashArray: [4, 2]
            }), $(go.Placeholder));

            var nodeResizeAdornmentTemplate = $(go.Adornment, "Spot", {locationSpot: go.Spot.Right},
                $(go.Placeholder),
                $(go.Shape, {
                    alignment: go.Spot.TopLeft,
                    cursor: "nw-resize",
                    desiredSize: new go.Size(6, 6),
                    fill: "lightblue",
                    stroke: "deepskyblue"
                }),
                $(go.Shape, {
                    alignment: go.Spot.Top,
                    cursor: "n-resize",
                    desiredSize: new go.Size(6, 6),
                    fill: "lightblue",
                    stroke: "deepskyblue"
                }),
                $(go.Shape, {
                    alignment: go.Spot.TopRight,
                    cursor: "ne-resize",
                    desiredSize: new go.Size(6, 6),
                    fill: "lightblue",
                    stroke: "deepskyblue"
                }),
                $(go.Shape, {
                    alignment: go.Spot.Left,
                    cursor: "w-resize",
                    desiredSize: new go.Size(6, 6),
                    fill: "lightblue",
                    stroke: "deepskyblue"
                }),
                $(go.Shape, {
                    alignment: go.Spot.Right,
                    cursor: "e-resize",
                    desiredSize: new go.Size(6, 6),
                    fill: "lightblue",
                    stroke: "deepskyblue"
                }),
                $(go.Shape, {
                    alignment: go.Spot.BottomLeft,
                    cursor: "se-resize",
                    desiredSize: new go.Size(6, 6),
                    fill: "lightblue",
                    stroke: "deepskyblue"
                }),
                $(go.Shape, {
                    alignment: go.Spot.Bottom,
                    cursor: "s-resize",
                    desiredSize: new go.Size(6, 6),
                    fill: "lightblue",
                    stroke: "deepskyblue"
                }),
                $(go.Shape, {
                    alignment: go.Spot.BottomRight,
                    cursor: "sw-resize",
                    desiredSize: new go.Size(6, 6),
                    fill: "lightblue",
                    stroke: "deepskyblue"
                }));

            myDiagram = $(go.Diagram, "myDiagramDiv", {
                "toolManager.mouseWheelBehavior": go.ToolManager.WheelZoom,
                initialContentAlignment: go.Spot.Center,
                layout: $(TableLayout, $(go.RowColumnDefinition, {
                    row: 1,
                    height: 22
                }), $(go.RowColumnDefinition, {column: 1, width: 22})),
                "SelectionMoved": function (e) {
                    e.diagram.layoutDiagram(true);
                },
                "ExternalObjectsDropped": function (e) {
                    pintarBorde(plan);
                    myPalette.remove(myPalette.selection.first());
                },
                "resizingTool": new LaneResizingTool(),
                "linkingTool.isEnabled": false,
                allowDrop: true,
                // feedback that dropping in the background is not allowed
                mouseDragOver: function (e) {
                    e.diagram.currentCursor = "not-allowed";
                },
                // when dropped in the background, not on a Node or a Group, cancel the drop
                mouseDrop: function (e) {
                    e.diagram.currentTool.doCancel();
                },
                "animationManager.isInitial": false,
                "undoManager.isEnabled": true
            });

            myDiagram.nodeTemplateMap.add("Column Header", $(go.Part, "Spot", {
                    row: 1, rowSpan: 9999, column: 2,
                    minSize: new go.Size(500, NaN),
                    stretch: go.GraphObject.Fill,
                    movable: false,
                    resizable: true,
                    resizeAdornmentTemplate: $(go.Adornment, "Spot", $(go.Placeholder), $(go.Shape, {
                        alignment: go.Spot.Right,
                        desiredSize: new go.Size(7, 50),
                        fill: "lightblue",
                        stroke: "dodgerblue",
                        cursor: "col-resize"
                    }))
                }, new go.Binding("column", "col"),
                $(go.Shape, {fill: null}, new go.Binding("fill", "color")),
                $(go.Panel, "Auto", {// this is positioned above the Shape, in row 1
                        alignment: go.Spot.Top, alignmentFocus: go.Spot.Bottom,
                        stretch: go.GraphObject.Horizontal,
                        height: myDiagram.layout.getRowDefinition(1).height
                    },
                    $(go.Shape, {fill: null, strokeWidth: 0}, new go.Binding("fill", "color_header")),
                    $(go.TextBlock, {
                        font: "bold 10pt sans-serif",
                        isMultiline: false,
                        wrap: go.TextBlock.None,
                        overflow: go.TextBlock.OverflowEllipsis
                    }, new go.Binding("text")))));


            myDiagram.nodeTemplateMap.add("Row Sider", $(go.Part, "Spot", {
                row: 2, column: 1, columnSpan: 9999,
                minSize: new go.Size(NaN, 200),
                stretch: go.GraphObject.Fill,
                movable: false,
                resizable: true,
                resizeAdornmentTemplate: $(go.Adornment, "Spot", $(go.Placeholder), $(go.Shape, {
                    alignment: go.Spot.Bottom,
                    desiredSize: new go.Size(50, 7),
                    fill: "lightblue",
                    stroke: "dodgerblue",
                    cursor: "row-resize"
                }))
            }, new go.Binding("row"), $(go.Shape, {fill: null}, new go.Binding("fill", "color")), $(go.Panel, "Auto", {
                alignment: go.Spot.Left, alignmentFocus: go.Spot.Right,
                stretch: go.GraphObject.Vertical, angle: 270,
                height: myDiagram.layout.getColumnDefinition(1).width
            }, $(go.Shape, {fill: "#F3F3F3", strokeWidth: 0}), $(go.TextBlock, {
                font: "bold 10pt sans-serif",
                isMultiline: true,
                wrap: go.TextBlock.WrapFit,
                overflow: go.TextBlock.OverflowEllipsis
            }, new go.Binding("text")))));

            myDiagram.nodeTemplateMap.add("Start", $(go.Node, "Spot", {
                    margin: new go.Margin(10, 10, 10, 10),

                    doubleClick: function (e, node) {
                        vm.mostrarDetallesTarea(node.data.tarea_id);
                    }
                }, new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify), new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify),
                {selectable: true, selectionAdornmentTemplate: nodeSelectionAdornmentTemplate, movable: false},
                {resizable: true, resizeObjectName: "PANEL", resizeAdornmentTemplate: nodeResizeAdornmentTemplate},
                new go.Binding("row"),
                new go.Binding("column", "col"),
                $(go.Panel, "Auto", $(go.Shape, "DividedEvent", {
                        fill: "white",
                        strokeWidth: 1,
                        height: 110,
                        stroke: "transparent",
                        portId: "",
                        fromLinkable: true,
                        toLinkable: true,
                        margin: new go.Margin(10, 0, 10, 0)
                    }, new go.Binding("fill", "color"), new go.Binding("stroke", "color_borde")),
                    $(go.Panel, "Table", {
                            margin: new go.Margin(0, 0, 10, 0),
                            defaultAlignment: go.Spot.Left
                        },
                        $(go.RowColumnDefinition, {column: 0, width: 50}),
                        $(go.RowColumnDefinition, {column: 1, width: 50}),
                        $(go.RowColumnDefinition, {column: 2, width: 200}),
                        $(go.TextBlock, {
                            font: "10pt  Segoe UI,sans-serif",
                            stroke: "#FFF"
                        }, {
                            row: 0, column: 2,
                            textAlign: "right",
                            margin: new go.Margin(0, 5, 0, 5)
                        }, new go.Binding("text", "cargo").makeTwoWay()),
                        $(go.TextBlock, {
                            font: "11pt  Segoe UI,sans-serif",
                            stroke: "#FFF"
                        }, {
                            row: 1, column: 0, columnSpan: 3,
                            textAlign: "right",
                            margin: new go.Margin(10, 5, 0, 5)
                        }, new go.Binding("text", "nombre").makeTwoWay()),
                        $(go.Shape, "LineH", {name: "LINEA", fill: "white", stroke: "#000"}, {
                            row: 2, column: 0, columnSpan: 3,
                            margin: new go.Margin(0, 0, 0, 0),
                            maxSize: new go.Size(300, 10),
                            minSize: new go.Size(300, 10)
                        }),
                        $(go.TextBlock, {
                            font: "10pt  Segoe UI,sans-serif",
                            stroke: "#FFF"
                        }, {
                            row: 3, column: 1, columnSpan: 3,
                            textAlign: "center",
                            font: "bold 9pt sans-serif",
                            margin: new go.Margin(0, 0, 0, 0)
                        }, new go.Binding("text", "producto").makeTwoWay())))));

            myDiagram.nodeTemplateMap.add("Hito", $(go.Node, "Spot", {
                    margin: new go.Margin(10, 10, 10, 10),
                    click: function (e, node) {
                        showConnections(node);
                    },
                    doubleClick: function (e, node) {
                        vm.mostrarDetallesTarea(node.data.tarea_id);
                    }
                }, new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify), new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify),
                {selectable: true, selectionAdornmentTemplate: nodeSelectionAdornmentTemplate, movable: false},
                {resizable: true, resizeObjectName: "PANEL", resizeAdornmentTemplate: nodeResizeAdornmentTemplate},
                new go.Binding("row"),
                new go.Binding("column", "col"),
                $(go.Panel, "Auto", $(go.Shape, "Parallelogram2", {
                        fill: "white",
                        strokeWidth: 1,
                        stroke: "transparent",
                        portId: "",
                        minSize: new go.Size(250, 70),
                        fromLinkable: true,
                        toLinkable: true
                    }, new go.Binding("fill", "color"), new go.Binding("stroke", "color_borde")),
                    $(go.Panel, "Table", {
                            margin: new go.Margin(0, 0, 10, 0),
                            defaultAlignment: go.Spot.Left
                        },
                        $(go.RowColumnDefinition, {column: 0, width: 200}),
                        $(go.TextBlock, {font: "10pt bold Segoe UI,sans-serif", stroke: "#000"}, {
                            row: 0,
                            column: 0,
                            columnSpan: 4,
                            textAlign: "center",
                            margin: new go.Margin(5, 5, 0, 15)
                        }, new go.Binding("text", "nombre").makeTwoWay())))));

            myDiagram.nodeTemplateMap.add("Grupo", $(go.Node, "Spot", {
                    padding: 16,
                    selectionAdornmentTemplate: // adornment when a group is selected
                        $(go.Adornment, "Auto", $(go.Shape, "RoundedRectangle",
                            {fill: null, stroke: "dodgerblue", strokeWidth: 3}),
                            $(go.Placeholder)
                        ),
                    toSpot: go.Spot.AllSides, // links coming into groups at any side
                }, $(go.Panel, "Auto"), $(go.TextBlock, {
                    name: "GROUPTEXT",
                    alignment: go.Spot.TopCenter,
                    alignmentFocus: new go.Spot(0, 0, 4, 4),
                    font: "Bold 10pt Sans-Serif"
                },
                new go.Binding("text", "nombre")), {
                    toolTip: $(go.Adornment, "Auto", $(go.Shape, {fill: "#EFEFCC"}), $(go.TextBlock, {margin: 4}, new go.Binding("text", "nombre")))
                }
            ));

            myDiagram.nodeTemplate = $(go.Node, "Auto", {
                    margin: new go.Margin(10, 10, 10, 10),
                    click: function (e, node) {
                        showConnections(node);
                    },
                    doubleClick: function (e, node) {
                        vm.mostrarDetallesTarea(node.data.tarea_id);
                    }
                }, new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify), new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify),
                {selectable: true, selectionAdornmentTemplate: nodeSelectionAdornmentTemplate, movable: false},
                {resizable: true, resizeObjectName: "PANEL", resizeAdornmentTemplate: nodeResizeAdornmentTemplate},
                new go.Binding("row"), new go.Binding("column", "col"), $(go.Shape, "Rectangle", {
                    fill: "white",
                    strokeWidth: 1,
                    stroke: "transparent",
                    portId: "",
                    fromLinkable: true,
                    toLinkable: true
                }, new go.Binding("fill", "color").makeTwoWay(), new go.Binding("stroke", "color_borde").makeTwoWay()),
                $(go.Panel, "Table", {
                        margin: new go.Margin(0, 0, 10, 0),
                        defaultAlignment: go.Spot.Left
                    },
                    $(go.RowColumnDefinition, {column: 0, width: 50}),
                    $(go.RowColumnDefinition, {column: 1, width: 50}),
                    $(go.RowColumnDefinition, {column: 2, width: 200}),
                    $(go.TextBlock, {font: "9pt  Segoe UI,sans-serif", stroke: "#000"}, {
                        row: 0,
                        column: 2,
                        textAlign: "right",
                        font: "bold 9pt Segoe UI,sans-serif",
                        margin: new go.Margin(5, 0, 10, 0)
                    }, new go.Binding("text", "cargo").makeTwoWay()),
                    $(go.TextBlock, {font: "10pt  Segoe UI,sans-serif", stroke: "#000"}, {
                        row: 1,
                        column: 0,
                        columnSpan: 3,
                        textAlign: "center",
                        margin: new go.Margin(0, 5, 0, 5)
                    }, new go.Binding("text", "nombre").makeTwoWay()),
                    $(go.Shape, "LineH", {name: "LINEA", fill: "white", stroke: "#000"}, {
                        row: 2,
                        column: 0,
                        columnSpan: 3,
                        margin: new go.Margin(0, 0, 0, 0),
                        maxSize: new go.Size(300, 10),
                        minSize: new go.Size(300, 10)
                    }),
                    $(go.TextBlock, {font: "9pt  Segoe UI,sans-serif", stroke: "#000"}, {
                        row: 3,
                        column: 1,
                        columnSpan: 2,
                        textAlign: "center",
                        font: "bold 9pt sans-serif",
                        margin: new go.Margin(0, 0, 0, 0)
                    }, new go.Binding("text", "producto").makeTwoWay()))); // end Node

            myDiagram.groupTemplate = $(go.Group, "Auto", {
                    layerName: "Background",
                    stretch: go.GraphObject.Fill,
                    selectable: false,
                    computesBoundsAfterDrag: true,
                    computesBoundsIncludingLocation: true,
                    handlesDragDropForMembers: true, // don't need to define handlers on member Nodes and Links
                    mouseDragEnter: function (e, group, prev) {
                        group.isHighlighted = true;
                    },
                    mouseDragLeave: function (e, group, next) {
                        group.isHighlighted = false;
                    },
                    mouseDrop: function (e, group) {
                        // if any dropped part wasn't already a member of this group, we'll want to let the group's row
                        // column allow themselves to be resized automatically, in case the row height or column width
                        // had been set manually by the LaneResizingTool
                        var anynew = e.diagram.selection.any(function (p) {
                            return p.containingGroup !== group;
                        });
                        // Don't allow headers/siders to be dropped
                        var anyHeadersSiders = e.diagram.selection.any(function (p) {
                            return p.category === "Column Header" || p.category === "Row Sider";
                        });
                        if (!anyHeadersSiders && group.addMembers(e.diagram.selection, true)) {
                            if (anynew) {
                                e.diagram.layout.getRowDefinition(group.row).height = NaN;
                                e.diagram.layout.getColumnDefinition(group.column).width = NaN;
                            }
                        } else {  // failure upon trying to add parts to this group
                            e.diagram.currentTool.doCancel();
                        }
                    }
                }, new go.Binding("row"), new go.Binding("column", "col"), new go.Binding('location', 'loc', go.Point.parse),
                // the group is normally unseen -- it is completely transparent except when given a color or when highlighted
                $(go.Shape, {
                    fill: "transparent", stroke: "transparent",
                    strokeWidth: myDiagram.nodeTemplate.margin.left,
                    stretch: go.GraphObject.Fill
                }, new go.Binding("fill", "color"), new go.Binding("stroke", "isHighlighted", function (h) {
                    return h ? "red" : "transparent";
                }).ofObject()),
                $(go.Placeholder, {// leave a margin around the member nodes of the group which is the same as the member node margin
                    alignment: (function (m) {
                        return new go.Spot(0, 0, m.top, m.left);
                    })(myDiagram.nodeTemplate.margin),
                    padding: (function (m) {
                        return new go.Margin(0, m.right, m.bottom, 0);
                    })(myDiagram.nodeTemplate.margin)
                })
            );

            //Datos del modelo
            var model = [];
            var columnas = plan.columnas;
            var siders = plan.siders;
            var colores = plan.colores;
            //Header columnas
            var col = 2;
            for (var i = 0; i < columnas.length; i++) {
                model.push({
                    key: "ch" + columnas[i].identificador,
                    text: columnas[i].nombre,
                    col: col,
                    category: "Column Header",
                    color_header: colores[i]
                });
                col++;
            }
            //Rows sider
            var row = 2;
            for (var i = 0; i < siders.length; i++) {
                model.push({
                    key: "sd" + siders[i].idSider,
                    text: siders[i].nombre,
                    row: row,
                    category: "Row Sider"
                });
                row++;
            }
            //Celdas
            var celdas = new Array();
            for (var i = 0; i < siders.length; i++) {
                for (var j = 0; j < columnas.length; j++) {
                    var celda = "Celda(" + siders[i].idSider + "," + columnas[j].identificador + ")";
                    model.push({
                        key: celda,
                        text: celda,
                        col: j + 2,
                        row: i + 2,
                        isGroup: true
                    });
                    celdas[celda] = 0;
                }
            }
            var tareasGrafico = plan.tareas;
            var tam = tareasGrafico.length;
            for (var i = 0; i < tam; i++) {
                var celda = "Celda(" + tareasGrafico[i].idGerencia + "," + tareasGrafico[i].idCriticidad + ")";
                //Buscar la tarea para la celda
                var objetoPlan = {
                    key: "" + tareasGrafico[i].tarea_id,
                    text: tareasGrafico[i].nombre,
                    cargo: tareasGrafico[i].cargo,
                    producto: tareasGrafico[i].producto,
                    partida: tareasGrafico[i].partida,
                    loc: celdas[celda] % 2 === 0 ? "" + (50 * celdas[celda]) + " " + (150 * celdas[celda]) : "" + (1000 + i * 4) + " " + (100 * (celdas[celda] - 1)),
                    tarea_id: tareasGrafico[i].tarea_id,
                    color_borde: tareasGrafico[i].color,
                    color: tareasGrafico[i].hito ? tareasGrafico[i].color : tareasGrafico[i].partida ? tareasGrafico[i].color : "#ffebee",
                    group: celda,
                    category: tareasGrafico[i].hito ? "Hito" : tareasGrafico[i].partida ? "Start" : ""
                }
                celdas[celda]++;
                model.push(objetoPlan);
            }

            myDiagram.linkTemplate = $(go.Link, {
                    routing: go.Link.AvoidsNodes,
                    toShortLength: 4,
                    corner: 20,
                }, {
                    relinkableFrom: true,
                    relinkableTo: true,
                    selectable: true
                },
                $(go.Shape, {isPanelMain: true, stroke: "black", strokeWidth: 3},
                    // the Shape.stroke color depends on whether Link.isHighlighted is true
                    new go.Binding("stroke", "isHighlighted", function (h) {
                        return h ? "yellow" : "black";
                    }).ofObject(), new go.Binding("strokeWidth", "isHighlighted", function (h) {
                        return h ? 5 : 3;
                    }).ofObject()),
                $(go.Shape, {toArrow: "standard", stroke: "black", strokeWidth: 3},
                    // the Shape.fill color depends on whether Link.isHighlighted is true
                    new go.Binding("fill", "isHighlighted", function (h) {
                        return h ? "yellow" : "black";
                    }).ofObject(), new go.Binding("strokeWidth", "isHighlighted", function (h) {
                        return h ? 5 : 3;
                    }).ofObject()));

            //Links
            // myDiagram.linkTemplate = $(go.Link, {routing: go.Link.AvoidsNodes, corner: 5}, {
            //     relinkableFrom: true,
            //     relinkableTo: true
            // }, $(go.Shape), $(go.Shape, {toArrow: "Standard"}));
            var links = plan.links;

            myDiagram.model = new go.GraphLinksModel(model, links);

            if (plan.diagrama !== "" && plan.diagrama !== null) {
                myDiagram.model = go.Model.fromJson(plan.diagrama);
            }
            //Centrar el grafico
            setTimeout(function () {
                vm.zoomToFit();
                myDiagram.commandHandler.increaseZoom(5);
                myDiagram.scrollToRect(new go.Rect(0, 0, 20, 20));
            }, 100);
        }

        function showConnections(node) {
            var myDiagram = node.diagram;
            myDiagram.startTransaction("highlight");
            // remove any previous highlighting
            myDiagram.clearHighlighteds();
            // for each Link coming out of the Node, set Link.isHighlighted
            node.findLinksOutOf().each(function (l) {
                l.isHighlighted = true;
            });
            // for each Node destination for the Node, set Node.isHighlighted
            node.findNodesOutOf().each(function (n) {
                n.isHighlighted = true;
            });
            myDiagram.commitTransaction("highlight");
        }

        var existePrimeraTarea = function () {
            var result = false;
            var modelo = myDiagram.model;
            for (var i = 0; i < modelo.nodeDataArray.length; i++) {
                if (modelo.nodeDataArray[i].id) {
                    result = true;
                    break;
                }
            }
            return result;
        };

        var pintarBorde = function (plan) {
            var modelo = myDiagram.model;
            var colores = plan.colores;
            var index = 0;
            for (var i = 0; i < modelo.nodeDataArray.length; i++) {
                if (modelo.nodeDataArray[i].id) {
                    var group = modelo.nodeDataArray[i].group;
                    index = parseInt(group[9] - 2);
                    var color_borde = colores[index];
                    modelo.nodeDataArray[i].color_borde = color_borde;
                    modelo.nodeDataArray[i].row = group[7];
                    modelo.nodeDataArray[i].col = group[9];
                    //Saber si es la primera tarea
                    if (start === 0) {
                        modelo.nodeDataArray[i].category = "Start";
                        modelo.nodeDataArray[i].color = color_borde;
                        start++;
                    }
                }
            }
            save(plan);
            setTimeout(function () {
                load(plan);
            }, 100);
        };

        var save = function (plan) {
            var modelo = myDiagram.model.toJson();
            myDiagram.isModified = false;
            localStorage.setItem("plan" + plan.plan_id, modelo);
        };
        var load = function (plan) {
            var modelo = localStorage.getItem("plan" + plan.plan_id);
            if (modelo !== undefined && modelo !== null) {
                myDiagram.model = go.Model.fromJson(modelo);
                myDiagram.delayInitialization(vm.relayoutDiagram());
            }

        };

        vm.mostrarModalTarea = function (tarea) {
            //Editar si es una tarea del diagrama
            if (tarea.group !== null) {
                $('#tarea-titulo').html(tarea.nombre);
                $('#tarea-producto').html(tarea.producto);
                $('#tarea-codigo').html(tarea.codigo);
                $('#tarea-nombre').html(tarea.nombre);
                $('#tarea-cargo').html(tarea.cargo);
                $('#tarea-recurrente').html(tarea.recurrente ? "Si" : "No");
                $('#tarea-fecha').html(tarea.fecha);
                $('#detalles-tarea').modal({
                    'show': true
                });
            } else {
                //Nueva tarea
                $('#cargo').html(tarea.cargo);
                $('#producto').val(tarea.producto);
                $('#id').val(tarea.id);
                $('#titulo').val(tarea.titulo);
                $('#detalles-tarea').modal({
                    'show': true
                });
            }
        };

        vm.destroyDiagrama = function () {
            $('#div-diagrama').html('<div id="myDiagramDiv" style="height:500px;"></div>');
            $('#div-paleta').html('<div id="myPaletteDiv" style="height:500px;"></div>');
            myDiagram = null;
        };
        vm.relayoutDiagram = function () {
            myDiagram.layout.invalidateLayout();
            myDiagram.layoutDiagram();
        };
        vm.zoomToFit = function () {
            myDiagram.zoomToFit();
        };
        vm.centerRoot = function () {
            myDiagram.scale = 1;
            myDiagram.scrollToRect(myDiagram.findNodeForKey(0).actualBounds);
        };
        vm.increaseZoom = function () {
            myDiagram.commandHandler.increaseZoom();
        };
        vm.decreaseZoom = function () {
            myDiagram.commandHandler.decreaseZoom();
        };
        vm.salvarDiagrama = function () {
            Metronic.blockUI({target: '#form-diagrama .portlet-body', animate: true});
            var diagrama = myDiagram.model.toJson();
            planService.salvarDiagrama($scope.planes[0].plan_id, diagrama).then(function (response) {
                Metronic.unblockUI('#form-diagrama .portlet-body');
                if (response.data.success) {
                    toastr.success(response.message, "Exito !!!");
                } else {
                    toastr.error(response.data.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            }).catch(function (response) {
                Metronic.unblockUI('#form-diagrama .portlet-body');
                toastr.error(response.data.error, "Error !!!", {"positionClass": "toast-top-center"});
            });
        };
    }
})();