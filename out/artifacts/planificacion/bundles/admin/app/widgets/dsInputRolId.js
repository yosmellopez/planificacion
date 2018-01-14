(function () {
    'use strict';

    angular
            .module('app.widgets')
            .directive('dsInputRolId', dsInputRolId);

    dsInputRolId.$inject = ['$rootScope'];
    function dsInputRolId($rootScope) {

        var directive = {
            link: link,
            restrict: 'A'
        };
        return directive;

        function link(scope, element, attrs) {            
            $(element).val(attrs.rol);
        }
    }
})();
