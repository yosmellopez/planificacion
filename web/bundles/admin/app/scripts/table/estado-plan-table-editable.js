var TableEditableEstadoPlan = function () {

    var oTable;
    var rowDelete = null;
    var cont = 0;
    var formTitle = "¿Deseas crear un nuevo estado de plan? Sigue los siguientes pasos:";

    //Inicializa la tabla
    var initTable = function () {
        var table = $('#estado-plan-table-editable');

        var order = [[1, "asc"]];
        var aoColumns = [
            {"bSortable": false, "sWidth": '2%', "sClass": 'text-center'},
            {"bSortable": true, "sWidth": '68%', "sClass": 'form-control-text'},
            {"bSortable": true, "sWidth": '15%', "sClass": 'text-center'},
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
                    "url": "estado-plan/listarEstado", // ajax source
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

    //Para directiva ds-init-form-estado
    var initForm = function () {
        //Validacion  
        $("#estado-form").validate({
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
    };

    //Reset forms
    var resetForms = function () {
        $('#estado-form input').each(function (e) {
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
        var portlet = $('#form-estado .portlet');
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
        $('#form-estado').addClass('ng-hide');
        $('#lista-estado').removeClass('ng-hide');
        restablecerPortlet();
    };

    //Funciones para angular
    //Boton nuevo
    var btnClickNuevo = function () {
        resetForms();
        $('#form-estado-title').html("¿Deseas crear un nuevo estado de plan? Sigue los siguientes pasos:");
        $('#form-estado').removeClass('ng-hide');
        $('#lista-estado').addClass('ng-hide');
    };
    //Boton eliminar
    var btnClickEliminar = function () {
        var ids = '';
        $('.checkboxes').each(function () {
            if ($(this).prop('checked'))
                ids += $(this).attr('data-id') + ',';
        });
        if (ids != '') {
            $('#modal-eliminar-seleccion').modal({
                'show': true
            });
        } else {
            toastr.error('Seleccione los elementos a eliminar', "Error !!!");
        }
    };
    //Boton eliminar del modal
    var btnClickModalEliminar = function () {
        var estado_id = rowDelete;

        Metronic.blockUI({target: '#estado-plan-table-editable', animate: true});

        $.ajax({
            type: "POST",
            url: "estado-plan/eliminarEstado",
            dataType: "json",
            data: {
                'estado_id': estado_id
            },
            success: function (response) {
                Metronic.unblockUI('#estado-plan-table-editable');
                if (response.success) {
                    oTable.getDataTable().ajax.reload();

                    toastr.success(response.message, "Exito !!!");

                } else {
                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            },
            failure: function (response) {
                Metronic.unblockUI('#estado-plan-table-editable');

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

        Metronic.blockUI({target: '#estado-plan-table-editable', animate: true});

        $.ajax({
            type: "POST",
            url: "estado-plan/eliminarEstados",
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify({
                'ids': ids
            }),
            success: function (response) {
                Metronic.unblockUI('#estado-plan-table-editable');
                if (response.success) {

                    oTable.getDataTable().ajax.reload();
                    toastr.success(response.message, "Exito !!!");

                } else {
                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            },
            failure: function (response) {
                Metronic.unblockUI('#estado-plan-table-editable');

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

        if ($('#estado-form').valid()) {

            var estado_id = $('#estado_id').val();

            var descripcion = $('#descripcion').val();
            var estado = ($('#estadoactivo').prop('checked')) ? true : false;

            Metronic.blockUI({target: '#form-estado .portlet-body', animate: true});

            $.ajax({
                type: "POST",
                url: "estado-plan/salvarEstado",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: JSON.stringify({
                    'estado_id': estado_id === "" ? null : estado_id,
                    'descripcion': descripcion,
                    'estado': estado
                }),
                success: function (response) {
                    Metronic.unblockUI('#form-estado .portlet-body');
                    if (response.success) {

                        toastr.success(response.message, "Exito !!!");

                        cerrarForms();
                        oTable.getDataTable().ajax.reload();
                        ;

                    } else {
                        toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                    }
                },
                failure: function (response) {
                    Metronic.unblockUI('#form-estado .portlet-body');

                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            });


        } else {

        }
    };

    //Funciones jquery    
    //Editar
    var initAccionEditar = function () {
        $(document).on('click', "#estado-plan-table-editable a.edit", function (e) {
            e.preventDefault();
            resetForms();

            var estado_id = $(this).data('id');
            $('#estado_id').val(estado_id);

            $('#form-estado').removeClass('ng-hide');
            $('#lista-estado').addClass('ng-hide');

            editRow(estado_id);
        });

        function editRow(estado_id) {

            Metronic.blockUI({target: '#form-estado .portlet-body', animate: true});

            $.ajax({
                type: "POST",
                url: "estado-plan/cargarDatos",
                dataType: "json",
                data: {
                    'estado_id': estado_id
                },
                success: function (response) {
                    Metronic.unblockUI('#form-estado .portlet-body');
                    if (response.success) {

                        //Datos estados
                        var descripcion = response.estado.descripcion;
                        $('#descripcion').val(descripcion);

                        if (!response.estado.estado) {
                            $('#estadoactivo').prop('checked', false);
                            $('#estadoinactivo').prop('checked', true);

                            jQuery.uniform.update('#estadoactivo');
                            jQuery.uniform.update('#estadoinactivo');
                        }

                        formTitle = "¿Deseas actualizar el estado de plan \"" + descripcion + "\" ? Sigue los siguientes pasos:";
                        $('#form-estado-title').html(formTitle);

                    } else {
                        toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                    }
                },
                failure: function (response) {
                    Metronic.unblockUI('#form-estado .portlet-body');

                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            });

        }
    };
    //Eliminar
    var initAccionEliminar = function () {
        $(document).on('click', "#estado-plan-table-editable a.delete", function (e) {
            e.preventDefault();

            rowDelete = $(this).data('id');
            $('#modal-eliminar').modal({
                'show': true
            });
        });
    };

    return {
        //main function to initiate the module
        init: function () {

            initTable();
            initForm();

            if (cont == 0) {
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