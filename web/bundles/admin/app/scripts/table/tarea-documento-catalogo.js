/* global Metronic, CKEDITOR */

var TableTareasDocumentosCatalogo = function () {

    var urlPath;
    var oTable;
    var rowDelete = null;
    var cont = 0;
    var plan = {};
    var servicioPlan = null;
    var formTitle = "¿Deseas crear una nueva tarea? Sigue los siguientes pasos:";

    //Inicializa la tabla
    var initTable = function (scope, planService) {
        servicioPlan = planService;
        var table = $('#tarea-table-editable');

        var order = [[1, "desc"]];
        var aoColumns = [
            {
                "className": 'details-control',
                "orderable": false,
                "data": null,
                "defaultContent": ''
            },
            {"bSortable": true, "sWidth": '20%', "sClass": 'text-center'},
            {"bSortable": true, "sWidth": '55%'},
            {"bSortable": true, "sWidth": '15%'},
            {"bSortable": true, "sWidth": '10%', "sClass": 'text-center'}
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
                    "url": "tarea/documentos" // ajax source
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
        // Add event listener for opening and closing details
        $('#tarea-table-editable tbody').on('click', 'td.details-control', function () {
            var tr = $(this).closest('tr');
            var row = oTable.getDataTable().row(tr);

            if (row.child.isShown()) {
                // This row is already open - close it
                row.child.hide();
                tr.removeClass('shown');
            }
            else {
                // Open this row
                row.child(format(row.data())).show();
                tr.addClass('shown');
            }
        });

        function format(d) {
            var tabla_modelos = '<table style="padding-left:50px;width: 100%" class="table table-striped table-bordered table-hover dt-responsive dataTable no-footer dtr-inline">';
            var modelos = d.documentos;

            //Agregar fila vacia
            if (modelos.length === 0) {
                var tr = '<tr>' +
                    '<td colspan="5" style="text-align: center">No existen archivos</td>' +
                    '</tr>';
                return tabla_modelos + '<tbody>' + tr + '</tbody></table>';
            }
            //Agregar elementos
            tabla_modelos += '<thead style="padding-left: 50px;"><tr><th>#</th><th>Nombre</th><th>Descripción</th><th>Estado</th></tr></thead>';
            tabla_modelos += '<tbody style="padding-left: 50px;">';
            for (var i = 0; i < modelos.length; i++) {
                var download_url = urlPath + "uploads/" + modelos[i].ruta;
                var estado = (modelos[i].estado) ? 'Activo <i class="fa fa-check-circle ic-color-ok"></i>' : 'Inactivo <i class="fa fa-minus-circle ic-color-error"></i>';
                var tr = '<tr id="' + i + '">' +
                    '<td style="width: 3%;">' + (i + 1) + '</td>' +
                    '<td style="width: 40%;"><a href="uploads/' + modelos[i].ruta + '" target="_new">' + modelos[i].ruta + '</a></td>' +
                    '<td style="width: 40%;">' + (modelos[i].descripcion === null ? "" : modelos[i].descripcion) + '</td>' +
                    '<td style="width: 10%;">' + estado + '</td>' +
                    '</tr>';
                tabla_modelos += tr;
            }
            return tabla_modelos + '</tbody></table>';
        }
    }
    //Buscar Tareas
    var buscarTareas = function (url) {
        oTable.getDataTable().ajax.url(url);
        oTable.getDataTable().ajax.reload();
    };

    return {
        //main function to initiate the module
        init: function (url, scope, planService) {
            urlPath = url;
            //Inicializar fechas de filtro
            initTable(scope, planService);
        },
        buscarTareas: buscarTareas
    };

}();