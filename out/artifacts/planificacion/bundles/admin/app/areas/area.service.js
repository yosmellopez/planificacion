(function () {
    'use strict';

    angular.module('app.areas')
            .factory('areaService', areaService);

    areaService.$inject = ['$http', 'logger'];
    function areaService($http, logger) {

        var service = {
            getAreas: getAreas,
            getAreasGerencia: getAreasGerencia
        };

        return service;

        function getAreas() {
            var settings = {

            };

            return $http.get("area/listarTodas", settings)
                    .then(success)
                    .catch(failed);

            function success(data, status, headers, config) {
                service.areas = data.data.areas;
                return service.areas;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

        function getAreasGerencia(idGerencia) {
            var settings = {

            };
            return $http.get("area/listarDeGerencia?gerencia_id=" + idGerencia, settings);
        }

    }

})();

