(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('role-management', {
            parent: 'admin',
            url: '/role-management?page&sort',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'jhipsterApp.role.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/role-management/roles-management.html',
                    controller: 'RoleManagementController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                }
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort)
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('role-management');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('role-management-detail', {
            parent: 'entity',
            url: '/role/{name}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'jhipsterApp.role.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/role-management/role-management-detail.html',
                    controller: 'RoleManagementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('role-management');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Role', function($stateParams, Role) {
                    return Role.get({name : $stateParams.name});
                }]
            }
        })
        .state('role-management.new', {
            parent: 'role-management',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/role-management/role-management-dialog.html',
                    controller: 'RoleManagementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('role-management', null, { reload: true });
                }, function() {
                    $state.go('role-management');
                });
            }]
        })
        .state('role-management.edit', {
            parent: 'role-management',
            url: '/{name}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/role-management/role-management-dialog.html',
                    controller: 'RoleManagementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Role', function(Role) {
                            return Role.get({name : $stateParams.name});
                        }]
                    }
                }).result.then(function() {
                    $state.go('role-management', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('role-management.delete', {
            parent: 'role-management',
            url: '/{name}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/role-management/role-management-delete-dialog.html',
                    controller: 'RoleManagementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Role', function(Role) {
                            return Role.get({name : $stateParams.name});
                        }]
                    }
                }).result.then(function() {
                    $state.go('role-management', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
