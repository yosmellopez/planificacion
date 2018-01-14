(function () {
    'use strict';

    var core = angular.module('app.core');

    core.config(toastrConfig);

    toastrConfig.$inject = ['toastr'];
    function toastrConfig(toastr) {
        toastr.options.timeOut = 4000;
        toastr.options.positionClass = 'toast-top-center';
    }

    var config = {
        appErrorPrefix: '[NG-Planificacion Error] ', //Configure the exceptionHandler decorator
        appTitle: 'Planificación',
        version: '1.0.0'
    };
    core.value('config', config);

    /* Configure ocLazyLoader(refer: https://github.com/ocombe/ocLazyLoad) */
    core.config(ocLazyLoaderConfig);

    ocLazyLoaderConfig.$inject = ['$ocLazyLoadProvider'];
    function ocLazyLoaderConfig($ocLazyLoadProvider) {
        $ocLazyLoadProvider.config({
            // global configs go here
        });
    }


})();
