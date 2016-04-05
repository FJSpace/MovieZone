'use strict';

/* Services */

var omdbCatalogueService = angular.module('OMDBCatalogueService', []);
var movieZoneService = angular.module('MovieZoneService', []);

// Representing the remote RESTful movie catalogue
omdbCatalogueService.factory('OMDBCatalogueProxy', ['$http',
    function($http) {
        var url = 'http://www.omdbapi.com/?';

        function findById(id) {
            return $http({
                url: url,
                method: 'GET',
                params: { i: id, type: 'movie', r: 'json', plot: 'full' }
            });
        };
        
        function findByName(name) {
            return $http({
                url: url,
                method: 'GET',
                params: { s: name, type: 'movie', r: 'json' }
            });
        }
        
        return {
          findById: findById,
          findByName: findByName
        };
        
    }]);

movieZoneService.factory('MovieZoneProxy', ['$http',
    function($http) {
        var url = 'http://localhost:8080/MovieZone/webresources';

        function register(username, password, emailAddress, firstName, lastName) {
            return $http({
                url: url + '/users/newuser',
                method: 'POST',
                data: { username: username,
                        password: password,
                        email: emailAddress,
                        fname: firstName,
                        lname: lastName }
            });
        }
        
        function login(username, password) {
            return $http({
                url: url + '/users/login',
                method: 'POST',
                data: { username: username,
                        password: password }
            });
        }
        
        function editUser(token, user) {
            return $http({
               url: url + '/users/me',
               method: 'PUT',
               headers: {'authorization': token},
               data:{
                   user:{
                        username: user.username,
                        firstName: user.firstName,
                        lastName: user.lastName,
                        emailAddress: user.emailAddress
                   }
               }
            });
        }
        
        function getMyProfile(token) {
            return $http({
                url: url + '/users/me',
                method: 'GET',
                headers: {'authorization': token}
            });
        }
        
        function getProfile(userId) {
            return $http({
                url: url + '/users/' + userId,
                method: 'GET'
            });
        }
        
        function getReviewsByMovie(movieId) {
            return $http({
                url: url + '/reviews/movie/' + movieId,
                method: 'GET'
            });
        }
        
        function getReviewsByUser(username) {
            return $http({
                url: url + '/reviews/user/' + username,
                method: 'GET'
            });
        }
        
        function getMyReviews(token){
            return $http({
                url: url
            })
        }
        
        function postReview(token, review, movieId) {
            return $http({
                url: url + '/reviews/' + movieId,
                method: 'POST',
                headers: { authorization: token },
                data: { review: review }
            });
        }
        
        function editReview(token, review, movieId) {
            return $http({
                url: url + '/reviews/' + movieId,
                method: 'PUT',
                headers: { authorization: token },
                data: { review: review }
            });
        }
        
        function deleteReview(token, movieId) {
            return $http({
                url: url + '/reviews/' + movieId,
                headers: { authorization: token },
                method: 'DELETE'
            });
        }
        
        function getMyReview(token, movieId) {
            return $http({
                url: url + '/reviews/me/' + movieId,
                headers: { authorization: token },
                method: 'GET'
            });
        }
        
        function getLatestReviews(nbrOfReviews) {
            return $http({
                url: url + '/reviews/latest/' + nbrOfReviews,
                method: 'GET'
            });
        }
        
        function getLatestByEnjoyer(nbrOfReviews) {
            return $http({
                url: url + '/reviews/latest/user/' + nbrOfReviews,
                method: 'GET',
                headers: { authorization: token }
            });
        }
        
        function postRating(token, movieId, rating) {
            return $http({
                url: url + '/ratings/movie/' + movieId,
                method: 'POST',
                headers: { authorization: token },
                data: { rating: rating }
            });
        }
        
        function deleteRating(token, movieId) {
            return $http({
                url: url + '/ratings/movie/' + movieId,
                method: 'DELETE',
                headers: { authorization: token }
            });
        }
        
        function getAverageRating(movieId) {
            return $http({
                url: url + '/ratings/' + movieId,
                method: 'GET',
                params: { field: 'averageRating' }
            });
        }
        
        function getMyRating(token, movieId) {
            return $http({
                url: url + '/ratings/movie/' + movieId,
                method: 'GET',
                headers: { authorization: token }
            });
        }        
        
        function getRatingsByUser(userId) {
            return $http({
                url: url + '/ratings/user/' + userId,
                method: 'GET'
            });
        }
        
        function getTopRatedMovies(nbrOfMovies) {
            return $http({
                url: url + '/ratings/top',
                method: 'GET',
                params: { nbrofmovies: nbrOfMovies }
            });
        }
        
        return {
            register: register,
            login: login,
            editUser: editUser,
            getProfile: getProfile,
            getMyProfile: getMyProfile,
            getReviewsByMovie: getReviewsByMovie,
            getReviewsByUser: getReviewsByUser,
            postReview: postReview,
            editReview: editReview,
            deleteReview: deleteReview,
            getMyReview: getMyReview,
            getLatestReviews: getLatestReviews,
            postRating: postRating,
            deleteRating: deleteRating,
            getAverageRating: getAverageRating,
            getMyRating: getMyRating,
            getRatingsByUser: getRatingsByUser,
            getTopRatedMovies: getTopRatedMovies
        };
       
    }]);