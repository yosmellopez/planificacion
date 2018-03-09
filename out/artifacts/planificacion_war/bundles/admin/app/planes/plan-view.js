(function () {
    'use strict';
    angular
            .module('app.planes')
            .controller('PlanesList', PlanesList);
    
    PlanesList.$inject = ['$rootScope', '$scope', '$timeout', 'planService'];
    
    function PlanesList($rootScope, $scope, $timeout, planService) {
        var vm = this;
        $rootScope.settings.layout.pageBodySolid = true;
        $rootScope.settings.layout.pageSidebarClosed = false;
        vm.planes = planService.planes;
    }
});