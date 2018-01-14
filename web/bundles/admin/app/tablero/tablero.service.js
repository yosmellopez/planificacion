(function () {
    'use strict';

    angular.module('app.tablero')
        .factory('tableroService', tableroService);

    tableroService.$inject = ['$http', 'logger'];

    function tableroService($http, logger) {

        var service = {
            getTablero: getTablero,
            getTableroArbol: getTableroArbol,
            getMisTareas: getMisTareas,
            moverTarea: moverTarea,
            buscarTablero: buscarTablero,
            buscarTareas: buscarTareas,
            buscarTarea: buscarTarea
        };

        return service;

        function getTablero() {
            var settings = {};

            return $http.get("tarea/listarTableros", settings).then(success).catch(failed);

            function success(response, status, headers, config) {
                service.tablero = response.data.board;
                return service.tableros;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

        function getMisTareas() {
            var settings = {};

            return $http.get("tablero/misTareas", settings).then(success).catch(failed);

            function success(data, status, headers, config) {
                service.tableros = data.data.tableros;
                return service.tableros;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

        function getTableroArbol() {
            var settings = {};
            return $http.get("tarea/listarDiagrama", settings).then(success).catch(failed);

            function success(data, status, headers, config) {
                service.tableros = data.data.tableros;
                return service.tableros;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

        function moverTarea(idTarea, idEstado) {
            var params = {};
            return $http.post("tarea/moverTarea/" + idTarea + "/" + idEstado, params).then(success).catch(failed);

            function success(response, status, headers, config) {
                toastr.success(response.data.message, "Exito !!!");
                return response.data;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

        function buscarTablero(parametros) {
            var settings = {
                params: parametros
            };
            return $http.get("tarea/listarTableros", settings);
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

        function buscarTarea(idTarea, idPlan) {
            var settings = {
                tarea_id: idTarea
            };

            return $http.post("tarea/cargarDatos?tarea_id=" + idTarea + "&plan_id=" + idPlan, settings);
        }
    }
})();

