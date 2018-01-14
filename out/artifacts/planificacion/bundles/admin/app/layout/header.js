(function () {
    'use strict';

    angular
        .module('app.layout')
        .controller('Header', Header);

    Header.$inject = ['$scope', 'urlPath', 'usuarioService'];
    function Header($scope, urlPath, usuarioService) {
        var vm = this;
        vm.urlPath = urlPath;
        vm.usuario = {};

        activate();

        function activate() {
            $scope.$on('$includeContentLoaded', function () {
                Layout.initHeader(); // init header
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
