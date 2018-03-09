(function () {
    'use strict';

    angular.module('app.planes').config(routeConfig);

    routeConfig.$inject = ['$stateProvider', 'urlPath'];

    function routeConfig($stateProvider, urlPath) {

        $stateProvider.state('planes', {
            url: "/planes",
            templateUrl: urlPath + "/bundles/admin/app/planes/planes.html",
            data: {
                pageTitle: ' - Mis Planes'
            },
            controller: "Planes",
            controllerAs: 'vm',
            resolve: {
                criticidadesPrepService: criticidadesPrepService,
                estadosPrepService: estadosPrepService,
                estadosTareasPrepService: estadosTareasPrepService,
                planesPrepService: planesPrepService,
                criticidadesTareasPrepService: criticidadesTareasPrepService,
                gerenciasPrepService: gerenciasPrepService,
                canalPrepService: canalPrepService,
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'app',
                            insertBefore: '#ng_load_plugins_before', // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
                            files: [
                                urlPath + '/bundles/admin/assets/bootstrap-fileinput/bootstrap-fileinput.css',
                                urlPath + '/bundles/admin/assets/bootstrap-fileinput/bootstrap-fileinput.js',
                                urlPath + '/bundles/admin/app/scripts/OrthogonalLinkReshapingTool.js',
                                urlPath + '/bundles/admin/app/scripts/LinkShiftingTool.js',
                                urlPath + '/bundles/admin/app/scripts/table/plan-table-editable.js'
                            ]
                        });
                    }]
            }
        });
        $stateProvider.state('plan-view', {
            url: "/view-plan",
            templateUrl: urlPath + "/bundles/admin/app/planes/plan-view.html",
            data: {
                pageTitle: 'Planes'
            },
            controller: "PlanesList",
            controllerAs: 'vm',
            resolve: {
                planPrepService: planPrepService,
                cargoPrepService: cargoPrepService,
                areaPrepService: areaPrepService,
                gerenciaPrepService: gerenciaPrepService,
                criticidadPrepService: criticidadPrepService
            }
        });
        $stateProvider.state('plan-diagrama', {
            url: "/diagrama-planes",
            templateUrl: urlPath + "/bundles/admin/app/planes/plan-diagrama.html",
            data: {
                pageTitle: 'Planes'
            },
            controller: "PlanesDiagrama",
            controllerAs: 'vm',
            resolve: {
                planPrepService: planPrepService,
                criticidadesTareasPrepService: criticidadesTareasPrepService,
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'app',
                            insertBefore: '#ng_load_plugins_before', // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
                            files: [
                                urlPath + '/bundles/admin/app/scripts/OrthogonalLinkReshapingTool.js',
                                urlPath + '/bundles/admin/app/scripts/LinkShiftingTool.js'
                            ]
                        });
                    }]
            }
        });

        authorizationPrepService.$inject = ['authorizationService'];

        function authorizationPrepService(authorizationService) {
            authorizationService.getAutheticatedUser().then(function (resp) {
                if (resp.data.success) {
                    var usuario = resp.data.usuario;
                    authorizationService.storeUser(usuario);
                } else {
                    window.location.href = urlPath + "logout";
                }
            }).catch(function (error) {
                window.location.href = urlSitio + "logout";
                logger.error('Error !!' + error.data);
            });
        }

        //Carga las gerencias
        gerenciasPrepService.$inject = ['gerenciaService'];

        function gerenciasPrepService(gerenciaService) {
            return gerenciaService.getGerencias();
        }

        //Carga las criticidades
        criticidadesPrepService.$inject = ['criticidadPlanService'];

        function criticidadesPrepService(criticidadPlanService) {
            return criticidadPlanService.getCriticidades();
        }

        //Carga los estados
        estadosPrepService.$inject = ['estadoPlanService'];

        function estadosPrepService(estadoPlanService) {
            return estadoPlanService.getEstados();
        }

        //Carga los estados
        estadosTareasPrepService.$inject = ['estadoTareaService'];

        function estadosTareasPrepService(estadoTareaService) {
            return estadoTareaService.getEstados();
        }

        //Carga los planes
        planesPrepService.$inject = ['planService'];

        function planesPrepService(planService) {
            planService.getTareas();
            return planService.getPlanes();
        }


        //Carga los niveles de alerta
        criticidadPrepService.$inject = ['criticidadTareaService'];

        function criticidadPrepService(criticidadTareaService) {
            return criticidadTareaService.getCriticidades();
        }

        //Carga las criticidades de tareas
        criticidadesTareasPrepService.$inject = ['criticidadTareaService'];

        function criticidadesTareasPrepService(criticidadTareaService) {
            return criticidadTareaService.getCriticidades();
        }

        //Carga los planes iniciales
        planPrepService.$inject = ['planService'];

        function planPrepService(planService) {
            return planService.listarPlanes();
        }

        //Carga los canales
        canalPrepService.$inject = ['canalService'];

        function canalPrepService(canalService) {
            return canalService.getCanales();
        }

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
    }
})();
