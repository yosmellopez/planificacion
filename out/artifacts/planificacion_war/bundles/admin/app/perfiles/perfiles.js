(function () {
    'use strict';

    angular
        .module('app.perfiles')
        .controller('Perfiles', Perfiles);

    Perfiles.$inject = ['$rootScope', '$scope', '$timeout', 'urlPath', 'usuarioService'];

    function Perfiles($rootScope, $scope, $timeout, urlPath, usuarioService) {
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
                TableEditablePerfiles.btnClickNuevo();
            }, 0);

        }

        function eliminar() {
            TableEditablePerfiles.btnClickEliminar();
        }

        function modalEliminar() {
            TableEditablePerfiles.btnClickModalEliminar();
        }

        function modalEliminarSeleccion() {
            TableEditablePerfiles.btnClickModalEliminarSeleccion();
        }

        function cerrarForm() {
            $timeout(function () {
                TableEditablePerfiles.btnClickCerrarForm();
            }, 0);
        }

        function salvarForm() {
            TableEditablePerfiles.btnClickSalvarForm();
        }

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                // initialize core components
                Metronic.initAjax();
                MyApp.init();

                $timeout(function () {
                    TableEditablePerfiles.init();
                }, 0);

            });
        }

    }
})();
