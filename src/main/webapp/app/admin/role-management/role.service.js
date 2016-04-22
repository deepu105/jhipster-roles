(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('Role', Role);

    Role.$inject = ['$resource'];

    function Role ($resource) {
        var resourceUrl =  'api/roles/:name';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
