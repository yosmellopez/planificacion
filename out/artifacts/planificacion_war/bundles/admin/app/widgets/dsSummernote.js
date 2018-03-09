(function () {
    'use strict';

    angular
            .module('app.widgets')
            .directive('dsSummernote', dsSummernote);

    dsSummernote.$inject = ['$rootScope'];
    function dsSummernote($rootScope) {

        var directive = {
            link: link,
            restrict: 'A'
        };
        return directive;

        function link(scope, element, attrs) {
            element.summernote({
                lang: 'es-ES',
                height: 200
            });
        }
    }
})();
