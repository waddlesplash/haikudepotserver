/*
 * Copyright 2013-2014, Andrew Lindesay
 * Distributed under the terms of the MIT License.
 */

angular.module('haikudepotserver').controller(
    'EditPkgCategoriesController',
    [
        '$scope','$log','$location','$routeParams',
        'jsonRpc','constants','errorHandling',
        'breadcrumbs','referenceData',
        function(
            $scope,$log,$location,$routeParams,
            jsonRpc,constants,errorHandling,
            breadcrumbs,referenceData) {

            // this is the maximum number of categories in which a package may be registered.  This
            // is just enforced in the user interface for practicalities sake.

            var CATEGORIES_LIMIT = 3;

            $scope.breadcrumbItems = undefined;
            $scope.pkg = undefined;
            $scope.amSaving = false;
            $scope.pkgCategories = undefined;

            $scope.shouldSpin = function() {
                return undefined == $scope.pkg || undefined == $scope.pkgCategories || $scope.amSaving;
            };

            // pulls the pkg data back from the server so that it can be used to
            // display the form.

            function refetchPkg() {
                jsonRpc.call(
                        constants.ENDPOINT_API_V1_PKG,
                        "getPkg",
                        [{
                            name: $routeParams.name,
                            versionType: 'NONE',
                            architectureCode: undefined // not required if we don't need the version
                        }]
                    ).then(
                    function(result) {
                        $scope.pkg = result;
                        $log.info('found '+result.name+' pkg');
                        refreshBreadcrumbItems();

                        // now get the categories and from the data in the pkg it should be possible to
                        // pre-select those categories which are presently configured on the pkg.

                        referenceData.pkgCategories().then(
                            function(data) {
                                $scope.pkgCategories = _.clone(data);

                                _.each($scope.pkgCategories, function(c) {
                                    c.selected = _.contains(
                                        $scope.pkg.pkgCategoryCodes,
                                        c.code);
                                });

                                updateDisablementOnPkgCategories();
                            },
                            function() {
                                // logging happens inside
                                $location.path("/error").search({});
                            }
                        )
                    },
                    function(err) {
                        errorHandling.handleJsonRpcError(err);
                    }
                );
            }

            refetchPkg();

            function refreshBreadcrumbItems() {
                $scope.breadcrumbItems = [
                    breadcrumbs.createViewPkg(
                        $scope.pkg,
                        $routeParams.version,
                        $routeParams.architectureCode),
                    {
                        title : 'Edit Categories',
                        path : $location.path()
                    }
                ];
            }

            // only a certain number of categories can be chosen for a package.  This function will mark the categories
            // as being UI-disabled in order that it is not possible for the user to choose too many categories.

            function updateDisablementOnPkgCategories() {
                var full = _.filter($scope.pkgCategories, function(c) { return c.selected; }).length >= CATEGORIES_LIMIT;

                _.each($scope.pkgCategories, function(c) {
                    c.disabled = full && !c.selected;
                });
            }

            // this gets fired when somebody selects or deselects a category.

            $scope.didChangePkgCategorySelection = function() {
                updateDisablementOnPkgCategories();
            };

            // stores the categories back to the server for this package.  When it has done this, it will return to
            // view the pkg again.

            $scope.goStorePkgCategories = function() {
                jsonRpc.call(
                    constants.ENDPOINT_API_V1_PKG,
                    'updatePkgCategories',
                    [{
                        pkgName : $scope.pkg.name,
                        pkgCategoryCodes : _.map(
                            _.filter(
                                $scope.pkgCategories,
                                function(c) { return c.selected; }
                            ),
                            function(c) {
                                return c.code;
                            }
                        )
                    }]
                ).then(
                    function() {
                        $log.info('have updated the pkg categories for pkg '+$scope.pkg.name);
                        $location.path('/pkg/'+$routeParams.name+'/'+$routeParams.version+'/'+$routeParams.architectureCode).search({});
                    },
                    function(err) {
                        $log.error('unable to update pkg categories');
                        errorHandling.handleJsonRpcError(err);
                    }
                );
            }

        }
    ]
);