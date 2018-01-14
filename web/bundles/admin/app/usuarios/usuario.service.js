(function () {
    'use strict';

    angular.module('app.usuarios')
            .factory('usuarioService', usuarioService);

    usuarioService.$inject = ['$http', 'logger'];
    function usuarioService($http, logger) {

        var service = {
            getUsuarioActual: getUsuarioActual,
            getUsuario: getUsuario,
            isAuthorized: isAuthorized,
            getPermisos: getPermisos
        };

        return service;

        function getUsuarioActual() {
            var settings = {};

            return $http.get("usuario/devolverUsuarioActual", settings)
                    .then(success)
                    .catch(failed);

            function success(data, status, headers, config) {
                service.usuario = data.data.usuario;
                storeUser(service.usuario);

                return service.usuario;
            }

            function failed(error) {
                logger.error('Error !!' + error.data);
            }
        }

        function isAuthorized(funcion_id) {
            var result = false;
            var usuario = getUsuario();
            if (funcion_id === "" || usuario === null) {
                result = true;
            } else {
                var menu = usuario.menu;
                for (var i = 0; i < menu.length; i++) {

                    if (menu[i].menu_id === funcion_id) {
                        result = true;
                        break;
                    }
                    for (var j = 0; j < menu[i].funciones.length; j++) {
                        if (menu[i].funciones[j].funcion_id === funcion_id) {
                            result = true;
                            break;
                        }
                    }
                    if (result) {
                        break;
                    }
                }
            }

            return result;
        }

        function getPermisos(funcion_id) {
            var permisos = null;
            var usuario = getUsuario();

            var usuario_permisos = usuario.permisos;
            for (var i = 0; i < usuario_permisos.length; i++) {

                if (usuario_permisos[i].funcion_id === funcion_id) {

                    permisos = {
                        funcion_id: funcion_id,
                        ver: (usuario_permisos[i].ver === 1) ? true : false,
                        agregar: (usuario_permisos[i].agregar === 1) ? true : false,
                        editar: (usuario_permisos[i].editar === 1) ? true : false,
                        eliminar: (usuario_permisos[i].eliminar === 1) ? true : false,
                        todos: (usuario_permisos[i].ver === 1 && usuario_permisos[i].agregar === 1 &&
                                usuario_permisos[i].editar === 1 && usuario_permisos[i].eliminar) ? true : false
                    }
                    break;
                }
            }

            return permisos;
        }

        function storeUser(usuario) {
            window.localStorage.setItem('fia_usuario_id', usuario.usuario_id);
            window.localStorage.setItem('fia_rol', usuario.rol);
            window.localStorage.setItem('fia_perfil', usuario.perfil);
            window.localStorage.setItem('fia_email', usuario.email);
            window.localStorage.setItem('fia_nombre', usuario.nombre);
            window.localStorage.setItem('fia_apellidos', usuario.apellidos);
            window.localStorage.setItem('fia_habilitado', usuario.habilitado);

            window.localStorage.setItem('fia_menu', JSON.stringify(usuario.menu));
            window.localStorage.setItem('fia_permisos', JSON.stringify(usuario.permisos));
            window.localStorage.setItem('fia_campos', JSON.stringify(usuario.campos));

            window.localStorage.setItem('fia_departamento_id', usuario.departamento_id);
            window.localStorage.setItem('fia_departamento', usuario.departamento);
        }

        function deleteUser() {
            window.localStorage.removeItem('fia_usuario_id');
            window.localStorage.removeItem('fia_rol');
            window.localStorage.removeItem('fia_perfil');
            window.localStorage.removeItem('fia_email');
            window.localStorage.removeItem('fia_nombre');
            window.localStorage.removeItem('fia_apellidos');
            window.localStorage.removeItem('fia_habilitado');
            window.localStorage.removeItem('fia_menu');
            window.localStorage.removeItem('fia_permisos');
            window.localStorage.removeItem('fia_campos');
            window.localStorage.removeItem('fia_departamento_id');
            window.localStorage.removeItem('fia_departamento');
        }

        function getUsuario() {

            var usuario = null;

            var menu = window.localStorage.getItem('fia_menu');
            if (menu !== 'undefined') {
                menu = JSON.parse(menu);

                var permisos = window.localStorage.getItem('fia_permisos');
                permisos = (permisos !== undefined) ? JSON.parse(permisos) : [];

                var campos = window.localStorage.getItem('fia_campos');
                campos = (campos !== undefined) ? JSON.parse(campos) : [];

                usuario = {
                    usuario_id: window.localStorage.getItem('fia_usuario_id'),
                    rol: window.localStorage.getItem('fia_rol'),
                    perfil: window.localStorage.getItem('fia_perfil'),
                    email: window.localStorage.getItem('fia_email'),
                    nombre: window.localStorage.getItem('fia_nombre'),
                    apellidos: window.localStorage.getItem('fia_apellidos'),
                    habilitado: window.localStorage.getItem('fia_habilitado'),
                    menu: menu,
                    permisos: permisos,
                    campos: campos,
                    departamento_id: window.localStorage.getItem('fia_departamento_id'),
                    departamento: window.localStorage.getItem('fia_departamento'),
                };
                service.usuario = usuario;
            }

            return usuario;
        }
    }

})();

