(function () {
    'use strict';

    angular
            .module('app.widgets')
            .directive('dsInputMaskHour', dsInputMaskHour);

    function dsInputMaskHour() {

        var directive = {
            link: link,
            restrict: 'A'
        };
        return directive;

        function link(scope, element, attrs) {
            element.inputmask("hh:mm", {
                "placeholder": "hh:mm"
            });
        }
    }
})();
