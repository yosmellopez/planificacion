(function () {
    'use strict';

    angular.module('app.estadoTarea')
        .factory('estadoTareaService', estadoTareaService);

    estadoTareaService.$inject = ['$http', 'logger'];

    function estadoTareaService($http, logger) {

        var service = {
            getEstados: getEstados
        };

        return service;

        function getEstados() {
            var settings = {};

            return $http.get("estado-tarea/listarTodos", settings)
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

