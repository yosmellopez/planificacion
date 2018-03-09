(function () {
    'use strict';

    angular
            .module('app.layout')
            .controller('App', App);

    App.$inject = ['$scope', '$rootScope'];
    function App($scope, $rootScope) {
        var vm = this;

        activate();

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                Metronic.initComponents(); // init core components
                //Layout.init(); //  Init entire layout(header, footer, sidebar, etc) on page load if the partials included in server side instead of loading with ng-include directive 
            });
        }
    }
})();
