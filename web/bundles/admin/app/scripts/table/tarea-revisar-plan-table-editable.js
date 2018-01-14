var TableEditableTareas = function () {

    var oTable;
    var rowDelete = null;
    var cont = 0;
    var formTitle = "¿Deseas crear una nueva tarea? Sigue los siguientes pasos:";

    //Inicializa la tabla
    var initTable = function () {
        var table = $('#tarea-table-editable');

        var order = [[3, "desc"]];
        var aoColumns = [
            {"bSortable": false, "sWidth": '5%', "sClass": 'text-center'},
            {"bSortable": true, "sWidth": '15%', "sClass": 'text-center'},
            {"bSortable": true},
            {"bSortable": true, "sWidth": '15%', "sClass": 'text-center'},
            {"bSortable": false, "sWidth": '15%', "sClass": 'text-center'}
        ];

        oTable = new Datatable();

        oTable.setAjaxParam("fechaInicio", $('#fechaInicio').val());
        oTable.setAjaxParam("fechaFin", $('#fechaFin').val());

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
                "serverSide": true,
                "lengthMenu": [
                    [15, 25, 30, 50, -1],
                    [15, 25, 30, 50, "Todos"]
                ],
                "pageLength": 15, // default record count per page
                "ajax": {
                    "url": "tarea/listarTareas", // ajax source
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

    //Init form
    var initForm = function () {
        //Validacion
        $("#tarea-form").validate({
            rules: {
                titulo: {
                    required: true
                },
                codigo: {
                    required: true
                }
            },
            messages: {
                titulo: {
                    required: "Este campo es obligatorio"
                },
                codigo: {
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

        //Ajax form
        var opciones_form = {
            beforeSubmit: function () {
                Metronic.blockUI({
                    target: '#form-tarea',
                    animate: true
                });
            }, //funcion que se ejecuta antes de enviar el form
            success: function (response) {
                Metronic.unblockUI('#form-tarea');
                if (response.success) {

                    toastr.success(response.message, "Exito !!!");

                    cerrarForms();
                    oTable.getDataTable().ajax.reload();

                    actualizarTareas(response.tareas);

                } else {
                    toastr.error(response.error, "Error !!!");
                }
            },
            dataType: 'json',
            resetForm: false
        };
        $('#tarea-form').ajaxForm(opciones_form);
    };

    //Reset forms
    var resetForms = function () {
        $('#tarea-form input').each(function (e) {
            $element = $(this);
            $element.val('');

            $element.data("title", "").removeClass("has-error").tooltip("destroy");
            $element.closest('.form-group').removeClass('has-error').addClass('success');
        });

        $('#tab-general a').click();
    };
    //Restablecer portlet
    var restablecerPortlet = function () {
        var portlet = $('#form-tarea .portlet');
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
        $('#form-tarea').addClass('ng-hide');
        $('#lista-tarea').removeClass('ng-hide');
        restablecerPortlet();
    };

    //Funciones para angular
    //Boton nuevo
    var btnClickNuevo = function () {
        resetForms();
        $('#form-tarea-title').html(formTitle);
        $('#form-tarea').removeClass('ng-hide');
        $('#lista-tarea').addClass('ng-hide');
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
        var nRow = rowDelete;
        var tarea_id = $(nRow).attr('id');

        Metronic.blockUI({target: '#tarea-table-editable', animate: true});

        $.ajax({
            type: "POST",
            url: "tarea/eliminarTarea",
            dataType: "json",
            data: {
                'tarea_id': tarea_id
            },
            success: function (response) {
                Metronic.unblockUI('#tarea-table-editable');
                if (response.success) {
                    oTable.fnDeleteRow(nRow);

                    toastr.success(response.message, "Exito !!!");

                    actualizarTareas(response.tareas);

                } else {
                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            },
            failure: function (response) {
                Metronic.unblockUI('#tarea-table-editable');

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

        Metronic.blockUI({target: '#tarea-table-editable', animate: true});

        $.ajax({
            type: "POST",
            url: "tarea/eliminarTareas",
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify({
                'ids': ids
            }),
            success: function (response) {
                Metronic.unblockUI('#tarea-table-editable');
                if (response.success) {

                    oTable.getDataTable().ajax.reload();
                    toastr.success(response.message, "Exito !!!");

                    actualizarTareas(response.tareas);

                } else {
                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            },
            failure: function (response) {
                Metronic.unblockUI('#tarea-table-editable');

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


        if ($('#tarea-form').valid()) {

            $('#sucesoras').val(devolverSucesoras());
            $('#predecesoras').val(devolverPredecesoras());

            $('#tarea-form').submit();

        } else {

        }

        function devolverSucesoras() {
            var string = '';
            for (var i = 0; i < sucesoras.length; i++) {
                string += "(" + sucesoras[i].tarea_sucesora_id + "," + sucesoras[i].sucesora_id + ")";
            }

            return string;
        }

        function devolverPredecesoras() {
            var string = '';
            for (var i = 0; i < predecesoras.length; i++) {
                string += "(" + predecesoras[i].tarea_predecesora_id + "," + predecesoras[i].predecesora_id + ")";
            }

            return string;
        }
    };

    //Funciones jquery    
    //Editar
    var initAccionEditar = function () {
        $(document).on('click', "#tarea-table-editable a.edit", function (e) {
            e.preventDefault();
            resetForms();

            var nRow = $(this).parents('tr')[0];
            var tarea_id = $(nRow).attr('id');
            $('#tarea_id').val(tarea_id);

            $('#form-tarea').removeClass('ng-hide');
            $('#lista-tarea').addClass('ng-hide');

            editRow(tarea_id);
        });

        function editRow(tarea_id) {

            Metronic.blockUI({target: '#form-tarea .portlet-body', animate: true});

            $.ajax({
                type: "POST",
                url: "tarea/cargarDatos",
                dataType: "json",
                data: {
                    'tarea_id': tarea_id
                },
                success: function (response) {
                    Metronic.unblockUI('#form-tarea .portlet-body');
                    if (response.success) {

                        formTitle = "Deseas actualizar la tarea \"" + response.tarea.titulo + "\" ? Sigue los siguientes pasos:";
                        $('#form-tarea-title').html(formTitle);

                        //Datos tarea
                        $('#titulo').val(response.tarea.titulo);
                        $('#codigo').val(response.tarea.codigo);

                        sucesoras = response.tarea.sucesoras;
                        dibujarTablaSucesoras();

                        predecesoras = response.tarea.predecesoras;
                        dibujarTablaPredecesoras();

                    } else {
                        toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                    }
                },
                failure: function (response) {
                    Metronic.unblockUI('#form-tarea .portlet-body');

                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            });

        }
    };
    //Eliminar
    var initAccionEliminar = function () {
        $(document).on('click', "#tarea-table-editable a.delete", function (e) {
            e.preventDefault();
            var nRow = $(this).parents('tr')[0];
            rowDelete = nRow;
            $('#modal-eliminar').modal({
                'show': true
            });
        });
    };

    //Boton filtrar
    var btnClickFiltrar = function () {

        oTable.setAjaxParam("fechaInicio", $('#fechaInicio').val());
        oTable.setAjaxParam("fechaFin", $('#fechaFin').val());

        oTable.getDataTable().ajax.reload();
    };

    //Inicializar fechas de filtro
    var initFechasFiltro = function () {
        //poner la fecha actual
        fecha_actual = new Date();
        var fechaInicio = MyApp.restarDiasAFecha(fecha_actual.format('d/m/Y'), 30);
        $('#fechaInicio').val(fechaInicio.format('d/m/Y'));

        var fechaFin = MyApp.sumarDiasAFecha(fecha_actual.format('d/m/Y'), 30);
        $('#fechaFin').val(fechaFin.format('d/m/Y'));
    };

    //Actualizar select tareas
    var actualizarTareas = function (tareas) {
        //Limipiar select tareas
        $('#sucesora option').each(function (e) {
            if ($(this).val() != "")
                $(this).remove();
        });
        $('#sucesora').select2();
        $('#predecesora option').each(function (e) {
            if ($(this).val() != "")
                $(this).remove();
        });
        $('#predecesora').select2();

        //Llenar tareas
        for (var i = 0; i < tareas.length; i++) {
            var id = tareas[i].tarea_id;
            var descripcion = tareas[i].codigo + ' - ' + tareas[i].titulo;

            $('#sucesora').append(new Option(descripcion, id, false, false));
            $('#predecesora').append(new Option(descripcion, id, false, false));

        }
        $('#sucesora').select2();
        $('#predecesora').select2();
    }

    //Sucesoras
    var sucesoras = new Array();
    var dibujarTablaSucesoras = function () {

        var tabla_sucesoras = "#table-sucesoras";

        if (sucesoras.length > 0)
            $(tabla_sucesoras).removeClass('hide');
        else
            $(tabla_sucesoras).addClass('hide');

        //Limpiar tabla_sucesoras
        $(tabla_sucesoras + ' tbody tr').each(function (e) {
            $(this).remove();
        });
        //Agregar elementos
        for (var i = 0; i < sucesoras.length; i++) {

            var acciones = '<a class="btn btn-icon-only red delete" href="javascript:;"><i class="fa fa-trash-o fa-fw"></i></a>';

            var tr = '<tr id="' + i + '"><td>' + (i + 1) + '</td>' +
                    '<td class="text-center">' + sucesoras[i].tarea_sucesora + '</td>' +
                    '<td class="text-center">' + acciones + '</td>' +
                    '</tr>';
            $(tr).appendTo(tabla_sucesoras + ' tbody');
        }

        $(tabla_sucesoras + ' .delete') // Destroy any pre-existing tooltip so we can repopulate with new tooltip content
                .data("title", 'Eliminar registro')
                .tooltip();
    };
    var ExisteSucesora = function (tarea_sucesora_id) {
        var result = false;
        for (var i = 0; i < sucesoras.length; i++) {
            if (sucesoras[i].tarea_sucesora_id == tarea_sucesora_id) {
                result = true;
                break;
            }
        }
        return result;
    };
    var btnClickSalvarSucesora = function () {

        var tarea_sucesora_id = $('#sucesora').val();

        if (tarea_sucesora_id != "") {
            if (!ExisteSucesora(tarea_sucesora_id)) {

                var $element = $('.selectpicker');
                $element.removeClass('has-error').tooltip("destroy");

                $element.closest('.form-group')
                        .removeClass('has-error');

                var tarea_sucesora = $('#sucesora option:selected').text();

                sucesoras.push({
                    tarea_sucesora_id: tarea_sucesora_id,
                    tarea_sucesora: tarea_sucesora,
                    sucesora_id: ""
                });

                resetFormSucesora();
                dibujarTablaSucesoras();
            } else {
                toastr.error("Ya se ha agregado la tarea", "Error !!!");
            }

        } else {
            if (tarea_sucesora_id == "") {
                var $element = $('#select-sucesora .selectpicker');
                $element.tooltip("destroy") // Destroy any pre-existing tooltip so we can repopulate with new tooltip content
                        .data("title", "Este campo es obligatorio")
                        .addClass("has-error")
                        .tooltip({
                            placement: 'bottom'
                        }); // Create a new tooltip based on the error messsage we just set in the title

                $element.closest('.form-group')
                        .removeClass('has-success').addClass('has-error');
            }
        }
    };
    var initTableSucesora = function () {
        function deleteSucesora(nRow) {
            var posicion = $(nRow).attr('id');
            //Eliminar sucesora
            sucesoras.splice(posicion, 1);

            dibujarTablaSucesoras();
            resetFormSucesora();
        }

        $(document).on('click', "#table-sucesoras a.delete", function (e) {

            e.preventDefault();
            var nRow = $(this).parents('tr')[0];
            var posicion = $(nRow).attr('id');

            if (sucesoras[posicion]) {
                var sucesora_id = sucesoras[posicion].sucesora_id;
                if (sucesora_id != "") {

                    Metronic.blockUI({target: '#table-sucesoras', animate: true});

                    $.ajax({
                        type: "POST",
                        url: "tarea/eliminarSucesora",
                        dataType: "json",
                        data: {
                            'sucesora_id': sucesora_id
                        },
                        success: function (response) {
                            Metronic.unblockUI('#table-sucesoras');
                            if (response.success) {

                                toastr.success(response.message, "Exito !!!");
                                deleteSucesora(nRow);
                            } else {
                                toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                            }
                        },
                        failure: function (response) {
                            Metronic.unblockUI('#table-sucesoras');

                            toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                        }
                    });
                } else {
                    deleteSucesora(nRow);
                }
            }
        });
    };
    var btnClickMisTareas = function () {

    }
    var resetFormSucesora = function () {

        $('#sucesora').select2('val', '');

        var $element = $('.selectpicker');
        $element.removeClass('has-error').tooltip("destroy");

        $element.closest('.form-group')
                .removeClass('has-error');
    };

    //Predecesoras
    var predecesoras = new Array();
    var dibujarTablaPredecesoras = function () {

        var tabla_predecesoras = "#table-predecesoras";

        if (predecesoras.length > 0)
            $(tabla_predecesoras).removeClass('hide');
        else
            $(tabla_predecesoras).addClass('hide');

        //Limpiar tabla_predecesoras
        $(tabla_predecesoras + ' tbody tr').each(function (e) {
            $(this).remove();
        });
        //Agregar elementos
        for (var i = 0; i < predecesoras.length; i++) {

            var acciones = '<a class="btn btn-icon-only red delete" href="javascript:;"><i class="fa fa-trash-o fa-fw"></i></a>';

            var tr = '<tr id="' + i + '"><td>' + (i + 1) + '</td>' +
                    '<td class="text-center">' + predecesoras[i].tarea_predecesora + '</td>' +
                    '<td class="text-center">' + acciones + '</td>' +
                    '</tr>';
            $(tr).appendTo(tabla_predecesoras + ' tbody');
        }

        $(tabla_predecesoras + ' .delete') // Destroy any pre-existing tooltip so we can repopulate with new tooltip content
                .data("title", 'Eliminar registro')
                .tooltip();
    };
    var ExistePredecesora = function (tarea_predecesora_id) {
        var result = false;
        for (var i = 0; i < predecesoras.length; i++) {
            if (predecesoras[i].tarea_predecesora_id == tarea_predecesora_id) {
                result = true;
                break;
            }
        }
        return result;
    };
    var btnClickSalvarPredecesora = function () {

        var tarea_predecesora_id = $('#predecesora').val();

        if (tarea_predecesora_id != "") {
            if (!ExistePredecesora(tarea_predecesora_id)) {

                var $element = $('.selectpicker');
                $element.removeClass('has-error').tooltip("destroy");

                $element.closest('.form-group')
                        .removeClass('has-error');

                var tarea_predecesora = $('#predecesora option:selected').text();

                predecesoras.push({
                    tarea_predecesora_id: tarea_predecesora_id,
                    tarea_predecesora: tarea_predecesora,
                    predecesora_id: ""
                });

                resetFormPredecesora();
                dibujarTablaPredecesoras();
            } else {
                toastr.error("Ya se ha agregado la tarea", "Error !!!");
            }

        } else {
            if (tarea_predecesora_id == "") {
                var $element = $('#select-predecesora .selectpicker');
                $element.tooltip("destroy") // Destroy any pre-existing tooltip so we can repopulate with new tooltip content
                        .data("title", "Este campo es obligatorio")
                        .addClass("has-error")
                        .tooltip({
                            placement: 'bottom'
                        }); // Create a new tooltip based on the error messsage we just set in the title

                $element.closest('.form-group')
                        .removeClass('has-success').addClass('has-error');
            }
        }
    };
    var initTablePredecesora = function () {
        function deletePredecesora(nRow) {
            var posicion = $(nRow).attr('id');
            //Eliminar predecesora
            predecesoras.splice(posicion, 1);

            dibujarTablaPredecesoras();
            resetFormPredecesora();
        }

        $(document).on('click', "#table-predecesoras a.delete", function (e) {

            e.preventDefault();
            var nRow = $(this).parents('tr')[0];
            var posicion = $(nRow).attr('id');

            if (predecesoras[posicion]) {
                var predecesora_id = predecesoras[posicion].predecesora_id;
                if (predecesora_id != "") {

                    Metronic.blockUI({target: '#table-predecesoras', animate: true});

                    $.ajax({
                        type: "POST",
                        url: "tarea/eliminarPredecesora",
                        dataType: "json",
                        data: {
                            'predecesora_id': predecesora_id
                        },
                        success: function (response) {
                            Metronic.unblockUI('#table-predecesoras');
                            if (response.success) {

                                toastr.success(response.message, "Exito !!!");
                                deletePredecesora(nRow);
                            } else {
                                toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                            }
                        },
                        failure: function (response) {
                            Metronic.unblockUI('#table-predecesoras');

                            toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                        }
                    });
                } else {
                    deletePredecesora(nRow);
                }
            }
        });
    };
    var resetFormPredecesora = function () {

        $('#predecesora').select2('val', '');

        var $element = $('.selectpicker');
        $element.removeClass('has-error').tooltip("destroy");

        $element.closest('.form-group')
                .removeClass('has-error');
    };

    return {
        //main function to initiate the module
        init: function () {
            //Inicializar fechas de filtro
            initFechasFiltro();

            initTable();
            initForm();
            initTableSucesora();
            initTablePredecesora();
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
        btnClickSalvarForm: btnClickSalvarForm,
        btnClickFiltrar: btnClickFiltrar,
        btnClickMisTareas: btnClickMisTareas,
        btnClickSalvarSucesora: btnClickSalvarSucesora,
        btnClickSalvarPredecesora: btnClickSalvarPredecesora
    };

}();