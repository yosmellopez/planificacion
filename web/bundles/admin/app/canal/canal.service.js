(function () {
    'use strict';

    angular.module('app.canal').factory('canalService', canalService);
    canalService.$inject = ['$http', 'logger'];

    function canalService($http, logger) {

        var service = {
            getCanales: getCanales
        };

        return service;

        function getCanales() {
            var settings = {};

            return $http.get("canal/listarTodos", settings)
                    .then(success)
                    .catch(failed);

            function success(response, status, headers, config) {
                service.canales = response.data.canales;
                return service.canales;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

    }

})();

