(function () {
    'use strict';

    angular.module('app.gerencias')
            .factory('gerenciaService', gerenciaService);

    gerenciaService.$inject = ['$http', 'logger'];
    function gerenciaService($http, logger) {

        var service = {
            getGerencias: getGerencias
        };

        return service;

        function getGerencias() {
            var settings = {
                
            };

            return $http.get("gerencia/listarTodas", settings)
                    .then(success)
                    .catch(failed);

            function success(data, status, headers, config) {   
                service.gerencias = data.data.gerencias;
                return service.gerencias;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

    }

})();

