(function () {
    'use strict';

    angular
        .module('app.criticidadTarea')
        .controller('CriticidadTarea', CriticidadTarea);

    CriticidadTarea.$inject = ['$rootScope', '$scope', '$timeout', 'urlPath'];

    function CriticidadTarea($rootScope, $scope, $timeout, urlPath) {
        var vm = this;
        vm.urlPath = urlPath;
        $scope.var = "";

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
                TableEditableCriticidadTarea.btnClickNuevo();
            }, 0);
        }

        function eliminar() {
            TableEditableCriticidadTarea.btnClickEliminar();
        }

        function modalEliminar() {
            TableEditableCriticidadTarea.btnClickModalEliminar();
        }

        function modalEliminarSeleccion() {
            TableEditableCriticidadTarea.btnClickModalEliminarSeleccion();
        }

        function cerrarForm() {
            $timeout(function () {
                TableEditableCriticidadTarea.btnClickCerrarForm();
            }, 0);
        }

        function salvarForm() {
            TableEditableCriticidadTarea.btnClickSalvarForm();
        }

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                // initialize core components
                Metronic.initAjax();
                MyApp.init();

                $timeout(function () {
                    TableEditableCriticidadTarea.init($scope);
                }, 0);

            });
        }

    }
})();
