'use strict';

describe('Controller Tests', function() {

    describe('Role Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRole, MockResource;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRole = jasmine.createSpy('MockRole');
            MockResource = jasmine.createSpy('MockResource');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Role': MockRole,
                'Resource': MockResource
            };
            createController = function() {
                $injector.get('$controller')("RoleManagementDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jhipsterApp:roleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
