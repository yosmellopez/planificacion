(function () {
    'use strict';

    angular.module('app.gerencias')
        .config(routerConfig);

    routerConfig.$inject = ['$stateProvider', 'urlPath'];

    function routerConfig($stateProvider, urlPath) {

        $stateProvider.state('direcciones', {
            url: "/direcciones",
            templateUrl: urlPath + "/bundles/admin/app/gerencias/gerencias.html",
            data: {
                pageTitle: ' - Direcciones'
            },
            controller: "Gerencias",
            controllerAs: 'vm',
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'app',
                        insertBefore: '#ng_load_plugins_before', // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
                        files: [
                            urlPath + '/bundles/admin/app/scripts/table/gerencia-table-editable.js'
                        ]
                    });
                }]
            }
        });
    }

})();
