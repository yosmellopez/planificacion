(function () {
    'use strict';

    angular
            .module('app.widgets')
            .directive('dsInputMaskDate', dsInputMaskDate);

    function dsInputMaskDate() {

        var directive = {
            link: link,
            restrict: 'A'
        };
        return directive;

        function link(scope, element, attrs) {
            element.inputmask("d/m/y", {
                "placeholder": "dd/mm/yyyy"
            });
        }
    }
})();
