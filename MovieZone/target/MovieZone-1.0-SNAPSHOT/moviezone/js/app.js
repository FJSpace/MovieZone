/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var movieZone = angular.module('MovieZone', [
    'ngRoute',
    'OMDBCatalogueService',
    'MovieZoneService',
    'MovieZoneControllers'
]);

movieZone.config(['$routeProvider',
    function ($routeProvider) {
        console.log('Routeprovider accessed.');// Injected object $routeProvider
        $routeProvider.
                when('/', {
                    templateUrl: 'moviezone/partials/frontpage.html',
                    controller: 'PhotoGridCtrl'
                }).
                when('/movies/:id', {
                    templateUrl: 'moviezone/partials/movie-detail.html',
                    controller: 'MovieCtrl'
                }).
                when('/reviews/:id/edit', {
                    templateUrl: 'moviezone/partials/reviews/edit.html',
                    controller: 'ReviewCtrl'
                }).
                when('/reviews/:id/new', {
                    templateUrl: 'moviezone/partials/reviews/new.html',
                    controller: 'ReviewCtrl'
                }).
                when('/searchResults', {
                    templateUrl: 'moviezone/partials/search-results.html',
                    controller: 'SearchCtrl'
                }).
                when('/users/me', {
                    templateUrl: 'moviezone/partials/my-page.html',
                    controller: 'PageCtrl'
                });
    }]);
