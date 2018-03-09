(function () {
    'use strict';

    angular
        .module('app.dashboard')
        .controller('Dashboard', Dashboard);

    Dashboard.$inject = ['$rootScope', '$scope', '$timeout', 'dashboardService', 'usuarioService'];

    function Dashboard($rootScope, $scope, $timeout, dashboardService, usuarioService) {
        var vm = this;

        vm.usuario = usuarioService.usuario;

        vm.salvar = salvar;

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageBodySolid = true;
        $rootScope.settings.layout.pageSidebarClosed = false;

        activate();

        function activate() {

            $scope.$on('$viewContentLoaded', function () {
                // initialize core components
                Metronic.initAjax();
                MyApp.init();

                $timeout(function () {
                    //TableEditableIndex.init(vm.tareas);
                }, 1000);
            });
        }

        function salvar() {
            TableEditableIndex.salvar();
        }
    }
})();
