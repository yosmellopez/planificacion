(function () {
    'use strict';

    angular
        .module('app.layout')
        .controller('Sidebar', Sidebar);

    Sidebar.$inject = ['$scope', 'urlPath', 'usuarioService'];
    function Sidebar($scope, urlPath, usuarioService) {
        var vm = this;
        vm.urlPath = urlPath;
        vm.usuario = {};

        activate();

        function activate() {
            $scope.$on('$includeContentLoaded', function () {
                Layout.initSidebar();
            });

            getUsuario().then(function (data) {

            });
        }

        function getUsuario() {
            return usuarioService.getUsuarioActual().then(function (data) {
                vm.usuario = data;
                return vm.usuario;
            });
        }
    }
})();
