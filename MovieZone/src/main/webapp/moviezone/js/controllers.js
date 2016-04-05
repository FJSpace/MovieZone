/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var movieZoneControllers = 
        angular.module('MovieZoneControllers', ['ui.bootstrap', 'ngCookies']);

movieZoneControllers.controller('PhotoGridCtrl', 
['$scope', 'OMDBCatalogueProxy', 'MovieZoneProxy', 
    function ($scope, omdb, movieZone) {
        var activeId = 0;

        movieZone.getTopRatedMovies()
                .success(function (movies) {

                    var movieArray = [];
                    movies.forEach(function (mov) {
                        omdb.findById(mov.id)
                                .success(function (omdbMovie) {
                                    movieArray.push({id: omdbMovie.imdbID,
                                        title: omdbMovie.Title,
                                        year: omdbMovie.Year,
                                        poster: omdbMovie.Poster});
                                    $scope.movieInfo = movieArray;
                                });
                    });

                    $scope.isActiveId = function (id) {
                        return activeId === id;
                    };

                    $scope.setActiveId = function (id) {
                        console.log('Setting active id to ' + id);
                        activeId = id;
                    };

                    $scope.resetActiveId = function () {
                        console.log('Resetting active id');
                        activeId = 0;
                    };
                });

    }]);

movieZoneControllers.controller('MovieDetailCtrl', 
['$scope', '$location', 'OMDBCatalogueProxy', 'MovieZoneProxy', 
    function ($scope, $location, omdb, movieZone) {
        var movieId = getIdFromLocation($location.path());

        omdb.findById(movieId)
                .success(function (omdbMovie) {
                    $scope.omdbMovie = omdbMovie;
                });
    }]);

movieZoneControllers.controller('MovieReviewCtrl', ['$scope', '$location',
    'OMDBCatalogueProxy', 'MovieZoneProxy', '$cookies',
    function ($scope, $location, omdb, movieZone, $cookies) {
        console.log('ENTERED MRC');
        $scope.sessionToken = $cookies.get('MovieZoneSessionToken');
        var movieId = getIdFromLocation($location.path());
        if (!$scope.sessionToken) {
            getAllReviews();
        } else {
            movieZone.getMyReview($scope.sessionToken, movieId)
                    .then(function (res) {
                        var review = res.data;
                        $scope.myReview = review;
                        getAllReviews(review.userName);
                    },
                            function (err) {
                                $scope.myReview = false;
                                getAllReviews();
                            });
                            
            movieZone.getMyRating($scope.sessionToken, movieId)
                .success(function (rating) {
                    $scope.myRating = rating;
                });
        }

        function getAllReviews(myUser) {
            movieZone.getReviewsByMovie(movieId)
                    .success(function (reviews) {
                        if (!reviews) {
                            return;
                        }
                        if (!myUser) {
                            $scope.reviews = reviews;
                        } else {
                            var otherReviews = reviews.filter(function (rev) {
                                return !(rev.userName === myUser);
                            });
                            $scope.reviews = otherReviews;
                        }
                    });
        };
        
        $scope.edit = function() {
            $scope.editing = true;
            $scope.reviewFieldText = $scope.myReview.reviewText;
        };
        
        $scope.delete = function() {
            movieZone.deleteReview($scope.sessionToken, movieId)
                    .success(function() {
                        $scope.myReview = null;
                        $scope.reviewTextField = false;
                        $scope.editing = false;
                    });
        };
        
        $scope.save = function(reviewText) {
            movieZone.postReview($scope.sessionToken, reviewText, movieId)
                    .success(function(myNewReview) {
                        $scope.myReview = myNewReview;
                        delete $scope.editing;
            });
        };
        
        movieZone.getReviewsByMovie(movieId)
                .success(function (reviews) {
                    $scope.reviews = reviews;
                });
    }]);

movieZoneControllers.controller('MovieCtrl', ['$scope', 'OMDBCatalogueProxy',
    'MovieZoneProxy', function ($scope, omdb, movieZone) {

    }]);

