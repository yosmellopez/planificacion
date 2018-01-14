(function () {
    'use strict';

    angular.module('app.tareas')
            .factory('tareaService', tareaService);

    tareaService.$inject = ['$http', 'logger'];
    function tareaService($http, logger) {

        var service = {
            getTareas: getTareas,
            getTareasArbol: getTareasArbol,
            buscarTareas: buscarTareas
        };

        return service;

        function getTareas() {
            var settings = {

            };

            return $http.get("tarea/listarTodas", settings)
                    .then(success)
                    .catch(failed);

            function success(data, status, headers, config) {
                service.tareas = data.data.tareas;
                return service.tareas;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

        function getTareasArbol() {
            var settings = {

            };

            return $http.get("tarea/listarDiagrama", settings)
                    .then(success)
                    .catch(failed);

            function success(data, status, headers, config) {
                service.tareas = data.data.tareas;
                return service.tareas;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

        function buscarTareas(idTarea) {
            var settings = {
                tarea_id: idTarea
            };

            return $http.get("tarea/cargarDatos", settings)
                    .then(success)
                    .catch(failed);

            function success(response, status, headers, config) {
                service.tarea = response.data.tarea;
                return service.tarea;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

    }

})();

