(function () {
    'use strict';

    angular.module('app.criticidadPlan')
        .config(routeConfig);

    routeConfig.$inject = ['$stateProvider', 'urlPath'];
    function routeConfig($stateProvider, urlPath) {

        $stateProvider
            .state('criticidadplan', {
                url: "/criticidad-de-plan",
                templateUrl: urlPath + "/bundles/admin/app/criticidad-plan/criticidades.html",
                data: {
                    pageTitle: ' - Criticidad de Planes'
                },
                controller: "CriticidadPlan",
                controllerAs: 'vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'app',
                            insertBefore: '#ng_load_plugins_before', // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
                            files: [
                                urlPath + '/bundles/admin/app/scripts/table/criticidad-plan-table-editable.js'
                            ]
                        });
                    }]
                }
            });
    }

})();
