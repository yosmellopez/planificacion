/* global go, Metronic, TableLayout, TableEditablePlanes, MyApp, celdas, NaN, LinkShiftingTool */

(function () {
    'use strict';

    angular
        .module('app.planes')
        .controller('Planes', Planes);

    Planes.$inject = ['$rootScope', '$scope', '$timeout', 'urlPath', 'estadoPlanService', 'criticidadPlanService', 'planService',
        'cargoService', 'areaService', 'gerenciaService', 'criticidadTareaService', 'estadoTareaService', 'canalService', 'logger'];

    function Planes($rootScope, $scope, $timeout, urlPath, estadoPlanService, criticidadPlanService, planService,
                    cargoService, areaService, gerenciaService, criticidadTareaService, estadoTareaService, canalService, logger) {
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
        $scope.planId = 0;
        $scope.tareasSeleccionadas = new Array();
        $scope.tareasAntecesoras = new Array();
        $scope.todasTareas = new Array();
        $scope.tareasAgrupadas = new Array();
        $scope.tareasPorAgrupar = new Array();
        $scope.historialTareas = new Array();
        $scope.sucesoras = new Array();
        $scope.antecesoras = new Array();
        $scope.nombreTarea = "";
        $scope.tituloModal = "";
        $scope.tarea = {};
        $scope.fullScreen = false;
        $scope.cargo = "";
        $scope.gerencia = "";
        $scope.nombreGerencia = "";
        $scope.direccion = "";
        $scope.criticidad = "";
        $scope.criticidades = vm.criticidades_tarea;
        $scope.recurrente = 'false';
        $scope.agrupada = false;
        $scope.seleccionado = false;
        $scope.tranversal = false;

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

        vm.cambiarRecurrente = function (valor) {
            $scope.recurrente = valor;
        };

        vm.cambiarValor = function () {
            $scope.agrupada = !$scope.agrupada;
            if ($scope.agrupada) {
                var tam = $scope.tareasAgrupadas.length;
                for (var i = 0; i < tam; i++) {
                    $scope.tareasPorAgrupar.push($scope.tareasAgrupadas[i]);
                }
                $scope.tareasAgrupadas = new Array();
            }
        };

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


        vm.filtarTareasCargo = function () {
            Metronic.unblockUI('#form-plan .portlet-body');
            $scope.cargo = $("#cargo").val();
            $scope.gerencia = $("#gerencia").val();
            $scope.direccion = $("#direccion").val();
            $scope.criticidad = $("#nivel-alerta").val();
            if (parseInt($scope.criticidad))
                $scope.criticidad = parseInt($scope.criticidad);
            else $scope.criticidad = "";
            if (parseInt($scope.cargo))
                $scope.cargo = parseInt($scope.cargo);
            else $scope.cargo = "";
            if (parseInt($scope.gerencia))
                $scope.gerencia = parseInt($scope.gerencia);
            else $scope.gerencia = "";
            if (parseInt($scope.direccion))
                $scope.direccion = parseInt($scope.direccion);
            else $scope.direccion = "";
            $timeout(function () {
                TableEditablePlanes.filtrarTareasPlan($scope.cargo, $scope.gerencia, $scope.direccion, $scope.criticidad);
            }, 0);
        };

        vm.mostrarMasDetalles = function (idTarea) {
            Metronic.blockUI({target: '#form-diagrama .portlet-body', animate: true});
            $('#modal-tarea-detalle').modal('toggle');
            $('.modal-backdrop').remove();
            $timeout(function () {
                planService.buscarTarea(idTarea, $scope.planId).then(success).catch(failed);
            }, 0);

            function success(response) {
                Metronic.unblockUI('#form-diagrama .portlet-body');
                $scope.tarea = response.data.tarea;
                $scope.antecesoras = response.data.antecesoras;
                $scope.sucesoras = response.data.sucesoras;
                $scope.tituloModal = $scope.tarea.nombre;
                $('#modal-tarea-detalle').modal({
                    show: true
                });
                $('#modal-tarea-detalle button.download').data("title", 'Descargar modelo').tooltip();
                $('#modal-tarea-detalle button.view').data("title", 'Ver Detalles de la Tarea').tooltip();
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        };

        vm.cerrarModal = function () {
            $('.modal-backdrop').remove();
        };

        vm.fullScreen = function () {
            var porlet = $("#pantallaCompleta")[0];
            $("#myDiagramDiv").css("height", h + "px");
            var w = $(window).width();
            var h = $(window).height();
            $(porlet).css("width", w + "px");
            $(porlet).css("height", h + "px");
            if (porlet.RequestFullScreen) {
                porlet.RequestFullScreen();
            } else if (porlet.webkitRequestFullScreen) {
                porlet.webkitRequestFullScreen();
            } else if (porlet.mozRequestFullScreen) {
                porlet.mozRequestFullScreen();
            } else if (porlet.msRequestFullscreen) {
                porlet.msRequestFullscreen();
            } else {
                alert("This browser doesn't supporter fullscreen");
            }
        };
        vm.borrarDiagrama = function () {
            $timeout(function () {
                TableEditablePlanes.btnClickBorrarDiagrama();
            }, 0);
        };
        vm.exportar = function () {
            $timeout(function () {
                TableEditablePlanes.exportar();
            }, 0);
        };

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
                        $scope.todasTareas.splice(elem.posicion, 1);
                        $(checkboxes[i]).parent().parent().parent().remove();
                    }
                }
            }
        };

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
        };

        vm.agregarAgrupada = function () {
            var checkboxes = $("#cuerpo-tareas-agrupar input[type=checkbox]");
            var tam = checkboxes.length;
            for (var i = 0; i < tam; i++) {
                if (checkboxes[i].checked) {
                    var elem = buscarElementoAgrupado(checkboxes[i].id, true);
                    if (elem !== null) {
                        $scope.tareasAgrupadas.push(elem.item);
                        $(checkboxes[i]).parent().parent().parent().remove();
                        $scope.tareasPorAgrupar.splice(elem.posicion, 1);
                        $scope.seleccionado = $scope.tareasAgrupadas.length !== 0;
                    }
                }
            }
        };


        function buscarElementoAgrupado(id, seleccionados) {
            if (!seleccionados) {
                var tam = $scope.tareasAgrupadas.length;
                for (var i = 0; i < tam; i++) {
                    if (parseInt($scope.tareasAgrupadas[i].tarea_id) === parseInt(id)) {
                        return {posicion: i, item: $scope.tareasAgrupadas[i]};
                    }
                }
                return null;
            } else {
                var tam = $scope.tareasPorAgrupar.length;
                for (var i = 0; i < tam; i++) {
                    if (parseInt($scope.tareasPorAgrupar[i].tarea_id) === parseInt(id)) {
                        return {posicion: i, item: $scope.tareasPorAgrupar[i]};
                    }
                }
                return null;
            }
        }

        vm.eliminarAgrupada = function () {
            var checkboxes = $("#table-agrupadas-cuerpo input[type=checkbox]");
            var tam = checkboxes.length;
            for (var i = 0; i < tam; i++) {
                if (checkboxes[i].checked) {
                    var elem = buscarElementoAgrupado(checkboxes[i].id, false);
                    if (elem !== null) {
                        $scope.todasTareas.push(elem.item);
                        $scope.tareasAgrupadas.splice(elem.posicion, 1);
                        $(checkboxes[i]).parent().parent().parent().remove();
                        $scope.seleccionado = $scope.tareasAgrupadas.length !== 0;
                    }
                }
            }
        };

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

        vm.seleccionarElemento = function (config) {
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
        };

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
        $scope.sucesoras = new Array();
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
                $scope.tarea = response.data.tarea;
                $scope.antecesoras = response.data.antecesoras;
                $scope.sucesoras = response.data.sucesoras;
                $scope.tituloModal = response.data.tarea.nombre;
                $('#modal-tarea-detalle').modal({
                    'show': true
                });
                $('#table-modelos-tarea .download').data("title", 'Descargar modelo').tooltip();
                $('#table-modelos-tarea .view').data("title", 'Ver Detalles de la Tarea').tooltip();
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        };

        vm.mostrarMasDetalles = function (idTarea) {
            Metronic.blockUI({target: '#form-diagrama .portlet-body', animate: true});
            $('#modal-tarea-detalle').modal('toggle');
            $('.modal-backdrop').remove();
            $timeout(function () {
                planService.buscarTarea(idTarea, $scope.idPlan).then(success).catch(failed);
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
        $scope.sucesoras = new Array();
        var myDiagram = null;
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
                    $scope.planId = vm.planes[0].plan_id;
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

        vm.mostrarMasDetalles = function (idTarea) {
            Metronic.blockUI({target: '#form-diagrama .portlet-body', animate: true});
            $('#modal-tarea-detalle').modal('toggle');
            $('.modal-backdrop').remove();
            $timeout(function () {
                planService.buscarTarea(idTarea, $scope.planId).then(success).catch(failed);
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
                $scope.sucesoras = response.data.sucesoras;
                $scope.tituloModal = response.data.tarea.nombre;
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
                //Nuevas opciones
                "InitialLayoutCompleted": function (e) {
                    // var div = e.diagram.div;
                    // var height = parseInt(e.diagram.documentBounds.height / 2) + 24;
                    //
                    // div.style.height = height + 'px';
                    // e.diagram.requestUpdate();
                },
                hasVerticalScrollbar: false,
                scale: 0.5,
                layout: $(TableLayout, $(go.RowColumnDefinition, {
                    row: 1,
                    height: 22
                }), $(go.RowColumnDefinition, {column: 1, width: 22})),
                "SelectionMoved": function (e) {
                    e.diagram.layoutDiagram(true);
                },
                "SelectionDeleted": function (e) {
                    myDiagram.commandHandler.undo();
                },
                "LinkDrawn": function (e) {
                    myDiagram.commandHandler.undo();
                },
                "ExternalObjectsDropped": function (e) {
                    pintarBorde(plan);
                },
                "resizingTool": new LaneResizingTool(),
                "linkingTool.isEnabled": true,
                allowDrop: true,
                // feedback that dropping in the background is not allowed
                mouseDragOver: function (e) {
                    e.diagram.currentCursor = "not-allowed";
                },
                // when dropped in the background, not on a Node or a Group, cancel the drop
                mouseDrop: function (e) {
                    // e.diagram.currentTool.doCancel();
                },
                "animationManager.isInitial": false,
                "undoManager.isEnabled": true,
                "linkReshapingTool": new OrthogonalLinkReshapingTool(),
                "LinkReshaped": function (e) {
                    e.subject.routing = go.Link.Orthogonal;
                }
            });

            myDiagram.toolManager.mouseDownTools.add($(LinkShiftingTool));

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
                    locationSpot: go.Spot.Center,
                    click: function (e, node) {
                        showConnections(node);
                    },
                    doubleClick: function (e, node) {
                        vm.mostrarDetallesTarea(node.data.tarea_id);
                    }
                }, /*new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),*/ new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify),
                new go.Binding("position", "bounds", go.Point.parse).makeTwoWay(go.Point.stringify),
                {selectable: true, selectionAdornmentTemplate: nodeSelectionAdornmentTemplate, movable: false},
                {resizable: false, resizeObjectName: "PANEL", resizeAdornmentTemplate: nodeResizeAdornmentTemplate},
                new go.Binding("row"),
                new go.Binding("column", "col"),
                $(go.Panel, "Auto", $(go.Shape, "DividedEvent", {
                        fill: "white",
                        strokeWidth: 1,
                        height: 110,
                        stroke: "transparent",
                        portId: "",
                        fromSpot: go.Spot.Right,
                        toSpot: go.Spot.Left,
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
                        }, new go.Binding("text").makeTwoWay()),
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
                    locationSpot: go.Spot.Center,
                    click: function (e, node) {
                        showConnections(node);
                    },
                    doubleClick: function (e, node) {
                        vm.mostrarDetallesTarea(node.data.tarea_id);
                    }
                }, /*new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),*/ new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify),
                {selectable: true, selectionAdornmentTemplate: nodeSelectionAdornmentTemplate, movable: false},
                {resizable: false, resizeObjectName: "PANEL", resizeAdornmentTemplate: nodeResizeAdornmentTemplate},
                new go.Binding("row"),
                new go.Binding("column", "col"),
                new go.Binding("position", "bounds", go.Point.parse).makeTwoWay(go.Point.stringify),
                $(go.Panel, "Auto", $(go.Shape, "Parallelogram2", {
                    fill: "white",
                    strokeWidth: 1,
                    stroke: "transparent",
                    portId: "",
                    minSize: new go.Size(250, 70),
                    fromSpot: go.Spot.Right,
                    toSpot: go.Spot.Left,
                    fromLinkable: true,
                    toLinkable: true
                }, new go.Binding("fill", "color"), new go.Binding("stroke", "color_borde")), $(go.Panel, "Table", {
                    margin: new go.Margin(0, 0, 10, 0),
                    defaultAlignment: go.Spot.Left
                }, $(go.RowColumnDefinition, {
                    column: 0,
                    width: 200
                }), $(go.TextBlock, {font: "10pt bold Segoe UI,sans-serif", stroke: "#000"}, {
                    row: 0,
                    column: 0,
                    columnSpan: 4,
                    textAlign: "center",
                    margin: new go.Margin(5, 5, 0, 15)
                }, new go.Binding("text").makeTwoWay())))));

            myDiagram.nodeTemplate = $(go.Node, "Auto", {
                    margin: new go.Margin(10, 10, 10, 10),
                    locationSpot: go.Spot.Center,
                    click: function (e, node) {
                        showConnections(node);
                    },
                    doubleClick: function (e, node) {
                        vm.mostrarDetallesTarea(node.data.tarea_id);
                    }
                }, /* new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),*/ new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify),
                new go.Binding("position", "bounds", go.Point.parse).makeTwoWay(go.Point.stringify),
                {selectable: true, selectionAdornmentTemplate: nodeSelectionAdornmentTemplate, movable: false},
                {resizable: false, resizeObjectName: "PANEL", resizeAdornmentTemplate: nodeResizeAdornmentTemplate},
                new go.Binding("row"), new go.Binding("column", "col"), $(go.Shape, "Rectangle", {
                    fill: "white",
                    strokeWidth: 1,
                    stroke: "transparent",
                    portId: "",
                    fromSpot: go.Spot.Right,
                    toSpot: go.Spot.Left,
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
                    }, new go.Binding("text").makeTwoWay()),
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

                    }
                }, new go.Binding("row"), new go.Binding("column", "col"), new go.Binding('location', 'loc', go.Point.parse), new go.Binding("columnSpan", "colSpan").makeTwoWay(),
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
                        return new go.Margin(50, m.right + 50, m.bottom + 50, 50);
                    })(myDiagram.nodeTemplate.margin)
                })
            );

            myDiagram.linkTemplate = $(go.Link, {
                    //Nuevas opciones
                    relinkableFrom: true,
                    relinkableTo: true,
                    reshapable: true,
                    resegmentable: true,
                    layerName: "Background"
                }, {
                    routing: go.Link.AvoidsNodes,
                    adjusting: go.Link.End,
                    curve: go.Link.JumpOver,
                    toShortLength: 4
                },
                ///Nuevas opciones
                new go.Binding("points").makeTwoWay(),
                // new go.Binding("routing", "routing", go.Binding.parseEnum(go.Link, go.Link.AvoidsNodes)).makeTwoWay(go.Binding.toString),
                //End Nuevas opciones
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

            var links = plan.links;

            if (plan.diagrama !== "" && plan.diagrama !== null) {
                myDiagram.model = go.Model.fromJson(plan.diagrama);
                var pos = myDiagram.model.modelData.position;
                if (pos)
                    myDiagram.initialPosition = go.Point.parse(pos);
                // myDiagram.delayInitialization(relayoutDiagram);
            } else {
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
                        peso: columnas[i].peso,
                        color_header: columnas[i].color
                    });
                    col++;
                }
                //Rows sider
                var row = 2;
                var filas = new Array();
                for (var i = 0; i < siders.length; i++) {
                    model.push({
                        key: "sd" + siders[i].idSider,
                        text: siders[i].nombre,
                        row: row,
                        peso: siders[i].peso,
                        category: "Row Sider"
                    });
                    filas.push({idGerencia: siders[i].idSider, fila: row});
                    row++;
                }
                //Celdas
                var celdas = new Array();
                var cont = new Array();
                for (var i = 0; i < siders.length; i++) {
                    for (var j = 0; j < columnas.length; j++) {
                        var celda = "Celda(" + siders[i].idSider + "," + columnas[j].identificador + ")";
                        cont["Fila(" + siders[i].idSider + ")"] = 0;
                        model.push({
                            key: celda,
                            text: celda,
                            col: j + 2,
                            row: i + 2,
                            isGroup: true
                        });
                        // Tareas Invisibles
                        model.push({
                            key: generateUUID(),
                            text: "",
                            color: "white",
                            size: "3 3",
                            group: celda
                        });
                        celdas[celda] = {pos: 0, x: 0, y: 0};
                    }
                }
                var tareasGrafico = plan.tareas;
                var tam = tareasGrafico.length;
                var gruposDiagrama = new Array();
                for (var i = 0; i < tam; i++) {
                    var celda = "Celda(" + tareasGrafico[i].idGerencia + "," + tareasGrafico[i].idCriticidad + ")";
                    //Buscar la tarea para la celda
                    if (tareasGrafico[i].partida) {
                        celdas[celda].x = 0;
                        celdas[celda].y = 0;
                    }
                    var objetoPlan = {
                        key: "" + tareasGrafico[i].key,
                        text: tareasGrafico[i].nombre,
                        cargo: tareasGrafico[i].cargo,
                        producto: tareasGrafico[i].producto,
                        partida: tareasGrafico[i].partida,
                        bounds: celdas[celda].x + " " + celdas[celda].y,
                        tarea_id: tareasGrafico[i].tarea_id,
                        color_borde: tareasGrafico[i].color,
                        color: tareasGrafico[i].hito ? tareasGrafico[i].color : tareasGrafico[i].partida ? tareasGrafico[i].color : "#ffebee",
                        group: celda,
                        category: tareasGrafico[i].hito ? "Hito" : tareasGrafico[i].partida ? "Start" : ""
                    }
                    model.push(objetoPlan);
                    celdas[celda].pos++;
                    celdas[celda].x += 500;
                    if (celdas[celda].pos % 3 === 0) {
                        celdas[celda].x = 0;
                        celdas[celda].y += 300;
                    }
                }
                var yTranversal = 150;
                var tareasTranversales = plan.tareasTranversales;
                var tamTranversal = tareasTranversales.length;
                for (var i = 0; i < tamTranversal; i++) {
                    var celda = "Celda(" + tareasTranversales[i].idGerencia + "," + tareasTranversales[i].idCriticidad + ")";
                    var texto = "Fila(" + tareasTranversales[i].idGerencia + ")";
                    //Buscar la tarea para la celda
                    var fila = buscarFila(filas, tareasTranversales[i].idGerencia);
                    var columna = tareasTranversales[i].col;
                    var nombreGrupo = "tv" + fila + tareasTranversales[i].grupo;
                    // var celdaMayorCantidad = buscarMaximoFila(columnas, tareasTranversales[i].idGerencia, tareasTranversales[i].idCriticidad, tareasTranversales[i].colSpan);
                    var grupo = {
                        key: nombreGrupo,
                        text: "",
                        color: "white",
                        // bounds: celdaMayorCantidad.x + " " + celdaMayorCantidad.y,
                        col: columna,
                        colSpan: tareasTranversales[i].colSpan,
                        row: fila,
                        isGroup: true
                    };
                    if (!existeGrupo(gruposDiagrama, grupo.key)) {
                        gruposDiagrama.push(grupo);
                        model.push(grupo);
                    }
                    model.push({
                        key: generateUUID(),
                        text: "",
                        color: "white",
                        bounds: 0 + " " + yTranversal,
                        size: "0 " + 200,
                        group: nombreGrupo
                    });
                    var objetoTranversal = {
                        key: "" + tareasTranversales[i].key,
                        text: tareasTranversales[i].nombre,
                        size: tareasTranversales[i].colSpan * (tam + tamTranversal + 600) + " " + 100,
                        bounds: 0 + " " + yTranversal,
                        partida: tareasTranversales[i].partida,
                        tarea_id: tareasTranversales[i].tarea_id,
                        cargo: tareasTranversales[i].cargo,
                        producto: tareasTranversales[i].producto,
                        color_borde: tareasTranversales[i].color,
                        color: tareasTranversales[i].hito ? tareasTranversales[i].color : tareasTranversales[i].partida ? tareasTranversales[i].color : "#ffebee",
                        group: nombreGrupo,
                        category: ""
                    };
                    console.log(yTranversal);
                    yTranversal += 300;
                    model.push(objetoTranversal);
                }
                myDiagram.model = new go.GraphLinksModel(model, links);
            }
        }
        ;

        var buscarMaximoFila = function (columnas, idGerencia, idCriticidad, colSpand) {
            var pos = 0;
            var posInicial = buscarPosicionNivelAlerta(columnas, idCriticidad);
            var celda = "Celda(" + idGerencia + "," + idCriticidad + ")";
            celdas[celda].y += 100;
            celdas[celda].x = 0;
            celdas[celda].pos += 2;
            while (pos < colSpand && posInicial < columnas.length - 1) {
                var celdaSiguiente = "Celda(" + idGerencia + "," + columnas[posInicial + 1].identificador + ")";
                if (celdas[celda].y < celdas[celdaSiguiente].y)
                    celda = celdaSiguiente;
                pos++;
                posInicial++;
                celdas[celdaSiguiente].y += 300;
                celdas[celdaSiguiente].x = 0;
                celdas[celdaSiguiente].pos += 2;
            }
            celdas[celda].y += 300;
            celdas[celda].x = 0;
            celdas[celda].pos += 2;
            return celdas[celda];
        }

        var buscarPosicionNivelAlerta = function (columnas, idCriticidad) {
            for (var i = 0; i < columnas.length; i++) {
                if (parseInt(idCriticidad) === parseInt(columnas[i].identificador))
                    return i;
            }
            return -1;
        }

        function existeGrupo(grupos, clave) {
            var tam = grupos.length;
            for (i = 0; i < tam; i++) {
                if (grupos[i].key === clave) {
                    return true;
                }
            }
            return false;
        }

        var generateUUID = function () {
            var d = new Date().getTime();
            var uuid = 'xxxxxxxxxxxx4xxxyxxxxxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                var r = (d + Math.random() * 16) % 16 | 0;
                d = Math.floor(d / 16);
                return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
            });
            return uuid;
        };

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

        function buscarFila(filas, idGerencia) {
            var tam = filas.length;
            for (var i = 0; i < tam; i++) {
                if (parseInt(filas[i].idGerencia) === parseInt(idGerencia)) {
                    return filas[i].fila;
                }
            }
            return null;
        }

        function existeGrupo(grupos, clave) {
            var tam = grupos.length;
            for (var i = 0; i < tam; i++) {
                if (grupos[i].key === clave) {
                    return true;
                }
            }
            return false;
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
        vm.generateUUID = function () {
            var d = new Date().getTime();
            var uuid = 'xxxxxxxxxxxx4xxxyxxxxxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                var r = (d + Math.random() * 16) % 16 | 0;
                d = Math.floor(d / 16);
                return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
            });
            return uuid;
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
        }
        ;
    }
})();