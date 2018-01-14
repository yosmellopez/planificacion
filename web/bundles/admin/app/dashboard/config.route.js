(function () {
    'use strict';

    angular.module('app.dashboard')
        .config(dashboardConfig);

    dashboardConfig.$inject = ['$stateProvider', '$urlRouterProvider', 'urlPath'];
    function dashboardConfig($stateProvider, $urlRouterProvider, urlPath) {
        // Redirect any unmatched url
        $urlRouterProvider.otherwise("/dashboard");

        $stateProvider.state('dashboard', {
            url: "/dashboard",
            templateUrl: urlPath + "/bundles/admin/app/dashboard/dashboard.html",
            data: {
                pageTitle: ' - Dashboard',
                funcionId: '1'
            },
            controller: "Dashboard",
            controllerAs: 'vm',
            resolve: {
                //tareasPrepService: tareasPrepService,
                usuarioPrepService: usuarioPrepService,
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'app',
                        insertBefore: '#ng_load_plugins_before', // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
                        files: [
                            urlPath + '/bundles/admin/app/scripts/table/index-table-editable.js'
                        ]
                    });
                }]
            }
        });


        //Carga el usuario actual
        usuarioPrepService.$inject = ['usuarioService'];
        function usuarioPrepService(usuarioService) {
            return usuarioService.getUsuarioActual();
        }

        //Carga los tareas definidos
        tareasPrepService.$inject = ['tareaService'];
        function tareasPrepService(tareaService) {
            return tareaService.getTareasArbol();
        }
    }

})();
