(function () {
    'use strict';

    angular
        .module('app.areas')
        .controller('Areas', Areas);

    Areas.$inject = ['$rootScope', '$scope', '$timeout', 'urlPath', 'gerenciaService'];

    function Areas($rootScope, $scope, $timeout, urlPath, gerenciaService) {
        var vm = this;
        vm.urlPath = urlPath;

        vm.gerencias = gerenciaService.gerencias;
        vm.nuevo = nuevo;
        vm.eliminar = eliminar;
        vm.modalEliminar = modalEliminar;
        vm.modalEliminarSeleccion = modalEliminarSeleccion;

        vm.cerrarForm = cerrarForm;
        vm.salvarForm = salvarForm;

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageBodySolid = true;
        $rootScope.settings.layout.pageSidebarClosed = false;

        activate();

        function nuevo() {
            $timeout(function () {
                TableEditableAreas.btnClickNuevo();
            }, 0);

        }

        function eliminar() {
            TableEditableAreas.btnClickEliminar();
        }

        function modalEliminar() {
            TableEditableAreas.btnClickModalEliminar();
        }

        function modalEliminarSeleccion() {
            TableEditableAreas.btnClickModalEliminarSeleccion();
        }

        function cerrarForm() {
            $timeout(function () {
                TableEditableAreas.btnClickCerrarForm();
            }, 0);
        }

        function salvarForm() {
            TableEditableAreas.btnClickSalvarForm();
        }

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                // initialize core components
                Metronic.initAjax();
                MyApp.init();

                $timeout(function () {
                    TableEditableAreas.init();
                }, 0);

            });
        }

    }
})();
