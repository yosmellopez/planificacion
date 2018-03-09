(function () {
    'use strict';

    angular
        .module('app.usuarios')
        .controller('Usuarios', Usuarios);

    Usuarios.$inject = ['$rootScope', '$scope', 'perfilService', 'urlPath', '$timeout', 'gerenciaService', 'usuarioService'];

    function Usuarios($rootScope, $scope, perfilService, urlPath, $timeout, gerenciaService, usuarioService) {
        var vm = this;
        vm.urlPath = urlPath;

        vm.perfiles = perfilService.perfiles;
        vm.gerencias = gerenciaService.gerencias;
        vm.usuarios = usuarioService.usuarios;

        vm.nuevo = nuevo;
        vm.eliminar = eliminar;
        vm.modalEliminar = modalEliminar;
        vm.modalEliminarSeleccion = modalEliminarSeleccion;

        vm.cerrarForm = cerrarForm;
        vm.salvarForm = salvarForm;
        $scope.titular = 'false';
        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageBodySolid = true;
        $rootScope.settings.layout.pageSidebarClosed = false;

        activate();

        function nuevo() {
            $timeout(function () {
                $scope.titular = 'false';
                $('#titular').prop('checked', false);
                $('#no-titular').prop('checked', true);
                jQuery.uniform.update('#titular');
                jQuery.uniform.update('#no-titular');
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
                    TableEditableUsuarios.init(vm);
                }, 0);

            });
        }

        vm.cambiarValor = function (valor) {
            $scope.titular = "" + valor + "";
        }

        vm.getValor = function () {
            return $scope.titular;
        }
    }
})();
