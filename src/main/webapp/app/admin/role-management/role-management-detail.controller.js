(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('RoleManagementDetailController', RoleManagementDetailController);

    RoleManagementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Role'];

    function RoleManagementDetailController($scope, $rootScope, $stateParams, entity, Role) {
        var vm = this;
        vm.role = entity;
        vm.load = function (name) {
            Role.get({name: name}, function(result) {
                vm.role = result;
            });
        };
        var unsubscribe = $rootScope.$on('jhipsterApp:roleUpdate', function(event, result) {
            vm.role = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
