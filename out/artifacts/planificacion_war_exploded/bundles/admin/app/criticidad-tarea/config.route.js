(function () {
    'use strict';

    angular.module('app.criticidadTarea')
        .config(routeConfig);

    routeConfig.$inject = ['$stateProvider', 'urlPath'];
    function routeConfig($stateProvider, urlPath) {

        $stateProvider
            .state('criticidadtarea', {
                url: "/criticidad-de-tarea",
                templateUrl: urlPath + "/bundles/admin/app/criticidad-tarea/criticidades.html",
                data: {
                    pageTitle: ' - Criticidad de Tareas'
                },
                controller: "CriticidadTarea",
                controllerAs: 'vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'app',
                            insertBefore: '#ng_load_plugins_before', // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
                            files: [
                                urlPath + '/bundles/admin/app/scripts/table/criticidad-tarea-table-editable.js'
                            ]
                        });
                    }]
                }
            });
    }

})();
