(function () {
    'use strict';

    angular
            .module('app.widgets')
            .directive('dsDateTimePicker', dsDateTimePicker);

    function dsDateTimePicker() {

        var directive = {
            link: link,
            restrict: 'A'
        };
        return directive;

        function link(scope, element, attrs) {
            element.datetimepicker({
                format: 'dd/mm/yyyy hh:ii:ss',
                language: 'es',
                /*weekStart: 1,
                autoclose: 1,
                todayHighlight: 1,
                startView: 2,
                forceParse: 0,
                showMeridian: 1*/
            });

        }
    }
})();
