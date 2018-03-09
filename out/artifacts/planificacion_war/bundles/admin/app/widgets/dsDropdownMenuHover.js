(function () {
    'use strict';

    angular
            .module('app.widgets')
            .directive('dsDropdownMenuHover', dsDropdownMenuHover);
    
    function dsDropdownMenuHover() {

        var directive = {
            link: link,
            restrict: 'A'
        };
        return directive;

        function link(scope, element, attrs) {
            element.dropdownHover();
        }
    }
})();
