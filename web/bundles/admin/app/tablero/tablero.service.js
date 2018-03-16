(function () {
    'use strict';

    angular.module('app.tablero')
            .factory('tableroService', tableroService);

    tableroService.$inject = ['$http', 'logger'];

    function tableroService($http, logger) {

        var service = {
            getTablero: getTablero,
            getTableroUsuario: getTableroUsuario,
            getTableroArbol: getTableroArbol,
            getMisTareas: getMisTareas,
            moverTarea: moverTarea,
            buscarTablero: buscarTablero,
            buscarTareas: buscarTareas,
            buscarTareasMias: buscarTareasMias,
            buscarTarea: buscarTarea,
            actualizarTableroUsuario: actualizarTableroUsuario
        };

        return service;

        function getTablero() {
            var settings = {};
            var existePlan = JSON.parse(localStorage.getItem("existePlan"));
            if (existePlan) {
                var plan = JSON.parse(localStorage.getItem("planActivo"));
                settings.params = {planId: plan.plan_id};
            }
            return $http.get("tarea/listarTableros", settings).then(success).catch(failed);

            function success(response, status, headers, config) {
                service.tablero = response.data.board;
                return service.tableros;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

        function getTableroUsuario() {
            var settings = {};
            var existePlan = JSON.parse(localStorage.getItem("existePlan"));
            if (existePlan) {
                var plan = JSON.parse(localStorage.getItem("planActivo"));
                settings.params = {planId: plan.plan_id, equipo: false};
            }
            return $http.get("tarea/listarTablerosUsuario", settings).then(success).catch(failed);

            function success(response, status, headers, config) {
                service.tablero = response.data.board;
                return service.tableros;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

        function actualizarTableroUsuario() {
            var settings = {};
            return $http.get("tarea/listarTablerosUsuario", settings);
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
            return $http.post("tarea/moverTarea/" + idTarea + "/" + idEstado, params);
        }

        function buscarTablero(parametros) {
            parametros = JSON.stringify(parametros);
            var settings = {
                params: {parametros: parametros}
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

        function buscarTarea(idTarea) {
            var settings = {
                tarea_id: idTarea
            };
            return $http.post("planTarea/detallesTarea/" + idTarea, settings);
        }

        function buscarTareasMias(equipo) {
            var settings = {};
            var existePlan = JSON.parse(localStorage.getItem("existePlan"));
            if (existePlan) {
                var plan = JSON.parse(localStorage.getItem("planActivo"));
                settings.params = {planId: plan.plan_id, equipo: equipo};
            }
            return $http.get("planTarea/tareasUsuario", settings);
        }
    }
})();

