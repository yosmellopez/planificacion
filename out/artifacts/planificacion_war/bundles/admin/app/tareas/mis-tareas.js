(function () {
    'use strict';

    angular
        .module('app.tareas')
        .controller('TareasDocumentos', TareasDocumentos);

    TareasDocumentos.$inject = ['$rootScope', '$scope', '$timeout', 'urlPath', 'gerenciaService', 'criticidadTareaService', 'estadoTareaService'];

    function TareasDocumentos($rootScope, $scope, $timeout, urlPath, gerenciaService, criticidadTareaService, estadoTareaService) {
        var vm = this;
        vm.urlPath = urlPath;

        vm.gerencias = gerenciaService.gerencias;
        vm.criticidades = criticidadTareaService.criticidades;
        vm.estados = estadoTareaService.estados;
        vm.mostrarNuevo = false;
        $scope.titulo = "Mis Tareas";
        vm.nuevo = nuevo;
        vm.eliminar = eliminar;
        vm.modalEliminar = modalEliminar;
        vm.modalEliminarSeleccion = modalEliminarSeleccion;

        vm.cerrarForm = cerrarForm;
        vm.salvarForm = salvarForm;
        vm.filtrar = filtrar;

        vm.nuevoModelo = nuevoModelo;
        vm.salvarModelo = salvarModelo;

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageBodySolid = true;
        $rootScope.settings.layout.pageSidebarClosed = false;

        activate();

        function nuevo() {
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

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                // initialize core components
                Metronic.initAjax();
                MyApp.init();

                $timeout(function () {
                    TableEditableTareas.init(vm.urlPath);
                }, 1000);
            });
        }

    }
})();
