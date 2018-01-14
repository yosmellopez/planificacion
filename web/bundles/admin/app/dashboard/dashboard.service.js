(function () {
    'use strict';

    angular.module('app.dashboard')
            .factory('dashboardService', dashboardService);

    dashboardService.$inject = ['$http', 'logger'];
    function dashboardService($http, logger) {

        var service = {
            getStats: getStats
        };

        return service;

        function getStats() {
            var settings = {
                
            };

            return $http.get("dashboard/stats", settings)
                    .then(success)
                    .catch(failed);

            function success(data, status, headers, config) {   
                service.stats = data.data.stats;
                return service.stats;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

    }

})();

