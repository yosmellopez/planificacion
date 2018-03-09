(function () {
    'use strict';

    angular
            .module('app.widgets')
            .directive('dsSelect2', dsSelect2);

    dsSelect2.$inject = ['$rootScope'];
    function dsSelect2($rootScope) {

        var directive = {
            link: link,
            restrict: 'A'
        };
        return directive;

        function link(scope, element, attrs) {
            element.select2({
                allowClear: true
            });
        }
    }
})();
