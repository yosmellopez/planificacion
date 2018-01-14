(function () {
    'use strict';

    angular
            .module('app.widgets')
            .directive('dsInputUsuarioId', dsInputUsuarioId);

    dsInputUsuarioId.$inject = ['$rootScope'];
    function dsInputUsuarioId($rootScope) {

        var directive = {
            link: link,
            restrict: 'A'
        };
        return directive;

        function link(scope, element, attrs) {             
            $(element).val(attrs.usuario);
        }
    }
})();
