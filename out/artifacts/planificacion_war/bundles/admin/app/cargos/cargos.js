(function () {
    'use strict';

    angular
        .module('app.cargos')
        .controller('Cargos', Cargos);

    Cargos.$inject = ['$rootScope', '$scope', '$timeout', 'urlPath', 'areaService', 'gerenciaService'];

    function Cargos($rootScope, $scope, $timeout, urlPath, areaService, gerenciaService) {
        var vm = this;
        vm.urlPath = urlPath;

        vm.areas = areaService.areas;
        vm.gerencias = gerenciaService.gerencias;

        vm.nuevo = nuevo;
        vm.eliminar = eliminar;
        vm.modalEliminar = modalEliminar;
        vm.modalEliminarSeleccion = modalEliminarSeleccion;

        vm.cerrarForm = cerrarForm;
        vm.salvarForm = salvarForm;

        // set sidebar closed and body solid layout mode
        $rootScope.settings.layout.pageBodySolid = true;
        $rootScope.settings.layout.pageSidebarClosed = false;
        $scope.jerarquia = "basico";

        activate();

        function nuevo() {
            $scope.jerarquia = "basico";
            $timeout(function () {
                TableEditableCargos.btnClickNuevo();
            }, 0);

        }

        function eliminar() {
            TableEditableCargos.btnClickEliminar();
        }

        function modalEliminar() {
            TableEditableCargos.btnClickModalEliminar();
        }

        function modalEliminarSeleccion() {
            TableEditableCargos.btnClickModalEliminarSeleccion();
        }

        function cerrarForm() {
            $timeout(function () {
                TableEditableCargos.btnClickCerrarForm();
            }, 0);
        }

        function salvarForm() {
            TableEditableCargos.btnClickSalvarForm();
        }

        function activate() {
            $scope.$on('$viewContentLoaded', function () {
                // initialize core components
                Metronic.initAjax();
                MyApp.init();

                $timeout(function () {
                    TableEditableCargos.init($scope);
                }, 0);

            });
        }

        vm.cambiarJerarquia = function (obj) {
            $scope.jerarquia = obj.nombre;
        }

        $scope.isBasico = function () {
            if ($scope.jerarquia === "basico") {
                $('#cargo-unidad').parent().removeClass("checked");
                $('#cargo-unidad').prop("checked", false);
                $('#cargo-direccion').parent().removeClass("checked");
                $('#cargo-direccion').prop("checked", false);
                $('#cargo-basico').parent().addClass("checked");
                $('#cargo-basico').prop("checked", true);
                return true;
            }
            return false;
        }

        $scope.isUnidad = function () {
            if ($scope.jerarquia === "unidad") {
                $('#cargo-basico').parent().removeClass("checked");
                $('#cargo-basico').prop("checked", false);
                $('#cargo-direccion').parent().removeClass("checked");
                $('#cargo-direccion').prop("checked", false);
                $('#cargo-unidad').parent().addClass("checked");
                $('#cargo-unidad').prop("checked", true);
                return true;
            }
            return false;
        }

        $scope.isDireccion = function () {
            if ($scope.jerarquia === "direccion") {
                $('#cargo-unidad').parent().removeClass("checked");
                $('#cargo-unidad').prop("checked", false);
                $('#cargo-basico').parent().removeClass("checked");
                $('#cargo-basico').prop("checked", false);
                $('#cargo-direccion').parent().addClass("checked");
                $('#cargo-direccion').prop("checked", true);
                return true;
            }
            return false;
        }
    }
})();
