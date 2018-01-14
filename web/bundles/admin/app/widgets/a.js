(function () {
    'use strict';

    angular
            .module('app.widgets')
            .directive('a', a);


    function a() {

        var directive = {
            link: link,
            restrict: 'E'
        };
        return directive;

        function link(scope, elem, attrs) {
            if (attrs.ngClick || attrs.href === '' || attrs.href === '#') {
                elem.on('click', function (e) {
                    e.preventDefault(); // prevent link click for above criteria
                });
            }
        }
    }
})();
