(function () {
    'use strict';

    angular.module('app.cargos')
        .config(routerConfig);

    routerConfig.$inject = ['$stateProvider', 'urlPath'];

    function routerConfig($stateProvider, urlPath) {

        $stateProvider.state('cargos', {
            url: "/cargos",
            templateUrl: urlPath + "/bundles/admin/app/cargos/cargos.html",
            data: {
                pageTitle: ' - Cargos'
            },
            controller: "Cargos",
            controllerAs: 'vm',
            resolve: {
                areasPrepService: areasPrepService,
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'app',
                        insertBefore: '#ng_load_plugins_before', // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
                        files: [
                            urlPath + '/bundles/admin/app/scripts/table/cargo-table-editable.js'
                        ]
                    });
                }]
            }
        });

        //Carga las areas
        areasPrepService.$inject = ['areaService'];

        function areasPrepService(areaService) {
            return areaService.getAreas();
        }
    }

})();
