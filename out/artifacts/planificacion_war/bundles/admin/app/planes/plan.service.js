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
            salvarDiagrama: salvarDiagrama,
            insertarEliminarLink: insertarEliminarLink,
            eliminarTareaGrafico: eliminarTareaGrafico,
            inicializarPlan: inicializarPlan
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

        function buscarTarea(idTarea) {
            var settings = {
                tarea_id: idTarea
            };
            return $http.post("planTarea/detallesTarea/" + idTarea, settings);
        }

        function buscarPlan(idPlan) {
            var settings = {
                params: {
                    plan_id: idPlan
                }
            };
            return $http.post("plan/cargarDatosDiagrama/" + idPlan, settings);
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

        function getTareasRelacionadas(idTarea) {
            var settings = {};
            return $http.get("planTarea/listarTareasRelacionadas/" + idTarea, settings);
        }

        function buscarTareas(plan, parametros) {
            var settings = {
                params: parametros
            };
            return $http.get("plan/buscarTareas?plan_id=" + plan, settings);
        }

        function insertarEliminarLink(from, to, accion, planId) {
            var settings = {};
            return $http.post("planTarea/insertarEliminarLink?from=" + from + "&to=" + to + "&accion=" + accion + "&planId=" + planId, settings);
        }

        function eliminarTareaGrafico(tareaId, planId) {
            var settings = {};
            return $http.post("planTarea/eliminarTareaGrafico?tareaId=" + tareaId + "&planId=" + planId, settings);
        }

        function inicializarPlan(plan) {
            var settings = {};
            return $http.put("plan/iniciarPlan", plan, settings);
        }
    }
})();

