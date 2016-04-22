(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .factory('RoleResources', RoleResources);

    RoleResources.$inject = ['$resource'];

    function RoleResources($resource) {
        var resourceUrl =  'api/resources/';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
