/* global toastr:false, moment, urlSitio:false */
(function () {
    'use strict';
    angular.module('app.core')
            .constant('toastr', toastr)
            .constant('urlPath', urlSitio);
})();
