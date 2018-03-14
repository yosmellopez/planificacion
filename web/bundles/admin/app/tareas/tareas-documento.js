/* global TableEditableTareas, Metronic */

(function () {
    'use strict';

    angular
        .module('app.tareas')
        .controller('TareasDocumentos', TareasDocumentos);

    TareasDocumentos.$inject = ['$rootScope', '$scope', '$timeout', 'urlPath', 'cargoService', 'areaService', 'gerenciaService', 'criticidadTareaService', 'estadoTareaService', 'canalService', 'usuarioService', 'planService'];

    function TareasDocumentos($rootScope, $scope, $timeout, urlPath, cargoService, areaService, gerenciaService, criticidadTareaService, estadoTareaService, canalService, usuarioService, planService) {
        var vm = this;
        vm.urlPath = urlPath;

        vm.mostrarNuevo = true;
        vm.usuario = usuarioService.usuario;
        $scope.titulo = "Documentos de Tareas";
        $scope.cargo = "";
        $scope.gerencia = "";
        $scope.direccion = "";
        $scope.nivelAlerta = "";
        $scope.recurrente = 'false';
        $scope.tranversal = false;
        $scope.usuario = vm.usuario;
        vm.selecciono = false;
        vm.estadosTareas = estadoTareaService.estados;
        vm.criticidades = criticidadTareaService.criticidades;
        $scope.criticidades = vm.criticidades;
        vm.gerencias = gerenciaService.gerencias;
        vm.areas = areaService.areas;
        vm.cargos = cargoService.cargos;
        vm.canales = canalService.canales;
        if (localStorage.getItem("existePlan")) {
            $scope.plan = JSON.parse(localStorage.getItem("planActivo"));
            $scope.titulo = $scope.plan.nombre;
            $scope.planId = $scope.plan.plan_id;
            $scope.mostrarInicial = true;
        }
        vm.filtarTareasCargo = filtarTareasCargo;
        vm.seleccionarElemento = seleccionarElemento;

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageBodySolid = true;
        $rootScope.settings.layout.pageSidebarClosed = false;

        activate();

        function seleccionarElemento(config) {
            var cargoSelect = $("#cargo-filtro");
            var gerenciaSelect = $("#gerencia-filtro");
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
                $scope.gerencia = $("#gerencia-filtro").val();
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
            $scope.cargo = $("#cargo-filtro").val();
            if (parseInt($scope.cargo))
                $scope.cargo = parseInt($scope.cargo);
            else
                $scope.cargo = "";
            $scope.gerencia = $("#gerencia-filtro").val();
            if (parseInt($scope.gerencia))
                $scope.gerencia = parseInt($scope.gerencia);
            else
                $scope.gerencia = "";
            $scope.direccion = $("#direccion").val();
            if (parseInt($scope.direccion))
                $scope.direccion = parseInt($scope.direccion);
            else
                $scope.direccion = "";
            $scope.criticidad = $("#nivel-alerta").val();
            if (parseInt($scope.criticidad))
                $scope.criticidad = parseInt($scope.criticidad);
            else
                $scope.criticidad = "";
            TableTareasDocumentos.buscarTareas("tarea/buscarTareas?cargo=" + $scope.cargo + "&area=" + $scope.gerencia + "&direccion=" + $scope.direccion + "&criticidad=" + $scope.criticidad);
        }

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

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                // initialize core components
                Metronic.initAjax();
                MyApp.init();

                $timeout(function () {
                    TableTareasDocumentos.init(vm.urlPath, $scope, planService);
                }, 1000);
            });
        }

    }
})();
