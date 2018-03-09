var TableEditableIndex = function () {

    var tareas = [];
    var myDiagram = null;

    var initDiagrama = function () {
        var $ = go.GraphObject.make;

        myDiagram =
                $(go.Diagram, "diagrama", {
                    initialContentAlignment: go.Spot.Center, // center Diagram contents
                    "undoManager.isEnabled": true, // enable Ctrl-Z to undo and Ctrl-Y to redo
                    //"grid.visible": true,
                    allowLink: false,
                    allowRelink: false,

                });

        var nodeSelectionAdornmentTemplate = $(go.Adornment, "Auto",
                $(go.Shape, {fill: null, stroke: "deepskyblue", strokeWidth: 1.5, strokeDashArray: [4, 2]}),
                $(go.Placeholder));

        var nodeResizeAdornmentTemplate = $(go.Adornment, "Spot", {
            locationSpot: go.Spot.Right},
                $(go.Placeholder),
                $(go.Shape, {alignment: go.Spot.TopLeft, cursor: "nw-resize", desiredSize: new go.Size(6, 6), fill: "lightblue", stroke: "deepskyblue"}),
                $(go.Shape, {alignment: go.Spot.Top, cursor: "n-resize", desiredSize: new go.Size(6, 6), fill: "lightblue", stroke: "deepskyblue"}),
                $(go.Shape, {alignment: go.Spot.TopRight, cursor: "ne-resize", desiredSize: new go.Size(6, 6), fill: "lightblue", stroke: "deepskyblue"}),
                $(go.Shape, {alignment: go.Spot.Left, cursor: "w-resize", desiredSize: new go.Size(6, 6), fill: "lightblue", stroke: "deepskyblue"}),
                $(go.Shape, {alignment: go.Spot.Right, cursor: "e-resize", desiredSize: new go.Size(6, 6), fill: "lightblue", stroke: "deepskyblue"}),
                $(go.Shape, {alignment: go.Spot.BottomLeft, cursor: "se-resize", desiredSize: new go.Size(6, 6), fill: "lightblue", stroke: "deepskyblue"}),
                $(go.Shape, {alignment: go.Spot.Bottom, cursor: "s-resize", desiredSize: new go.Size(6, 6), fill: "lightblue", stroke: "deepskyblue"}),
                $(go.Shape, {alignment: go.Spot.BottomRight, cursor: "sw-resize", desiredSize: new go.Size(6, 6), fill: "lightblue", stroke: "deepskyblue"})
                );

        myDiagram.nodeTemplate =
                $(go.Node, "Auto", {
                    doubleClick: function (e, node) {
                        mostrarModalTarea(node.data);
                    }
                },
                        new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
                        new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify),
                        {selectable: true, selectionAdornmentTemplate: nodeSelectionAdornmentTemplate},
                        {resizable: true, resizeObjectName: "PANEL", resizeAdornmentTemplate: nodeResizeAdornmentTemplate},
                        $(go.Shape, "RoundedRectangle",
                                {
                                    strokeWidth: 0,
                                    portId: "",
                                    fromLinkable: true,
                                    toLinkable: true,
                                },
                                new go.Binding("fill", "color")),
                        $(go.TextBlock,
                                {margin: 8},
                                new go.Binding("text", "titulo"))
                        );

        myDiagram.linkTemplate =
                $(go.Link,
                        {routing: go.Link.AvoidsNodes, corner: 5},
                        {relinkableFrom: true, relinkableTo: true},
                        $(go.Shape),
                        $(go.Shape, {toArrow: "Standard"})
                        );

        myDiagram.model = new go.GraphLinksModel(tareas[0], tareas[1]);
    }

    function salvar() {
        var model = myDiagram.model.nodeDataArray;

        Metronic.blockUI({target: '#row-diagrama', animate: true});

        $.ajax({
            type: "POST",
            url: "tarea/salvarDiagrama",
            dataType: "json",
            data: {
                'diagrama': model
            },
            success: function (response) {
                Metronic.unblockUI('#row-diagrama');
                if (response.success) {

                } else {
                    toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
                }
            },
            failure: function (response) {
                Metronic.unblockUI('#row-diagrama');

                toastr.error(response.error, "Error !!!", {"positionClass": "toast-top-center"});
            }
        });
    }

    function mostrarModalTarea(tarea) {
        $('#tarea-detalle-codigo').html(tarea.codigo);
        $('#tarea-detalle-titulo').html(tarea.titulo);
        $('#tarea-detalle-fecha').html(tarea.fecha);

        $('#modal-detalle-tarea').modal({
            'show': true
        });
    }

    return {
        //main function to initiate the module
        init: function (tareas_param) {
            tareas = tareas_param;

            initDiagrama();
        },
        salvar: salvar
    };

}();