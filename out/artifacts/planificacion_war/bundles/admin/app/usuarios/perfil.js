(function () {
    'use strict';

    angular
            .module('app.usuarios')
            .controller('Perfil', Perfil);

    Perfil.$inject = ['$rootScope', '$scope', 'usuarioService'];

    function Perfil($rootScope, $scope, usuarioService) {
        var vm = this;  
        
        vm.usuario = {};
        if (usuarioService.usuario) {
            vm.usuario = usuarioService.usuario;
        }
        
        vm.modificar = modificar;
        vm.cerrarForm = cerrarForm;
        vm.salvarForm = salvarForm;

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageBodySolid = true;
        $rootScope.settings.layout.pageSidebarClosed = false;

        activate();
        
        function modificar() {
            TablePerfil.btnClickModificar();
        }
        
        function cerrarForm() {
            TablePerfil.btnClickCerrarForm();
        }
        function salvarForm() {
            TablePerfil.btnClickSalvarForm();
        }

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                // initialize core components
                Metronic.initAjax();
                Layout.setSidebarMenuActiveLink('set', $('#sidebar_menu_link_home'));
                MyApp.init();
            });

            if (!usuarioService.usuario) {
                Metronic.blockUI({
                    target: '#perfil-usuario',
                    animate: true
                });

                return getUsuario().then(function (data) {
                    Metronic.unblockUI('#perfil-usuario');
                });
            }
        }

        function getUsuario() {
            return usuarioService.getUsuarioActual().then(function (data) {
                vm.usuario = data;
                return vm.usuario;
            });
        }

    }
})();
