(function () {
    'use strict';
    angular.module('app.usuarios')
        .factory('authorizationService', authorizationService);
    authorizationService.$inject = ['$http', 'logger'];

    function authorizationService($http, logger) {
        var service = {
            getAutheticatedUser: getAutheticatedUser,
            getUsuario: getUsuario,
            storeUser: storeUser
        };

        return service;

        function getAutheticatedUser() {
            var settings = {};
            return $http.get("authentication/usuario", settings);
        }

        function storeUser(usuario) {
            localStorage.setItem('usuarioId', usuario.usuario_id);
            localStorage.setItem('rol', usuario.rol);
            localStorage.setItem('perfil', usuario.perfil);
            localStorage.setItem('correo', usuario.email);
            localStorage.setItem('nombre', usuario.nombre);
            localStorage.setItem('apellidos', usuario.apellidos);
        }

        function getUsuario() {
            service.usuario = {
                usuario_id: localStorage.getItem('usuarioId'),
                rol: localStorage.getItem('rol'),
                perfil: localStorage.getItem('perfil'),
                email: localStorage.getItem('correo'),
                nombre: localStorage.getItem('nombre'),
                apellidos: localStorage.getItem('apellidos')
            };
            return usuario;
        }
    }
})();

