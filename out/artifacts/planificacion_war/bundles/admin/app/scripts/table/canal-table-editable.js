var TableEditableCanal = function () {

    var oTable;
    var rowDelete = null;
    var cont = 0;
    var formTitle = "¿Deseas crear un nuevo canal? Sigue los siguientes pasos:";

    //Inicializar table
    var initTable = function () {
        var table = $('#canal-table-editable');

        var order = [[1, "asc"]];
        var aoColumns = [
            {"bSortable": false, "sWidth": '1%', "sClass": 'text-center'},
            {"bSortable": true, "sWidth": '45%'},
            {"bSortable": true, "sWidth": '45%'},
            {"bSortable": false, "sWidth": '9%', "sClass": 'text-center'}
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
                    "url": "canal/listarCanales" // ajax source
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
        $('#canal-form input').each(function (e) {
            $element = $(this);
            $element.val('');

            $element.data("title", "").removeClass("has-error").tooltip("destroy");
            $element.closest('.form-group').removeClass('has-error').addClass('success');
        });

        $('#gerencia').select2('val', '');

    };
    //Restablecer portlet
    var restablecerPortlet = function () {
        var portlet = $('#form-canal .portlet');
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
        $('#form-canal').addClass('ng-hide');
        $('#lista-canal').removeClass('ng-hide');
        restablecerPortlet();
    };
    //Validacion
    var initForm = function () {
        //Validacion
        $("#canal-form").validate({
            rules: {
                descripcion: {
                    required: true
                },
                nombre: {
                    required: true
                }
            },
            messages: {
                descripcion: {
                    required: "Este campo es obligatorio"
                },
                nombre: {
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

    //Funciones para angular
    //Boton nuevo
    var btnClickNuevo = function () {
        resetForms();
        $('#form-canal-title').html(formTitle);
        $('#form-canal').removeClass('ng-hide');
        $('#lista-canal').addClass('ng-hide');
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
            toastr.error('Select items to remove', "Error !!!");
        }
    };
    //Boton eliminar del modal
    var btnClickModalEliminar = function () {
        var canal_id = rowDelete;

        Metronic.blockUI({target: '#canal-table-editable', animate: true});

        $.ajax({
            type: "POST",
            url: "canal/eliminarCanal",
            dataType: "json",
            data: {
                'canal_id': canal_id
            },
            success: function (response) {
                Metronic.unblockUI('#canal-table-editable');

                if (response.success) {
                    oTable.getDataTable().ajax.reload();

                    toastr.success(response.message, "Exito !!!");

                } else {
                    toastr.error(response.error, "Error !!!");
                }
            },
            failure: function (response) {
                Metronic.unblockUI('#canal-table-editable');

                toastr.error(response.error, "Error !!!");
            }
        });
    };
    //Boton eliminar del modal
    var btnClickModalEliminarSeleccion = function () {
        var ids = new Arrary();
        $('.checkboxes').each(function () {
            if ($(this).prop('checked'))
                ids.push(parseInt($(this).attr('data-id')));
        });
        Metronic.blockUI({target: '#canal-table-editable', animate: true});
        $.ajax({
            type: "POST",
            url: "canal/eliminarCanales",
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify({
                'ids': ids
            }),
            success: function (response) {
                Metronic.unblockUI('#canal-table-editable');
                if (response.success) {

                    oTable.getDataTable().ajax.reload();
                    toastr.success(response.message, "Exito !!!");

                } else {
                    toastr.error(response.error, "Error !!!");
                }
            },
            failure: function (response) {
                Metronic.unblockUI('#canal-table-editable');

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
        if ($('#canal-form').valid()) {

            var canal_id = $('#canal_id').val();

            var nombre = $('#nombre').val();
            var descripcion = $('#descripcion').val();

            Metronic.blockUI({target: '#form-canal .portlet-body', animate: true});

            $.ajax({
                type: "POST",
                url: "canal/salvarCanal",
                contentType: "application/json;charset=utf-8",
                dataType: "json",
                data: JSON.stringify({
                    'canal_id': canal_id === "" ? null : canal_id,
                    'nombre': nombre,
                    'descripcion': descripcion
                }),
                success: function (response) {
                    Metronic.unblockUI('#form-canal .portlet-body');
                    if (response.success) {

                        toastr.success(response.message, "Exito !!!");
                        cerrarForms();
                        oTable.getDataTable().ajax.reload();
                    } else {
                        toastr.error(response.error, "Error !!!");
                    }
                },
                failure: function (response) {
                    Metronic.unblockUI('#form-canal .portlet-body');

                    toastr.error(response.error, "Error !!!");
                }
            });
        } 
    };

    //Funciones jquery
    //Editar
    var initAccionEditar = function () {
        $(document).on('click', "#canal-table-editable a.edit", function (e) {
            e.preventDefault();
            resetForms();

            var canal_id = $(this).data('id');
            $('#canal_id').val(canal_id);

            $('#form-canal').removeClass('ng-hide');
            $('#lista-canal').addClass('ng-hide');

            editRow(canal_id);
        });

        function editRow(canal_id) {

            Metronic.blockUI({
                target: '#form-canal .portlet-body',
                animate: true
            });

            $.ajax({
                type: "POST",
                url: "canal/cargarDatos",
                dataType: "json",
                data: {
                    'canal_id': canal_id
                },
                success: function (response) {
                    Metronic.unblockUI('#form-canal .portlet-body');
                    if (response.success) {
                        //Datos canal

                        $('#descripcion').val(response.canal.descripcion);
                        $('#nombre').val(response.canal.nombre);

                        formTitle = "Deseas actualizar el canal \"" + response.canal.descripcion + "\" ? Sigue los siguientes pasos:";
                        $('#form-canal-title').html(formTitle);

                    } else {
                        toastr.error(response.error, "Error !!!");
                    }
                },
                failure: function (response) {
                    Metronic.unblockUI('#form-canal .portlet-body');

                    toastr.error(response.error, "Error !!!");
                }
            });

        }
    };
    //Eliminar
    var initAccionEliminar = function () {
        $(document).on('click', "#canal-table-editable a.delete", function (e) {
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