(function () {
    'use strict';

    angular.module('app.tablero')
        .config(tableroConfig);

    tableroConfig.$inject = ['$stateProvider', '$urlRouterProvider', 'urlPath'];

    function tableroConfig($stateProvider, $urlRouterProvider, urlPath) {
        $urlRouterProvider.otherwise("/dashboard");

        $stateProvider.state('tablero', {
            url: "/dashboard",
            templateUrl: urlPath + "/bundles/admin/app/tablero/tablero.html",
            data: {
                pageTitle: ' - Lista de Tablero'
            },
            controller: "Tablero",
            controllerAs: 'vm',
            resolve: {
                tableroPrepService: tableroPrepService,
                cargoPrepService: cargoPrepService,
                areaPrepService: areaPrepService,
                gerenciaPrepService: gerenciaPrepService,
                criticidadPrepService: criticidadPrepService,
                planPrepService: planPrepService,
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'app',
                        insertBefore: '#ng_load_plugins_before', // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
                        files: [
                            urlPath + '/bundles/admin/assets/bootstrap-fileinput/bootstrap-fileinput.css',
                            urlPath + '/bundles/admin/assets/bootstrap-fileinput/bootstrap-fileinput.js',
                            urlPath + '/bundles/admin/assets/jqwidgets/jqxsortable.js',
                            urlPath + '/bundles/admin/assets/jqwidgets/jqxkanban.js',
                            urlPath + '/bundles/admin/assets/jqwidgets/jqxdata.js',
                            urlPath + '/bundles/admin/app/scripts/demos.js'
                        ]
                    });
                }]
            }
        });

        //Carga las gerencias
        tableroPrepService.$inject = ['tableroService'];

        function tableroPrepService(tableroService) {
            return tableroService.getTablero();
        }

        //Carga los cargos
        cargoPrepService.$inject = ['cargoService'];

        function cargoPrepService(cargoService) {
            return cargoService.getCargos();
        }

        //Carga los area
        areaPrepService.$inject = ['areaService'];

        function areaPrepService(areaService) {
            return areaService.getAreas();
        }

        //Carga los direcion
        gerenciaPrepService.$inject = ['gerenciaService'];

        function gerenciaPrepService(gerenciaService) {
            return gerenciaService.getGerencias();
        }

        //Carga los niveles de alerta
        criticidadPrepService.$inject = ['criticidadTareaService'];

        function criticidadPrepService(criticidadTareaService) {
            return criticidadTareaService.getCriticidades();
        }

        //Carga los planes
        planPrepService.$inject = ['planService'];

        function planPrepService(planService) {
            return planService.listarPlanes();
        }
    }
})();
