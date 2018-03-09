(function () {
    'use strict';

    angular.module('app.criticidadPlan')
        .factory('criticidadPlanService', criticidadPlanService);

    criticidadPlanService.$inject = ['$http', 'logger'];

    function criticidadPlanService($http, logger) {

        var service = {
            getCriticidades: getCriticidades
        };

        return service;

        function getCriticidades() {
            var settings = {};

            return $http.get("criticidad-plan/listarTodos", settings)
                .then(success)
                .catch(failed);

            function success(data, status, headers, config) {
                service.criticidades = data.data.criticidades;
                return service.criticidades;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

    }

})();

