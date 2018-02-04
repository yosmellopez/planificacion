(function () {
    'use strict';

    angular.module('app.areas')
        .config(routerConfig);

    routerConfig.$inject = ['$stateProvider', 'urlPath'];

    function routerConfig($stateProvider, urlPath) {

        $stateProvider.state('gerencias', {
            url: "/gerencias",
            templateUrl: urlPath + "/bundles/admin/app/areas/areas.html",
            data: {
                pageTitle: ' - Unidades'
            },
            controller: "Areas",
            controllerAs: 'vm',
            resolve: {
                gerenciasPrepService: gerenciasPrepService,
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'app',
                        insertBefore: '#ng_load_plugins_before', // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
                        files: [
                            urlPath + '/bundles/admin/app/scripts/table/area-table-editable.js'
                        ]
                    });
                }]
            }
        });

        //Carga las gerencias
        gerenciasPrepService.$inject = ['gerenciaService'];

        function gerenciasPrepService(gerenciaService) {
            return gerenciaService.getGerencias();
        }
    }

})();
