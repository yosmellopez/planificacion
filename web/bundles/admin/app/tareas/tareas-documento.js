/* global TableEditableTareas, Metronic */

(function () {
    'use strict';

    angular
        .module('app.tareas')
        .controller('TareasDocumentos', TareasDocumentos);

    TareasDocumentos.$inject = ['$rootScope', '$scope', '$timeout', 'urlPath', 'cargoService', 'areaService', 'gerenciaService', 'criticidadTareaService', 'estadoTareaService', 'canalService', 'usuarioService'];

    function TareasDocumentos($rootScope, $scope, $timeout, urlPath, cargoService, areaService, gerenciaService, criticidadTareaService, estadoTareaService, canalService, usuarioService) {
        var vm = this;
        vm.urlPath = urlPath;

        vm.mostrarNuevo = true;
        vm.usuario = usuarioService.usuario;
        $scope.titulo = "Lista de Tareas";
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

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                // initialize core components
                Metronic.initAjax();
                MyApp.init();

                $timeout(function () {
                    TableTareasDocumentos.init(vm.urlPath, $scope);
                }, 1000);
            });
        }

    }
})();
