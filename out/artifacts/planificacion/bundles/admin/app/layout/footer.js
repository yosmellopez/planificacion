(function () {
    'use strict';

    angular
            .module('app.layout')
            .controller('Footer', Footer);
    
    Footer.$inject = ['$scope'];
    function Footer($scope) {        
        var vm = this;
        
        activate();

        function activate() {
            $scope.$on('$includeContentLoaded', function () {
                Layout.initFooter(); // init footer
            });
        }
    }
})();
