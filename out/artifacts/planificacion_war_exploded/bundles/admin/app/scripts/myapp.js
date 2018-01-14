/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var MyApp = function () {

    var handleSummernote = function () {
        /* set variables locally for increased performance */
        if (!jQuery().summernote) {
            return;
        }
        $.extend($.summernote.lang, {
            'es-ES': {
                font: {
                    name: 'Fuente',
                    bold: 'Negrita',
                    italic: 'Cursiva',
                    underline: 'Subrayado',
                    superscript: 'Superíndice',
                    subscript: 'Subíndice',
                    strikethrough: 'Tachado',
                    clear: 'Quitar estilo de fuente',
                    height: 'Altura de línea',
                    size: 'Tamaño de la fuente'
                },
                image: {
                    image: 'Imagen',
                    insert: 'Insertar imagen',
                    resizeFull: 'Redimensionar a tamaño completo',
                    resizeHalf: 'Redimensionar a la mitad',
                    resizeQuarter: 'Redimensionar a un cuarto',
                    floatLeft: 'Flotar a la izquierda',
                    floatRight: 'Flotar a la derecha',
                    floatNone: 'No flotar',
                    dragImageHere: 'Arrastrar una imagen aquí',
                    selectFromFiles: 'Seleccionar desde los archivos',
                    url: 'URL de la imagen'
                },
                link: {
                    link: 'Link',
                    insert: 'Insertar link',
                    unlink: 'Quitar link',
                    edit: 'Editar',
                    textToDisplay: 'Texto para mostrar',
                    url: '¿Hacia que URL lleva el link?',
                    openInNewWindow: 'Abrir en una nueva ventana'
                },
                video: {
                    video: 'Video',
                    videoLink: 'Link del video',
                    insert: 'Insertar video',
                    url: '¿URL del video?',
                    providers: '(YouTube, Vimeo, Vine, Instagram, DailyMotion, o Youku)'
                },
                table: {
                    table: 'Tabla'
                },
                hr: {
                    insert: 'Insertar línea horizontal'
                },
                style: {
                    style: 'Estilo',
                    normal: 'Normal',
                    blockquote: 'Cita',
                    pre: 'Código',
                    h1: 'Título 1',
                    h2: 'Título 2',
                    h3: 'Título 3',
                    h4: 'Título 4',
                    h5: 'Título 5',
                    h6: 'Título 6'
                },
                lists: {
                    unordered: 'Lista desordenada',
                    ordered: 'Lista ordenada'
                },
                options: {
                    help: 'Ayuda',
                    fullscreen: 'Pantalla completa',
                    codeview: 'Ver código fuente'
                },
                paragraph: {
                    paragraph: 'Párrafo',
                    outdent: 'Menos tabulación',
                    indent: 'Más tabulación',
                    left: 'Alinear a la izquierda',
                    center: 'Alinear al centro',
                    right: 'Alinear a la derecha',
                    justify: 'Justificar'
                },
                color: {
                    recent: 'Último color',
                    more: 'Más colores',
                    background: 'Color de fondo',
                    foreground: 'Color de fuente',
                    transparent: 'Transparente',
                    setTransparent: 'Establecer transparente',
                    reset: 'Restaurar',
                    resetToDefault: 'Restaurar por defecto'
                },
                shortcut: {
                    shortcuts: 'Atajos de teclado',
                    close: 'Cerrar',
                    textFormatting: 'Formato de texto',
                    action: 'Acción',
                    paragraphFormatting: 'Formato de párrafo',
                    documentStyle: 'Estilo de documento'
                },
                history: {
                    undo: 'Deshacer',
                    redo: 'Rehacer'
                }
            }
        });
    };

    var handleSelect = function () {
        /* set variables locally for increased performance */
        if (!jQuery().select2) {
            return;
        }
        $.fn.select2.locales['es'] = {
            formatNoMatches: function () {
                return "No se encontraron resultados";
            },
            formatInputTooShort: function (input, min) {
                var n = min - input.length;
                return "Por favor, introduzca " + n + " car" + (n == 1 ? "ácter" : "acteres");
            },
            formatInputTooLong: function (input, max) {
                var n = input.length - max;
                return "Por favor, elimine " + n + " car" + (n == 1 ? "ácter" : "acteres");
            },
            formatSelectionTooBig: function (limit) {
                return "Sólo puede seleccionar " + limit + " elemento" + (limit == 1 ? "" : "s");
            },
            formatLoadMore: function (pageNumber) {
                return "Cargando más resultados…";
            },
            formatSearching: function () {
                return "Buscando…";
            }
        };

        $.extend($.fn.select2.defaults, $.fn.select2.locales['es']);

    };

    var handleToolTips = function () {
        $('.portlet > .portlet-title > .tools > .remove').tooltip({
            container: 'body',
            title: 'Cerrar'
        });
        $('.my-remove').tooltip({
            container: 'body',
            title: 'Cerrar'
        });
        $(".tools .collapse").data("title", 'Colapsar').tooltip();
        $(".tools .expand").data("title", 'Expandir').tooltip();
        $('.exportar-excel').tooltip({
            container: 'body',
            title: 'Exportar excel'
        });
        $('.importar-excel').tooltip({
            container: 'body',
            title: 'Importar excel'
        });
    };

    var handleTools = function () {
        $('body').on('click', '.portlet > .portlet-title > .tools > .collapse, .portlet .portlet-title > .tools > .expand', function (e) {
            e.preventDefault();
            var el = $(this).closest(".portlet").children(".portlet-body");
            if ($(this).hasClass("collapse")) {
                $(".collapse").data("title", "").tooltip("destroy");
                $(this).removeClass("collapse").addClass("expand");
                $(".expand").data("title", 'Expandir').tooltip();

                el.slideUp(100);
            } else {
                $(".expand").data("title", "").tooltip("destroy");
                $(this).removeClass("expand").addClass("collapse");
                $(".collapse").data("title", 'Colapsar').tooltip();

                el.slideDown(100);
            }
        });

    };

    var handleDateTimePickers = function () {
        if (!jQuery().datepicker) {
            return;
        }

        $.fn.datepicker.dates.es = {
            days: ["Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"],
            daysShort: ["Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"],
            daysMin: ["Do", "Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do"],
            months: ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"],
            monthsShort: ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"],
            today: "Hoy",
            clear: "Borrar",
            weekStart: 1,
            format: "dd/mm/yyyy"
        };

        if (!jQuery().datetimepicker) {
            return;
        }

        $.fn.datetimepicker.dates['es'] = {
            days: ["Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"],
            daysShort: ["Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"],
            daysMin: ["Do", "Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do"],
            months: ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"],
            monthsShort: ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"],
            today: "Hoy",
            suffix: [],
            meridiem: []
        };

    };

    var handleNewValidateType = function () {
        jQuery.validator.addMethod("rut", function (value, element) {
            return this.optional(element) || $.Rut.validar(value);
        }, "Este campo debe ser un rut valido.");

        jQuery.validator.addMethod("date60",
            function (value, element) {
                //La fecha inicial no puede ser anterior a 60 dias
                var result = false;
                if (value === "") {
                    result = true;
                    return result;
                }

                var value_array = value.split('/');
                var value_dia = value_array[0];
                var value_mes = value_array[1];
                var value_year = value_array[2];

                var value_fecha = new Date();
                value_fecha.setDate(value_dia);
                value_fecha.setMonth(parseInt(value_mes) - 1);
                value_fecha.setYear(value_year);

                value = value_fecha.format('Y/m/d');

                //Anterior 60 dias
                var fecha_actual_menos_60 = new Date();
                var mouths_menos_60 = fecha_actual_menos_60.getMonth() - parseInt(2);
                fecha_actual_menos_60.setMonth(mouths_menos_60);
                fecha_actual_menos_60 = fecha_actual_menos_60.format('Y/m/d');
                //Posterior 60 dias
                var fecha_actual_mas_60 = new Date();
                var mouths_mas_60 = fecha_actual_mas_60.getMonth() + parseInt(2);
                fecha_actual_mas_60.setMonth(mouths_mas_60);
                fecha_actual_mas_60 = fecha_actual_mas_60.format('Y/m/d');

                if ((value >= fecha_actual_menos_60) && (value <= fecha_actual_mas_60)) {
                    result = true;
                }

                return result;
            },
            "La fecha inicial no puede ser anterior ni posterior a 60 días"
        );

        $(document).on('keypress', ".just-number", function (e) {
            var keynum = window.event ? window.event.keyCode : e.which;

            if ((keynum === 8) || (keynum === 0))
                return true;

            return /\d/.test(String.fromCharCode(keynum));
        });

        $(document).on('keypress', ".just-letters", function (e) {
            var keynum = window.event ? window.event.keyCode : e.which;

            if ((keynum === 8) || (keynum === 0))
                return true;

            return /^[a-zA-ZñÑáúíóéÁÚÍÓÉ\s]*$/.test(String.fromCharCode(keynum));
        });

        $(document).on('keypress', ".just-rut", function (e) {
            var keynum = window.event ? window.event.keyCode : e.which;

            if ((keynum === 8) || (keynum === 0))
                return true;

            return /^[0-9k\-]*$/.test(String.fromCharCode(keynum));
        });

    };

    //Sumar días a una fecha
    var sumarDiasAFecha = function (fecha, days) {

        fechaVencimiento = "";
        if (fecha !== "") {
            var fecha_registro = fecha;
            var fecha_registro_array = fecha_registro.split("/");
            var year = fecha_registro_array[2];
            var mouth = fecha_registro_array[1] - 1;
            var day = fecha_registro_array[0];

            var fechaVencimiento = new Date(year, mouth, day);

            //Obtenemos los milisegundos desde media noche del 1/1/1970
            var tiempo = fechaVencimiento.getTime();
            //Calculamos los milisegundos sobre la fecha que hay que sumar o restar...
            var milisegundos = parseInt(days * 24 * 60 * 60 * 1000);
            //Modificamos la fecha actual
            fechaVencimiento.setTime(tiempo + milisegundos);
        }


        return fechaVencimiento;
    };
    //Restar días a una fecha
    var restarDiasAFecha = function (fecha, days) {

        fechaVencimiento = "";
        if (fecha !== "") {
            var fecha_registro = fecha;
            var fecha_registro_array = fecha_registro.split("/");
            var year = fecha_registro_array[2];
            var mouth = fecha_registro_array[1] - 1;
            var day = fecha_registro_array[0];

            var fechaVencimiento = new Date(year, mouth, day);

            //Obtenemos los milisegundos desde media noche del 1/1/1970
            var tiempo = fechaVencimiento.getTime();
            //Calculamos los milisegundos sobre la fecha que hay que sumar o restar...
            var milisegundos = parseInt(days * 24 * 60 * 60 * 1000);
            //Modificamos la fecha actual
            fechaVencimiento.setTime(tiempo - milisegundos);
        }


        return fechaVencimiento;
    };
    //Sumar meses a una fecha
    var sumarMesesAFecha = function (fecha, meses) {
        fechaVencimiento = "";
        if (fecha !== "") {
            var fecha_registro = fecha;
            var fecha_registro_array = fecha_registro.split("/");
            var year = fecha_registro_array[2];
            var mouth = fecha_registro_array[1] - 1;
            var day = fecha_registro_array[0];

            var fechaVencimiento = new Date(year, mouth, day);

            var mouths = parseInt(mouth) + parseInt(meses);
            fechaVencimiento.setMonth(mouths);
        }

        return fechaVencimiento;
    };

    var handleInputNumber = function () {
        if (!jQuery().number) {
            return;
        }

        $('.form-control-number').number(true, 0, ',', '.');
    }

    var handleInputMasks = function () {
        $.extend($.inputmask.defaults, {
            'autounmask': true
        });

        $(".mask_date").inputmask("d/m/y", {
            "placeholder": "dd/mm/yyyy"
        });
    };

    var generateUUID = function () {
        var d = new Date().getTime();
        var uuid = 'xxxxxxxxxxxx4xxxyxxxxxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = (d + Math.random() * 16) % 16 | 0;
            d = Math.floor(d / 16);
            return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
        });
        return uuid;
    }

    return {
        //main function to initiate the module
        init: function () {

            handleSummernote();
            handleSelect();
            handleToolTips();
            handleTools();
            handleDateTimePickers();
            handleNewValidateType();
            handleInputNumber();
            handleInputMasks();
        },
        sumarDiasAFecha: sumarDiasAFecha,
        restarDiasAFecha: restarDiasAFecha,
        sumarMesesAFecha: sumarMesesAFecha,
        generateUUID: generateUUID
    };

}();


