(function () {
    'use strict';

    angular
            .module('app.layout')
            .controller('ThemePanel', ThemePanel);
    
    ThemePanel.$inject = ['$scope'];
    function ThemePanel($scope) {        
        var vm = this;
        
        activate();

        function activate() {
            $scope.$on('$includeContentLoaded', function () {
                Demo.init(); // init theme panel
            });
        }
    }
})();
