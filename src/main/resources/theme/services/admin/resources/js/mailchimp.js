module.factory('MailChimpConfigUpdater', function ($resource) {
    return $resource(authUrl + '/realms/:realm/mailchimp-resource/config', {
        realm: '@realm'
    }, {
        update: {
            method: 'PUT'
        }
    });
});

module.factory('MailChimpConfigFactory', function ($resource) {
    return $resource(authUrl + '/realms/:realm/mailchimp-resource/config', {
        realm: '@realm'
    });
});

module.factory('MailChimpConfigLoader', function (Loader, MailChimpConfigFactory, $route, $q) {
    return Loader.get(MailChimpConfigFactory, function () {
        return {
            realm: $route.current.params.realm,
        }
    });
});

module.controller('MailChimpConfigCtrl', function ($scope, realm, serverInfo, config, $location, MailChimpConfigUpdater, Notifications) {
    $scope.realm = realm;
    $scope.config = angular.copy(config);
    $scope.listenedEvents = angular.copy(config.listenedEvents);

    $scope.events = {
        'multiple': true,
        'simple_tags': true,
        'tags': serverInfo.enums['eventType']
    };

    $scope.$watch('config', function () {
        if (!angular.equals($scope.config, config)) {
            $scope.changed = true;
        }
    }, true);

    $scope.$watch('listenedEvents', function () {
        if (areEventsDifferent()) {
            $scope.changed = true;
        }
    }, true);

    $scope.save = function () {
        const tmpConfig = {
            listenedEvents: $scope.listenedEvents,
            apiKey: $scope.config.apiKey,
            listId: $scope.config.listId,
            id: $scope.config.id,
        };

        MailChimpConfigUpdater.update({
            realm: realm.realm
        }, tmpConfig, function () {
            $location.url("/realms/" + realm.realm + "/partial-mailchimp");
            Notifications.success("Your changes have been saved to the realm.");
        })
    }

    $scope.cancel = function () {
        $location.url("/realms/" + realm.realm + "/partial-mailchimp");
    };

    $scope.clearEvents = function () {
        Dialog.confirmDelete($scope.realm.realm, 'events', function () {
            RealmEvents.remove({id: $scope.realm.realm}, function () {
                Notifications.success("The events has been cleared.");
            });
        });
    };

    function areEventsDifferent() {
        if (!$scope.listenedEvents) {
            return false;
        }
        if (!config.listenedEvents) {
            return false;
        }

        if (typeof $scope.listenedEvents === "string") {
            return false;
        }
        if (typeof config.listenedEvents === "string") {
            return false;
        }

        const cleanScopeEvents = $scope.listenedEvents.filter(i => !!i);
        const cleanConfigEvents = config.listenedEvents.filter(i => !!i);

        return !angular.equals(cleanScopeEvents, cleanConfigEvents);
    }
});

module.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
        .when('/realms/:realm/partial-mailchimp', {
            templateUrl: resourceUrl + '/partials/partial-mailchimp.html',
            resolve: {
                realm: function (RealmLoader) {
                    return RealmLoader();
                },
                config: function (MailChimpConfigLoader) {
                    return MailChimpConfigLoader();
                },
                serverInfo: function (ServerInfoLoader) {
                    return ServerInfoLoader();
                }
            },
            controller: 'MailChimpConfigCtrl'
        })
}]);
