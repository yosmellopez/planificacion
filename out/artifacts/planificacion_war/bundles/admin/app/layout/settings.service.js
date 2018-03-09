(function () {
    'use strict';

    angular.module('app.layout')
            .factory('settings', settings);

    settings.$inject = ['$rootScope'];
    function settings($rootScope) {

        $rootScope.settings = service;

        var service = {
            layout: {
                pageSidebarClosed: false, // sidebar menu state
                pageBodySolid: false, // solid body color state
                pageAutoScrollOnLoad: 1000 // auto scroll to top on page load
            },
            layoutImgPath: Metronic.getAssetsPath() + 'admin/layout/img/',
            layoutCssPath: Metronic.getAssetsPath() + 'admin/layout/css/'
        };
        
        return service;

    }  

})();

