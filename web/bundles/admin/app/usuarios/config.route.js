(function () {
    'use strict';

    angular.module('app.usuarios')
        .config(usuariosConfig);

    usuariosConfig.$inject = ['$stateProvider', 'urlPath'];
    function usuariosConfig($stateProvider, urlPath) {

        $stateProvider
            .state('usuarios', {
                url: "/usuarios",
                templateUrl: urlPath + "/bundles/admin/app/usuarios/usuarios.html",
                data: {
                    pageTitle: ' - Mis Usuarios'
                },
                controller: "Usuarios",
                controllerAs: 'vm',
                resolve: {
                    perfilesPrepService: perfilesPrepService,
                    gerenciasPrepService: gerenciasPrepService,
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'app',
                            insertBefore: '#ng_load_plugins_before', // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
                            files: [
                                urlPath + '/bundles/admin/js/sha-1.js',
                                urlPath + '/bundles/admin/app/scripts/table/usuario-table-editable.js'
                            ]
                        });
                    }]
                }
            })
            .state('perfil', {
                url: "/usuario/perfil",
                templateUrl: urlPath + "/bundles/admin/app/usuarios/perfil.html",
                data: {pageTitle: ' - Mi Perfil'},
                controller: "Perfil",
                controllerAs: 'vm',
                resolve: {
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'app',
                            insertBefore: '#ng_load_plugins_before', // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
                            files: [
                                urlPath + '/bundles/admin/js/sha-1.js',
                                urlPath + '/bundles/admin/app/scripts/perfil.js'
                            ]
                        });
                    }]
                }
            });

        //Carga los perfiles definicios
        perfilesPrepService.$inject = ['perfilService'];
        function perfilesPrepService(perfilService) {
            return perfilService.getPerfiles();
        }

        //Carga las gerencias
        gerenciasPrepService.$inject = ['gerenciaService'];

        function gerenciasPrepService(gerenciaService) {
            return gerenciaService.getGerencias();
        }
    }

})();
