/* global go, CKEDITOR, Metronic, NaN, LinkShiftingTool, TableLayout */

var TableEditablePlanes = function () {

    var urlPath;
    var oTable;
    var rowDelete = null;
    var currentPlanId = 0;
    var posicionTareaEditar = -1;
    var criticidadesTareas = new Array();
    var tareasImportadas = new Array();
    var relacionados = new Array();
    var planNuevo = true;
    var loggerSistema = {};
    var scopePlan = {};
    var tareaimportada = false;
    var idTareaImportar = 0;
    var idTareaEditar = 0;
    var cont = 0;
    var formTitle = "¿Deseas crear una nuevo plan? Sigue los siguientes pasos:";
    var servicioPlan = null;

    //Inicializa la tabla
    var initTable = function (planService, scope, logger) {
        servicioPlan = planService;
        scopePlan = scope;
        loggerSistema = logger;

        var table = $('#plan-table-editable');

        var order = [[4, "desc"]];
        var aoColumns = [
            {"bSortable": false, "sWidth": '2%', "sClass": 'text-center'},
            {"bSortable": true},
            {"bSortable": true, "sWidth": '30%'},
            {"bSortable": true, "sWidth": '10%'},
            {"bSortable": true, "sWidth": '10%', "sClass": 'text-center'},
            {"bSortable": false, "sWidth": '15%', "sClass": 'text-center'}
        ];
        CKEDITOR.replace('editor');
        oTable = new Datatable();

        oTable.setAjaxParam("fechaInicio", null);
        oTable.setAjaxParam("fechaFin", null);
        oTable.setAjaxParam("estado_id", null);
        oTable.setAjaxParam("filtrar", false);

        oTable.init({
            src: table,
            onSuccess: function (grid, a) {
                // execute some code after table records loaded
                setTimeout(function () {
                    $("#plan-table-editable a.diagrama") // Destroy any pre-existing tooltip so we can repopulate with new tooltip content
                        .data("title", 'Realizar diagrama')
                        .tooltip();
                    $("#plan-table-editable a.ver-plan") // Destroy any pre-existing tooltip so we can repopulate with new tooltip content
                        .data("title", 'Ver datos del plan')
                        .tooltip();
                    $("#plan-table-editable a.clonar") // Destroy any pre-existing tooltip so we can repopulate with new tooltip content
                        .data("title", 'Clonar plan')
                        .tooltip();
                }, 1000);
            },
            onError: function (grid) {
                // execute some code on network or other general error
            },
            loadingMessage: 'Por favor espere...',
            dataTable: {
                serverSide: true,
                "lengthMenu": [
                    [15, 25, 30, 50, -1],
                    [15, 25, 30, 50, "Todos"]
                ],
                "pageLength": 15, // default record count per page
                "ajax": {
                    "url": "plan/listarPlanes" // ajax source
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
        $("#plan-form").validate({
            rules: {
                descripcion: {
                    required: true
                },
                estado: {
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
                estado: {
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

                    $element.closest('.form-group')
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
                    $element.closest('.form-group').removeClass('has-success').addClass('has-error');
                });
            }
        });
    };

    //Reset forms
    var resetForms = function () {
        $('#plan-form input').each(function (e) {
            $element = $(this);
            $element.val('');

            $element.data("title", "").removeClass("has-error").tooltip("destroy");
            $element.closest('.form-group').removeClass('has-error').addClass('success');
        });
        $('#criticidad').select2('val', '');
        $('#estado').select2('val', '');

        var $element = $('.selectpicker');
        $element.removeClass('has-error').tooltip("destroy");

        $element.closest('.form-group')
            .removeClass('has-error');
        //Tareas
        var tareas = new Array();
        $('#tab-general a').click();
//        destroyDiagrama();
    };
    //Restablecer portlet
    var restablecerPortlet = function () {
        var portlet = $('#form-plan .portlet');
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
        $('#form-plan').addClass('ng-hide');
        $('#form-diagrama').addClass('ng-hide');
        $('#lista-plan').removeClass('ng-hide');
        destroyDiagrama();
        restablecerPortlet();
    };

    var initAccionClonar = function () {
        $(document).on('click', "#plan-table-editable a.clonar", function (e) {
            e.preventDefault();
            resetForms();
            $('#plan-clonado-nombre').val('');
            var plan_id = $(this).data('id');
            var nombre = $(this).data('nombre');
            $('#plan_clonado-titulo').html("Clonal Plan - " + nombre);
            showModalClone();
            $('#plan-clonado-id').val(plan_id);
        });

        function showModalClone() {
            $('#modal-clonar-plan').modal({
                'show': true
            });
        }
    };

    //Funciones para angular
    //Boton nuevo
    var btnClickClonarPlan = function () {
        var idPlan = $('#plan-clonado-id').val();
        var nombre = $('#plan-clonado-nombre').val();
        Metronic.blockUI({target: '#modal-clonar-plan', animate: true});

        $.ajax({
            type: "POST",
            url: "plan/clonarPlan",
            dataType: "json",
            data: {
                'plan_id': idPlan,
                'nombre': nombre
            },
            success: function (response) {
                Metronic.unblockUI('#modal-clonar-plan');
                if (response.success) {
                    $('#modal-clonar-plan').modal('hide');
                    toastr.success(response.message, "Exito !!!", {"positionClass": "toast-top-center"});
                    oTable.getDataTable().draw();
                } else {
                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            },
            failure: function (response) {
                Metronic.unblockUI('#form-diagrama .portlet-body');
                toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
            }
        });
    };
    //Funciones para angular
    //Boton nuevo
    var btnClickNuevo = function () {
        planNuevo = true;
        resetForms();
        $('#form-plan-title').html(formTitle);
        $('#form-plan').removeClass('ng-hide');
        $('#lista-plan').addClass('ng-hide');
        tareas = new Array();
        localStorage.removeItem("tareasImportadas");
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
        var plan_id = rowDelete;
        Metronic.blockUI({target: '#plan-table-editable', animate: true});
        $.ajax({
            type: "POST",
            url: "plan/eliminarPlan",
            dataType: "json",
            data: {
                'plan_id': plan_id
            },
            success: function (response) {
                Metronic.unblockUI('#plan-table-editable');
                if (response.success) {
                    oTable.getDataTable().clear().draw();
                    toastr.success(response.message, "Exito !!!");
                    //Actualizar filtro plan
                    actualizarPlanes(response.planes);
                } else {
                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            },
            failure: function (response) {
                Metronic.unblockUI('#plan-table-editable');
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
        Metronic.blockUI({target: '#plan-table-editable', animate: true});
        $.ajax({
            type: "POST",
            url: "plan/eliminarPlanes",
            dataType: "json",
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({ids: ids}),
            success: function (response) {
                Metronic.unblockUI('#plan-table-editable');
                if (response.success) {
                    oTable.getDataTable().clear().draw();
                    toastr.success(response.message, "Exito !!!");
                    //Actualizar filtro plan
                    actualizarPlanes(response.planes);
                } else {
                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            },
            failure: function (response) {
                Metronic.unblockUI('#plan-table-editable');
                toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
            }
        });
    };
    //Boton cerrar form
    var btnClickCerrarForm = function () {
        cerrarForms();
    };
    //Boton cerrar form
    var btnClickCerrarPlan = function () {
        resetForms();
        $('#form-plan').addClass('ng-hide');
        $('#view-plan').addClass('ng-hide');
        $('#form-diagrama').addClass('ng-hide');
        $('#lista-plan').removeClass('ng-hide');
        restablecerPortlet();
    };
    //Boton salvar nuevo form
    var btnClickSalvarForm = function () {
        Metronic.scrollTo($('.page-title'));
        var estado_id = $('#estado').val();

        if ($('#plan-form').valid() && estado_id !== "") {
            Metronic.blockUI({target: '#form-plan .portlet-body', animate: true});
            var plan_id = $('#plan_id').val();
            var descripcion = $('#descripcion').val();
            var nombre = $('#nombrePlan').val();
            if (planNuevo) {
                tareasString = localStorage.getItem("tareasImportadas");
                tareasImportadas = JSON.parse(tareasString);
            }
            $.ajax({
                type: "POST",
                url: "plan/salvarPlan",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data: JSON.stringify({
                    'plan_id': plan_id === "" ? null : plan_id,
                    'estado_id': estado_id,
                    'nombre': nombre,
                    'descripcion': descripcion,
                    'tareas': tareas
                }),
                success: function (response) {
                    Metronic.unblockUI('#form-plan .portlet-body');
                    if (response.success) {
                        toastr.success(response.message, "Exito !!!");
                        cerrarForms();
                        $("#cuerpo-plan").empty();
                        oTable.getDataTable().clear().draw();
                        //Actualizar filtro plan
//                        actualizarPlanes(response.planes);
                    } else {
                        toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                    }
                },
                failure: function (response) {
                    Metronic.unblockUI('#form-plan .portlet-body');
                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            });

        } else {
            if (estado_id === "") {
                var $element = $('#select-estado .selectpicker');
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
    var tablaTareasPlan = null;
    var initAccionEditar = function () {
        $(document).on('click', "#plan-table-editable a.edit", function (e) {
            e.preventDefault();
            resetForms();
            planNuevo = false;
            var plan_id = $(this).data('id');
            $('#plan_id').val(plan_id);
            $('#form-plan').removeClass('ng-hide');
            $('#lista-plan').addClass('ng-hide');
            tareaimportada = false;
            mostrarTareasPlan(plan_id);
            editRow(plan_id);
        });

        function editRow(plan_id) {
            localStorage.removeItem("tareasImportadas");
            currentPlanId = plan_id;
            scopePlan.planId = plan_id;
            Metronic.blockUI({target: '#form-plan .portlet-body', animate: true});
            $.ajax({
                type: "POST",
                url: "plan/cargarDatos",
                dataType: "json",
                data: {
                    'plan_id': plan_id
                },
                success: function (response) {
                    Metronic.unblockUI('#form-plan .portlet-body');
                    if (response.success) {
                        formTitle = "Deseas actualizar el plan \"" + response.plan.descripcion + "\" ? Sigue los siguientes pasos:";
                        $('#form-plan-title').html(formTitle);
                        //Datos plan
                        $('#descripcion').val(response.plan.descripcion);
                        $('#nombrePlan').val(response.plan.nombre);
                        $('#estado').select2('val', response.plan.estado_id);
                        criticidadesTareas = response.niveles;
                    } else {
                        toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                    }
                },
                failure: function (response) {
                    Metronic.unblockUI('#form-plan .portlet-body');
                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            });

        }

        function mostrarTareasPlan(planId) {
            var table = $('#table-tareas');
            var order = [[2, "desc"]];
            var aoColumns = [
                {"bSortable": false, "sWidth": '5%', "sClass": 'text-center'},
                {"bSortable": false, "sWidth": '35%'},
                {"bSortable": false, "sWidth": '20%'},
                {"bSortable": false, "sWidth": '20%'},
                {"bSortable": true, "sWidth": '5%', "sClass": 'text-center'},
                {"bSortable": false, "sWidth": '15%', "sClass": 'text-center'}
            ];
            tablaTareasPlan = new Datatable();
            tablaTareasPlan.setAjaxParam("planId", planId);
            tablaTareasPlan.setAjaxParam("cargo", null);
            tablaTareasPlan.setAjaxParam("gerencia", null);
            tablaTareasPlan.setAjaxParam("direccion", null);
            tablaTareasPlan.setAjaxParam("criticidad", null);
            tablaTareasPlan.init({
                src: table,
                onSuccess: function (grid) {
                    // execute some code after table records loaded
                    setTimeout(function () {
                        $("#table-tareas a.edit")
                            .data("title", 'Editar tarea')
                            .tooltip();
                        $("#table-tareas a.delete")
                            .data("title", 'Eliminar Tarea')
                            .tooltip();
                        $("#table-tareas a.partida")
                            .data("title", 'Seleccionar tarea partida')
                            .tooltip();
                    }, 1000);
                },
                onError: function (grid) {
                    // execute some code on network or other general error
                },
                loadingMessage: 'Por favor espere...',
                dataTable: {
                    "serverSide": false,
                    "destroy": true,
                    "lengthMenu": [
                        [15, 25, 30, 50, -1],
                        [15, 25, 30, 50, "Todos"]
                    ],
                    "pageLength": 15, // default record count per page
                    "ajax": {
                        "url": "planTarea/listarTareasPlan/" + planId // ajax source
                    },
                    "order": order,
                    responsive: {
                        details: {}
                    },
                    "aoColumns": aoColumns
                }
            });
            var tableWrapper = tablaTareasPlan.getTableWrapper();
            tableWrapper.find('.dataTables_filter input').addClass("form-control input-sm input-inline");
            tableWrapper.find('.dataTables_length select').select2({minimumResultsForSearch: Infinity});
        }
    };

    var filtrarTareasPlan = function (cargo, gerencia, direccion, criticidad) {
        tablaTareasPlan.setAjaxParam("planId", currentPlanId);
        tablaTareasPlan.setAjaxParam("cargo", cargo);
        tablaTareasPlan.setAjaxParam("gerencia", gerencia);
        tablaTareasPlan.setAjaxParam("direccion", direccion);
        tablaTareasPlan.setAjaxParam("criticidad", criticidad);
        tablaTareasPlan.getDataTable().ajax.reload();
    }

    //Eliminar
    var initAccionEliminar = function () {
        $(document).on('click', "#plan-table-editable a.delete", function (e) {
            e.preventDefault();
            rowDelete = $(this).data('id');
            $('#modal-eliminar').modal({
                'show': true
            });
        });
    };

    //Boton filtrar
    var btnClickFiltrar = function () {
        oTable.setAjaxParam("fechaInicio", $('#fechaInicio').val());
        oTable.setAjaxParam("fechaFin", $('#fechaFin').val());
        oTable.setAjaxParam("criticidad_id", $('#filtro-criticidad').val());
        oTable.setAjaxParam("estado_id", $('#filtro-estado').val());
        oTable.setAjaxParam("filtrar", true);
        oTable.getDataTable().ajax.reload();
    };

    //Inicializar fechas de filtro
    var initFechasFiltro = function () {
        //poner rango mes actual
        var fecha_actual = new Date();
        var fechaInicio = new Date(fecha_actual.getFullYear(), fecha_actual.getMonth(), 1);
        $('#fechaInicio').val(fechaInicio.format('d/m/Y'));
        $('#filtro-importar-fechaInicio').val(fechaInicio.format('d/m/Y'));
        var fechaFin = new Date(fecha_actual.getFullYear(), fecha_actual.getMonth() + 1, 0);
        $('#fechaFin').val(fechaFin.format('d/m/Y'));
        $('#filtro-importar-fechaFin').val(fechaFin.format('d/m/Y'));
    };

    //Importar tareas
    var oTableImportarTareas;
    var initTableImportarTareas = function () {
        var table = $('#importar-tarea-table-editable');

        var order = [[2, "desc"]];
        var aoColumns = [
            {"bSortable": false, "sWidth": '5%', "sClass": 'text-center'},
            {"bSortable": false, "sWidth": '5%', "sClass": 'text-center'},
            {"bSortable": false, "sWidth": '40%'},
            {"bSortable": true, "sWidth": '40%'},
            {"bSortable": true, "sWidth": '10%', "sClass": 'text-center'},
            {"bSortable": false, "sWidth": '5%', "sClass": 'text-center'}
        ];

        oTableImportarTareas = new Datatable();

        oTableImportarTareas.setAjaxParam("fechaInicio", $('#filtro-importar-fechaInicio').val());
        oTableImportarTareas.setAjaxParam("fechaFin", $('#filtro-importar-fechaFin').val());
        oTableImportarTareas.setAjaxParam("criticidad_id", $('#filtro-importar-criticidad').val());
        oTableImportarTareas.setAjaxParam("planNuevo", planNuevo);
        tareasImportadas = localStorage.getItem("tareasImportadas");
        if (tareasImportadas === null || tareasImportadas === undefined) {
            tareasImportadas = new Array();
            oTableImportarTareas.setAjaxParam("tareasImportadas", "");
        } else {
            tareasImportadas = JSON.parse(tareasImportadas);
            var idsTareasImportadas = new Array();
            tareasImportadas.forEach(function (item) {
                idsTareasImportadas.push(item.tarea_id);
            });
            oTableImportarTareas.setAjaxParam("tareasImportadas", JSON.stringify(idsTareasImportadas));
        }
        oTableImportarTareas.setAjaxParam("idPlan", currentPlanId);

        oTableImportarTareas.init({
            src: table,
            onSuccess: function (grid) {
                // execute some code after table records loaded
                setTimeout(function () {
                    $("#importar-tarea-table-editable a.add") // Destroy any pre-existing tooltip so we can repopulate with new tooltip content
                        .data("title", 'Importar tarea')
                        .tooltip();
                }, 1000);
            },
            onError: function (grid) {
                // execute some code on network or other general error
            },
            loadingMessage: 'Por favor espere...',
            dataTable: {
                "serverSide": false,
                "destroy": true,
                "lengthMenu": [
                    [15, 25, 30, 50, -1],
                    [15, 25, 30, 50, "Todos"]
                ],
                "pageLength": 15, // default record count per page
                "ajax": {
                    "url": "tarea/listarTareasParaImportar" // ajax source
                },
                "order": order,
                responsive: {
                    details: {}
                },
                "aoColumns": aoColumns
            }
        });
        var tableWrapper = oTableImportarTareas.getTableWrapper(); // datatable creates the table wrapper by adding with id {your_table_jd}_wrapper
        tableWrapper.find('.dataTables_filter input').addClass("form-control input-sm input-inline");
        tableWrapper.find('.dataTables_length select').select2({minimumResultsForSearch: Infinity}); // initialize select2 dropdown
    };
    var btnClickMostrarModalImportar = function () {
        initTableImportarTareas();
        $('#modal-importar').modal({
            'show': true
        });
    };
    var btnClickFiltrarImportarTareas = function () {
        oTableImportarTareas.setAjaxParam("fechaInicio", $('#filtro-importar-fechaInicio').val());
        oTableImportarTareas.setAjaxParam("fechaFin", $('#filtro-importar-fechaFin').val());
    };
    var initAccionImportar = function () {
        $(document).on('click', "#importar-tarea-table-editable a.add", function (e) {

            var tarea_id = $(this).data('id');
            idTareaImportar = tarea_id;
            importarTarea(idTareaImportar);
        });

        //Cargar areas
        var cargarAreas = function (e) {
            //Limipiar select
            $('#area-tarea option').each(function (e) {
                if ($(this).val() !== "")
                    $(this).remove();
            });
            $('#area').select2();
            //Limipiar select
            $('#cargo-tarea option').each(function (e) {
                if ($(this).val() !== "")
                    $(this).remove();
            });
            $('#cargo-tarea').select2();
            var gerencia_id = $("#gerencia-tarea").val();
            if (gerencia_id !== "") {
                Metronic.blockUI({
                    target: '#tarea-form',
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
                        Metronic.unblockUI('#tarea-form');
                        if (response.success) {
                            for (var i = 0; i < response.areas.length; i++) {
                                var area_id = response.areas[i].area_id;
                                var descripcion = response.areas[i].descripcion;
                                $('#area-tarea').append(new Option(descripcion, area_id, false, false));
                            }
                            $('#area-tarea').select2();
                        } else {
                            toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                        }
                    },
                    failure: function (response) {
                        Metronic.unblockUI('#tarea-form');

                        toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                    }
                });
            }
        };
    };

    var initSeletGerencia = function () {
        $("#gerencia-tarea").bind('change', cargarAreas);
    };

    var importarTarea = function (tarea_id) {
        tareaimportada = true;
        plandId = currentPlanId !== '' && currentPlanId !== undefined && currentPlanId !== null ? currentPlanId : null;

        Metronic.blockUI({target: '#modal-importar .modal-body', animate: true});
        $.ajax({
            type: "PUT",
            url: !planNuevo ? "tarea/cargarDatos" : "tarea/cargarDatos/" + tarea_id + "&plan_id=0",
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify({
                'tarea_id': tarea_id,
                'plan_id': plandId
            }),
            success: function (response) {
                Metronic.unblockUI('#modal-importar .modal-body');
                if (response.success) {
                    tablaTareasPlan.getDataTable().ajax.reload();
                    $("#" + idTareaImportar).remove();
                    //Definir archivos de la tarea
                    resetFormTarea();

                    var tarea = response.tarea;
                    if (tareaimportada)
                        tareas.push(tarea);
                    else {
                        tareas[posicionTareaEditar] = tarea;
                    }
                    if (planNuevo) {
                        tareasImportadas.push(tarea);
                        localStorage.setItem("tareasImportadas", JSON.stringify(tareasImportadas));
                    }
                    toastr.success("La tarea se adicionó correctamente", "Exito !!!");
                } else {
                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            },
            failure: function (response) {
                Metronic.unblockUI('#modal-importar .modal-body');
                toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
            }
        });
    };

    //Tareas
    var tareas = new Array();
    var nEditingRowTarea = null;
    var initTableTareas = function () {
        $(document).on('click', "#table-tareas a.edit", function (e) {
            e.preventDefault();
            var idTarea = $(this).data('id');
            //Reset form tarea
            resetFormTarea();
            Metronic.blockUI({target: '#table-tareas', animate: true});
            editarTarea(idTarea);
        });

        $(document).on('click', "#table-tareas a.delete", function (e) {
            e.preventDefault();
            var nRow = $(this).parents('tr')[0];
            var tareaId = $(this).data('id');
            if (planNuevo) {
                tareasImportadas.forEach(function (item, index) {
                    if (parseInt(item.tarea_id) === parseInt(tareas[posicion].tarea_id)) {
                        tareasImportadas.splice(index, 1);
                    }
                });
            }
            if (tareaId !== "") {
                Metronic.blockUI({target: '#table-tareas', animate: true});
                $.ajax({
                    type: "POST",
                    url: "plan/eliminarTarea",
                    dataType: "json",
                    data: {
                        'plan_id': currentPlanId,
                        'tarea_id': tareaId
                    },
                    success: function (response) {
                        Metronic.unblockUI('#table-tareas');
                        if (response.success) {
                            toastr.success(response.message, "Exito !!!");
                            tablaTareasPlan.getDataTable().ajax.reload();
                        } else {
                            toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                        }
                    },
                    failure: function (response) {
                        Metronic.unblockUI('#table-tareas');
                        toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                    }
                });
            }
        });
        $(document).on('click', "#table-tareas a.partida", function (e) {
            var tareaId = $(this).data('id');
            if (!planNuevo) {
                if (tareaId !== "") {
                    Metronic.blockUI({target: '#table-tareas', animate: true});
                    $.ajax({
                        type: "POST",
                        url: "plan/cambiarTareaPartida",
                        dataType: "json",
                        data: {
                            'plan_id': currentPlanId,
                            'tarea_id': tareaId
                        },
                        success: function (response) {
                            Metronic.unblockUI('#table-tareas');
                            if (response.success) {
                                toastr.success(response.message, "Exito !!!");
                                tablaTareasPlan.getDataTable().ajax.reload();
                            } else {
                                toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                            }
                        },
                        failure: function (response) {
                            Metronic.unblockUI('#table-tareas');
                            toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                        }
                    });
                } else {
                    deleteTarea(nRow);
                }
            } else {
                tareasImportadas = JSON.parse(localStorage.getItem("tareasImportadas"));
                tam = tareasImportadas.length;
                for (var i = 0; i < tam; i++) {
                    elem = tareasImportadas[i];
                    if (parseInt(elem.tarea_id) === parseInt(tarea_id)) {
                        tdPartida = $("#" + posicion + " td:nth-child(7)")[0];
                        elem.partida = !elem.partida;
                        $(tdPartida).html(elem.partida ? "Si <i class='fa fa-check-circle ic-color-ok'></i>" : "No <i class='fa fa-minus-circle ic-color-error'></i>");
                        tareasImportadas[i] = elem;
                        tareas[posicion].partida = elem.partida;
                        localStorage.setItem("tareasImportadas", JSON.stringify(tareasImportadas));
                    }
                }
            }
        });

        function deleteTarea(nRow) {
            var posicion = $(nRow).attr('id');
            //Eliminar tarea
            tareas.splice(posicion, 1);
            resetFormTarea();
        }

        function cambiarPartidaTarea() {
            resetFormTarea();
        }

        function editarTarea(tareaId) {
            servicioPlan.getTareasRelacionadas(tareaId, currentPlanId).then(function (response) {
                Metronic.unblockUI('#table-tareas');
                $('#modal-configurar-tarea').modal({
                    'show': true
                });
                scopePlan.tareasAntecesoras = response.data.antecesoras;
                scopePlan.todasTareas = response.data.todasTareas;
                scopePlan.tareasSeleccionadas = response.data.sucesoras;
                scopePlan.tareasAgrupadas = response.data.agrupadas;
                scopePlan.tareasPorAgrupar = response.data.noAgrupadas;
                var tarea = scopePlan.tarea = response.data.tarea;
                scopePlan.tituloModal = tarea.nombre;
                formTitle = "Deseas actualizar la tarea \"" + tarea.nombre + "\" ? Sigue los siguientes pasos:";
                $('#tarea-configurar-titulo').html(formTitle);
                $('#tarea-configurar-id').val(tarea.tarea_id);
                $('#codigo-tarea').val(tarea.codigo);
                $('#tarea-configurar-nombre').val(tarea.nombre);
                $('#producto').val(tarea.producto);
                criticidad = tarea.criticidad_id;
                criticidades = new Array();
                for (i = 0; i < criticidad.length; i++)
                    criticidades.push(criticidad[i].criticidad_id);
                select = $('#criticidad-tarea-plan').select2();
                select.val(criticidades).trigger("change");
                CKEDITOR.instances.editor.setData(tarea.descripcion);
                canal = tarea.canales_id;
                canales = new Array();
                for (i = 0; i < canal.length; i++)
                    canales.push(canal[i].canal_id);
                selectCanal = $('#canal-tarea').select2();
                selectCanal.val(canales).trigger("change");
                estadoTarea = $('#estado-tarea').select2();
                estadoTarea.val(tarea.estado_id).trigger("change");
                if (tarea.recurrente) {
                    scopePlan.recurrente = "true";
                    $('#tiempo-recurrencia').val(tarea.tiempoRecurrencia);
                    $('#recurrente-activo').attr('checked', true);
                    $('#recurrente-inactivo').removeAttr("checked");
                    $('#recurrente-inactivo').prop('checked', false);
                    jQuery.uniform.update('#recurrente-activo');
                    jQuery.uniform.update('#recurrente-inactivo');
                } else {
                    scopePlan.recurrente = "false";
                    $('#tiempo-recurrencia').val(tarea.tiempoRecurrencia);
                    $('#recurrente-activo').attr('checked', false);
                    $('#recurrente-inactivo').attr('checked', true);
                    jQuery.uniform.update('#recurrente-activo');
                    jQuery.uniform.update('#recurrente-inactivo');
                }
                if (tarea.partida) {
                    $('#partidaactivo-tarea').prop('checked', true);
                    $('#partidainactivo-tarea').prop('checked', false);
                    jQuery.uniform.update('#partidaactivo-tarea');
                    jQuery.uniform.update('#partidainactivo-tarea');
                }
                if (tarea.hito) {
                    $('#hitoactivo').prop('checked', true);
                    $('#hitoinactivo').prop('checked', false);
                    jQuery.uniform.update('#hitoactivo');
                    jQuery.uniform.update('#hitoinactivo');
                }
                if (tarea.tranversal) {
                    $('#tarea-tranversal-activa').prop('checked', tarea.tranversal);
                    $('#tarea-tranversal-inactiva').prop('checked', !tarea.tranversal);
                    jQuery.uniform.update('#tarea-tranversal-activa');
                    jQuery.uniform.update('#tarea-tranversal-inactiva');
                }
                //Archivos
                dibujarTablaArchivos("#table-archivos", tarea);
                //Gerencia
                $("#gerencia-tarea").unbind('change', cargarAreas);
                $('#gerencia-tarea').select2('val', tarea.gerencia_id);
                //Areas
                $("#area-tarea").unbind('change', cargarCargos);
                var areas = tarea.areas;
                for (var i = 0; i < areas.length; i++) {
                    $('#area-tarea').append(new Option(areas[i].descripcion, areas[i].area_id, false, false));
                }
                $('#area-tarea').select2();
                $('#area-tarea').select2('val', tarea.area_id);

                //Cargos
                var cargos = tarea.cargos;
                for (var i = 0; i < cargos.length; i++) {
                    $('#cargo-tarea').append(new Option(cargos[i].nombre, cargos[i].cargo_id, false, false));
                }
                $('#cargo-tarea').select2();
                $('#cargo-tarea').select2('val', tarea.cargo_id);

                $("#gerencia-tarea").bind('change', cargarAreas);
                $("#area-tarea").bind('change', cargarCargos);
            }).catch(function (error) {

            });
        }

        function dibujarTablaAncesoras(tabla, tareasAntecesoras) {
            $(tabla + ' tr').each(function (e) {
                $(this).remove();
            });

            //Agregar fila vacia
            if (tareasAntecesoras.length === 0) {
                var tr = '<tr>' +
                    '<td colspan="4">No existen tareas</td>' +
                    '</tr>';
                $(tr).appendTo(tabla);
            }
            //Agregar elementos
            for (var i = 0; i < tareasAntecesoras.length; i++) {
                var tr = '<tr id="' + i + '">' +
                    '<td>' + tareasAntecesoras[i].code + '</td>' +
                    '<td>' + tareasAntecesoras[i].name + '</td>' +
                    '<td>' + tareasAntecesoras[i].criticidad + '</td>' +
                    '<td>' + tareasAntecesoras[i].cargo + '</td>' +
                    '</tr>';
                $(tr).appendTo(tabla);
            }
        }
    };
    var initFormTarea = function () {
        //Validacion
        $("#formulario-configurar-tarea").validate({
            rules: {
                nombre: {
                    required: true
                },
                cargo: {
                    required: true
                },
                criticidad: {
                    required: true
                },
                canal: {
                    required: true
                },
                estadoTarea: {
                    required: true
                }
            },
            messages: {
                nombre: {
                    required: "Este campo es obligatorio"
                },
                cargo: {
                    required: "Este campo es obligatorio"
                },
                criticidad: {
                    required: "Este campo es obligatorio"
                },
                canal: {
                    required: "Este campo es obligatorio"
                },
                estadoTarea: {
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
    var resetFormTarea = function () {
        $("#formulario-configurar-tarea")[0].reset();
        $('#formulario-configurar-tarea, #tarea-form textarea').each(function (e) {
            $element = $(this);
            $element.val('');

            $element.data("title", "").removeClass("has-error").tooltip("destroy");
            $element.closest('.form-group').removeClass('has-error').addClass('success');
        });

        $('#criticidad-tarea-plan').select2('val', '');
        $('#canal-tarea').select2('val', '');
        $('#estado-tarea').select2('val', '');
        $('#partidaactivo-tarea').prop('checked', false);
        jQuery.uniform.update('#partidaactivo-tarea');
        $('#partidainactivo-tarea').prop('checked', true);
        jQuery.uniform.update('#partidainactivo-tarea');

        $('#hitoactivo').prop('checked', false);
        jQuery.uniform.update('#hitoactivo');
        $('#hitoinactivo').prop('checked', true);
        jQuery.uniform.update('#hitoinactivo');

        $('#tarea-tranversal-activa').prop('checked', false);
        $('#tarea-tranversal-inactiva').prop('checked', true);
        jQuery.uniform.update('#tarea-tranversal-activa');
        jQuery.uniform.update('#tarea-tranversal-inactiva');

        $('#gerencia-tarea').select2('val', '');

        //Limipiar select
        $('#area-tarea option').each(function (e) {
            if ($(this).val() !== "")
                $(this).remove();
        });
        $('#area-tarea').select2('val', '');

        //Limipiar select cargo
        $('#cargo-tarea option').each(function (e) {
            if ($(this).val() !== "")
                $(this).remove();
        });
        $('#cargo-tarea').select2();

        var $element = $('.selectpicker');
        $element.removeClass('has-error').tooltip("destroy");
        $element.closest('.form-group').removeClass('has-error');
        nEditingRowTarea = null;

        //Reset archivos
        resetFormArchivo();
        $('#tab-general-tarea a').click();
    };
    var btnClickSalvarFormTarea = function () {
        Metronic.scrollTo($('.page-title'));

        var criticidad_id = $('#criticidad-tarea-plan').val();
        var estado_id = $('#estado-tarea').val();
        var tam = scopePlan.tareasAgrupadas.length;
        if (criticidad_id !== null && criticidad_id.length !== 0 && estado_id !== "") {
            if (scopePlan.agrupada && tam === 0) {
                toastr.error("Si la tarea es agrupada debe seleccionar al menos una tarea para agrupar", "Error !!!", {"positionClass": "toast-top-center"});
            } else {
                Metronic.blockUI({target: '#modal-configurar-tarea', animate: true});
                var tarea_id = $('#tarea-configurar-id').val();
                var codigo = $('#codigo-tarea').val();
                var nombre = $('#tarea-configurar-nombre').val();
                var producto = $('#producto').val();
                var hito = ($('#hitoactivo').prop('checked')) ? true : false;
                var descripcion = CKEDITOR.instances.editor.getData();
                var canales_id = $('#canal-tarea').val();
                if (canales_id === "" || canales_id === null)
                    canales_id = new Array();
                var recurrente = ($('#recurrente-activo').prop('checked')) ? true : false;
                var cargo_id = $('#cargo-tarea').val();
                if (recurrente)
                    var tiempoRecurrencia = $('#tiempo-recurrencia').val();
                var partida = ($('#partidaactivo-tarea').prop('checked')) ? true : false;
                var tranversal = ($('#tarea-tranversal-activa').prop('checked')) ? true : false;
                var pos = 0;
                criticalyString = '';
                criticidad_id.forEach(function (item, index) {
                    if (item !== "") {
                        for (var j = 0; j < criticidadesTareas.length; j++) {
                            if (parseInt(item) === parseInt(criticidadesTareas[j].criticidad_id)) {
                                criticalyString += (pos === 0 ? "" : ", ") + criticidadesTareas[j].nombre;
                                pos++;
                            }
                        }
                    } else {
                        criticidad_id.splice(index, 1);
                    }
                });
                $.ajax({
                    type: "POST",
                    url: "tarea/salvarTarea?plan=true&planId=" + currentPlanId,
                    dataType: "json",
                    contentType: "application/json;charset=utf-8",
                    data: JSON.stringify({
                        'tarea_id': tarea_id === "" ? null : tarea_id,
                        'codigo': codigo,
                        'producto': producto,
                        'nombre': nombre,
                        'descripcion': descripcion,
                        'criticidad_id': criticidad_id,
                        'tranversal': tranversal,
                        'estado': estado_id,
                        'partida': partida,
                        'hito': hito,
                        'cargo_id': cargo_id,
                        'canales_id': canales_id,
                        'recurrente': recurrente,
                        'tiempoRecurrencia': tiempoRecurrencia,
                        'relacionadas': scopePlan.tareasSeleccionadas,
                        'agrupadas': scopePlan.tareasAgrupadas
                    }),
                    success: function (response) {
                        Metronic.unblockUI('#modal-configurar-tarea');
                        if (response.success) {
                            CKEDITOR.instances.editor.setData('');
                            toastr.success(response.message, "Exito !!!");
                            $('#modal-configurar-tarea').modal('toggle');
                            tablaTareasPlan.getDataTable().draw();
                        } else {
                            toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                        }
                    },
                    failure: function (response) {
                        Metronic.unblockUI('#modal-configurar-tarea');

                        toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                    }
                });

            }
        } else {
            if (criticidad_id === "") {
                var $element = $('#select-criticidad-tarea .selectpicker');
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
    //Archivos
    var initFormArchivo = function () {
        $("#archivo-form").validate({
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
                    target: '#archivo-form',
                    animate: true
                });
            }, //funcion que se ejecuta antes de enviar el form
            success: function (response) {
                Metronic.unblockUI('#archivo-form');
                if (response.success) {
                    var posicion_tarea = $(nEditingRowTarea).attr('id');
                    var posicion_archivo = $(nEditingRowArchivo).attr('id');
                    var archivo = response.name;
                    var ruta = response.ruta;
                    var nombre = $('#archivo-nombre').val();
                    var descripcion = $('#archivo-descripcion').val();
                    tareas[posicion_tarea].modelos[posicion_archivo].archivo = archivo;
                    tareas[posicion_tarea].modelos[posicion_archivo].nombre = nombre;
                    tareas[posicion_tarea].modelos[posicion_archivo].descripcion = descripcion;
                    tareas[posicion_tarea].modelos[posicion_archivo].ruta = ruta;
                    resetFormArchivo();
                    dibujarTablaArchivos("#table-archivos");

                } else {
                    toastr.error(response.error, "Error !!!");
                }
            },
            dataType: 'json',
            resetForm: false
        };
        $('#archivo-form').ajaxForm(opciones_form);

    };
    var nEditingRowArchivo = null;
    var dibujarTablaArchivos = function (tabla, tarea) {
        //Limpiar tabla
        $(tabla + ' tbody tr').each(function (e) {
            $(this).remove();
        });

        var archivos = [];
        if (tarea.modelos) {
            archivos = tarea.modelos;
        }
        //Agregar fila vacia
        if (archivos.length === 0) {
            var tr = '<tr>' +
                '<td colspan="4">No existen archivos</td>' +
                '</tr>';
            $(tr).appendTo(tabla + ' tbody');
        }

        //Agregar elementos
        for (var i = 0; i < archivos.length; i++) {
            var acciones = '<a class="btn btn-icon-only red delete table-action" href="javascript:;">' +
                '<i class="fa fa-trash-o fa-fw"></i></a>';
            if (archivos[i].archivo !== "") {
                var download_url = urlPath + "uploads/" + archivos[i].ruta;
                acciones += '<a class="btn btn-icon-only purple download table-action" href="' + download_url + '" target="_blank">' +
                    '<i class="fa fa-download fa-fw"></i></a>';
            }
            var tr = '<tr id="' + i + '">' +
                '<td style="width: 3%;">' + (i + 1) + '</td>' +
                '<td style="width: 20%;"><a href="uploads/' + archivos[i].ruta + '" target="_new">' + archivos[i].ruta + '</a></td>' +
                '<td style="width: 50%;">' + (archivos[i].descripcion === null ? "" : archivos[i].descripcion) + '</td>' +
                '<td style="width: 15%;" class="text-center">' + acciones + '</td>' +
                '</tr>';
            $(tr).appendTo(tabla + ' tbody');
        }

        $(tabla + ' .edit') // Destroy any pre-existing tooltip so we can repopulate with new tooltip content
            .data("title", 'Editar archivo')
            .tooltip();

        $(tabla + ' .delete') // Destroy any pre-existing tooltip so we can repopulate with new tooltip content
            .data("title", 'Eliminar archivo')
            .tooltip();

        $(tabla + ' .download') // Destroy any pre-existing tooltip so we can repopulate with new tooltip content
            .data("title", 'Descargar archivo')
            .tooltip();
    };
    var mostrarModalArchivo = function () {
        $('#modal-archivo').modal({
            'show': true
        });
    };
    var btnClickSalvarArchivo = function () {
        var posicion_tarea = $(nEditingRowTarea).attr('id');
        var posicion_archivo = $(nEditingRowArchivo).attr('id');
        if ($('#archivo-form').valid()) {
            if (tareas[posicion_tarea].modelos[posicion_archivo].archivo === "") {
                if (!$('#fileinput-archivo').fileinput().hasClass('fileinput-new')) {
                    salvarArchivo(posicion_tarea, posicion_archivo);
                } else {
                    if ($('#fileinput-archivo').fileinput().hasClass('fileinput-new')) {
                        var $element = $('#fileinput-archivo');
                        $element.tooltip("destroy") // Destroy any pre-existing tooltip so we can repopulate with new tooltip content
                            .data("title", "Este campo es obligatorio")
                            .addClass("has-error")
                            .tooltip({
                                placement: 'bottom'
                            }); // Create a new tooltip based on the error messsage we just set in the title
                        $element.closest('.form-group').removeClass('has-success').addClass('has-error');
                    }
                }
            } else {
                if (!$('#fileinput-archivo').fileinput().hasClass('fileinput-new')) {
                    salvarArchivo(posicion_tarea, posicion_archivo);
                } else {
                    var nombre = $('#archivo-nombre').val();
                    var descripcion = $('#archivo-descripcion').val();
                    tareas[posicion_tarea].modelos[posicion_archivo].descripcion = descripcion;
                    tareas[posicion_tarea].modelos[posicion_archivo].nombre = nombre;
                    resetFormArchivo();
                    dibujarTablaArchivos("#table-archivos");
                }
            }

        }

    };

    function salvarArchivo(posicion_tarea, posicion_archivo) {
        var formData = new FormData($('#archivo-form'));
        formData.append($("#file").attr("name"), $('#file').prop('files')[0]);
        formData.append($("#archivo-nombre").attr("name"), $("#archivo-nombre").val());
        formData.append($("#archivo-descripcion").attr("name"), $("#archivo-descripcion").val());
        formData.append("tareaId", idTareaEditar);
        formData.append("documentId", tareas[posicion_tarea].modelos[posicion_archivo].modelo_id);
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
                            $('#modal-archivo').modal("toggle");
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
                var modelo = res.responseJSON.modelo;
                tareas[posicion_tarea].modelos[posicion_archivo].descripcion = modelo.descripcion;
                tareas[posicion_tarea].modelos[posicion_archivo].nombre = modelo.nombre;
                tareas[posicion_tarea].modelos[posicion_archivo].ruta = modelo.ruta;
                var progressbar = $('#progressbar');
                progressbar.val(0);
                $('.progress-value').html('0%');
                dibujarTablaArchivos("#table-archivos");
            }
        });

    }

    var initTableArchivo = function () {
        function deleteArchivo(nRow) {
            var posicion_tarea = $(nEditingRowTarea).attr('id');

            var posicion = $(nRow).attr('id');
            //Eliminar modelo
            tareas[posicion_tarea].modelos.splice(posicion, 1);
            dibujarTablaArchivos("#table-archivos");
            resetFormArchivo();
        }

        $(document).on('click', "#table-archivos a.edit", function (e) {
            e.preventDefault();
            var posicion_tarea = $(nEditingRowTarea).attr('id');
            var nRow = $(this).parents('tr')[0];
            var posicion = $(nRow).attr('id');
            if (tareas[posicion_tarea].modelos[posicion]) {
                idTareaEditar = tareas[posicion_tarea].tarea_id;
                //Reset form archivo
                resetFormArchivo();
                nEditingRowArchivo = nRow;
                var archivo = tareas[posicion_tarea].modelos[posicion];
                var nombre = archivo.nombre;
                $('#archivo-nombre').val(nombre);
                var descripcion = archivo.descripcion;
                $('#archivo-descripcion').val(descripcion);
                $("#modal-archivo .modal-title").html("Editar Archivo");
                mostrarModalArchivo();
            }
        });
        $(document).on('click', "#table-archivos a.delete", function (e) {
            e.preventDefault();
            var posicion_tarea = $(nEditingRowTarea).attr('id');
            var nRow = $(this).parents('tr')[0];
            var posicion = $(nRow).attr('id');
            if (tareas[posicion_tarea].modelos[posicion]) {
                var archivo = tareas[posicion_tarea].modelos[posicion];
                if (archivo) {
                    Metronic.blockUI({target: '#table-archivos', animate: true});
                    $.ajax({
                        type: "POST",
                        url: "tarea/eliminarModelo?modelo_id=" + archivo.modelo_id,
                        dataType: "json",
                        data: {
                            'archivo': archivo
                        },
                        success: function (response) {
                            Metronic.unblockUI('#table-archivos');
                            if (response.success) {

                                toastr.success(response.message, "Exito !!!");
                                deleteArchivo(nRow);
                            } else {
                                toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                            }
                        },
                        failure: function (response) {
                            Metronic.unblockUI('#table-archivos');

                            toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                        }
                    });
                } else {
                    deleteArchivo(nRow);
                }
            }
        });
    };
    var resetFormArchivo = function () {
        $('#archivo-form input, #archivo-form textarea').each(function (e) {
            $element = $(this);
            $element.val('');
            $element.data("title", "").removeClass("has-error").tooltip("destroy");
            $element.closest('.form-group').removeClass('has-error').addClass('success');
        });
        $('#fileinput-archivo').fileinput('clear');
        nEditingRowArchivo = null;
        $('#modal-archivo').modal('hide');
    };

    //Cargar cargos
    var cargarCargos = function (e) {
        //Limipiar select
        $('#cargo-tarea option').each(function (e) {
            if ($(this).val() !== "")
                $(this).remove();
        });
        $('#cargo').select2();
        var area_id = $("#area-tarea").val();
        if (area_id !== "") {
            Metronic.blockUI({
                target: '#modal-configurar-tarea',
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
                    Metronic.unblockUI('#modal-configurar-tarea');
                    if (response.success) {
                        for (var i = 0; i < response.cargos.length; i++) {
                            var cargo_id = response.cargos[i].cargo_id;
                            var descripcion = response.cargos[i].nombre;
                            $('#cargo-tarea').append(new Option(descripcion, cargo_id, false, false));
                        }
                        $('#cargo-tarea').select2();
                    } else {
                        toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                    }
                },
                failure: function (response) {
                    Metronic.unblockUI('#modal-configurar-tarea');
                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            });
        }

    };

    var initSeletArea = function () {
        $("#area-tarea").bind('change', cargarCargos);
    };
    var initSeletArea = function () {
        $("#area-tarea").bind('change', cargarCargos);
    };
    //Cargar areas
    var cargarAreas = function (e) {
        //Limipiar select
        $('#area-tarea option').each(function (e) {
            if ($(this).val() !== "")
                $(this).remove();
        });
        $('#area-tarea').select2();
        //Limipiar select
        $('#cargo-tarea option').each(function (e) {
            if ($(this).val() !== "")
                $(this).remove();
        });
        $('#cargo-tarea').select2();
        var gerencia_id = $("#gerencia-tarea").val();
        if (gerencia_id !== "") {
            Metronic.blockUI({
                target: '#modal-configurar-tarea',
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
                    Metronic.unblockUI('#modal-configurar-tarea');
                    if (response.success) {
                        for (var i = 0; i < response.areas.length; i++) {
                            var area_id = response.areas[i].area_id;
                            var descripcion = response.areas[i].descripcion;
                            $('#area-tarea').append(new Option(descripcion, area_id, false, false));
                        }
                        $('#area-tarea').select2();
                    } else {
                        toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                    }
                },
                failure: function (response) {
                    Metronic.unblockUI('#modal-configurar-tarea');
                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            });
        }
    };
    var initSeletGerencia = function () {
        $("#gerencia").bind('change', cargarAreas);
    };
    //Actualizar select planes
    var actualizarPlanes = function (planes) {
        //Limipiar select categorias
        $('#filtro-plan option').each(function (e) {
            if ($(this).val() !== "")
                $(this).remove();
        });
        $('#filtro-plan').select2();
        //Llenar planes
        for (var i = 0; i < planes.length; i++) {
            var id = planes[i].plan_id;
            var descripcion = planes[i].descripcion;

            $('#filtro-plan').append(new Option(descripcion, id, false, false));
        }
        $('#filtro-plan').select2();
    };

    //Diagrama
    var myDiagram = null;
    var start = 0;
    var creada = false;
    var celdas = new Array();
    var initAccionDiagrama = function () {
        $(document).on('click', "#plan-table-editable a.diagrama", function (e) {
            e.preventDefault();
            resetForms();
            var plan_id = $(this).data('id');
            $('#plan_id').val(plan_id);
            $('#form-diagrama').removeClass('ng-hide');
            $('#lista-plan').addClass('ng-hide');
            cargarDatosDiagrama(plan_id);
        });

        function cargarDatosDiagrama(plan_id) {
            Metronic.blockUI({target: '#form-diagrama .portlet-body', animate: true});
            servicioPlan.buscarPlan(plan_id).then(function (response) {
                Metronic.unblockUI('#form-diagrama .portlet-body');
                plan = response.data.plan;
                $("#form-diagrama-title").html("DIAGRAMA " + plan.descripcion);
                scopePlan.planId = plan.plan_id;
                mostrarDiagrama(plan);
            }).catch(function (error) {
                loggerSistema.error('Error !!' + error.data);
                Metronic.unblockUI('#form-diagrama .portlet-body');
            });
        }
    };
    var mostrarDiagrama = function (plan) {
        initDiagrama(plan);
    };
    var initDiagrama = function (plan) {
        // define a custom ResizingTool to limit how far one can shrink a row or column
        function LaneResizingTool() {
            go.ResizingTool.call(this);
        }

        go.Diagram.inherit(LaneResizingTool, go.ResizingTool);
        /** @override */
        LaneResizingTool.prototype.computeMinSize = function () {
            var diagram = this.diagram;
            var lane = this.adornedObject.part;  // might be row or column
            var horiz = (lane.category === "Column Header");  // or "Row Header"
            var margin = diagram.nodeTemplate.margin;
            var bounds = new go.Rect();
            diagram.findTopLevelGroups().each(function (g) {
                if (horiz ? (g.column === lane.column) : (g.row === lane.row)) {
                    var b = diagram.computePartsBounds(g.memberParts);
                    if (b.isEmpty())
                        return;  // nothing in there?  ignore it
                    b.unionPoint(g.location);  // keep any empty space on the left and top
                    b.addMargin(margin);  // assume the same node margin applies to all nodes
                    if (bounds.isEmpty()) {
                        bounds = b;
                    } else {
                        bounds.unionRect(b);
                    }
                }
            });
            // limit the result by the standard value of computeMinSize
            var msz = go.ResizingTool.prototype.computeMinSize.call(this);
            if (bounds.isEmpty())
                return msz;
            return new go.Size(Math.max(msz.width, bounds.width), Math.max(msz.height, bounds.height));
        };
        /** @override */
        LaneResizingTool.prototype.resize = function (newr) {
            var lane = this.adornedObject.part;
            var horiz = (lane.category === "Column Header");
            var vertical = (lane.category === "Row Sider");
            var lay = this.diagram.layout;  // the TableLayout
            if (horiz) {
                var col = lane.column;
                var coldef = lay.getColumnDefinition(col);
                coldef.width = newr.width;
            } else {
                if (vertical) {
                    var row = lane.row;
                    var rowdef = lay.getRowDefinition(row);
                    rowdef.height = newr.height;
                } else {
                    var shape = lane.resizeObject;
                    if (shape !== null) {  // set its desiredSize length, but leave each breadth alone
                        shape.width = newr.width;
                        shape.height = newr.height;
                    }
                }
            }

            lay.invalidateLayout();
        };
        // end LaneResizingTool class
        var $ = go.GraphObject.make;
        var nodeSelectionAdornmentTemplate = $(go.Adornment, "Auto", $(go.Shape, {
            fill: null,
            stroke: "deepskyblue",
            strokeWidth: 1.5,
            strokeDashArray: [4, 2]
        }), $(go.Placeholder));

        var nodeResizeAdornmentTemplate = $(go.Adornment, "Spot", {locationSpot: go.Spot.Right},
            $(go.Placeholder),
            $(go.Shape, {
                alignment: go.Spot.TopLeft,
                cursor: "nw-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),
            $(go.Shape, {
                alignment: go.Spot.Top,
                cursor: "n-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),
            $(go.Shape, {
                alignment: go.Spot.TopRight,
                cursor: "ne-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),
            $(go.Shape, {
                alignment: go.Spot.Left,
                cursor: "w-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),
            $(go.Shape, {
                alignment: go.Spot.Right,
                cursor: "e-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),
            $(go.Shape, {
                alignment: go.Spot.BottomLeft,
                cursor: "se-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),
            $(go.Shape, {
                alignment: go.Spot.Bottom,
                cursor: "s-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),
            $(go.Shape, {
                alignment: go.Spot.BottomRight,
                cursor: "sw-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }));

        myDiagram = $(go.Diagram, "myDiagramDiv", {
            "toolManager.mouseWheelBehavior": go.ToolManager.WheelZoom,
            initialContentAlignment: go.Spot.Center,
            //Nuevas opciones
            "InitialLayoutCompleted": function (e) {
                // var div = e.diagram.div;
                // var height = parseInt(e.diagram.documentBounds.height / 2) + 24;
                //
                // div.style.height = height + 'px';
                // e.diagram.requestUpdate();
            },
            hasVerticalScrollbar: false,
            scale: 0.5,
            layout: $(TableLayout, $(go.RowColumnDefinition, {
                row: 1,
                height: 22
            }), $(go.RowColumnDefinition, {column: 1, width: 22})),
            "SelectionMoved": function (e) {
                e.diagram.layoutDiagram(true);
            },
            "ExternalObjectsDropped": function (e) {
                pintarBorde(plan);
            },
            "SelectionDeleted": function (e) {
                creada = false;
                var contador = 0;
                e.subject.each(function (n) {
                    if (n.part.data.tarea_id) {
                        if (contador === 0)
                            eliminarTareaGrafico(n.part.data.tarea_id, plan.plan_id);
                    } else {
                        var from = n.part.data.from;
                        var to = n.part.data.to;
                        insertarEliminarLink(from, to, false, plan.plan_id);
                    }
                });
            },
            "LinkDrawn": function (e) {
                if (!creada)
                    insertarEliminarLink(e.subject.data.from, e.subject.data.to, true, plan.plan_id);
            },
            "resizingTool": new LaneResizingTool(),
            "linkingTool.isEnabled": true,
            allowDrop: true,
            // feedback that dropping in the background is not allowed
            mouseDragOver: function (e) {
                e.diagram.currentCursor = "not-allowed";
            },
            // when dropped in the background, not on a Node or a Group, cancel the drop
            mouseDrop: function (e) {
                // e.diagram.currentTool.doCancel();
            },
            "animationManager.isInitial": false,
            "undoManager.isEnabled": true,
            "linkReshapingTool": new OrthogonalLinkReshapingTool(),
            "LinkReshaped": function (e) {
                e.subject.routing = go.Link.Orthogonal;
            }
        });

        myDiagram.toolManager.mouseDownTools.add($(LinkShiftingTool));

        myDiagram.nodeTemplateMap.add("Column Header", $(go.Part, "Spot", {
                row: 1, rowSpan: 9999, column: 2,
                minSize: new go.Size(500, NaN),
                stretch: go.GraphObject.Fill,
                movable: false,
                resizable: true,
                resizeAdornmentTemplate: $(go.Adornment, "Spot", $(go.Placeholder), $(go.Shape, {
                    alignment: go.Spot.Right,
                    desiredSize: new go.Size(7, 50),
                    fill: "lightblue",
                    stroke: "dodgerblue",
                    cursor: "col-resize"
                }))
            }, new go.Binding("column", "col"),
            $(go.Shape, {fill: null}, new go.Binding("fill", "color")),
            $(go.Panel, "Auto", {// this is positioned above the Shape, in row 1
                    alignment: go.Spot.Top, alignmentFocus: go.Spot.Bottom,
                    stretch: go.GraphObject.Horizontal,
                    height: myDiagram.layout.getRowDefinition(1).height
                },
                $(go.Shape, {fill: null, strokeWidth: 0}, new go.Binding("fill", "color_header")),
                $(go.TextBlock, {
                    font: "bold 10pt sans-serif",
                    isMultiline: false,
                    wrap: go.TextBlock.None,
                    overflow: go.TextBlock.OverflowEllipsis
                }, new go.Binding("text")))));


        myDiagram.nodeTemplateMap.add("Row Sider", $(go.Part, "Spot", {
            row: 2, column: 1, columnSpan: 9999,
            minSize: new go.Size(NaN, 200),
            stretch: go.GraphObject.Fill,
            movable: false,
            resizable: true,
            resizeAdornmentTemplate: $(go.Adornment, "Spot", $(go.Placeholder), $(go.Shape, {
                alignment: go.Spot.Bottom,
                desiredSize: new go.Size(50, 7),
                fill: "lightblue",
                stroke: "dodgerblue",
                cursor: "row-resize"
            }))
        }, new go.Binding("row"), $(go.Shape, {fill: null}, new go.Binding("fill", "color")), $(go.Panel, "Auto", {
            alignment: go.Spot.Left, alignmentFocus: go.Spot.Right,
            stretch: go.GraphObject.Vertical, angle: 270,
            height: myDiagram.layout.getColumnDefinition(1).width
        }, $(go.Shape, {fill: "#F3F3F3", strokeWidth: 0}), $(go.TextBlock, {
            font: "bold 10pt sans-serif",
            isMultiline: true,
            wrap: go.TextBlock.WrapFit,
            overflow: go.TextBlock.OverflowEllipsis
        }, new go.Binding("text")))));

        myDiagram.nodeTemplateMap.add("Start", $(go.Node, "Spot", {
                margin: new go.Margin(10, 10, 10, 10),
                avoidableMargin: new go.Margin(10, 10, 10, 10),
                locationSpot: go.Spot.Center,
                click: function (e, node) {
                    showConnections(node);
                },
                doubleClick: function (e, node) {
                    mostrarDetallesTarea(plan, node.data.tarea_id);
                }
            }, /*new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),*/ new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify),
            new go.Binding("position", "bounds", go.Point.parse).makeTwoWay(go.Point.stringify),
            {selectable: true, selectionAdornmentTemplate: nodeSelectionAdornmentTemplate, movable: true},
            {resizable: true, resizeObjectName: "PANEL", resizeAdornmentTemplate: nodeResizeAdornmentTemplate},
            new go.Binding("row"),
            new go.Binding("column", "col"),
            $(go.Panel, "Auto", $(go.Shape, "DividedEvent", {
                    fill: "white",
                    strokeWidth: 1,
                    height: 110,
                    stroke: "transparent",
                    portId: "",
                    fromSpot: go.Spot.Right,
                    toSpot: go.Spot.Left,
                    fromLinkable: true,
                    toLinkable: true,
                    margin: new go.Margin(10, 0, 10, 0)
                }, new go.Binding("fill", "color"), new go.Binding("stroke", "color_borde")),
                $(go.Panel, "Table", {
                        margin: new go.Margin(0, 0, 10, 0),
                        defaultAlignment: go.Spot.Left
                    },
                    $(go.RowColumnDefinition, {column: 0, width: 50}),
                    $(go.RowColumnDefinition, {column: 1, width: 50}),
                    $(go.RowColumnDefinition, {column: 2, width: 200}),
                    $(go.TextBlock, {
                        font: "10pt  Segoe UI,sans-serif",
                        stroke: "#FFF"
                    }, {
                        row: 0, column: 2,
                        textAlign: "right",
                        margin: new go.Margin(0, 5, 0, 5)
                    }, new go.Binding("text", "cargo").makeTwoWay()),
                    $(go.TextBlock, {
                        font: "11pt  Segoe UI,sans-serif",
                        stroke: "#FFF"
                    }, {
                        row: 1, column: 0, columnSpan: 3,
                        textAlign: "right",
                        margin: new go.Margin(10, 5, 0, 5)
                    }, new go.Binding("text").makeTwoWay()),
                    $(go.Shape, "LineH", {name: "LINEA", fill: "white", stroke: "#000"}, {
                        row: 2, column: 0, columnSpan: 3,
                        margin: new go.Margin(0, 0, 0, 0),
                        maxSize: new go.Size(300, 10),
                        minSize: new go.Size(300, 10)
                    }),
                    $(go.TextBlock, {
                        font: "10pt  Segoe UI,sans-serif",
                        stroke: "#FFF"
                    }, {
                        row: 3, column: 1, columnSpan: 3,
                        textAlign: "center",
                        font: "bold 9pt sans-serif",
                        margin: new go.Margin(0, 0, 0, 0)
                    }, new go.Binding("text", "producto").makeTwoWay())))));

        myDiagram.nodeTemplateMap.add("Hito", $(go.Node, "Spot", {
                margin: new go.Margin(10, 10, 10, 10),
                avoidableMargin: new go.Margin(10, 10, 10, 10),
                locationSpot: go.Spot.Center,
                click: function (e, node) {
                    showConnections(node);
                },
                doubleClick: function (e, node) {
                    mostrarDetallesTarea(plan, node.data.tarea_id);
                }
            }, /*new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),*/ new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify),
            {selectable: true, selectionAdornmentTemplate: nodeSelectionAdornmentTemplate, movable: true},
            {resizable: true, resizeObjectName: "PANEL", resizeAdornmentTemplate: nodeResizeAdornmentTemplate},
            new go.Binding("row"),
            new go.Binding("column", "col"),
            new go.Binding("position", "bounds", go.Point.parse).makeTwoWay(go.Point.stringify),
            $(go.Panel, "Auto", $(go.Shape, "Parallelogram2", {
                fill: "white",
                strokeWidth: 1,
                stroke: "transparent",
                portId: "",
                minSize: new go.Size(250, 70),
                fromSpot: go.Spot.Right,
                toSpot: go.Spot.Left,
                fromLinkable: true,
                toLinkable: true
            }, new go.Binding("fill", "color"), new go.Binding("stroke", "color_borde")), $(go.Panel, "Table", {
                margin: new go.Margin(0, 0, 10, 0),
                defaultAlignment: go.Spot.Left
            }, $(go.RowColumnDefinition, {
                column: 0,
                width: 200
            }), $(go.TextBlock, {font: "10pt bold Segoe UI,sans-serif", stroke: "#000"}, {
                row: 0,
                column: 0,
                columnSpan: 4,
                textAlign: "center",
                margin: new go.Margin(5, 5, 0, 15)
            }, new go.Binding("text").makeTwoWay())))));

        myDiagram.nodeTemplate = $(go.Node, "Auto", {
                margin: new go.Margin(10, 10, 10, 10),
                avoidableMargin: new go.Margin(10, 10, 10, 10),
                locationSpot: go.Spot.Center,
                click: function (e, node) {
                    showConnections(node);
                },
                doubleClick: function (e, node) {
                    mostrarDetallesTarea(plan, node.data.tarea_id);
                }
            }, /* new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),*/ new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify),
            new go.Binding("position", "bounds", go.Point.parse).makeTwoWay(go.Point.stringify),
            {selectable: true, selectionAdornmentTemplate: nodeSelectionAdornmentTemplate, movable: true},
            {resizable: true, resizeObjectName: "PANEL", resizeAdornmentTemplate: nodeResizeAdornmentTemplate},
            new go.Binding("row"), new go.Binding("column", "col"), $(go.Shape, "Rectangle", {
                fill: "white",
                strokeWidth: 1,
                stroke: "transparent",
                portId: "",
                fromSpot: go.Spot.Right,
                toSpot: go.Spot.Left,
                fromLinkable: true,
                toLinkable: true
            }, new go.Binding("fill", "color").makeTwoWay(), new go.Binding("stroke", "color_borde").makeTwoWay()),
            $(go.Panel, "Table", {
                    margin: new go.Margin(0, 0, 10, 0),
                    defaultAlignment: go.Spot.Left
                },
                $(go.RowColumnDefinition, {column: 0, width: 50}),
                $(go.RowColumnDefinition, {column: 1, width: 50}),
                $(go.RowColumnDefinition, {column: 2, width: 200}),
                $(go.TextBlock, {font: "9pt  Segoe UI,sans-serif", stroke: "#000"}, {
                    row: 0,
                    column: 2,
                    textAlign: "right",
                    font: "bold 9pt Segoe UI,sans-serif",
                    margin: new go.Margin(5, 0, 10, 0)
                }, new go.Binding("text", "cargo").makeTwoWay()),
                $(go.TextBlock, {font: "10pt  Segoe UI,sans-serif", stroke: "#000"}, {
                    row: 1,
                    column: 0,
                    columnSpan: 3,
                    textAlign: "center",
                    margin: new go.Margin(0, 5, 0, 5)
                }, new go.Binding("text").makeTwoWay()),
                $(go.Shape, "LineH", {name: "LINEA", fill: "white", stroke: "#000"}, {
                    row: 2,
                    column: 0,
                    columnSpan: 3,
                    margin: new go.Margin(0, 0, 0, 0),
                    maxSize: new go.Size(300, 10),
                    minSize: new go.Size(300, 10)
                }),
                $(go.TextBlock, {font: "9pt  Segoe UI,sans-serif", stroke: "#000"}, {
                    row: 3,
                    column: 1,
                    columnSpan: 2,
                    textAlign: "center",
                    font: "bold 9pt sans-serif",
                    margin: new go.Margin(0, 0, 0, 0)
                }, new go.Binding("text", "producto").makeTwoWay()))); // end Node

        myDiagram.groupTemplate = $(go.Group, "Auto", {
                layerName: "Background",
                stretch: go.GraphObject.Fill,
                selectable: false,
                computesBoundsAfterDrag: true,
                computesBoundsIncludingLocation: true,
                handlesDragDropForMembers: true,  // don't need to define handlers on member Nodes and Links
                mouseDragEnter: function (e, group, prev) {
                    group.isHighlighted = true;
                },
                mouseDragLeave: function (e, group, next) {
                    group.isHighlighted = false;
                },
                mouseDrop: function (e, group) {

                }
            }, new go.Binding("row"), new go.Binding("column", "col"), new go.Binding("position", "bounds", go.Point.parse).makeTwoWay(go.Point.stringify), new go.Binding("columnSpan", "colSpan").makeTwoWay(),
            // the group is normally unseen -- it is completely transparent except when given a color or when highlighted
            $(go.Shape, {
                fill: "transparent", stroke: "transparent",
                strokeWidth: 3,
                stretch: go.GraphObject.Fill
            }, new go.Binding("fill", "color"), new go.Binding("stroke", "isHighlighted", function (h) {
                return h ? "red" : "blue";
            }).ofObject()),
            $(go.Placeholder, {// leave a margin around the member nodes of the group which is the same as the member node margin
                alignment: (function (m) {
                    return new go.Spot(0, 0, m.top, m.left);
                })(myDiagram.nodeTemplate.margin),
                padding: (function (m) {
                    return new go.Margin(50, m.right + 50, m.bottom + 50, 50);
                })(myDiagram.nodeTemplate.margin)
            })
        );
        myDiagram.linkTemplate = $(go.Link, {
                //Nuevas opciones
                relinkableFrom: true,
                relinkableTo: true,
                reshapable: true,
                resegmentable: true,
                layerName: "Background"
            }, {
                routing: go.Link.AvoidsNodes,
                adjusting: go.Link.End,
                curve: go.Link.JumpOver,
                toShortLength: 4
            },
            ///Nuevas opciones
            new go.Binding("points").makeTwoWay(),
            // new go.Binding("routing", "routing", go.Binding.parseEnum(go.Link, go.Link.AvoidsNodes)).makeTwoWay(go.Binding.toString),
            //End Nuevas opciones
            $(go.Shape, {isPanelMain: true, stroke: "black", strokeWidth: 3},
                // the Shape.stroke color depends on whether Link.isHighlighted is true
                new go.Binding("stroke", "isHighlighted", function (h) {
                    return h ? "yellow" : "black";
                }).ofObject(), new go.Binding("strokeWidth", "isHighlighted", function (h) {
                    return h ? 5 : 3;
                }).ofObject()),
            $(go.Shape, {toArrow: "standard", stroke: "black", strokeWidth: 3},
                // the Shape.fill color depends on whether Link.isHighlighted is true
                new go.Binding("fill", "isHighlighted", function (h) {
                    return h ? "yellow" : "black";
                }).ofObject(), new go.Binding("strokeWidth", "isHighlighted", function (h) {
                    return h ? 5 : 3;
                }).ofObject()));

        var links = plan.links;

        if (plan.diagrama !== "" && plan.diagrama !== null) {
            myDiagram.model = go.Model.fromJson(plan.diagrama);
            var pos = myDiagram.model.modelData.position;
            if (pos)
                myDiagram.initialPosition = go.Point.parse(pos);
            // myDiagram.delayInitialization(relayoutDiagram);
        } else {
            //Datos del modelo
            var model = [];
            var columnas = plan.columnas;
            var siders = plan.siders;
            var colores = plan.colores;
            //Header columnas
            var col = 2;
            for (var i = 0; i < columnas.length; i++) {
                model.push({
                    key: "ch" + columnas[i].identificador,
                    text: columnas[i].nombre,
                    col: col,
                    category: "Column Header",
                    peso: columnas[i].peso,
                    color_header: columnas[i].color
                });
                col++;
            }
            //Rows sider
            var row = 2;
            var filas = new Array();
            for (var i = 0; i < siders.length; i++) {
                model.push({
                    key: "sd" + siders[i].idSider,
                    text: siders[i].nombre,
                    row: row,
                    peso: siders[i].peso,
                    category: "Row Sider"
                });
                filas.push({idGerencia: siders[i].idSider, fila: row});
                row++;
            }
            //Celdas
            celdas = new Array();
            var cont = new Array();
            for (var i = 0; i < siders.length; i++) {
                for (var j = 0; j < columnas.length; j++) {
                    var celda = "Celda(" + siders[i].idSider + "," + columnas[j].identificador + ")";
                    cont["Fila(" + siders[i].idSider + ")"] = 0;
                    model.push({
                        key: celda,
                        text: celda,
                        col: j + 2,
                        color: columnas[j].color + "1e",
                        row: i + 2,
                        isGroup: true
                    });
                    // Tareas Invisibles
                    model.push({
                        key: generateUUID(),
                        text: "",
                        color: "white",
                        size: "3 3",
                        group: celda
                    });
                    celdas[celda] = {pos: 0, x: 0, y: 0};
                }
            }
            var tareasGrafico = plan.tareas;
            var tam = tareasGrafico.length;
            var gruposDiagrama = new Array();
            for (var i = 0; i < tam; i++) {
                var celda = "Celda(" + tareasGrafico[i].idGerencia + "," + tareasGrafico[i].idCriticidad + ")";
                //Buscar la tarea para la celda
                if (tareasGrafico[i].partida) {
                    celdas[celda].x = 0;
                    celdas[celda].y = 0;
                }
                var objetoPlan = {
                    key: "" + tareasGrafico[i].key,
                    text: tareasGrafico[i].nombre,
                    cargo: tareasGrafico[i].cargo,
                    producto: tareasGrafico[i].producto,
                    partida: tareasGrafico[i].partida,
                    bounds: celdas[celda].x + " " + celdas[celda].y,
                    tarea_id: tareasGrafico[i].tarea_id,
                    color_borde: tareasGrafico[i].color,
                    color: tareasGrafico[i].hito ? tareasGrafico[i].color : tareasGrafico[i].partida ? tareasGrafico[i].color : "#ffebee",
                    group: celda,
                    category: tareasGrafico[i].hito ? "Hito" : tareasGrafico[i].partida ? "Start" : ""
                }
                model.push(objetoPlan);
                celdas[celda].pos++;
                celdas[celda].x += 500;
                if (celdas[celda].pos % 3 === 0) {
                    celdas[celda].x = 0;
                    celdas[celda].y += 300;
                }
            }
            var yTranversal = 150;
            var tareasTranversales = plan.tareasTranversales;
            var tamTranversal = tareasTranversales.length;
            for (var i = 0; i < tamTranversal; i++) {
                var celda = "Celda(" + tareasTranversales[i].idGerencia + "," + tareasTranversales[i].idCriticidad + ")";
                var texto = "Fila(" + tareasTranversales[i].idGerencia + ")";
                //Buscar la tarea para la celda
                var fila = buscarFila(filas, tareasTranversales[i].idGerencia);
                var columna = tareasTranversales[i].col;
                var nombreGrupo = "tv" + fila + tareasTranversales[i].grupo;
                // var celdaMayorCantidad = buscarMaximoFila(columnas, tareasTranversales[i].idGerencia, tareasTranversales[i].idCriticidad, tareasTranversales[i].colSpan);
                var grupo = {
                    key: nombreGrupo,
                    text: "",
                    color: "transparent",
                    // bounds: celdaMayorCantidad.x + " " + celdaMayorCantidad.y,
                    col: columna,
                    colSpan: tareasTranversales[i].colSpan,
                    row: fila,
                    isGroup: true
                };
                if (!existeGrupo(gruposDiagrama, grupo.key)) {
                    gruposDiagrama.push(grupo);
                    model.push(grupo);
                }
                model.push({
                    key: generateUUID(),
                    text: "",
                    color: "transparent",
                    bounds: 0 + " " + yTranversal,
                    size: "0 " + 200,
                    group: nombreGrupo
                });
                var objetoTranversal = {
                    key: "" + tareasTranversales[i].key,
                    text: tareasTranversales[i].nombre,
                    size: tareasTranversales[i].colSpan * (tam + tamTranversal + 800) + " " + 100,
                    bounds: 0 + " " + yTranversal,
                    partida: tareasTranversales[i].partida,
                    tarea_id: tareasTranversales[i].tarea_id,
                    cargo: tareasTranversales[i].cargo,
                    producto: tareasTranversales[i].producto,
                    color_borde: tareasTranversales[i].color,
                    color: tareasTranversales[i].hito ? tareasTranversales[i].color : tareasTranversales[i].partida ? tareasTranversales[i].color : "#ffebee",
                    group: nombreGrupo,
                    tranversal: tareasTranversales[i].tranversal,
                    category: ""
                };
                yTranversal += 300;
                model.push(objetoTranversal);
            }
            for (var i = 0; i < myDiagram.model.nodeDataArray.length; i++) {
                if (myDiagram.model.nodeDataArray[i].key == key) {
                    myDiagram.model.nodeDataArray[i].titulo = titulo;
                    myDiagram.model.nodeDataArray[i].id = id;
                    myDiagram.model.nodeDataArray[i].cargo = cargo;
                    myDiagram.model.nodeDataArray[i].producto = producto;
                    break;
                }
            }

            myDiagram.model = new go.GraphLinksModel(model, links);
        }
    };

    function buscarFila(filas, idGerencia) {
        var tam = filas.length;
        for (i = 0; i < tam; i++) {
            if (parseInt(filas[i].idGerencia) === parseInt(idGerencia)) {
                return filas[i].fila;
            }
        }
        return null;
    }

    function insertarEliminarLink(from, to, accion, planId) {
        $("#modal-confirmar").modal({
            show: true
        });
        $("#btn-confirmar-si").click(function (e) {
            e.preventDefault();
            Metronic.blockUI({target: '#form-diagrama .portlet-body', animate: true});

            servicioPlan.insertarEliminarLink(from, to, accion, planId).then(function (resp) {
                Metronic.unblockUI('#form-diagrama .portlet-body');
                if (resp.data.success) {
                    creada = true;
                    toastr.success(resp.data.message, "Exito !!!");
                } else {
                    toastr.error(resp.data.error, "Error !!!");
                }
            }).catch(function (error) {

            });
        });
        $("#btn-confirmar-no").click(function (e) {
            e.preventDefault();
            myDiagram.commandHandler.undo();
        });
    }

    function eliminarTareaGrafico(tareaId, planId) {
        $("#modal-confirmar-eliminar-tarea").modal({
            show: true
        });
        $("#btn-confirmar-tarea-si").click(function (e) {
            e.preventDefault();
            Metronic.blockUI({target: '#form-diagrama .portlet-body', animate: true});
            servicioPlan.eliminarTareaGrafico(tareaId, planId).then(function (resp) {
                Metronic.unblockUI('#form-diagrama .portlet-body');
                if (resp.data.success) {
                    toastr.success(resp.data.message, "Exito !!!");
                } else {
                    toastr.error(resp.data.error, "Error !!!");
                }
            }).catch(function (error) {

            });
        });
        $("#btn-confirmar-tarea-no").click(function (e) {
            e.preventDefault();
            myDiagram.commandHandler.undo();
        });
    }

    function buscarMaximoFila(columnas, idGerencia, idCriticidad, colSpand) {
        var pos = 0;
        var posInicial = buscarPosicionNivelAlerta(columnas, idCriticidad);
        var celda = "Celda(" + idGerencia + "," + idCriticidad + ")";
        celdas[celda].y += 100;
        celdas[celda].x = 0;
        celdas[celda].pos += 2;
        while (pos < colSpand && posInicial < columnas.length - 1) {
            var celdaSiguiente = "Celda(" + idGerencia + "," + columnas[posInicial + 1].identificador + ")";
            if (celdas[celda].y < celdas[celdaSiguiente].y)
                celda = celdaSiguiente;
            pos++;
            posInicial++;
            celdas[celdaSiguiente].y += 300;
            celdas[celdaSiguiente].x = 0;
            celdas[celdaSiguiente].pos += 2;
        }
        celdas[celda].y += 300;
        celdas[celda].x = 0;
        celdas[celda].pos += 2;
        return celdas[celda];
    }

    function buscarPosicionNivelAlerta(columnas, idCriticidad) {
        for (var i = 0; i < columnas.length; i++) {
            if (parseInt(idCriticidad) === parseInt(columnas[i].identificador))
                return i;
        }
        return -1;
    }

    function existeGrupo(grupos, clave) {
        var tam = grupos.length;
        for (i = 0; i < tam; i++) {
            if (grupos[i].key === clave) {
                return true;
            }
        }
        return false;
    }

    function showConnections(node) {
        var myDiagram = node.diagram;
        myDiagram.startTransaction("highlight");
        // remove any previous highlighting
        myDiagram.clearHighlighteds();
        // for each Link coming out of the Node, set Link.isHighlighted
        node.findLinksOutOf().each(function (l) {
            l.isHighlighted = true;
        });
        // for each Node destination for the Node, set Node.isHighlighted
        node.findNodesOutOf().each(function (n) {
            n.isHighlighted = true;
        });
        myDiagram.commitTransaction("highlight");
    }

    var existePrimeraTarea = function () {
        var result = false;
        var modelo = myDiagram.model;
        for (var i = 0; i < modelo.nodeDataArray.length; i++) {
            if (modelo.nodeDataArray[i].id) {
                result = true;
                break;
            }
        }
        return result;
    };

    var save = function (plan) {
        var modelo = myDiagram.model.toJson();
        myDiagram.isModified = false;
        localStorage.setItem("plan" + plan.plan_id, modelo);
    };

    var load = function (plan) {
        var modelo = localStorage.getItem("plan" + plan.plan_id);
        if (modelo !== undefined && modelo !== null) {
            myDiagram.model = go.Model.fromJson(modelo);
            myDiagram.delayInitialization(relayoutDiagram);
        }

    };

    function mostrarDetallesTarea(plan, idTarea) {
        Metronic.blockUI({target: '#form-diagrama .portlet-body', animate: true});
        servicioPlan.buscarTarea(idTarea, plan.plan_id).then(success).catch(failed);

        function success(response) {
            Metronic.unblockUI('#form-diagrama .portlet-body');
            $('#modal-tarea-detalle').modal({
                'show': true
            });
            scopePlan.antecesoras = new Array();
            scopePlan.sucesoras = new Array();
            scopePlan.tarea = response.data.tarea;
            scopePlan.antecesoras = response.data.antecesoras;
            scopePlan.sucesoras = response.data.sucesoras;
            scopePlan.tituloModal = response.data.tarea.nombre;
            $('#table-modelos-tarea .download').data("title", 'Descargar modelo').tooltip();
            $('#table-modelos-tarea .view').data("title", 'Ver Detalles de la Tarea').tooltip();
        }

        function failed(error) {
            loggerSistema.error('Error !!' + error.data);
        }
    }

    var pintarBorde = function (plan, datos) {
        var modelo = myDiagram.model;
        var colores = plan.colores;
        for (var i = 0; i < modelo.nodeDataArray.length; i++) {
            if (modelo.nodeDataArray[i].id) {
                var group = modelo.nodeDataArray[i].group;
                index = parseInt(group[9] - 2);
                var color_borde = colores[index];
                modelo.nodeDataArray[i].color_borde = color_borde;
                modelo.nodeDataArray[i].row = group[7];
                modelo.nodeDataArray[i].col = group[9];
                //Saber si es la primera tarea
                if (modelo.nodeDataArray[i].partida) {
                    modelo.nodeDataArray[i].category = "Terminator";
                    modelo.nodeDataArray[i].color = color_borde;
                    start++;
                }
            }
        }
        save(plan);
        setTimeout(function () {
            load(plan);
        }, 100);
    };

    var destroyDiagrama = function () {
        $('#div-diagrama').html('<div id="myDiagramDiv" style="height:500px;"></div>');
        $('#div-paleta').html('<div id="myPaletteDiv" style="height:500px;"></div>');
        myDiagram = null;
    };
    var relayoutDiagram = function () {
        myDiagram.layout.invalidateLayout();
        myDiagram.layoutDiagram();
    };
    var btnClickZoomToFit = function () {
        var div = myDiagram.div;
        var height = 700;
        div.style.height = height + 'px';
        myDiagram.requestUpdate();
        myDiagram.zoomToFit();
    };
    var btnClickCenterRoot = function () {
        var div = myDiagram.div;
        var height = parseInt(myDiagram.documentBounds.height / 2) + 24;
        div.style.height = height + 'px';
        myDiagram.requestUpdate();
        myDiagram.scale = 0.5;
        myDiagram.scrollToRect(new go.Rect(0, 0, 20, 20));
    };
    var btnClickIncreaseZoom = function () {
        myDiagram.commandHandler.increaseZoom();
    };
    var btnClickDecreaseZoom = function () {
        myDiagram.commandHandler.decreaseZoom();
    };
    var generateUUID = function () {
        var d = new Date().getTime();
        var uuid = 'xxxxxxxxxxxx4xxxyxxxxxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = (d + Math.random() * 16) % 16 | 0;
            d = Math.floor(d / 16);
            return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
        });
        return uuid;
    };
    var btnClickSalvarDiagrama = function () {
        Metronic.blockUI({target: '#form-diagrama .portlet-body', animate: true});
        var plan_id = $('#plan_id').val();
        myDiagram.model.modelData.position = go.Point.stringify(myDiagram.position);
        var diagrama = myDiagram.model.toJson();
        $.ajax({
            type: "POST",
            url: "plan/salvarDiagrama",
            dataType: "json",
            data: {
                'plan_id': plan_id,
                'diagrama': diagrama
            },
            success: function (response) {
                Metronic.unblockUI('#form-diagrama .portlet-body');
                if (response.success) {

                    toastr.success(response.message, "Exito !!!");

                } else {
                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            },
            failure: function (response) {
                Metronic.unblockUI('#form-diagrama .portlet-body');

                toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
            }
        });
    };

    var btnClickBorrarDiagrama = function () {
        Metronic.blockUI({target: '#form-diagrama .portlet-body', animate: true});
        var plan_id = $('#plan_id').val();
        $.ajax({
            type: "POST",
            url: "plan/borrarDiagrama?plan_id=" + plan_id,
            dataType: "json",
            data: {
                'plan_id': plan_id
            },
            success: function (response) {
                Metronic.unblockUI('#form-diagrama .portlet-body');
                if (response.success) {
                    toastr.success(response.message, "Exito !!!");
                } else {
                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            },
            failure: function (response) {
                Metronic.unblockUI('#form-diagrama .portlet-body');
                toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
            }
        });
    };

    var exportar = function () {
        var ancho = myDiagram.viewportBounds.width;
        var alto = myDiagram.viewportBounds.height;
        var imagen = myDiagram.makeImage({
            scale: 0.2,
            size: new go.Size(ancho, alto),
            maxSize: new go.Size(ancho + 2, alto + 2)
        });
        $('#a-diagrama-imagen').attr('href', imagen.src);
        $('#diagrama-imagen').attr('src', imagen.src);
        $('#modal-imagen').modal({
            'show': true
        });
    }

    return {
        //main function to initiate the module
        init: function (url, planService, scope, logger) {
            urlPath = url;
            //Inicializar fechas de filtro
            initFechasFiltro();

            initTable(planService, scope, logger);
            initForm();
            //Importar tareas
            //Tareas
            initFormTarea();
            initFormArchivo();
            //Relacionados
            if (cont === 0) {
                initAccionEditar();
                initAccionEliminar();
                //Importar
                initAccionImportar();
                //Tareas
                initTableTareas();
                initSeletGerencia();
                initSeletArea();
                initTableArchivo();
                //cargos
                initSeletGerencia();
                initSeletArea();
                //Diagrama
                initAccionDiagrama();
                initAccionClonar();
            }
            cont++;
        },
        btnClickNuevo: btnClickNuevo,
        btnClickEliminar: btnClickEliminar,
        mostrarDiagrama: mostrarDiagrama,
        btnClickImportarTarea: btnClickSalvarFormTarea,
        btnClickClonarPlan: btnClickClonarPlan,
        btnClickModalEliminar: btnClickModalEliminar,
        btnClickModalEliminarSeleccion: btnClickModalEliminarSeleccion,
        btnClickCerrarForm: btnClickCerrarForm,
        btnClickSalvarForm: btnClickSalvarForm,
        btnClickFiltrar: btnClickFiltrar,
        btnClickMostrarModalImportar: btnClickMostrarModalImportar,
        btnClickFiltrarImportarTareas: btnClickFiltrarImportarTareas,
        btnClickSalvarFormTarea: btnClickSalvarFormTarea,
        btnClickSalvarArchivo: btnClickSalvarArchivo,
        btnClickZoomToFit: btnClickZoomToFit,
        btnClickCenterRoot: btnClickCenterRoot,
        btnClickIncreaseZoom: btnClickIncreaseZoom,
        btnClickDecreaseZoom: btnClickDecreaseZoom,
        btnClickSalvarDiagrama: btnClickSalvarDiagrama,
        btnClickBorrarDiagrama: btnClickBorrarDiagrama,
        filtrarTareasPlan: filtrarTareasPlan,
        exportar: exportar
    };

}
();