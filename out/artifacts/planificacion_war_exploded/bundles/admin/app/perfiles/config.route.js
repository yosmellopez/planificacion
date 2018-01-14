(function () {
    'use strict';

    angular.module('app.perfiles')
            .config(perfilesConfig);

    perfilesConfig.$inject = ['$stateProvider', 'urlPath'];
    function perfilesConfig($stateProvider, urlPath) {

        $stateProvider.state('perfiles', {
            url: "/perfiles",
            templateUrl: urlPath + "/bundles/admin/app/perfiles/perfiles.html",
            data: {
                pageTitle: ' - Mis Perfiles'
            },
            controller: "Perfiles",
            controllerAs: 'vm',
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'app',
                            insertBefore: '#ng_load_plugins_before', // load the above css files before a LINK element with this ID. Dynamic CSS files must be loaded between core and theme css files
                            files: [
                                urlPath +'/bundles/admin/app/scripts/table/perfil-table-editable.js'
                            ]
                        });
                    }]
            }
        });
    }

})();
