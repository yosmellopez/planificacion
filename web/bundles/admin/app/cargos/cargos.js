(function () {
    'use strict';

    angular
        .module('app.cargos')
        .controller('Cargos', Cargos);

    Cargos.$inject = ['$rootScope', '$scope', '$timeout', 'urlPath', 'areaService'];

    function Cargos($rootScope, $scope, $timeout, urlPath, areaService) {
        var vm = this;
        vm.urlPath = urlPath;

        vm.areas = areaService.areas;

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
                TableEditableCargos.btnClickNuevo();
            }, 0);

        }

        function eliminar() {
            TableEditableCargos.btnClickEliminar();
        }

        function modalEliminar() {
            TableEditableCargos.btnClickModalEliminar();
        }

        function modalEliminarSeleccion() {
            TableEditableCargos.btnClickModalEliminarSeleccion();
        }

        function cerrarForm() {
            $timeout(function () {
                TableEditableCargos.btnClickCerrarForm();
            }, 0);
        }

        function salvarForm() {
            TableEditableCargos.btnClickSalvarForm();
        }

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                // initialize core components
                Metronic.initAjax();
                MyApp.init();

                $timeout(function () {
                    TableEditableCargos.init();
                }, 0);

            });
        }

    }
})();
