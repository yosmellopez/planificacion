(function () {
    'use strict';

    angular.module('app.estadoPlan')
        .config(routeConfig);

    routeConfig.$inject = ['$stateProvider', 'urlPath'];
    function routeConfig($stateProvider, urlPath) {

        $stateProvider
            .state('estadoplan', {
                url: "/estados-de-plan",
                templateUrl: urlPath + "/bundles/admin/app/estado-plan/estados.html",
                data: {
                    pageTitle: ' - Estados de Plan'
                },
                controller: "EstadoPlan",
                controllerAs: 'vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'app',
                            insertBefore: '#ng_load_plugins_before', // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
                            files: [
                                urlPath + '/bundles/admin/app/scripts/table/estado-plan-table-editable.js'
                            ]
                        });
                    }]
                }
            });
    }

})();
