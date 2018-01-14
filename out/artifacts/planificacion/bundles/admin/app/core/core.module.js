(function () {
    'use strict';

    angular.module('app.core', [
        /*
         * Angular modules
         */
        "ui.router", "ui.bootstrap", "oc.lazyLoad", "ngSanitize",
        /*
         * Our reusable cross app code modules
         */
        'blocks.exception', 'blocks.logger'
        /*
         * 3rd Party modules
         */

    ])
        .run(run)
    ;

    run.$inject = ["$rootScope", "settings", "$state"];
    function run($rootScope, settings, $state) {
        $rootScope.$state = $state; // state to be accessed from view
        $rootScope.settings = settings;
    }

})();
