(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('RoleManagementDeleteController',RoleManagementDeleteController);

    RoleManagementDeleteController.$inject = ['$uibModalInstance', 'entity', 'Role'];

    function RoleManagementDeleteController($uibModalInstance, entity, Role) {
        var vm = this;
        vm.role = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (name) {
            Role.delete({name: name},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