movieZoneControllers.controller('ReviewCtrl', ['$scope', 'OMDBCatalogueProxy',
    'MovieZoneProxy', function ($scope, omdb, movieZone) {

    }]);

movieZoneControllers.controller('LoginModalCtrl',['$scope', '$uibModal', 'MovieZoneProxy', '$cookies', '$window', '$location',
function ($scope, $uibModal, moviezone, $cookies, $window, $location) {
        
  $scope.animationsEnabled = true;
  $scope.sessionToken = $cookies.get('MovieZoneSessionToken');

  $scope.open = function () {

    var modalInstance = $uibModal.open({
      animation: $scope.animationsEnabled,
      templateUrl: 'moviezone/partials/login-modal.html',
      controller: 'LoginModalInstanceCtrl'
      });
  };

  $scope.toggleAnimation = function () {
    $scope.animationsEnabled = !$scope.animationsEnabled;
  };
  
  $scope.logout = function(){
      console.log("Logging out");
      $cookies.remove('MovieZoneSessionToken');
      $scope.sessionToken = null;
      if($location.path().indexOf('users') !== -1){
            $location.path('/');
      }
  };

}]);


movieZoneControllers.controller('LoginModalInstanceCtrl',['$scope', '$uibModalInstance', 'MovieZoneProxy','$cookies','$window', 
function ($scope, $uibModalInstance, moviezone, $cookies, $window) {
    $scope.loginFailed=false;
    $scope.emailFailed=false;
    $scope.registerFailed=false;
    $scope.login = function () {
        console.log("reached Login");
        moviezone.login($scope.loginData.username, $scope.loginData.password)
                .then(function(res){
                    var now = new $window.Date();
                    var exp = new $window.Date(now.getFullYear(), now.getMonth()+6, now.getDate());
                    $cookies.putObject('MovieZoneSessionToken', res.data, {
                        expires: exp
                    });
                    $uibModalInstance.close();
                    $window.location.reload();
                 
                }, 
                function(error){
                    console.log(error);
                    $scope.loginFailed=true;
                });
  };
  
  
  $scope.register = function(){
      console.log("Reaching register");
      console.log("All data: " + $scope.registerData.uName +" " + $scope.registerData.password +" " + $scope.registerData.email+" "+
        $scope.registerData.fName +" "+ $scope.registerData.lName);
      moviezone.register($scope.registerData.uName, $scope.registerData.password, $scope.registerData.email, 
        $scope.registerData.fName, $scope.registerData.lName)
                .then(function(res){
                    console.log(res);
                    $cookies.putObject('MovieZoneSessionToken', res.data);
                    $uibModalInstance.close();
                    $window.location.reload();
                },
                function(error){
                    if(error.status === 406)
                        $scope.emailFailed=true;
                    else
                        $scope.registerFailed=true;
                });
  };
  $scope.cancel = function () {
    $uibModalInstance.close();
  };
}]);

movieZoneControllers.controller('SearchCtrl', ['$rootScope', '$scope', 'OMDBCatalogueProxy','$location','$cookies', 
function($rootScope, $scope, omdb, $location, $cookies){
    $rootScope.redirect = function(){
        $location.path('/searchResults');
    };
    
    $rootScope.searchData = {
    };
    
    $rootScope.clear=function(){
        $rootScope.searchData.searchString="";
    };
    $rootScope.search = function(){
      $scope.searchData.lastSearch=$rootScope.searchData.searchString;
      omdb.findByName($rootScope.searchData.searchString)
              .success(function(movies){
                  $rootScope.searchData.results = movies.Search;
                  $rootScope.searchData.response = movies.Response;
                  console.log("TOKEN WAS " + $cookies.get('MovieZoneSessionToken'));
      }); 
  };
}]);

