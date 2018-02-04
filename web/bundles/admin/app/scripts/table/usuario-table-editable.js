/* global Metronic */

var TableEditableUsuarios = function () {

    var oTable;
    var rowDelete = null;
    var cont = 0;
    var formTitle = "¿Deseas crear un nuevo usuario? Sigue los siguientes pasos:";

    //Inicializa la tabla
    var initTable = function () {
        var table = $('#usuario-table-editable');

        var order = [[1, "asc"]];
        var aoColumns = [
            {"bSortable": false, "sWidth": '1%', "sClass": 'text-center'},
            {"bSortable": true, "sWidth": '20%'},
            {"bSortable": true, "sWidth": '16%', "sClass": 'form-control-text'},
            {"bSortable": true, "sWidth": '16%', "sClass": 'form-control-text'},
            {"bSortable": true, "sWidth": '16%', "sClass": 'text-center'},
            {"bSortable": true, "sWidth": '16%', "sClass": 'text-center'},
            {"bSortable": false, "sWidth": '15%', "sClass": 'text-center'}
        ];

        oTable = new Datatable();
        oTable.init({
            src: table,
            onSuccess: function (grid) {
                // execute some code after table records loaded
            },
            onError: function (grid) {
                // execute some code on network or other general error
            },
            loadingMessage: 'Por favor espere...',
            dataTable: {
                "serverSide": false,
                "lengthMenu": [
                    [15, 25, 30, 50, -1],
                    [15, 25, 30, 50, "Todos"]
                ],
                "pageLength": 15, // default record count per page
                "ajax": {
                    "url": "usuario/listarUsuario", // ajax source
                },
                "order": order,
                responsive: {
                    details: {}
                },
                "aoColumns": aoColumns
            }
        });

        var tableWrapper = oTable.getTableWrapper(); // datatable creates the table wrapper by adding with id {your_table_jd}_wrapper
        tableWrapper.find('.dataTables_filter input').addClass("form-control input-sm input-inline");
        tableWrapper.find('.dataTables_length select').select2({minimumResultsForSearch: Infinity}); // initialize select2 dropdown
        //Checkboxes
        table.find('.group-checkable').change(function () {
            var set = jQuery(this).attr("data-set");
            var checked = jQuery(this).is(":checked");
            jQuery(set).each(function () {
                if (checked) {
                    $(this).prop("checked", true);
                    $(this).parents('tr').addClass("active");
                } else {
                    $(this).prop("checked", false);
                    $(this).parents('tr').removeClass("active");
                }
                jQuery.uniform.update(this);
            });
        });
        table.on('change', 'tbody tr .checkboxes', function () {
            $(this).parents('tr').toggleClass("active");
        });
    };
    //Reset forms
    var resetForms = function () {
        $('#usuario-form input').each(function (e) {
            $element = $(this);
            $element.val('');

            $element.data("title", "").removeClass("has-error").tooltip("destroy");
            $element.closest('.form-group').removeClass('has-error').addClass('success');
        });

        $('#rol').select2('val', '');

        $('#estadoactivo').prop('checked', true);
        jQuery.uniform.update('#estadoactivo');

        $('#gerencia').select2('val', '');

        //Limipiar select
        $('#area option').each(function (e) {
            if ($(this).val() !== "")
                $(this).remove();
        });
        $('#area').select2();

        //Limipiar select cargo
        $('#cargo option').each(function (e) {
            if ($(this).val() !== "")
                $(this).remove();
        });
        $('#cargo').select2();

        var $element = $('.selectpicker');
        $element.removeClass('has-error').tooltip("destroy");

        $element.closest('.form-group')
            .removeClass('has-error');

    };
    //Restablecer portlet
    var restablecerPortlet = function () {
        var portlet = $('#form-usuario .portlet');
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
        $('#form-usuario').addClass('ng-hide');
        $('#lista-usuario').removeClass('ng-hide');
        restablecerPortlet();
    };
    //Validacion y Inicializacion de ajax form
    var initNuevoForm = function () {
        $("#usuario-form").validate({
            rules: {
                rol: {
                    required: true
                },
                /*password: {
                 required: true
                 },*/
                repetirpassword: {
                    //required: true,
                    equalTo: '#password'
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
                //password: "Este campo es obligatorio",
                repetirpassword: {
                    //required: "Este campo es obligatorio",
                    equalTo: "Escribe el mismo valor de nuevo"
                },
                nombre: "Este campo es obligatorio",
                apellidos: "Este campo es obligatorio",
                email: {
                    required: "Este campo es obligatorio",
                    email: "El Email debe ser válido"
                },
                rol: "Este campo es obligatorio"
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

        $("#password").rules("add", {
            required: true,
            messages: {
                required: "Este campo es obligatorio"
            }
        });
        $("#repetirpassword").rules("add", {
            required: true,
            messages: {
                required: "Este campo es obligatorio"
            }
        });
    };
    var initEditarForm = function () {
        $("#password").rules("remove");
        $("#repetirpassword").rules("remove", "required");
    };

    //Funciones para angular
    //Boton nuevo
    var btnClickNuevo = function () {
        resetForms();
        $('#form-usuario-title').html(formTitle);
        $('#form-usuario').removeClass('ng-hide');
        $('#lista-usuario').addClass('ng-hide');
    };
    //Boton eliminar
    var btnClickEliminar = function () {
        var ids = new Array();
        $('.checkboxes').each(function () {
            if ($(this).prop('checked'))
                ids.push(parseInt($(this).attr('data-id')));
        });
        if (ids.length !== 0) {
            $('#modal-eliminar-seleccion').modal({
                'show': true
            });
        } else {
            toastr.error('Seleccione los elementos a eliminar', "Error !!!");
        }
    };
    //Boton eliminar del modal
    var btnClickModalEliminar = function () {
        var usuario_id = rowDelete;

        Metronic.blockUI({target: '#usuario-table-editable', animate: true});

        $.ajax({
            type: "POST",
            url: "usuario/eliminarUsuario",
            dataType: "json",
            data: {
                'usuario_id': usuario_id
            },
            success: function (response) {
                Metronic.unblockUI('#usuario-table-editable');
                if (response.success) {
                    oTable.getDataTable().ajax.reload();
                    toastr.success(response.message, "Exito !!!");
                } else {
                    toastr.error(response.error, "Error !!!");
                }
            },
            failure: function (response) {
                $('.dataTables_processing').css('display', 'none');
                toastr.error(response.error, "Error !!!");
            }
        });
    };
    //Boton eliminar del modal
    var btnClickModalEliminarSeleccion = function () {
        var ids = new Array();
        $('.checkboxes').each(function () {
            if ($(this).prop('checked'))
                ids.push(parseInt($(this).attr('data-id')));
        });

        Metronic.blockUI({
            target: '#usuario-table-editable',
            animate: true
        });

        $.ajax({
            type: "POST",
            url: "usuario/eliminarUsuarios",
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify({
                ids: ids
            }),
            success: function (response) {
                Metronic.unblockUI('#usuario-table-editable');
                if (response.success) {
                    oTable.getDataTable().ajax.reload();
                    toastr.success(response.message, "Exito !!!");
                } else {
                    toastr.error(response.error, "Error !!!");
                }
            },
            failure: function (response) {
                Metronic.unblockUI('#usuario-table-editable');
                toastr.error(response.error, "Error !!!");
            }
        });

    };
    //Boton cerrar form
    var btnClickCerrarForm = function () {
        cerrarForms();
    };
    //Boton salvar nuevo form
    var btnClickSalvarForm = function () {
        Metronic.scrollTo($('.page-title'));

        //Validacion
        initNuevoForm();

        var usuario_id = $('#usuario_id').val();
        if (usuario_id !== "") {
            initEditarForm();
        }

        var rol_id = $('#rol').val();

        if ($('#usuario-form').valid() && rol_id !== "") {

            var nombre = $('#nombre').val();
            var apellidos = $('#apellidos').val();
            var email = $('#email').val();

            var password = $('#password').val();
            password = (password !== "") ? hex_sha1(password) : "";

            var estado = ($('#estadoactivo').prop('checked')) ? 1 : 0;
            var titular = ($('#titular').prop('checked')) ? true : false;
            var cargo_id = $('#cargo').val();

            Metronic.blockUI({target: '#form-usuario .portlet-body', animate: true});

            $.ajax({
                type: "POST",
                url: "usuario/salvarUsuario.json",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: JSON.stringify({
                    'usuario_id': usuario_id === "" ? null : usuario_id,
                    'rol': rol_id,
                    'cargo': cargo_id,
                    'habilitado': estado,
                    'passwordcodificada': password,
                    'nombre': nombre,
                    'apellidos': apellidos,
                    'titular': titular,
                    'email': email
                }),
                success: function (response) {
                    Metronic.unblockUI('#form-usuario .portlet-body');
                    if (response.success) {

                        toastr.success(response.message, "Exito !!!");
                        cerrarForms();
                        oTable.getDataTable().ajax.reload();
                    } else {
                        toastr.error(response.error, "Error !!!");
                    }
                },
                failure: function (response) {
                    Metronic.unblockUI('#form-usuario .portlet-body');
                    toastr.error(response.error, "Error !!!");
                }
            });

        } else {
            if (rol_id === "") {
                var $element = $('#select-rol .selectpicker');
                $element.tooltip("destroy") // Destroy any pre-existing tooltip so we can repopulate with new tooltip content
                    .data("title", "Este campo es obligatorio")
                    .addClass("has-error")
                    .tooltip({
                        placement: 'bottom'
                    }); // Create a new tooltip based on the error messsage we just set in the title

                $element.closest('.form-group').removeClass('has-success').addClass('has-error');
            }
        }
    };

    //Funciones jquery
    //Editar
    var initAccionEditar = function () {
        $(document).on('click', "#usuario-table-editable a.edit", function (e) {
            e.preventDefault();
            resetForms();

            var usuario_id = $(this).data('id');
            $('#usuario_id').val(usuario_id);
            $('#form-usuario').removeClass('ng-hide');
            $('#lista-usuario').addClass('ng-hide');

            editRow(usuario_id);
        });

        function editRow(usuario_id) {
            Metronic.blockUI({
                target: '#usuario-form',
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
                    Metronic.unblockUI('#usuario-form');
                    if (response.success) {
                        //Datos usuario    
                        var rol_id = response.usuario.rol;
                        $('#rol').select2('val', rol_id);
                        formTitle = "¿Deseas actualizar el usuario \"" + response.usuario.nombre + "\" ? Sigue los siguientes pasos:";
                        $('#form-usuario-title').html(formTitle);
                        $('#nombre').val(response.usuario.nombre);
                        $('#apellidos').val(response.usuario.apellidos);
                        $('#email').val(response.usuario.email);
                        if (!response.usuario.habilitado) {
                            $('#estadoactivo').prop('checked', false);
                            $('#estadoinactivo').prop('checked', true);
                            jQuery.uniform.update('#estadoactivo');
                            jQuery.uniform.update('#estadoinactivo');
                        }
                        if (response.usuario.titular) {
                            $('#titular').prop('checked', true);
                            $('#no-titular').prop('checked', false);
                            jQuery.uniform.update('#titular');
                            jQuery.uniform.update('#backup');
                        }
                        //Gerencia
                        $("#gerencia").unbind('change', cargarAreas);
                        $('#gerencia').select2('val', response.usuario.gerencia_id);
                        //Areas
                        $("#area").unbind('change', cargarCargos);
                        var areas = response.usuario.areas;
                        for (var i = 0; i < areas.length; i++) {
                            $('#area').append(new Option(areas[i].descripcion, areas[i].area_id, false, false));
                        }
                        $('#area').select2();
                        $('#area').select2('val', response.usuario.area_id);

                        //Cargos
                        var cargos = response.usuario.cargos;
                        for (var i = 0; i < cargos.length; i++) {
                            $('#cargo').append(new Option(cargos[i].nombre, cargos[i].cargo_id, false, false));
                        }
                        $('#cargo').select2();
                        $('#cargo').select2('val', response.usuario.cargo_id);

                        $("#gerencia").bind('change', cargarAreas);
                        $("#area").bind('change', cargarCargos);

                    } else {
                        toastr.error(response.error, "Error !!!");
                    }
                },
                failure: function (response) {
                    Metronic.unblockUI('#usuario-form');
                    toastr.error(response.error, "Error !!!");
                }
            });

        }
    };
    //Activar
    var initAccionActivar = function () {
        //Activar usuario
        $(document).on('click', "#usuario-table-editable a.block", function (e) {
            e.preventDefault();
            /* Get the row as a parent of the link that was clicked on */
            var usuario_id = $(this).data('id');
            cambiarEstadoUsuario(usuario_id);
        });

        function cambiarEstadoUsuario(usuario_id) {

            Metronic.blockUI({target: '#usuario-table-editable', animate: true});

            $.ajax({
                type: "POST",
                url: "usuario/activarUsuario",
                dataType: "json",
                data: {
                    'usuario_id': usuario_id
                },
                success: function (response) {
                    Metronic.unblockUI('#usuario-table-editable');

                    if (response.success) {
                        toastr.success("La operación se realizó correctamente", "Exito !!!");
                        oTable.getDataTable().ajax.reload();

                    } else {
                        toastr.error(response.error, "Error !!!");
                    }
                },
                failure: function (response) {
                    Metronic.unblockUI('#usuario-table-editable');
                    toastr.error(response.error, "Error !!!");
                }
            });
        }
    };
    //Eliminar
    var initAccionEliminar = function () {
        $(document).on('click', "#usuario-table-editable a.delete", function (e) {
            e.preventDefault();

            rowDelete = $(this).data('id');
            $('#modal-eliminar').modal({
                'show': true
            });
        });
    };

    //Cargar cargos
    var cargarCargos = function (e) {

        //Limipiar select
        $('#cargo option').each(function (e) {
            if ($(this).val() !== "")
                $(this).remove();
        });
        $('#cargo').select2();

        var area_id = $("#area").val();

        if (area_id !== "") {
            Metronic.blockUI({
                target: '#usuario-form',
                animate: true
            });

            $.ajax({
                type: "POST",
                url: "cargo/listarDeArea",
                dataType: "json",
                data: {
                    'area_id': area_id
                },
                success: function (response) {
                    Metronic.unblockUI('#usuario-form');
                    if (response.success) {
                        for (var i = 0; i < response.cargos.length; i++) {
                            var cargo_id = response.cargos[i].cargo_id;
                            var descripcion = response.cargos[i].nombre;

                            $('#cargo').append(new Option(descripcion, cargo_id, false, false));
                        }

                        $('#cargo').select2();

                    } else {
                        toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                    }
                },
                failure: function (response) {
                    Metronic.unblockUI('#usuario-form');

                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            });
        }

    };
    var initSeletArea = function () {
        $("#area").bind('change', cargarCargos);
    };

    //Cargar areas
    var cargarAreas = function (e) {

        //Limipiar select
        $('#area option').each(function (e) {
            if ($(this).val() !== "")
                $(this).remove();
        });
        $('#area').select2();
        //Limipiar select
        $('#cargo option').each(function (e) {
            if ($(this).val() !== "")
                $(this).remove();
        });
        $('#cargo').select2();

        var gerencia_id = $("#gerencia").val();

        if (gerencia_id !== "") {
            Metronic.blockUI({
                target: '#usuario-form',
                animate: true
            });

            $.ajax({
                type: "POST",
                url: "area/listarDeGerencia",
                dataType: "json",
                data: {
                    'gerencia_id': gerencia_id
                },
                success: function (response) {
                    Metronic.unblockUI('#usuario-form');
                    if (response.success) {
                        for (var i = 0; i < response.areas.length; i++) {
                            var area_id = response.areas[i].area_id;
                            var descripcion = response.areas[i].nombre;
                            $('#area').append(new Option(descripcion, area_id, false, false));
                        }

                        $('#area').select2();

                    } else {
                        toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                    }
                },
                failure: function (response) {
                    Metronic.unblockUI('#usuario-form');

                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            });
        }


    };
    var initSeletGerencia = function () {
        $("#gerencia").bind('change', cargarAreas);
    };

    return {
        //main function to initiate the module
        init: function () {

            initTable();
            if (cont === 0) {
                initAccionEditar();
                initAccionActivar();
                initAccionEliminar();
                initSeletGerencia();
                initSeletArea();
            }
            cont++;
        },
        btnClickNuevo: btnClickNuevo,
        btnClickEliminar: btnClickEliminar,
        btnClickModalEliminar: btnClickModalEliminar,
        btnClickModalEliminarSeleccion: btnClickModalEliminarSeleccion,
        btnClickCerrarForm: btnClickCerrarForm,
        btnClickSalvarForm: btnClickSalvarForm

    };

}();