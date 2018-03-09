/* global Metronic, CKEDITOR */

var TableEditableTareas = function () {

    var urlPath;
    var oTable;
    var rowDelete = null;
    var cont = 0;
    var idTareaEditar = 0;
    var scopeTarea = null;
    var formTitle = "¿Deseas crear una nueva tarea? Sigue los siguientes pasos:";

    //Inicializa la tabla
    var initTable = function (scope) {
        scopeTarea = scope;
        var table = $('#tarea-table-editable');

        var order = [[1, "desc"]];
        var aoColumns = [
            {"bSortable": false, "sWidth": '1%', "sClass": 'text-center'},
            {"bSortable": true, "sWidth": '45%', "sClass": 'text-center'},
            {"bSortable": true, "sWidth": '35%'},
            {"bSortable": true, "sWidth": '10%', "sClass": 'text-center'},
            {"bSortable": false, "sWidth": '15%', "sClass": 'text-center'}
        ];
        CKEDITOR.replace('editor');
        oTable = new Datatable();
        oTable.setAjaxParam("filtrar", false);

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
                    "url": "tarea/listarTareas" // ajax source
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
    }
    ;

    //Init form
    var initForm = function () {
        //Validacion
        $("#tarea-form").validate({
            rules: {
                nombre: {
                    required: true
                },
                criticidad: {
                    required: true
                }
            },
            messages: {
                nombre: {
                    required: "Este campo es obligatorio"
                },
                criticidad: {
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
        $('#tarea-form input, #tarea-form textarea').each(function (e) {
            $element = $(this);
            $element.val('');

            $element.data("title", "").removeClass("has-error").tooltip("destroy");
            $element.closest('.form-group').removeClass('has-error').addClass('success');
        });
        $('#canales').select2("val", "");
        CKEDITOR.instances.editor.setData('');
        var $element = $('.selectpicker');
        $element.removeClass('has-error').tooltip("destroy");

        $element.closest('.form-group')
                .removeClass('has-error');

        //Reset archivos
        resetFormModelo();
        modelos = new Array();
        dibujarTablaModelos("#table-modelos");

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
        idTareaEditar = 0;
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
        var tarea_id = rowDelete;
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
                    oTable.getDataTable().ajax.reload();
                    toastr.success(response.message, "Exito !!!");
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
            Metronic.blockUI({target: '#form-tarea .portlet-body', animate: true});
            var tarea_id = $('#tarea_id').val();
            var nombre = $('#nombre').val();
            var producto = $('#producto').val();
            var descripcion = CKEDITOR.instances.editor.getData();
            var canales = $('#canales').val();
            if (canales === null || canales === "")
                canales = new Array();
            $.ajax({
                type: tarea_id === "" ? "POST" : "PUT",
                url: tarea_id === "" ? "tarea/salvarTarea" : "tarea/salvarTarea/" + tarea_id,
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: JSON.stringify({
                    'tarea_id': tarea_id === "" ? null : tarea_id,
                    'producto': producto,
                    'nombre': nombre,
                    'descripcion': descripcion,
                    'modelos': modelos,
                    'canales_id': canales
                }),
                success: function (response) {
                    Metronic.unblockUI('#form-tarea .portlet-body');
                    if (response.success) {
                        CKEDITOR.instances.editor.setData('');
                        toastr.success(response.message, "Exito !!!");
                        cerrarForms();
                        oTable.getDataTable().ajax.reload();
                        oTable.getDataTable().clear().draw();
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

    //Funciones jquery    
    //Editar
    var initAccionEditar = function () {
        $(document).on('click', "#tarea-table-editable a.edit", function (e) {
            e.preventDefault();
            resetForms();
            var tarea_id = $(this).data('id');
            idTareaEditar = tarea_id;
            $('#tarea_id').val(tarea_id);
            $('#form-tarea').removeClass('ng-hide');
            $('#lista-tarea').addClass('ng-hide');
            editRow(tarea_id);
        });

        function editRow(tarea_id) {
            Metronic.blockUI({target: '#form-tarea .portlet-body', animate: true});
            $.ajax({
                type: "POST",
                url: "tarea/cargarDatos/" + tarea_id,
                dataType: "json",
                data: {
                    'tarea_id': tarea_id
                },
                success: function (response) {
                    Metronic.unblockUI('#form-tarea .portlet-body');
                    if (response.success) {
                        formTitle = "Deseas actualizar la tarea \"" + response.tarea.nombre + "\" ? Sigue los siguientes pasos:";
                        $('#form-tarea-title').html(formTitle);
                        $('#nombre').val(response.tarea.nombre);
                        $('#producto').val(response.tarea.producto);

                        CKEDITOR.instances.editor.setData(response.tarea.descripcion);
                        canales_id = response.tarea.canales_id;
                        canales = new Array();
                        for (i = 0; i < canales_id.length; i++)
                            canales.push(canales_id[i].canal_id);
                        selectCanales = $('#canales').select2();
                        selectCanales.val(canales).trigger("change");
                        modelos = response.tarea.modelos;
                        dibujarTablaModelos("#table-modelos");
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

            rowDelete = $(this).data('id');
            $('#modal-eliminar').modal({
                'show': true
            });
        });
    };

    //Modelos
    var initFormModelo = function () {
        $("#modelo-form").validate({
            rules: {
                nombre: {
                    required: true
                },
                file: {
                    required: true
                }
            },
            messages: {
                nombre: {
                    required: "Este campo es obligatorio"
                },
                file: {
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
                    target: '#modelo-form',
                    animate: true
                });
            }, //funcion que se ejecuta antes de enviar el form
            success: function (response) {
                Metronic.unblockUI('#modelo-form');
                if (response.success) {

                    var archivo = response.name;
                    var ruta = response.ruta;
                    var descripcion = $('#modelo-descripcion').val();
                    var estado = ($('#estadoactivo').prop('checked')) ? 1 : 0;

                    modelos.push({
                        archivo: archivo,
                        descripcion: descripcion,
                        estado: estado,
                        ruta: ruta,
                        modelo_id: ""
                    });
                    resetFormModelo();
                    dibujarTablaModelos("#table-modelos");
                } else {
                    toastr.error(response.error, "Error !!!");
                }
            },
            dataType: 'json',
            resetForm: false
        };
        $('#modelo-form').ajaxForm(opciones_form);

    };
    var modelos = new Array();
    var nEditingRow = null;
    var dibujarTablaModelos = function (tabla_modelos) {
        //Limpiar tabla_modelos
        $(tabla_modelos + ' tbody tr').each(function (e) {
            $(this).remove();
        });
        //Agregar fila vacia
        if (modelos.length === 0) {
            var tr = '<tr>' +
                    '<td colspan="5">No existen archivos</td>' +
                    '</tr>';
            $(tr).appendTo(tabla_modelos + ' tbody');
        }
        //Agregar elementos
        for (var i = 0; i < modelos.length; i++) {
            var download_url = urlPath + "uploads/" + modelos[i].ruta;
            var acciones = '<a class="btn btn-icon-only green edit table-action" href="javascript:;"><i class="fa fa-edit fa-fw"></i></a> <a class="btn btn-icon-only red delete table-action" href="javascript:;"><i class="fa fa-trash-o fa-fw"></i></a> <a class="btn btn-icon-only yellow download table-action" href="' + download_url + '" target="_blank"><i class="fa fa-download fa-fw"></i></a>';
            var estado = (modelos[i].estado) ? 'Activo <i class="fa fa-check-circle ic-color-ok"></i>' : 'Inactivo <i class="fa fa-minus-circle ic-color-error"></i>';
            var tr = '<tr id="' + i + '">' +
                    '<td style="width: 3%;">' + (i + 1) + '</td>' +
                    '<td style="width: 20%;"><a href="uploads/' + modelos[i].ruta + '" target="_new">' + modelos[i].ruta + '</a></td>' +
                    '<td style="width: 50%;">' + (modelos[i].descripcion === null ? "" : modelos[i].descripcion) + '</td>' +
                    '<td style="width: 10%;">' + estado + '</td>' +
                    '<td style="width: 15%;" class="text-center">' + acciones + '</td>' +
                    '</tr>';
            $(tr).appendTo(tabla_modelos + ' tbody');
        }

        $(tabla_modelos + ' .edit') // Destroy any pre-existing tooltip so we can repopulate with new tooltip content
                .data("title", 'Editar archivo')
                .tooltip();

        $(tabla_modelos + ' .delete') // Destroy any pre-existing tooltip so we can repopulate with new tooltip content
                .data("title", 'Eliminar archivo')
                .tooltip();

        $(tabla_modelos + ' .download') // Destroy any pre-existing tooltip so we can repopulate with new tooltip content
                .data("title", 'Descargar archivo')
                .tooltip();
    };
    //Boton nuevo archivo
    var btnClickNuevoModelo = function () {
        resetFormModelo();
        var archivo = $('#archivo')[0];
        $(archivo).css("display", "none");
        $('#file').prop('required', true);
        $("#modal-modelo .modal-title").html("Nuevo Archivo");
        $('#modal-modelo').modal({
            'show': true
        });
    };
    //Boton salvar archivo
    var btnClickSalvarModelo = function () {
        if (nEditingRow === null) {
            if ($('#modelo-form').valid() && !$('#fileinput-archivo').fileinput().hasClass('fileinput-new')) {
                var formData = new FormData($('#modelo-form'));
                formData.append($("#file").attr("name"), $('#file').prop('files')[0]);
                formData.append($("#modelo-descripcion").attr("name"), $("#modelo-descripcion").val());
                formData.append($("#estadoactivo").attr("name"), $("#estadoactivo").prop('checked') ? true : false);
                formData.append("tareaId", idTareaEditar);
                $.ajax({
                    url: "tarea/salvarModelo",
                    type: 'POST',
                    xhr: function () {
                        myXhr = $.ajaxSettings.xhr();
                        if (myXhr.upload) {
                            myXhr.upload.addEventListener('progress', function (progress) {
                                // calculate upload progress
                                var percentage = Math.floor((progress.loaded / progress.total) * 100);
                                // log upload progress to console
                                var progressbar = $('#progressbar');
                                progressbar.val(percentage);
                                $('.progress-value').html("Subiendo... " + percentage + '%');
                                if (percentage === 100) {
                                    $('.progress-value').html(percentage + '%' + " Hecho!");
                                }
                            }, false);
                        }
                        return myXhr;
                    },
                    data: formData,
                    cache: false,
                    contentType: false,
                    processData: false,
                    complete: function (res) {
                        if (res.responseJSON.success) {
                            toastr.success("Archivo subido con éxito", "Error !!!");
                            var modelo = res.responseJSON.modelo;
                            modelos.push({
                                archivo: modelo.ruta,
                                descripcion: modelo.descripcion,
                                estado: modelo.estado,
                                ruta: modelo.ruta,
                                modelo_id: modelo.modelo_id
                            });
                            resetFormModelo();
                            dibujarTablaModelos("#table-modelos");
                            var progressbar = $('#progressbar');
                            progressbar.val(0);
                            $('.progress-value').html('0%');
                        } else
                            toastr.error(res.responseJSON.error, "Error !!!");
                    }
                });
            } else {
                if ($('#fileinput-archivo').fileinput().hasClass('fileinput-new')) {
                    var $element = $('#fileinput-archivo');
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
        } else {
            if ($('#modelo-form').valid()) {
                var descripcion = $('#modelo-descripcion').val();
                var nombre = $("#archivo").val();
                var estado = ($('#estadoactivo').prop('checked')) ? true : false;

                var posicion = $(nEditingRow).attr('id');
                if (modelos[posicion]) {
                    modelos[posicion].nombre = nombre;
                    modelos[posicion].descripcion = descripcion;
                    modelos[posicion].estado = estado;
                    if ($('#file').prop('files')[0]) {
                        var formData = new FormData($('#modelo-form'));
                        formData.append($("#file").attr("name"), $('#file').prop('files')[0]);
                        formData.append($("#modelo-descripcion").attr("name"), descripcion);
                        formData.append($("#estadoactivo").attr("name"), estado);
                        formData.append("tareaId", idTareaEditar);
                        formData.append("documentId", modelos[posicion].modelo_id);
                        $.ajax({
                            url: "tarea/salvarModeloTarea",
                            type: 'POST',
                            xhr: function () {
                                myXhr = $.ajaxSettings.xhr();
                                if (myXhr.upload) {
                                    myXhr.upload.addEventListener('progress', function (progress) {
                                        // calculate upload progress
                                        var percentage = Math.floor((progress.loaded / progress.total) * 100);
                                        // log upload progress to console
                                        var progressbar = $('#progressbar');
                                        progressbar.val(percentage);
                                        $('.progress-value').html("Subiendo... " + percentage + '%');
                                        if (percentage === 100) {
                                            $('.progress-value').html(percentage + '%' + " Hecho!");
                                        }
                                    }, false);
                                }
                                return myXhr;
                            },
                            data: formData,
                            cache: false,
                            contentType: false,
                            processData: false,
                            complete: function (res) {
                                if (res.responseJSON.success) {
                                    $('#fileinput-archivo').fileinput().removeClass("fileinput-new");
                                    var modelo = res.responseJSON.modelo;
                                    modelos[posicion] = {
                                        archivo: modelo.ruta,
                                        descripcion: modelo.descripcion,
                                        estado: modelo.estado,
                                        ruta: modelo.ruta,
                                        modelo_id: modelo.modelo_id
                                    };
                                    resetFormModelo();
                                    dibujarTablaModelos("#table-modelos");
                                    var progressbar = $('#progressbar');
                                    progressbar.val(0);
                                    $('.progress-value').html('0%');
                                } else
                                    toastr.error(res.responseJSON.error, "Error !!!");
                            }
                        });
                    } else if (nombre !== "") {
                        var idArchivo = $('#nombre-archivo')[0];
                        var modelo_id = $(idArchivo).val();
                        $.ajax({
                            type: "POST",
                            url: "documento/salvarArchivo",
                            dataType: "json",
                            contentType: "application/json;charset=utf-8",
                            data: JSON.stringify({
                                'modelo_id': modelo_id,
                                'nombre': nombre,
                                'descripcion': descripcion,
                                'activo': estado
                            }),
                            success: function (response) {
                                Metronic.unblockUI('#form-tarea .portlet-body');
                                if (response.success) {
                                    CKEDITOR.instances.editor.setData('');
                                    toastr.success(response.message, "Exito !!!");
                                    resetFormModelo();
                                    dibujarTablaModelos("#table-modelos");
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
                }
            }
        }
    };
    //Acciones de la tabla de archivo
    var initTableModelo = function () {
        function deleteModelo(nRow) {
            var posicion = $(nRow).attr('id');
            //Eliminar archivo
            modelos.splice(posicion, 1);
            dibujarTablaModelos("#table-modelos");
            resetFormModelo();
        }

        $('input[name=file]').change(function (ev) {
            var archivo = $('#archivo')[0];
            $(archivo).css("display", "none");
        });

        $(document).on('click', "#table-modelos a.edit", function (e) {
            e.preventDefault();
            var nRow = $(this).parents('tr')[0];
            var posicion = $(nRow).attr('id');

            if (modelos[posicion]) {

                //Reset form archivo
                resetFormModelo();
                nEditingRow = nRow;

                var nombre = modelos[posicion].ruta;
                var archivo = $('#archivo')[0];
                var idArchivo = $('#nombre-archivo')[0];
                $(idArchivo).val(modelos[posicion].modelo_id);
                $(archivo).css("display", "block");
                $(archivo).val(nombre);
                $('#file').prop('required', false);
                var descripcion = modelos[posicion].descripcion;
                $('#modelo-descripcion').val(descripcion);

                var estado = modelos[posicion].estado;
                if (estado === 0) {
                    $('#estadoactivo').prop('checked', false);
                    $('#estadoinactivo').prop('checked', true);

                    jQuery.uniform.update('#estadoactivo');
                    jQuery.uniform.update('#estadoinactivo');
                }
                $("#modal-modelo .modal-title").html("Editar Archivo - " + nombre);
                $('#modal-modelo').modal({
                    'show': true
                });
            }
        });
        $(document).on('click', "#table-modelos a.delete", function (e) {
            e.preventDefault();
            var nRow = $(this).parents('tr')[0];
            var posicion = $(nRow).attr('id');
            if (modelos[posicion]) {
                var archivo = modelos[posicion];
                Metronic.blockUI({target: '#table-modelos', animate: true});
                $.ajax({
                    type: "POST",
                    url: "tarea/eliminarModelo?modelo_id=" + archivo.modelo_id,
                    dataType: "json",
                    data: {
                        'modelo_id': archivo.modelo_id
                    },
                    success: function (response) {
                        Metronic.unblockUI('#table-modelos');
                        if (response.success) {
                            toastr.success(response.message, "Exito !!!");
                            deleteModelo(nRow);
                        } else {
                            toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                        }
                    },
                    failure: function (response) {
                        Metronic.unblockUI('#table-modelos');
                        toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                    }
                });
            }
        });
    };

    //Reset form archivo
    var resetFormModelo = function () {
        $('#modelo-form input, #modelo-form textarea').each(function (e) {
            $element = $(this);
            $element.val('');
            $element.data("title", "").removeClass("has-error").tooltip("destroy");
            $element.closest('.form-group').removeClass('has-error').addClass('success');
        });
        $('#estadoactivo').prop('checked', true);
        $('#estadoinactivo').prop('checked', false);
        jQuery.uniform.update('#estadoactivo');
        jQuery.uniform.update('#estadoinactivo');
        $('#fileinput-archivo').fileinput('clear');
        nEditingRow = null;
        $('#modal-modelo').modal('hide');
    };


    //Buscar Tareas
    var buscarTareas = function (url) {
        oTable.getDataTable().ajax.url(url);
        oTable.getDataTable().ajax.reload();
    };


    return {
        //main function to initiate the module
        init: function (url, scope) {
            urlPath = url;
            //Inicializar fechas de filtro
            initTable(scope);
            initForm();
            initFormModelo();
            if (cont === 0) {
                initAccionEditar();
                initAccionEliminar();
                initTableModelo();
            }
            cont++;
        },
        btnClickNuevo: btnClickNuevo,
        btnClickEliminar: btnClickEliminar,
        btnClickModalEliminar: btnClickModalEliminar,
        btnClickModalEliminarSeleccion: btnClickModalEliminarSeleccion,
        btnClickCerrarForm: btnClickCerrarForm,
        btnClickSalvarForm: btnClickSalvarForm,
        btnClickNuevoModelo: btnClickNuevoModelo,
        btnClickSalvarModelo: btnClickSalvarModelo,
        buscarTareas: buscarTareas
    };

}();