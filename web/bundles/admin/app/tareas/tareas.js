(function () {
    'use strict';

    angular
        .module('app.tareas')
        .controller('Tareas', Tareas);

    Tareas.$inject = ['$rootScope', '$scope', '$timeout', 'urlPath', 'cargoService', 'areaService', 'gerenciaService', 'criticidadTareaService', 'estadoTareaService', 'canalService', 'usuarioService'];

    function Tareas($rootScope, $scope, $timeout, urlPath, cargoService, areaService, gerenciaService, criticidadTareaService, estadoTareaService, canalService, usuarioService) {
        var vm = this;
        vm.urlPath = urlPath;

        vm.nuevo = nuevo;
        vm.mostrarNuevo = true;
        $scope.titulo = "Lista de Tareas";
        $scope.cargo = "";
        $scope.gerencia = "";
        $scope.direccion = "";
        $scope.nivelAlerta = "";
        $scope.recurrente = 'false';
        vm.selecciono = false;
        vm.usuario = usuarioService.usuario;
        vm.estadosTareas = estadoTareaService.estados;
        vm.criticidades = criticidadTareaService.criticidades;
        vm.gerencias = gerenciaService.gerencias;
        vm.areas = areaService.areas;
        vm.cargos = cargoService.cargos;
        vm.canales = canalService.canales;
        vm.eliminar = eliminar;
        vm.modalEliminar = modalEliminar;
        vm.filtarTareasCargo = filtarTareasCargo;
        vm.modalEliminarSeleccion = modalEliminarSeleccion;

        vm.cerrarForm = cerrarForm;
        vm.salvarForm = salvarForm;
        vm.filtrar = filtrar;

        vm.nuevoModelo = nuevoModelo;
        vm.salvarModelo = salvarModelo;
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

        function filtrar() {
            TableEditableTareas.btnClickFiltrar();
        }

        function nuevoModelo() {
            TableEditableTareas.btnClickNuevoModelo();
        }

        function salvarModelo() {
            TableEditableTareas.btnClickSalvarModelo();
        }

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
            $scope.gerencia = $("#gerencia-filtro").val();
            $scope.direccion = $("#direccion").val();
            $scope.criticidad = $("#nivel-alerta").val();
            TableEditableTareas.buscarTareas("tarea/buscarTareas?cargo=" + $scope.cargo + "&area=" + $scope.gerencia + "&direccion=" + $scope.direccion + "&criticidad=" + $scope.criticidad)
        }

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                // initialize core components
                Metronic.initAjax();
                MyApp.init();

                $timeout(function () {
                    TableEditableTareas.init(vm.urlPath, $scope);
                }, 1000);
            });
        }

    }
})();
