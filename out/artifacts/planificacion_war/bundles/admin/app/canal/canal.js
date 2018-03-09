(function () {
    'use strict';

    angular
            .module('app.canal')
            .controller('Canal', Canal);

    Canal.$inject = ['$rootScope', '$scope', '$timeout', 'urlPath'];

    function Canal($rootScope, $scope, $timeout, urlPath) {
        var vm = this;
        vm.urlPath = urlPath;

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
                TableEditableCanal.btnClickNuevo();
            }, 0);
        }

        function eliminar() {
            TableEditableCanal.btnClickEliminar();
        }

        function modalEliminar() {
            TableEditableCanal.btnClickModalEliminar();
        }

        function modalEliminarSeleccion() {
            TableEditableCanal.btnClickModalEliminarSeleccion();
        }

        function cerrarForm() {
            $timeout(function () {
                TableEditableCanal.btnClickCerrarForm();
            }, 0);
        }

        function salvarForm() {
            TableEditableCanal.btnClickSalvarForm();
        }

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                // initialize core components
                Metronic.initAjax();
                MyApp.init();

                $timeout(function () {
                    TableEditableCanal.init();
                }, 0);

            });
        }

    }
})();
