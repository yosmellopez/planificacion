(function () {
    'use strict';

    angular
        .module('app.estadoPlan')
        .controller('EstadoPlan', EstadoPlan);

    EstadoPlan.$inject = ['$rootScope', '$scope', '$timeout', 'urlPath'];

    function EstadoPlan($rootScope, $scope, $timeout, urlPath) {
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
                TableEditableEstadoPlan.btnClickNuevo();
            }, 0);
        }

        function eliminar() {
            TableEditableEstadoPlan.btnClickEliminar();
        }

        function modalEliminar() {
            TableEditableEstadoPlan.btnClickModalEliminar();
        }

        function modalEliminarSeleccion() {
            TableEditableEstadoPlan.btnClickModalEliminarSeleccion();
        }

        function cerrarForm() {
            $timeout(function () {
                TableEditableEstadoPlan.btnClickCerrarForm();
            }, 0);
        }

        function salvarForm() {
            TableEditableEstadoPlan.btnClickSalvarForm();
        }

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                // initialize core components
                Metronic.initAjax();
                MyApp.init();

                $timeout(function () {
                    TableEditableEstadoPlan.init();
                }, 0);

            });
        }

    }
})();
