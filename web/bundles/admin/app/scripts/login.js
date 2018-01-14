var Login = function () {

    var handleLogin = function () {

        $('.login-form').validate({
            errorElement: 'span', //default input error message container
            errorClass: 'help-block', // default input error message class
            focusInvalid: false, // do not focus the last invalid input
            rules: {
                username: {
                    required: true,
                    email: true
                },
                password: {
                    required: true
                },
                remember: {
                    required: false
                }
            },
            messages: {
                username: {
                    required: "Este campo es obligatorio",
                    email: "El Email debe ser válido"
                },
                password: {
                    required: "Este campo es obligatorio"
                }
            },
            invalidHandler: function (event, validator) { //display error alert on form submit   
                $('.alert-danger', $('.login-form')).show();
            },
            highlight: function (element) { // hightlight error inputs
                $(element)
                        .closest('.form-group').addClass('has-error'); // set error class to the control group
            },
            success: function (label) {
                label.closest('.form-group').removeClass('has-error');
                label.remove();
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element.closest('.input-icon'));
            },
            submitHandler: function (form) {
                Login();
            }
        });

        $('.login-form input').keypress(function (e) {
            if (e.which === 13) {
                if ($('.login-form').validate().form()) {
                    Login();
                }
                return false;
            }
        });

        function Login() {
            var nick = $('#username').val();
            nick = nick.toLowerCase();
            var pass = $('#password').val();
            var remember_me = $('#remember').prop('checked');
            var target_path = $('#target_path').val();

            Metronic.blockUI({
                target: '#blockui-loading',
                animate: true
            });
            $.ajax({
                type: "POST",
                url: "usuario/autenticar",
                dataType: "json",
                data: {
                    'email': nick,
                    'pass': hex_sha1(pass),
                    'remember_me': remember_me,
                    'target_path': target_path
                },
                success: function (response) {
                    Metronic.unblockUI('#blockui-loading');

                    if (response.success) {
                        storeUser(response.usuario);
                        window.location.href = response.url;
                    } else {
                        toastr.error(response.error, "Error !!!");
                    }
                },
                failure: function (response) {
                    Metronic.unblockUI('#blockui-loading');

                    toastr.error(response.error, "Error !!!");
                }
            });
        }
    }

    var handleForgetPassword = function () {
        $('.forget-form').validate({
            errorElement: 'span', //default input error message container
            errorClass: 'help-block', // default input error message class
            focusInvalid: false, // do not focus the last invalid input
            ignore: "",
            rules: {
                email: {
                    required: true,
                    email: true
                }
            },
            messages: {
                email: {
                    required: "Este campo es obligatorio.",
                    email: "El Email debe ser válido."
                }
            },
            invalidHandler: function (event, validator) { //display error alert on form submit   

            },
            highlight: function (element) { // hightlight error inputs
                $(element)
                        .closest('.form-group').addClass('has-error'); // set error class to the control group
            },
            success: function (label) {
                label.closest('.form-group').removeClass('has-error');
                label.remove();
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element.closest('.input-icon'));
            },
            submitHandler: function (form) {
                ForgetForm();
            }
        });

        $('.forget-form input').keypress(function (e) {
            if (e.which === 13) {
                if ($('.forget-form').validate().form()) {
                    ForgetForm();
                }
                return false;
            }
        });

        function ForgetForm() {
            var email = $('#email').val();

            Metronic.blockUI({
                target: '#blockui-loading',
                animate: true
            });
            $.ajax({
                type: "POST",
                url: "usuario/olvidoContrasenna",
                dataType: "json",
                data: {
                    'email': email
                },
                success: function (response) {
                    Metronic.unblockUI('#blockui-loading');

                    if (response.success) {
                        jQuery('.login-form').show();
                        jQuery('.forget-form').hide();

                        toastr.success(response.message, "Exito !!!");
                    } else {
                        toastr.error(response.error, "Error !!!");
                    }
                },
                failure: function (response) {
                    Metronic.unblockUI('#blockui-loading');

                    toastr.error(response.error, "Error !!!");
                }
            });
        }

        jQuery('#forget-password').click(function () {
            jQuery('.login-form').hide();
            jQuery('.forget-form').show();
        });

        jQuery('#back-btn').click(function () {
            jQuery('.login-form').show();
            jQuery('.forget-form').hide();
        });

    }

    var storeUser = function (usuario) {
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
    };

    return {
        //main function to initiate the module
        init: function () {
            handleLogin();
            handleForgetPassword();
        },
        login: function () {
            handleLogin();
            return false;
        }

    };

}();