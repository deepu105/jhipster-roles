(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('RoleManagementDialogController', RoleManagementDialogController);

    RoleManagementDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Role', 'RoleResources'];

    function RoleManagementDialogController ($scope, $stateParams, $uibModalInstance, entity, Role, RoleResources) {
        var vm = this;
        vm.role = entity;
        vm.setResources = setResources;

        if(entity.$promise) {
            entity.$promise.then(function() {
                vm.setResources();
            }.bind(this));
        } else {
            vm.setResources();
        }

        var onSaveSuccess = function (result) {
            $scope.$emit('jhipsterApp:roleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (entity.name !== null) {
                Role.update(vm.role, onSaveSuccess, onSaveError);
            } else {
                Role.save(vm.role, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        function setResources() {
            if (!vm.role.resources) {
                RoleResources.query(function (resources) {
                    vm.role.resources = resources.map(function(resourceName) {
                        return {
                            name: resourceName,
                            permission : 0
                        }
                    });
                });
            } else {
                RoleResources.query(function (resources) {
                    angular.forEach(resources, function(resourceName, key) {
                        if (vm.role.resources.filter(function(obj) { return obj.name === resourceName; }).length === 0) {
                            vm.role.resources.push({
                                name: resourceName,
                                permission : 0
                            });
                        }
                    });
                });
            }
        };
    }
})();
