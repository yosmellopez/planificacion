(function () {
    'use strict';

    angular.module('app.estadoTarea')
        .config(routeConfig);

    routeConfig.$inject = ['$stateProvider', 'urlPath'];
    function routeConfig($stateProvider, urlPath) {

        $stateProvider
            .state('estadotarea', {
                url: "/estados-de-tarea",
                templateUrl: urlPath + "/bundles/admin/app/estado-tarea/estados.html",
                data: {
                    pageTitle: ' - Estados de Tareas'
                },
                controller: "EstadoTarea",
                controllerAs: 'vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'app',
                            insertBefore: '#ng_load_plugins_before', // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
                            files: [
                                urlPath + '/bundles/admin/app/scripts/table/estado-tarea-table-editable.js'
                            ]
                        });
                    }]
                }
            });
    }

})();
