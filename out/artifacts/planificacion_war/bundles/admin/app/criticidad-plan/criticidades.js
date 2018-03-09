(function () {
    'use strict';

    angular
        .module('app.criticidadPlan')
        .controller('CriticidadPlan', CriticidadPlan);

    CriticidadPlan.$inject = ['$rootScope', '$scope', '$timeout', 'urlPath'];

    function CriticidadPlan($rootScope, $scope, $timeout, urlPath) {
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
                TableEditableCriticidadPlan.btnClickNuevo();
            }, 0);
        }

        function eliminar() {
            TableEditableCriticidadPlan.btnClickEliminar();
        }

        function modalEliminar() {
            TableEditableCriticidadPlan.btnClickModalEliminar();
        }

        function modalEliminarSeleccion() {
            TableEditableCriticidadPlan.btnClickModalEliminarSeleccion();
        }

        function cerrarForm() {
            $timeout(function () {
                TableEditableCriticidadPlan.btnClickCerrarForm();
            }, 0);
        }

        function salvarForm() {
            TableEditableCriticidadPlan.btnClickSalvarForm();
        }

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                // initialize core components
                Metronic.initAjax();
                MyApp.init();

                $timeout(function () {
                    TableEditableCriticidadPlan.init();
                }, 0);

            });
        }

    }
})();
