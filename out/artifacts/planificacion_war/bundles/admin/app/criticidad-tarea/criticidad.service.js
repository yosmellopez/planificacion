(function () {
    'use strict';

    angular.module('app.criticidadTarea')
        .factory('criticidadTareaService', criticidadTareaService);

    criticidadTareaService.$inject = ['$http', 'logger'];

    function criticidadTareaService($http, logger) {

        var service = {
            getCriticidades: getCriticidades
        };

        return service;

        function getCriticidades() {
            var settings = {};

            return $http.get("criticidad-tarea/listarTodos", settings)
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

