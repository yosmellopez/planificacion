(function () {
    'use strict';

    angular
        .module('app.gerencias')
        .controller('Gerencias', Gerencias);

    Gerencias.$inject = ['$rootScope', '$scope', '$timeout', 'urlPath'];

    function Gerencias($rootScope, $scope, $timeout, urlPath) {
        var vm = this;
        vm.urlPath = urlPath

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
                TableEditableGerencias.btnClickNuevo();
            }, 0);

        }

        function eliminar() {
            TableEditableGerencias.btnClickEliminar();
        }

        function modalEliminar() {
            TableEditableGerencias.btnClickModalEliminar();
        }

        function modalEliminarSeleccion() {
            TableEditableGerencias.btnClickModalEliminarSeleccion();
        }

        function cerrarForm() {
            $timeout(function () {
                TableEditableGerencias.btnClickCerrarForm();
            }, 0);
        }

        function salvarForm() {
            TableEditableGerencias.btnClickSalvarForm();
        }

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                // initialize core components
                Metronic.initAjax();
                MyApp.init();

                $timeout(function () {
                    TableEditableGerencias.init();
                }, 0);

            });
        }

    }
})();
