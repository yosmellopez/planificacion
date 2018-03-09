(function () {
    'use strict';

    angular
            .module('app.widgets')
            .directive('dsInputMiniColors', dsInputMiniColors);

    function dsInputMiniColors() {

        var directive = {
            link: link,
            restrict: 'A'
        };
        return directive;

        function link(scope, element, attrs) {

            element.minicolors({
                control: attrs.control || 'hue',
                defaultValue: attrs.defaultValue || '',
                inline: attrs.inline === 'true',
                letterCase: attrs.letterCase || 'lowercase',
                opacity: attrs.opacity,
                position: attrs.position || 'bottom left',
                change: function(hex, opacity) {
                    if (!hex) return;
                    if (opacity) hex += ', ' + opacity;
                    if (typeof console === 'object') {
                        console.log(hex);
                    }
                },
                theme: 'bootstrap'
            });
        }
    }
})();
