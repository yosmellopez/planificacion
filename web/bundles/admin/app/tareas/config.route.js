(function () {
    'use strict';

    angular.module('app.tareas')
        .config(tareasConfig);

    tareasConfig.$inject = ['$stateProvider', 'urlPath'];

    function tareasConfig($stateProvider, urlPath) {

        $stateProvider.state('tareas', {
            url: "/tareas",
            templateUrl: urlPath + "/bundles/admin/app/tareas/tareas.html",
            data: {
                pageTitle: ' - Lista de Tareas'
            },
            controller: "Tareas",
            controllerAs: 'vm',
            resolve: {
                criticidadesPrepService: criticidadesPrepService,
                estadosPrepService: estadosPrepService,
                estadosTareasPrepService: estadosTareasPrepService,
                criticidadesTareasPrepService: criticidadesTareasPrepService,
                gerenciasPrepService: gerenciasPrepService,
                areasPrepService: areasPrepService,
                cargosPrepService: cargosPrepService,
                canalPrepService: canalPrepService,
                usuarioPrepService: usuarioPrepService,
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'app',
                        insertBefore: '#ng_load_plugins_before', // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
                        files: [
                            urlPath + '/bundles/admin/assets/bootstrap-fileinput/bootstrap-fileinput.css',
                            urlPath + '/bundles/admin/assets/bootstrap-fileinput/bootstrap-fileinput.js',
                            urlPath + '/bundles/admin/app/scripts/table/tarea-table-editable.js'
                        ]
                    });
                }]
            }
        });
        $stateProvider.state('documentos', {
            url: "/documentos",
            templateUrl: urlPath + "/bundles/admin/app/tareas/documentos-tareas.html",
            data: {
                pageTitle: ' - Documentos de Tareas'
            },
            controller: "TareasDocumentos",
            controllerAs: 'vm',
            resolve: {
                criticidadesTareasPrepService: criticidadesTareasPrepService,
                gerenciasPrepService: gerenciasPrepService,
                areasPrepService: areasPrepService,
                cargosPrepService: cargosPrepService,
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'app',
                        insertBefore: '#ng_load_plugins_before', // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
                        files: [
                            urlPath + '/bundles/admin/app/scripts/table/tarea-documento.js'
                        ]
                    });
                }]
            }
        });
        $stateProvider.state('documentos-catalogo', {
            url: "/documentos-catalogo",
            templateUrl: urlPath + "/bundles/admin/app/tareas/documentos-tareas-catalogo.html",
            data: {
                pageTitle: ' - Documentos de Tareas'
            },
            controller: "TareasDocumentosCatalogo",
            controllerAs: 'vm',
            resolve: {
                criticidadesTareasPrepService: criticidadesTareasPrepService,
                gerenciasPrepService: gerenciasPrepService,
                areasPrepService: areasPrepService,
                cargosPrepService: cargosPrepService,
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'app',
                        insertBefore: '#ng_load_plugins_before', // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
                        files: [
                            urlPath + '/bundles/admin/app/scripts/table/tarea-documento-catalogo.js'
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

        //Carga las areas
        areasPrepService.$inject = ['areaService'];

        function areasPrepService(areaService) {
            return areaService.getAreas();
        }

        //Carga las areas
        cargosPrepService.$inject = ['cargoService'];

        function cargosPrepService(cargoService) {
            return cargoService.getCargos();
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

        //Carga las criticidades de tareas
        criticidadesTareasPrepService.$inject = ['criticidadTareaService'];

        function criticidadesTareasPrepService(criticidadTareaService) {
            return criticidadTareaService.getCriticidades();
        }

        //Carga los canales
        canalPrepService.$inject = ['canalService'];

        function canalPrepService(canalService) {
            return canalService.getCanales();
        }

        //Carga los usuarios
        usuarioPrepService.$inject = ['usuarioService'];

        function usuarioPrepService(usuarioService) {
            return usuarioService.getUsuarioActual();
        }
    }

})();
