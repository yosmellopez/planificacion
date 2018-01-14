var TableEditableCriticidadTarea = function () {

    var oTable;
    var rowDelete = null;
    var cont = 0;
    var angularScope = null;
    var formTitle = "¿Deseas crear una nueva criticidad de tarea? Sigue los siguientes pasos:";

    //Inicializa la tabla
    var initTable = function (scope) {
        var table = $('#criticidad-tarea-table-editable');
        angularScope = scope;
        var order = [[1, "asc"]];
        var aoColumns = [
            {"bSortable": false, "sWidth": '2%', "sClass": 'text-center'},
            {"bSortable": true, "sWidth": '60%', "sClass": 'form-control-text'},
            {"bSortable": true, "sWidth": '60%', "sClass": 'form-control-text'},
            {"bSortable": true, "sWidth": '20%', "sClass": 'text-center'},
            {"bSortable": true, "sWidth": '20%', "sClass": 'text-center'},
            {"bSortable": true, "sWidth": '20%', "sClass": 'text-center'},
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
                    "url": "criticidad-tarea/listarCriticidad", // ajax source
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

    //Validacion
    var initForm = function () {
        //Validacion
        $("#criticidad-form").validate({
            rules: {
                descripcion: {
                    required: true
                }
            },
            messages: {
                descripcion: {
                    required: "Este campo es obligatorio"
                }
            },
            showErrors: function (errorMap, errorList) {
                // Clean up any tooltips for valid elements
                $.each(this.validElements(), function (index, element) {
                    var $element = $(element);

                    $element.data("title", "") // Clear the title - there is no error associated anymore
                            .removeClass("has-error")
                            .tooltip("destroy");

                    $element.closest('.form-group')
                            .removeClass('has-error').addClass('success');
                });

                // Create new tooltips for invalid elements
                $.each(errorList, function (index, error) {
                    var $element = $(error.element);
                    $element.tooltip("destroy") // Destroy any pre-existing tooltip so we can repopulate with new tooltip content
                            .data("title", error.message)
                            .addClass("has-error").tooltip({placement: 'bottom'}); // Create a new tooltip based on the error messsage we just set in the title
                    $element.closest('.form-group').removeClass('has-success').addClass('has-error');

                });
            }
        });
    };

    //Reset forms
    var resetForms = function () {
        $('#criticidad-form input').each(function (e) {
            $element = $(this);
            $element.val('');
            $element.data("title", "").removeClass("has-error").tooltip("destroy");
            $element.closest('.form-group').removeClass('has-error').addClass('success');
        });

        $('#estadoactivo').prop('checked', true);
        jQuery.uniform.update('#estadoactivo');

    };
    //Restablecer portlet
    var restablecerPortlet = function () {
        var portlet = $('#form-criticidad .portlet');
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
        $('#form-criticidad').addClass('ng-hide');
        $('#lista-criticidad').removeClass('ng-hide');
        restablecerPortlet();
    };

    //Funciones para angular
    //Boton nuevo
    var btnClickNuevo = function () {
        resetForms();
        $('#form-criticidad-title').html(formTitle);
        $('#form-criticidad').removeClass('ng-hide');
        $('#lista-criticidad').addClass('ng-hide');
    };
    //Boton eliminar
    var btnClickEliminar = function () {
        var ids = '';
        $('.checkboxes').each(function () {
            if ($(this).prop('checked'))
                ids += $(this).attr('data-id') + ',';
        });
        if (ids !== '') {
            $('#modal-eliminar-seleccion').modal({
                'show': true
            });
        } else {
            toastr.error('Seleccione los elementos a eliminar', "Error !!!");
        }
    };
    //Boton eliminar del modal
    var btnClickModalEliminar = function () {
        var criticidad_id = rowDelete;
        Metronic.blockUI({target: '#criticidad-tarea-table-editable', animate: true});
        $.ajax({
            type: "POST",
            url: "criticidad-tarea/eliminarCriticidad",
            dataType: "json",
            data: {
                'criticidad_id': criticidad_id
            },
            success: function (response) {
                Metronic.unblockUI('#criticidad-tarea-table-editable');
                if (response.success) {
                    oTable.getDataTable().ajax.reload();
                    toastr.success(response.message, "Exito !!!");
                } else {
                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            },
            failure: function (response) {
                Metronic.unblockUI('#criticidad-tarea-table-editable');

                toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
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
        Metronic.blockUI({target: '#criticidad-tarea-table-editable', animate: true});
        $.ajax({
            type: "POST",
            url: "criticidad-tarea/eliminarCriticidades",
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify({
                'ids': ids
            }),
            success: function (response) {
                Metronic.unblockUI('#criticidad-tarea-table-editable');
                if (response.success) {
                    oTable.getDataTable().ajax.reload();
                    toastr.success(response.message, "Exito !!!");
                } else {
                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            },
            failure: function (response) {
                Metronic.unblockUI('#criticidad-tarea-table-editable');
                toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
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

        if ($('#criticidad-form').valid()) {

            var criticidad_id = $('#criticidad_id').val();
            var nombre = $('#nombre').val();
            var descripcion = $('#descripcion').val();
            var peso = $('#peso').val();
            var color = $('#color').val();
            var estado = ($('#estadoactivo').prop('checked')) ? true : false;

            Metronic.blockUI({target: '#form-criticidad .portlet-body', animate: true});

            $.ajax({
                type: "POST",
                url: "criticidad-tarea/salvarCriticidad",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: JSON.stringify({
                    'criticidad_id': criticidad_id,
                    'descripcion': descripcion,
                    'peso': peso,
                    'color': color,
                    'nombre': nombre,
                    'estado': estado
                }),
                success: function (response) {
                    Metronic.unblockUI('#form-criticidad .portlet-body');
                    if (response.success) {
                        toastr.success(response.message, "Exito !!!");
                        cerrarForms();
                        oTable.getDataTable().ajax.reload();
                    } else {
                        toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                    }
                },
                failure: function (response) {
                    Metronic.unblockUI('#form-criticidad .portlet-body');

                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            });


        } else {

        }
    };

    //Funciones jquery
    //Editar
    var initAccionEditar = function () {
        $(document).on('click', "#criticidad-tarea-table-editable a.edit", function (e) {
            e.preventDefault();
            resetForms();

            var criticidad_id = $(this).data('id');
            $('#criticidad_id').val(criticidad_id);

            $('#form-criticidad').removeClass('ng-hide');
            $('#lista-criticidad').addClass('ng-hide');

            editRow(criticidad_id);
        });
        function editRow(criticidad_id) {

            Metronic.blockUI({target: '#form-criticidad .portlet-body', animate: true});

            $.ajax({
                type: "POST",
                url: "criticidad-tarea/cargarDatos",
                dataType: "json",
                data: {
                    'criticidad_id': criticidad_id
                },
                success: function (response) {
                    Metronic.unblockUI('#form-criticidad .portlet-body');
                    if (response.success) {

                        //Datos criticidads
                        var descripcion = response.criticidad.descripcion;
                        $('#descripcion').val(descripcion);
                        $('#nombre').val(response.criticidad.nombre);
                        $('#peso').val(response.criticidad.peso);
                        $('#color').val(response.criticidad.color);
                        $('#color').css("background-color", response.criticidad.color);
                        angularScope.var = response.criticidad.color;

                        if (!response.criticidad.estado) {
                            $('#estadoactivo').prop('checked', false);
                            $('#estadoinactivo').prop('checked', true);

                            jQuery.uniform.update('#estadoactivo');
                            jQuery.uniform.update('#estadoinactivo');
                        }

                        formTitle = "¿Deseas actualizar la criticidad de tarea \"" + descripcion + "\" ? Sigue los siguientes pasos:";
                        $('#form-criticidad-title').html(formTitle);

                    } else {
                        toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                    }
                },
                failure: function (response) {
                    Metronic.unblockUI('#form-criticidad .portlet-body');

                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            });

        }
    };
    //Eliminar
    var initAccionEliminar = function () {
        $(document).on('click', "#criticidad-tarea-table-editable a.delete", function (e) {
            e.preventDefault();

            rowDelete = $(this).data('id');
            $('#modal-eliminar').modal({
                'show': true
            });
        });
    };

    return {
        //main function to initiate the module
        init: function (scope) {
            angularScope = scope;
            initTable(scope);
            initForm();

            if (cont === 0) {
                initAccionEditar();
                initAccionEliminar();
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