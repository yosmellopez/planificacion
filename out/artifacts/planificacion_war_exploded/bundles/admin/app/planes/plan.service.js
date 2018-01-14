(function () {
    'use strict';

    angular.module('app.planes')
        .factory('planService', planService);

    planService.$inject = ['$http', 'logger'];

    function planService($http, logger) {

        var service = {
            getPlanes: getPlanes,
            getPlan: getPlan,
            listarPlanes: listarPlanes,
            buscarTarea: buscarTarea,
            buscarTareas: buscarTareas,
            buscarPlan: buscarPlan,
            getTareas: getTareas,
            getTareasRelacionadas: getTareasRelacionadas,
            salvarDiagrama: salvarDiagrama
        };

        return service;

        function getPlanes() {
            var settings = {};

            return $http.get("plan/listarTodos", settings)
                .then(success)
                .catch(failed);

            function success(data, status, headers, config) {
                service.planes = data.data.planes;
                return service.planes;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

        function listarPlanes() {
            var settings = {};

            return $http.get("plan/listarTodosPlanes", settings)
                .then(success)
                .catch(failed);

            function success(response, status, headers, config) {
                service.planes = response.data.planes;
                return service.planes;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

        function getPlan(idPlan) {
            var settings = {
                params: {
                    plan_id: idPlan
                }
            };
            return $http.get("plan/cargarDatosPlan", settings)
                .then(success)
                .catch(failed);

            function success(response, status, headers, config) {
                service.plan = response.data.plan;
                return service.plan;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

        function buscarTarea(idTarea, idPlan) {
            var params = {
                tarea_id: idTarea
            };
            return $http.post("tarea/cargarDatos?tarea_id=" + idTarea + "&plan_id=" + idPlan, params);
        }

        function buscarPlan(idPlan) {
            var settings = {
                params: {
                    plan_id: idPlan
                }
            };
            return $http.post("plan/cargarDatosDiagrama?plan_id=" + idPlan, settings);
        }

        function salvarDiagrama(idPlan, diagrama) {
            var data = {
                plan_id: idPlan,
                diagrama: diagrama
            };
            return $http({
                method: 'POST',
                url: "plan/salvarDiagrama",
                params: data,
                headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'}
            });
        }

        function getTareas() {
            var settings = {};
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

        function getTareasRelacionadas(idTarea, idPlan) {
            var settings = {};
            return $http.get("planTarea/listarTareasRelacionadas/" + idTarea + "/" + idPlan, settings);
        }

        function buscarTareas(plan, parametros) {
            var settings = {
                params: parametros
            };
            return $http.get("plan/buscarTareas?plan_id=" + plan, settings);
        }
    }
})();

