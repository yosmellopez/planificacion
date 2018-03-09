(function () {
    'use strict';

    angular
            .module('app.estadoTarea')
            .controller('EstadoTarea', EstadoTarea);

    EstadoTarea.$inject = ['$rootScope', '$scope', '$timeout', 'urlPath'];

    function EstadoTarea($rootScope, $scope, $timeout, urlPath) {
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
                TableEditableEstadoTarea.btnClickNuevo();
            }, 0);
        }

        function eliminar() {
            TableEditableEstadoTarea.btnClickEliminar();
        }

        function modalEliminar() {
            TableEditableEstadoTarea.btnClickModalEliminar();
        }

        function modalEliminarSeleccion() {
            TableEditableEstadoTarea.btnClickModalEliminarSeleccion();
        }

        function cerrarForm() {
            $timeout(function () {
                TableEditableEstadoTarea.btnClickCerrarForm();
            }, 0);
        }

        function salvarForm() {
            TableEditableEstadoTarea.btnClickSalvarForm($scope.var);
        }

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                // initialize core components
                Metronic.initAjax();
                MyApp.init();
                $timeout(function () {
                    TableEditableEstadoTarea.init($scope);
                }, 0);

            });
        }

    }
})();
