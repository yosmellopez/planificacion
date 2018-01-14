var TablePerfil = function () {

    //Validacion y Inicializacion de ajax form
    var initForm = function () {
        //Validacion           
        $("#editar-usuario-form").validate({
            rules: {
                repetirpassword: {
                    equalTo: '#editar-password'
                },
                nombre: {
                    required: true
                },
                apellidos: {
                    required: true
                },
                email: {
                    required: true,
                    email: true
                }
            },
            messages: {
                repetirpassword: {
                    equalTo: "Escribe el mismo valor de nuevo"
                },
                nombre: "Este campo es obligatorio",
                apellidos: "Este campo es obligatorio",
                email: {
                    required: "Este campo es obligatorio",
                    email: "El Email debe ser válido"
                }
            },
            showErrors: function (errorMap, errorList) {
                // Clean up any tooltips for valid elements
                $.each(this.validElements(), function (index, element) {
                    var $element = $(element);

                    $element.data("title", "") // Clear the title - there is no error associated anymore
                            .removeClass("has-error")
                            .tooltip("destroy");

                    $element
                            .closest('.form-group')
                            .removeClass('has-error').addClass('success');
                });

                // Create new tooltips for invalid elements
                $.each(errorList, function (index, error) {
                    var $element = $(error.element);

                    $element.tooltip("destroy") // Destroy any pre-existing tooltip so we can repopulate with new tooltip content
                            .data("title", error.message)
                            .addClass("has-error")
                            .tooltip({
                                placement: 'bottom'
                            }); // Create a new tooltip based on the error messsage we just set in the title 

                    $element.closest('.form-group')
                            .removeClass('has-success').addClass('has-error');

                });
            }
        });
        //Ajax form
        var opciones_form = {
            beforeSubmit: function () {
                Metronic.blockUI({
                    target: '#editar-usuario-form',
                    animate: true
                });
            }, //funcion que se ejecuta antes de enviar el form
            success: function (response) {
                Metronic.unblockUI('#editar-usuario-form');
                if (response.success) {

                    toastr.success(response.message, "Exito !!!");
                    //Actualizar el nombre del usuario                    
                    $('#header-user-name').html($('#editar-nombre').val());
                    cerrarForms();
                } else {
                    App.alertError(response.error);
                }
            },
            dataType: 'json',
            resetForm: false
        };
        $('#editar-usuario-form').ajaxForm(opciones_form);
    };
    //Reset forms
    var resetForms = function () {
        $('#editar-usuario-form input').each(function (e) {
            if ($(this).attr('id') != 'editar-usuarioId') {
                $(this).val("");
            }
        });
    };
    //Restablecer portlet
    var restablecerPortlet = function () {
        var portlet = $('#editar-form-usuario .portlet');
        if ($('body').hasClass('page-portlet-fullscreen')) {
            $('body').removeClass('page-portlet-fullscreen');
        }
        if (portlet.hasClass('portlet-fullscreen')) {
            portlet.find('.portlet-title .fullscreen').removeClass('on');
            portlet.removeClass('portlet-fullscreen');
            $('body').removeClass('page-portlet-fullscreen');
            portlet.children('.portlet-body').css('height', 'auto');
        }
    };
    //Cerrar forms
    var cerrarForms = function () {
        resetForms();
        $('#editar-form-usuario').addClass('ng-hide');
        $('#perfil-usuario').removeClass('ng-hide');
        restablecerPortlet();
    };
    //Cargar datos del usuario
    function editRow() {
        var usuario_id = $('#editar-usuarioId').val();

        Metronic.blockUI({
            target: '#editar-usuario-form',
            animate: true
        });

        $.ajax({
            type: "POST",
            url: "usuario/cargarDatos",
            dataType: "json",
            data: {
                'usuario_id': usuario_id
            },
            success: function (response) {
                Metronic.unblockUI('#editar-usuario-form');
                if (response.success) {
                    //Datos usuario
                    $('#editar-email').val(response.usuario.email);
                    $('#editar-nombre').val(response.usuario.nombre);
                    $('#editar-apellidos').val(response.usuario.apellidos);

                } else {
                    toastr.error(response.error, "Error !!!");
                }
            },
            failure: function (response) {
                Metronic.unblockUI('#editar-usuario-form');

                toastr.error(response.error, "Error !!!");
            }
        });

    }

    //Funciones para angular
    //Boton modificar
    var btnClickModificar = function () {
        $('#editar-form-usuario').removeClass('ng-hide');
        $('#perfil-usuario').addClass('ng-hide');

        editRow();
    };
    //Boton cerrar form
    var btnClickCerrarForm = function () {
        cerrarForms();
    };
    //Boton salvar nuevo form
    var btnClickSalvarForm = function () {
        Metronic.scrollTo($('.page-title'));

        if ($('#editar-password').val() != "")
            $('#editar-passwordcodificada').val(hex_sha1($('#editar-password').val()));

        if ($('#editar-usuario-form').valid()) {
            $('#editar-habilitado').val(($('#editar-habilitado').prop('checked')) ? 1 : 0);

            $('#editar-usuario-form').submit();
        } else {
            Metronic.scrollTo($('#editar-form-usuario'));
        }
    };

    return {
        //main function to initiate the module
        init: function () {
            initForm();
        },
        btnClickModificar: function () {
            btnClickModificar();
        },
        btnClickCerrarForm: function () {
            btnClickCerrarForm();
        },
        btnClickSalvarForm: function () {
            btnClickSalvarForm();
        }

    };

}();