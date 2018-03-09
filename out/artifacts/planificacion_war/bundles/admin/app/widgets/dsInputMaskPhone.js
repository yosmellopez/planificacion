(function () {
    'use strict';

    angular
            .module('app.widgets')
            .directive('dsInputMaskPhone', dsInputMaskPhone);

    function dsInputMaskPhone() {

        var directive = {
            link: link,
            restrict: 'A'
        };
        return directive;

        function link(scope, element, attrs) {
            element.inputmask("mask", {
                "mask": "(+56) 999-999-999"
            });
        }
    }
})();
