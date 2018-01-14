(function () {
    'use strict';

    angular.module('app.perfiles')
            .factory('perfilService', perfilService);

    perfilService.$inject = ['$http', 'logger'];
    function perfilService($http, logger) {

        var service = {
            getPerfiles: getPerfiles
        };

        return service;

        function getPerfiles() {
            var settings = {
                
            };

            return $http.get("perfil/listarTodosPerfiles", settings)
                    .then(success)
                    .catch(failed);

            function success(data, status, headers, config) {   
                service.perfiles = data.data.perfiles;
                return service.perfiles;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

    }

})();