movieZoneControllers.controller('PageCtrl', ['$scope','$rootScope','MovieZoneProxy', '$cookies', '$window', '$location',
function($scope, $rootScope, moviezone, $cookies, $window, $location){
    $scope.successfulEdit = true;
    $scope.showEdit = false;
    $scope.setOldUserData = function(){
        $scope.oldUserData={
            username: $scope.user.userName,
            firstName: $scope.user.fname,
            lastName: $scope.user.lname,
            emailAddress: $scope.user.email
        };
    };
     $scope.setNewUserData = function(){
        $scope.newUserData={
            username: $scope.user.userName,
            firstName: $scope.user.fname,
            lastName: $scope.user.lname,
            emailAddress: $scope.user.email
        };
    };
    if(!$cookies.get('MovieZoneSessionToken')){
         $location.path('/');
    }
    moviezone.getMyProfile($cookies.get('MovieZoneSessionToken'))
        .then(function(res){
            $rootScope.user = res.data.user;
            $rootScope.userReviews = res.data.reviews;
            $scope.setOldUserData();
        }, 
        function(error){
            $scope.failedToLoad = true;
        });
    $scope.cancel = function(){
        $scope.showEdit = false;
        $scope.successfulEdit = true;
    };
    
    $scope.setEdit = function(){
        $scope.showEdit = true;
        $scope.setOldUserData();
    };
    
    $scope.editUser = function(){
        $scope.setNewUserData();
        console.log($scope.newUserData);
        moviezone.editUser($cookies.get('MovieZoneSessionToken'), $scope.newUserData)
                .then(
                function(res){
                    $scope.showEdit = false;
                    $scope.successfulEdit = true;
                    //$window.location.reload();
                },
                function(error){
                    console.log(error);
                    $scope.successfulEdit = false;
                });
    };
    
    
}]);


function getIdFromLocation(locationString) {
    var matchId = /^\/movies\/(tt[\w]+)/;
    var movieId = locationString.match(matchId)[1];
    return movieId;
};


movieZoneControllers.controller('RatingCtrl', function ($scope) {
  $scope.max = 5;
  $scope.isReadonly = true;

  $scope.hoveringOver = function(value) {
    $scope.overStar = value;
    $scope.percent = 100 * (value / $scope.max);
  };
});

movieZoneControllers.controller('AverageRatingCtrl', 
['$scope', '$location', 'OMDBCatalogueProxy', 'MovieZoneProxy', 
    function($scope, $location, omdb, movieZone) {
        var movieId = getIdFromLocation($location.path());
        movieZone.getAverageRating(movieId)
                .success(function(rating) {
                    if (rating && rating !== '0') {
                        $scope.avgRating = rating;
                    } else {
                        getOmdbRating();
                    }
        });

        function getOmdbRating() {
            omdb.findById(movieId)
                    .success(function(omdbMovie) {
                        console.log(omdbMovie.imdbRating);
                var imdbRating = Number(omdbMovie.imdbRating)/2.0;
                if (isNaN(imdbRating)) {
                    $scope.avgRating = 0;
                } else {
                    $scope.avgRating = imdbRating;
                }
            });
        }
    }]);

movieZoneControllers.controller('LatestReviewsCtrl', 
['$scope', 'OMDBCatalogueProxy', 'MovieZoneProxy', 
    function($scope, omdb, movieZone) {
        $scope.latestReviews = [];
        
        movieZone.getLatestReviews(10)
                .success(function(reviews) {

            reviews.forEach(function(review) {
                omdb.findById(review.movieId)
                        .success(function(omdbMovie) {
                            populateReview(review, omdbMovie);
                });
            });
        });
        
        function populateReview(review, omdbMovie) {
            $scope.latestReviews.push({ review: review, movie: omdbMovie });
        }
        
    }]);

movieZoneControllers.controller('LatestUserReviewsCtrl', 
['$scope', 'OMDBCatalogueProxy', 'MovieZoneProxy', 
    function($scope, omdb, movieZone) {
        $scope.latestUserReviews = [];
        
        movieZone.getLatestByEnjoyer($cookies.get('MovieZoneSessionToken'),10)
                .success(function(reviews) {

            reviews.forEach(function(review) {
                omdb.findById(review.movieId)
                        .success(function(omdbMovie) {
                            populateUserReview(review, omdbMovie);
                });
            });
        });
        
        function populateUserReview(review, omdbMovie) {
            $scope.latestUserReviews.push({ review: review, movie: omdbMovie });
        }
    }]);