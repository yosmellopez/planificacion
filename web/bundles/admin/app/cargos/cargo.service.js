(function () {
    'use strict';

    angular.module('app.cargos')
            .factory('cargoService', cargoService);

    cargoService.$inject = ['$http', 'logger'];
    function cargoService($http, logger) {

        var service = {
            getCargos: getCargos,
            getCargosGerencia: getCargosGerencia,
            getCargosArea: getCargosArea
        };

        return service;

        function getCargos() {
            var settings = {

            };

            return $http.get("cargo/listarTodos", settings)
                    .then(success)
                    .catch(failed);

            function success(data, status, headers, config) {
                service.cargos = data.data.cargos;
                return service.cargos;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

        function getCargosGerencia(idGerencia) {
            var settings = {

            };
            return $http.get("cargo/listarDeGerencia?gerencia_id=" + idGerencia, settings);

        }

        function getCargosArea(idArea) {
            var settings = {

            };
            return $http.get("cargo/listarDeArea?area_id=" + idArea, settings);
        }

    }

})();

