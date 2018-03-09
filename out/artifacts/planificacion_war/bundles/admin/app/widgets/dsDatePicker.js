(function () {
    'use strict';

    angular
            .module('app.widgets')
            .directive('dsDatePicker', dsDatePicker);

    function dsDatePicker() {

        var directive = {
            link: link,
            restrict: 'A'
        };
        return directive;

        function link(scope, element, attrs) {
            element.datepicker({
                format: 'dd/mm/yyyy',
                language: 'es',
                todayHighlight: true,
                autoclose: true
            });
        }
    }
})();
