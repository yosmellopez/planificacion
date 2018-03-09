(function () {
    'use strict';

    angular.module('app.estadoPlan')
        .factory('estadoPlanService', estadoPlanService);

    estadoPlanService.$inject = ['$http', 'logger'];

    function estadoPlanService($http, logger) {

        var service = {
            getEstados: getEstados
        };

        return service;

        function getEstados() {
            var settings = {};

            return $http.get("estado-plan/listarTodos", settings)
                .then(success)
                .catch(failed);

            function success(data, status, headers, config) {
                service.estados = data.data.estados;
                return service.estados;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

    }

})();

