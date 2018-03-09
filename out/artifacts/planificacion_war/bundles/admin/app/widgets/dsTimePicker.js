(function () {
    'use strict';

    angular
            .module('app.widgets')
            .directive('dsTimePicker', dsTimePicker);

    function dsTimePicker() {

        var directive = {
            link: link,
            restrict: 'A'
        };
        return directive;

        function link(scope, element, attrs) {
            element.timepicker({
                autoclose: true,
                minuteStep: 30,
                showSeconds: false,
                showMeridian: false
            });
            element.inputmask("hh:mm", {
                "placeholder": "hh:mm"
            });

            // handle input group button click
            $('.timepicker').parent('.input-group').on('click', '.input-group-addon', function(e){
                e.preventDefault();
                $(this).parent('.input-group').find('.timepicker').timepicker('showWidget');
            });

        }
    }
})();
