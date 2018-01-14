(function () {
    'use strict';

    angular
        .module('app.usuarios')
        .controller('Usuarios', Usuarios);

    Usuarios.$inject = ['$rootScope', '$scope', 'perfilService', 'urlPath',
        '$timeout', 'gerenciaService'];

    function Usuarios($rootScope, $scope, perfilService, urlPath,
                      $timeout, gerenciaService) {
        var vm = this;
        vm.urlPath = urlPath;

        vm.perfiles = perfilService.perfiles;
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
                TableEditableUsuarios.btnClickNuevo();
            }, 0);

        }

        function eliminar() {
            TableEditableUsuarios.btnClickEliminar();
        }

        function modalEliminar() {
            TableEditableUsuarios.btnClickModalEliminar();
        }

        function modalEliminarSeleccion() {
            TableEditableUsuarios.btnClickModalEliminarSeleccion();
        }

        function cerrarForm() {
            $timeout(function () {
                TableEditableUsuarios.btnClickCerrarForm();
            }, 0);
        }

        function salvarForm() {
            TableEditableUsuarios.btnClickSalvarForm();
        }

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                // initialize core components
                Metronic.initAjax();
                MyApp.init();

                $timeout(function () {
                    TableEditableUsuarios.init();
                }, 0);

            });
        }

    }
})();
