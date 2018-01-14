(function () {
    'use strict';

    angular
            .module('app.widgets')
            .directive('dsInputRating', dsInputRating);

    function dsInputRating() {

        var directive = {
            link: link,
            restrict: 'A'
        };
        return directive;

        function link(scope, element, attrs) {

            element.rating();
            $(element).each(function () {
                $('<span class="label label-default"></span>')
                    .text($(this).val() || ' ')
                    .insertAfter(this);
            });
            $(element).on('change', function () {
                $(this).next('.label').text($(this).val());
            });
        }
    }
})();
