(function () {
    'use strict';

    angular
            .module('app.widgets')
            .directive('dsSwitch', dsSwitch);

    dsSwitch.$inject = ['$rootScope'];
    function dsSwitch($rootScope) {

        var directive = {
            link: link,
            restrict: 'A'
        };
        return directive;

        function link(scope, element, attrs) {
            element.bootstrapSwitch();
        }
    }
})();
